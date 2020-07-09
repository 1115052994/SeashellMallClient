package com.liemi.seashellmallclient.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityBindPhoneBinding;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.InputListenView;

public class BindPhoneActivity extends BaseLoginActivity<ActivityBindPhoneBinding> {
    @Override
    protected int getContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initUI() {
    }

    @Override
    protected void initData() {
        super.initData();
        ToastUtils.showShort(R.string.sharemall_please_bind_your_mobile_number_first);
    }

    @Override
    protected String getPhone() {
        return mBinding.etMobile.getText().toString();
    }

    @Override
    protected String getAuthType() {
        return LoginParam.AUTH_CODE_BIND_PHONE;
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        //确认绑定
        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();
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
            }else {
                doBindPhone(mBinding.etMobile.getText().toString(),
                        mBinding.etCode.getText().toString(), UserInfoCache.get().getToken().getToken());
            }

        }
    }
}
