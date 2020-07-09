package com.liemi.seashellmallclient.data.entity.user;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.CouponParam;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.List;

import static com.netmi.baselibrary.utils.ResourceUtil.getString;

/*
 * 我的优惠券
 * */
public class MineCouponEntity extends BaseEntity implements Serializable, Comparable<MineCouponEntity> {
    private String name;
    private String rule;
    private int type;
    //优惠券使用状态：1.未使用、2.已过期、3.已使用、4.分享被领取、5.分享中
    private int status;
    private String condition_num;
    private String discount_num;
    @SerializedName(value = "coupon_id", alternate = {"id"})
    private int coupon_id;
    //有效期
    private String expire;
    //开始时间
    private String start_time;
    //结束时间
    private String end_time;
    private String invite_name;//邀请人名字
    private String invite_time;//邀请人时间

    //优惠券对应使用的店铺
    private String shop_id;
    //优惠券对应使用的商品
    private List<String> item_id;
    //优惠券模板类型：1、平台券，2、店铺内全部商品券，3、店铺内部分商品券；4品类券
    private int item_type;

    private int cu_id;
    private String receive_name;//领取人名
    private String receive_time;//领取时间
    //优惠券是否被选中，只有未使用的优惠券才有
    private boolean select = false;

    public String getTypeName(int item_type) {
        this.item_type = item_type;
        return getTypeName();
    }

    public String getTypeName() {
        switch (item_type) {
            case CouponParam.COUPON_TYPE_PLATFORM:
                return getString(R.string.sharemall_coupon_use_platform);
            case CouponParam.COUPON_TYPE_STORE:
                return getString(R.string.sharemall_coupon_use_store);
            case CouponParam.COUPON_TYPE_GOODS:
                return getString(R.string.sharemall_coupon_use_goods);
            case CouponParam.COUPON_TYPE_CATEGORY:
                return getString(R.string.sharemall_coupon_use_category);
        }
        return getString(R.string.sharemall_mine_coupon_page);
    }

    @Override
    public int compareTo(@NonNull MineCouponEntity o) {
        return Double.compare(Strings.toDouble(o.getDiscount_num()), Strings.toDouble(getDiscount_num()));
    }

    public String getExpire() {
        if (TextUtils.isEmpty(expire)) {
            expire = DateUtil.strToMMDDDate(start_time) + " ~ " + DateUtil.strToMMDDDate(end_time);
        }
        return expire;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
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

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getInvite_name() {
        return invite_name;
    }

    public void setInvite_name(String invite_name) {
        this.invite_name = invite_name;
    }

    public int getCu_id() {
        return cu_id;
    }

    public void setCu_id(int cu_id) {
        this.cu_id = cu_id;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getInvite_time() {
        return invite_time;
    }

    public void setInvite_time(String invite_time) {
        this.invite_time = invite_time;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<String> getItem_id() {
        return item_id;
    }

    public void setItem_id(List<String> item_id) {
        this.item_id = item_id;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }
}
