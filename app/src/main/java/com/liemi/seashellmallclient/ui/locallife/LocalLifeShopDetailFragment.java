package com.liemi.seashellmallclient.ui.locallife;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeShopEntity;
import com.liemi.seashellmallclient.data.entity.order.FillOrderEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.*;
import com.liemi.seashellmallclient.ui.good.order.PayResultActivity;
import com.liemi.seashellmallclient.ui.mine.verification.VerificationOrderPayOnlineActivity;
import com.liemi.seashellmallclient.ui.video.VideoPlayer2Activity;
import com.liemi.seashellmallclient.ui.video.VideoPlayerActivity;
import com.liemi.seashellmallclient.utils.MapUtils;
import com.liemi.seashellmallclient.widget.GoodsBannerViewHolder;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.baselibrary.widget.RoundImageView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.liemi.seashellmallclient.R;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static cn.jzvd.Jzvd.CURRENT_STATE_PLAYING;
import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;

public class LocalLifeShopDetailFragment extends BaseShopDetailedFragment<LocallifeFragmentShopDetailBinding> {

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    private View bannerView;

    BaseRViewAdapter<LocalLifeGoodsEntity, BaseViewHolder> goodsAdapter;
    private Map<String, Integer> mapTypeList;
    private List<String> mapTypeStringList;
    //创建订单
    private FillOrderEntity orderCommand = new FillOrderEntity();
    private OrderPayEntity payEntity;

    @Override
    protected int getContentView() {
        return R.layout.locallife_fragment_shop_detail;
    }


    @Override
    protected MyXRecyclerView getXrvData() {
        return mBinding.xrvShop;
    }

    @Override
    protected CheckBox getCbCollect() {
        return null;
    }

