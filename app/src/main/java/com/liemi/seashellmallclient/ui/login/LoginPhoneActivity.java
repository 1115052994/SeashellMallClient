package com.liemi.seashellmallclient.ui.login;

import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import cn.sharesdk.framework.PlatformActionListener;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityLoginPhoneBinding;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.InputListenView;

public class LoginPhoneActivity extends BaseLoginActivity<ActivityLoginPhoneBinding> implements PlatformActionListener, TextViewBindingAdapter.AfterTextChanged{
    public static final String LOGIN_DISPLAY = "loginDisplay";
    public static final String LOGIN_USER_AGREE = "loginUserAgree";
    private UserAgreeDialogFragment userAgreeDialogFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_login_phone;
    }

    @Override
    protected void initUI() {

        if (!TextUtils.equals((String) SPs.get(getContext(), LOGIN_DISPLAY, ""), AppUtils.getAppVersionName())||
                !SPs.getBoolean(getContext(),LOGIN_USER_AGREE,false)){
            SPs.put(getContext(), LOGIN_DISPLAY, AppUtils.getAppVersionName());
            if (userAgreeDialogFragment == null) {
                userAgreeDialogFragment = new UserAgreeDialogFragment();
                userAgreeDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
            } else {
                userAgreeDialogFragment.onStart();
            }
        }

        mBinding.setTextChange(this);
        mBinding.etMobile.setText(LoginInfoCache.get().getLogin());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected String getAuthType() {
        return LoginParam.AUTH_CODE_LOGIN;
    }

    @Override
    protected String getPhone() {
        return mBinding.etMobile.getText().toString();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);

        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();
            return;
        } else if (view.getId() == R.id.tv_forget_password) {
            //点击忘记密码
            JumpUtil.overlay(this, ForgetPassActivity.class, ParamConstant.TITLE,getString(R.string.sharemall_forget_pay_pwd));
            return;
        } else if (view.getId() == R.id.tv_phone_register) {
            //点击快捷注册
            JumpUtil.startForResult(this, RegisterActivity.class, 1000, null);
            return;
        } else if (view.getId() == R.id.tv_login) {
            String login = mBinding.etMobile.getText().toString();
            String code = mBinding.etCode.getText().toString();
            if (TextUtils.isEmpty(login)) {
                ToastUtils.showShort(R.string.sharemall_please_enter_phone_number_first);
            } else if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort(R.string.basemall_please_input_the_code);
            } else if (!Strings.isPhone(login)) {
                ToastUtils.showShort(R.string.sharemall_please_input_right_phone);
            } else {
                doLogin(mBinding.etMobile.getText().toString(), null,mBinding.etCode.getText().toString(), null, null, LoginParam.LOGIN_CODE);
            }
            return;
        } else if (view.getId() == R.id.tv_agreement) {
            doAgreement(LoginParam.PROTOCOL_TYPE_USER_PRIVACY);
            return;
        } else if (view.getId() == R.id.tv_service){
            doAgreement(LoginParam.PROTOCOL_TYPE_SERVICE);
            return;
        }else if (view.getId() == R.id.tv_wechat_login){
            //微信登录
            requestWechatAuth();
            return;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(mBinding.etMobile.getText().toString())
                && !TextUtils.isEmpty(mBinding.etCode.getText().toString())){
//            mBinding.tvLogin.setEnabled(true);
        }else {
//            mBinding.tvLogin.setEnabled(false);
        }
    }

}
