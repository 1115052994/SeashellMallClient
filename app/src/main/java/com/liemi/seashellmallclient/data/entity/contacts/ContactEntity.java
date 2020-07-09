package com.liemi.seashellmallclient.data.entity.contacts;

/**
 * Created by Bingo on 2019/1/7.
 */

public class ContactEntity {
    /**
     * "nickname": "18856855449",//昵称
     "head_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15397899666447.png",//头像
     "role": "0",//角色标志  0：普通用户  1：推手
     "cid": "",//云信
     "uid": "921"//用户id
     */

    private String nickname;
    private String head_url;
    private int role;
    private String cid;
    private String uid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
