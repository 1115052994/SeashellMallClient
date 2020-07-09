package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * Created by Bingo on 2019/1/2.
 */

public class VIPIncomeListEntity extends BaseEntity {
    /**
     * "id": "715",
     * "uid": "921",
     * "shop_id": "0",
     * "type": "1",
     * "price": "100.00",//金额
     * "status": "2",//1未发放   2已发放   3拒绝发放
     * "relation_id": "1465",
     * "create_time": "2018-12-26",//时间
     * "update_time": "2018-12-22 17:49:46",
     * "order_id": "1655",
     * "is_cash": "1",
     * "me_cash_id": "0",
     * "type_name": "销售奖励"
     */

    private String id;
    private String price;
    private String create_time;
    private String update_time;
    private String title;
    private String status;

    public String auditName() {
        String s = "";
        switch (status) {
            case "1":
                s = "申请中";
                break;
            case "2":
                s = "审核失败";
                break;
            case "3":
                s = "审核通过";
                break;
            case "5":
                s = "成功到帐";
                break;
            case "6":
                s = "提现失败";
                break;
        }

        return s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
