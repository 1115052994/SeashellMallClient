package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/28
 * 修改备注：
 */
public class VipBrowseRecordEntity extends BaseEntity {


    /**
     * id : 23
     * uid : 1468
     * item_id : 1521
     * click_number : 1
     * create_time : 2019-05-27 11:37:03
     * update_time : 2019-05-27 11:37:03
     * userBehaviorTime : ["2019-05-27 11:36:02"]
     * is_order : 1
     * item_title : 十六色眼影盘网红大地珠哑光裸妆初学者少女系
     * item_price : 89.00
     * item_img : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABWXZMTK01245789_1552023283.png
     */

    private String id;
    private String uid;
    private String item_id;
    //点击数
    private String click_number;
    private String create_time;
    private String update_time;
    //0:未下单  1:已下单
    private int is_order;
    private String item_title;
    //商品价格
    private String item_price;
    //商品图片
    private String item_img;
    //点击时间
    private List<String> userBehaviorTime;

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

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getClick_number() {
        return click_number;
    }

    public void setClick_number(String click_number) {
        this.click_number = click_number;
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

    public int getIs_order() {
        return is_order;
    }

    public void setIs_order(int is_order) {
        this.is_order = is_order;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public List<String> getUserBehaviorTime() {
        return userBehaviorTime;
    }

    public void setUserBehaviorTime(List<String> userBehaviorTime) {
        this.userBehaviorTime = userBehaviorTime;
    }
}
