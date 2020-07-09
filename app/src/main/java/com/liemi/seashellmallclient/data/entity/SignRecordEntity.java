package com.liemi.seashellmallclient.data.entity;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class SignRecordEntity extends BaseEntity {

    /**
     * list : [{"day":1,"status":0},{"day":2,"status":0},{"day":3,"status":0},{"day":4,"status":0},{"day":5,"status":0},{"day":6,"status":0},{"day":7,"status":0},{"day":8,"status":0},{"day":9,"status":0},{"day":10,"status":0},{"day":11,"status":0},{"day":12,"status":0},{"day":13,"status":0},{"day":14,"status":0},{"day":15,"status":0},{"day":16,"status":0},{"day":17,"status":0},{"day":18,"status":0},{"day":19,"status":0},{"day":20,"status":0},{"day":21,"status":0},{"day":22,"status":0},{"day":23,"status":0},{"day":24,"status":0},{"day":25,"status":0},{"day":26,"status":0},{"day":27,"status":0},{"day":28,"status":0},{"day":29,"status":0},{"day":30,"status":0},{"day":31,"status":0}]
     * rest_days : 10
     * total_days : 0
     * growth : 0
     * coin : 0
     * receive_coin : 5
     * config_day : 10
     */

    private int rest_days;
    private int total_days;
    private int growth;
    private int coin;
    private String receive_coin;
    private int config_day;
    private List<ListBean> list;

    public int getRest_days() {
        return rest_days;
    }

    public void setRest_days(int rest_days) {
        this.rest_days = rest_days;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
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
         * day : 1
         * status : 0
         */

        private int day;
        private int status;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

