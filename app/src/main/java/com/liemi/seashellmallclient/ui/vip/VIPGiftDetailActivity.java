package com.liemi.seashellmallclient.ui.vip;

import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.good.GoodDetailUrlEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.liemi.seashellmallclient.data.event.ShareEvent;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityVipgiftDetailBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemGoodDetailWebviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemVipGiftDetailBannerBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemVipGiftDetailInfoBinding;
import com.liemi.seashellmallclient.ui.good.order.FillOrderActivity;
import com.liemi.seashellmallclient.utils.HTMLFormat;
import com.liemi.seashellmallclient.widget.BannerViewHolder;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.annotations.NonNull;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.netmi.baselibrary.data.Constant.BASE_HTML;

public class VIPGiftDetailActivity extends BaseXRecyclerActivity<ActivityVipgiftDetailBinding, BaseEntity> {
    private GoodsDetailedEntity goodEntity;
    private List<BaseEntity> baseEntities;
    private String ivid;
    private String price;

    @Override
    protected int getContentView() {
        return R.layout.activity_vipgift_detail;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_vip_gift));

        if (getIntent().hasExtra(VipParam.hideBottom)) {
            mBinding.llBottom.setVisibility(View.GONE);
            mBinding.tvShareNow.setVisibility(View.VISIBLE);
        }

        xRecyclerView = mBinding.rvContent;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.sharemall_item_vip_gift_detail_banner;
                } else if (viewType == 2) {
                    return R.layout.sharemall_item_vip_gift_detail_info;
                } else if (viewType == 4) {
                    return R.layout.sharemall_item_good_detail_webview;
                }
                return R.layout.sharemall_item_good_detail_web;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof PageEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof GoodsDetailedEntity) {
                    return 2;
                } else if (adapter.getItem(position) instanceof GoodDetailUrlEntity) {
                    return 4;
                }
                return 3;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (getBinding() instanceof SharemallItemVipGiftDetailBannerBinding) {
                            final SharemallItemVipGiftDetailBannerBinding bannerBinding = (SharemallItemVipGiftDetailBannerBinding) getBinding();

                            PageEntity<BannerEntity> pageEntity = (PageEntity<BannerEntity>) adapter.getItem(position);
                            final List<String> showImgs = new ArrayList<>();
                            for (BannerEntity entity : pageEntity.getList()) {
                                showImgs.add(entity.getImg_url());
                            }
                            if (!showImgs.isEmpty()) {

                                //轮播间隔
                                bannerBinding.cbBanner.setDelayedTime(5000);
                                //滑动速度
                                bannerBinding.cbBanner.setDuration(1800);
                                //指示器隐藏
                                bannerBinding.cbBanner.setIndicatorVisible(false);
                                // 可在project下查看lambda记录.txt
                                bannerBinding.cbBanner.setBannerPageClickListener((View view, int i) -> JumpUtil.overlayImagePreview(getActivity(), showImgs, i));
                                // 设置数据
                                bannerBinding.cbBanner.setPages(showImgs, BannerViewHolder::new);

                                bannerBinding.cbBanner.start();
                            }

                        } else if (getBinding() instanceof SharemallItemVipGiftDetailInfoBinding) {
                            final SharemallItemVipGiftDetailInfoBinding binding = (SharemallItemVipGiftDetailInfoBinding) getBinding();
                            if (Strings.toFloat(goodEntity.getPostage()) > 0) {
                                binding.tvPostage.setText(getString(R.string.sharemall_order_carriage2) + goodEntity.getPostage());
                            } else {
                                binding.tvPostage.setText(getString(R.string.sharemall_free_shipping));
                            }
                        } else if (getBinding() instanceof SharemallItemGoodDetailWebviewBinding) {
                            WebView webView = ((SharemallItemGoodDetailWebviewBinding) getBinding()).wvGood;
                            webView.setWebViewClient(new WebViewClient());
                            webView.setWebChromeClient(new WebChromeClient());
                            webView.setHorizontalScrollBarEnabled(false);
                            webView.setVerticalScrollBarEnabled(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                webView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
                            }
                            webView.loadDataWithBaseURL(BASE_HTML, HTMLFormat.getNewData(((GoodDetailUrlEntity) item).getRich_text()), "text/html", "UTF-8", null);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        baseEntities = new ArrayList<>();
        doListSpecsGroup();
        mBinding.rvContent.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_more_gift) {
            if (!AppManager.getInstance().existActivity(VipUpgradeActivity.class)) {
                JumpUtil.overlay(getContext(), VipUpgradeActivity.class);
            }
            finish();
        } else if (i == R.id.tv_buy_now) {
            if (Strings.isEmpty(ivid)) {
                ToastUtils.showShort(getString(R.string.sharemall_lack_good_parameters));
                return;
            }

            ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();
            ShopCartEntity addShopCart = new ShopCartEntity();
            addShopCart.setShop(goodEntity.getShop());
            List<GoodsDetailedEntity> list = new ArrayList<>();
            goodEntity.setNum("1");
            goodEntity.setIvid(ivid);
            goodEntity.setPrice(price);
            list.add(goodEntity);
            addShopCart.setList(list);
            shopCartEntities.add(addShopCart);
            Bundle bundle = new Bundle();
            bundle.putSerializable(GoodsParam.SHOP_CARTS, shopCartEntities);
            JumpUtil.overlay(getContext(), FillOrderActivity.class, bundle);

        } else if (i == R.id.tv_share_now) {
            finish();
            EventBus.getDefault().post(new ShareEvent());

        }
    }

    public void showData(GoodsDetailedEntity goodEntity) {
        this.goodEntity = goodEntity;

        mBinding.tvShareNow.setEnabled(Strings.toInt(goodEntity.getStock()) > 0);

        PageEntity<BannerEntity> pageEntity = new PageEntity<>();
        for (String imgsBean : goodEntity.getItemImgs()) {
            pageEntity.getList().add(new BannerEntity(imgsBean));
        }
        baseEntities.clear();
        baseEntities.add(pageEntity);
        baseEntities.add(goodEntity);
        baseEntities.add(new BaseEntity());
        baseEntities.add(new GoodDetailUrlEntity(goodEntity.getRich_text(), goodEntity.getParam()));
        adapter.setData(baseEntities);
        mBinding.rvContent.setNoMore(true);
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getGoodsDetailed(getIntent().getStringExtra(GoodsParam.ITEM_ID))
                .compose(RxSchedulers.<BaseData<GoodsDetailedEntity>>compose())
                .compose((this).<BaseData<GoodsDetailedEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<GoodsDetailedEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<GoodsDetailedEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() == null) {
                                ToastUtils.showShort(getString(R.string.sharemall_no_commodity_information));
                                finish();
                                return;
                            }
                            if (data.getData().getStatus() != 5) {
                                //状态：1上传中 2上架待审核 3待定价 4待上架
                                // 5已上架 6 下架待审核 7已下架'
                                ToastUtils.showShort(getString(R.string.sharemall_goods_not_on_the_shelf));
                                finish();
                                return;
                            }
                            goodEntity = data.getData();
                        } else {
                            showError(data.getErrmsg());
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                        showData(goodEntity);
                    }
                });
    }

    private void doListSpecsGroup() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listSpecsGroup(getIntent().getStringExtra(GoodsParam.ITEM_ID))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<List<SpecsGroupEntity>>>() {

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<List<SpecsGroupEntity>> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null && data.getData().size() > 0) {
                                ivid = data.getData().get(0).getIvid();
                                price = data.getData().get(0).getPrice();
                            }
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
}
