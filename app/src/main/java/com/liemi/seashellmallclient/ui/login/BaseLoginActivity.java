package com.liemi.seashellmallclient.ui.login;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.WXUserEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.mine.setting.SettingActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.LoginInfo;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录注册基类，用于将登录注册需要用到的接口放在这里请求
 */
public abstract class BaseLoginActivity<T extends ViewDataBinding> extends BaseImageCodeActivity<T> implements PlatformActionListener {

    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this, true);
    }

    @Override
    protected String getPhone() {
        return null;
    }

    @Override
    protected String getAuthType() {
        return null;
    }

    //跳转首页
    protected void toHomePage(ShareMallUserInfoEntity userInfoEntity) {
        //用户token信息
        AccessTokenCache.put(userInfoEntity.getToken());
        //保存用户信息
        UserInfoCache.put(userInfoEntity);
        JumpUtil.overlay(getContext(), MainActivity.class);
        AppManager.getInstance().finishAllActivity(MainActivity.class);
    }

    //请求微信授权
    protected void requestWechatAuth() {
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        if (!platform.isClientValid()) {
            showError(getString(R.string.sharemall_login_no_wechat_client));
            return;
        }
        showProgress("");
        platform.setPlatformActionListener(this);
        platform.removeAccount(false);
        platform.authorize();
    }

    @Override
    public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
        if (!TextUtils.isEmpty(platform.getDb().getUserId())) {
            String wxUserInfo = platform.getDb().exportData();
            String unionId = null;
            if (!Strings.isEmpty(wxUserInfo)) {
                WXUserEntity entity = new Gson().fromJson(wxUserInfo, WXUserEntity.class);
                if (entity != null && !Strings.isEmpty(entity.getUnionid())) {
                    unionId = entity.getUnionid();
                }
            }

            //缓存微信头像和昵称
            UserInfoCache.get().setHead_url(platform.getDb().getUserIcon());
            UserInfoCache.get().setNickname(platform.getDb().getUserName());

            //微信登录
            doLogin(null, null, null,platform.getDb().getUserId(), unionId, LoginParam.LOGIN_WECHAT);
        } else {
            showError(getString(R.string.sharemall_login_get_wechat_auth_error));
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        showError(getString(R.string.sharemall_login_get_wechat_auth_error));
    }

    @Override
    public void onCancel(Platform platform, int i) {
        showError(getString(R.string.sharemall_login_cancel_wechat_auth));
    }

    //接口返回后的判断
    private void toBindPhoneOrCode(ShareMallUserInfoEntity userInfo) {
        //保存用户信息
        UserInfoCache.put(userInfo);
        //用户没有绑定手机号，首先去绑定手机号
        if (userInfo.getIs_bind_phone() != ShareMallUserInfoEntity.BIND) {
            //跳转到绑定手机号页面
            JumpUtil.overlay(getContext(), BindPhoneActivity.class);
            return;
        }
        //用户绑定的手机号是否为新手机号，新手机号则跳转到绑定邀请码页面
       /* if (true){
            JumpUtil.overlay(getContext(),BindInviteCodeActivity.class);
            return;
        }*/

        //查看用户是否绑定邀请码
        /*if (ShareMallParam.DISTRIBUTOR_OPEN && userInfo.getIs_invited() != ShareMallUserInfoEntity.BIND) {
            JumpUtil.overlay(getContext(), FillInvitationCodeActivity.class);
            return;
        }*/

        toHomePage(userInfo);
    }

    //请求注册协议
    protected void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<AgreementEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<AgreementEntity> data) {
                        if (dataExist(data)) {
                           /* BaseWebviewActivity.start(getContext(),
                                    data.getData().getTitle(),
                                    data.getData().getUrl(), null);*/
                            Intent intent = new Intent(getContext(), BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, data.getData().getTitle());
                            if (data.getData().getLink_type()==2) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, data.getData().getContent());
                            startActivity(intent);
                        }
                    }
                });
    }

    //登录
    protected void doLogin(final String phone, final String password,final String code, final String openId, final String unionId, final String scenario) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doLogin(phone, password, phone, code, openId, unionId, scenario)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<ShareMallUserInfoEntity>>() {

                    @Override
                    public void onSuccess(BaseData<ShareMallUserInfoEntity> data) {
                        hideProgress();

                        //登录成功之后保存用户
                        if (!TextUtils.isEmpty(openId)) {
                            LoginInfo loginInfo = new LoginInfo(openId);
                            loginInfo.setUnionId(unionId);
                            LoginInfoCache.put(loginInfo);
                        }

                        if (!TextUtils.isEmpty(phone)) {
                            LoginInfoCache.put(new LoginInfo(phone, null));
                        }

                        toBindPhoneOrCode(data.getData());
                    }

                    @Override
                    public void onFail(BaseData<ShareMallUserInfoEntity> data) {
                        //用户没有注册微信
                        if (data.getErrcode() == 50001) {
                            doWechatRegister(openId, unionId);
                        } else {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }

    //微信注册
    protected void doWechatRegister(final String openId, final String unionId) {
        RetrofitApiFactory.createApi(LoginApi.class)
                .doRegister(null,
                        null, null,
                        null, null, openId, unionId,
                        "register_wechat")
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<ShareMallUserInfoEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<ShareMallUserInfoEntity> data) {
                        LoginInfo loginInfo = new LoginInfo(openId);
                        loginInfo.setUnionId(unionId);
                        //微信注册成功保存openId
                        LoginInfoCache.put(loginInfo);

                        //取出微信授权时获得的头像和昵称
                        String headUrl = UserInfoCache.get().getHead_url(),
                                nickname = UserInfoCache.get().getNickname();

                        if (!TextUtils.isEmpty(headUrl) || !TextUtils.isEmpty(nickname)) {
                            //保存用户信息
                            UserInfoCache.put(data.getData());
                            //更新信息
                            doUpdateUserInfo(headUrl, nickname);
                        } else {
                            toBindPhoneOrCode(data.getData());
                        }
                    }
                });
    }

    //请求修改个人信息
    protected void doUpdateUserInfo(final String headImage, final String nickName) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .initUserInfo(UserInfoCache.get().getToken().getToken(), headImage, nickName)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        ShareMallUserInfoCache.get().setHead_url(headImage);
                        ShareMallUserInfoCache.get().setNickname(nickName);
                        toBindPhoneOrCode(ShareMallUserInfoCache.get());
                    }
                });

    }

    //请求绑定手机号
    protected void doBindPhone(String phone, String authCode, String token) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doBindPhone(phone, authCode, token)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<ShareMallUserInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<ShareMallUserInfoEntity> data) {
                        toBindPhoneOrCode(data.getData());
                    }
                });
    }

    //请求绑定邀请码
    protected void doBindInviteCode(String code){
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doBindInvitationCode(code)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("绑定成功");
                        toBindPhoneOrCode(ShareMallUserInfoCache.get());
                    }
                });
    }

}
