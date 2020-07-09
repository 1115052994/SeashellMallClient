package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.utils.DensityUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.RoundImageView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/3
 * 修改备注：
 */
public class BannerViewHolder implements MZViewHolder<String> {
    private RoundImageView mImageView;

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.sharemall_item_banner_base, null);
        mImageView = (RoundImageView) view.findViewById(R.id.iv_banner);
        mImageView.setCornerRadius(DensityUtils.dp2px(16));
        return view;
    }

    @Override
    public void onBind(Context context, int position, String data) {
        GlideShowImageUtils.gifload(context, data, mImageView);
    }
}