package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：优惠券接口
 * 创建人：Simple
 * 创建时间：2019/3/19
 * 修改备注：
 */
public interface CouponApi {

    /**
     * 可用优惠券列表(店铺/商品详情页/平台)
     *
     * @param scenario 场景，商品详情页：item,   店铺：shop,   平台：platform
     */
    @FormUrlEncoded
    @POST("coupon/coupon-templet-api/item-index")
    Observable<BaseData<PageEntity<CouponEntity>>> listCoupon(@Field("scenario") String scenario,
                                                              @Field("shop_id") String shop_id,
                                                              @Field("item_id") String item_id,
                                                              @Field("start_page") int startPage,
                                                              @Field("pages") int pages);


    /**
     * 领取优惠券
     */
    @FormUrlEncoded
    @POST("coupon/coupon-templet-api/receive")
    Observable<BaseData> getCoupon(@Field("ctid") String ctid);


    /**
     * 每日弹窗优惠券列表
     */
    @FormUrlEncoded
    @POST("coupon/coupon-templet-api/daily-pop")
    Observable<BaseData<PageEntity<CouponEntity>>> getCouponDialog(@Field("param") String param);

}
