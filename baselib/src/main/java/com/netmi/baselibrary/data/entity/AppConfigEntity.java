package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/24 16:48
 * 修改备注：
 */
public class AppConfigEntity {

    private PlatformEntity platformEntity;

    //推送消息
    private int status;

    private AppThemeEntity appThemeEntity;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PlatformEntity getPlatformEntity() {
        if (platformEntity == null) {
            platformEntity = new PlatformEntity();
        }
        return platformEntity;
    }

    public void setPlatformEntity(PlatformEntity platformEntity) {
        this.platformEntity = platformEntity;
    }

    public AppThemeEntity getAppTheme() {
        if (appThemeEntity == null) {
            appThemeEntity = new AppThemeEntity();
        }
        return appThemeEntity;
    }

    public void setAppTheme(AppThemeEntity appThemeEntity) {
        this.appThemeEntity = appThemeEntity;
    }
}
