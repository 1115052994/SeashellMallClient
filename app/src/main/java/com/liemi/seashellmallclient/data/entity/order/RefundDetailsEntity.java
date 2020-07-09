package com.liemi.seashellmallclient.data.entity.order;

import android.text.TextUtils;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.widget.countdown.CountDownItem;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/4
 * 修改备注：
 */
public class RefundDetailsEntity extends CountDownItem implements Serializable {

    public static final int REFUND_STATUS_LOGISTICS_FILLED = 3;

    private String id;
    private String order_id;
    private String order_sku_id;
    private String remark;
    private String price_total;
    private int status;
    private String refund_num;
    private String create_time;
    private String refund_no;
    private int refund_status;
    private int type;
    private String agree_time;
    private String success_time;
    private String no_pass_time;
    private String failed_time;
    private String bec_type;
    private String refuse_remark;
    private String mail_no;
    private String mail_name;
    private String mail_code;
    private String shop_id;
    private int state;
    private OrderBean order;
    private StoreEntity shop;
    private String imgs;
    private List<RefundImgsBean> meRefundImgs;
    private List<OrderSkusEntity> orderSku;
    private List<String> hints;
    private String hintTitle;
    private long second;
    private String platform_refuse_remark;
    private String status_name;

    public List<OrderSkusEntity> getGoods() {
        List<OrderSkusEntity> goods = new ArrayList<>();
        if (!Strings.isEmpty(orderSku)) {
            for (OrderSkusEntity entity : orderSku) {
                entity.setImg_url(imgs);
                goods.add(entity);
            }
        }
        return goods;
    }

