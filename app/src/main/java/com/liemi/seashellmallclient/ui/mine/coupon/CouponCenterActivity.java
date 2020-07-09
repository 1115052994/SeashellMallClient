package com.liemi.seashellmallclient.ui.mine.coupon;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CouponApi;
import com.liemi.seashellmallclient.data.entity.BannerJumpEntity;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.event.CouponRefreshEvent;
import com.liemi.seashellmallclient.databinding.SharemallActivityXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemCouponCenterTopBinding;
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
import org.greenrobot.eventbus.EventBus;

public class CouponCenterActivity extends BaseXRecyclerActivity<SharemallActivityXrecyclerviewBinding, CouponEntity> {
    private SharemallItemCouponCenterTopBinding topBinding;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_xrecyclerview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(R.string.sharemall_get_coupon_center);

        xRecyclerView = mBinding.xrvData;

        topBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_item_coupon_center_top, mBinding.llContent, false);

        adapter = new BaseRViewAdapter<CouponEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_dialog_coupon;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.ll_collect && getItem(position).getIs_accept() == 0) {
                            doCollectCoupon(getItem(position));
                        }
                    }
                };
            }
        };

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.addHeaderView(topBinding.getRoot());
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    protected void initData() {
        doGetBanner();
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .listCoupon("platform", null, null, PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<PageEntity<CouponEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<CouponEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    private void doGetBanner() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .listBanner(7)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<BannerEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<BannerEntity>> data) {
                        if(data.getData() != null
                                && !Strings.isEmpty(data.getData().getList())) {
                            topBinding.ivBanner.setVisibility(View.VISIBLE);
                            final BannerEntity entity = data.getData().getList().get(0);
                            GlideShowImageUtils.displayNetImage(getContext(), entity.getImg_url(),
                                    topBinding.ivBanner);
                            topBinding.ivBanner.setOnClickListener((View view) -> new BannerJumpEntity().toJump(getContext(), entity));
                        }
                    }
                });
    }

    private void doCollectCoupon(CouponEntity couponEntity) {
        showProgress("");
        RetrofitApiFactory.createApi(CouponApi.class)
                .getCoupon(couponEntity.getId())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        couponEntity.setIs_accept(1);
                        adapter.notifyDataSetChanged();
                        //通知未使用优惠券列表更新
                        EventBus.getDefault().post(new CouponRefreshEvent());
                    }

                });

    }
}
