package com.liemi.seashellmallclient.data.entity.order;

import android.text.TextUtils;

import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.helper.PayChannelHelper;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Strings;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/8
 * 修改备注：
 */
public class OrderRebateEntity extends BaseEntity {

    /**
     * nickname : Panda.Chen
     * head_url : http://thirdwx.qlogo.cn/mmopen/vi_32/ic7iavxDDNWTjZZCtvlqLm6CMiaP4ia4rJ1ls9Zc4gNL4rehYyqk2njOdknNnOLvP7PV3DWCYnSIicLichYUPyDa9jmw/132
     * order_no : 2019040317340358781977
     * create_time : 2019-04-03 17:34:03
     * pay_time : 2019-04-03 17:34:12
     * pay_channel : 5
     * list : [{"spu_name":"巴比布朗10大潮流色口红","sku_price":"199.00","num":"1","skus":"#103","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABWXZMNTHK014789_1552023089.jpg","activity_type":"1"}]
     * total_item_num : 1
     * total_item_price : 199.00
     * expected_price : 39.80
     * refund_price :
     * total_price : 39.8
     */

    private String nickname;
    private String head_url;
    private String order_no;
    private String create_time;
    private String pay_time;
    private String settlement_time;
    private String pay_channel;
    private int total_item_num;
    private String total_item_price;
    private String expected_price;
    private String refund_price;
    private String total_price;
    //支付方式
    private String pay_name;
    private StoreEntity shop;
    private List<OrderSkusEntity> list;

    public String getPay_name() {
        if (TextUtils.isEmpty(pay_name)) {
            pay_name = PayChannelHelper.getTextByType(Strings.toInt(pay_channel));
        }
        return pay_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(String settlement_time) {
        this.settlement_time = settlement_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public int getTotal_item_num() {
        return total_item_num;
    }

    public void setTotal_item_num(int total_item_num) {
        this.total_item_num = total_item_num;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public String getExpected_price() {
        return expected_price;
    }

    public void setExpected_price(String expected_price) {
        this.expected_price = expected_price;
    }

    public String getRefund_price() {
        return refund_price;
    }

    public void setRefund_price(String refund_price) {
        this.refund_price = refund_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public List<OrderSkusEntity> getList() {
        return list;
    }

    public void setList(List<OrderSkusEntity> list) {
        this.list = list;
    }


}
