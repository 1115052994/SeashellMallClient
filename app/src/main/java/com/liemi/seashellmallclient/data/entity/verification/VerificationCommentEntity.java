package com.liemi.seashellmallclient.data.entity.verification;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/10 10:34
 * 修改备注：
 */
public class VerificationCommentEntity {


    private int star = 0;
    private String order_id;
    private String comment;
    private List<String> img_url = new ArrayList<>();
    private String video_url;

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }
}
