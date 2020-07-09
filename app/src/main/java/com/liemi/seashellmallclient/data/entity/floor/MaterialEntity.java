package com.liemi.seashellmallclient.data.entity.floor;

import android.text.TextUtils;
import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaterialEntity extends BaseEntity implements Serializable {

    //类型
    public static final int SHARE_COMMENT_RECOMMEND = 1;//商品推荐
    public static final int SHARE_COMMENT_MARKETING = 2;//营销素材
    public static final int SHARE_COMMENT_NEW_USER = 3;//新手必发

    private int id;//素材主键
    private int item_id;//商品主键
    private String code;//二维码
    private String rich_text;//富文本内容
    private String create_time;//创建时间
    private List<String> imgs;//图片
    private String head_url;//头像
    private String nickname;//昵称
    private String share;//预估赚
    private int is_related_item;
    private String total_num;

    public String getShare() {
        String s = share;
        if (!TextUtils.isEmpty(share)) {
            if (s.indexOf(".") > 0) {
                //正则表达
                s = s.replaceAll("0+?$", "");//去掉后面无用的零
                s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
            }
        }
        return s;
    }

    public List<String> getImgs() {
        if (imgs == null) {
            imgs = new ArrayList<>();
        }
        return imgs;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }


    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getIs_related_item() {
        return is_related_item;
    }

    public void setIs_related_item(int is_related_item) {
        this.is_related_item = is_related_item;
    }
}

