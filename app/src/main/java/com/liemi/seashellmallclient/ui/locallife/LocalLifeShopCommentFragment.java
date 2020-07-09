package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.databinding.LocallifeShopCommentItemCommentBinding;
import com.liemi.seashellmallclient.ui.video.VideoPlayer2Activity;
import com.liemi.seashellmallclient.ui.video.VideoPlayerActivity;
import com.liemi.seashellmallclient.widget.MyRecyclerView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.liemi.seashellmallclient.ui.locallife.BaseShopDetailedFragment.ITEM_ID;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 15:43
 * 修改备注：
 */
public class LocalLifeShopCommentFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerviewBinding, CommentEntity> {

    private static final String COMMENT_FLAG = "comment_flag";


    private String itemId, flag;

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview;
    }

    public static LocalLifeShopCommentFragment newInstance(String itemId, String flag) {
        LocalLifeShopCommentFragment f = new LocalLifeShopCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, itemId);
        bundle.putString(COMMENT_FLAG, flag);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<CommentEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.locallife_shop_comment_item_comment;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<CommentEntity>(binding) {
                    @Override
                    public void bindData(CommentEntity item) {
                        float star;
                        if (TextUtils.isEmpty(item.getLevel())){
                            star = 0f;
                        }else {
                            star = Float.valueOf(item.getLevel());
                        }
                        getBinding().rbStarServer.setStar(star, false);
                        MyRecyclerView rvPic = getBinding().rvImg;
                        rvPic.setNestedScrollingEnabled(false);
                        if (getItem(position).getMeCommetImgs() != null
                                && !getItem(position).getMeCommetImgs().isEmpty()) {
                            rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                            final BaseRViewAdapter<String, BaseViewHolder> imgAdapter = new BaseRViewAdapter<String, BaseViewHolder>(context) {

                                @Override
                                public int layoutResId(int position) {
                                    return R.layout.sharemall_item_multi_pic_show;
                                }

                                @Override
                                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            //打开预览
                                            Intent intentPreview = new Intent(getContext(), ImagePreviewDelActivity.class);
                                            intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, ImageItemUtil.String2ImageItem(getItems()));
                                            intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                                            intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                                            intentPreview.putExtra(ImagePicker.EXTRA_PREVIEW_HIDE_DEL, true);
                                            startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                                        }
                                    };
                                }
                            };

                            rvPic.setAdapter(imgAdapter);
                            imgAdapter.setData(getItem(position).getMeCommetImgs());
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId()==R.id.iv_imageView){
                            JumpUtil.overlay(getContext(), VideoPlayer2Activity.class, VideoPlayerActivity.VIDEO_URL,getItem(position).getVideo_url());
                        }
                    }

                    @Override
                    public LocallifeShopCommentItemCommentBinding getBinding() {
                        return (LocallifeShopCommentItemCommentBinding) super.getBinding();
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        itemId = getArguments().getString(ITEM_ID);
        flag = getArguments().getString(COMMENT_FLAG);
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {
        doListComment();
    }


    private void doListComment() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getCommentList(PageUtil.toPage(startPage), Constant.PAGE_ROWS,"",itemId,flag)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        if (dataExist(data)){
                            if (!Strings.isEmpty(data.getData().getList())) {
                                showData(data.getData());
                            }
                        }else {
                            showError(data.getErrmsg());
                        }
                    }

                });
    }


}
