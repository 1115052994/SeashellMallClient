package com.liemi.seashellmallclient.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityLoginHomeBinding;
import com.netmi.baselibrary.utils.JumpUtil;
import com.tencent.bugly.beta.Beta;

public class LoginHomeActivity extends BaseLoginActivity<ActivityLoginHomeBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_login_home;
    }

    @Override
    protected void initUI() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.bt_sms_login) {
            //点击短信登录
            JumpUtil.overlay(this, LoginPhoneActivity.class);
            return;
        }
        if (id == R.id.bt_pwd_login) {
            //点击密码登录
            JumpUtil.overlay(this, PwdLoginActivity.class);
            return;
        }
        if (id == R.id.tv_register_quick) {
            //点击快捷注册
            JumpUtil.overlay(this, RegisterActivity.class);
            return;
        }

    }

}
