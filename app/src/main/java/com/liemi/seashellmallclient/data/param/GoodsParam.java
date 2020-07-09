package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface GoodsParam {



    //商品没有营销活动
    int GOODS_ACTIVITY_NORMAL = 0;
    //秒杀活动商品
    int GOODS_ACTIVITY_SECOND_KILL = 1;
    //拼团活动商品
    int GOODS_ACTIVITY_GROUPON = 2;
    //砍价活动商品
    int GOODS_ACTIVITY_BARGAIN = 3;

    //VIP礼包商品类型
    int GOODS_VIP_GIFT = 4;

    /**
     * 商品分类
     */
    String MC_ID = "category_mc_id";

    String MC_NAME = "category_mc_name";

    String MC_HOT_GOODS = "category_mc_hot_goods";

    String MC_NEW_GOODS = "category_mc_new_goods";

    String MC_COUPON_ID = "category_mc_coupon_id";

    String STORE_ID = "store_id";

    /**
     * 商品排序
     */
    String SORT_ASC = "SORT_ASC", SORT_DESC = "SORT_DESC";

    //评论是否有图
    String COMMENT_FLAG = "comment_flag";

    //商品Id
    String ITEM_ID = "item_id";
    String CURRENT_TAB = "current_tab";
    String ITEM_TYPE = "itemType";
    String GOODS_ENTITY = "goodsEntity";

    //购物车
    String SHOP_CARTS = "shop_carts";

    //促销活动
    String PROMOTIONAL_TYPE = "PromotionalType";
    //新人必买
    int PROMOTIONAL_TYPE_NEW = 12;
    //VIP专区
    int PROMOTIONAL_TYPE_VIP = 13;

}
