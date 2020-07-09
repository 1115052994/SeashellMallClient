package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.InvoiceEntity;
import com.liemi.seashellmallclient.data.entity.comment.MuchCommentEntity;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.MycoinEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.entity.order.*;
import com.liemi.seashellmallclient.data.entity.user.IdCardEntity;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import io.reactivex.Observable;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by Bingo on 2019/1/2.
 */

public interface OrderApi {
    /**
     * 创建订单
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("order/order-api/create")
    Observable<BaseData<OrderPayEntity>> createOrder(@Body FillOrderEntity orderCommand);

    /**
     * 商品评论列表获取
     *
     * @param flag 是否有图 0：否 1：是
     */
    @FormUrlEncoded
    @POST("item/me-commet-api/index")
    Observable<BaseData<PageCommentEntity<CommentEntity>>> listComment(@Field("start_page") int start_page,
                                                                       @Field("pages") int pages,
                                                                       @Field("item_id") String item_id,
                                                                       @Field("flag") String flag);

    /**
     * 删除订单
     */
    @FormUrlEncoded
    @POST("order/order-api/main-delete")
    Observable<BaseData> delOrder(@Field("main_order_id") String orderId);

    /**
     * 提醒发货
     */
    @FormUrlEncoded
    @POST("order/order-api/remind")
    Observable<BaseData> remindOrder(@Field("order_id") String orderId);

    /**
     * 订单取消
     */
    @FormUrlEncoded
    @POST("order/order-api/main-cancel")
    Observable<BaseData> cancelOrder(@Field("main_order_id") String orderId);

    /**
     * 确认收货
     */
    @FormUrlEncoded
    @POST("order/order-api/confirm")
    Observable<BaseData> confirmReceipt(@Field("order_id") String orderId);

    /**
     * 支付宝支付接口
     *
     * @param pay_channel 支付渠道 支付渠道:0-微信支付1-支付宝 3-积分支付
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app")
    Observable<BaseData<String>> orderPayAli(@Field("order_id") String pay_order_no,
                                             @Field("channel") String pay_channel);

    /**
     * 微信支付接口
     */
    @FormUrlEncoded
    @POST("pay/pay-api/app")
    Observable<BaseData<WXPayData>> orderPayWechat(@Field("order_id") String pay_order_no,
                                                   @Field("channel") String pay_channel);

    /*
     * 钱包支付
     * */
    @FormUrlEncoded
    @POST("pay/pay-api/hai")
    Observable<BaseData<String>> orderPayHai(@Field("order_id") String pay_order_no,
                                             @Field("pay_password") String pay_password,
                                             @Field("channel") String pay_channel,
                                             @Field("type") String type);

    /**
     * 根据订单号获取支付信息
     */
    @FormUrlEncoded
    @POST("pay/pay-api/reset-order")
    Observable<BaseData<OrderPayEntity>> getOrderPayInfo(@Field("order_id") String order_id);

    /**
     * 物流数据获取
     */
    @FormUrlEncoded
    @POST("order/order-api/logistics-list")
    Observable<BaseData<List<LogisticEntity>>> getLogistic(@Field("order_no") String order_no);

    /**
     * 订单批量评论
     */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("item/me-commet-api/create")
    Observable<BaseData> submitComment(@Body MuchCommentEntity muchCommentEntity);

    /**
     * 退款列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-list")
    Observable<BaseData<PageEntity<RefundListEntity>>> listOrderRefund(@Field("start_page") int startPage,
                                                                       @Field("pages") int pages);


    /**
     * 平台订单订单详情获取
     */
    @FormUrlEncoded
    @POST("order/order-api/main-order-info")
    Observable<BaseData<OrderDetailedEntity>> getOrderDetailed(@Field("main_order_id") String main_order_id);


    /**
     * 平台订单列表
     */
    @FormUrlEncoded
    @POST("order/order-api/main-order-list")
    Observable<BaseData<PageEntity<OrderDetailedEntity>>> listAllOrder(@Field("start_page") int start_page,
                                                                       @Field("pages") int pages,
                                                                       @Field("status") Integer status);


