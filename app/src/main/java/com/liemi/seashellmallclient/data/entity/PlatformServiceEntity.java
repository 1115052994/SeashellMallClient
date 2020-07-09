package com.liemi.seashellmallclient.data.entity;

public class PlatformServiceEntity {
    @Deprecated
    private String token;   //智齿token，已废弃
    private String accid;

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    @Deprecated
    public String getToken() {
        return token;
    }

    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }
}
