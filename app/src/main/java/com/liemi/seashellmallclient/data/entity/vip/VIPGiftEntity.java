package com.liemi.seashellmallclient.data.entity.vip;

import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;

/**
 * Created by Bingo on 2018/12/29.
 */

public class VIPGiftEntity extends GoodsListEntity {

    private boolean isHiddenBottom; //是否隐藏底部购买按钮

    private String gift_name;

    private int gift_level;

    public boolean isHiddenBottom() {
        return isHiddenBottom;
    }

    public void setHiddenBottom(boolean hiddenBottom) {
        isHiddenBottom = hiddenBottom;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public int getGift_level() {
        return gift_level;
    }

    public void setGift_level(int gift_level) {
        this.gift_level = gift_level;
    }
}
