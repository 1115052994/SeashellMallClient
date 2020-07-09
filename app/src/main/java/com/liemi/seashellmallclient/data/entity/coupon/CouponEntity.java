package com.liemi.seashellmallclient.data.entity.coupon;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/3/19
 * 修改备注：
 */
public class CouponEntity extends BaseEntity implements Serializable {

    private String id;
    //优惠券类型：1.vip礼包   2.签到    3.新用户注册   4.新用户下单  5.平台发放
    private String coupon_type;
    //优惠券使用类型  0:满减券， 1:抵价券
    private int use_type;
    //优惠券名称
    private String name;
    //规则，折扣券的金额在这里，满减券这里不用
    private String rule;
    private String give_num;
    //1平台券所有商品可用，2店铺全部商品可用，3店铺部分商品可用
    private int item_type;
    private String total_num;
    private String create_time;
    private String update_time;
    //有效时间，单位为小时
    private String expire_hour;
    //满的额度
    private String condition_num;
    //减的额度
    private String discount_num;
    //是否领取，0否1是
    private int is_accept;

    public String getItemTypeValue() {
        return new MineCouponEntity().getTypeName(item_type);
    }

    public String couponDate() {
        return String.format(ResourceUtil.getString(R.string.sharemall_format_coupon_expire),
                DateUtil.strToMMDDPointDate(create_time) + " - " + DateUtil.strToMMDDPointDate(update_time));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public int getUse_type() {
        return use_type;
    }

    public void setUse_type(int use_type) {
        this.use_type = use_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getGive_num() {
        return give_num;
    }

    public void setGive_num(String give_num) {
        this.give_num = give_num;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
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

    public String getExpire_hour() {
        return expire_hour;
    }

    public void setExpire_hour(String expire_hour) {
        this.expire_hour = expire_hour;
    }

    public String getCondition_num() {
        return condition_num;
    }

    public void setCondition_num(String condition_num) {
        this.condition_num = condition_num;
    }

    public String getDiscount_num() {
        return discount_num;
    }

    public void setDiscount_num(String discount_num) {
        this.discount_num = discount_num;
    }

    public int getIs_accept() {
        return is_accept;
    }

    public void setIs_accept(int is_accept) {
        this.is_accept = is_accept;
    }
}
