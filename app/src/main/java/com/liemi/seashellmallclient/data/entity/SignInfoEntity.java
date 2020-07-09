package com.liemi.seashellmallclient.data.entity;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class SignInfoEntity extends BaseEntity {

    /**
     * coupon_value : 10
     * coupon_name : 签到优惠券
     * list : [{"name":"积分任务","score":"5"},{"name":"成长任务","score":"10"}]
     * is_sign : 0
     * continue_day : 0
     * receive_coin : 5
     * config_day : 10
     */

    private String coupon_value = "";
    private String coupon_name;
    private int is_sign;
    private int continue_day;
    private String receive_coin;
    private int config_day;
    private List<ListBean> list;

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }

    public int getContinue_day() {
        return continue_day;
    }

    public void setContinue_day(int continue_day) {
        this.continue_day = continue_day;
    }

    public String getReceive_coin() {
        return receive_coin;
    }

    public void setReceive_coin(String receive_coin) {
        this.receive_coin = receive_coin;
    }

    public int getConfig_day() {
        return config_day;
    }

    public void setConfig_day(int config_day) {
        this.config_day = config_day;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : 积分任务
         * score : 5
         */

        private String name;
        private String score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}