    public String getStatusToString() {
        if (type == 1) {   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 0:
                    return ResourceUtil.getString(R.string.sharemall_refund_cancel);
                case 1:
                    return ResourceUtil.getString(R.string.sharemall_waiting_dealers);
                case 2:
                    return ResourceUtil.getString(R.string.sharemall_refund_success);
                case 3:
                    return ResourceUtil.getString(R.string.sharemall_businessmen_refuse_refunds);
                case 4:
                    return ResourceUtil.getString(R.string.sharemall_refund_cancel);
                case 5:
                    return ResourceUtil.getString(R.string.sharemall_refund_failed);
                default:
                    return "";
            }
        } else {
            switch (refund_status) {        //0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 0:
                    return ResourceUtil.getString(R.string.sharemall_refund_cancel);
                case 1:
                    return ResourceUtil.getString(R.string.sharemall_waiting_dealers);
                case 2:
                    return ResourceUtil.getString(R.string.sharemall_enter_logistics_no);
                case 3:
                    return ResourceUtil.getString(R.string.sharemall_waiting_dealers);
                case 4:
                    return ResourceUtil.getString(R.string.sharemall_businessmen_refuse_refunds);
                case 5:
                    return ResourceUtil.getString(R.string.sharemall_refund_success);
                default:
                    return "";
            }
        }
    }

    public String getShowHintTitle() {
        if (Strings.isEmpty(hintTitle)) {
            if (type == 1) {   //1：未发货 2：已发货
                switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                    // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                    case 1:
                        hints = new ArrayList<>();
                        hints.add(ResourceUtil.getString(R.string.sharemall_businessmen_agree_system_refund));
                        hints.add(ResourceUtil.getString(R.string.sharemall_merchant_refuses_tips));
                        hintTitle = ResourceUtil.getString(R.string.sharemall_refund_details_wait_deal_info);
                        break;
                    case 3:
                        hints = null;
                        hintTitle = String.format(ResourceUtil.getString(R.string.sharemall_format_refund_details_refuse), refuse_remark);
                        break;
                    case 5:
                        hints = null;
                        hintTitle = ResourceUtil.getString(R.string.sharemall_refund_failed_tips);
                    default:
                        hints = null;
                        hintTitle = "";
                        break;
                }
            } else {
                switch (refund_status) {        //0.取消退款申请 1.发起退款退货申请
                    // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                    case 1:
                        hints = new ArrayList<>();
                        hints.add(ResourceUtil.getString(R.string.sharemall_businessmen_agree_system_refund));
                        hints.add(ResourceUtil.getString(R.string.sharemall_merchant_refuses_tips));
                        hintTitle = ResourceUtil.getString(R.string.sharemall_refund_details_wait_deal_info);
                        break;
                    case 2:
                        hintTitle = ResourceUtil.getString(R.string.sharemall_refund_details_input_no_info);
                        hints = null;
                        break;
                    case 3:
                        hints = new ArrayList<>();
                        hints.add(ResourceUtil.getString(R.string.sharemall_businessmen_agree_system_refund));
                        hints.add(ResourceUtil.getString(R.string.sharemall_merchant_refuses_tips));
                        hintTitle = ResourceUtil.getString(R.string.sharemall_logistics_filled_tips);
                        break;
                    case 4:
                        hints = null;
                        hintTitle = String.format(ResourceUtil.getString(R.string.sharemall_format_refund_details_refuse), refuse_remark);
                        break;
                    default:
                        hints = null;
                        hintTitle = "";
                        break;
                }
            }
        }
        return hintTitle;
    }


    public int getStatusResource() {
        if (type == 1) {   //1：未发货 2：已发货
            switch (status) {        //退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 1:
                    return R.mipmap.sharemall_ic_refund_details_wait_deal;
                case 2:
                    return R.mipmap.sharemall_ic_refund_details_success;
                case 3:
                    return R.mipmap.sharemall_ic_refund_details_refuse;
                case 5:
                    return R.mipmap.sharemall_ic_refund_details_refuse;
                default:
                    return R.mipmap.sharemall_ic_refund_details_refuse;
            }
        } else {
            switch (refund_status) {//0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货  3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 1:
                    return R.mipmap.sharemall_ic_refund_details_wait_deal;
                case 2:
                    return R.mipmap.sharemall_ic_refund_details_input_no;
                case 3:
                    return R.mipmap.sharemall_ic_refund_details_wait_deal;
                case 4:
                    return R.mipmap.sharemall_ic_refund_details_refuse;
                case 5:
                    return R.mipmap.sharemall_ic_refund_details_success;
                default:
                    return R.mipmap.sharemall_ic_refund_details_refuse;
            }
        }
    }

    public String getRefuse_remark() {
        if (TextUtils.isEmpty(refuse_remark))
            return getPlatform_refuse_remark();
        return refuse_remark;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    @Override
    public long initMillisecond() {
        return second * 1000L;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public String getPlatform_refuse_remark() {
        return platform_refuse_remark;
    }

    public void setPlatform_refuse_remark(String platform_refuse_remark) {
        this.platform_refuse_remark = platform_refuse_remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    public String getHintTitle() {
        return hintTitle;
    }

    public void setHintTitle(String hintTitle) {
        this.hintTitle = hintTitle;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sku_id() {
        return order_sku_id;
    }

    public void setOrder_sku_id(String order_sku_id) {
        this.order_sku_id = order_sku_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }

    public String getRefund_num() {
        return refund_num;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRefund_no() {
        return refund_no;
    }

    public void setRefund_no(String refund_no) {
        this.refund_no = refund_no;
    }

    public String getAgree_time() {
        return agree_time;
    }

    public void setAgree_time(String agree_time) {
        this.agree_time = agree_time;
    }

    public String getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(String success_time) {
        this.success_time = success_time;
    }

    public String getNo_pass_time() {
        return no_pass_time;
    }

    public void setNo_pass_time(String no_pass_time) {
        this.no_pass_time = no_pass_time;
    }

    public String getFailed_time() {
        return failed_time;
    }

    public void setFailed_time(String failed_time) {
        this.failed_time = failed_time;
    }

    public String getBec_type() {
        return bec_type;
    }

    public void setBec_type(String bec_type) {
        this.bec_type = bec_type;
    }

    public int getStatus() {
        return status;
    }

    public int getRefund_status() {
        return refund_status;
    }

    public int getType() {
        return type;
    }

    public void setRefuse_remark(String refuse_remark) {
        this.refuse_remark = refuse_remark;
    }

    public String getMail_no() {
        return mail_no;
    }

    public void setMail_no(String mail_no) {
        this.mail_no = mail_no;
    }

    public String getMail_name() {
        return mail_name;
    }

    public void setMail_name(String mail_name) {
        this.mail_name = mail_name;
    }

    public String getMail_code() {
        return mail_code;
    }

    public void setMail_code(String mail_code) {
        this.mail_code = mail_code;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public List<RefundImgsBean> getMeRefundImgs() {
        return meRefundImgs;
    }

    public void setMeRefundImgs(List<RefundImgsBean> meRefundImgs) {
        this.meRefundImgs = meRefundImgs;
    }

    public List<OrderSkusEntity> getOrderSku() {
        return orderSku;
    }

    public void setOrderSku(List<OrderSkusEntity> orderSku) {
        this.orderSku = orderSku;
    }

    public static class RefundImgsBean implements Serializable {
        private String id;
        private String img_url;
        private String r_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getR_id() {
            return r_id;
        }

        public void setR_id(String r_id) {
            this.r_id = r_id;
        }
    }

    public static class OrderBean implements Serializable {
        /**
         * pay_channel : 0
         * amount_eth : 0
         * type : 0
         */

        private String pay_channel;
        private String amount_eth;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
