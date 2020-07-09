package com.netmi.baselibrary.data.api;

import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.ImageCodeEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/4 15:05
 * 修改备注：
 */
public interface LoginApi {

    /**
     * 用户登录
     * scenario：
     * 1：用户名+密码登录场景： login_default
     * 2：手机号+密码登录场景：login_phone
     * 3：手机号+验证码登录场景：login_code
     * 4: 微信登录场景：login_wechat
     */
    @FormUrlEncoded
    @POST("member/user-api/login")
    Call<BaseData<UserInfoEntity>> doLogin(@Field("phone") String phone,
                                           @Field("password") String password,
                                           @Field("openid") String openid,
                                           @Field("unionid") String unionid,
                                           @Field("scenario") String scenario);

    /**
     * 更新凭证
     */
    @FormUrlEncoded
    @POST("member/user-api/refresh")
    Call<BaseData<UserInfoEntity>> doRefreshToken(@Field("refresh_token") String refresh_token);


    /**
     * 协议相关统一API
     */
    @FormUrlEncoded
    @POST("content/content-api/view")
    Observable<BaseData<AgreementEntity>> getAgreement(@Field("type") int type);

    /**
     * 获取验证码
     * type:请求类型 验证码获取类型
     * register ： 注册验证码
     * login：登录验证码
     * reset：忘记密码验证码
     * setphone：更换手机号验证码
     * payPassword:设置支付密码
     */
    @FormUrlEncoded
    @POST("sms/index/sms-code")
    Observable<BaseData> getSmsCode(@Field("phone") String phone,
                                    @Field("type") String type);


    /**
     * 获取图片验证码
     *
     * @param type register: 注册验证码
     *             login：登录验证码
     *             reset：忘记密码验证码
     *             payPassword：设置/重置支付密码
     *             bindPhone: 绑定手机号验证码
     *             changePhone: 修改手机号验证码
     *             member: 会员验证短信
     *             applyCash: 提现
     */
    @FormUrlEncoded
    @POST("member/user-api/get-img-code")
    Observable<BaseData<ImageCodeEntity>> getImageCode(@Field("type") String type);

}
