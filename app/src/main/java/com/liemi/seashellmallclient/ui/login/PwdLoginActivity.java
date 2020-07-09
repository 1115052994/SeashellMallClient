package com.liemi.seashellmallclient.ui.login;

import android.databinding.DataBindingUtil;
import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityPwdLoginBinding;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

import cn.sharesdk.framework.PlatformActionListener;

public class PwdLoginActivity extends BaseLoginActivity implements PlatformActionListener, TextViewBindingAdapter.AfterTextChanged {

    public static final String START_FROM = "startFrom";
    private ActivityPwdLoginBinding mPwdLoginBinding;
    protected int from = 0;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(),true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(LoginInfoCache.get().getLogin()) && mPwdLoginBinding != null)
            mPwdLoginBinding.etMobile.setText(LoginInfoCache.get().getLogin());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pwd_login;
    }

    @Override
    protected void initUI() {
        mPwdLoginBinding = DataBindingUtil.setContentView(this, getContentView());
        mPwdLoginBinding.setTextChange(this);
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            from = getIntent().getExtras().getInt(START_FROM, 0);
        }
    }

    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();

        } else if (view.getId() == R.id.tv_forget_password) {
            //点击忘记密码
            JumpUtil.overlay(this, ForgetPassActivity.class, ParamConstant.TITLE,getString(R.string.sharemall_forget_pay_pwd));
        } else if (view.getId() == R.id.tv_phone_register) {

            //点击快捷注册
            JumpUtil.startForResult(this, RegisterActivity.class, 1000, null);

        } else if (view.getId() == R.id.bt_login) {
            String login = mPwdLoginBinding.etMobile.getText().toString();
            String password = MD5.GetMD5Code(mPwdLoginBinding.etPassword.getText().toString(),true);
            if (TextUtils.isEmpty(login)) {
                ToastUtils.showShort(R.string.sharemall_please_enter_phone_number_first);
            } else if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort(R.string.basemall_please_input_password);
            } else if (!Strings.isPhone(login)) {
                ToastUtils.showShort(R.string.sharemall_please_input_right_phone);
            } else {
                doLogin(login, password, null, null, null,
                    "login_phone");
            }
        } else if (view.getId() == R.id.tv_agreement) {
            doAgreement(LoginParam.PROTOCOL_TYPE_USER_PRIVACY);
            return;
        } else if (view.getId() == R.id.tv_service){
            doAgreement(LoginParam.PROTOCOL_TYPE_SERVICE);
            return;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(mPwdLoginBinding.etMobile.getText().toString())
                && !TextUtils.isEmpty(mPwdLoginBinding.etPassword.getText().toString())){
            mPwdLoginBinding.btLogin.setEnabled(true);
        }else {
            mPwdLoginBinding.btLogin.setEnabled(false);
        }
    }


}
