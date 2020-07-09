package com.liemi.seashellmallclient.ui.login;

import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityRegisterBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.LoginInfo;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> implements TextViewBindingAdapter.OnTextChanged {

    private CountDownTimer mCountDownTimer;
    //记录用户是否获取了验证码
    private boolean mGetAuthCode = false;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(),true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUI() {
        mBinding.setTextChange(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();

        if (view.getId() == R.id.iv_close) {
            //点击返回
            finish();
        }
        else if (view.getId() == R.id.tv_agreement) {
            doAgreement(LoginParam.PROTOCOL_TYPE_USER_PRIVACY);
            return;
        } else if (view.getId() == R.id.tv_service){
            doAgreement(LoginParam.PROTOCOL_TYPE_SERVICE);
            return;
        }
        if (id == R.id.tv_get_auth_code) {
            //点击获取验证码
            if (checkPhone()) {
                doAuthCode();
            }
            return;
        }
       /* if (id == R.id.cb_check) {
            mBinding.cbCheck.setChecked(!mBinding.cbCheck.isChecked());
            return;
        }*/
        if (id == R.id.bt_register) {
            //点击注册
            if (!mGetAuthCode) {
                showError(getString(R.string.basemall_get_authentication_code_first));
                return;
            }
            if (!mBinding.cbCheck.isChecked()){
                showError("请同意《服务协议与隐私政策》");
                return;
            }
            if (checkCanRegister()) {
                //判断用户两次输入的密码是否一致
                //开始注册
                doRegister();
                return;
            }
        }

    }

    //查看用户是否可以开始注册
    private boolean checkCanRegister() {
        if (!checkPhone()) {
            return false;
        }
        if (!checkAuthCode()) {
            return false;
        }
        /*if (!checkPassword()) {
            return false;
        }
        if (!checkPasswordAgain()) {
            return false;
        }
        if (!checkPasswordRight()){
            return false;
        }*/
        return true;
    }

    //查看用户输入的手机号是否正确
    private boolean checkPhone() {
        if (Strings.isPhone(mBinding.etMobile.getText())) {
            return true;
        }
        showError(this.getString(R.string.sharemall_please_input_right_phone));
        return false;
    }

    //查看用户是否输入验证码
    private boolean checkAuthCode() {
        if (TextUtils.isEmpty(mBinding.etAuthCode.getText().toString())) {
            showError(getString(R.string.sharemall_please_input_code));
            return false;
        }
        return true;
    }

    //查看用户是否输入密码
    private boolean checkPassword() {
        if (TextUtils.isEmpty(mBinding.etPassword.getText().toString())) {
            showError(getString(R.string.basemall_please_input_password));
            return false;
        }
        if (mBinding.etPassword.getText().toString().length() < 6) {
            showError(getString(R.string.sharemall_password_min_six));
            return false;
        }
        return true;
    }

    //查看用户是否再次输入密码
    private boolean checkPasswordAgain() {
        if (TextUtils.isEmpty(mBinding.etAgainPwd.getText().toString())) {
            showError(getString(R.string.basemall_please_input_password_again));
            return false;
        }
        return true;
    }

    //查看用户两次输入的密码是否一致
    private boolean checkPasswordRight() {
        if (!mBinding.etAgainPwd.getText().toString().equals(mBinding.etPassword.getText().toString())) {
            showError(getString(R.string.sharemall_two_inconsistent_passwords));
            return false;
        }
        return true;
    }

    //开始倒计时
    private void startCountDownTimer() {
        mBinding.tvGetAuthCode.setEnabled(false);
        mCountDownTimer = new CountDownTimer(Constant.COUNT_DOWN_TIME_DEFAULT, 1000) {
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
                .doAuthCode(mBinding.etMobile.getText().toString(), null,null,"register")
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        showError(baseData.getErrmsg());
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            mGetAuthCode = true;
                            mBinding.etAuthCode.setText("");
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
                        hideProgress();
                    }
                });
    }

    //执行注册
    private void doRegister() {
        showProgress("");
        //MD5.GetMD5Code(mBinding.etPassword.getText().toString(), true)
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegister(mBinding.etMobile.getText().toString(),
                        mBinding.etAuthCode.getText().toString(),
                        null,
                        null,
                        mBinding.etInviteCode.getText().toString(),
                        null,
                        null,
                        "register_phone")
                .compose(this.<BaseData<ShareMallUserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<ShareMallUserInfoEntity>>compose())
                .subscribe(new BaseObserver<BaseData<ShareMallUserInfoEntity>>() {
                    @Override
                    public void onNext(BaseData<ShareMallUserInfoEntity> baseData) {
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            showError(getString(R.string.basemall_register_success));
                            //保存登录信息
                            LoginInfoCache.put(new LoginInfo(mBinding.etMobile.getText().toString(),
                                    mBinding.etPassword.getText().toString()));
                            //保存token信息
                            AccessTokenCache.put(baseData.getData().getToken());
                            //保存用户信息
                            UserInfoCache.put(baseData.getData());
                            //TODO:查看是否需要注册新歌推送
                           /* if(SPs.isOpenPushStatus()){
                                PushUtils.registerPush();
                            }*/
//                            JumpUtil.overlay(getContext(),SetPayPasswordActivity.class);
                            JumpUtil.overlay(getContext(), MainActivity.class);
                            AppManager.getInstance().finishAllActivity(MainActivity.class);
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
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }


    //请求服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent(RegisterActivity.this, BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                            if (agreementEntityBaseData.getData().getLink_type()==2) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                            startActivity(intent);
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
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
