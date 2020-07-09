package com.netmi.baselibrary.data.entity;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 11:26
 * 修改备注：
 */
public class UserInfoEntity implements Serializable {
    //性别分类
    public static final int SEX_MAN = 1;//男
    public static final int SEX_WOMEN = 2;//女
    public static final int SEX_UNKNOW = 3;//未知

    //是否绑定
    public static final int BIND = 1;//绑定
    public static final int UNBIND = 0;//未绑定


    //用户主键
    private String uid;
    //手机号
    private String phone;
    //微信用户主键，备用字段
    private String wechat_id;
    //昵称
    private String nickname;
    //用户头像
    private String head_url;
    //会员积分
    private float score;

    //注册时间
    private String create_time;
    //性别
    private int sex;
    //性别字符,自定义字段
    private String sexFormat;
    //出生日期
    private String date_birth;
    //年龄
    private int age;

    //身份证号
    private String id_card;
    //省id
    private String p_id;
    //市id
    private String c_id;
    //区id
    private String d_id;
    //详细地址
    private String address;
    //地址明细
    private String full_address;
    //个性签名
    private String sign_name;
    //用户token
    private AccessToken token;
    //是否绑定QQ 0:否  1：是
    private int is_bind_qq;
    //是否绑定微博
    private int is_bind_weibo;
    //是否绑定邮箱
    private int is_bind_email;
    //是否绑定微信
    private int is_bind_wechat;
    //是否绑定手机号
    private int is_bind_phone;
    //是否设置支付密码
    private int is_set_paypassword;
    //可用eth
    private String eth_remain;
    //可用eth对应的cny
    private String eth_cny;

    private String share_code;

    public String getSexFormat() {
        if(sex == SEX_MAN){
            return "男";
        }else if(sex == SEX_WOMEN){
            return "女";
        }else if(sex == SEX_UNKNOW){
            return "未知";
        }
        return sexFormat;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public int getIs_set_paypassword() {
        return is_set_paypassword;
    }

    public void setIs_set_paypassword(int is_set_paypassword) {
        this.is_set_paypassword = is_set_paypassword;
    }

    private String birthday;
    private String com_name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public void setSexFormat(String sexFormat) {
        this.sexFormat = sexFormat;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }

    public int getIs_bind_qq() {
        return is_bind_qq;
    }

    public void setIs_bind_qq(int is_bind_qq) {
        this.is_bind_qq = is_bind_qq;
    }

    public int getIs_bind_weibo() {
        return is_bind_weibo;
    }

    public void setIs_bind_weibo(int is_bind_weibo) {
        this.is_bind_weibo = is_bind_weibo;
    }

    public int getIs_bind_email() {
        return is_bind_email;
    }

    public void setIs_bind_email(int is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public int getIs_bind_wechat() {
        return is_bind_wechat;
    }

    public void setIs_bind_wechat(int is_bind_wechat) {
        this.is_bind_wechat = is_bind_wechat;
    }

    public int getIs_bind_phone() {
        return is_bind_phone;
    }

    public void setIs_bind_phone(int is_bind_phone) {
        this.is_bind_phone = is_bind_phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getEth_remain() {
        return eth_remain;
    }

    public void setEth_remain(String eth_remain) {
        this.eth_remain = eth_remain;
    }

    public String getEth_cny() {
        return eth_cny;
    }

    public void setEth_cny(String eth_cny) {
        this.eth_cny = eth_cny;
    }
}
