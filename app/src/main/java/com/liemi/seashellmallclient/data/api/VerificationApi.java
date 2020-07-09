package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationCommentEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDelEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.*;

public interface VerificationApi {
    /*
    * 订单列表
    * */
    @FormUrlEncoded
    @POST("offline/local-order-api/list")
    Observable<BaseData<PageEntity<VerificationOrderDetailEntity>>> getVerificationOrderList(@Field("start_page") int start_page,
                                                                                             @Field("pages") int pages,
                                                                                             @Field("status") String status);
    /*
    * 订单详情
    * */
    @FormUrlEncoded
    @POST("offline/local-order-api/info")
    Observable<BaseData<VerificationOrderDetailEntity>> getVerificationOrderDetail(@Field("order_id") String order_id);

    /*
    * 删除订单
    * */
    @FormUrlEncoded
    @POST("offline/local-order-api/delete")
    Observable<BaseData> doDelVerificationOrder(@Field("order_id") String order_id);

    /*
     * 核销代金券
     * */
    @FormUrlEncoded
    @POST("offline/local-order-api/used")
    Observable<BaseData<VerificationOrderDelEntity>> doUseVerificationCode(@Field("code") String code);

    /*
    * 门店买单
    * */
    @FormUrlEncoded
    @POST("offline/local-order-api/buy")
    Observable<BaseData<OrderPayEntity>> doBuy(@Field("amount") String amount,
                                               @Field("shop_id") String shop_id);

    /*
    * 订单评价
    * */
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("offline/local-order-api/evaluate")
    Observable<BaseData> doComment(@Body VerificationCommentEntity commentEntity);
}
