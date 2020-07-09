package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface LoginParam {


    /**
     * 不同登录方式的组合
     */
    String LOGIN_CODE = "login_code";//手机号+验证码登录
    String LOGIN_WECHAT = "login_wechat";//微信登录

    /**
     * 不同获取验证码的组合
     */
    String AUTH_CODE_REGISTER = "register";//注册验证码
    String AUTH_CODE_LOGIN = "login";//登录验证码
    String AUTH_CODE_RESET = "reset";//忘记密码验证码
    String AUTH_CODE_PAY_PASSWORD = "payPassword";//设置支付密码
    String AUTH_CODE_BIND_PHONE = "bindPhone";//绑定手机
    String AUTH_CODE_CHANGE_PHONE = "changePhone";//修改手机号
    String AUTH_CODE_CHECK_PHONE = "checkPhone";//检查手机号
    String AUTH_CODE_APPLY_CASH = "applyCash";//提现

    //用户能够输入最大密码长度
    int MAX_PASSWORD_LENGTH = 16;
    //用户输入的最小密码长度
    int MIN_PASSWORD_LENGTH = 6;


    /**
     * 协议相关统一
     *
     * 1、登录注册用户协议
     * 2、关于我们富文本
     * 18、提现规则说明
     * 28、发票开具说明
     * 30、VIP成长值说明
     * 32、签到规则说明
     * 33、用户隐私协议
     * 34、积分规则说明
     * 35、微信名片说明
     * 36、邀请码规则说明
     * 37、VIP任务会员规则
     * 38、服务协议
     */
    int PROTOCOL_TYPE_LOGIN = 1;
    int PROTOCOL_TYPE_ABOUT_US = 2;
    int PROTOCOL_TYPE_WITHDRAW = 18;
    int PROTOCOL_TYPE_INVOICE = 28;
    int PROTOCOL_TYPE_VIP_GROWTH = 30;
    int PROTOCOL_TYPE_SIGN_IN = 32;
    int PROTOCOL_TYPE_USER_PRIVACY = 33;
    int PROTOCOL_TYPE_INTEGRAL = 34;
    int PROTOCOL_TYPE_WECHAT_CARD = 35;
    int PROTOCOL_TYPE_INVITATION = 36;
    int PROTOCOL_TYPE_VIP_TASK = 37;
    int PROTOCOL_TYPE_SERVICE= 38;
    int PROTOCOL_TYPE_HAI = 39;


}
