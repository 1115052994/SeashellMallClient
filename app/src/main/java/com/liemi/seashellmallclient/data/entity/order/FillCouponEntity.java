package com.liemi.seashellmallclient.data.entity.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/4
 * 修改备注：
 */
public class FillCouponEntity {

    private List<ItemDataBean> item_data = new ArrayList<>();

    public List<ItemDataBean> getItem_data() {
        return item_data;
    }

    public void setItem_data(List<ItemDataBean> item_data) {
        this.item_data = item_data;
    }

    public static class ItemDataBean {

        private String ivid;
        private String num;

        public ItemDataBean(String ivid, String num) {
            this.ivid = ivid;
            this.num = num;
        }

        public String getIvid() {
            return ivid;
        }

        public void setIvid(String ivid) {
            this.ivid = ivid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
