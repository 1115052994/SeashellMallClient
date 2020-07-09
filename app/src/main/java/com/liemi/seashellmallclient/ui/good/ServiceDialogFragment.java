package com.liemi.seashellmallclient.ui.good;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.databinding.SharemallDialogGoodDetailParamBinding;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 18:20
 * 修改备注：
 */
public class ServiceDialogFragment extends BaseDialogFragment<SharemallDialogGoodDetailParamBinding> {

    private BaseRViewAdapter<GoodsDetailedEntity.MeNaturesBean, BaseViewHolder> adapter;

    private GoodsDetailedEntity goodEntity;

    public ServiceDialogFragment setGoodsEntity(GoodsDetailedEntity goodsEntity) {
        this.goodEntity = goodsEntity;
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
        return R.layout.sharemall_dialog_good_detail_param;
    }

    @Override
    protected void initUI() {
        mBinding.ivCancel.setOnClickListener((View view) -> dismiss());
        mBinding.tvConfirm.setOnClickListener((View v) -> dismiss());
        mBinding.rvParam.setNestedScrollingEnabled(false);
        mBinding.rvParam.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvParam.setAdapter(adapter = new BaseRViewAdapter<GoodsDetailedEntity.MeNaturesBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_service;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                };
            }


        });
    }

    @Override
    protected void initData() {
        if (goodEntity != null) {
            adapter.setData(goodEntity.getMeNatures());
        }
    }
}
