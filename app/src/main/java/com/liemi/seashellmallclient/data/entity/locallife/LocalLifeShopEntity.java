package com.liemi.seashellmallclient.data.entity.locallife;

import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

import java.util.List;

public class LocalLifeShopEntity extends BaseEntity {
    /*
    *   "id": "236",//店铺ID
                "img_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/2019/09/02/10/58/41/976be1d62bb1411e807dabbac50fa843.png",//展示图
                "name": "cj",//名称
                "full_name": "",//地址
                "star": "0",//星级 0.5代表半颗星 1代表一颗星
                "longitude": null,//经度
                "latitude": null,//纬度
                "category": "测试",//分类名称
                "distance": 100//距离
            is_collect:0  //是否收藏 1是0否
        "remark": "1111111111",//简介
        "opening_hours": "10:14~23:14",//营业时间
        "shop_remind_tel": "18856855441",//电话
        "score_rate": "15000",//积分比例
        "item": [ ] //商品
    }
    * */
    private String id;
    private String img_url;
    private List<String> itemImgs;
    private String short_video_url;
    private String license_url; //营业执照
    private String name;
    private String full_name;
    private String star;
    private String distance;
    private String category;
    private String longitude;
    private String latitude;
    private String remark;
    private String opening_hours;
    private String shop_remind_tel;
    private String score_rate;
    private List<LocalLifeGoodsEntity> item;
    private CommentEntity comment;
    private int is_collect;
    private int startCount;

    public int getStartCount() {
        float v = FloatUtils.string2Float(getStar());
        startCount = FloatUtils.floatToInt(v);
        return startCount;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public CommentEntity getComment() {
        return comment;
    }

    public void setComment(CommentEntity comment) {
        this.comment = comment;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public String getLicense_url() {
        return license_url;
    }

    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }

    public String getShort_video_url() {
        return short_video_url;
    }

    public void setShort_video_url(String short_video_url) {
        this.short_video_url = short_video_url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getShop_remind_tel() {
        return shop_remind_tel;
    }

    public void setShop_remind_tel(String shop_remind_tel) {
        this.shop_remind_tel = shop_remind_tel;
    }

    public List<LocalLifeGoodsEntity> getItem() {
        return item;
    }

    public void setItem(List<LocalLifeGoodsEntity> item) {
        this.item = item;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public String getScore_rate() {
        return score_rate;
    }

    public void setScore_rate(String score_rate) {
        this.score_rate = score_rate;
    }
}
