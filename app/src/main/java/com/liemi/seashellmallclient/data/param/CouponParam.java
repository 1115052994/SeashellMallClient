package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface CouponParam {

    //下单的优惠券列表选择
    int COUPON_TYPE_ALL = 0;
    //平台券
    int COUPON_TYPE_PLATFORM = 1;
    //店铺券
    int COUPON_TYPE_STORE = 2;
    //商品券
    int COUPON_TYPE_GOODS = 3;
    //品类券
    int COUPON_TYPE_CATEGORY = 4;


    //下单页优惠券选择框
    String CHOICE_COUPON = "choiceCoupon";
    String CONDITION_PRICE = "conditionPrice";
    String GOODS_LIST = "goodsList";

    //优惠券列表
    String COUPON_LIST = "couponList";


    //优惠券状态
    String COUPON_STATUS = "couponStatus";

    int COUPON_STATUS_NOT_USED = 1;//未使用
    int COUPON_STATUS_USED = 3;//已使用
    int COUPON_STATUS_TIMED = 2;//已过期
    int COUPON_STATUS_SHARE = 4;//分享被领取
    int COUPON_STATUS_NOT_USED_SHARING = 6;//未使用和分享中



}
