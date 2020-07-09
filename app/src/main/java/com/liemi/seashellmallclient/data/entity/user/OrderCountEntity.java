package com.liemi.seashellmallclient.data.entity.user;

import com.netmi.baselibrary.utils.Strings;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/8 12:08
 * 修改备注：
 */
public class OrderCountEntity {

    //设置邮费
    private String meail_num;
    //代付款
    private String obligation_num;
    //代发货
    private String sendgoods_num;
    //待收货
    private String getgoods_num;
    //带评价
    private String assess_num;
    //退款退货
    private String refund_num;
    private String waiting_used;

    public String getWaiting_used() {
        if(Strings.toInt(waiting_used) > 0) {
            return waiting_used;
        }
        return null;
    }

    public void setWaiting_used(String waiting_used) {
        this.waiting_used = waiting_used;
    }

    public String getMeail_num() {
        return meail_num;
    }

    public void setMeail_num(String meail_num) {
        this.meail_num = meail_num;
    }

    public String getObligation_num() {
        if(Strings.toInt(obligation_num) > 0){
            return obligation_num;
        }
        return null;
    }

    public void setObligation_num(String obligation_num) {
        this.obligation_num = obligation_num;
    }

    public String getSendgoods_num() {
        if(Strings.toInt(sendgoods_num) > 0){
            return sendgoods_num;
        }
        return null;
    }

    public void setSendgoods_num(String sendgoods_num) {
        this.sendgoods_num = sendgoods_num;
    }

    public String getGetgoods_num() {
        if(Strings.toInt(getgoods_num) > 0) {
            return getgoods_num;
        }
        return null;
    }

    public void setGetgoods_num(String getgoods_num) {
        this.getgoods_num = getgoods_num;
    }

    public String getAssess_num() {
        if(Strings.toInt(assess_num) > 0) {
            return assess_num;
        }
        return null;
    }

    public void setAssess_num(String assess_num) {
        this.assess_num = assess_num;
    }

    public String getRefund_num() {
        if(Strings.toInt(refund_num) > 0) {
            return refund_num;
        }
        return null;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }
}

