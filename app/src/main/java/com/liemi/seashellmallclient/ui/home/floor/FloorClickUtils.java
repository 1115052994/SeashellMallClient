package com.liemi.seashellmallclient.ui.home.floor;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.category.CategoryGoodsActivity;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.home.FloorActivity;
import com.liemi.seashellmallclient.ui.home.PromotionalGoodsActivity;
import com.liemi.seashellmallclient.ui.home.SignInActivity;
import com.liemi.seashellmallclient.ui.mine.coupon.CouponCenterActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.ui.vip.VIPGiftDetailActivity;
import com.liemi.seashellmallclient.ui.vip.VipUpgradeActivity;

import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;

import static com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity.*;
import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/2
 * 修改备注：
 */
public class FloorClickUtils {

    public static final String FLOOR_PARAM = "param";

    public static FloorClickUtils getInstance() {
        return FloorClickUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final FloorClickUtils instance = new FloorClickUtils();
    }

    //统一处理点击事件
    public void floorDataClick(Context context, NewFloorEntity.FloorDataBean entity, String shopId) {
        //商品：1，-- 分类对应列表：2，-- 店铺：3，-- 热门商品列表：4，-- 新品推荐列表：5，-- 推荐店铺列表：6，-- 富文本：7，-- 外链：8，
        // -- 其他同一店铺下楼层的use_position：9，-- 新人必买：10，-- 品牌精选：11，-- VIP专区：12，-- 签到领币：13  --领券中心：16
        switch (entity.getType()) {
            case FLOOR_TYPE_GOOD:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    GoodDetailPageActivity.start(context, entity.getParam(), null);

                } else {
                    Logs.e(context.getString(R.string.sharemall_unallocated_goods));
                }
                break;
            case FLOOR_TYPE_GOODS_CATEGORY:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    JumpUtil.overlay(context, CategoryGoodsActivity.class,
                            new FastBundle()
                                    .put(GoodsParam.MC_ID, entity.getParam())
                                    .put(GoodsParam.STORE_ID, shopId)
                                    .putString(GoodsParam.MC_NAME, entity.getTitle()));
                } else {
                    Logs.e(context.getString(R.string.sharemall_unallocated_goods_list));
                }
                break;
            case FLOOR_TYPE_STORE:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    StoreDetailActivity.start(context, entity.getParam());
                } else {
                    Logs.e(context.getString(R.string.sharemall_unallocated_store));
                }
                break;
            case FLOOR_TYPE_GOODS_NEWS:
            case FLOOR_TYPE_GOODS_HOT:
                Bundle goodsBundle = new Bundle();
                goodsBundle.putString(entity.getType() == FLOOR_TYPE_GOODS_HOT ? GoodsParam.MC_HOT_GOODS : GoodsParam.MC_NEW_GOODS, "1");
                goodsBundle.putString(GoodsParam.MC_NAME, entity.getType() == FLOOR_TYPE_GOODS_HOT ?
                        context.getString(R.string.sharemall_hot_commodity) : context.getString(R.string.sharemall_new_arrivals));
                if (!TextUtils.isEmpty(entity.getParam())) {
                    goodsBundle.putString(GoodsParam.STORE_ID, entity.getParam());
                }
                JumpUtil.overlay(context, CategoryGoodsActivity.class, goodsBundle);
                break;
            case FLOOR_TYPE_GOODS_RECOMMEND:
//                EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_store));
                break;
            case FLOOR_TYPE_WEB_CONTENT:
            case FLOOR_TYPE_WEB_URL:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    Bundle bundle = new Bundle();
                    if (entity.getType() == FLOOR_TYPE_WEB_CONTENT) {
                        bundle.putString(WEBVIEW_TITLE, TextUtils.isEmpty(entity.getTitle()) ? context.getString(R.string.sharemall_detail) : entity.getTitle());
                    }
                    bundle.putInt(WEBVIEW_TYPE, entity.getType() == FLOOR_TYPE_WEB_CONTENT ? BaseWebviewActivity.WEBVIEW_TYPE_CONTENT : WEBVIEW_TYPE_URL);
                    bundle.putString(WEBVIEW_CONTENT, entity.getType() == FLOOR_TYPE_WEB_CONTENT ? entity.getParam() : entity.getParam());
                    JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                } else {
                    Logs.e(context.getString(R.string.sharemall_unallocated_page));
                }
                break;
            case FLOOR_TYPE_ACTIVITY:
                //楼层活动
                JumpUtil.overlay(context, FloorActivity.class, FLOOR_PARAM, entity.getParam(), GoodsParam.STORE_ID, shopId);
                break;
            case FLOOR_TYPE_NEW_BUY:
                JumpUtil.overlay(context, PromotionalGoodsActivity.class, new FastBundle().putInt(GoodsParam.PROMOTIONAL_TYPE, GoodsParam.PROMOTIONAL_TYPE_NEW));
                break;
            case FLOOR_TYPE_VIP:
                JumpUtil.overlay(context, PromotionalGoodsActivity.class, new FastBundle().putInt(GoodsParam.PROMOTIONAL_TYPE, GoodsParam.PROMOTIONAL_TYPE_VIP));
                break;
            case FLOOR_TYPE_SIGN:
                JumpUtil.overlay(context, SignInActivity.class);
                break;
            case FLOOR_TYPE_VIP_GIFT_LIST:
                JumpUtil.overlay(context, VipUpgradeActivity.class);
                break;
            case FLOOR_TYPE_VIP_GIFT_DETAIL:
                JumpUtil.overlay(context, VIPGiftDetailActivity.class, GoodsParam.ITEM_ID, entity.getParam());
                break;
            case FLOOR_TYPE_COUPON_CENTER:
                JumpUtil.overlay(context, CouponCenterActivity.class);
                break;
            default:
                break;
        }
    }


}
