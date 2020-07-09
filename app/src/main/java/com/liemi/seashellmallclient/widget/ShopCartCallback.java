package com.liemi.seashellmallclient.widget;

import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;

/**
 * Created by Bingo on 2018/11/27.
 */

public interface ShopCartCallback {

    void doDelete(int position);

    void doUpdateCartNum(final GoodsDetailedEntity goodEntity, final float num);

    void childCheck();
}
