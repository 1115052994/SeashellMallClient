package com.liemi.seashellmallclient.data.entity.order;

import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.AddressEntity;
import com.liemi.seashellmallclient.data.entity.InvoiceEntity;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.utils.OrderStatusUtils;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.entity.good.MycoinEntity.DEDUCTION_INTEGRAL;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/4
 * 修改备注：
 */
public class OrderDetailedEntity extends BaseEntity implements Serializable {

    private String id;
    private String main_order_id;
    private String order_id;
    private int goods_num;
    private String goods_amount;
    private String order_amount;
    private String order_pay_amount;
    private String order_pay_score;
    private String order_coupon_amount;
    private String order_logistics_freight;
    private String status_name;
    private int status;
    private String order_no;
    private String pay_end_time;
    private String to_name;
    private String to_tel;
    private String to_address;
    //下单时间
    private String create_time;
    //付款时间
    private String pay_time;
    //发货时间
    private String deliver_time;
    //成交时间
    private String finish_time;
    //订单关闭时间
    private String close_time;
    private String is_remind;
    private String logistics_no;
    private String logistics_company_code;
    private String pay_channel;
    private String last_pay_channel;
    private String now_time;
    private String card_no;
    private String card_name;
    private List<MainOrdersBean> MainOrders;
    private InvoiceEntity orderInvoice;
    private String type;
    private String pay_hai;

    public MainOrdersBean getMainOrder() {
        if (Strings.isEmpty(MainOrders)) {
            return new MainOrdersBean();
        }
        return MainOrders.get(0);
    }