    @Override
    protected void initUI() {
        super.initUI();
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.locallife_item_goods_detail_banner;
                } else if (viewType == 2) {
                    return R.layout.locallife_item_shop_detail_info;
                } else if (viewType == 3) {
                    return R.layout.locallife_item_shop_detail_goods;
                }else if (viewType == 4){
                    return R.layout.locallife_item_shop_detail_comment;
                }
                return R.layout.sharemall_layout_empty;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof BannerEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof LocalLifeShopEntity) {
                    return 2;
                } else if (adapter.getItem(position) instanceof LocalLifeGoodsEntity) {
                    return 3;
                }else if (adapter.getItem(position) instanceof CommentEntity) {
                    return 4;
                }
                return 0;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {
                    @Override
                    public void bindData(final BaseEntity item) {
                        super.bindData(item);
                        if (getBinding() instanceof LocallifeItemGoodsDetailBannerBinding) {
                            final LocallifeItemGoodsDetailBannerBinding bannerBinding = (LocallifeItemGoodsDetailBannerBinding) getBinding();
                            List<String> bannerList = new ArrayList<>();
                            boolean hasVideo = !TextUtils.isEmpty(shopDetailEntity.getShort_video_url());
                            if (hasVideo) {
                                bannerList.add(shopDetailEntity.getShort_video_url());
                            }
                            List<String> itemImageList = new ArrayList<>();
                            itemImageList.add(shopDetailEntity.getImg_url());
                            shopDetailEntity.setItemImgs(itemImageList);
                            bannerList.addAll(shopDetailEntity.getItemImgs());

                            //轮播间隔
                            bannerBinding.cbHome.setDelayedTime(5000);
                            //滑动速度
                            bannerBinding.cbHome.setDuration(1500);

                            bannerBinding.cbHome.setCanLoop(!hasVideo);
                            //指示器隐藏
                            bannerBinding.cbHome.setIndicatorVisible(false);

                            bannerBinding.cbHome.setBannerPageClickListener((View view, int i) -> JumpUtil.overlayImagePreview(getActivity(), shopDetailEntity.getItemImgs(), i - (hasVideo ? 1 : 0)));

                            // 设置数据
                            bannerBinding.cbHome.setPages(bannerList,
                                    () -> new LocalLifeShopBannerViewHolder(bannerBinding.cbHome, (View view) -> bannerView = view)
                                            .setDefaultImage(Strings.isEmpty(shopDetailEntity.getItemImgs()) ? null : shopDetailEntity.getItemImgs().get(0)));

                            bannerBinding.cbHome.start();

                        } else if (getBinding() instanceof LocallifeItemShopDetailInfoBinding) {
                            LocallifeItemShopDetailInfoBinding detailInfoBinding = (LocallifeItemShopDetailInfoBinding) getBinding();
                            if (TextUtils.equals(shopDetailEntity.getStar(),"0")){
                                detailInfoBinding.rbStarServer.setVisibility(View.GONE);
                                detailInfoBinding.tvScore.setVisibility(View.GONE);
                            }else {
                                detailInfoBinding.rbStarServer.setVisibility(View.VISIBLE);
                                detailInfoBinding.tvScore.setVisibility(View.VISIBLE);
                                float star;
                                if (TextUtils.isEmpty(shopDetailEntity.getStar())){
                                    star = 0f;
                                }else {
                                    star = Float.valueOf(shopDetailEntity.getStar());
                                }
                                    detailInfoBinding.rbStarServer.setStarShop(star, false);


                            }


                        }else if (getBinding() instanceof LocallifeItemShopDetailGoodsBinding){
                            LocallifeItemShopDetailGoodsBinding goodsBinding = (LocallifeItemShopDetailGoodsBinding) getBinding();
                            goodsBinding.rvFloor.setLayoutManager(new LinearLayoutManager(getContext()));
                            goodsBinding.rvFloor.setAdapter( goodsAdapter = new BaseRViewAdapter<LocalLifeGoodsEntity, BaseViewHolder>(getContext()) {
                                @Override
                                public int layoutResId(int position) {
                                    return R.layout.locallife_item_shop_goods_other;
                                }

                                @Override
                                public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {
                                        @Override
                                        public void bindData(Object item) {
                                            LocalLifeGoodsEntity goods = getItem(position);
                                            String startTime = goods.getStart_date().replace("-", "/");
                                            String endTime = goods.getEnd_date().replace("-", "/");
                                            getBinding().tvValidity.setText("有效期："+startTime+"-"+endTime);
                                            getBinding().tvSales.setText("销量 "+goods.getDeal());
                                            super.bindData(item);
                                            getBinding().tvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                                        }

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            /*if (view.getId()==R.id.tv_buy_now){
                                                //提交订单
                                                orderCommand.setAmount(getItem(position).getPrice());
                                                orderCommand.setOrder_type("11");
                                                List<FillOrderEntity.SectionsBean> sectionsBeans = new ArrayList<>();
                                                //商品列表
                                                List<FillOrderEntity.Good> list = new ArrayList<>();
                                                //店铺信息
                                                FillOrderEntity.SectionsBean sectionsBean = new FillOrderEntity.SectionsBean();
                                                FillOrderEntity.Good good = new FillOrderEntity.Good();
                                                good.setItem_id(getItem(position).getItem_id());
                                                good.setNum(1);
                                                list.add(good);
                                                sectionsBean.setItem_data(list);
                                                sectionsBeans.add(sectionsBean);

                                                orderCommand.setSections(sectionsBeans);
                                                doOrderCreate(orderCommand);
                                            }else {*/
                                                //查看商品详情
                                                JumpUtil.overlay(getActivity(), LocalLifeGoodsDetailActivity.class, ParamConstant.GOOD_ID, getItem(position).getItem_id());
//                                            }
                                        }

                                        @Override
                                        public LocallifeItemShopGoodsOtherBinding getBinding() {
                                            return (LocallifeItemShopGoodsOtherBinding) super.getBinding();
                                        }
                                    };
                                }
                            });
                            goodsAdapter.setData(shopDetailEntity.getItem());

                        } else if (getBinding() instanceof LocallifeItemShopDetailCommentBinding) {
                            LocallifeItemShopDetailCommentBinding binding = (LocallifeItemShopDetailCommentBinding) getBinding();
                            float star;
                            if (TextUtils.isEmpty(((CommentEntity) getItem(position)).getLevel())){
                                star = 0f;
                            }else {
                                star = Float.valueOf(((CommentEntity) getItem(position)).getLevel());
                            }
                            binding.rbStarServer.setStar(star, false);

                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_comment) {    //  评论
                            Bundle bundle = new Bundle();
                            bundle.putString(ITEM_ID,shopDetailEntity.getId());
                            bundle.putString("title",shopDetailEntity.getName());
                            JumpUtil.overlay(getContext(), LocalLifeShopCommentAllActivity.class,bundle);
                        } else if (i == R.id.tv_all) {  //查看全部商品
                            JumpUtil.overlay(getContext(), LocalLifeGoodsAllActivity.class, SHOP_ID, shopDetailEntity.getId());
                        } else if (i == R.id.iv_1 || i == R.id.iv_2 || i == R.id.iv_3) {
                            if (shopDetailEntity.getComment() != null && !Strings.isEmpty(shopDetailEntity.getComment().getMeCommetImgs())) {
                                JumpUtil.overlayImagePreview(getActivity(), shopDetailEntity.getComment().getMeCommetImgs(), i == R.id.iv_1 ? 0 : i == R.id.iv_2 ? 1 : 2);
                            }
                        }else if (i == R.id.tv_call){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopDetailEntity.getShop_remind_tel()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else if (i==R.id.tv_location_navigate){
                            if (TextUtils.isEmpty(shopDetailEntity.getLatitude())|| TextUtils.isEmpty(shopDetailEntity.getLongitude())){
                                ToastUtils.showShort("暂无当前店铺位置信息！");
                            }else {
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
                        }else if (i==R.id.iv_business_qualification){
                            List<String> list = new ArrayList<>();
                            if (TextUtils.isEmpty(shopDetailEntity.getLicense_url())){
                                ToastUtils.showShort("暂无营业资质图片！");
                            }else {
                                list.add(shopDetailEntity.getLicense_url());
                                JumpUtil.overlayImagePreview(getActivity(), list, i - 0);
                            }
                        }else if (view.getId()==R.id.iv_imageView){
                            JumpUtil.overlay(getContext(), VideoPlayer2Activity.class, VideoPlayerActivity.VIDEO_URL,shopDetailEntity.getComment().getVideo_url());
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
                double[] endLocation = MapUtils.bdToGaoDe(Strings.toDouble(shopDetailEntity.getLatitude()), Strings.toDouble(shopDetailEntity.getLongitude()));
                MapUtils.openGaoDeNavi(getContext(),location[1],location[0],"我的位置",endLocation[1],endLocation[0],shopDetailEntity.getName());
                break;
            case 1://百度
                MapUtils.openBaiDuNavi(getContext(),MainActivity.latitude,MainActivity.longitude,"我的位置",Strings.toDouble(shopDetailEntity.getLatitude()), Strings.toDouble(shopDetailEntity.getLongitude()),shopDetailEntity.getName());
                break;
            case 2://腾讯
                double[] slocation = MapUtils.bdToGaoDe(MainActivity.latitude, MainActivity.longitude);
                double[] elocation = MapUtils.bdToGaoDe(Strings.toDouble(shopDetailEntity.getLatitude()), Strings.toDouble(shopDetailEntity.getLongitude()));
                MapUtils.openTencentMap(getContext(),slocation[1],slocation[0],"我的位置",elocation[1],elocation[0],shopDetailEntity.getName());
                break;
        }
    }

    @Override
    protected void initData() {
        mBinding.setDoClick(this);
        mBinding.executePendingBindings();
        xRecyclerView.refresh();
    }

    @Override
    public void showData(LocalLifeShopEntity shopDetailEntity) {
        this.shopDetailEntity = shopDetailEntity;
        mBinding.setItem(shopDetailEntity);
        mBinding.executePendingBindings();
        showResult();
    }



    private void showResult() {
        hideProgress();
        ArrayList<BaseEntity> baseEntities = new ArrayList<>();
        //Banner
        baseEntities.add(new BannerEntity(shopDetailEntity.getImg_url()));
        //店铺信息
        if (shopDetailEntity!= null && !TextUtils.isEmpty(shopDetailEntity.getId())) {
            baseEntities.add(shopDetailEntity);
        }
        //全部商品
        if (shopDetailEntity.getItem()!=null){
            baseEntities.add(new LocalLifeGoodsEntity());

        }
        //评论
        if (shopDetailEntity.getComment() != null) {
            baseEntities.add(shopDetailEntity.getComment());
        }

        adapter.setData(baseEntities);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_buy) {
            showBuyDialog(false);
        }else if (view.getId()==R.id.tv_collect){
            if (MApplication.getInstance().checkUserIsLogin()) {
                if (shopDetailEntity != null) {
                    //收藏商品
                    if (shopDetailEntity.getIs_collect() == 0) {//未收藏状态
                        doCollection();
                    }//取消收藏商品
                    else if (shopDetailEntity.getIs_collect() == 1) {//收藏状态
                        doCollectionDel();
                    }
                }
            }
        }
    }

    private void showBuyDialog(boolean addShopCart) {
        if (MApplication.getInstance().checkUserIsLogin()) {
            if (shopDetailEntity!=null){
                Bundle bundle = new Bundle();
                bundle.putString(SHOP_ID,shopDetailEntity.getId());
                bundle.putString("title",shopDetailEntity.getName());
//                bundle.putString("score",shopDetailEntity.getScore_rate());
                JumpUtil.overlay(getActivity(), LocalLifeShopPayActivity.class, bundle);
            }
        }
    }

    private void doCollection() {
        if (shopDetailEntity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_please_wait_load_success));
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollection(shopDetailEntity.getId())
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            mBinding.tvCollect.setText("已收藏");
                            shopDetailEntity.setIs_collect(1);
                            showError("收藏成功");
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doCollectionDel() {
        if (shopDetailEntity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_please_wait_load_success));
            return;
        }
        showProgress("");
        List<String> list = new ArrayList<>();
        list.add(shopDetailEntity.getId());
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollectionDel(list)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            mBinding.tvCollect.setText("收藏");
                            shopDetailEntity.setIs_collect(0);
                            showError("取消收藏");
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    class LocalLifeShopBannerViewHolder implements MZViewHolder<String> {

        private RoundImageView ivBanner;

        private JzvdStd jVideo;

        private Context mContext;

        private MZBannerView mzBannerView;

        private View parentView;

        private View.OnClickListener returnViewListener;

        private String defaultImage;

        public LocalLifeShopBannerViewHolder(MZBannerView mzBannerView, View.OnClickListener returnViewListener) {
            this.mzBannerView = mzBannerView;
            this.returnViewListener = returnViewListener;
        }

        public LocalLifeShopBannerViewHolder setDefaultImage(String defaultImage) {
            this.defaultImage = defaultImage;
            return this;
        }

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.sharemall_item_locallife_shop_banner, null);
            ivBanner = view.findViewById(R.id.iv_banner);
            jVideo = view.findViewById(R.id.videoplayer);
            mContext = context;
            parentView = view;
            return view;
        }

        @Override
        public void onBind(Context context, int position, String imgUrl) {
            if (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg")
                    || imgUrl.endsWith(".bmp")
                    || imgUrl.endsWith(".gif")
                    || imgUrl.endsWith(".png")) {
                GlideShowImageUtils.gifload(mContext, imgUrl, ivBanner);
            } else {
                jVideo.setVisibility(View.VISIBLE);
                jVideo.setUp(imgUrl, "", Jzvd.SCREEN_WINDOW_NORMAL);
                if (!TextUtils.isEmpty(defaultImage)) {
                    GlideShowImageUtils.displayNetImage(mContext, defaultImage, jVideo.thumbImageView, R.drawable.baselib_bg_default_pic);
                } else {
                    Glide.with(mContext)
                            .load(imgUrl)
                            .apply(RequestOptions.frameOf(10))
                            .into(jVideo.thumbImageView);
                }

                if (returnViewListener != null) {
                    returnViewListener.onClick(parentView);
                }

                mzBannerView.addPageChangeListener(new ViewPager.OnPageChangeListener() {

                    //实现切换页面暂停播放的功能
                    private int lastIndex;
                    private boolean isPlaying;

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (jVideo.getVisibility() == View.VISIBLE) {

                            if (lastIndex == 0) {
                                isPlaying = jVideo.currentState == CURRENT_STATE_PLAYING;
                                if (isPlaying) {
                                    jVideo.startButton.performClick();
                                }
                            } else if (position == 0) {
                                if (isPlaying) {
                                    jVideo.startButton.performClick();
                                }
                            }
                        }
                        lastIndex = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        }


    }

}
