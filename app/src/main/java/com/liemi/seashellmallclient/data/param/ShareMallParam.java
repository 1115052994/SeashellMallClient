package com.liemi.seashellmallclient.data.param;

import static com.netmi.baselibrary.data.Constant.BASE_API;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/26
 * 修改备注：
 */
public interface ShareMallParam {

    //是否开启分销功能
    boolean DISTRIBUTOR_OPEN = true;

    /**
     * 智齿客服key
     */
    String SOBOT_KEY = "d66f4daede9249a79332c64f5bd1f416";

    /**
     * 分享商品
     */
    String SHARE_VIP_GIFT = BASE_API + "netmi-shop-h5/dist/#/newGiftNew?share_code=";

    /**
     * 首页和店铺楼层数据的缓存，用于暂时解决RecyclerView嵌套造成的位移问题
     */
    int FLOOR_VIEW_CACHE = 50;


}
