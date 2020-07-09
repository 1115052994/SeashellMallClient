package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.GoodDetailUrlEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsDetailEntity;
import com.liemi.seashellmallclient.data.entity.order.FillOrderEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.LocallifeFragmentGoodsDetailBinding;
import com.liemi.seashellmallclient.databinding.LocallifeItemGoodsDetailTitleBinding;
import com.liemi.seashellmallclient.databinding.LocallifeItemGoodsDetailWebBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemGoodsDetailWebviewBinding;
import com.liemi.seashellmallclient.ui.good.order.PayResultActivity;
import com.liemi.seashellmallclient.ui.mine.verification.VerificationOrderPayOnlineActivity;
import com.liemi.seashellmallclient.utils.HTMLFormat;
import com.liemi.seashellmallclient.utils.MapUtils;
import com.liemi.seashellmallclient.widget.GoodsBannerViewHolder;
import com.liemi.seashellmallclient.widget.OpenImageInterfaceJS;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.liemi.seashellmallclient.R;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liemi.seashellmallclient.data.ParamConstant.GOOD_ID;
import static com.netmi.baselibrary.data.Constant.BASE_API;

public class LocalLifeGoodsDetailFragment extends BaseXRecyclerFragment<LocallifeFragmentGoodsDetailBinding, LocalLifeGoodsDetailEntity> implements CompoundButton.OnCheckedChangeListener {

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    private LocalLifeGoodsDetailEntity goodsDetailEntity;

    private View cbShop;

    private WebView goodsWebView;
    private String good_id;
    private Map<String, Integer> mapTypeList;
    private List<String> mapTypeStringList;

    //创建订单
    private FillOrderEntity orderCommand = new FillOrderEntity();
    private OrderPayEntity payEntity;

    @Override
    protected int getContentView() {
        return R.layout.locallife_fragment_goods_detail;
    }

