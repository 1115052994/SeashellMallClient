package com.liemi.seashellmallclient.ui.login;

import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivitySetPayPasswordBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class SetPayPasswordActivity extends BaseActivity<ActivitySetPayPasswordBinding> implements TextViewBindingAdapter.AfterTextChanged {

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(),true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_pay_password;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_pay_pwd_setting2));
        mBinding.setAfterText(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if(view.getId() == R.id.bt_confirm){
            String password = mBinding.etInputPayPassword.getText().toString();
            String passwordAgain = mBinding.etInputPayPasswordAgain.getText().toString();
            if(TextUtils.isEmpty(password)){
                showError(getString(R.string.sharemall_please_enter_payment_password));
                return;
            }
            if(TextUtils.isEmpty(passwordAgain)){
                showError(getString(R.string.sharemall_please_enter_confirmation_password));
                return;
            }
            if(password.length() < 6){
                showError(getString(R.string.sharemall_password_min_six));
                return;
            }
            if(!password.equals(passwordAgain)){
                showError(getString(R.string.sharemall_two_inconsistent_passwords));
                return;
            }
            doSetPayPassword();
        }else if (view.getId() == R.id.ll_back){
            JumpUtil.overlay(getContext(), MainActivity.class);
            AppManager.getInstance().finishAllActivity(MainActivity.class);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!TextUtils.isEmpty(mBinding.etInputPayPassword.getText().toString())
                && !TextUtils.isEmpty(mBinding.etInputPayPasswordAgain.getText().toString())){
            mBinding.btConfirm.setEnabled(true);
        }else{
            mBinding.btConfirm.setEnabled(false);
        }
    }

    //请求设置支付密码
    private void doSetPayPassword(){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doSetPayPassword(MD5.GetMD5Code(mBinding.etInputPayPassword.getText().toString(),true))
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if(baseData.getErrcodeJugde() == Constant.SUCCESS_CODE){
                            showError(getString(R.string.sharemall_setting_payment_password_successfully));
                            //用户已经设置了交易密码，手动进行绑定，主要用于用户在密码管理页面跳转过来之后无法立即更新数据
                            if((UserInfoCache.get(ShareMallUserInfoEntity.class)) != null){
                                ShareMallUserInfoEntity en = (UserInfoCache.get(ShareMallUserInfoEntity.class));
                                en.setIs_set_paypassword(ShareMallUserInfoEntity.BIND);
                                UserInfoCache.put(en);
                            }
                            finish();
//                            JumpUtil.overlay(getContext(), MainActivity.class);
//                            AppManager.getInstance().finishAllActivity(MainActivity.class);
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
