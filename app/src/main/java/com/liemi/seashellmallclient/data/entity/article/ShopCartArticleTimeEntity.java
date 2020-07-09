package com.liemi.seashellmallclient.data.entity.article;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * Created by Bingo on 2019/1/9.
 */

public class ShopCartArticleTimeEntity extends BaseEntity {
    private String time; //日期
    private List<ShopCartArticleEntity> list;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ShopCartArticleEntity> getList() {
        return list;
    }

    public void setList(List<ShopCartArticleEntity> list) {
        this.list = list;
    }
}
