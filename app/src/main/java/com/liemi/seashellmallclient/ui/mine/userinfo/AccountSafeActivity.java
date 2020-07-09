package com.liemi.seashellmallclient.ui.mine.userinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityAccountSafeBinding;
import com.liemi.seashellmallclient.ui.login.ForgetPassActivity;
import com.liemi.seashellmallclient.ui.login.LoginPwdActivity;
import com.liemi.seashellmallclient.ui.login.PayPwdActivity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;

public class AccountSafeActivity extends BaseActivity<ActivityAccountSafeBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("账户安全");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.rl_login_password) {//登录密码
            JumpUtil.overlay(this, LoginPwdActivity.class);
        }else if (id == R.id.rl_pay_password) {//交易密码
            JumpUtil.overlay(this, PayPwdActivity.class);
        }
    }
}
