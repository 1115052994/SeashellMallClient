package com.liemi.seashellmallclient.data.entity.order;

import com.google.gson.annotations.SerializedName;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.good.AbsGoodsDetails;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.utils.OrderStatusUtils;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.io.Serializable;

import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_NORMAL;
import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_SECOND_KILL;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/4
 * 修改备注：
 */
public class OrderSkusEntity extends AbsGoodsDetails implements Serializable {
    private String id;
    private String order_id;
    private String spu_name;
    private String sku_info;
    private String sku_score;
    private String spu_earn_score;
    private String sku_price;
    private String old_price;
    private String eth_price;
    private String spu_type;
    private int activity_type;
    private String sub_total;
    private int status;
    private String remark;
    private String create_time;
    private String update_time;
    private String del_flag;
    private String uid;
    private String pay_channel;
    private int item_type;
    private int is_abroad;
    private String shopName;

    @Override
    public boolean isSecKill() {
        return activity_type == GOODS_SECOND_KILL;
    }

    @Override
    public boolean isAbroad() {
        return is_abroad == 1;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public String getShowPrice() {
        return FloatUtils.formatMoney(sku_price);
    }

    @Override
    public String getTitle() {
        return getSpu_name();
    }

    public String getSpu_name() {
        return (is_abroad == 1 ? ResourceUtil.getString(R.string.sharemall_title_cross_border_purchase) : "") + spu_name;
    }

    public boolean isCanApplyRefund() {
        //待收货，待发货，4-退货申请5-退货申请驳回6-退货中7-已退货 均会显示这个按钮
        if (status == OrderParam.ORDER_WAIT_RECEIVE || status == OrderParam.ORDER_WAIT_SEND || status == OrderParam.ORDER_REFUND_ASK
                || status == OrderParam.ORDER_REFUND_NOT_ALLOW || status == OrderParam.ORDER_REFUND_NOW || status == OrderParam.ORDER_REFUND_SUCCESS) {
            //拼团成功才能申请售后
            return item_type == GOODS_NORMAL;
        }
        return false;
    }

    public String getStatusFormat() {
        return OrderStatusUtils.getInstance().getStatusFormat(status, -1, "");
    }

    @Override
    public long initMillisecond() {
        return 0;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getIs_abroad() {
        return is_abroad;
    }

    public void setIs_abroad(int is_abroad) {
        this.is_abroad = is_abroad;
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


    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public String getSku_info() {
        return sku_info;
    }

    public void setSku_info(String sku_info) {
        this.sku_info = sku_info;
    }

    public String getSku_score() {
        return sku_score;
    }

    public void setSku_score(String sku_score) {
        this.sku_score = sku_score;
    }

    public String getSpu_earn_score() {
        return spu_earn_score;
    }

    public void setSpu_earn_score(String spu_earn_score) {
        this.spu_earn_score = spu_earn_score;
    }

    public String getSku_price() {
        return sku_price;
    }

    public void setSku_price(String sku_price) {
        this.sku_price = sku_price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getEth_price() {
        return eth_price;
    }

    public void setEth_price(String eth_price) {
        this.eth_price = eth_price;
    }

    public String getSpu_type() {
        return spu_type;
    }

    public void setSpu_type(String spu_type) {
        this.spu_type = spu_type;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

}
