package com.liemi.seashellmallclient.data.entity.good;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/7
 * 修改备注：
 */
public class GoodsLabelEntity implements Serializable {
    /**
     * name : 哈哈标签
     * item_id : 1272
     */

    private String name;
    private String item_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
