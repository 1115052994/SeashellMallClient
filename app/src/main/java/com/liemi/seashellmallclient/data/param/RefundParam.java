package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/17
 * 修改备注：
 */
public interface RefundParam {


    //申请退款实体
    String SKU_ENTITY = "skuEntity";
    //修改申请
    String REFUND_ENTITY = "refundEntity";

    //退款类型
    String REFUND_APPLY_FOR_TYPE = "refundApplyForType";

    //仅退款
    int REFUND_TYPE_ONLY_CASH = 1;
    //退货退款
    int REFUND_TYPE_GOODS_AND_CASH = 2;

    //发起退款
    int REFUND_STATUS_LAUNCH = 1;
    //拒绝退款
    int REFUND_STATUS_REFUSE = 3;


    //发起退货退款
    int REFUND_GOODS_STATUS_LAUNCH = 1;
    //卖家同意退货
    int REFUND_GOODS_STATUS_AGREE = 2;
    //已填写物流单号，等待卖家审核
    int REFUND_GOODS_STATUS_WAIT_AUDIT = 3;
    //卖家拒绝
    int REFUND_GOODS_STATUS_REFUSE = 4;


}
