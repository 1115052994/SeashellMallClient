package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/28
 * 修改备注：
 */
public interface GoodsBuyDialogParam {

    //加入购物车
    int GOODS_BUY_ADD_SHOPCART = 0;

    //立即购买
    int GOODS_BUY_IMMEDIATELY = 1;

    //单独购买
    int GROUP_BUY_TYPE_ALONE = 2;

    //发起拼团
    int GROUP_BUY_TYPE_LAUNCH = 3;

    //去参团
    int GROUP_BUY_TYPE_JOIN = 4;

    //普通商品规格选择
    int GOODS_BUY_SPEC_CHOICE = 5;

    //秒杀商品规格选择
    int GOODS_BUY_SPEC_CHOICE_SECKILL = 6;

    //团购商品规格选择
    int GOODS_BUY_SPEC_CHOICE_GROUPON = 7;

    //分享砍价
    int GOODS_BUY_SHARE_BARGAIN = 8;

    //分享砍价选择规格
    int GOODS_BUY_SPEC_CHOICE_BARGAIN = 9;

}
