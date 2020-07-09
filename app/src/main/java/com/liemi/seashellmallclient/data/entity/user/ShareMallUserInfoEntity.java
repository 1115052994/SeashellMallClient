package com.liemi.seashellmallclient.data.entity.user;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.ShareMallParam;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.MApplication;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 11:26
 * 修改备注：
 */
public class ShareMallUserInfoEntity extends UserInfoEntity {
    //用户是否为推手：0代表普通用户 1代表推手
    private int role;

    //卡号
    private String card_no;

    private int is_follow;
    //是否是代理商：1是 2否
    private int is_agent;
    //代理商分享限制次数
    private int purchase_num;
    //上级昵称
    private String up_nickname;
    //用户是否有上级 1有 0没有
    private int is_invited;
    //微信昵称
    private String wechat_name;
    //微信号
    private String wechat;
    //微信二维码
    private String wechat_img;
    //云信id
    private String cid;
    //云信token
    private String yun_token;

    private int is_hand;    //是否是推手

    private String level_name;  //推手等级名称

    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isVip() {
        return role != 0 && ShareMallParam.DISTRIBUTOR_OPEN;
    }

    public int getIs_hand() {
        return is_hand;
    }

    public void setIs_hand(int is_hand) {
        this.is_hand = is_hand;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public int getPurchase_num() {
        return purchase_num;
    }

    public void setPurchase_num(int purchase_num) {
        this.purchase_num = purchase_num;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getIs_invited() {
        return is_invited;
    }

    public void setIs_invited(int is_invited) {
        this.is_invited = is_invited;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getUp_nickname() {
        return up_nickname;
    }

    public void setUp_nickname(String up_nickname) {
        this.up_nickname = up_nickname;
    }

    public String getWechat_name() {
        return wechat_name;
    }

    public void setWechat_name(String wechat_name) {
        this.wechat_name = wechat_name;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWechat_img() {
        return wechat_img;
    }

    public void setWechat_img(String wechat_img) {
        this.wechat_img = wechat_img;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getYun_token() {
        return yun_token;
    }

    public void setYun_token(String yun_token) {
        this.yun_token = yun_token;
    }
}
