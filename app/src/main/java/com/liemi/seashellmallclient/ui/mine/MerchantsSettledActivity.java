package com.liemi.seashellmallclient.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.liemi.seashellmallclient.databinding.ActivityMerchantsSettledBinding;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;

public class MerchantsSettledActivity extends BaseActivity<ActivityMerchantsSettledBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_merchants_settled;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.merchants_settled));
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_bottom1 || view.getId() == R.id.iv_bottom){
            Uri uri = Uri.parse("https://www.pgyer.com/vXVE");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return;
        }
    }

    @Override
    protected void initData() {
        doGetImgUrl();
    }

    private void displayLongPic(String path, SubsamplingScaleImageView longImg, ImageView normal) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .asBitmap()
                .load(path)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource.getHeight() > resource.getWidth() * 3) {
                            longImg.setQuickScaleEnabled(true);
                            longImg.setZoomEnabled(false);
                            longImg.setPanEnabled(true);
                            longImg.setDoubleTapZoomDuration(100);
                            longImg.setMaxScale(10f);
                            longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                            longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                            float scale = getImageScale(getContext(), resource);
                            longImg.setImage(ImageSource.cachedBitmap(resource), new ImageViewState(scale, new PointF(0, 0), 0));
                        } else {
                            normal.setImageBitmap(resource);
                        }
                    }
                });


    }

    private float getImageScale(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return 2.0f;
        }
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();

        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }

        return scale>=2f?2f:0f;
    }

    private void doGetImgUrl() {
        showProgress("");
        RetrofitApiFactory.createApi(VIPApi.class)
                    .getMerchantsSettledImage(null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<VIPShareImgEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        String img_url = data.getData().getImg_url();
                        displayLongPic(img_url, mBinding.ivBottom, mBinding.ivBottom1);
                        /*GlideShowImageUtils.displayNetImage(getContext(), img_url, mBinding.ivBg,
                                R.mipmap.ic_merchants_settled_bg);*/
                    }
                });

    }
}
