package com.liemi.seashellmallclient.ui.mine.refund;

import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.param.RefundParam;
import com.liemi.seashellmallclient.databinding.ActivityApplyRefundTypeBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;
import static com.liemi.seashellmallclient.ui.mine.refund.ApplyForRefundActivity.*;

public class ApplyRefundTypeActivity extends BaseActivity<ActivityApplyRefundTypeBinding> {

    private OrderSkusEntity skusEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_refund_type;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_order_details_good_apply_sales));
    }

    @Override
    protected void initData() {
        skusEntity = (OrderSkusEntity) getIntent().getSerializableExtra(RefundParam.SKU_ENTITY);
        if (skusEntity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_info));
            finish();
            return;
        }
        mBinding.setItem(skusEntity);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int clickId = view.getId();
        if (clickId == R.id.rl_refund_only_money) {
            //点击仅退款
            ApplyForRefundActivity.start(getContext(), skusEntity, RefundParam.REFUND_TYPE_ONLY_CASH, null);
            finish();
            return;
        }
        if (clickId == R.id.rl_refund_goods) {
            //点击退款退货
            ApplyForRefundActivity.start(getContext(), skusEntity, RefundParam.REFUND_TYPE_GOODS_AND_CASH, null);
            finish();
        }
    }

}
