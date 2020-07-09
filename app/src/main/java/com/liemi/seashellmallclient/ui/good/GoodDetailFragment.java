package com.liemi.seashellmallclient.ui.good;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CouponApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.ShareApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.entity.floor.MaterialEntity;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodDetailUrlEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsLabelEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.*;
import com.liemi.seashellmallclient.ui.shopcart.ShopCartActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.ui.vip.VipUpgradeActivity;
import com.liemi.seashellmallclient.utils.HTMLFormat;
import com.liemi.seashellmallclient.utils.SobotApiUtils;
import com.liemi.seashellmallclient.widget.GoodsBannerViewHolder;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.*;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.baselibrary.widget.ScrollSpeedLinearLayoutManger;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_NORMAL;
import static com.liemi.seashellmallclient.data.param.GoodsBuyDialogParam.*;
import static com.netmi.baselibrary.data.Constant.ALL_PAGES;
import static com.netmi.baselibrary.data.Constant.BASE_HTML;

public class GoodDetailFragment extends BaseGoodsDetailedFragment<SharemallFragmentGoodDetailBinding> {

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    private WebView goodsWebView;

    private GoodsDetailedEntity goodEntity;

    private MaterialEntity materialEntity = new MaterialEntity();

    //购买弹出框
    private GoodsBuyDialogFragment buyDialogFragment;

    //一级规格名称
    private TextView tvProperty;

    //优惠券列表
    private PageEntity<CouponEntity> couponPageEntity;