    /**
     * 订单申请退款(未发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-back")
    Observable<BaseData> applyRefund(@Field("id") String orderId,
                                     @Field("name") String refundReason,
                                     @Field("remark") String refundRemark,
                                     @Field("price_total") String priceTotal,
                                     @Field("img_url[]") List<String> img_url);

    /**
     * 获取退款金额和邮费
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/postage")
    Observable<BaseData<RefundPriceEntity>> getRefundPrice(@Field("id") String orderId);

    /**
     * 订单申请退款(已发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     * img_url	否	string	退款图片数组
     * price_total	是	int	退款价格
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/add")
    Observable<BaseData> updateApplyRefundGood(@Field("id") String orderId,
                                               @Field("name") String refundReason,
                                               @Field("remark") String refundRemark,
                                               @Field("price_total") String priceTotal,
                                               @Field("img_urls[]") List<String> imgUrls,
                                               @Field("type") int type,
                                               @Field("state") String state);

    /**
     * 填写退货物流
     * mail_no	否	string	快递单号(refund_status为2时传)
     * mail_name	否	string	快递名称(refund_status为2时传)
     * mail_code	否	string	快递编号(refund_status为2时传)
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/goods-back")
    Observable<BaseData> applyRefundLogistic(@Field("id") String id,
                                             @Field("mail_no") String logisticsNo,
                                             @Field("mail_name") String logisticsCompany,
                                             @Field("mail_code") String companyCode,
                                             @Field("img_url[]") List<String> imgs,
                                             @Field("refund_status") int refundStatus);

    /**
     * 用户填写快递信息
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/agree")
    Observable<BaseData> updateApplyLogistic(@Field("id") String id,
                                             @Field("mail_no") String logisticsNo,
                                             @Field("mail_name") String logisticsCompany,
                                             @Field("mail_code") String companyCode,
                                             @Field("img_url[]") List<String> imgs);

    /**
     * 获取物流公司列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/delivery")
    Observable<BaseData<List<LogisticCompanyEntity>>> listLogisticCompany(@Field("param") int param);

    /**
     * 获取退款原因列表
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/run")
    Observable<BaseData<List<RefundReasonEntity>>> listRefundReason(@Field("param") int param);

    /**
     * 订单申请退款(已发货状态时退款)
     * id	是	int	子订单id
     * name	是	string	退款原因
     * remark	否	string	退款备注
     * img_url	否	string	退款图片数组
     * price_total	是	int	退款价格
     * refund_status	是	string	1等待商家审核2商家已审核，用户填写快递单号
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/goods-back")
    Observable<BaseData> applyRefundGood(@Field("id") String orderId,
                                         @Field("name") String refundReason,
                                         @Field("remark") String refundRemark,
                                         @Field("price_total") String priceTotal,
                                         @Field("img_url[]") List<String> imgUrls,
                                         @Field("state") int state);

    /**
     * 退款详情
     * refund_id	是	int	退款id	1
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/refund-details")
    Observable<BaseData<RefundDetailsEntity>> getRefundDetailed(@Field("refund_id") String refundId);


    /**
     * 退款取消申请
     * id	是	int	子订单id
     * type	是	string	1未发货状态时取消申请退款2已发货状态时取消申请退款
     */
    @FormUrlEncoded
    @POST("order/me-refund-api/cancel")
    Observable<BaseData> cancelRefundApply(@Field("id") String orderId,
                                           @Field("type") int type);

    /**
     * 获取优惠券和积分
     */
    @FormUrlEncoded
    @POST("/member/user-coin-api/get-my-coin")
    Observable<BaseData<MycoinEntity>> getMyCoin(@Field("param") String param);

    /**
     * 验证余额密码
     */
    @FormUrlEncoded
    @POST("member/user-api/check-pay-password")
    Observable<BaseData> checkPayPWD(@Field("password") String password);


    /**
     * 用户身份证接口
     */
    @FormUrlEncoded
    @POST("order/order-api/get-card")
    Observable<BaseData<IdCardEntity>> getIdCard(@Field("param") String param);

    /**
     * 获取发票信息
     */
    @FormUrlEncoded
    @POST("order/order-api/get-invoice-info")
    Observable<BaseData<InvoiceEntity>> getInvoice(@Field("param") String param);

    /**
     * 计算运费
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("order/order-api/express-fee")
    Observable<BaseData<List<ExpressFeeEntity>>> listExpressFee(@Body FillExpressFeeEntity param);

    /**
     * 判断商品是否在配送区域内
     */
    @FormUrlEncoded
    @POST("order/order-api/can-buy")
    Observable<BaseData<List<GoodsDeliveryEntity>>> listGoodsDelivery(@Field("address_id") String address_id,
                                                                      @Field("item_list[]") List<String> itemList);

    /*
     * 钱转换成海贝数量
     * */
    @FormUrlEncoded
    @POST("shop/shop-api/change")
    Observable<BaseData> getMoney(@Field("money") String money);

    /**
     * 创建订单获取优惠券
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("coupon/coupon-api/order-coupon-index")
    Observable<BaseData<PageEntity<MineCouponEntity>>> getFillOrderCoupon(@Body FillCouponEntity param);

    /*
     * 下单限额
     * */
    @FormUrlEncoded
    @POST("order/order-api/is-pay")
    Observable<BaseData<String>> isPay(@Field("shop_id[]") List<String> shop_id,
                                       @Field("type") String type,
                                       @Field("pay_order_no") String orderId,
                                       @Field("haibei_amount") String haibei_amount);

}
