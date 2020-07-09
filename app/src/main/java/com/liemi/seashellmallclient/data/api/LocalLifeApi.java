package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsDetailEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeShopEntity;
import com.liemi.seashellmallclient.data.entity.locallife.ShopOneCateEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

public interface LocalLifeApi {
    /*
     * 店铺列表
     * */
    @FormUrlEncoded
    @POST("offline/local-shop-api/index")
    Observable<BaseData<PageEntity<LocalLifeShopEntity>>> getShopList(@Field("start_page") int startPage,
                                                                      @Field("pages") int row,
                                                                      @Field("longitude") String longitude,
                                                                      @Field("latitude") String latitude,
                                                                      @Field("sort") String sort,
                                                                      @Field("sort_type") String sort_type,
                                                                      @Field("category_id") String category_id);

    /*
    * 店铺详情
    * */
    @FormUrlEncoded
    @POST("offline/local-shop-api/detail")
    Observable<BaseData<LocalLifeShopEntity>> getShopDetails(@Field("shop_id") String shop_id);

    /**
     * 评论列表获取
     *
     * @param flag 是否有图 0：否 1：是
     */
    @FormUrlEncoded
    @POST("offline/local-item-api/get-evaluate")
    Observable<BaseData<PageCommentEntity<CommentEntity>>> getCommentList(@Field("start_page") int start_page,
                                                                          @Field("pages") int pages,
                                                                          @Field("item_id") String item_id,
                                                                          @Field("shop_id") String shop_id,
                                                                          @Field("flag") String flag);

    /*
    * 商品列表
    * */
    @FormUrlEncoded
    @POST("offline/local-item-api/list")
    Observable<BaseData<PageEntity<LocalLifeGoodsEntity>>> getGoodsList(@Field("start_page") int start_page,
                                                                        @Field("pages") int pages,
                                                                        @Field("shop_id") String shop_id);

    /*
     * 商品详情
     * */
    @FormUrlEncoded
    @POST("offline/local-item-api/item-info")
    Observable<BaseData<LocalLifeGoodsDetailEntity>> getGoodsDetails(@Field("item_id") String item_id,
                                                                     @Field("longitude") String longitude,
                                                                     @Field("latitude") String latitude);

    /*
    * 店铺分类列表
    * */
    @FormUrlEncoded
    @POST("offline/local-shop-api/get-category")
    Observable<BaseData<List<ShopOneCateEntity>>> getShopCategory(@Field("defaultData") String defaultData);
}
