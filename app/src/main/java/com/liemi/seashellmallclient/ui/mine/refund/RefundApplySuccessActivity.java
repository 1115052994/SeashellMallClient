package com.liemi.seashellmallclient.ui.mine.refund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.event.OrderRefreshEvent;
import com.liemi.seashellmallclient.data.event.OrderRefundEvent;
import com.liemi.seashellmallclient.databinding.ActivityRefundApplySuccessBinding;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import org.greenrobot.eventbus.EventBus;

public class RefundApplySuccessActivity extends BaseActivity<ActivityRefundApplySuccessBinding> {

    public static final String REFUND_TIP = "refundTip";

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply_success;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_commit_apply));
        String tip = getIntent().getStringExtra(REFUND_TIP);
        if (!TextUtils.isEmpty(tip)) {
            mBinding.tvTip.setText(tip);
        }

        EventBus.getDefault().post(new OrderRefundEvent());
        EventBus.getDefault().post(new OrderRefreshEvent());
        EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_mine));
        AppManager.getInstance().finishAllActivity(MainActivity.class, RefundApplySuccessActivity.class);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            finish();
        } else {
        }
    }
}
