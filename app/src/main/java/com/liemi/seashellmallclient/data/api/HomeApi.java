package com.liemi.seashellmallclient.data.api;


import com.liemi.seashellmallclient.data.entity.HomeDialogEntity;
import com.liemi.seashellmallclient.data.entity.SignInfoEntity;
import com.liemi.seashellmallclient.data.entity.SignRecordEntity;
import com.liemi.seashellmallclient.data.entity.floor.FloorPageEntity;
import com.liemi.seashellmallclient.data.entity.floor.FloorTypeEntity;
import com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.netmi.baselibrary.data.entity.AppThemeEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

public interface HomeApi {

    /**
     *
     */
    @FormUrlEncoded
    @POST("item/me-item-api/index")
    Observable<BaseData<PageEntity<GoodsListEntity>>> getVipOrBuy(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages,
                                                                  @Field("is_vip") int is_vip,
                                                                  @Field("is_buy") int is_buy);

    /**
     * 楼层展示接口
     * @param use_position	是	int	位置编号
     * @param shop_id       是	int	店铺id	1
     */
    @FormUrlEncoded
    @POST("floor/floor-api/get-floor-info")
    Observable<BaseData<FloorPageEntity<NewFloorEntity>>> doListFloors(@Field("use_position") String use_position,
                                                                       @Field("shop_id") String shop_id);
    /**
     * 楼层分类接口
     */
    @FormUrlEncoded
    @POST("floor/floor-api/get-use-position-list")
    Observable<BaseData<List<FloorTypeEntity>>> doGetFloorType(@Field("introduction") String introduction,
                                                               @Field("shop_id") String shop_id);

    /**
     * 签到首页信息
     */
    @FormUrlEncoded
    @POST("sign/user-sign-api/info")
    Observable<BaseData<SignInfoEntity>> doGetSignInfo(@Field("param") String param);

    /**
     * 签到记录
     */
    @FormUrlEncoded
    @POST("sign/user-sign-api/record-info")
    Observable<BaseData<SignRecordEntity>> doGetSignRecord(@Field("param") String param);

    /**
     * 签到记录
     */
    @FormUrlEncoded
    @POST("sign/user-sign-api/create")
    Observable<BaseData> doSign(@Field("param") String param);

    /**
     * 首页弹窗
     */
    @FormUrlEncoded
    @POST("hand/shop-conf-api/get-info")
    Observable<BaseData<HomeDialogEntity>> doHomeDialog(@Field("param") String param);

    /**
     * 获取新人必买海报
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-new-people-poster")
    Observable<BaseData<VIPShareImgEntity>> getNewShare(@Field("param") String param);

    /**
     * 获取VIP专区分享海报
     */
    @FormUrlEncoded
    @POST("item/me-item-api/get-vip-poster")
    Observable<BaseData<VIPShareImgEntity>> getVipShare(@Field("param") String param);

    /**
     * 获取APP风格参数
     */
    @FormUrlEncoded
    @POST("base/templet-api/info")
    Observable<BaseData<AppThemeEntity>> getAppTheme(@Field("param") String param);

}
