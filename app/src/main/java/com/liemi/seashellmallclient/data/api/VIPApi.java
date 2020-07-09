package com.liemi.seashellmallclient.data.api;

import com.google.gson.internal.LinkedTreeMap;
import com.liemi.seashellmallclient.data.entity.order.OrderRebateEntity;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPIncomeInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPMemberEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPGiftEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPIncomeListEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPIncomeOrderEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPMemberEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPMemberPageEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPUserInfoEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

/**
 * Created by Bingo on 2018/12/29.
 */

public interface VIPApi {
    /**
     * 获取用户成长值信息
     */
    /*@FormUrlEncoded
    @POST("member/growth-api/get-info")
    Observable<BaseData<VIPProgressEntity>> getVIPProgressInfo(@Field("param") String param);*/


    /**
     * 获取VIP Banner
     * 类型： vip礼包页vip-gift 代理商礼包页agent
     */
    @FormUrlEncoded
    @POST("banner/banner-api/get-vip-banner")
    Observable<BaseData<LinkedTreeMap<String, String>>> getVIPBanner(@Field("type") String type);

    /**
     * 我的会员列表
     */
    @FormUrlEncoded
    @POST("hand/hand-api/get-first-vip-list")
    Observable<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>> getMyVIPMembers(@Field("start_page") int startPage,
                                                                                 @Field("pages") int pages);
    /**
     * 获取我的粉丝（VIP模块）
     */
    @FormUrlEncoded
    @POST("hand/fans-api/fans")
    Observable<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>> getMyVIPFollowers(@Field("start_page") int startPage,
                                                                                   @Field("pages") int pages,
                                                                                   @Field("type") String type);

    /**
     * 获取我的二级粉丝（VIP模块）
     */
    @FormUrlEncoded
    @POST("hand/fans-api/fans")
    Observable<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>> getMyVIPFollowersSecond(@Field("start_page") int startPage,
                                                                                   @Field("pages") int pages,
                                                                                         @Field("fans_uid") String fans_uid,
                                                                                         @Field("type") String type);

    /**
     * 获取预计收入
     */
    @FormUrlEncoded
    @POST("hand/hand-api/get-my-income")
    Observable<BaseData<MyVIPIncomeInfoEntity>> getMyVIPIncomeInfo(@Field("param") String param);
    /**
     * 获取推手信息
     */
    @FormUrlEncoded
    @POST("hand/hand-api/info")
    Observable<BaseData<VIPUserInfoEntity>> getVIPUserInfo(@Field("param") String param);
    /**
     * 商品列表获取
     * mcid	是	int	分类主键
     *
     * @param item_type 商品类型 默认0:普通商品 1:纯积分商品（暂无） 2:现金+积分商品（暂无）
     *                  4：推手礼包商品(实为VIP升级礼包) 5：经理礼包商品 6:代理商礼包
     */
    @FormUrlEncoded
    @POST("item/me-item-api/index")
    Observable<BaseData<PageEntity<VIPGiftEntity>>> getVIPGoods(@Field("start_page") int start_page,
                                                                @Field("pages") int pages,
                                                                @Field("item_type") String item_type);

    /**
     * 获取推手收益订单
     * 0:全部 1:退款 2:已结算 3:未结算
     */
    @FormUrlEncoded
    @POST("hand/hand-api/order")
    Observable<BaseData<PageEntity<VIPIncomeOrderEntity>>> getVIPIncomeOrder(@Field("start_page") int startPage,
                                                                             @Field("pages") int pages,
                                                                             @Field("order_no") String orderNo,
                                                                             @Field("start_time") String startTime,
                                                                             @Field("end_time") String endTime,
                                                                             @Field("type") String status);
    /**
     * 订单返利详情页
     * id 收益订单的id
     */
    @FormUrlEncoded
    @POST("hand/hand-api/income-detail")
    Observable<BaseData<OrderRebateEntity>> getOrderRebate(@Field("hand_log_id") String orderId);

