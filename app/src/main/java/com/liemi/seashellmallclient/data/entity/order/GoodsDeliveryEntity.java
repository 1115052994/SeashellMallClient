package com.liemi.seashellmallclient.data.entity.order;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/28
 * 修改备注：
 */
public class GoodsDeliveryEntity {


    /**
     * item_id : 1751
     * is_buy : 1
     */

    private String item_id;
    //是否能购买  0：否  1：是
    private int is_buy;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getIs_buy() {
        return is_buy;
    }

    public void setIs_buy(int is_buy) {
        this.is_buy = is_buy;
    }
}
