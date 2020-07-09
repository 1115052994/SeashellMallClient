package com.netmi.baselibrary.data.entity;

/**
 * 类描述：图形验证码
 * 创建人：Simple
 * 创建时间：2019/5/27
 * 修改备注：
 */
public class ImageCodeEntity {


    /**
     * url : https://social-shop.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXMNTK0234567_1558676305.png
     * sign : aRWe3xTmJRc3Yf2DbCRv1TJmNzJmNmU5YWUyNjQ5ZjRjM2MzMzc4MWViMzRkNmE0NmM4MDVmNmQwYjBkNmZiYWIwMmM3NzQ2Njg4ODg1NGFxEl6lYFjCipgdqjTw6XQmJBLUFTHEajHfEcjpbhgK+C4/tCUltpZCQSenxUDaPQVukIdH991Kt8AUP4UQD4Xnjh8UyCcFig3M2VVvWaFS1nlGPynQcSllkBcbfkG2+GI=
     */

    private String url;
    private String sign;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
