package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 18:11
 * 修改备注：
 */
public class LoginInfo {

    public LoginInfo(){

    }

    public LoginInfo(String login, String password){
        this.login = login;
        this.password = password;
    }

    public LoginInfo(String openId){
        this.openid = openId;
    }

    /**
     * 用户名
     */
    private String login;
    /**
     * 密码
     */
    private String password;
    /**
     * 平台类型
     */
    private String type = "2"; //平台类型（1:IOS,2:安卓,3:PC,4:移动H5）

    /**
     * 微信登录
     */
    private String openid;
    private String unionId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
