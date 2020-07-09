package com.netmi.baselibrary.data.entity;

import com.netmi.baselibrary.utils.FloatUtils;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/18 10:27
 * 修改备注：
 */
public class BaseEntity implements Serializable {


    //将字符串加上人民币符号
    public String formatMoney(String money) {
        return FloatUtils.formatMoney(money);
    }

    public String formatMoney(double money) {
        return FloatUtils.formatMoney(money);
    }

}
