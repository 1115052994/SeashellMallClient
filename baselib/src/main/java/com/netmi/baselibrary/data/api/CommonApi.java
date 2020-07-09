package com.netmi.baselibrary.data.api;

import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.data.entity.FileUrlEntity;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.PlatformEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/30 17:43
 * 修改备注：
 */
public interface CommonApi {

    /**
     * 获取省、市、区信息列表
     * data_type:
     * 0:获取单级数据 1： 获取所有数据
     */
    @FormUrlEncoded
    @POST("base/district-api/index")
    Observable<BaseData<List<CityChoiceEntity>>> listCity(@Field("data_type") int data_type);

    /**
     * banner列表信息获取接口
     *
     * @param seat_id 3：商学院模块新手引导
     *                7、优惠券模块领券中心
     *                12、新人必买商品列表
     *                13、VIP专享区商品列表
     *                16、VIP临时卡位身份权益
     *                200.本地生活
     */
    @FormUrlEncoded
    @POST("banner/banner-api/index")
    Observable<BaseData<PageEntity<BannerEntity>>> listBanner(@Field("seat_id") int seat_id);

    /**
     * oss基础配置信息获取
     */
    @FormUrlEncoded
    @POST("base/oss-api/info")
    Observable<BaseData<OssConfigureEntity>> getOssConfigure(@Field("param") int param);

    /**
     * 获取用户基本信息
     */
    @FormUrlEncoded
    @POST("usermember/user-data-api/index")
    Observable<BaseData<UserInfoEntity>> getUserInfo(@Field("param") int param);

    /**
     * 平台相关基本信息获取
     */
    @FormUrlEncoded
    @POST("base/intel-api/info")
    Observable<BaseData<PlatformEntity>> getPlatformInfo(@Field("param") String param);

    /**
     * 图片上传
     */
    @Multipart
    @POST("material/index/upload")
    Observable<BaseData<FileUrlEntity>> uploadFiles(@Part MultipartBody.Part multiparts);

    /**
     * 开机广告页
     */
    @FormUrlEncoded
    @POST("banner/banner-api/open-ad")
    Observable<BaseData<BannerEntity>> getAdvertising(@Field("param") String param);

}
