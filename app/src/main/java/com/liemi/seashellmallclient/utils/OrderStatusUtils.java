package com.liemi.seashellmallclient.utils;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.netmi.baselibrary.utils.ResourceUtil;

/**
 * 类描述：订单状态统一处理返回
 * 创建人：Simple
 * 创建时间：2019/4/2
 * 修改备注：
 */
public class OrderStatusUtils {

    public static OrderStatusUtils getInstance() {
        return OrderStatusUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final OrderStatusUtils instance = new OrderStatusUtils();
    }

    /**
     * @param status      主订单状态
     * @param groupStatus 拼团订单状态
     * @param defaultName 默认状态
     */
    public String getStatusFormat(int status, int groupStatus, String defaultName) {
        //拼团中和拼团失败直接返回
        switch (groupStatus) {
            case GrouponParam.GROUPON_ORDER_FAIL:
                return "拼团失败";
        }
        switch (status) {
            case OrderParam.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_wait_pay);
            case OrderParam.ORDER_WAIT_SEND:
                //三种状态：拼团中；  拼团成功，待发货；  待发货
                return (groupStatus == GrouponParam.GROUPON_ORDER_ING ? ResourceUtil.getString(R.string.sharemall_groupon_order_ing) :
                        groupStatus == GrouponParam.GROUPON_ORDER_SUCCESS ? "拼团成功" + "，" + ResourceUtil.getString(R.string.sharemall_order_wait_send) :
                                ResourceUtil.getString(R.string.sharemall_order_wait_send));
            case OrderParam.ORDER_WAIT_RECEIVE:
                return (groupStatus == GrouponParam.GROUPON_ORDER_SUCCESS ? "拼团成功" + "，" : "") + ResourceUtil.getString(R.string.sharemall_order_wait_receive);
            case OrderParam.ORDER_WAIT_COMMENT:
                return (groupStatus == GrouponParam.GROUPON_ORDER_SUCCESS ? "拼团成功" + "，" : "") + ResourceUtil.getString(R.string.sharemall_order_wait_appraise);
            case OrderParam.ORDER_REFUND_ASK:
                return ResourceUtil.getString(R.string.sharemall_order_refund_apply);
            case OrderParam.ORDER_REFUND_NOT_ALLOW:
                return ResourceUtil.getString(R.string.sharemall_order_refund_apply_fail);
            case OrderParam.ORDER_REFUND_NOW:
                return ResourceUtil.getString(R.string.sharemall_order_refund_ing);
            case OrderParam.ORDER_REFUND_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_order_refund_complete);
            case OrderParam.ORDER_CANCEL:
                return ResourceUtil.getString(R.string.sharemall_cancel_transaction);
            case OrderParam.ORDER_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_transaction_complete);
            case OrderParam.ORDER_PAY_FAIL:
                return ResourceUtil.getString(R.string.sharemall_pay_failure);
        }
        return defaultName;
    }

}
