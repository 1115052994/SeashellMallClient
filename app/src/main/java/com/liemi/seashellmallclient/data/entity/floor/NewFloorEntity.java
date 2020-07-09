package com.liemi.seashellmallclient.data.entity.floor;

import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Strings;

import java.util.List;

/**
 * Created by Bingo on 2018/11/19.
 */

public class NewFloorEntity extends BaseEntity {


    //搜索栏
    @Deprecated
    public static final int FLOOR_SEARCH_BAR = 1;
    //banner轮播图
    public static final int FLOOR_BANNER = 2;
    //公告
    public static final int FLOOR_NOTICE = 3;
    //分类导航
    public static final int FLOOR_NAVIGATION = 4;
    //图层
    public static final int FLOOR_IMAGE = 5;
    //热门商品
    public static final int FLOOR_GOODS_HOT = 6;
    //新品推荐
    public static final int FLOOR_GOODS_NEWS = 7;
    //推荐店铺
    public static final int FLOOR_STORE = 8;
    //秒杀
    public static final int FLOOR_SECKILL = 9;
    //自定义商品列表
    public static final int FLOOR_CUSTOM_GOODS = 10;
    //优惠券板块
    public static final int FLOOR_COUPON = 11;
    //拼团
    public static final int FLOOR_GROUP = 12;
    //秒杀板块横向样式
    public static final int FLOOR_SECKILL_HORIZONTAL = 13;
    //拼团板块横向样式
    public static final int FLOOR_GROUP_HORIZONTAL = 14;
    //砍价专区
    public static final int FLOOR_BARGAIN = 15;
    //砍价横向
    public static final int FLOOR_BARGAIN_HORIZONTAL = 16;

    //用于复杂布局的JSON重构
    public static final int TANGRAM_NUMBER = 1000;
    //与吸顶分离的内容
    public static final int FLOOR_TANGRAM_VIEWPAGE = 102;
    //与吸顶分离的横向内容
    public static final int FLOOR_TANGRAM_VIEWPAGE_HORIZONTA = 112;


    //点击效果：商品
    public static final int FLOOR_TYPE_GOOD = 1;
    //点击效果：分类对应列表
    public static final int FLOOR_TYPE_GOODS_CATEGORY = 2;
    //点击效果：店铺
    public static final int FLOOR_TYPE_STORE = 3;
    //点击效果：热门商品列表
    public static final int FLOOR_TYPE_GOODS_HOT = 4;
    //点击效果：新品推荐列表
    public static final int FLOOR_TYPE_GOODS_NEWS = 5;
    //点击效果：推荐店铺列表
    public static final int FLOOR_TYPE_GOODS_RECOMMEND = 6;
    //点击效果：富文本
    public static final int FLOOR_TYPE_WEB_CONTENT = 7;
    //点击效果：外链
    public static final int FLOOR_TYPE_WEB_URL = 8;
    //点击效果：活动，对应另外的楼层
    public static final int FLOOR_TYPE_ACTIVITY = 9;
    //点击效果：新人必买
    public static final int FLOOR_TYPE_NEW_BUY = 10;
    //点击效果：品牌精选
    public static final int FLOOR_TYPE_BRAND = 11;
    //点击效果：VIP专区
    public static final int FLOOR_TYPE_VIP = 12;
    //点击效果：签到领币
    public static final int FLOOR_TYPE_SIGN = 13;
    //点击效果：礼包列表
    public static final int FLOOR_TYPE_VIP_GIFT_LIST = 14;
    //点击效果：礼包详情
    public static final int FLOOR_TYPE_VIP_GIFT_DETAIL = 15;
    public static final int FLOOR_TYPE_COUPON_CENTER = 16;


    private boolean active;
    private int type;
    private int top;
    private int bottom;
    private boolean is_opacity;
    private int nums;
    private String title;
    private int posId;
    private List<String> goods_list;
    private List<GoodsListEntity> goods_data;
    private List<StoreEntity> shops_list;
    private List<FloorDataBean> floor_data;
    private List<CouponEntity> couponList;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CouponEntity> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponEntity> couponList) {
        this.couponList = couponList;
    }

    public int getRealType() {
        return type - TANGRAM_NUMBER;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public boolean isIs_opacity() {
        return is_opacity;
    }

    public void setIs_opacity(boolean is_opacity) {
        this.is_opacity = is_opacity;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    public List<String> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<String> goods_list) {
        this.goods_list = goods_list;
    }

    public List<StoreEntity> getShops_list() {
        return shops_list;
    }

    public void setShops_list(List<StoreEntity> shops_list) {
        this.shops_list = shops_list;
    }

    public List<FloorDataBean> getFloor_data() {
        return floor_data;
    }

    public void setFloor_data(List<FloorDataBean> floor_data) {
        this.floor_data = floor_data;
    }

    public List<GoodsListEntity> getGoods_data() {
        return goods_data;
    }

    public void setGoods_data(List<GoodsListEntity> goods_data) {
        this.goods_data = goods_data;
    }

    public static class FloorDataBean {
        /**
         * title : 设置标题
         * img_url :
         * type :
         * param :
         */

        private String title;
        private String img_url;
        private String type;
        //店铺：3，-- 热门商品列表：4，-- 新品推荐列表：5，-- 推荐店铺列表：6，-- 富文本：7，-- 外链：8，
        private String param;
        private String time;
        private float img_width;
        private float img_height;

        public FloorDataBean() {

        }

        public FloorDataBean(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getType() {
            return Strings.toInt(type);
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getImg_width() {
            return img_width;
        }

        public void setImg_width(float img_width) {
            this.img_width = img_width;
        }

        public float getImg_height() {
            return img_height;
        }

        public void setImg_height(float img_height) {
            this.img_height = img_height;
        }
    }
}
