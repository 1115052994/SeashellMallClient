package com.liemi.seashellmallclient.data.entity.order;

import com.liemi.seashellmallclient.data.entity.InvoiceEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 9:51
 * 修改备注：
 */
public class FillOrderEntity implements Serializable {

    //收货地址主键
    private String address_id;
    //默认普通订单，拼团订单：9， 砍价订单：10
    private String order_type;
    //订单的支付价格
    private String amount;
    //积分
    private double pay_score;
    private String display_price;
    private String pay_amount;
    //支付密码
    private String password;
    //平台优惠券ID
    private String p_cuid;
    private List<SectionsBean> sections = new ArrayList<>();
    //身份证号码，跨境购
    private String card_no;
    //身份证姓名
    private String card_name;

    private InvoiceEntity invoice;

    public String getDisplay_price() {
        return display_price;
    }

    public void setDisplay_price(String display_price) {
        this.display_price = display_price;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getP_cuid() {
        return p_cuid;
    }

    public void setP_cuid(String p_cuid) {
        this.p_cuid = p_cuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getPay_score() {
        return pay_score;
    }

    public void setPay_score(double pay_score) {
        this.pay_score = pay_score;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public List<SectionsBean> getSections() {
        return sections;
    }

    public void setSections(List<SectionsBean> sections) {
        this.sections = sections;
    }

    public InvoiceEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEntity invoice) {
        this.invoice = invoice;
    }

    public static class Good implements Serializable {

        private String cart_id;

        private int num;

        private String ivid;

        private int item_type;

        //拼团团队id，拼团商品下单时使用，不传此值为新建团队，传时为加入团队
        private String group_team_id;

        //砍价成功的团ID
        private String bargain_id;

        private String item_id;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getIvid() {
            return ivid;
        }

        public void setIvid(String ivid) {
            this.ivid = ivid;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public String getGroup_team_id() {
            return group_team_id;
        }

        public void setGroup_team_id(String group_team_id) {
            this.group_team_id = group_team_id;
        }

        public String getBargain_id() {
            return bargain_id;
        }

        public void setBargain_id(String bargain_id) {
            this.bargain_id = bargain_id;
        }

        @Override
        public String toString() {
            return "{" +
                    "cart_id=" +
                    ", num=" + num +
                    ", ivid=" + ivid +
                    ", item_type=" + item_type +
                    '}';
        }
    }

    public static class SectionsBean {

        private String cuid;
        private String remark;
        private InvoiceEntity invoice;
        private List<Good> item_data;
        //发货时是否隐藏商品价格 0否1是，默认否
        private String display_price;

        public String getCuid() {
            return cuid;
        }

        public void setCuid(String cuid) {
            this.cuid = cuid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public InvoiceEntity getInvoice() {
            return invoice;
        }

        public void setInvoice(InvoiceEntity invoice) {
            this.invoice = invoice;
        }

        public List<Good> getItem_data() {
            return item_data;
        }

        public void setItem_data(List<Good> item_data) {
            this.item_data = item_data;
        }

        public String getDisplay_price() {
            return display_price;
        }

        public void setDisplay_price(String display_price) {
            this.display_price = display_price;
        }
    }
}
