package com.liemi.seashellmallclient.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.databinding.ActivityBindInviteCodeBinding;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

public class BindInviteCodeActivity extends BaseLoginActivity<ActivityBindInviteCodeBinding> {


    @Override
    protected int getContentView() {
        return R.layout.activity_bind_invite_code;
    }

    @Override
    protected void initUI() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_close){
            finish();
            return;
        }
        //确认绑定
        if (view.getId() == R.id.tv_login) {
            String code = mBinding.etInviteCode.getText().toString();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort(R.string.sharemall_please_enter_phone_number_first);
            } else if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort(R.string.basemall_please_input_the_code);
            } else if (!Strings.isPhone(code)) {
                ToastUtils.showShort(R.string.sharemall_please_input_right_phone);
            }else {
                doBindInviteCode(mBinding.etInviteCode.getText().toString());
            }
            return;
        }else if (view.getId() == R.id.tv_jump){
            toHomePage(ShareMallUserInfoCache.get());
        }
    }
}
