package com.liemi.seashellmallclient.data.entity.contacts;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * Created by Bingo on 2019/1/7.
 */

public class RecentContactEntity extends BaseEntity {
    /**
     * "title": "的",
     "type": "1",////1：系统公告；3商学院；4用户资产
     "create_time": "2018-12-28 10:43:42",
     "logo_url": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/AFWZNTH012345678_1546674669.png",
     "name": "最新公告",
     "all_no_readnum": 0
     */

    public static final int MESSAGE_TYPE_SYSTEM = 1;

    public static final int MESSAGE_TYPE_NOTICE = 2;

    public static final int MESSAGE_TYPE_COLLEGE = 3;

    public static final int MESSAGE_TYPE_ASSET = 4;

    //智齿客服消息列表
    public static final int MESSAGE_TYPE_SOBOT = 5;

    private String title;
    //1：系统公告； 2系统通知； 3商学院；4用户资产
    private int type;
    private String create_time;
    private String logo_url;
    private String name;
    private int all_no_readnum;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAll_no_readnum() {
        return all_no_readnum;
    }

    public void setAll_no_readnum(int all_no_readnum) {
        this.all_no_readnum = all_no_readnum;
    }
}
