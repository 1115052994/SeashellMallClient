package com.liemi.seashellmallclient.data.api;


import com.liemi.seashellmallclient.data.entity.*;
import com.liemi.seashellmallclient.data.entity.contacts.RecentContactEntity;
import com.liemi.seashellmallclient.data.entity.good.ShareImgEntity;
import com.liemi.seashellmallclient.data.entity.user.*;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/8 16:19
 * 修改备注：
 */
public interface MineApi {
    /**
     * 修改支付密码
     *
     * @param old_pass
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-pay-password")
    Observable<BaseData> doChangePayPassword(@Field("old_pass") String old_pass,
                                             @Field("password") String password);

    /**
     * 修改登录密码
     *
     * @param old_pass
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-password")
    Observable<BaseData> doChangeLoginPassword(@Field("old_pass") String old_pass,
                                               @Field("password") String password);

    /**
     * 修改支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-pay-password")
    Observable<BaseData> doForgetPayPassword(@Field("phone") String phone,
                                             @Field("code") String code,
                                             @Field("password") String password);

    /**
     * 用户个人信息
     */
    @FormUrlEncoded
    @POST("/member/user-api/info")
    Observable<BaseData<ShareMallUserInfoEntity>> getUserInfo(@Field("defaultParam") int param);

    /**
     * 上级微信名片
     */
/*
    @FormUrlEncoded
    @POST("/member/user-api/get-up-wechat")
    Observable<BaseData<UpWechatEntity>> getUpWechat(@Field("defaultParam") int param);
*/

    /**
     * 验证验证码
     */
    @FormUrlEncoded
    @POST("member/user-api/check-code")
    Observable<BaseData> doAuthCode(@Field("phone") String phone, @Field("type") String type, @Field("code") String code);

    /**
     * 收藏的店铺列表
     *
     * @param start_page
     * @param pages
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-collection-api/index")
    Observable<BaseData<PageEntity<MineCollectionStoreEntity>>> doMineCollectionStore(@Field("start_page") int start_page,
                                                                                      @Field("pages") int pages);

    /**
     * 收货地址列表
     *
     * @param start_page 起始页
     * @param pages      每页树木
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/index")
    Observable<BaseData<PageEntity<AddressEntity>>> doAddressList(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages);

    /**
     * 新增收货地址
     *
     * @param name    收货人姓名
     * @param p_id    省id
     * @param c_id    市id
     * @param d_id    区id
     * @param tel     联系电话
     * @param address 详细地址
     * @param is_top  是否设置默认地址 0：否，1：是
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/create")
    Observable<BaseData<AddressEntity>> doAddNewAddress(@Field("name") String name,
                                                        @Field("p_id") String p_id,
                                                        @Field("c_id") String c_id,
                                                        @Field("d_id") String d_id,
                                                        @Field("tel") String tel,
                                                        @Field("address") String address,
                                                        @Field("is_top") int is_top);

    /**
     * 修改收货地址
     *
     * @param maid      地址id
     * @param post_code 邮编
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/update")
    Observable<BaseData> doUpdateAddress(@Field("maid") String maid,
                                         @Field("name") String name,
                                         @Field("p_id") String p_id,
                                         @Field("c_id") String c_id,
                                         @Field("d_id") String d_id,
                                         @Field("tel") String tel,
                                         @Field("address") String address,
                                         @Field("is_top") int is_top,
                                         @Field("post_code") String post_code);

    /**
     * 收货地址详情接口
     */
    @FormUrlEncoded
    @POST("shop/me-address-api/default-view")
    Observable<BaseData<AddressEntity>> getDefaultAddress(@Field("param") String param);

    /**
     * 我的海贝信心指数
     */
    @FormUrlEncoded
    @POST("order/order-api/hai")
    Observable<BaseData<HaiBeiConfidenceEntity>> getMinHai(@Field("type") int type);


    /**
     * 商品分享图片接口
     */
/*    @FormUrlEncoded
    @POST("item/me-item-api/merger-img")
    Observable<BaseData<List<ShareImgEntity>>> getShareImg(@Field("item_id") String item_id,
                                                           @Field("group_team_id") String group_team_id);*/

    /**
     * 用户公告信息
     * 1：系统公告；3商学院；4用户资产 2系统通知
     *
     * @param type_arr   需要获取的公告类型
     * @param key_word   关键字
     * @param start_page 起始页
     * @param pages      每一页的条数
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/index")
    Observable<BaseData<UserNoticeEntity>> doUserNotices(@Field("type_arr[]") String[] type_arr,
                                                         @Field("key_word") String key_word,
                                                         @Field("start_page") int start_page,
                                                         @Field("pages") int pages);


    /**
     * 设置公告已读
     *
     * @param notice_id 公告id
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/set-read")
    Observable<BaseData> doSetNoticeRead(@Field("notice_id") int notice_id);

    /**
     * 更新用户信息
     *
     * @param head_url   头像
     * @param nickname   昵称
     * @param sex        性别
     * @param date_birth 生日
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/update")
    Observable<BaseData> doUserInfoUpdate(@Field("head_url") String head_url,
                                          @Field("nickname") String nickname,
                                          @Field("sex") String sex,
                                          @Field("date_birth") String date_birth,
                                          @Field("wechat") String wechat,
                                          @Field("wechat_img") String wechat_img,
                                          @Field("wechat_name") String wechat_name);


    /**
     * 微信注册初始化
     */
    @FormUrlEncoded
    @POST("/member/user-api/update")
    Observable<BaseData> initUserInfo(@Field("token") String token,
                                      @Field("head_url") String head_url,
                                      @Field("nickname") String nickname);


