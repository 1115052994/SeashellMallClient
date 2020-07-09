package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.utils.FloatUtils;

/**
 * 类描述：分页实体
 * 创建人：Simple
 * 创建时间：2017/7/18 13:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class VIPMemberPageEntity<T> extends PageEntity<T> {

    private double income;
    // 粉丝数量
    private String fans_num;
    // 未开单粉丝数量
    private String fans_no_order_num;
    // 已开单粉丝数量
    private String fans_order_num;

    private String total_count;

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String getIncome() {
        return FloatUtils.formatMoney(income);
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getFans_no_order_num() {
        return fans_no_order_num;
    }

    public void setFans_no_order_num(String fans_no_order_num) {
        this.fans_no_order_num = fans_no_order_num;
    }

    public String getFans_order_num() {
        return fans_order_num;
    }

    public void setFans_order_num(String fans_order_num) {
        this.fans_order_num = fans_order_num;
    }
}
