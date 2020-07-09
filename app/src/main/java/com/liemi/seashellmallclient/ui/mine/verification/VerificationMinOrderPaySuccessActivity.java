package com.liemi.seashellmallclient.ui.mine.verification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.databinding.ActivityVerificationMinOrderPaySuccessBinding;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderActivity;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;

public class VerificationMinOrderPaySuccessActivity extends BaseActivity<ActivityVerificationMinOrderPaySuccessBinding> {

    public static final String PAY_GOODS = "payGoods";
    private int type;   //0门店买单 1核销

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_min_order_pay_success;
    }

    @Override
    protected void initUI() {
        boolean success = getIntent().getBooleanExtra("success", false);
        type = getIntent().getIntExtra("type", -1);
        if (success) {
            mBinding.tvPayError.setText("支付成功");
            mBinding.ivError.setImageResource(R.mipmap.sharemall_ic_result_success);
        } else {
            mBinding.tvPayError.setText("支付失败");
            mBinding.ivError.setImageResource(R.mipmap.sharemall_ic_result_fail);
        }
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra(ORDER_ENTITY)) {
            OrderPayEntity detailsEntity = (OrderPayEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
            mBinding.tvMoney.setText(detailsEntity.getShowPrice());
            return;
        }
        if (getIntent().hasExtra(ParamConstant.PRICE_TOTAL)) {
            mBinding.tvMoney.setText(getIntent().getStringExtra(ParamConstant.PRICE_TOTAL));
            return;
        }
        ToastUtils.showShort(getString(R.string.sharemall_lack_order_parameters));
        finish();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        /*if (view.getId() == R.id.tv_read_order) {
            if (type == 1) {
                MApplication.getInstance().appManager.finishActivity(MyVerificationActivity.class);
                JumpUtil.overlay(this, MyVerificationActivity.class);
                finish();
            } else if (type == 0) {
                MApplication.getInstance().appManager.finishActivity(MineOrderActivity.class);
                JumpUtil.overlay(this, MineOrderActivity.class);
                finish();
            }
        } else */if (view.getId() == R.id.tv_finish) {
            MApplication.getInstance().appManager.finishAllActivity(MainActivity.class);
            EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_local_life));
            finish();
        }
    }
}
