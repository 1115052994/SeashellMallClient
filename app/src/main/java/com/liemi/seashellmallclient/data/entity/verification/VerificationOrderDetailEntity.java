package com.liemi.seashellmallclient.data.entity.verification;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;

public class VerificationOrderDetailEntity extends BaseEntity {
    /*
    *         "order_no": "366615441721792385",//订单编号
        "id": "10738",//订单ID
        "create_time": "2019-10-09 15:58:52",//创建时间
        "pay_time": "2019-10-09 16:04:51",//支付时间
        "amount": "11.00",//实付
        "status": "1",//状态 0待付款；1待核销；3待评价；9完成；7已退款
        "sku_info": "{\"item_id\":\"11\",\"item_type\":0,\"ivid\":11,\"num\":1,\"price\":\"11.00\"}",
        "code": "1967156865",//核销码
        "num": 1,//数量
         "item_id": "11",//商品ID
        "title": "测试商品1号",//商品名称
        "price": "11.00",//单价
        "img_url": "https://panjinren.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMTHK0124789_1569206663.jpeg",//图片
        "start_date": "2019-10-09",//开始日期
        "end_date": "2019-10-31",//截止日期
        "purchase_note": "111111111",//购买须知
        *   "total_price": "11.00",//总价
         "used_time": "",//核销时间
         "earn_score": "0.0000",//获得积分
         "order_main_id": "2997",//主订单ID
        "user": {//用户信息
            "nickname": "Near",//昵称
            "head_url"://头像 "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK9BHsYJRKhYWGicDqKWukWYics7mQohWXK2ia95RoGKMHrtxB2wNJtiac5qbiaL7kum1OXjkibRfTUx3ibg/132",
            "phone": "18856855449"//手机号

        }
    * */
    private String order_no;
    private String id;
    private String create_time;
    private String pay_time;
    private String amount;
    private String status;
    private String sku_info;
    private String code;
    private int num;
    private String title;
    private String price;
    private String img_url;
    private String start_date;
    private String end_date;
    private String purchase_note;
    private String spu_name;
    private Shop shop;
    private User user;
    private String total_price;
    private String used_time;
    private String earn_score;
    private String strNum;
    private int intStatus;
    private String item_id;
    private String order_main_id;
    private String refund_time;
    private String statusFormat;
    private String pay_hai;
    private String pay_channel;
    private String shop_id;
    private String type;

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

    public int getStatusImage() {
        switch (Strings.toInt(status)) {
            case OrderParam.VERIFICATION_WAIT_PAY:
                return R.mipmap.sharemall_ic_order_wait_pay;
            case OrderParam.VERIFICATION_WAIT_USE:
                return R.mipmap.ic_verification_wait;
            default:
                return R.mipmap.ic_verification_finish;
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //左边按钮显示的文字
    private String leftButtonStr;
    //右边按钮显示的文字
    private String rightButtonStr;

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_main_id() {
        return order_main_id;
    }

    public void setOrder_main_id(String order_main_id) {
        this.order_main_id = order_main_id;
    }

    public String getLeftButtonStr() {
        switch (Strings.toInt(status)) {
            case OrderParam.VERIFICATION_WAIT_COMMENT:
            case OrderParam.VERIFICATION_REFUND_SUCCESS:
            case OrderParam.VERIFICATION_SUCCESS:
                return "删除订单";
          /*  case Constant.ORDER_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_cancel);
            case Constant.ORDER_WAIT_SEND:
                return "";
            case Constant.ORDER_WAIT_RECEIVE:
                return ResourceUtil.getString(R.string.sharemall_order_logistics);*/
          default:
              return "";
        }

    }

    public String getRightButtonStr() {
        switch (Strings.toInt(status)) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中-已退货8-取消交易9-交易完成10-支付失败
            case OrderParam.VERIFICATION_WAIT_PAY:
                return ResourceUtil.getString(R.string.sharemall_order_go_pay);
            case OrderParam.VERIFICATION_WAIT_COMMENT:
                return "立即评价";
            default:
                return "";
        }
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getPay_hai() {
        return pay_hai;
    }

    public void setPay_hai(String pay_hai) {
        this.pay_hai = pay_hai;
    }

    public String getStrNum(){
        return String.valueOf(getNum());
    }

    public void setStrNum(String strNum) {
        this.strNum = strNum;
    }

    public int getIntStatus(){
        return Strings.toInt(getStatus());
    }

    public void setIntStatus(int intStatus) {
        this.intStatus = intStatus;
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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSku_info() {
        return sku_info;
    }

    public void setSku_info(String sku_info) {
        this.sku_info = sku_info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPurchase_note() {
        return purchase_note;
    }

    public void setPurchase_note(String purchase_note) {
        this.purchase_note = purchase_note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUsed_time() {
        return used_time;
    }

    public void setUsed_time(String used_time) {
        this.used_time = used_time;
    }

    public String getEarn_score() {
        return earn_score;
    }

    public void setEarn_score(String earn_score) {
        this.earn_score = earn_score;
    }

    public class Shop implements Serializable {
        private String name;
        private String id;
        private String logo_url;

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

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

    public class User implements Serializable {
        private String nickname;
        private String head_url;
        private String phone;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
