package com.liemi.seashellmallclient.ui.mine.userinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityChangePhoneAuthBinding;
import com.liemi.seashellmallclient.ui.login.BaseImageCodeActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.widget.InputListenView;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ChangePhoneAuthActivity extends BaseImageCodeActivity<ActivityChangePhoneAuthBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_change_phone_auth;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_change_phone));
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding.setPhone(UserInfoCache.get().getPhone());
        new InputListenView(mBinding.btnSave, mBinding.etInputVerificationCode) {
        };
        mBinding.executePendingBindings();
    }

    @Override
    protected String getPhone() {
        return UserInfoCache.get().getPhone();
    }

    @Override
    protected String getAuthType() {
        return LoginParam.AUTH_CODE_CHECK_PHONE;
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.btn_save) {
            doAuthCode();
        }
    }

    //验证验证码
    private void doAuthCode() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doAuthCode(UserInfoCache.get().getPhone(), LoginParam.AUTH_CODE_CHECK_PHONE, mBinding.etInputVerificationCode.getText().toString())
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            JumpUtil.overlay(ChangePhoneAuthActivity.this, ChangePhoneActivity.class);
                            finish();
                        } else {
                            showError(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }
}
