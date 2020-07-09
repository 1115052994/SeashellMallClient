package com.liemi.seashellmallclient.data.entity.shopcar;

import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.InvoiceEntity;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/21 21:23
 * 修改备注：
 */
public class ShopCartEntity extends BaseEntity implements Serializable {
    private StoreEntity shop;
    private List<GoodsDetailedEntity> list;
    private boolean checked;
    //最终邮费
    private String postage;

    //商品数
    private int goodsNum;
    //商品小计 disabledSumPrice + availableSumPrice
    private float sumPrice;
    //不可使用积分和优惠券的商品价格总和
    private float disabledSumPrice;

    //可用积分和优惠券的商品价格总额
    private float availableSumPrice;
    //下单时的买家留言
    private String remark = "";
    //店铺优惠券
    private String cuid;
    //店铺发票
    private InvoiceEntity invoice;
    //发货时是否隐藏商品价格 0否1是，默认否
    private int display_price;
    //选择的店铺优惠券
    private MineCouponEntity choiceCoupon;
    //可用的优惠券
    private ArrayList<MineCouponEntity> couponList;

    public ShopCartEntity() {
        list = new ArrayList<>();
    }

    public ShopCartEntity(ShopCartAdapterEntity entity) {
        shop = entity.getShop();
        list = entity.getList();
        checked = entity.isChecked();
    }

    public String getShowPostage() {
        if (TextUtils.isEmpty(postage)) {
            return ResourceUtil.getString(R.string.sharemall_distribution_not_supported);
        } else if (Strings.toDouble(postage) > 0) {
            return FloatUtils.formatMoney(postage);
        } else {
            return ResourceUtil.getString(R.string.sharemall_free_shipping);
        }
    }

    //店铺商品小计
    public float subTotal() {
        float subTotal = getSumPrice() - (choiceCoupon != null ? Strings.toFloat(choiceCoupon.getDiscount_num()) : 0) + Strings.toFloat(postage);
        return subTotal > 0f ? subTotal : 0f;
    }

    public float getDisabledSumPrice() {
        return disabledSumPrice;
    }

    public void setDisabledSumPrice(float disabledSumPrice) {
        this.disabledSumPrice = disabledSumPrice;
    }

    public float getAvailableSumPrice() {
        return availableSumPrice;
    }

    public void setAvailableSumPrice(float availableSumPrice) {
        this.availableSumPrice = availableSumPrice;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getPostage() {
        return postage;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public List<GoodsDetailedEntity> getList() {
        return list;
    }

    public void setList(List<GoodsDetailedEntity> list) {
        this.list = list;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public InvoiceEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEntity invoice) {
        this.invoice = invoice;
    }

    public MineCouponEntity getChoiceCoupon() {
        return choiceCoupon;
    }

    public void setChoiceCoupon(MineCouponEntity choiceCoupon) {
        this.choiceCoupon = choiceCoupon;
    }

    public ArrayList<MineCouponEntity> getCouponList() {
        return couponList;
    }

    public void setCouponList(ArrayList<MineCouponEntity> couponList) {
        this.couponList = couponList;
    }

    public int getDisplay_price() {
        return display_price;
    }

    public void setDisplay_price(int display_price) {
        this.display_price = display_price;
    }
}
