package com.netmi.baselibrary.data.entity;

import android.support.annotation.DrawableRes;

public class EmptyLayoutEntity extends BaseEntity {
    private Integer resourceId;
    private String tip;

    public EmptyLayoutEntity() {
    }

    public EmptyLayoutEntity(String tip) {
        this.tip = tip;
    }

    public EmptyLayoutEntity(@DrawableRes Integer resourceId) {
        this.resourceId = resourceId;
    }

    public EmptyLayoutEntity(@DrawableRes Integer resourceId, String tip) {
        this.resourceId = resourceId;
        this.tip = tip;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(@DrawableRes Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
