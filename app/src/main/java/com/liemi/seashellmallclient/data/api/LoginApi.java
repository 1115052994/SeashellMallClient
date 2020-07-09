package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.netmi.baselibrary.data.entity.BaseData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户登录注册相关
 */
public interface LoginApi {
    /**
     * 获取验证码
     *
     * @param phone 手机号码   必选字段
     * @param type  请求类型   必选字段
     * @return type 取值：
     * register:注册验证码
     * login:登录验证码
     * reset:忘记密码验证码
     * payPassword:设置支付密码
     * bindPhone:绑定手机号验证码
     * changePhone:修改手机号验证码
     * applyCash:提现
     */
    @FormUrlEncoded
    @POST("member/user-api/send-sms")
    Observable<BaseData> doAuthCode(@Field("phone") String phone,
                                    @Field("code") String code,
                                    @Field("sign") String sign,
                                    @Field("type") String type);

    /**
     * 用户注册
     *
     * @param phone           注册手机号  可选字段
     * @param code            注册验证码   可选字段
     * @param password        注册验证码   可选字段
     * @param username        用户名 可选字段
     * @param invitation_code 邀请码 可选字段
     * @param openid          微信openId    可选字段
     * @param scenario        注册类型
     * @return scenario取值：
     * register_phone 手机号+验证码+密码注册场景
     * register_default    用户名+手机号+密码注册场景
     * register_wechat     微信openId注册场景
     * register_wechat_phone   微信openId+手机号+验证码
     */
    @FormUrlEncoded
    @POST("/member/user-api/register")
    Observable<BaseData<ShareMallUserInfoEntity>> doRegister(@Field("phone") String phone,
                                                             @Field("code") String code,
                                                             @Field("password") String password,
                                                             @Field("username") String username,
                                                             @Field("invitation_code") String invitation_code,
                                                             @Field("openid") String openid,
                                                             @Field("unionid") String unionid,
                                                             @Field("scenario") String scenario);

    /**
     * 登录通用方法
     *
     * @param username 用户名   用于与用户名+密码登录方式
     * @param password 密码     用于用户名+密码  手机号+密码登录方式
     * @param phone    手机号     用于手机号+密码   手机号+验证码登录方式
     * @param code     验证码     用于手机号+验证码登录方式
     * @param openid   微信用户id  用于微信登陆方式
     * @param scenario 登录方式    用于标识用户的登录方式
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<ShareMallUserInfoEntity>> doLogin(@Field("username") String username,
                                                          @Field("password") String password,
                                                          @Field("phone") String phone,
                                                          @Field("code") String code,
                                                          @Field("openid") String openid,
                                                          @Field("unionid") String unionid,
                                                          @Field("scenario") String scenario);

    /**
     * 登录通用方法
     *
     * @param username 用户名   用于与用户名+密码登录方式
     * @param password 密码     用于用户名+密码  手机号+密码登录方式
     * @param phone    手机号     用于手机号+密码   手机号+验证码登录方式
     * @param code     验证码     用于手机号+验证码登录方式
     * @param openid   微信用户id  用于微信登陆方式
     * @param scenario 登录方式    用于标识用户的登录方式
     * @return
     */
    @FormUrlEncoded
    @POST("/member/user-api/login")
    Observable<BaseData<ShareMallUserInfoEntity>> doLogin(@Field("username") String username,
                                                          @Field("password") String password,
                                                          @Field("phone") String phone,
                                                          @Field("code") String code,
                                                          @Field("openid") String openid,
                                                          @Field("scenario") String scenario);


    /**
     * 更换手机号
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-phone")
    Observable<BaseData> doChangePhone(@Field("phone") String phone,
                                       @Field("code") String code);

    /**
     * 绑定手机号
     *
     * @param phone 手机号
     * @param code  验证码
     * @param token 用户token
     */
    @FormUrlEncoded
    @POST("member/user-api/bind-phone")
    Observable<BaseData<ShareMallUserInfoEntity>> doBindPhone(@Field("phone") String phone,
                                                              @Field("code") String code,
                                                              @Field("token") String token);

    /**
     * 绑定邀请码的接口
     *
     * @param code 邀请码
     */
    @FormUrlEncoded
    @POST("hand/fans-api/create-fans")
    Observable<BaseData> doBindInvitationCode(@Field("share_code") String code);

    /**
     * 设置支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/set-pay-password")
    Observable<BaseData> doSetPayPassword(@Field("password") String password);

    /**
     * 修改支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-pay-password")
    Observable<BaseData> doChangePayPassword(@Field("old_pass") String old_pass,
                                             @Field("password") String password);

    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/change-password")
    Observable<BaseData> doChangeLoginPassword(@Field("old_pass") String old_pass,
                                               @Field("password") String password);

    /**
     * 忘记登录密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-password")
    Observable<BaseData> doForgetPassword(@Field("password") String password,
                                             @Field("code") String code,
                                             @Field("phone") String phone,
                                          @Field("graphCode") String graphCode,
                                          @Field("sign") String sign);   /**
     * 忘记支付密码
     */
    @FormUrlEncoded
    @POST("/member/user-api/reset-pay-password")
    Observable<BaseData> doForgetPayPassword(@Field("password") String password,
                                             @Field("code") String code,
                                             @Field("phone") String phone);
}
