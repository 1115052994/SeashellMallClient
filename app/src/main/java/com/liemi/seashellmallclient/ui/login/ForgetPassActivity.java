package com.liemi.seashellmallclient.ui.login;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.databinding.ActivityForgetPassBinding;
import com.liemi.seashellmallclient.ui.BaseBlackTitleActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.InputListenView;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ForgetPassActivity extends BaseBlackTitleActivity<ActivityForgetPassBinding> {

    private String title;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(),true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_pass;
    }

    @Override
    protected void initUI() {
        super.initUI();
        mBinding.etInputPhone.setText(UserInfoCache.get().getPhone());

        new InputListenView(mBinding.btnConfirm, mBinding.etInputAuthCode, mBinding.etInputNewPassword, mBinding.etInputNewPasswordAgain) {
        };
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();
        }
        if (id == R.id.tv_get_auth_code) {
            doAuthCode();
            return;
        }
        if (id == R.id.btn_confirm) {
            String code = mBinding.etInputAuthCode.getText().toString();
            String password = mBinding.etInputNewPassword.getText().toString();
            String passwordAgain = mBinding.etInputNewPasswordAgain.getText().toString();
            if (TextUtils.isEmpty(code)) {
                showError(getString(R.string.sharemall_please_input_code));
            } else if (TextUtils.isEmpty(password) || password.length() != 6) {
                showError(getString(R.string.sharemall_please_input_new_password));
            } else if (TextUtils.isEmpty(passwordAgain)) {
                showError(getString(R.string.sharemall_please_input_new_password_again));
            } else if (!TextUtils.equals(password, passwordAgain)) {
                showError(getString(R.string.sharemall_two_inconsistent_passwords));
            } else {
                doForgetLoginPassword();
            }
        }
    }


    //开启倒计时
    private void startCountDownTimer() {
        mBinding.tvGetAuthCode.setEnabled(false);
        CountDownTimer mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.tvGetAuthCode.setText(millisUntilFinished / 1000 + getString(R.string.sharemall_reget_auth_code));
            }

            @Override
            public void onFinish() {
                mBinding.tvGetAuthCode.setEnabled(true);
                mBinding.tvGetAuthCode.setText(getString(R.string.sharemall_get_verification_code));
            }
        };
        mCountDownTimer.start();
    }

    //获取验证码
    private void doAuthCode() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(mBinding.etInputPhone.getText().toString(),
                        null,null,"reset")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        showError(baseData.getErrmsg());
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            startCountDownTimer();
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

    //忘记登录密码
    private void doForgetLoginPassword(){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doForgetPassword(MD5.GetMD5Code(mBinding.etInputNewPassword.getText().toString(),true),
                        mBinding.etInputAuthCode.getText().toString(),
                        mBinding.etInputPhone.getText().toString(),null,null)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if(baseData.getErrcode() == Constant.SUCCESS_CODE){
                            //重置成功
                            showError(getString(R.string.basemall_reset_login_password_successful));
                            finish();
                        }else{
                            ToastUtils.showShort(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }
}
