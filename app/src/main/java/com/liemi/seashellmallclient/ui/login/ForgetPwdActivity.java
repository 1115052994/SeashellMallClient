package com.liemi.seashellmallclient.ui.login;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivityForgetPwdBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.MD5;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.Observable;

public class ForgetPwdActivity extends BaseActivity<ActivityForgetPwdBinding> {
    private String type = "";
    private ShareMallUserInfoEntity userInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initUI() {
        String text = getIntent().getStringExtra("forget");
        type = text;
        userInfoEntity = UserInfoCache.get(ShareMallUserInfoEntity.class);
        if (TextUtils.equals(text, "login")) {//忘记登录密码
            getTvTitle().setText("忘记登录密码");
            mBinding.etNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etNewPwd1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            mBinding.etNewPwd1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        } else {//忘记交易密码
            getTvTitle().setText("忘记交易密码");
            mBinding.etNewPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
            mBinding.etNewPwd1.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        mBinding.etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mBinding.etNewPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_get_auth_code) {
            doAuthCode();
            return;
        }
        if (id == R.id.tv_save) {
            String code = mBinding.etAuthCode.getText().toString();
            String password = mBinding.etNewPwd.getText().toString();
            String passwordAgain = mBinding.etNewPwd1.getText().toString();
            if (TextUtils.isEmpty(code)) {
                showError(getString(R.string.sharemall_please_input_code));
            } else if (TextUtils.isEmpty(password) || password.length() < 6) {
                showError(getString(R.string.sharemall_please_input_new_password));
            } else if (TextUtils.isEmpty(passwordAgain)) {
                showError(getString(R.string.sharemall_please_input_new_password_again));
            } else if (!TextUtils.equals(password, passwordAgain)) {
                showError(getString(R.string.sharemall_two_inconsistent_passwords));
            } else {
                doForgetPassword();
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
                .doAuthCode(userInfoEntity.getPhone(),
                        null,null,TextUtils.equals(type,"login")?"reset":"payPassword")
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

    private void doForgetPassword() {
        showProgress("");
        Observable<BaseData> observable = null;
        if (TextUtils.equals(type,"login")){
            observable = RetrofitApiFactory.createApi(LoginApi.class)
                    .doForgetPassword(MD5.GetMD5Code(mBinding.etNewPwd.getText().toString(),true),
                            mBinding.etAuthCode.getText().toString(),
                            userInfoEntity.getPhone(),null,null);
        }else {
            observable = RetrofitApiFactory.createApi(LoginApi.class)
                    .doForgetPayPassword(MD5.GetMD5Code(mBinding.etNewPwd.getText().toString(), true),
                            mBinding.etAuthCode.getText().toString(),
                            userInfoEntity.getPhone());
        }

        observable.compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if(baseData.getErrcodeJugde() == Constant.SUCCESS_CODE){
                            if (TextUtils.equals(type,"login")) {
                                showError(getString(R.string.basemall_reset_login_password_successful));
                            }else {
                                showError(UserInfoCache.get(ShareMallUserInfoEntity.class).getIs_set_paypassword() == 1 ?
                                        getString(R.string.sharemall_restart_pay_pwd_success) : getString(R.string.sharemall_set_pwd_success));
                                UserInfoCache.get(ShareMallUserInfoEntity.class).setIs_set_paypassword(1);
                                UserInfoCache.put(UserInfoCache.get(ShareMallUserInfoEntity.class));
                            }
                            finish();
                        }else{
                            showError(baseData.getErrmsg());
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
