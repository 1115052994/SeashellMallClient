package com.netmi.baselibrary.data.cache;


import com.netmi.baselibrary.data.entity.LoginInfo;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:53
 * 修改备注：
 */
public class LoginInfoCache {

    /**
     * 缓存用户信息
     */
    private static LoginInfo loginInfo;
    /**
     * 用户账号
     */
    public static final String LOGIN = "login";

    /**
     * 用户密码
     */
    public static final String PASSWORD = "password";

    /**
     * 微信登录
     */
    public static final String OPENID = "openid";

    public static final String UNIONID = "unionid";

    /**
     * 保存登陆用户信息
     *
     * @param loginInfo
     */
    public static void put(LoginInfo loginInfo) {

        PrefCache.putData(LOGIN, loginInfo.getLogin());
        PrefCache.putData(PASSWORD, loginInfo.getPassword());
        PrefCache.putData(OPENID, loginInfo.getOpenid());
        PrefCache.putData(UNIONID, loginInfo.getUnionId());
        LoginInfoCache.loginInfo = loginInfo;
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static LoginInfo get() {
        if(loginInfo == null) {
            loginInfo = new LoginInfo();
            loginInfo.setLogin((String) PrefCache.getData(LOGIN, ""));
            loginInfo.setPassword((String) PrefCache.getData(PASSWORD, ""));
            loginInfo.setOpenid((String) PrefCache.getData(OPENID, ""));
            loginInfo.setUnionId((String) PrefCache.getData(UNIONID, ""));
        }
        return loginInfo;

    }

    public static void clear() {

        PrefCache.removeData(PASSWORD);
        PrefCache.removeData(OPENID);
        PrefCache.removeData(UNIONID);
        loginInfo = null;
    }
}
