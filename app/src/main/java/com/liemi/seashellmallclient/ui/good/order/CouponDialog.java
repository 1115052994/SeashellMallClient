package com.liemi.seashellmallclient.ui.good.order;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.liemi.seashellmallclient.databinding.SharemallDialogCoinBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemCoinBinding;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

import java.util.ArrayList;

import static com.liemi.seashellmallclient.data.param.CouponParam.*;

public class CouponDialog extends BaseDialogFragment<SharemallDialogCoinBinding> {

    private BaseRViewAdapter<MineCouponEntity, BaseViewHolder> adapter;

    private MineCouponEntity choiceCouponEntity;

    private float totalPrice;

    private ArrayList<MineCouponEntity> couponList;

    private ChoiceCouponListener choiceCouponListener;

    public static CouponDialog newInstance(MineCouponEntity choiceCouponEntity, float price, ArrayList<MineCouponEntity> goodsList) {
        CouponDialog f = new CouponDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHOICE_COUPON, choiceCouponEntity);
        bundle.putFloat(CONDITION_PRICE, price);
        bundle.putSerializable(GOODS_LIST, goodsList);
        f.setArguments(bundle);
        return f;
    }

    public CouponDialog setChoiceCouponListener(ChoiceCouponListener choiceCouponListener) {
        this.choiceCouponListener = choiceCouponListener;
        return this;
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
        return R.layout.sharemall_dialog_coin;
    }

    @Override
    protected void initUI() {
        mBinding.tvTitle.setOnClickListener((View view) -> dismiss());
        if (getArguments() != null) {
            choiceCouponEntity = (MineCouponEntity) getArguments().getSerializable(CHOICE_COUPON);
            totalPrice = getArguments().getFloat(CONDITION_PRICE);
            couponList = (ArrayList<MineCouponEntity>) getArguments().getSerializable(GOODS_LIST);
        }

        if(Strings.isEmpty(couponList)) {
            ToastUtils.showShort(R.string.sharemall_not_coupon);
            dismiss();
        }

        mBinding.rvParam.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvParam.setAdapter(adapter = new BaseRViewAdapter<MineCouponEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_coin;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MineCouponEntity>(binding) {
                    @Override
                    public void bindData(MineCouponEntity item) {
                        //设置当前的优惠券状态
                        SharemallItemCoinBinding couponBinding = (SharemallItemCoinBinding) getBinding();
                        if (choiceCouponEntity != null && choiceCouponEntity.getCoupon_id() == item.getCoupon_id()) {
                            couponBinding.setCouponType(1);
                        } else {
                            couponBinding.setCouponType(0);
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        MineCouponEntity entity = getItem(position);
                        if (Strings.toFloat(entity.getCondition_num()) > totalPrice) {
                            ToastUtils.showShort(getString(R.string.sharemall_select_coupon_inconformity));
                        } else {
                            if (choiceCouponEntity != null && choiceCouponEntity.getCoupon_id() == entity.getCoupon_id()) {
                                if (choiceCouponListener != null) {
                                    choiceCouponListener.choiceBack(null);
                                }
                            } else {
                                if (choiceCouponListener != null) {
                                    choiceCouponListener.choiceBack(entity);
                                }
                            }
                            dismiss();
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        adapter.setData(couponList);
    }

    //优惠券选择监听
    public interface ChoiceCouponListener {

        void choiceBack(MineCouponEntity entity);

    }

}
