package com.liemi.seashellmallclient.data.entity.good;

import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayErrorGoods implements Serializable {
    private String image;
    private String title;
    private String price;
    private String value_names;
    private String num;
    private OrderPayEntity payEntity;
    private ArrayList<PayErrorGoods> goodsList = new ArrayList<>();

    public String showPrice(){
        return FloatUtils.formatMoney(price);
    }

    public PayErrorGoods() {
    }

    public PayErrorGoods(OrderPayEntity payEntity) {
        this.payEntity = payEntity;
    }

    public PayErrorGoods getGoodsList(List<OrderSkusEntity> goods) {
        goodsList.clear();
        if (!Strings.isEmpty(goods)) {
            for (OrderSkusEntity entity : goods) {
                PayErrorGoods good = new PayErrorGoods();
                good.setTitle(entity.getSpu_name());
                good.setNum(entity.getNum());
                good.setImage(entity.getImg_url());
                good.setPrice(entity.getSku_price());
                good.setValue_names(entity.getValue_names());
                goodsList.add(good);
            }
        }
        return this;
    }

    public PayErrorGoods getGoodsListByShopCart(ArrayList<ShopCartEntity> shopCartEntities) {
        goodsList.clear();
        if (!Strings.isEmpty(shopCartEntities)) {
            for (ShopCartEntity entity : shopCartEntities) {
                for (GoodsDetailedEntity goodsDetailedEntity : entity.getList()) {
                    PayErrorGoods goods = new PayErrorGoods();
                    goods.setTitle(goodsDetailedEntity.getTitle());
                    goods.setNum(goodsDetailedEntity.getNum());
                    goods.setImage(goodsDetailedEntity.getImg_url());
                    goods.setPrice(goodsDetailedEntity.getPrice());
                    goods.setValue_names(goodsDetailedEntity.getValue_names());
                    goodsList.add(goods);
                }
            }
        }
        return this;
    }

    public ArrayList<PayErrorGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<PayErrorGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public OrderPayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(OrderPayEntity payEntity) {
        this.payEntity = payEntity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}