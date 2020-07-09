package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.floor.MaterialEntity;
import com.liemi.seashellmallclient.data.entity.good.ItemShareEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
public interface ShareApi {

    /**
     * 发圈素材列表
     *
     * @param show_type 1.商品推荐 2.营销素材 3.新手必发
     */
    @FormUrlEncoded
    @POST("stuff/item-material-api/list")
    Observable<BaseData<PageEntity<MaterialEntity>>> doShareMomentList(@Field("start_page") int start_page,
                                                                       @Field("pages") int pages,
                                                                       @Field("show_type") int show_type);

    /**
     * 商品素材
     *
     * @param use_type 1.官方素材 2.用户素材
     */
    @FormUrlEncoded
    @POST("stuff/item-material-api/item-list")
    Observable<BaseData<PageEntity<MaterialEntity>>> doGetMaterial(@Field("start_page") int start_page,
                                                                   @Field("pages") int pages,
                                                                   @Field("item_id") String item_id,
                                                                   @Field("use_type") int use_type);

    /**
     * 删除素材
     *
     * @param materialId 素材主键
     */
    @FormUrlEncoded
    @POST("stuff/item-material-api/del")
    Observable<BaseData> doDelMaterial(@Field("material_id") int materialId);

    /**
     * 上传素材
     */
    @FormUrlEncoded
    @POST("stuff/item-material-api/create")
    Observable<BaseData> doUploadMaterial(@Field("item_id") String goodId,
                                          @Field("rich_text") String content,
                                          @Field("img_arr[]") List<String> uploadUrls);


    /**
     * 分享商品，该接口调用成功后才可以分享
     */
    @FormUrlEncoded
    @POST("item/me-item-api/share-item")
    Observable<BaseData> beforeShare(@Field("param") String param);


    /**
     * 分享商品小程序到微信
     */
    @FormUrlEncoded
    @POST("item/me-item-api/share-item-to-small")
    Observable<BaseData<ItemShareEntity>> shareItem(@Field("item_id") String item_id);
}
