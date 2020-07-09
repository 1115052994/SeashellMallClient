package com.liemi.seashellmallclient.data.entity.order;

/**
 * Created by Bingo on 2018/11/27.
 */

public class RefundPriceEntity {
    /**
     * “refund_price”: 3364,//退款金额
     * “postage”: “12.00”//邮费
     */

    private String refund_price;
    private String postage;
    private String pay_channel; //退款类型 4.以太币
    //1:退邮费 0：不退
    private int is_postage;

    public boolean hasPostage() {
        return is_postage == 1;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(String refund_price) {
        this.refund_price = refund_price;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public int getIs_postage() {
        return is_postage;
    }

    public void setIs_postage(int is_postage) {
        this.is_postage = is_postage;
    }
}
