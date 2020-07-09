package com.liemi.seashellmallclient.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

/**
 * Created by Bingo on 2019/1/28.
 */

public class CommendGoodEntity extends BaseEntity {
    /**
     * "item_id": "1056", //商品主键
     "img_url": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15206678485607.png", //商品图片
     "title": "新上架商品", //商品标题
     "price": "10.00", //商品价格
     "remark": "新上架商品", //商品标注
     "shop_id": "151" //商品店铺
     "old_price": "12.00" //商品原价
     */


    private String item_id;
    private String img_url;
    private String title;
    private String price;
    private String remark;
    private String shop_id;
    private String old_price;

    public String getShowPrice() {
        return FloatUtils.formatMoney(price);
    }

    public String getOld_price() {
        return FloatUtils.formatMoney(old_price);
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }
}
