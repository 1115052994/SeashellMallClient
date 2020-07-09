package com.liemi.seashellmallclient.data.entity.locallife;

import android.text.TextUtils;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.List;

public class ShopDetailGoodsEntity extends BaseEntity {
    //秒杀商品类型
    public static final int GOODS_SECOND_KILL = 1;
    //普通商品类型
    public static final int GOODS_NORMAL = 0;

    //秒杀未开始
    public static final int SECOND_KILL_NOT_START = -1;
    //秒杀已结束
    public static final int SECOND_KILL_END = 1;

    /**
     * 我的收藏中是否被选中
     */
    private boolean checked;
    private String item_id;
    private String shop_id;
    private String title;
    private String remark;
    private String img_url;
    private String sequence;
    private String stock;
    private String deal_num;
    private String rich_text;
    private int status;
    private String is_new;
    private String is_hot;
    private String is_sales;
    private String param;
    private String shop_tel;
    private int is_collection;
    private String old_price;
    private String price;
    private String item_code;
    private String earn_score;
    private String postage;
    private int item_type;
    private CommentEntity meCommet;
    private StoreEntity shop;
    private List<String> itemImgs;
    private List<MeNaturesBean> meNatures;
    private List<MeLabelsBean> meLabels;
    private List<ColorListBean> color_list;
    private String total_level;     //商品好评度
    private String service;
    private SeckillItemBean seckillItem;
    private String share;
    private String max_price;
    private String min_price;
    private String buy_rich_text;
    private Boolean isLocalEqualsServiceTime;
    //商品类型，0普通商品，1秒杀商品，2拼团商品
    private int activity_type;
    //商品是否正在参与秒杀活动 0否1是
    private int is_seckill;
    //商品关联活动商品
    private MeItemActivityBean meItemActivity;
    //秒杀当前的状态，默认0进行中,-1未开始， 1已结束
    private int secondKillStatus;

    private String accid;

    public String getService() {
        if (!TextUtils.isEmpty(service)) {
            return service;
        } else {
            return ResourceUtil.getString(R.string.sharemall_not_service_describe);
        }
    }

    //毫秒
    public Long getServiceTime() {
        Long serviceTime = null;
        if (getSeckillItem() != null &&
                !Strings.isEmpty(getSeckillItem().getNow_time())) {
            serviceTime = DateUtil.strToLong(getSeckillItem().getNow_time());
        }
        return serviceTime;
    }

    //是否禁用秒杀
    public boolean isForbidden() {
        boolean isForbidden = false;
        if (isSecKill()) {
            long currentTime;
            if (isLocalEqualsServiceTime()) {
                currentTime = System.currentTimeMillis();
            } else {
                currentTime = getServiceTime();
            }
            if ((getSecKillStartTime() != null && currentTime < getSecKillStartTime()) ||
                    (getSecKillEndTime() != null && currentTime > getSecKillEndTime())) {
                isForbidden = true;
            }
        }
        return isForbidden;
    }

    //毫秒
    public Long getSecKillStartTime() {
        Long serviceTime = null;
        if (getSeckillItem() != null &&
                !Strings.isEmpty(getSeckillItem().getStart_time())) {
            serviceTime = DateUtil.strToLong(getSeckillItem().getStart_time());
        }
        return serviceTime;
    }

    //毫秒
    public Long getSecKillEndTime() {
        Long serviceTime = null;
        if (getSeckillItem() != null &&
                !Strings.isEmpty(getSeckillItem().getEnd_time())) {
            serviceTime = DateUtil.strToLong(getSeckillItem().getEnd_time());
        }
        return serviceTime;
    }

    public boolean isLocalEqualsServiceTime() {      //判断手机系统时间是否正确
        if (isLocalEqualsServiceTime != null) {
            return isLocalEqualsServiceTime;
        }

        isLocalEqualsServiceTime = false;

        Long serviceTime = getServiceTime();
        if (serviceTime != null && Math.abs(serviceTime - System.currentTimeMillis()) < 3000) {//误差在三秒以内
            isLocalEqualsServiceTime = true;
        }
        return isLocalEqualsServiceTime;
    }

    public String getShowPrice() {
        switch (item_type) {
            default:
                return FloatUtils.formatMoney(price);
        }
    }

