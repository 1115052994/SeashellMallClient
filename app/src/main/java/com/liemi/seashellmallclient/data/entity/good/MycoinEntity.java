package com.liemi.seashellmallclient.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class MycoinEntity extends BaseEntity {


    /**
     * coin : 0
     * coupon : 2
     */

    //1000积分抵扣1元
    public static final int DEDUCTION_INTEGRAL = 1000;

    private int coin;
    private int coupon;
    //可抵扣的金额
    private int cash;

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
