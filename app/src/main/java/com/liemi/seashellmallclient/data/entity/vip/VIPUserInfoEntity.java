package com.liemi.seashellmallclient.data.entity.vip;

import android.text.TextUtils;

import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;

/**
 * Created by Bingo on 2018/12/29.
 */

public class VIPUserInfoEntity implements Serializable {

    /**
     * "id": "1",                        ## 推手id
     * "uid": "628",                        ## 用户主键
     * "puid": "0",                    ## 上级用户主键
     * "shop_id": "0",                    ## 店铺主键（暂时存在，后期可能使用）
     * "level": "3",                        ## 推手等级 1：高级推手 2：经理 3：总裁
     * "create_time": "2018-12-05 09:24:24",                    ## 创建时间
     * "greate_time": "2018-12-05 09:24:27",                    ## 成为推手时间
     * "manage_time": null,                    ## 成为经理时间
     * "president_time": null,                        ## 成为总裁时间
     * "phone": "18521538854",                        ## 电话
     * "share_code": "98b20b",                        ## 邀请码
     * "balance": "100.00",                        ## 余额
     * "HAND_NUMBER": "10000001",                    ## 推手编号
     * "p_relations": "",                        ## 关系链
     * "hand_count": "0",                    ## 推手数量
     * "update_level_time": "2018-12-06 09:53:58",                    ## 升级时间
     * "freeze": "0.00",                    ## 冻结金额
     * "head_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15397899666447.png",                ## 头像
     * "nickname": "",                        ## 昵称
     * "hand_order": 1,                    ## 分红订单数
     * "person_award": "100.00",                    ## 分红金额
     * "team_award": null,                    ## 团队分红收益
     * "total_award": 100                    ## 总分红收益
     * "fans": "0", //推送粉丝数量
     * "first_vip": "0", //直推会员数
     * "order_num": "0"  //收益订单数
     */

    private String id;
    private String uid;
    private String puid;
    private String shop_id;
    private int level;
    private String create_time;
    private String greate_time;
    private String phone;
    private String share_code;
    private double balance;
    private String hand_number;
    private String p_relations;
    private String hand_count;
    private String update_level_time;
    private String freeze;
    private String head_url;
    private String nickname;
    private String total_income;
    private int hand_order;
    private String person_award;
    private double total_award;
    private int fans;
    private int first_vip;
    private int order_num;
    private int is_agent; //是否是代理商1：是；2否
    private int purchase_num; //代理商可分享礼包次数
    private int warden;         //是否联合创始人 0否1是
    private String level_name;
    //是否签约工猫：1是；0否
    private int is_sign;

    //是否临时分销员 0否，1是
    private int is_stuck;
    //临时分销员到期时间
    private String stuck_time;
    //当前服务器时间
    private String current_time;
    //卡位开始时间
    private String to_stuck_time;
    //团队等级
    private int hand_team;
    private String today_income;
    private String month_income;

    //临时会员
    public boolean isStuckVip() {
        return is_stuck == 1;
    }

    public String getToday_income() {
        return today_income;
    }

    public void setToday_income(String today_income) {
        this.today_income = today_income;
    }

    public String getMonth_income() {
        return month_income;
    }

    public void setMonth_income(String month_income) {
        this.month_income = month_income;
    }

    public int getWarden() {
        return warden;
    }

    public void setWarden(int warden) {
        this.warden = warden;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public int getPurchase_num() {
        return purchase_num;
    }

    public void setPurchase_num(int purchase_num) {
        this.purchase_num = purchase_num;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGreate_time() {
        return greate_time;
    }

    public void setGreate_time(String greate_time) {
        this.greate_time = greate_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public String getBalance() {
        return FloatUtils.formatDouble(balance);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getHand_number() {
        return hand_number;
    }

    public void setHand_number(String hand_number) {
        this.hand_number = hand_number;
    }

    public String getP_relations() {
        return p_relations;
    }

    public void setP_relations(String p_relations) {
        this.p_relations = p_relations;
    }

    public String getHand_count() {
        return hand_count;
    }

    public void setHand_count(String hand_count) {
        this.hand_count = hand_count;
    }

    public String getUpdate_level_time() {
        return update_level_time;
    }

    public void setUpdate_level_time(String update_level_time) {
        this.update_level_time = update_level_time;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getHand_order() {
        return hand_order;
    }

    public void setHand_order(int hand_order) {
        this.hand_order = hand_order;
    }

    public String getPerson_award() {
        return person_award;
    }

    public void setPerson_award(String person_award) {
        this.person_award = person_award;
    }

    public double getTotal_award() {
        return total_award;
    }

    public void setTotal_award(double total_award) {
        this.total_award = total_award;
    }

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFirst_vip() {
        return first_vip;
    }

    public void setFirst_vip(int first_vip) {
        this.first_vip = first_vip;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }

    public int getIs_stuck() {
        return is_stuck;
    }

    public void setIs_stuck(int is_stuck) {
        this.is_stuck = is_stuck;
    }

    public String getStuck_time() {
        return stuck_time;
    }

    public void setStuck_time(String stuck_time) {
        this.stuck_time = stuck_time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getTo_stuck_time() {
        return to_stuck_time;
    }

    public void setTo_stuck_time(String to_stuck_time) {
        this.to_stuck_time = to_stuck_time;
    }

    public int getHand_team() {
        return hand_team;
    }

    public void setHand_team(int hand_team) {
        this.hand_team = hand_team;
    }
}