    /**
     * 删除收货地址
     *
     * @param maid 地址id
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/me-address-api/delete")
    Observable<BaseData> doDeleteAddress(@Field("maid") String maid);

    /**
     * 意见反馈接口
     */
    @FormUrlEncoded
    @POST("/feedback/feedback-api/create")
    Observable<BaseData> doSuggestionBack(@Field("remark") String remark,
                                          @Field("tel") String phone,
                                          @Field("imgs[]") List<String> imgs,
                                          @Field("link_man") String link_man);

    /**
     * 取消收藏店铺
     *
     * @param shop_id 要取消收藏的id
     * @return
     */
    @FormUrlEncoded
    @POST("/shop/shop-api/delete-collection")
    Observable<BaseData> doUnCollectionStore(@Field("shop_id[]") List<String> shop_id);

    /**
     * 设置全部已读
     *
     * @param defaultData
     * @return
     */
    @FormUrlEncoded
    @POST("/notice/notice-api/set-read-all")
    Observable<BaseData> doNoticeAllRead(@Field("defaultData") String defaultData);

    /**
     * 订单数量统计
     */
    @FormUrlEncoded
    @POST("order/order-api/get-count")
    Observable<BaseData<OrderCountEntity>> getOrderCount(@Field("param") int param);

    /**
     * 获取我的积分数量
     */
    @FormUrlEncoded
    @POST("member/user-coin-api/get-my-coin")
    Observable<BaseData<MineIntegralNumEntity>> doMineIntegralNum(@Field("default_data") String data);

    /**
     * 获取积分任务
     */
/*
    @FormUrlEncoded
    @POST("member/user-coin-api/get-coin-task")
    Observable<BaseData<PageEntity<MineIntegralGetEntity>>> doMineYubiGet(@Field("default_data") String data);
*/

    /**
     * 获取积分明细
     */
/*
    @FormUrlEncoded
    @POST("member/user-coin-api/get-coin-list")
    Observable<BaseData<PageEntity<MineIntegralDetailsEntity>>> doMineYubiDetails(@Field("start_page") int start_page,
                                                                                  @Field("pages") int pages);
*/

    /**
     * 我的优惠券列表
     */
    @FormUrlEncoded
    @POST("coupon/coupon-api/index")
    Observable<BaseData<PageEntity<MineCouponEntity>>> listCoupon(@Field("start_page") int start_page,
                                                                  @Field("pages") int pages,
                                                                  @Field("status") int status,
                                                                  @Field("item_arr[]") List<String> item_arr,
                                                                  @Field("scenario") String scenario);

    /**
     * 分享优惠券
     */
    @FormUrlEncoded
    @POST("coupon/coupon-share-api/batch-create")
    Observable<BaseData<MineCouponShareEntity>> doShareSelectCoupon(@Field("coupon_ids[]") List<String> coupon_ids);

    /**
     * 更新优惠券状态
     */
    @FormUrlEncoded
    @POST("coupon/coupon-share-api/update-status")
    Observable<BaseData> doChangeCouponStatus(@Field("share_id") String share_id);


    /**
     * 用户成长值信息
     */
    @FormUrlEncoded
    @POST("member/growth-api/get-info")
    Observable<BaseData<MineGrowthEntity>> doMineGrowthInfo(@Field("defalutData") String defaultData);

    /**
     * 获取成长值明细列表
     */
/*
    @FormUrlEncoded
    @POST("member/growth-api/get-growth-list")
    Observable<BaseData<PageEntity<MineGrowthDetailsEntity>>> doMineGrowthDetailsList(@Field("start_page") int start_page,
                                                                                      @Field("pages") int pages);
*/

    /**
     * 成长任务列表
     */
/*
    @FormUrlEncoded
    @POST("member/growth-api/get-growth-task")
    Observable<BaseData<PageEntity<MineGrowthTaskEntity>>> doMineGrowthTaskList(@Field("defaultData") String defaultData);
*/

    /**
     * 平台客服
     *
     * @param type 客服类型 0平台 1店铺
     */
    @FormUrlEncoded
    @POST("customer/index-api/token-by-shop-id")
    Observable<BaseData<SobotSystemEntity>> doGetSobotInfo(@Field("type") int type,
                                                           @Field("shop_id") String shop_id,
                                                           @Field("item_id") String item_id);

    /**
     * 获取用户未读消息数量
     */
    @FormUrlEncoded
    @POST("notice/notice-api/get-num")
    Observable<BaseData<NotReadNumEntity>> getAllUnreadNum(@Field("param") String param);


    /**
     * 获取消息列表
     */
    @FormUrlEncoded
    @POST("notice/notice-api/get-message-list")
    Observable<BaseData<List<RecentContactEntity>>> getRecentContacts(@Field("param") String param);

}
