package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 15:11
 * 修改备注：
 */
public interface StoreApi {

    /**
     * 店铺列表获取
     * is_recommend	是	int	是否为推荐店铺 0：否 1：是 不填则为全部
     */
    @FormUrlEncoded
    @POST("shop/shop-api/index")
    Observable<BaseData<PageEntity<StoreEntity>>> listStore(@Field("start_page") int start_page,
                                                            @Field("pages") int pages,
                                                            @Field("is_recommend") String is_recommend,
                                                            @Field("key_word") String key_word);

    /**
     * 收藏店铺
     */
    @FormUrlEncoded
    @POST("shop/shop-api/shop-collection")
    Observable<BaseData> shopCollection(@Field("shop_id") String shop_id);

    /**
     * 删除店铺
     */
    @FormUrlEncoded
    @POST("shop/shop-api/delete-collection")
    Observable<BaseData> shopCollectionDel(@Field("shop_id[]") List<String> shop_ids);


    /**
     * 购物车列表
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/index")
    Observable<BaseData<PageEntity<ShopCartEntity>>> listShopCart(@Field("param") String param,
                                                                  @Field("start_page") int start_page,
                                                                  @Field("pages") int pages);


    /**
     * 删除购物车
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/delete")
    Observable<BaseData> shopCartDel(@Field("cart_ids[]") List<String> cart_ids);

    /**
     * 更新购物车数量
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/update")
    Observable<BaseData> shopCartUpdate(@Field("cart_id") String cart_id,
                                        @Field("num") String num);

    /**
     * 店铺详情获取
     */
    @FormUrlEncoded
    @POST("shop/shop-api/view")
    Observable<BaseData<StoreEntity>> getStoreDetail(@Field("shop_id") String id);


    /**
     * 商品规格列表接口
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-property")
    Observable<BaseData<List<SpecsEntity>>> listGoodsSpecs(@Field("item_id") String item_id);

    /**
     * 获取商品全部规格组合
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-all-property")
    Observable<BaseData<List<SpecsGroupEntity>>> listSpecsGroup(@Field("item_id") String item_id);


    /**
     * 加入购物车
     */
    @FormUrlEncoded
    @POST("item/me-cart-api/create")
    Observable<BaseData> addShopCart(@Field("ivid") String ivid,
                                     @Field("num") String num);

    /**
     * 获取订单信息
     */
    @FormUrlEncoded
    @POST("/pay/pay-api/main-reset-order")
    Observable<BaseData<OrderPayEntity>> doGetPayEntity(@Field("main_order_id") String main_order_id);

}
