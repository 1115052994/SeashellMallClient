package com.liemi.seashellmallclient.data.entity.good;

import com.google.gson.annotations.SerializedName;
import com.liemi.seashellmallclient.widget.countdown.CountDownItem;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/4
 * 修改备注：
 */
public abstract class AbsGoodsDetails extends CountDownItem implements Serializable {


    private String item_id;

    private String img_url;

    private String title;

    @SerializedName(value = "value_names", alternate = {"skus"})
    private String value_names;

    private String num;

    //是否秒杀商品
    public abstract boolean isSecKill();

    //是否跨境购商品
    public abstract boolean isAbroad();

    //是否拼团商品
    public abstract boolean isGroup();

    //显示价格
    public abstract String getShowPrice();

    @Override
    public long initMillisecond() {
        return 0;
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

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
