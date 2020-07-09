package com.liemi.seashellmallclient.ui.login;

import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivityUpdatePwdBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.Observable;

public class UpdatePwdActivity extends BaseActivity<ActivityUpdatePwdBinding>  {

    private String type = "";

    @Override
    protected int getContentView() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initUI() {
        String text = getIntent().getStringExtra("update");
        type = text;
        if (TextUtils.equals(text, "login")) {//修改登录密码
            getTvTitle().setText("修改登录密码");
            mBinding.etOldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etNewPwd1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBinding.etOldPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            mBinding.etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            mBinding.etNewPwd1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        } else {//修改交易密码
            getTvTitle().setText("修改交易密码");
            mBinding.etOldPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
            mBinding.etNewPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
            mBinding.etNewPwd1.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        mBinding.etOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mBinding.etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mBinding.etNewPwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected void initData() {

    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_save) {
            String oldPwd = mBinding.etOldPwd.getText().toString();
            String password = mBinding.etNewPwd.getText().toString();
            String passwordAgain = mBinding.etNewPwd1.getText().toString();
            if (TextUtils.isEmpty(oldPwd)){
                showError("请输入原密码");
            }
            if (TextUtils.isEmpty(password)) {
                showError("请输入新密码");
                return;
            }
            if (TextUtils.isEmpty(passwordAgain)) {
                showError("请再次输入新密码");
                return;
            }
            if (!password.equals(passwordAgain)) {
                showError(getString(R.string.sharemall_two_inconsistent_passwords));
                return;
            }
            doUpdatePassword();
        }
    }

    private void doUpdatePassword() {
        showProgress("");
        Observable<BaseData> observable = null;
        if (TextUtils.equals(type,"login")){
            observable = RetrofitApiFactory.createApi(LoginApi.class)
                    .doChangeLoginPassword(MD5.GetMD5Code(mBinding.etOldPwd.getText().toString(), true),MD5.GetMD5Code(mBinding.etNewPwd.getText().toString(), true));
        }else {
            observable = RetrofitApiFactory.createApi(LoginApi.class)
                    .doChangePayPassword(MD5.GetMD5Code(mBinding.etOldPwd.getText().toString(), true),MD5.GetMD5Code(mBinding.etNewPwd.getText().toString(), true));
        }

                observable.compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if(baseData.getErrcodeJugde() == Constant.SUCCESS_CODE){
                            showError("密码修改成功");
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
