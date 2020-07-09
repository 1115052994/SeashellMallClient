package com.netmi.baselibrary.ui;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.mob.MobSDK;
import com.netmi.baselibrary.data.base.Aes128CdcUtils;
import com.netmi.baselibrary.data.base.AesGsonConverterFactory;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.listener.LogoutListener;
import com.netmi.baselibrary.utils.AppManager;
import com.tencent.bugly.Bugly;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/4 14:33
 * 修改备注：
 */
public abstract class MApplication extends Application {

    private static MApplication instance;

    /**
     * App Activity 自定义栈管理
     */
    public AppManager appManager;

    /**
     * 退出的监听
     */
    public LogoutListener logoutListener;

    /**
     * 个推通知栏弹出的应用logo
     */
    protected abstract int getPushIcon();

    public MApplication() {
        super();
        instance = this;
    }

    public static MApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException();
        return instance;
    }

    public static Context getAppContext() {
        if (instance == null)
            throw new IllegalStateException();
        return instance.getApplicationContext();
    }


    /**
     * 获取Bugly的appId
     */
    protected abstract String getBuglyAppId();

    /**
     * 数据加解密的秘钥，由后台提供，返回null不加密
     */
    protected abstract String getAesKey();

    @Override
    public void onCreate() {
        super.onCreate();
        appManager = AppManager.getInstance();
        appManager.init(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            closeAndroidPDialog();
        }

        //Mob第三方分享
        MobSDK.init(this);

        //前往bugly平台申请APP_ID，并配置
        Bugly.init(getApplicationContext(), getBuglyAppId(), true);

        AesGsonConverterFactory.ENCRYPTION = !TextUtils.isEmpty(getAesKey());

        Aes128CdcUtils.init(getAesKey());
    }

    /**
     * 检查用户是否登录
     */
    public boolean checkUserIsLogin() {
        if (TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            gotoLogin();
            return false;
        }
        return true;
    }

    /**
     * 退出当前登录，跳转到登录界面
     */
    public void gotoLogin() {
        if (logoutListener != null) {
            logoutListener.logout();
        }

        //清除本地用户数据
        LoginInfoCache.clear();
        AccessTokenCache.clear();
        UserInfoCache.clear();

        //转到登录界面
        if (logoutListener != null) {
            logoutListener.login();
        }
    }

    /**
     * 返回首页
     */
    public void backHome() {

    }

    /**
     * Android P限制了开发者调用非官方公开API方法或接口
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}