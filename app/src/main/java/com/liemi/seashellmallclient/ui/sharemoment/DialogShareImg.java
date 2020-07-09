package com.liemi.seashellmallclient.ui.sharemoment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.*;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallDialogShareImageBinding;
import com.netmi.baselibrary.ui.BaseProgressDialog;
import com.netmi.baselibrary.utils.ScreenUtils;
import com.netmi.baselibrary.utils.ToastUtils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class DialogShareImg extends BaseProgressDialog implements View.OnClickListener, PlatformActionListener {

    private String imageUrl;

    private View.OnClickListener onClickListener;

    public DialogShareImg(@NonNull Context context, String imageUrl) {
        super(context, R.style.sharemall_my_dialog_style);
        this.imageUrl = imageUrl;
    }

    public DialogShareImg setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharemallDialogShareImageBinding shareImageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_dialog_share_image, null, false);
        setContentView(shareImageBinding.getRoot());

        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = ScreenUtils.getScreenWidth();
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            dialogWindow.setAttributes(lp);
            dialogWindow.setWindowAnimations(com.netmi.baselibrary.R.style.dialog_bottom_anim_style);
        }

        setOnDismissListener(dialog -> hideProgress());

        shareImageBinding.setDoClick(this);
        if (!TextUtils.isEmpty(imageUrl)) {
            shareImageBinding.setImgUrl(imageUrl);
        }
    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
        if (i1 == R.id.iv_share_cancel || i1 == R.id.view_bg) {
            dismiss();

        } else if (i1 == R.id.tv_share_friend) {
            if (!TextUtils.isEmpty(imageUrl)) {
                showProgress("");
                Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                if (!platform.isClientValid()) {
                    ToastUtils.showShort(R.string.sharemall_login_no_wechat_client);
                    return;
                }
                platform.setPlatformActionListener(this);
                Platform.ShareParams shareParams = new Platform.ShareParams();
                shareParams.setShareType(Platform.SHARE_IMAGE);
                shareParams.setImageUrl(imageUrl);
                platform.share(shareParams);
            } else {
                ToastUtils.showShort(R.string.sharemall_share_image_not_tips);
            }

        } else if (i1 == R.id.tv_share_wechat_moment) {
            if (!TextUtils.isEmpty(imageUrl)) {
                showProgress("");
                Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
                if (!platform.isClientValid()) {
                    ToastUtils.showShort(R.string.sharemall_login_no_wechat_client);
                    return;
                }
                platform.setPlatformActionListener(this);
                Platform.ShareParams shareParams = new Platform.ShareParams();
                shareParams.setShareType(Platform.SHARE_IMAGE);
                shareParams.setImageUrl(imageUrl);
                platform.share(shareParams);
            } else {
                ToastUtils.showShort(R.string.sharemall_share_image_not_tips);
            }
        } else if (i1 == R.id.tv_share_save_local) {//点击将图片保存在本地
            saveBitmap(false);
        }
    }

    //保存图片到本地
    private void saveBitmap(final boolean toWechat) {
        showProgress("");
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            saveBitmapToFile(resource, toWechat);
                        }
                    });
        } else {
            ToastUtils.showShort(getContext().getString(R.string.sharemall_choose_image_you_want_to_save_first));
            hideProgress();
        }
    }

    public void saveBitmapToFile(Bitmap croppedImage, boolean toWechat) {
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), croppedImage, String.valueOf(new Date().getTime()), "");
        ToastUtils.showShort(getContext().getString(R.string.sharemall_saved_path, path.substring(0, path.lastIndexOf(File.separator))));

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse(path);
        intent.setData(uri);
        getContext().sendBroadcast(intent);
        dismiss();
        if (toWechat) {
            DialogShareSaveImageFinish dialogShareSaveImageFinish = new DialogShareSaveImageFinish(activity);
            if (!dialogShareSaveImageFinish.isShowing()) {
                dialogShareSaveImageFinish.show();
            }
        }
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        hideProgress();
        ToastUtils.showShort(R.string.sharemall_thank_support);
        dismiss();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        hideProgress();
        ToastUtils.showShort(R.string.sharemall_share_error_tips);
        dismiss();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        hideProgress();
        dismiss();
    }
}
