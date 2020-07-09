package com.liemi.seashellmallclient.ui.mine.setting;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivitySettingBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.CacheUtils;
import com.netmi.baselibrary.utils.CleanUtils;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    //用户退出登录的提示
    private AddressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_setting));
        mBinding.tvVersion.setText(("V" + AppUtils.getAppVersionName()));
        mBinding.tvVersionTips.setVisibility(Beta.getUpgradeInfo() != null ? View.VISIBLE : View.GONE);
        mBinding.tvFu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mBinding.tvFu.getPaint().setAntiAlias(true);//抗锯齿
        mBinding.tvYin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mBinding.tvYin.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(),true);
    }

    @Override
    protected void initData() {
        //从本地缓存获取用户信息
        if (UserInfoCache.get(ShareMallUserInfoEntity.class) == null) {
            showError(getString(R.string.sharemall_lack_info));
            finish();
        }
        mBinding.setUserInfo(UserInfoCache.get(ShareMallUserInfoEntity.class));
        //设置缓存大小
        setCacheSize();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            //共享元素动画的退出
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_suggestion_feedback) {
            //点击意见反馈
            JumpUtil.overlay(this, SuggestionFeedbackActivity.class);
            return;
        }
        if (view.getId() == R.id.tv_about_us) {
            //点击关于我们
            doAgreement(LoginParam.PROTOCOL_TYPE_ABOUT_US);
            return;
        }
        if (view.getId() == R.id.ll_clear_cache) {
            //点击清除缓存
            if (CleanUtils.cleanInternalCache() && CleanUtils.cleanExternalCache()) {
                showError(getString(R.string.sharemall_operation_success));
                mBinding.setCacheNum("0B");
            } else {
                showError(getString(R.string.sharemall_cache_clear_fail));
            }
            return;
        }

        if (view.getId() == R.id.ll_version) {
            //点击版本更新
            Beta.checkUpgrade();
            return;
        }

        if (view.getId() == R.id.tv_exit_account) {
            //点击退出账号
            showLogoutDialog();
            return;
        }

        if (view.getId() == R.id.tv_sevice || view.getId() == R.id.tv_fu){
            doAgreement(LoginParam.PROTOCOL_TYPE_SERVICE);
            return;
        }
        if (view.getId() == R.id.tv_privatel || view.getId() == R.id.tv_yin){
            doAgreement(LoginParam.PROTOCOL_TYPE_USER_PRIVACY);
            return;
        }
    }

    private void setCacheSize() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> ex) {
                try {
                    ex.onNext(CacheUtils.getInstance().getTotalCacheSize(getContext()));
                    ex.onComplete();
                } catch (Exception e) {
                    Logs.i("error:" + e.getMessage());
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Logs.i("获取到缓存大小：" + s);
                        if (!TextUtils.isEmpty(s)) {
                            mBinding.setCacheNum(s);
                        } else {
                            mBinding.setCacheNum("0B");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    //显示询问用户是否退出登录
    private void showLogoutDialog() {
        if (mDialog == null) {
            mDialog = new AddressDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        mDialog.setTitle(getString(R.string.sharemall_hint2));
        mDialog.setMessage(getString(R.string.sharemall_confirm_exit_account));
        mDialog.setConfirm(getString(R.string.sharemall_confirm_ok));
        mDialog.setClickConfirmListener(new AddressDialog.ClickConfirmListener() {
            @Override
            public void clickConfirm() {
                MApplication.getInstance().gotoLogin();
                finish();
            }
        });

    }

    //请求服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent(SettingActivity.this, BaseWebviewActivity.class);
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