    /**
     * 获取我的福利金
     */
    @FormUrlEncoded
    @POST("hand/hand-api/income-list")
    Observable<BaseData<PageEntity<VIPIncomeListEntity>>> getVIPIncomeList(@Field("start_page") int startPage,
                                                                           @Field("pages") int pages,
                                                                           @Field("start_time") String startTime,
                                                                           @Field("end_time") String endTime,
                                                                           @Field("type") int type);
    /**
     * 邀请好友海报接口
     */
    @FormUrlEncoded
    @POST("member/user-api/inviting-posters")
    Observable<BaseData<VIPShareImgEntity>> VIPInviteFriend(@Field("param") String param);
    /**
     * 获取店铺分享海报
     */
    @FormUrlEncoded
    @POST("member/user-api/get-vip-share-poster")
    Observable<BaseData<VIPShareImgEntity>> getStoreSharePoster(@Field("param") String param);

    /**
     * 邀请好友海报接口
     */
    @FormUrlEncoded
    @POST("banner/banner-api/shop-bg")
    Observable<BaseData<VIPShareImgEntity>> getMerchantsSettledImage(@Field("param") String param);

    /**
     * 分享收益海报
     */
    /*@FormUrlEncoded
    @POST("member/user-api/get-income-poster")
    Observable<BaseData<VIPShareImgEntity>> getIncomePoster(@Field("param") String param);
*/
    /**
     * 推手用户列表
     */
    @FormUrlEncoded
    @POST("handConfig/hand-config-api/index")
    Observable<BaseData<List<VIPMemberEntity>>> listVipMember(@Field("param") String param);
    /**
     * 推手规则
     */
   /* @FormUrlEncoded
    @POST("handUpdate/hand-update-level-api/info")
    Observable<BaseData<VipRuleEntity>> getVipRule(@Field("param") String param);
*/

    /**
     * 获取推手任务海报
     */
    @FormUrlEncoded
    @POST("handUpdate/hand-update-level-api/get-poster")
    Observable<BaseData<LinkedTreeMap<String, String>>> getVipPoster(@Field("param") String param);

    /**
     * 工猫电签
     */
    @FormUrlEncoded
    @POST("hand/me-cash-api/sign")
    Observable<BaseData<String>> signature(@Field("name") String name,
                                           @Field("mobile") String mobile);

    /**
     * 获取等级列表
     */
   /* @FormUrlEncoded
    @POST("hand/hand-team-api/get-levels")
    Observable<BaseData<List<VipLevelEntity>>> listVipLevel(@Field("param") String param);
*/
    /**
     * 社群会员列表
     */
    /*@FormUrlEncoded
    @POST("hand/hand-team-api/get-list")
    Observable<BaseData<PageEntity<MyVIPMemberEntity>>> listCommunityFans(@Field("start_page") int startPage,
                                                                          @Field("pages") int pages,
                                                                          @Field("level") String level,
                                                                          @Field("relation") String relation,
                                                                          @Field("min_income") String min_income,
                                                                          @Field("max_income") String max_income);
*/
    /**
     * 站内信推送
     */
    @FormUrlEncoded
    @POST("hand/hand-team-api/notice")
    Observable<BaseData> sendNotice(@Field("level") String level,
                                    @Field("relation") String relation,
                                    @Field("min_income") String min_income,
                                    @Field("max_income") String max_income,
                                    @Field("content") String content,
                                    @Field("img") String img,
                                    @Field("push_uids[]") List<String> push_uids);


    /**
     * 粉丝订单以及返利列表接口
     */
    /*@FormUrlEncoded
    @POST("member/user-behavior-api/rebate-list")
    Observable<BaseData<PageEntity<VIPIncomeOrderEntity>>> listFansOrder(@Field("start_page") int startPage,
                                                                         @Field("pages") int pages,
                                                                         @Field("fans_uid") String fans_uid,
                                                                         @Field("start_time") String startTime,
                                                                         @Field("end_time") String endTime);
*/
    /**
     * 粉丝订单以及返利列表接口
     */
    /*@FormUrlEncoded
    @POST("member/user-behavior-api/browse-list")
    Observable<BaseData<PageEntity<VipBrowseRecordEntity>>> listFansBrowse(@Field("start_page") int startPage,
                                                                           @Field("pages") int pages,
                                                                           @Field("fans_uid") String fans_uid,
                                                                           @Field("sort") String sort,
                                                                           @Field("start_time") String startTime,
                                                                           @Field("end_time") String endTime);
*/
    /*
     * 二级会员列表
     * */
    @FormUrlEncoded
    @POST("hand/hand-api/get-second-vip-list")
    Observable<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>> listMyMembers(@Field("start_page") int startPage,
                                                                               @Field("pages") int pages,
                                                                               @Field("lower_uid") String lower_uid);
}
