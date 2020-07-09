package com.liemi.seashellmallclient.data.entity.good;

import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/16 16:57
 * 修改备注：
 */
public class GoodsDetailedEntity extends BaseEntity implements Serializable {


    //秒杀商品类型
    public static final int GOODS_SECOND_KILL = 1;
    //普通商品类型
    public static final int GOODS_NORMAL = 0;
    //拼团商品类型
    public static final int GOODS_GROUPON = 2;

    //VIP礼包商品类型
    public static final int GOODS_VIP_GIFT = 4;

    //商品活动未开始
    public static final int GOODS_ACTIVITY_NOT_START = -1;
    //商品活动已结束
    public static final int GOODS_ACTIVITY_END = 1;

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
    //1为只能vip购买
    private int is_vip;
    private String shop_tel;
    private int is_collection;
    private String old_price;
    private String price;
    private String item_code;
    private String earn_score;
    private String postage;
    //0普通商品， 4礼包商品
    private int item_type;
    private CommentEntity meCommet;
    private StoreEntity shop;
    private List<String> itemImgs;
    private List<MeNaturesBean> meNatures;
    private List<GoodsLabelEntity> meLabels;
    private List<ColorListBean> color_list;
    private String total_level;     //商品好评度
    private String service;
    private SeckillItemBean seckillItem;
    private GroupItemBean groupItem;
    private String share;
    private String buy_rich_text;
    //商品类型，0普通商品，1秒杀商品，2拼团商品
    private int activity_type;
    //商品是否正在参与秒杀活动 0否1是
    private int is_seckill;
    //商品关联活动商品
    private MeItemActivityBean meItemActivity;
    //活动当前的状态，默认0进行中,-1未开始， 1已结束
    private int activityStatus;
    //商品短视频
    private String short_video_url;
    //当前参团人数
    private String item_buy_item;
    //是否海外商品：0 不是，1是
    private int is_abroad;
    //是否可以购买商品，null为可以购买，否则显示不可购买的文案
    private String can_buy;
    //海贝
    private String currency;

    /**
     * 购物车的字段
     */
    private String cart_id;
    private String ivid;
    private String num;
    private String value_names;//商品规格名称

    public String getTitle() {
        return (is_abroad == 1 ? ResourceUtil.getString(R.string.sharemall_title_cross_border_purchase) : "") + title;
    }

    public String getGroupLabel() {
        return isGroupItem() ? ResourceUtil.getString(R.string.sharemall_title_group_buying) : "";
    }

    public String getDefaultService() {
        if (!TextUtils.isEmpty(service)) {
            return service;
        } else {
            return ResourceUtil.getString(R.string.sharemall_not_service_describe);
        }
    }

    //无法购买的商品
    public boolean unableBuy() {
        return isForbidden() || Strings.toInt(getStock()) <= 0;
    }

    //编辑模式下，任何商品都可被选
    public boolean unableBuy(boolean edit) {
        return !edit && unableBuy();
    }

    //是否禁用秒杀
    public boolean isForbidden() {
        boolean isForbidden = false;
        if (isSecKill()) {
            long currentTime = getServiceTime();
            long startTime = getSecKillStartTime();
            long endTime = getSecKillEndTime();
            if ((startTime > 0 && currentTime < startTime) ||
                    (endTime > 0 && currentTime > endTime)) {
                isForbidden = true;
            }
        }
        return isForbidden;
    }

    private long getLongTime(String type) {
        if (getSeckillItem() == null) return 0;
        long longTime = 0;
        String date = TextUtils.equals(type, "start") ? getSeckillItem().getStart_time() :
                TextUtils.equals(type, "now") ? getSeckillItem().getNow_time() : getSeckillItem().getEnd_time();
        if (!Strings.isEmpty(date)) {
            longTime = DateUtil.strToLong(date);
        }
        return longTime;
    }

    //服务器当前时间
    public long getServiceTime() {
        return getLongTime("now");
    }

    //秒杀开始时间
    public long getSecKillStartTime() {
        return getLongTime("start");
    }

    //秒杀结束时间
    public long getSecKillEndTime() {
        return getLongTime("end");
    }

