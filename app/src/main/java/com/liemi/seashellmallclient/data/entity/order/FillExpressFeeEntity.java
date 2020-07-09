package com.liemi.seashellmallclient.data.entity.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/28
 * 修改备注：
 */
public class FillExpressFeeEntity {

    private String address_id;

    private List<ItemListBean> item_list = new ArrayList<>();

    //判断商品是否在配送区域内
    private boolean isBuy = true;

    public List<String> getGoodsIds() {
        List<String> list = new ArrayList<>();
        for (ItemListBean bean : item_list) {
            list.add(bean.getItem_id());
        }
        return list;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public List<ItemListBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    public static class ItemListBean {

        private String num;
        private String item_id;

        public ItemListBean(String item_id, String num) {
            this.item_id = item_id;
            this.num = num;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }
}
