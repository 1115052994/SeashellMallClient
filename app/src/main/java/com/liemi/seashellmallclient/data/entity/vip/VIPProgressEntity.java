package com.liemi.seashellmallclient.data.entity.vip;

/**
 * Created by Bingo on 2018/12/29.
 * 获取用户成长值信息
 */
public class VIPProgressEntity {
    private int growth; //用户成长值
    private int less_growth; //距离VIP的差值

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getLess_growth() {
        return less_growth;
    }

    public void setLess_growth(int less_growth) {
        this.less_growth = less_growth;
    }
}