    //可用于列表滑动，视频小窗口播放
    private View bannerView;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_good_detail;
    }

    @Override
    protected MyXRecyclerView getXrvData() {
        return mBinding.xrvGood;
    }

    @Override
    protected void initUI() {
        super.initUI();
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.sharemall_item_goods_detail_banner;
                } else if (viewType == 2) {
                    return R.layout.sharemall_item_goods_detail_info;
                } else if (viewType == 4) {
                    return R.layout.sharemall_item_goods_detail_comment;
                } else if (viewType == 5) {
                    return R.layout.sharemall_item_goods_detail_webview;
                } else if (viewType == 6) {
                    return R.layout.sharemall_item_goods_detailed_store;
                }
                return R.layout.sharemall_item_goods_detail_web;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof BannerEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof GoodsDetailedEntity) {
                    return 2;
                } else if (adapter.getItem(position) instanceof CommentEntity) {
                    return 4;
                } else if (adapter.getItem(position) instanceof GoodDetailUrlEntity) {
                    return 5;
                } else if (adapter.getItem(position) instanceof StoreEntity) {
                    return 6;
                }
                return super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {
                    @Override
                    public void bindData(final BaseEntity item) {
                        super.bindData(item);
                        if (getBinding() instanceof SharemallItemGoodsDetailBannerBinding) {
                            final SharemallItemGoodsDetailBannerBinding bannerBinding = (SharemallItemGoodsDetailBannerBinding) getBinding();
                            List<String> bannerList = new ArrayList<>();
                            boolean hasVideo = !TextUtils.isEmpty(goodEntity.getShort_video_url());
                            if (hasVideo) {
                                bannerList.add(goodEntity.getShort_video_url());
                            }
                            bannerList.addAll(goodEntity.getItemImgs());

                            //轮播间隔
                            bannerBinding.cbBanner.setDelayedTime(5000);
                            //滑动速度
                            bannerBinding.cbBanner.setDuration(1500);

                            bannerBinding.cbBanner.setCanLoop(!hasVideo);
                            //指示器隐藏
                            bannerBinding.cbBanner.setIndicatorVisible(true);

                            bannerBinding.cbBanner.setBannerPageClickListener((View view, int i) -> JumpUtil.overlayImagePreview(getActivity(), goodEntity.getItemImgs(), i - (hasVideo ? 1 : 0)));

                            // 设置数据
                            bannerBinding.cbBanner.setPages(bannerList,
                                    () -> new GoodsBannerViewHolder(bannerBinding.cbBanner, (View view) -> bannerView = view)
                                            .setDefaultImage(Strings.isEmpty(goodEntity.getItemImgs()) ? null : goodEntity.getItemImgs().get(0)));

                            bannerBinding.cbBanner.start();


                        } else if (getBinding() instanceof SharemallItemGoodsDetailInfoBinding) {
                            final SharemallItemGoodsDetailInfoBinding binding = (SharemallItemGoodsDetailInfoBinding) getBinding();

                            tvProperty = binding.tvProperty;

                            if (couponPageEntity != null && !Strings.isEmpty(couponPageEntity.getList())) {
                                binding.llCoupon.setVisibility(View.VISIBLE);
                                FlexboxLayout flexboxLayout = binding.flCoupon;
                                flexboxLayout.removeAllViews();
                                flexboxLayout.setFlexWrap(FlexWrap.NOWRAP);
                                for (CouponEntity bean : couponPageEntity.getList()) {
                                    //只显示三张
                                    if (flexboxLayout.getFlexItemCount() > 2) break;

                                    SharemallItemGoodsDetailedCouponBinding
                                            couponBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_item_goods_detailed_coupon, flexboxLayout, false);
                                    couponBinding.tvCouponName.setText(String.format(getString(R.string.sharemall_format_coupon_info), bean.getCondition_num(), bean.getDiscount_num()));
                                    FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (flexboxLayout.getFlexItemCount() < 2) {
                                        //前两张完整显示，最后一张可压缩省略显示
                                        lp.setMinWidth((int) couponBinding.tvCouponName.getPaint().measureText(couponBinding.tvCouponName.getText().toString()) + DensityUtils.dp2px(24));
                                    }
                                    flexboxLayout.addView(couponBinding.getRoot(), lp);
                                }
                            } else {
                                binding.llCoupon.setVisibility(View.GONE);
                            }

                            binding.setIsVIP(ShareMallUserInfoCache.get().isVip());
                            binding.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            if (Strings.toFloat(goodEntity.getPostage()) > 0) {
                                binding.tvPostage.setText(goodEntity.formatMoney(goodEntity.getPostage()));
                            } else {
                                binding.tvPostage.setText(getString(R.string.sharemall_free_shipping));
                            }
                        } else if (getBinding() instanceof SharemallItemGoodsDetailWebBinding) {
                            SharemallItemGoodsDetailWebBinding binding = (SharemallItemGoodsDetailWebBinding) getBinding();
                            binding.btDetail.setOnClickListener((View view) -> {
                                binding.btDetail.setClickable(false);
                                binding.btService.setClickable(true);
                                binding.btService.setChecked(false);
                                if (goodsWebView != null) {
                                    goodsWebView.loadDataWithBaseURL(BASE_HTML, HTMLFormat.getNewData(goodEntity.getRich_text()), "text/html", "UTF-8", null);
                                }
                            });
                            binding.btService.setOnClickListener((View view) -> {
                                binding.btService.setClickable(false);
                                binding.btDetail.setClickable(true);
                                binding.btDetail.setChecked(false);
                                if (goodsWebView != null) {
                                    goodsWebView.loadDataWithBaseURL(BASE_HTML, HTMLFormat.getNewData(goodEntity.getBuy_rich_text()), "text/html", "UTF-8", null);
                                }
                            });
                        } else if (getBinding() instanceof SharemallItemGoodsDetailWebviewBinding) {
                            goodsWebView = ((SharemallItemGoodsDetailWebviewBinding) getBinding()).wvGood;
                            goodsWebView.setWebViewClient(new WebViewClient());
                            goodsWebView.setWebChromeClient(new WebChromeClient());
                            goodsWebView.setHorizontalScrollBarEnabled(false);
                            goodsWebView.setVerticalScrollBarEnabled(false);
                            goodsWebView.getSettings().setDefaultFontSize(20);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                goodsWebView.getSettings().setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
                            }
                            goodsWebView.loadDataWithBaseURL(BASE_HTML, HTMLFormat.getNewData(goodEntity.getRich_text()), "text/html", "UTF-8", null);

                        } else if (getBinding() instanceof SharemallItemGoodsDetailCommentBinding) {
                            SharemallItemGoodsDetailCommentBinding binding = (SharemallItemGoodsDetailCommentBinding) getBinding();
                            binding.rbStarServer.setStar(Float.valueOf(((CommentEntity) getItem(position)).getLevel()), false);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.ll_type) {

                            showBuyDialog(GOODS_BUY_SPEC_CHOICE);

                        } else if (i == R.id.ll_coupon) {
                            doListCoupon(true);
                        } else if (i == R.id.ll_service) {
                            if (!TextUtils.isEmpty(goodEntity.getService())) {
                                new ServiceDialogFragment().setGoodsEntity(goodEntity).show(getChildFragmentManager(), TAG);
                            }
                        } else if (i == R.id.tv_comment) {
                            switchTab(2);
                        } else if (i == R.id.rl_store) {
                            StoreDetailActivity.start(getContext(), goodEntity.getShop_id());
                        } else if (i == R.id.iv_e1 || i == R.id.iv_e2 || i == R.id.iv_e3) {
                            if (goodEntity.getMeCommet() != null && !Strings.isEmpty(goodEntity.getMeCommet().getMeCommetImgs())) {
                                JumpUtil.overlayImagePreview(getActivity(), goodEntity.getMeCommet().getMeCommetImgs(), i == R.id.iv_e1 ? 0 : i == R.id.iv_e2 ? 1 : 2);
                            }
                        }
                    }
                };
            }
        });

        xRecyclerView.setItemViewCacheSize(10);
    }

    public GoodsDetailedEntity getGoodEntity() {
        return goodEntity;
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding.setDoClick(this);
        mBinding.executePendingBindings();
        showProgress("");
        xRecyclerView.refresh();
    }

    private void switchTab(int position) {
        if (getActivity() instanceof GoodDetailPageActivity) {
            ((GoodDetailPageActivity) getActivity()).switchTab(position);
        }
    }

    private boolean isCanBuy() {

        if (goodEntity == null) {
            ToastUtils.showShort(R.string.sharemall_no_data);
            return false;
        }

        if (!ShareMallUserInfoCache.get().isVip()
                && goodEntity.getIs_vip() == 1) {
            new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.sharemall_become_vip_message)
                    .setPositiveButton(R.string.sharemall_become_vip, (DialogInterface dialog, int which) -> JumpUtil.overlay(getContext(), VipUpgradeActivity.class))
                    .setNegativeButton(R.string.sharemall_cancel, null)
                    .show();
            return false;
        }

        boolean canBuy = TextUtils.isEmpty(goodEntity.getCan_buy());
        if (!canBuy) {
            ToastUtils.showShort(goodEntity.getCan_buy());
        }
        return canBuy;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_buy) {
            if (isCanBuy()) {
                showBuyDialog(GOODS_BUY_IMMEDIATELY);
            }
        } else if (view.getId() == R.id.tv_add_shop_cart) {
            if (isCanBuy()) {

                showBuyDialog(GOODS_BUY_ADD_SHOPCART);

            }
        } else if (view.getId() == R.id.iv_server) {
            doGetSobotInfo();
        } else if (view.getId() == R.id.iv_shop_car) {
            JumpUtil.overlay(getContext(), ShopCartActivity.class);
        }
    }

    private void showBuyDialog(int buyType) {

        if (buyDialogFragment == null) {
            buyDialogFragment = new GoodsBuyDialogFragment().setGoodsEntity(goodEntity);
            buyDialogFragment.setTvProperty(tvProperty);
            buyDialogFragment.setBuyType(buyType);
            buyDialogFragment.show(getChildFragmentManager(), TAG);
        } else {
            buyDialogFragment.setBuyType(buyType);
            buyDialogFragment.onStart();
        }


    }

    @Override
    public void showData(GoodsDetailedEntity goodEntity) {
        this.goodEntity = goodEntity;
        mBinding.setItem(goodEntity);
        mBinding.setIsVIP(ShareMallUserInfoCache.get().isVip());

        mBinding.executePendingBindings();
        //普通商品，显示优惠券领取
        if (goodEntity.getActivity_type() == GOODS_NORMAL) {
            doListCoupon(false);
        }
    }


    private boolean hasStock() {
        if (goodEntity.isGroupItem()) {
            return Strings.toInt(goodEntity.getStock()) >= Strings.toInt(goodEntity.getGroupItem().getNumber());
        } else {
            return Strings.toInt(goodEntity.getStock()) > 0;
        }
    }

    public void onRestart() {
        if (buyDialogFragment != null) {
            buyDialogFragment.setHideDialog(true);
        }
    }

    private void showResult() {
        hideProgress();
        ArrayList<BaseEntity> baseEntities = new ArrayList<>();
        //Banner
        baseEntities.add(new BannerEntity(""));
        //详情
        baseEntities.add(goodEntity);
        //店铺信息
        if (goodEntity.getShop() != null && !TextUtils.isEmpty(goodEntity.getShop().getId())) {
            baseEntities.add(goodEntity.getShop());
        }
        //评论
        if (goodEntity.getMeCommet() != null) {
            baseEntities.add(goodEntity.getMeCommet());
        }
        //商品详情和购买须知
        baseEntities.add(new BaseEntity());
        //富文本
        baseEntities.add(new GoodDetailUrlEntity(goodEntity.getRich_text(), goodEntity.getParam(), goodEntity.getBuy_rich_text()));
        adapter.setData(baseEntities);
    }


    //是否显示优惠券弹窗窗口
    private void doListCoupon(boolean showDialog) {
        if (showDialog) {
            showProgress("");
        }
        RetrofitApiFactory.createApi(CouponApi.class)
                .listCoupon("item", null, goodEntity.getItem_id(), 0, Constant.ALL_PAGES)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<PageEntity<CouponEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        onComplete();
                    }

                    @Override
                    public void onNext(@NonNull BaseData<PageEntity<CouponEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            couponPageEntity = data.getData();
                            if (showDialog) {
                                GoodsCouponDialogFragment.newInstance((ArrayList<CouponEntity>) couponPageEntity.getList()).show(getChildFragmentManager(), TAG);
                            }
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (!showDialog) {
                            showResult();
                        } else {
                            hideProgress();
                        }
                    }
                });
    }


    private void doGetSobotInfo() {
        /*String liemi_intel_tel = AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + liemi_intel_tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doGetSobotInfo(0, "", goodEntity.getItem_id())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<SobotSystemEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<SobotSystemEntity> data) {
                        SobotApiUtils.getInstance().toCustomServicePage(getContext(), UserInfoCache.get(), goodEntity, data.getData());
                    }
                });
    }


}
