package com.netmi.baselibrary.data.cache;


import com.netmi.baselibrary.data.entity.AppConfigEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/4/18 17:57
 * 修改备注：
 */
public class AppConfigCache {

    private static final String APP_CONFIG = "appConfig";

    private static final String APP_CONFIG_PUSH = "appConfigPush";

    /**
     * 缓存APP配置信息
     */
    private AppConfigEntity appConfigEntity;


    private AppConfigCache() {
        getAppConfigEntity();
    }

    public static AppConfigCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final AppConfigCache instance = new AppConfigCache();
    }


    public static void put(AppConfigEntity entity) {
        getInstance().setAppConfigEntity(entity);
    }

    public static AppConfigEntity get() {
        return getInstance().getAppConfigEntity();
    }

    public static void setStatus(int status) {
        get().setStatus(status);
        PrefCache.putData(APP_CONFIG_PUSH, status);
    }

    private AppConfigEntity getAppConfigEntity() {
        if (appConfigEntity == null) {
            appConfigEntity = new AppConfigEntity();
            appConfigEntity.setStatus((Integer) PrefCache.getData(APP_CONFIG_PUSH, 0));
        }
        return appConfigEntity;
    }

    private void setAppConfigEntity(AppConfigEntity appConfigEntity) {
        this.appConfigEntity = appConfigEntity;
    }

    public static void clear() {
        getInstance().setAppConfigEntity(null);
    }

}
