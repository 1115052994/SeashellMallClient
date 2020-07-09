package com.liemi.seashellmallclient.utils;


import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.SPs;

import static com.netmi.baselibrary.data.Constant.IS_PUSH;

/*
 * 个推注册和取消注册
 * */
public class PushUtils {
    private static String TAG = "PushUtils";

    //登录成功个推绑定别名
    public static void bindPush() {
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getUid())) {
            PushManager.getInstance().bindAlias(MApplication.getAppContext(), Constant.PUSH_PREFIX + AccessTokenCache.get().getUid(),
                    PushManager.getInstance().getClientid(MApplication.getAppContext()));
            Log.e(TAG, "onReceiveClientId -> " + "aliadid = " + Constant.PUSH_PREFIX + AccessTokenCache.get().getUid());
        }
        SPs.put(MApplication.getAppContext(), IS_PUSH, false);
    }

    //个推取消绑定别名
    public static void unbindPush() {
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getUid())) {
            PushManager.getInstance().unBindAlias(MApplication.getAppContext(), Constant.PUSH_PREFIX + AccessTokenCache.get().getUid(), true,
                    PushManager.getInstance().getClientid(MApplication.getAppContext()));
        }
        SPs.put(MApplication.getAppContext(), IS_PUSH, true);
    }

}
