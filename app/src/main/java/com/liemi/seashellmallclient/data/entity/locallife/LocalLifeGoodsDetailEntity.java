package com.liemi.seashellmallclient.data.entity.locallife;

import android.text.TextUtils;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

public class LocalLifeGoodsDetailEntity extends BaseEntity {
    /*
    * "item_id": "11",//商品ID
           "shop_id": "241",//店铺ID
           "title": "测试商品1号",//名称
           "img_url": [
               "https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg"
           ],//图片
           "price": "11.00",//单价
           "old_price": "11.00",//现价
           "stock": "95",//库存
           "deal_num": "1",//销量
           "rich_text": "<p>1111111111111</p>",//详情
           "deal_num_false": "0",//虚假销量
           "status": "5",//状态
           "sort": "1",//排序
           "purchase_note": "111111111",//购买规则
           "start_date": "2019-10-09",//开始日期
           "end_date": "2019-10-31",//截止日期
           "score": "1650"//获得积分
           "shop": {    }
    *
    * */
    private String item_id;
    private String shop_id;
    private String title;
    private List<String> img_url;
    private String price;
    private String old_price;
    private String stock;
    private String deal_num;
    private String rich_text;
    private String deal_num_false;
    private String status;
    private String sort;
    private String purchase_note;
    private String start_date;
    private String end_date;
    private String score;
    private String currency;
    private LocalLifeShop shop;

    public String getCurrency() {
        return TextUtils.isEmpty(currency)?"":currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
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

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public String getDeal_num_false() {
        return deal_num_false;
    }

    public void setDeal_num_false(String deal_num_false) {
        this.deal_num_false = deal_num_false;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPurchase_note() {
        return purchase_note;
    }

    public void setPurchase_note(String purchase_note) {
        this.purchase_note = purchase_note;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public LocalLifeShop getShop() {
        return shop;
    }

    public void setShop(LocalLifeShop shop) {
        this.shop = shop;
    }

    public class LocalLifeShop {
        /*
        *  "id": "241",//店铺ID
            "logo_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/2019/10/10/14/04/40/f503fcfc7e05422b921880713ace6072.jpg",//店铺logo
            "img_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/2019/09/28/10/29/16/03a90a6797a64620a20ac6c966afbad6.jpg",//店铺展示图
            "name": "near的测试店铺",//店铺名称
            "remark": "1111111111",//店铺简介
            "full_name": "浙江省-杭州市-江干区 新加坡科技园",//店铺地址
            "content": "",
            "shop_remind_tel": "",//电话
            "qrcode": null,
            "sum_item": "0",
            "rccode": "",
            "wxcode": "",
            "status": "1",
            "longitude": "120.36977",//经纬度
            "latitude": "120.36977"//经纬度
            "opening_hours": null,//营业时间
              "distance": 0//距离
        * */
        private String id;
        private String logo_url;
        private String img_url;
        private String name;
        private String remark;
        private String full_name;
        private String content;
        private String shop_remind_tel;
        private String qrcode;
        private String sum_item;
        private String rccode;
        private String wxcode;
        private String status;
        private String longitude;
        private String latitude;
        private String opening_hours;
        private String distance;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShop_remind_tel() {
            return shop_remind_tel;
        }

        public void setShop_remind_tel(String shop_remind_tel) {
            this.shop_remind_tel = shop_remind_tel;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getSum_item() {
            return sum_item;
        }

        public void setSum_item(String sum_item) {
            this.sum_item = sum_item;
        }

        public String getRccode() {
            return rccode;
        }

        public void setRccode(String rccode) {
            this.rccode = rccode;
        }

        public String getWxcode() {
            return wxcode;
        }

        public void setWxcode(String wxcode) {
            this.wxcode = wxcode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(String opening_hours) {
            this.opening_hours = opening_hours;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
