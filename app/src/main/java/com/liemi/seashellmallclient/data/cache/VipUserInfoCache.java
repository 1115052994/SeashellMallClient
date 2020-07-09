package com.liemi.seashellmallclient.data.cache;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.liemi.seashellmallclient.data.entity.vip.VIPUserInfoEntity;
import com.netmi.baselibrary.data.cache.PrefCache;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/30
 * 修改备注：
 */
public class VipUserInfoCache {

    /**
     * 缓存Vip用户信息
     */
    private VIPUserInfoEntity userInfoEntity;

    private static final String VIP_USER_INFO = "vip_user_info";

    private volatile static VipUserInfoCache mSingleton = null;

    private VipUserInfoCache () {}

    public static VipUserInfoCache getInstance() {
        if (mSingleton == null) {
            synchronized (VipUserInfoCache.class) {
                if (mSingleton == null) {
                    mSingleton = new VipUserInfoCache();
                }
            }
        }
        return mSingleton;
    }

    /**
     * 保存登陆用户信息
     *
     * @param entity
     */
    public void put(VIPUserInfoEntity entity) {
        PrefCache.putData(VIP_USER_INFO, new Gson().toJson(entity));
        userInfoEntity = entity;
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public VIPUserInfoEntity get() {
        if (userInfoEntity == null) {
            String userInfo = ((String) PrefCache.getData(VIP_USER_INFO, ""));
            if(!TextUtils.isEmpty(userInfo)){
                userInfoEntity = new Gson().fromJson(userInfo, VIPUserInfoEntity.class);
            } else {
                userInfoEntity = new VIPUserInfoEntity();
            }
        }
        return userInfoEntity;
    }

    public void clear() {
        PrefCache.removeData(VIP_USER_INFO);
        userInfoEntity = null;
    }
}
