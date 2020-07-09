package com.liemi.seashellmallclient.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallItemMultiPicBinding;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

/**
 * Created by Bingo on 2018/6/18.
 */
public class PhotoAdapter extends BaseRViewAdapter<String, BaseViewHolder> {

    private int max = 3;

    private boolean canAddImg = true; //是否支持添加图片

    private View.OnClickListener onClickListener;

    public PhotoAdapter(Context context) {
        super(context);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.sharemall_item_multi_pic;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<String>(binding) {
            @Override
            public void bindData(String item) {
                getBinding().ivDelete.setVisibility(View.GONE);
                //最后一张图
                if (position == getItemCount() - 1) {
                    //达到最大选择数量后，隐藏添加图片功能
                    if (position >= max) {
                        getBinding().ivPic.setVisibility(View.GONE);
                        canAddImg = false;
                    } else {
                        getBinding().ivPic.setVisibility(View.VISIBLE);
                        getBinding().ivPic.setImageResource(R.mipmap.sharemall_ic_add_image);
                        canAddImg = true;
                    }
                } else {
                    getBinding().ivPic.setVisibility(View.VISIBLE);
                    getBinding().ivDelete.setVisibility(View.VISIBLE);
                    if (getItem(position).startsWith("http")) {
                        GlideShowImageUtils.displayNetImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                    } else {
                        GlideShowImageUtils.displayNativeImage(context, getItem(position), getBinding().ivPic, R.drawable.baselib_bg_default_pic);
                    }
                }
                super.bindData(item);
            }

            @Override
            public SharemallItemMultiPicBinding getBinding() {
                return (SharemallItemMultiPicBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (view.getId() == R.id.iv_delete) {
                    remove(position);
                } else if (position == getItemCount() - 1 && canAddImg) {
                    //图片选择
                    ImagePicker.getInstance().setMultiMode(true);
                    ImagePicker.getInstance().setSelectLimit(max);
                    Intent intent = new Intent(context, ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, ImageItemUtil.String2ImageItem(getItems()));
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE_SELECT);
                } else {
                    //打开预览
                    Intent intentPreview = new Intent(context, ImagePreviewDelActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(getItems()));
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                    intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                    ((Activity) context).startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                }

                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }

            }
        };
    }
}
