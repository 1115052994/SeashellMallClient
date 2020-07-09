package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import static cn.jzvd.Jzvd.CURRENT_STATE_PLAYING;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/3
 * 修改备注：
 */
public class GoodsBannerViewHolder implements MZViewHolder<String> {

    private ImageView ivBanner;

    private JzvdStd jVideo;

    private Context mContext;

    private MZBannerView mzBannerView;

    private View parentView;

    private View.OnClickListener returnViewListener;

    private String defaultImage;

    public GoodsBannerViewHolder(MZBannerView mzBannerView, View.OnClickListener returnViewListener) {
        this.mzBannerView = mzBannerView;
        this.returnViewListener = returnViewListener;
    }

    public GoodsBannerViewHolder setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
        return this;
    }

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.sharemall_item_good_banner, null);
        ivBanner = view.findViewById(R.id.iv_banner);
        jVideo = view.findViewById(R.id.videoplayer);
        mContext = context;
        parentView = view;
        return view;
    }

    @Override
    public void onBind(Context context, int position, String imgUrl) {
        if (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg")
                || imgUrl.endsWith(".bmp")
                || imgUrl.endsWith(".gif")
                || imgUrl.endsWith(".png")) {
            GlideShowImageUtils.gifload(mContext, imgUrl, ivBanner);
        } else {
            jVideo.setVisibility(View.VISIBLE);
            jVideo.setUp(imgUrl, "", Jzvd.SCREEN_WINDOW_NORMAL);
            if (!TextUtils.isEmpty(defaultImage)) {
                GlideShowImageUtils.displayNetImage(mContext, defaultImage, jVideo.thumbImageView, R.drawable.baselib_bg_default_pic);
            } else {
                Glide.with(mContext)
                        .load(imgUrl)
                        .apply(RequestOptions.frameOf(10))
                        .into(jVideo.thumbImageView);
            }

            if (returnViewListener != null) {
                returnViewListener.onClick(parentView);
            }

            mzBannerView.addPageChangeListener(new ViewPager.OnPageChangeListener() {

                //实现切换页面暂停播放的功能
                private int lastIndex;
                private boolean isPlaying;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (jVideo.getVisibility() == View.VISIBLE) {

                        if (lastIndex == 0) {
                            isPlaying = jVideo.currentState == CURRENT_STATE_PLAYING;
                            if (isPlaying) {
                                jVideo.startButton.performClick();
                            }
                        } else if (position == 0) {
                            if (isPlaying) {
                                jVideo.startButton.performClick();
                            }
                        }
                    }
                    lastIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }


}