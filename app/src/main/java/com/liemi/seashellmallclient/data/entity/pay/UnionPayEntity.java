package com.liemi.seashellmallclient.data.entity.pay;

public class UnionPayEntity {

    /**
     * "bizType": "000201",
     *         "txnSubType": "01",
     *         "orderId": "2019062413304847468587", //订单号
     *         "txnType": "01",
     *         "encoding": "utf-8",
     *         "version": "5.1.0",
     *         "accessType": "0",
     *         "txnTime": "20190624142306", //下单时间
     *         "respMsg": "成功[0000000]",
     *         "merId": "777290058110048", //商户id
     *         "tn": "673216666333016930200",  //所需Tn
     *         "signMethod": "01",
     *         "respCode": "00",
     */

    private String bizType;
    private String txnSubType;
    private String orderId;
    private String txnType;
    private String encoding;
    private String version;
    private String accessType;
    private String txnTime;
    private String respMsg;
    private String merId;
    private String tn;
    private String signMethod;
    private String respCode;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
}
