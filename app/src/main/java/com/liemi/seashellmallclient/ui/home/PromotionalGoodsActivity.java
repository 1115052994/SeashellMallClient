package com.liemi.seashellmallclient.ui.home;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.BannerJumpEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallActivityXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemPromotionalGoodsBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemPromotionalGoodsTopBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.sharemoment.DialogShareImg;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.Observable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/19
 * 修改备注：
 */
public class PromotionalGoodsActivity extends BaseXRecyclerActivity<SharemallActivityXrecyclerviewBinding, GoodsListEntity> {

    private SharemallItemPromotionalGoodsTopBinding topBinding;

    private int type;

    private boolean isVip;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_xrecyclerview;
    }

    @Override
    protected void initUI() {
        getRightSettingImage().setBackgroundResource(R.mipmap.sharemall_ic_share_gray);
        getRightSettingImage().setVisibility(View.VISIBLE);

        topBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.sharemall_item_promotional_goods_top, mBinding.llContent, false);
        type = getIntent().getIntExtra(GoodsParam.PROMOTIONAL_TYPE, GoodsParam.PROMOTIONAL_TYPE_NEW);
        isVip = ShareMallUserInfoCache.get().isVip();

        getTvTitle().setText(getString(isVipArea() ? R.string.sharemall_vip_exclusive_area : R.string.sharemall_new_people_must_buy));

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setNestedScrollingEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<GoodsListEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_promotional_goods;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<GoodsListEntity>(binding) {
                    @Override
                    public void bindData(GoodsListEntity item) {
                        getBinding().setIsVIP(isVip && isVipArea());
                        super.bindData(item);
                    }

                    @Override
                    public SharemallItemPromotionalGoodsBinding getBinding() {
                        return (SharemallItemPromotionalGoodsBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Bundle bundle = null;
                        if (view.getId() == R.id.tv_extend) {
                            bundle = new Bundle();
                            bundle.putInt(GoodsParam.CURRENT_TAB, 1);
                        }
                        GoodDetailPageActivity.start(getContext(), getItem(position).getItem_id(), bundle);
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        doGetBanner();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_setting) {
            doGetImgUrl();
        }

    }

    private void showSelectImageDialog(String imageUrl) {
        new DialogShareImg(getActivity(), imageUrl).setActivity(getActivity()).show();
    }

    private boolean isVipArea() {
        return type == GoodsParam.PROMOTIONAL_TYPE_VIP;
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(HomeApi.class)
                .getVipOrBuy(PageUtil.toPage(startPage), Constant.PAGE_ROWS, isVipArea() ? 1 : 0, isVipArea() ? 0 : 1)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<GoodsListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    private void doGetBanner() {
        showProgress("");
        RetrofitApiFactory.createApi(CommonApi.class)
                .listBanner(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<BannerEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<BannerEntity>> data) {
                        if (dataExist(data) && !Strings.isEmpty(data.getData().getList())) {
                            BannerEntity bannerEntity = data.getData().getList().get(0);
                            GlideShowImageUtils.displayNetImage(getContext(), bannerEntity.getImg_url(), topBinding.ivBanner);
                            topBinding.ivBanner.setOnClickListener(v -> new BannerJumpEntity().toJump(getContext(), bannerEntity));
                            xRecyclerView.addHeaderView(topBinding.getRoot());
                        }
                    }

                    @Override
                    public void onComplete() {
                        xRecyclerView.refresh();
                    }
                });
    }

    private void doGetImgUrl() {
        showProgress("");
        Observable<BaseData<VIPShareImgEntity>> observable;
        if (isVipArea()) {
            observable = RetrofitApiFactory.createApi(HomeApi.class)
                    .getVipShare(null);
        } else {
            observable = RetrofitApiFactory.createApi(HomeApi.class)
                    .getNewShare(null);
        }
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<VIPShareImgEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        if (dataExist(data)) {
                            showSelectImageDialog(data.getData().getImg_url());
                        }
                    }
                });

    }


}
