package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.category.GoodsOneCateEntity;
import com.liemi.seashellmallclient.data.entity.category.GoodsTwoCateEntity;
import com.liemi.seashellmallclient.data.entity.good.CommendGoodEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
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
 * 创建时间：2018/8/15 15:39
 * 修改备注：
 */
public interface CategoryApi {

    /**
     * 商品分类列表获取
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category")
    Observable<BaseData<List<GoodsOneCateEntity>>> listTotalCategory(@Field("param") String param);

    /**
     * 获取二级分类
     */
    @FormUrlEncoded
    @POST("item/me-category-api/get-category-list")
    Observable<BaseData<List<GoodsTwoCateEntity>>> getSecondCategory(@Field("shop_id") String shopId,
                                                                     @Field("pid") String pid);

    /**
     * 商品列表获取
     * mcid	是	int	分类主键
     *
     * @param sort_name 排序字段名称 is_new：新品排序,price：价格排序,deal_num：销量排序，
     *                  popularity 人气排序，commission佣金排序 不填为综合排序
     * @param sort_type 排序规则 SORT_DESC：逆序（从大到小） SORT_ASC：正序（从小到大）
     * @param item_type 商品类型 默认0:普通商品 1:纯积分商品 2:现金+积分商品
     */
    @FormUrlEncoded
    @POST("item/me-item-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> listGoods(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("item_type") String item_type,
                                                                @Field("mcid") String mcid,
                                                                @Field("key_word") String key_word,
                                                                @Field("min_price") String min_price,
                                                                @Field("max_price") String max_price,
                                                                @Field("min_stock") String min_stock,
                                                                @Field("max_stock") String max_stock,
                                                                @Field("shop_id") String shop_id,
                                                                @Field("is_hot") String is_hot,
                                                                @Field("is_new") String is_new,
                                                                @Field("sort_name") String sort_name,
                                                                @Field("sort_type") String sort_type,
                                                                @Field("item_ids[]") List<String> item_ids);

    /**
     * 猜你喜欢 商品列表
     */
    @FormUrlEncoded
    @POST("item/me-item-api/like")
    Observable<BaseData<List<CommendGoodEntity>>> getCommendGoods(@Field("param") String param);


    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST("item/me-item-api/view")
    Observable<BaseData<GoodsDetailedEntity>> getGoodsDetailed(@Field("item_id") String goodId);

    /**
     * 收藏商品
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/collection")
    Observable<BaseData> goodCollection(@Field("item_id") String goodId);

    /**
     * 删除商品收藏
     */
    @FormUrlEncoded
    @POST("item/me-item-collection-api/del-collection")
    Observable<BaseData> goodCollectionDel(@Field("item_id[]") String[] goodId);

    /**
     * 获取热门搜索信息
     */
    @FormUrlEncoded
    @POST("item/me-item-api/hot-label")
    Observable<BaseData<List<String>>> getHotSearchList(@Field("shop_id") String shopId);
}
