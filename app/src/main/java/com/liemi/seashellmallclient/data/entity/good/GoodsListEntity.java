package com.liemi.seashellmallclient.data.entity.good;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/15 18:20
 * 修改备注：
 */
public class GoodsListEntity extends BaseEntity implements Serializable {

    /**
     * "item_id": "1206",//商品主键
     * "shop_id": "170",//商铺主键
     * "title": "植绒A005-296（花布）",//商品名称
     * "remark": "植绒（花布），宽幅145CM±3CM",//商品二级名称
     * "img_url": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1522647229947.jpg",//展示图片
     * "sequence": "0",//默认排序值
     * "stock": "1000",//剩余总库存
     * "deal_num": "0",//总卖出量
     * "rich_text": "......",//富文本介绍
     * "status": "5",//状态：1上传中 2上架待审核 3待定价 4待上架 5已上架 6 下架待审核 7已下架'
     * "is_new": "1",//是否新品 0 否 1 是'
     * "is_hot": "0",//是否热卖 0：否 1：是'
     * "is_sales": "1",//是否促销 0：否 1：是'
     * "param": "/item/me-item/info?id=1206",//app使用的富文本链接（相对路劲）
     * "shop_tel": "400-880-8012",
     * "is_collection": "0",//是否收藏 0：否 1：是
     * "old_price": "0.00",//商品原价 （备用字段）
     * "price": "28.00",//现价（米单价）
     * "item_code": "",
     * "item_type": "0",//商品类型 默认0:普通商品1:纯积分商品（暂无） 2:现金+积分商品（暂无）
     * 4：推手礼包商品 5：经理礼包商品 6:代理商礼包
     * "postage": "0.00",//邮费 为0时包邮
     * <p>
     * //商品下的对应图片列表 itemImgs : ["http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1522647229947.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472293140.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472305473.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472318525.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15226472321840.jpg"]
     * meNatures : []
     * meLabels : [{"name":"哈哈标签","item_id":"1272"},{"name":"热门商品122","item_id":"1272"}]
     * meCommet : {"item_id":null,"uid":null,"content":null,"flag":null,"create_time":null,"to_commet_id":null,"commet_id":null,"sum_commet":null,"meCommetImgs":[],"u":null}
     * shop : {"id":"170","logo_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209237777317.jpg","img_url":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15209239077242.jpg","name":"嗨布商城","remark":"嗨布商城","discount":"0.1000", //店铺分销比例 "full_name":"浙江省-嘉兴市-海宁市 许村镇市场路118号1幢1708室","content":null,"shop_tel":"400-880-8012","qrcode":null,"sum_item":"93","rccode":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402113211.png","wxcode":"http://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15215402227337.png"}
     */

    private String item_id;
    private String shop_id;
    private String title;
    private String remark;
    private String img_url;
    private String sequence;
    private String stock;
    private String deal_num;
    private String rich_text;
    private String status;
    private String is_new;
    private String is_hot;
    private String is_sales;
    private String param;
    private String shop_tel;
    private String is_collection;
    private String old_price;
    private String price;
    private String item_code;
    private String share;
    private int growth;
    private int item_type;
    private String postage;
    private int activity_type;
    private StoreEntity shop;
    private List<String> itemImgs;
    private List<GoodsLabelEntity> meLabels;
    private String coupon;
    //是否海外商品：0 不是，1是
    private int is_abroad;
    private int is_hand_shop;
    private int sort;
    private boolean checked;

    public String getTitle() {
        return (is_abroad == 1 ? ResourceUtil.getString(R.string.sharemall_title_cross_border_purchase) : "") + title;
    }

    public String getDeal_num() {
        return FloatUtils.toBigUnit(deal_num);
    }

    public String getShowPrice() {
        return formatMoney(price);
    }

    public String getOld_price() {
        return formatMoney(old_price);
    }

    public boolean isSecKill() {
        return activity_type == 1;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getIs_abroad() {
        return is_abroad;
    }

    public void setIs_abroad(int is_abroad) {
        this.is_abroad = is_abroad;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIs_hand_shop() {
        return is_hand_shop;
    }

    public void setIs_hand_shop(int is_hand_shop) {
        this.is_hand_shop = is_hand_shop;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(String is_collection) {
        this.is_collection = is_collection;
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

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }


    public StoreEntity getShop() {
        return shop;
    }

    public void setShop(StoreEntity shop) {
        this.shop = shop;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public List<GoodsLabelEntity> getMeLabels() {
        return meLabels;
    }

    public void setMeLabels(List<GoodsLabelEntity> meLabels) {
        this.meLabels = meLabels;
    }
}
