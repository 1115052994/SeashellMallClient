package com.liemi.seashellmallclient.widget.countdown;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/16
 * 修改备注：
 */
public abstract class CountDownItem extends BaseEntity implements Serializable, CountDownMillisecond {

    private long millisecond = -10;

    public long getMillisecond() {
        if (millisecond == -10) {
            millisecond = initMillisecond();
        }
        return millisecond;
    }

    public void setMillisecond(long millisecond) {
        this.millisecond = millisecond;
    }

    public abstract long initMillisecond();

}
