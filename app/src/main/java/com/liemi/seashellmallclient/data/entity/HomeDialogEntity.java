package com.liemi.seashellmallclient.data.entity;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class HomeDialogEntity extends BaseEntity {

    /**
     * img_url : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/FWXZMNTHK0125678_1546930623.png
     * position : 3
     * info : 218
     */

    private String img_url;
    private int position;
    private String info;
    private String content;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
