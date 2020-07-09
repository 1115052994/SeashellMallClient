package com.liemi.seashellmallclient.data.entity;
import com.netmi.baselibrary.data.entity.PageEntity;

/**
 * Created by Bingo on 2019/2/16.
 */

public class ShareMallPageEntity<T> extends PageEntity<T> {

    private String total_num;

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }
}
