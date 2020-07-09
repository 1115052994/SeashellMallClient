package com.liemi.seashellmallclient.data.entity.vip;

import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.utils.OrderStatusUtils;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Strings;

import java.util.List;

import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_SECOND_KILL;

/**
 * Created by Bingo on 2018/12/29.
 */

public class VIPIncomeOrderEntity  extends BaseEntity {


    /**
     * hand_log_id	string	推手记录编号(查详情用)
     * spu_name	string	商品名称
     * sku_price	float	商品单价
     * status	int	订单状态 0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
     * create_time	timestamp	下单时间
     * skus	string	规格值
     * img_url	url	商品图片
     * total_num	int	商品数量小计
     * is_self	int	是否自买
     */

    private int total_num;
    private String total_price;
    private String hand_log_id;
    private int is_self;
    private String activity_type;
    private String img_url;
    private String skus;
    private String create_time;
    private String status;
    private String sku_price;
    private String spu_name;
    private String order_id;

    private String order_no;

    private List<OrderSkusEntity> orderSkus;

    public OrderSkusEntity orderSku(){
        return orderSkus.get(0);
    }

    public String getStatusFormat() {
        return OrderStatusUtils.getInstance().getStatusFormat(Strings.toInt(status), -1, "");
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSku_price() {
        return sku_price;
    }

    public void setSku_price(String sku_price) {
        this.sku_price = sku_price;
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getHand_log_id() {
        return hand_log_id;
    }

    public void setHand_log_id(String hand_log_id) {
        this.hand_log_id = hand_log_id;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
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

    public List<OrderSkusEntity> getOrderSkus() {
        return orderSkus;
    }

    public void setOrderSkus(List<OrderSkusEntity> orderSkus) {
        this.orderSkus = orderSkus;
    }
}
