package com.liemi.seashellmallclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.HomeDialogEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.ui.category.CategoryGoodsActivity;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.coupon.CouponCenterActivity;
import com.liemi.seashellmallclient.ui.mine.coupon.MineCouponActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import static com.netmi.baselibrary.data.Constant.BASE_HTML;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.*;


public class HomeDialog extends Dialog {

    public HomeDialog(@NonNull Context context) {
        super(context);
    }

    public HomeDialog(@NonNull Context context, int myDialogStyle) {
        super(context);
    }

    public static class Builder {
        private HomeDialogEntity entity;
        private Context context;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public Builder setData(HomeDialogEntity homeDialogEntity) {
            this.entity = homeDialogEntity;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public HomeDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.sharemall_dialog_home, null);
            final HomeDialog homeDialog = new HomeDialog(context, R.style.sharemall_MyDialog);
            ImageView cancelImage = view.findViewById(R.id.iv_cancel);
            ImageView ivContent = view.findViewById(R.id.iv_content);
            if (!TextUtils.isEmpty(entity.getImg_url())) {
                GlideShowImageUtils.displayNetImage(context, entity.getImg_url(), ivContent, R.drawable.baselib_bg_default_pic, 4);
            }
            cancelImage.setOnClickListener((View cancelView) -> homeDialog.cancel());
            homeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            homeDialog.setContentView(view);
            homeDialog.setCancelable(isCancelable);
            homeDialog.setCanceledOnTouchOutside(isCancelOutside);
            ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (entity.getPosition()) {
                        case 1:
                            //商品详情
                            if (!TextUtils.isEmpty(entity.getInfo())) {
                                GoodDetailPageActivity.start(context, entity.getInfo(), null);
                            } else {
                                ToastUtils.showShort("未配置商品");
                            }
                            homeDialog.dismiss();
                            break;
                        case 2:
                            //跳外链
                            if (!TextUtils.isEmpty(entity.getInfo())) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, "详情");
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_URL);
                                bundle.putString(WEBVIEW_CONTENT, entity.getInfo());
                                JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                            } else {
                                ToastUtils.showShort("未配置页面");
                            }
                            homeDialog.dismiss();
                        case 4:
                            //跳转富文本
                            if (!TextUtils.isEmpty(entity.getInfo())) {
                                Bundle bundle = new Bundle();
                                bundle.putString(WEBVIEW_TITLE, "详情");
                                bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_URL);
                                bundle.putString(WEBVIEW_CONTENT, BASE_HTML + entity.getInfo());
                                JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                            } else {
                                ToastUtils.showShort("未配置页面");
                            }
                            homeDialog.dismiss();
                            break;
                        case 3:
                            //跳商品列表
                            if (!TextUtils.isEmpty(entity.getInfo())) {
                                JumpUtil.overlay(context, CategoryGoodsActivity.class,
                                        new FastBundle()
                                                .put(GoodsParam.MC_ID, entity.getInfo())
                                                .putString(GoodsParam.MC_NAME, ""));
                            } else {
                                ToastUtils.showShort("未配置商品列表");
                            }
                            homeDialog.dismiss();
                            break;
                        case 5:
                            //优惠券列表
                            JumpUtil.overlay(context, MineCouponActivity.class);
                            break;
                        case 6:
                            //领券中心
                            JumpUtil.overlay(context, CouponCenterActivity.class);
                            break;
                    }
                }
            });
            return homeDialog;

        }
    }
}
