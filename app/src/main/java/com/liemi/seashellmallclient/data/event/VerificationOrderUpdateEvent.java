package com.liemi.seashellmallclient.data.event;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/8 21:06
 * 修改备注：
 */
public class VerificationOrderUpdateEvent {

    private String mpid;

    private int status;

    public VerificationOrderUpdateEvent(String mpid, int status){
        this.mpid = mpid;
        this.status = status;
    }

    public String getMpid() {
        return mpid;
    }

    public int getStatus() {
        return status;
    }

}
