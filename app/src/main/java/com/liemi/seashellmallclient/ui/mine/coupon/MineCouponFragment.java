package com.liemi.seashellmallclient.ui.mine.coupon;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.liemi.seashellmallclient.data.entity.user.MineCouponShareEntity;
import com.liemi.seashellmallclient.data.event.CouponRefreshEvent;
import com.liemi.seashellmallclient.data.param.CouponParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentMineCouponBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemMineCouponBinding;
import com.liemi.seashellmallclient.ui.sharemoment.DialogShareImg;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MineCouponFragment extends BaseXRecyclerFragment<SharemallFragmentMineCouponBinding, MineCouponEntity> implements XRecyclerView.LoadingListener {

    private int couponStatus;

    public static MineCouponFragment newInstance(int status) {
        MineCouponFragment mineCouponFragment = new MineCouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CouponParam.COUPON_STATUS, status);
        mineCouponFragment.setArguments(bundle);
        return mineCouponFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_mine_coupon;
    }

    @Override
    protected void initUI() {
        couponStatus = getArguments() != null ? getArguments().getInt(CouponParam.COUPON_STATUS) : 0;
        mBinding.setCouponType(couponStatus);
        mBinding.setDoClick(this);

        xRecyclerView = mBinding.frContent;
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        xRecyclerView.getItemAnimator().setChangeDuration(300);
        xRecyclerView.getItemAnimator().setMoveDuration(300);

        adapter = new BaseRViewAdapter<MineCouponEntity, BaseViewHolder>(getContext(),
                xRecyclerView, R.layout.baselib_include_no_data_view2,
                new EmptyLayoutEntity(R.mipmap.sharemall_ic_no_coupon,
                        getString(R.string.sharemall_no_coupon))) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_mine_coupon;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MineCouponEntity>(binding) {
                    @Override
                    public void bindData(MineCouponEntity item) {
                        getBinding().tvCouponPrice.setTextColor(item.getStatus() == CouponParam.COUPON_STATUS_NOT_USED ? getResources().getColor(AppConfigCache.get().getAppTheme().getColor_price(R.color.bgColor)) : getResources().getColor(R.color.gray_99));
                        switch (item.getStatus()) {
                            case CouponParam.COUPON_STATUS_USED:
                                getBinding().ivStatus.setImageResource(R.mipmap.sharemall_ic_coupon_uesd);
                                break;
                            case CouponParam.COUPON_STATUS_TIMED:
                                getBinding().ivStatus.setImageResource(R.mipmap.sharemall_ic_coupon_invalid);
                                break;
                 /*           case CouponParam.COUPON_STATUS_SHARE:
                                getBinding().ivStatus.setImageResource(R.mipmap.sharemall_ic_coupon_received);
                                break;
                            case 5: //分享中
                                getBinding().ivStatus.setImageResource(R.mipmap.sharemall_ic_coupon_sharing);
                                break;*/
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_use) {
                            switch (getItem(position).getItem_type()) {
                                case 1:
                                    //平台券
                                    MApplication.getInstance().backHome();
                                    break;
                                case 2:
                                    //店铺券
                                    if (!TextUtils.isEmpty(getItem(position).getShop_id())) {
                                        StoreDetailActivity.start(getContext(), getItem(position).getShop_id());
                                    }
                                    break;
                                case 3:
                                    break;
                            }
                        }
                        //点击的时候查看是否是未使用的优惠券，如果是，改变状态
                        else if (getItem(position).getStatus() == CouponParam.COUPON_STATUS_NOT_USED) {
                            getItem(position).setSelect(!items.get(position).isSelect());
                            notifyPosition(position);
                        }
                    }

                    @Override
                    public SharemallItemMineCouponBinding getBinding() {
                        return (SharemallItemMineCouponBinding) super.getBinding();
                    }
                };
            }
        };

        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_share_coupon) {
            doShareSelectCoupon();
        } else if (view.getId() == R.id.tv_get_coupon || view.getId() == R.id.tv_coupon_center) {
            JumpUtil.overlay(getContext(), CouponCenterActivity.class);
        }
    }


    //接收从分享页面传递的数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareSuccess(CouponRefreshEvent event) {
        if (couponStatus == CouponParam.COUPON_STATUS_NOT_USED_SHARING) {
            //刷新数据
            xRecyclerView.refresh();
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .listCoupon(PageUtil.toPage(startPage), Constant.PAGE_ROWS, couponStatus, null, "all")
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<PageEntity<MineCouponEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<MineCouponEntity>> data) {
                        showData(data.getData());
                    }
                });
    }


    //请求分享优惠券
    private void doShareSelectCoupon() {
        List<String> selectIds = new ArrayList<>();
        for (MineCouponEntity entity : adapter.getItems()) {
            if (entity.isSelect()) {
                selectIds.add(String.valueOf(entity.getCoupon_id()));
            }
        }
        //遍历查询用户选择的优惠券信息
        if (selectIds.isEmpty()) {
            showError(getString(R.string.sharemall_lack_share_coupon));
            return;
        }

        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doShareSelectCoupon(selectIds)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<MineCouponShareEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<MineCouponShareEntity> data) {
                        if (dataExist(data)) {
                            new DialogShareImg(requireContext(), data.getData().getPost_url())
                                    .setOnClickListener((View v) -> {
                                        if (v.getId() == R.id.tv_share_friend
                                                || v.getId() == R.id.tv_share_wechat_moment
                                                || v.getId() == R.id.tv_share_save_local) {
                                            doChangeCouponStatus(data.getData());
                                        }
                                    })
                                    .setActivity(getActivity())
                                    .show();
                        }
                    }
                });
    }

    private void doChangeCouponStatus(MineCouponShareEntity entity) {
        RetrofitApiFactory.createApi(MineApi.class)
                .doChangeCouponStatus(entity.getShare_id())
                .compose(RxSchedulers.compose())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        xRecyclerView.refresh();
                    }
                });
    }

}