    public String getShowOldPrice() {
        switch (item_type) {
            default:
                if (Strings.isEmpty(old_price)) {
                    return "¥0.00";
                }
                return FloatUtils.formatMoney(old_price);
        }
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public boolean getIs_seckill() {
        return is_seckill == 1;
    }

    public void setIs_seckill(int is_seckill) {
        this.is_seckill = is_seckill;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean isSecKill() {
        return getSeckillItem() != null;
    }

    public String getBuy_rich_text() {
        return buy_rich_text;
    }

    public void setBuy_rich_text(String buy_rich_text) {
        this.buy_rich_text = buy_rich_text;
    }

    public SeckillItemBean getSeckillItem() {
        return seckillItem;
    }

    public void setSeckillItem(SeckillItemBean seckillItem) {
        this.seckillItem = seckillItem;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getShowShare() {
        return "¥" + share;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMax_old_price() {
        return max_old_price;
    }

    public void setMax_old_price(String max_old_price) {
        this.max_old_price = max_old_price;
    }

    public String getMin_old_price() {
        return min_old_price;
    }

    public void setMin_old_price(String min_old_price) {
        this.min_old_price = min_old_price;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }

    private String max_old_price;
    private String min_old_price;

    /**
     * 购物车的字段
     */
    private String cart_id;
    private String ivid;
    private String num;
    private String value_names;//商品规格名称

    private String eth_cny;
    private String eth_price;

    public String getEth_cny() {
        return eth_cny;
    }

    public void setEth_cny(String eth_cny) {
        this.eth_cny = eth_cny;
    }

    public String getEth_price() {
        return eth_price;
    }

    public void setEth_price(String eth_price) {
        this.eth_price = eth_price;
    }

    public String getTotal_level() {
        return total_level;
    }

    public void setTotal_level(String total_level) {
        this.total_level = total_level;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getIvid() {
        return ivid;
    }

    public void setIvid(String ivid) {
        this.ivid = ivid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRealPrice() {
        return price;
    }


    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getIs_sales() {
        return is_sales;
    }

    public void setIs_sales(String is_sales) {
        this.is_sales = is_sales;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getShop_tel() {
        return shop_tel;
    }

    public void setShop_tel(String shop_tel) {
        this.shop_tel = shop_tel;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getEarn_score() {
        return earn_score;
    }

    public void setEarn_score(String earn_score) {
        this.earn_score = earn_score;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public CommentEntity getMeCommet() {
        return meCommet;
    }

    public void setMeCommet(CommentEntity meCommet) {
        this.meCommet = meCommet;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public List<MeNaturesBean> getMeNatures() {
        return meNatures;
    }

    public void setMeNatures(List<MeNaturesBean> meNatures) {
        this.meNatures = meNatures;
    }

    public List<MeLabelsBean> getMeLabels() {
        return meLabels;
    }

    public void setMeLabels(List<MeLabelsBean> meLabels) {
        this.meLabels = meLabels;
    }

    public List<ColorListBean> getColor_list() {
        return color_list;
    }

    public void setColor_list(List<ColorListBean> color_list) {
        this.color_list = color_list;
    }

    public MeItemActivityBean getMeItemActivity() {
        return meItemActivity;
    }

    public void setMeItemActivity(MeItemActivityBean meItemActivity) {
        this.meItemActivity = meItemActivity;
    }

    public static class MeNaturesBean implements Serializable {
        /**
         * name : 材质：
         * remark : 涤纶、胶贴
         * item_id : 1025
         */

        private String name;
        private String remark;
        private String item_id;
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }

    public static class MeLabelsBean implements Serializable {
        /**
         * name : 哈哈标签
         * item_id : 1272
         */

        private String name;
        private String item_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }

    public static class ColorListBean implements Serializable {

        private String name;
        private String pid;
        private String mcid;
        private String img_url;
        private String item_id;
        private int is_select;
        private boolean checked;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getMcid() {
            return mcid;
        }

        public void setMcid(String mcid) {
            this.mcid = mcid;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public int getIs_select() {
            return is_select;
        }

        public void setIs_select(int is_select) {
            this.is_select = is_select;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

    public static class SeckillItemBean implements Serializable {
        /**
         * item_id : 1350
         * seckill_img : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/WZMNTHK012345678_1545378893.jpg
         * seckill_price : 33.00
         * seckill_price_old : 22.00
         * start_time : 2018-12-27 18:30:00
         * end_time : 2018-12-27 22:30:00
         * now_time : 2018-12-29 16:39:27
         */

        private String item_id;
        private String seckill_img;
        private String seckill_price;
        private String seckill_price_old;
        private String start_time;
        private String end_time;
        private String now_time;
        private long buy_num;

        public long getBuy_num() {
            return buy_num;
        }

        public void setBuy_num(long buy_num) {
            this.buy_num = buy_num;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getSeckill_img() {
            return seckill_img;
        }

        public void setSeckill_img(String seckill_img) {
            this.seckill_img = seckill_img;
        }

        public String getSeckill_price() {
            return seckill_price;
        }

        public void setSeckill_price(String seckill_price) {
            this.seckill_price = seckill_price;
        }

        public String getSeckill_price_old() {
            return seckill_price_old;
        }

        public void setSeckill_price_old(String seckill_price_old) {
            this.seckill_price_old = seckill_price_old;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getNow_time() {
            return now_time;
        }

        public void setNow_time(String now_time) {
            this.now_time = now_time;
        }
    }

    public static class MeItemActivityBean implements Serializable {
        /**
         * id : 11
         * item_id : 1508
         * seckill_item_id :
         * group_item_id :
         * old_item_id : 1343
         */

        private String id;
        private String item_id;
        private String seckill_item_id;
        private String group_item_id;
        private String old_item_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getSeckill_item_id() {
            return seckill_item_id;
        }

        public void setSeckill_item_id(String seckill_item_id) {
            this.seckill_item_id = seckill_item_id;
        }

        public String getGroup_item_id() {
            return group_item_id;
        }

        public void setGroup_item_id(String group_item_id) {
            this.group_item_id = group_item_id;
        }

        public String getOld_item_id() {
            return old_item_id;
        }

        public void setOld_item_id(String old_item_id) {
            this.old_item_id = old_item_id;
        }
    }
}
