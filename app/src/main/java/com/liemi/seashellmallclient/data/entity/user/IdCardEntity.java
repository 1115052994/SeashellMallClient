package com.liemi.seashellmallclient.data.entity.user;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/16
 * 修改备注：
 */
public class IdCardEntity extends BaseEntity {

    private String card_no;
    private String card_name;

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }
}
