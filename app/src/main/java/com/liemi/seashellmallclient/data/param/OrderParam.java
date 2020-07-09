package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface OrderParam {

    /**
     * 订单状态,0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败
     */
    String ORDER_STATE = "order_state";
    String ORDER_MPID = "order_mpid";
    int ORDER_WAIT_PAY = 0;
    int ORDER_WAIT_SEND = 1;
    int ORDER_WAIT_RECEIVE = 2;
    int ORDER_WAIT_COMMENT = 3;
    int ORDER_SUCCESS = 9;
    int ORDER_REFUND_ASK = 4;
    int ORDER_REFUND_NOT_ALLOW = 5;
    int ORDER_REFUND_NOW = 6;
    int ORDER_REFUND_SUCCESS = 7;
    int ORDER_CANCEL = 8;
    int ORDER_PAY_FAIL = 10;

    /*
     * 核销状态 0待付款；1待核销；3待评价；9完成；7已退款
     * */
     int VERIFICATION_WAIT_PAY = 0;
     int VERIFICATION_WAIT_USE = 1;
     int VERIFICATION_WAIT_COMMENT = 3;
     int VERIFICATION_SUCCESS = 9;
     int VERIFICATION_REFUND_SUCCESS = 7;


    /**
     * 订单业务,1： order_remind： 提醒发货 2：order_delete:删除订单, 3：order_cancel：取消订单, 4：order_take：确认收货, 5：order_refund：申请退款
     */
    String ORDER_DO_REMIND = "order_remind";
    String ORDER_DO_DELETE = "order_delete";
    String ORDER_DO_CANCEL = "order_cancel";
    String ORDER_DO_ACCEPT = "order_take";
    String ORDER_DO_REFUND = "order_refund";


    //发票选择
    int REQUEST_INVOICE = 0x1123;
    //地址选择
    int REQUEST_ADDRESS = 0x1125;

    //发票
    String INVOICE_ENTITY = "invoice_entity";
    //订单支付
    String ORDER_PAY_ENTITY = "orderPayEntity";

    //0-微信支付
    int PAY_CHANNEL_WECHAT = 0;

    //1-支付宝支付
    int PAY_CHANNEL_ALIPAY = 1;

    //10-余额支付
    int PAY_CHANNEL_BALANCE = 10;

}
