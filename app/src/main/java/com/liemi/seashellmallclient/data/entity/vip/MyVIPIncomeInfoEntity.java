package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;

/**
 * Created by Bingo on 2019/1/2.
 */

public class MyVIPIncomeInfoEntity implements Serializable {

    /**
     * "today": {
     "start_time": "2018-12-26",
     "emd_time": "2018-12-26",
     "income": "100.00" //今日预计收入
     },
     "week": {
     "start_time": "2018-12-24",
     "emd_time": "2018-12-26",
     "income": "100.00" //本周预计收入
     },
     "month": {
     "start_time": "2018-12-01 ",
     "emd_time": "2018-12-26",
     "income": "100.00" //本月预计收入
     },
     "total": {
     "start_time": "2018-1-1",
     "emd_time": "2018-12-26",
     "income": "100.00" //累计
     }
     */

    private TotalBean today;
    private TotalBean week;
    private TotalBean month;
    private TotalBean total;

    public TotalBean getToday() {
        return today;
    }

    public void setToday(TotalBean today) {
        this.today = today;
    }

    public TotalBean getWeek() {
        return week;
    }

    public void setWeek(TotalBean week) {
        this.week = week;
    }

    public TotalBean getMonth() {
        return month;
    }

    public void setMonth(TotalBean month) {
        this.month = month;
    }

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public static class TotalBean implements Serializable {
        /**
         * start_time : 2018-1-1
         * emd_time : 2018-12-26
         * income : 100.00
         */

        private String start_time;
        private String end_time;
        private double income;
        private String total_money;
        private String month_money;
        private String today_money;
        private String week_money;
        private String money;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getWeek_money() {
            return week_money;
        }

        public void setWeek_money(String week_money) {
            this.week_money = week_money;
        }

        public String getMonth_money() {
            return month_money;
        }

        public void setMonth_money(String month_money) {
            this.month_money = month_money;
        }

        public String getToday_money() {
            return today_money;
        }

        public void setToday_money(String today_money) {
            this.today_money = today_money;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getIncome() {
            return FloatUtils.twoDecimal(FloatUtils.formatDouble(income),true);
        }

        public void setIncome(double income) {
            this.income = income;
        }
    }
}
