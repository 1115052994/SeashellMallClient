package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;

/**
 * Created by Bingo on 2018/12/29.
 * 我的会员列表
 */
public class MyVIPMemberEntity extends BaseEntity implements Serializable {

    /**
     "nickname": "颜狗",                    ## 用户昵称
     "uid":"1486",//粉丝UID
     "head_url": "https://wx.qlogo.cn/mmopen/vi_32/R3oqKFZKBdveekre4H4cMHxyCgOS7WR1bLzDuPQDVmrGjibNy2o2arQqDKrUDEBFawP0jia1LexrlbIgSm1VSv5Q/132",                ## 头像
     "role": "0",                        ## 用户类型 0:普通用户,1:推手
     "hand_level_name": "经理",                        ##  推手等级名称 （用户类型为1的才有此数据）
     "hand_level": "1",                        ##  推手等级（用户类型为1的才有此数据）
     "create_time": "2018-12-06 14:00:22",                    ## 创建时间
     "update_time": "2018-12-06 14:00:22",                    ## 更新时间
     "fans_level": "0",                        ## 粉丝等级 0：临时粉丝 1：永久粉丝
     "id": "1",                        ## 粉丝id
     "puid": "628"，                    ## 上级用户id
     "income": "3.80",  //返利
     "order_num": "1"  //开单数
     */
    private String nickname;
    private String uid;
    private String head_url;
    private String role;
    private String hand_level_name;
    private String hand_level;
    private String create_time;
    private String update_time;
    private String fans_level;
    private String id;
    private String puid;
    private String income;
    private String order_num;
    private String fans_uid;
    private String share_code;
    private String fans_num;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getFans_uid() {
        return fans_uid;
    }

    public void setFans_uid(String fans_uid) {
        this.fans_uid = fans_uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHand_level_name() {
        return hand_level_name;
    }

    public void setHand_level_name(String hand_level_name) {
        this.hand_level_name = hand_level_name;
    }

    public String getHand_level() {
        return hand_level;
    }

    public void setHand_level(String hand_level) {
        this.hand_level = hand_level;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getFans_level() {
        return fans_level;
    }

    public void setFans_level(String fans_level) {
        this.fans_level = fans_level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }
}
