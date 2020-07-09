package com.liemi.seashellmallclient.data.entity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopDetailActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.*;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/3/20
 * 修改备注：
 */
public class BannerJumpEntity {

    public void toJump(Context context, BannerEntity entity){
            switch (entity.getShow_type()) {
                case 2:
                    //跳转外链
                case 3:
                    //跳富文本
                    if (!TextUtils.isEmpty(entity.getParam())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WEBVIEW_TITLE, TextUtils.isEmpty(entity.getName()) ? ResourceUtil.getString(R.string.sharemall_detail) : entity.getName());
                        bundle.putInt(WEBVIEW_TYPE, entity.getShow_type());
                        bundle.putString(WEBVIEW_CONTENT, entity.getShow_type() == WEBVIEW_TYPE_URL ? entity.getParam() : entity.getContent());
                        JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                    } else {
                        ToastUtils.showShort(R.string.sharemall_unallocated_page);
                    }
                    break;
                case 5:
                    //跳商品
                    if (!TextUtils.isEmpty(entity.getParam())) {
                        GoodDetailPageActivity.start(context, entity.getParam(), null);
                    } else {
                        ToastUtils.showShort(R.string.sharemall_unallocated_goods);
                    }
                    break;
                case 6:
                    //跳店铺
                    if (!TextUtils.isEmpty(entity.getParam())) {
                        StoreDetailActivity.start(context, entity.getParam());
                    } else {
                        ToastUtils.showShort(R.string.sharemall_unallocated_store);
                    }
                    break;
            }
    }

    public void toJump1(Context context, BannerEntity entity) {
        switch (entity.getShow_type()) {
            /*case 2:
                //跳转外链*/
            case 3:
                //跳富文本
                if (!TextUtils.isEmpty(entity.getParam())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(WEBVIEW_TITLE, TextUtils.isEmpty(entity.getName()) ? ResourceUtil.getString(R.string.sharemall_detail) : entity.getName());
                    bundle.putInt(WEBVIEW_TYPE, entity.getShow_type());
                    bundle.putString(WEBVIEW_CONTENT, entity.getShow_type() == WEBVIEW_TYPE_URL ? entity.getParam() : entity.getContent());
                    JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                } else {
                    ToastUtils.showShort(R.string.sharemall_unallocated_page);
                }
                break;
           /* case 5:
                //跳商品
                if (!TextUtils.isEmpty(entity.getParam())) {
                    GoodDetailPageActivity.start(context, entity.getParam(), null);
                } else {
                    ToastUtils.showShort(R.string.sharemall_unallocated_goods);
                }
                break;*/
            case 6:
                //跳店铺
                if (!TextUtils.isEmpty(entity.getParam())) {
                    LocalLifeShopDetailActivity.start(context, entity.getParam());
                } else {
                    ToastUtils.showShort(R.string.sharemall_unallocated_store);
                }
                break;
        }
    }

}
