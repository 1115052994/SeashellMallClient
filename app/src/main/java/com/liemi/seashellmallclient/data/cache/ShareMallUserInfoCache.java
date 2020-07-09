package com.liemi.seashellmallclient.data.cache;

import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.netmi.baselibrary.data.cache.UserInfoCache;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/7/25
 * 修改备注：
 */
public class ShareMallUserInfoCache {

    /**
     * 获取登录用户信息
     *
     */
    public static ShareMallUserInfoEntity get() {
        return UserInfoCache.get(ShareMallUserInfoEntity.class);
    }

}
