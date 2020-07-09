package com.liemi.seashellmallclient.ui.mine.userinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityChangePhoneBinding;
import com.liemi.seashellmallclient.ui.login.BaseImageCodeActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.widget.InputListenView;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ChangePhoneActivity extends BaseImageCodeActivity<ActivityChangePhoneBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void initUI() {
        super.initData();
        getTvTitle().setText(getString(R.string.sharemall_change_phone));
        new InputListenView(mBinding.btnSave, mBinding.etInputNewPhone, mBinding.etInputVerificationCode) {
        };
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected String getPhone() {
        return mBinding.etInputNewPhone.getText().toString();
    }

    @Override
    protected String getAuthType() {
        return LoginParam.AUTH_CODE_CHANGE_PHONE;
    }

    @Override
    public void doClick(View view) {
        if (view.getId() == R.id.tv_get_code) {
            if (!Strings.isPhone(mBinding.etInputNewPhone.getText().toString())) {
                showError(getString(R.string.sharemall_please_input_right_phone));
                return;
            }
        }
        if (view.getId() == R.id.btn_save) {
            if (TextUtils.isEmpty(mBinding.etInputNewPhone.getText().toString())) {
                showError(getString(R.string.sharemall_address_add_input_phone));
                return;
            }
            if (!Strings.isPhone(mBinding.etInputNewPhone.getText().toString())) {
                showError(getString(R.string.sharemall_address_add_input_phone));
                return;
            }
            if (TextUtils.isEmpty(mBinding.etInputVerificationCode.getText().toString())) {
                showError(getString(R.string.sharemall_input_verification_code));
                return;
            }
            doChangePhone();
        }

        super.doClick(view);
    }

    private void doChangePhone() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doChangePhone(mBinding.etInputNewPhone.getText().toString(),
                        mBinding.etInputVerificationCode.getText().toString())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    public void onNext(BaseData baseData) {
                        if (baseData.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            showError(getString(R.string.sharemall_operation_success));
                            UserInfoCache.get().setPhone(mBinding.etInputNewPhone.getText().toString());
                            finish();
                        } else {
                            showError(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }
}