    public String getActivityStatus() {
        switch (activityStatus) {
            case GOODS_ACTIVITY_NOT_START:
                return ResourceUtil.getString(R.string.sharemall_activity_not_start);
            case GOODS_ACTIVITY_END:
                return ResourceUtil.getString(R.string.sharemall_activity_over);
            default:
                return null;
        }
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getDeal_num() {
        return FloatUtils.toBigUnit(deal_num);
    }

    public String getShowPrice() {
        return formatMoney(price);
    }

    public String getShowOldPrice() {
        return formatMoney(old_price);
    }

    public boolean isSecKill() {
        return getSeckillItem() != null || activity_type == GOODS_SECOND_KILL;
    }

    public boolean isGroupItem() {
        return activity_type == GOODS_GROUPON && getGroupItem() != null;
    }

    public String getShowShare() {
        return formatMoney(isGroupItem() ? getGroupItem().getGroup_share() : share);
    }

    public int getIs_abroad() {
        return is_abroad;
    }

    public void setIs_abroad(int is_abroad) {
        this.is_abroad = is_abroad;
    }

    public boolean getIs_seckill() {
        return is_seckill == 1;
    }

    public SeckillItemBean getSeckillItem() {
        return seckillItem;
    }

    public void setIs_seckill(int is_seckill) {
        this.is_seckill = is_seckill;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public String getShort_video_url() {
        return short_video_url;
    }

    public void setShort_video_url(String short_video_url) {
        this.short_video_url = short_video_url;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItem_buy_item() {
        return item_buy_item;
    }

    public void setItem_buy_item(String item_buy_item) {
        this.item_buy_item = item_buy_item;
    }

    public String getBuy_rich_text() {
        return buy_rich_text;
    }

    public void setBuy_rich_text(String buy_rich_text) {
        this.buy_rich_text = buy_rich_text;
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

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
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

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
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

    public List<GoodsLabelEntity> getMeLabels() {
        return meLabels;
    }

    public void setMeLabels(List<GoodsLabelEntity> meLabels) {
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

    public String getCan_buy() {
        return can_buy;
    }

    public void setCan_buy(String can_buy) {
        this.can_buy = can_buy;
    }

    public GroupItemBean getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(GroupItemBean groupItem) {
        this.groupItem = groupItem;
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
        //用户剩余可购买数量
        private int num;

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

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
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

    public static class GroupItemBean extends BaseEntity implements Serializable {
        /**
         * item_id : 1572
         * group_img : https://social-shop.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZNTH0134567_1554796867.jpg
         * group_type : 0
         * fail_group_type : 0
         * number : 3
         * group_time : 1440
         * start_time : 2019-04-11 10:00:00
         * end_time : 2019-04-12 23:00:00
         * group_scene_id : 4
         * now_time : 2019-04-11 10:52:27
         * is_join_group : 2
         * group_share : 17.90
         */

        private String item_id;
        private String team_id;
        private String group_img;
        private String group_type;
        private String fail_group_type;
        private String number;
        private String group_time;
        private String start_time;
        private String end_time;
        private String group_scene_id;
        private String now_time;
        //1不在拼团， 2在拼团中
        private int is_join_group;
        private String group_share;
        private String push_status;
        //用户可以购买的数量
        private int num;

        public boolean isStarted() {
            return DateUtil.strToLong(now_time) > DateUtil.strToLong(start_time);
        }

        public boolean joinedGroup() {
            return is_join_group == 2;
        }

        public String buttonType() {
            //提醒我，已预约
            if (TextUtils.isEmpty(push_status)) {
                return ResourceUtil.getString(R.string.sharemall_groupon_remind_me);
            } else {
                return ResourceUtil.getString(R.string.sharemall_booked);
            }
        }

        public String getTeam_id() {
            return team_id;
        }

        public void setTeam_id(String team_id) {
            this.team_id = team_id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPush_status() {
            return push_status;
        }

        public void setPush_status(String push_status) {
            this.push_status = push_status;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getGroup_img() {
            return group_img;
        }

        public void setGroup_img(String group_img) {
            this.group_img = group_img;
        }

        public String getGroup_type() {
            return group_type;
        }

        public void setGroup_type(String group_type) {
            this.group_type = group_type;
        }

        public String getFail_group_type() {
            return fail_group_type;
        }

        public void setFail_group_type(String fail_group_type) {
            this.fail_group_type = fail_group_type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getGroup_time() {
            return group_time;
        }

        public void setGroup_time(String group_time) {
            this.group_time = group_time;
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

        public String getGroup_scene_id() {
            return group_scene_id;
        }

        public void setGroup_scene_id(String group_scene_id) {
            this.group_scene_id = group_scene_id;
        }

        public String getNow_time() {
            return now_time;
        }

        public void setNow_time(String now_time) {
            this.now_time = now_time;
        }

        public int getIs_join_group() {
            return is_join_group;
        }

        public void setIs_join_group(int is_join_group) {
            this.is_join_group = is_join_group;
        }

        public String getGroup_share() {
            return group_share;
        }

        public void setGroup_share(String group_share) {
            this.group_share = group_share;
        }
    }
}
