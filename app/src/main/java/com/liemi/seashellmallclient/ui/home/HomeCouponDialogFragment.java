package com.liemi.seashellmallclient.ui.home;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.param.CouponParam;
import com.liemi.seashellmallclient.databinding.SharemallDialogFragmentGetCouponBinding;
import com.liemi.seashellmallclient.utils.DensityUtils;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

import java.util.ArrayList;


public class HomeCouponDialogFragment extends BaseDialogFragment<SharemallDialogFragmentGetCouponBinding> {

    public static HomeCouponDialogFragment newInstance(ArrayList<CouponEntity> couponList) {
        HomeCouponDialogFragment f = new HomeCouponDialogFragment();
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
    protected int getContentView() {
        return R.layout.sharemall_dialog_fragment_get_coupon;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.width = window.getWindowManager().getDefaultDisplay().getWidth() - DensityUtils.dp2px(80);
            window.setAttributes(lp);
        }
    }

    @Override
    protected void initUI() {
        ArrayList<CouponEntity> couponList = (ArrayList<CouponEntity>) getArguments().getSerializable(CouponParam.COUPON_LIST);
        if (Strings.isEmpty(couponList)) {
            ToastUtils.showShort(R.string.sharemall_not_coupon);
            dismiss();
            return;
        }

        mBinding.ivGetAll.setOnClickListener((View view) -> dismiss());
        mBinding.rvCoupon.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseRViewAdapter<CouponEntity, BaseViewHolder> adapter = getAdapter();
        mBinding.rvCoupon.setAdapter(adapter);
        couponList.add(0, new CouponEntity());
        adapter.setData(couponList);
    }

    private BaseRViewAdapter<CouponEntity, BaseViewHolder> getAdapter() {
        return new BaseRViewAdapter<CouponEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == -1) {
                    return R.layout.sharemall_item_home_dialog_coupon_top;
                }
                return R.layout.sharemall_item_home_dialog_coupon;
            }

            @Override
            public int getItemViewType(int position) {
                return position == 0 ? -1 : super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                };
            }
        };
    }

    @Override
    protected void initData() {
    }


}
