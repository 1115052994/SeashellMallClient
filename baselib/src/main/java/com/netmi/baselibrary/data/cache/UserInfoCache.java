package com.netmi.baselibrary.data.cache;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.utils.Strings;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/1 10:05
 * 修改备注：
 */
public class UserInfoCache {

    /**
     * 缓存用户信息
     */
    private static UserInfoEntity userInfoEntity;

    private static final String USER_INFO = "user_info";

    /**
     * 保存登陆用户信息
     *
     * @param entity
     */
    public static void put(UserInfoEntity entity) {
        PrefCache.putData(USER_INFO, new Gson().toJson(entity));
        UserInfoCache.userInfoEntity = entity;
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static UserInfoEntity get() {
        if (userInfoEntity == null) {
            userInfoEntity = get(UserInfoEntity.class);
        }
        return userInfoEntity;
    }

    public static <T extends UserInfoEntity> T get(Class<T> classOfT) {
        if (userInfoEntity == null || !TextUtils.equals(userInfoEntity.getClass().getName(), classOfT.getName())) {
            String userInfo = ((String) PrefCache.getData(USER_INFO, ""));
            if (!Strings.isEmpty(userInfo)) {
                userInfoEntity = new Gson().fromJson(userInfo, classOfT);
            }

            if (userInfoEntity == null) {
                try {
                    userInfoEntity = (T) Class.forName(classOfT.getName()).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return (T) userInfoEntity;
    }


    public static void clear() {
        PrefCache.removeData(USER_INFO);
        userInfoEntity = null;
    }
}
