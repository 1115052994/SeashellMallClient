package com.liemi.seashellmallclient.data.entity.floor;

import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.util.Calendar;


public class SeckillItemList extends BaseEntity {
    /**
     * item_id : 1350
     * seckill_img : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/WZMNTHK012345678_1545378893.jpg
     * seckill_price : 33.00
     * seckill_price_old : 22.00
     * start_time : 2018-12-23 18:30:00
     * end_time : 2018-12-23 22:30:00
     * shop_id : 195
     * title : 定制窗帘布卧室窗帘遮光窗帘成品平面落地窗客厅简约现代田园飘窗
     * remark : 窗帘
     * stock : 237
     * deal_num : 11
     * status : 5
     * item_type : 0
     * is_seckill : 1
     * percent : null
     */

    private String item_id;
    private String seckill_img;
    private String price;
    private String old_price;
    private String start_time;
    private String end_time;
    private String shop_id;
    private String title;
    private String remark;
    private String stock;
    private String deal_num;
    private String status;
    private String item_type;
    private String is_seckill;
    private String share;
    //是否海外商品：0 不是，1是
    private int is_abroad;

    public String getTitle() {
        return is_abroad == 1 ? ResourceUtil.getString(R.string.sharemall_title_cross_border_purchase) + title : title;
    }

    public boolean getIsEnd() {
        if (DateUtil.strToLong(end_time) > Calendar.getInstance().getTimeInMillis()) {
            return false;
        } else {
            return true;
        }
    }

    public String getDeal_num() {
        return FloatUtils.toBigUnit(deal_num);
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSeckill_img() {
        return seckill_img;
    }

    public void setSeckill_img(String seckill_img) {
        this.seckill_img = seckill_img;
    }

    public int getIs_abroad() {
        return is_abroad;
    }

    public void setIs_abroad(int is_abroad) {
        this.is_abroad = is_abroad;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
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

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getIs_seckill() {
        return is_seckill;
    }

    public void setIs_seckill(String is_seckill) {
        this.is_seckill = is_seckill;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}

