package com.liemi.seashellmallclient.ui.good;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.widget.LinearLayout;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CouponApi;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.param.CouponParam;
import com.liemi.seashellmallclient.databinding.SharemallDialogFragmentCouponBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemDialogCouponTopBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.DensityUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;


public class GoodsCouponDialogFragment extends BaseDialogFragment<SharemallDialogFragmentCouponBinding> {

    private BaseRViewAdapter<CouponEntity, BaseViewHolder> topAdapter;

    public static GoodsCouponDialogFragment newInstance(ArrayList<CouponEntity> couponList) {
        GoodsCouponDialogFragment f = new GoodsCouponDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CouponParam.COUPON_LIST, couponList);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public int getTheme() {
        return R.style.sharemall_CustomDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_dialog_fragment_coupon;
    }

    @Override
    protected void initUI() {
        ArrayList<CouponEntity> couponList = (ArrayList<CouponEntity>) getArguments().getSerializable(CouponParam.COUPON_LIST);
        if (Strings.isEmpty(couponList)) {
            ToastUtils.showShort(R.string.sharemall_not_coupon);
            dismiss();
            return;
        }
        //可领取的优惠券
        ArrayList<CouponEntity> collectCoupon = new ArrayList<>();
        //已领取的优惠券
        ArrayList<CouponEntity> collectedCoupon = new ArrayList<>();

        for (CouponEntity entity : couponList) {
            if (entity.getIs_accept() == 1) {
                collectedCoupon.add(entity);
            } else {
                collectCoupon.add(entity);
            }
        }

        xRecyclerView = mBinding.xrvData;

        //最大高度
        if (couponList.size() > 3) {
            xRecyclerView.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(400)));
        }

        SharemallItemDialogCouponTopBinding
                topBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_item_dialog_coupon_top, mBinding.llContent, false);

        topBinding.rvCouponCollect.setLayoutManager(new LinearLayoutManager(getContext()));
        topBinding.rvCouponCollect.setAdapter(topAdapter = getAdapter());
        topAdapter.setData(collectCoupon);
        topBinding.tvCouponGet.setVisibility(collectCoupon.size() == 0 ? View.GONE : View.VISIBLE);
        topBinding.tvCouponReceived.setVisibility(collectedCoupon.size() == 0 ? View.GONE : View.VISIBLE);

        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.addHeaderView(topBinding.getRoot());
        BaseRViewAdapter<CouponEntity, BaseViewHolder> adapter = getAdapter();
        xRecyclerView.setAdapter(adapter);
        adapter.setData(collectedCoupon);
    }

    private BaseRViewAdapter<CouponEntity, BaseViewHolder> getAdapter() {
        return new BaseRViewAdapter<CouponEntity, BaseViewHolder>(getContext()) {
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
    }

    @Override
    protected void initData() {
    }


    private void doCollectCoupon(CouponEntity couponEntity) {
        showProgress("");
        RetrofitApiFactory.createApi(CouponApi.class)
                .getCoupon(couponEntity.getId())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            couponEntity.setIs_accept(1);
                            topAdapter.notifyDataSetChanged();
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
