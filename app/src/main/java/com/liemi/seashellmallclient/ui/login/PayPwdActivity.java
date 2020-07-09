package com.liemi.seashellmallclient.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityPayPwdBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;

public class PayPwdActivity extends BaseActivity<ActivityPayPwdBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_pwd;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("交易密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.rl_update_pay_password){//修改交易密码
            JumpUtil.overlay(getContext(),UpdatePwdActivity.class,"update","pay");
        }else if (id == R.id.rl_forget_pay_password){//忘记交易密码
            JumpUtil.overlay(getContext(),ForgetPwdActivity.class,"forget","pay");
        }
    }
}
