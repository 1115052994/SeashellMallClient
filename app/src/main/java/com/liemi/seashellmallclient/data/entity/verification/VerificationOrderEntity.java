package com.liemi.seashellmallclient.data.entity.verification;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

/**
 * 我的核销订单信息
 */
public class VerificationOrderEntity extends VerificationOrderDetailEntity {
    /*
    *                 "id": "10737",//订单ID
                "order_no": "366596952919494785",//订单编号
                "create_time": "2019-10-09 14:45:24",//创建时间
                "status": "0",//状态值
                "amount": "11.00",//订单总价
                "img_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg",//商品展示图
                "shop": {
                    "name": "near的测试店铺",//店铺名称
                    "id": "241"//店铺ID
                }
    * */
    private String id;
    private String order_no;
    private String create_time;
    private String status;
    private String amount;
    private String img_url;
    private String spu_name;
    private Shop shop;

    private String statusFormat;

    public String getStatusFormat() {
        switch (Strings.toInt(status)) {
            case OrderParam.VERIFICATION_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_wait_pay);
            case OrderParam.VERIFICATION_WAIT_USE:
                return ResourceUtil.getString(R.string.order_verification_wait);
            case OrderParam.VERIFICATION_WAIT_COMMENT:
                return ResourceUtil.getString(R.string.sharemall_order_wait_appraise);
            case OrderParam.VERIFICATION_REFUND_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_order_refund_complete);
            case OrderParam.VERIFICATION_SUCCESS:
                return ResourceUtil.getString(R.string.order_verification_success);
        }
        return statusFormat;
    }

    //左边按钮显示的文字
    private String leftButtonStr;
    //右边按钮显示的文字
    private String rightButtonStr;


    public String getLeftButtonStr() {
        switch (Strings.toInt(status)) {
            case OrderParam.VERIFICATION_WAIT_COMMENT:
                return ResourceUtil.getString(R.string.local_life_comment);
          /*  case Constant.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_cancel);
            case Constant.ORDER_WAIT_SEND:
                return "";
            case Constant.ORDER_WAIT_RECEIVE:
                return ResourceUtil.getString(R.string.sharemall_order_logistics);*/
        }
        return "";
    }

    public String getRightButtonStr() {
        switch (Strings.toInt(status)) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case OrderParam.VERIFICATION_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_go_pay);
            case OrderParam.VERIFICATION_WAIT_COMMENT:
            case OrderParam.VERIFICATION_REFUND_SUCCESS:
            case OrderParam.VERIFICATION_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_delete);
            default:
                return "";
        }
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public void setLeftButtonStr(String leftButtonStr) {
        this.leftButtonStr = leftButtonStr;
    }

    public void setRightButtonStr(String rightButtonStr) {
        this.rightButtonStr = rightButtonStr;
    }

    public void setStatusFormat(String statusFormat) {
        this.statusFormat = statusFormat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

   /* public Shop getShop() {
        return shop;
    }*/

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public class Shop {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
