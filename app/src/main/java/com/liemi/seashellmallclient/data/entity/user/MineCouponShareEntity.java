package com.liemi.seashellmallclient.data.entity.user;

import java.io.Serializable;

/**
 * 分享优惠券回调的数据
 */
public class MineCouponShareEntity implements Serializable {
    private String post_url;
    private String share_id;

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }
}
