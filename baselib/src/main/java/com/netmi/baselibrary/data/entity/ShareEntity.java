package com.netmi.baselibrary.data.entity;

import android.app.Activity;
import android.support.annotation.DrawableRes;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/7/25 21:20
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ShareEntity {

    /**
     * 分享的界面
     */
    private Activity activity;
    /**
     * 分享图片的url
     */
    private String imgUrl;

    private Integer imgRes;
    /**
     * 标题
     */
    private String title = "猎米网络";
    /**
     * 内容
     */
    private String content = "猎米网络";
    /**
     * 链接
     */
    private String linkUrl;

    public Integer getImgRes() {
        return imgRes;
    }

    public void setImgRes(@DrawableRes Integer imgRes) {
        this.imgRes = imgRes;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
