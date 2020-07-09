package com.liemi.seashellmallclient.data.param;

/**
 * 类描述：拼团常量
 * 创建人：Simple
 * 创建时间：2019/8/29
 * 修改备注：
 */
public interface GrouponParam {


    /**
     * 拼团失败错误码
     */
    int ERROR_CODE_GROUPON_FAIL = 30004;


    //正在拼团
    int GROUPON_ORDER_ING = 0;
    //拼团成功
    int GROUPON_ORDER_SUCCESS = 1;
    //拼团失败
    int GROUPON_ORDER_FAIL = 2;

    //推广团实体类
    String EXTENSION_GROUP_ENTITY = "groupDetailed";

    //拼团订单
    int ORDER_TYPE_GROUPON = 9;

    //砍价订单
    int ORDER_TYPE_BARGAIN = 10;
}