    @Override
    protected void initUI() {
        Bundle bundle = getArguments();
        good_id = bundle.getString(GOOD_ID);
        xRecyclerView = mBinding.xrvGoods;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.locallife_item_goods_detail_title;
                } else if (viewType == 2) {
                    return R.layout.sharemall_item_goods_detail_webview;
                }
                return R.layout.locallife_item_goods_detail_web;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof LocalLifeGoodsDetailEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof GoodDetailUrlEntity) {
                    return 2;
                }
                return super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {
                    @Override
                    public void bindData(final BaseEntity item) {
                        super.bindData(item);
                        if (getBinding() instanceof LocallifeItemGoodsDetailTitleBinding) {
                            final LocallifeItemGoodsDetailTitleBinding goodsDetailBinding = (LocallifeItemGoodsDetailTitleBinding) getBinding();
                            List<String> bannerList = new ArrayList<>();
                            bannerList.addAll(goodsDetailEntity.getImg_url());
                            //轮播间隔
                            goodsDetailBinding.cbHome.setDelayedTime(5000);
                            //滑动速度
                            goodsDetailBinding.cbHome.setDuration(1500);

                            goodsDetailBinding.cbHome.setCanLoop(false);
                            //指示器隐藏
                            goodsDetailBinding.cbHome.setIndicatorVisible(false);

                            goodsDetailBinding.cbHome.setBannerPageClickListener((View view, int i) -> JumpUtil.overlayImagePreview(getActivity(), goodsDetailEntity.getImg_url(), i - 0));

                            // 设置数据
                            goodsDetailBinding.cbHome.setPages(bannerList,
                                    () -> new GoodsBannerViewHolder(goodsDetailBinding.cbHome, (View view) -> cbShop = view)
                                            .setDefaultImage(Strings.isEmpty(goodsDetailEntity.getImg_url()) ? null : goodsDetailEntity.getImg_url().get(0)));

                            goodsDetailBinding.cbHome.start();
                            String distance = goodsDetailEntity.getShop().getDistance();
                            double d = Strings.toDouble(distance);
                            if (d < 100) {
                                goodsDetailBinding.tvDistance.setText(new DecimalFormat("0.0").format(d) + "m");
                            } else {
                                String format = new DecimalFormat("0.0").format(d / 1000);
                                goodsDetailBinding.tvDistance.setText(format + "km");
                            }
                        } else if (getBinding() instanceof LocallifeItemGoodsDetailWebBinding) {
                            LocallifeItemGoodsDetailWebBinding goodsDetailWebBinding = (LocallifeItemGoodsDetailWebBinding) getBinding();
                            goodsDetailWebBinding.btDetail.setOnClickListener((View view) -> {
                                goodsDetailWebBinding.btDetail.setClickable(false);
                                goodsDetailWebBinding.btService.setClickable(true);
                                goodsDetailWebBinding.btService.setChecked(false);
                                if (goodsWebView != null) {
                                    goodsWebView.loadDataWithBaseURL(BASE_API, HTMLFormat.getNewContent(goodsDetailEntity.getRich_text()), "text/html", "UTF-8", null);
                                }
                            });
                            goodsDetailWebBinding.btService.setOnClickListener((View view) -> {
                                goodsDetailWebBinding.btService.setClickable(false);
                                goodsDetailWebBinding.btDetail.setClickable(true);
                                goodsDetailWebBinding.btDetail.setChecked(false);
                                if (goodsWebView != null) {
//                                    goodsWebView.loadDataWithBaseURL(BASE_API, HTMLFormat.getNewContent(shopDetailEntity.getBuy_rich_text()), "text/html", "UTF-8", null);
                                }
                            });
                        } else if (getBinding() instanceof SharemallItemGoodsDetailWebviewBinding) {
                            goodsWebView = ((SharemallItemGoodsDetailWebviewBinding) getBinding()).wvGood;
                            goodsWebView.getSettings().setDefaultTextEncodingName("UTF-8");
                            goodsWebView.setWebViewClient(new WebViewClient());
                            goodsWebView.setWebChromeClient(new WebChromeClient());
//                            ImageFullWebViewClient.setWebSettings(webView.getSettings());
                            goodsWebView.setHorizontalScrollBarEnabled(false);
                            goodsWebView.setVerticalScrollBarEnabled(false);
                            goodsWebView.getSettings().setDefaultFontSize(20);
                            goodsWebView.addJavascriptInterface(new OpenImageInterfaceJS(getActivity()), "App");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                goodsWebView.getSettings().setMixedContentMode(goodsWebView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
                            }
                            goodsWebView.loadDataWithBaseURL(BASE_API, HTMLFormat.getNewContent(goodsDetailEntity.getRich_text()), "text/html", "UTF-8", null);

                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_distance) {
                            if (TextUtils.isEmpty(goodsDetailEntity.getShop().getLatitude()) || TextUtils.isEmpty(goodsDetailEntity.getShop().getLongitude())) {
                                ToastUtils.showShort("暂无当前商家位置信息！");
                            } else {
                                //高德、百度、腾讯
                                mapTypeList = new HashMap<>();
                                mapTypeStringList = new ArrayList<>();
                                if (MapUtils.isGdMapInstalled()){
                                    mapTypeList.put("高德地图", 0);
                                    mapTypeStringList.add("高德地图");
                                }
                                if (MapUtils.isBaiduMapInstalled()){
                                    mapTypeList.put("百度地图", 1);
                                    mapTypeStringList.add("百度地图");
                                }
                                if (MapUtils.isTencentMapInstalled()){
                                    mapTypeList.put("腾讯地图", 2);
                                    mapTypeStringList.add("腾讯地图");
                                }
                                showMapTypeDialog(mapTypeList.size());
                            }

                        } else if (i == R.id.tv_call) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + goodsDetailEntity.getShop().getShop_remind_tel()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                };
            }
        });
    }

    private void showMapTypeDialog(int num) {
        if (num == 0) {
            ToastUtils.showShort("请安装地图软件");
        } else {
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), (int options1, int option2, int options3, View v) ->
                    doSeletMap(mapTypeList.get(mapTypeStringList.get(options1)))).build();
            pvOptions.setPicker(mapTypeStringList);
            pvOptions.setSelectOptions(0);
            pvOptions.show();
        }
    }

    private void doSeletMap(Integer type) {
        switch (type){
            case 0://高德
                double[] location = MapUtils.bdToGaoDe(MainActivity.latitude, MainActivity.longitude);
                double[] endLocation = MapUtils.bdToGaoDe(Strings.toDouble(goodsDetailEntity.getShop().getLatitude()), Strings.toDouble(goodsDetailEntity.getShop().getLongitude()));
                MapUtils.openGaoDeNavi(getContext(),location[1],location[0],"我的位置",endLocation[1],endLocation[0],goodsDetailEntity.getShop().getName());
                break;
            case 1://百度
                MapUtils.openBaiDuNavi(getContext(),MainActivity.latitude,MainActivity.longitude,"我的位置",Strings.toDouble(goodsDetailEntity.getShop().getLatitude()), Strings.toDouble(goodsDetailEntity.getShop().getLongitude()),goodsDetailEntity.getShop().getName());
                break;
            case 2://腾讯
                double[] slocation = MapUtils.bdToGaoDe(MainActivity.latitude, MainActivity.longitude);
                double[] elocation = MapUtils.bdToGaoDe(Strings.toDouble(goodsDetailEntity.getShop().getLatitude()), Strings.toDouble(goodsDetailEntity.getShop().getLongitude()));
                MapUtils.openTencentMap(getContext(),slocation[1],slocation[0],"我的位置",elocation[1],elocation[0],goodsDetailEntity.getShop().getName());
                break;
        }
    }

    @Override
    protected void initData() {
        mBinding.setDoClick(this::onClick);
        mBinding.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        xRecyclerView.refresh();
    }

    public void showData(LocalLifeGoodsDetailEntity goodsDetailEntity) {
        this.goodsDetailEntity = goodsDetailEntity;
        mBinding.setItem(goodsDetailEntity);
        mBinding.executePendingBindings();
        showResult();
    }

    private void showResult() {
        hideProgress();
        ArrayList<BaseEntity> baseEntities = new ArrayList<>();
        //详情
        baseEntities.add(goodsDetailEntity);

        //富文本
        baseEntities.add(new GoodDetailUrlEntity(goodsDetailEntity.getRich_text(), "", ""));
        adapter.setData(baseEntities);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_buy) {
            //提交订单
//            JumpUtil.overlay(getActivity(), LocalLifeSubmitOrderActivity.class, GOOD_ID, goodsDetailEntity.getItem_id());

            orderCommand.setAmount(goodsDetailEntity.getPrice());
            orderCommand.setOrder_type("11");
            List<FillOrderEntity.SectionsBean> sectionsBeans = new ArrayList<>();
            //商品列表
            List<FillOrderEntity.Good> list = new ArrayList<>();
            //店铺信息
            FillOrderEntity.SectionsBean sectionsBean = new FillOrderEntity.SectionsBean();
            FillOrderEntity.Good good = new FillOrderEntity.Good();
            good.setItem_id(goodsDetailEntity.getItem_id());
            good.setNum(1);
            list.add(good);
            sectionsBean.setItem_data(list);
            sectionsBeans.add(sectionsBean);

            orderCommand.setSections(sectionsBeans);
            doOrderCreate(orderCommand);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getGoodsDetails(good_id, MainActivity.getLongitude(), MainActivity.getLatitude())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<LocalLifeGoodsDetailEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<LocalLifeGoodsDetailEntity> data) {
                        if (dataExist(data)) {
                            showData(data.getData());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void doOrderCreate(final FillOrderEntity orderCommand) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .createOrder(orderCommand)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        EventBus.getDefault().post(new ShopCartEvent());
                        if (dataExist(data)) {
                            payEntity = data.getData();
                            if (!TextUtils.isEmpty(data.getData().getPay_order_no())) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("type",1);
                                bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                                bundle.putString("shop_id",goodsDetailEntity.getShop_id());
                                JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                            }
                        }
                    }
                });
    }
}
