package com.liemi.seashellmallclient.data.entity.locallife;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class LocalLifeGoodsEntity extends BaseEntity {
    /*
        * "item_id": "11",//店铺ID
                "title": "测试商品1号",//商品名称
                "img_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg",//图片
                "price": "11.00",//现价
                "deal": "1",//销量
                "start_date": "2019-10-09",//有效期开始
                "end_date": "2019-10-31",//截止日期
                "old_price": "11.00"//原价

                "shop_id": "241",//店铺ID

                "stock": "95",//库存
                "deal_num": "1",//销量
                "status": "5",//状态 5已上架

        * */
    private String item_id;
    private String title;
    private String img_url;
    private String price;
    private String deal;
    private String start_date;
    private String end_date;
    private String old_price;
    private String shop_id;
    private String stock;
    private String deal_num;
    private String status;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeal_num() {
        return deal_num;
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

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }
}
