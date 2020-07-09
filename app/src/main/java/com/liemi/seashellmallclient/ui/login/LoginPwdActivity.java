package com.liemi.seashellmallclient.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityLoginPwdBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;

public class LoginPwdActivity extends BaseActivity<ActivityLoginPwdBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_login_pwd;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("登录密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.rl_update_login_password){//修改登录密码
            JumpUtil.overlay(getContext(),UpdatePwdActivity.class,"update","login");
        }else if (id == R.id.rl_forget_login_password){//忘记登录密码
            JumpUtil.overlay(getContext(),ForgetPwdActivity.class,"forget","login");
        }
    }
}