    public String getLeftButtonStr() {
        switch (status) {
            case OrderParam.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_cancel);
            case OrderParam.ORDER_WAIT_SEND:
                return "";
            case OrderParam.ORDER_WAIT_RECEIVE:
                return ResourceUtil.getString(R.string.sharemall_order_logistics);
        }
        return "";
    }

    public String getRightButtonStr() {
        //拼团中的订单，显示邀请好友，失败显示删除订单
        int groupStatus = isGroupOrder();

        if (groupStatus == GrouponParam.GROUPON_ORDER_FAIL) {
            return ResourceUtil.getString(R.string.sharemall_order_delete);
        }

        switch (status) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case OrderParam.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_go_pay);
            case OrderParam.ORDER_WAIT_SEND:
                return groupStatus == GrouponParam.GROUPON_ORDER_ING ? ResourceUtil.getString(R.string.sharemall_groupon_invite_friend_join) : ResourceUtil.getString(R.string.sharemall_order_remind);
            case OrderParam.ORDER_WAIT_RECEIVE:
                return ResourceUtil.getString(R.string.sharemall_order_confirm_accept);
            case OrderParam.ORDER_WAIT_COMMENT:
                return ResourceUtil.getString(R.string.sharemall_order_go_comment);
            case OrderParam.ORDER_CANCEL:
            case OrderParam.ORDER_SUCCESS:
                return ResourceUtil.getString(R.string.sharemall_order_delete);
            default:
                return "";
        }
    }

    public String getStatusFormat() {
        return OrderStatusUtils.getInstance().getStatusFormat(status, isGroupOrder(), status_name);
    }

    //是否拼团订单
    public int isGroupOrder() {
        return -1;
    }

    public List<OrderSkusEntity> getGoods() {
        List<OrderSkusEntity> goods = new ArrayList<>();
        if (!Strings.isEmpty(MainOrders)) {
            for (MainOrdersBean entity : MainOrders) {
                if (!Strings.isEmpty(entity.getOrderSkus())) {
                    goods.addAll(entity.getOrderSkus());
                }
            }
        }
        return goods;
    }

    public AddressEntity getAddressEntity() {
        AddressEntity addressEntity = new AddressEntity();
        if (!Strings.isEmpty(to_tel)) {
            addressEntity.setTel(to_tel);
        }
        if (!Strings.isEmpty(to_name)) {
            addressEntity.setName(to_name);
        }
        addressEntity.setFull_name(to_address);
        return addressEntity;
    }

    public String getFormatPayChannel() {
        switch (pay_channel) {
            case "0":
                return ResourceUtil.getString(R.string.sharemall_wx_pay);
            case "1":
                return ResourceUtil.getString(R.string.sharemall_ali_pay);
            case "2":
                return ResourceUtil.getString(R.string.sharemall_apple_pay);
            case "3":
                return ResourceUtil.getString(R.string.sharemall_integral_pay);
            case "4":
                return ResourceUtil.getString(R.string.sharemall_eth);
            case "10":
                return ResourceUtil.getString(R.string.sharemall_balance_payment);
        }
        return "";
    }

    public String getOrderPayScoreReal() {
        if (Strings.isEmpty(order_pay_score)) {
            return formatMoney("0.00");
        }
        return "-" + formatMoney(Strings.toDouble(order_pay_score) / DEDUCTION_INTEGRAL);
    }

    public int getStatusImage() {
        //0-未付款1-待发货2-待收货3-待评价4-退货申请 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
        int groupStatus = isGroupOrder();

        switch (groupStatus) {
            case GrouponParam.GROUPON_ORDER_FAIL:
                return R.mipmap.sharemall_ic_order_group_fail;
        }
        switch (status) {
            case OrderParam.ORDER_WAIT_PAY:
                return R.mipmap.sharemall_ic_order_wait_pay;
            case OrderParam.ORDER_WAIT_SEND:
                return groupStatus == GrouponParam.GROUPON_ORDER_SUCCESS ? R.mipmap.sharemall_ic_order_group_success : R.mipmap.sharemall_ic_order_wait_send;
            case OrderParam.ORDER_WAIT_RECEIVE:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case OrderParam.ORDER_WAIT_COMMENT:
                return R.mipmap.sharemall_ic_order_wait_comment;
            case OrderParam.ORDER_CANCEL:
                return R.mipmap.sharemall_ic_order_cancel;
            default:
                return R.mipmap.sharemall_ic_order_finish;
        }
    }

    private String getHideText(String content, int start, int end) {
        if (!TextUtils.isEmpty(content) && !content.contains("*")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                if (i > start && i < end) {
                    sb.append("*");
                } else {
                    sb.append(content.charAt(i));
                }
            }
            content = sb.toString();
        }
        return content;
    }

    public String getPay_hai() {
        return pay_hai;
    }

    public void setPay_hai(String pay_hai) {
        this.pay_hai = pay_hai;
    }

    public String getCard_no() {
        return getHideText(card_no, 5, card_no.length() - 4);
    }

    public String getCard_name() {
        return getHideText(card_name, 0, card_name.length());
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_order_id() {
        return main_order_id;
    }

    public void setMain_order_id(String main_order_id) {
        this.main_order_id = main_order_id;
    }

    public InvoiceEntity getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(InvoiceEntity orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_pay_amount() {
        return order_pay_amount;
    }

    public void setOrder_pay_amount(String order_pay_amount) {
        this.order_pay_amount = order_pay_amount;
    }

    public String getOrder_pay_score() {
        return order_pay_score;
    }

    public void setOrder_pay_score(String order_pay_score) {
        this.order_pay_score = order_pay_score;
    }

    public String getOrder_coupon_amount() {
        return order_coupon_amount;
    }

    public void setOrder_coupon_amount(String order_coupon_amount) {
        this.order_coupon_amount = order_coupon_amount;
    }

    public String getOrder_logistics_freight() {
        return order_logistics_freight;
    }

    public void setOrder_logistics_freight(String order_logistics_freight) {
        this.order_logistics_freight = order_logistics_freight;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPay_end_time() {
        return pay_end_time;
    }

    public void setPay_end_time(String pay_end_time) {
        this.pay_end_time = pay_end_time;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_tel() {
        return to_tel;
    }

    public void setTo_tel(String to_tel) {
        this.to_tel = to_tel;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_remind() {
        return is_remind;
    }

    public void setIs_remind(String is_remind) {
        this.is_remind = is_remind;
    }

    public String getLogistics_no() {
        return logistics_no;
    }

    public void setLogistics_no(String logistics_no) {
        this.logistics_no = logistics_no;
    }

    public String getLogistics_company_code() {
        return logistics_company_code;
    }

    public void setLogistics_company_code(String logistics_company_code) {
        this.logistics_company_code = logistics_company_code;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getLast_pay_channel() {
        return last_pay_channel;
    }

    public void setLast_pay_channel(String last_pay_channel) {
        this.last_pay_channel = last_pay_channel;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public List<MainOrdersBean> getMainOrders() {
        return MainOrders;
    }

    public void setMainOrders(List<MainOrdersBean> MainOrders) {
        this.MainOrders = MainOrders;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getDeliver_time() {
        return deliver_time;
    }

    public void setDeliver_time(String deliver_time) {
        this.deliver_time = deliver_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class MainOrdersBean extends BaseEntity implements Serializable {

        private String id;
        private String order_id;
        private String order_no;
        private String shop_id;
        private String address_id;
        private String product_count;
        private String amount;
        private String hand_balance_amount;
        private String coupon_amount;
        private String pay_score;
        private String logistics_freight;
        private String logistics_no;
        private String logistics_company_code;
        private String earn_score;
        private String pay_time;
        private String remark;
        private String status;
        private String type;
        private String seller_message;
        private String is_remind;
        private int display_price;
        private String to_name;
        private String to_tel;
        private String to_address;
        private String create_time;
        private String update_time;
        private String pay_channel;
        private String amount_eth;
        private StoreEntity shop;
        private String status_name;
        private List<OrderSkusEntity> orderSkus;


        public String getOrderPayScoreReal() {
            if (Strings.isEmpty(pay_score)) {
                return formatMoney("0.00");
            }
            return "-" + formatMoney(Strings.toDouble(pay_score) / DEDUCTION_INTEGRAL);
        }
        public String getShowPrice() {
            return FloatUtils.formatMoney(amount);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getShop_id() {
            /*if(shop != null) {
                return shop.getId();
            }*/
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public String getProduct_count() {
            return product_count;
        }

        public void setProduct_count(String product_count) {
            this.product_count = product_count;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHand_balance_amount() {
            return hand_balance_amount;
        }

        public void setHand_balance_amount(String hand_balance_amount) {
            this.hand_balance_amount = hand_balance_amount;
        }

        public String getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(String coupon_amount) {
            this.coupon_amount = coupon_amount;
        }

        public String getPay_score() {
            return pay_score;
        }

        public void setPay_score(String pay_score) {
            this.pay_score = pay_score;
        }

        public String getLogistics_freight() {
            return logistics_freight;
        }

        public void setLogistics_freight(String logistics_freight) {
            this.logistics_freight = logistics_freight;
        }

        public String getLogistics_no() {
            return logistics_no;
        }

        public void setLogistics_no(String logistics_no) {
            this.logistics_no = logistics_no;
        }

        public String getLogistics_company_code() {
            return logistics_company_code;
        }

        public void setLogistics_company_code(String logistics_company_code) {
            this.logistics_company_code = logistics_company_code;
        }

        public String getEarn_score() {
            return earn_score;
        }

        public void setEarn_score(String earn_score) {
            this.earn_score = earn_score;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSeller_message() {
            return seller_message;
        }

        public void setSeller_message(String seller_message) {
            this.seller_message = seller_message;
        }

        public String getIs_remind() {
            return is_remind;
        }

        public void setIs_remind(String is_remind) {
            this.is_remind = is_remind;
        }

        public int getDisplay_price() {
            return display_price;
        }

        public void setDisplay_price(int display_price) {
            this.display_price = display_price;
        }

        public String getTo_name() {
            return to_name;
        }

        public void setTo_name(String to_name) {
            this.to_name = to_name;
        }

        public String getTo_tel() {
            return to_tel;
        }

        public void setTo_tel(String to_tel) {
            this.to_tel = to_tel;
        }

        public String getTo_address() {
            return to_address;
        }

        public void setTo_address(String to_address) {
            this.to_address = to_address;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getPay_channel() {
            return pay_channel;
        }

        public void setPay_channel(String pay_channel) {
            this.pay_channel = pay_channel;
        }

        public String getAmount_eth() {
            return amount_eth;
        }

        public void setAmount_eth(String amount_eth) {
            this.amount_eth = amount_eth;
        }

        public StoreEntity getShop() {
            return shop;
        }

        public void setShop(StoreEntity shop) {
            this.shop = shop;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public List<OrderSkusEntity> getOrderSkus() {
            return orderSkus;
        }

        public void setOrderSkus(List<OrderSkusEntity> orderSkus) {
            this.orderSkus = orderSkus;
        }


    }
}
