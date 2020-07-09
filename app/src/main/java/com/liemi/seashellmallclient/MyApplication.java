package com.liemi.seashellmallclient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;


import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.liemi.seashellmallclient.data.cache.VipUserInfoCache;
import com.liemi.seashellmallclient.data.param.ShareMallParam;
import com.liemi.seashellmallclient.service.AppPushService;
import com.liemi.seashellmallclient.service.XyPushIntentService;
import com.liemi.seashellmallclient.ui.login.LoginHomeActivity;
import com.liemi.seashellmallclient.ui.login.LoginPhoneActivity;
import com.liemi.seashellmallclient.ui.login.PwdLoginActivity;
import com.liemi.seashellmallclient.utils.PushUtils;
import com.netmi.baselibrary.BuildConfig;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.LoginInfo;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.data.listener.LogoutListener;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;

import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.sobot.chat.SobotApi;

import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

import org.greenrobot.eventbus.EventBus;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

import static com.netmi.baselibrary.data.Constant.PUSH_PREFIX;

public class MyApplication extends MApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        //监听退出登录
        logoutListener = new LogoutListener() {
            @Override
            public void logout() {
                // 清理缓存&注销监听&清除状态
                VipUserInfoCache.getInstance().clear();
                //注销个推别名
                PushUtils.unbindPush();
                SobotApi.exitSobotChat(getApplicationContext());
            }

            @Override
            public void login() {
                if (!AppManager.getInstance().existActivity(LoginPhoneActivity.class)) {
                    Intent intent = new Intent(getAppContext(), LoginPhoneActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        SobotApi.initSobotSDK(this, ShareMallParam.SOBOT_KEY, UserInfoCache.get().getUid());

        //个推初始化
        getPushIcon();
        PushManager.getInstance().initialize(getApplicationContext(), AppPushService.class);
        // XyPushIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), XyPushIntentService.class);

        TangramBuilder.init(getApplicationContext(), new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                GlideShowImageUtils.gifload(getApplicationContext(), url, view);
            }
        }, ImageView.class);

        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
//                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .setSkinAllActivityEnable(false)                        //避免第三方库换肤
                .loadSkin();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void backHome() {
        EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_home));
        AppManager.getInstance().finishAllActivity(MainActivity.class);
    }

    @Override
    protected int getPushIcon() {
        //设置通知栏及通知栏顶部图标：
        // 为了修改默认的通知图标以及通知栏顶部提示小图标，
        // 请务必在资源目录的 res/drawable-ldpi/、res/drawable-mdpi/、res/drawable-hdpi/、res/drawable-xhdpi/、res/drawable-xxhdpi/ 等各分辨率目录下，
        // 放置相应尺寸的文件名为 push.png 和 push_small.png 的图
        return R.drawable.push_small;
    }

    @Override
    protected String getBuglyAppId() {
        return AppUtils.isDebug() ? "" : AppUtils.isRelease() ? "0dedf16733" : "";
    }

    @Override
    protected String getAesKey() {
        return AppUtils.isDebug() ? ""
                : "";//AppUtils.isRelease() ? "d3JzcFN0YUxueEJ3Q3BJZg==" : "TG03eVcwVnROT3RMejhuUA=="
    }
}
