package com.liemi.seashellmallclient.ui.mine.verification;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.VerificationApi;
import com.liemi.seashellmallclient.data.entity.verification.VerificationCommentEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.liemi.seashellmallclient.data.event.OrderUpdateEvent;
import com.liemi.seashellmallclient.databinding.ActivityVerificationMineCommentBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemMultiPicBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemVerificationCommentBinding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.ui.BaseBlackTitleActivity;
import com.liemi.seashellmallclient.widget.PhotoAdapter;
import com.liemi.seashellmallclient.widget.RatingBarView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_SUCCESS;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class VerificationMineCommentActivity extends BaseBlackTitleActivity<ActivityVerificationMineCommentBinding> implements FileUploadContract.View {

    private BaseRViewAdapter<VerificationOrderDetailEntity, BaseViewHolder> adapter;

    private RecyclerView recyclerView;

    private VerificationCommentEntity commentEntity;

    private VerificationOrderDetailEntity mOrderEntity;

//    private int editPosition;

    private FileUploadPresenterImpl fileUploadPresenter;
    private int currentPosition;

    private int max = 3;

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_mine_comment;
    }

    @Override
    protected void initUI() {
        super.initUI();
        initTitleStr(getString(R.string.sharemall_order_comment));
        basePresenter = fileUploadPresenter = new FileUploadPresenterImpl(this);
        recyclerView = mBinding.rvGoods;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<VerificationOrderDetailEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_verification_comment;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    private boolean canAddImg = true;
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        //设置添加图片的按钮
                        RecyclerView rvPic = getBinding().rvPic;
                        rvPic.setNestedScrollingEnabled(false);
                        rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                        PhotoAdapter photoAdapter = new PhotoAdapter(getContext());
                        photoAdapter.setOnClickListener((View v) -> {
                            if (v.getId() == R.id.iv_delete) {
                                commentEntity.setImg_url(photoAdapter.getItems());
                                return;
                            }
                        });
                        rvPic.setAdapter(photoAdapter);

                        //根据用户设置的星数设置提示的文字
                        getBinding().rbStarServer.setOnRatingListener(new RatingBarView.OnRatingListener() {
                            @Override
                            public void onRating(Object bindObject, int RatingScore) {
                                commentEntity.setStar(RatingScore);
                                getBinding().rbStarServer.setStar(RatingScore,true);
                                getBinding().setCommentStr(RatingScore > 0 ? getResources().getStringArray(R.array.sharemall_comment_array)[RatingScore - 1] : "");
                            }
                        });


                        if (commentEntity.getImg_url() != null) {
                            photoAdapter.setData(commentEntity.getImg_url());
                        }

                        if (!Strings.isEmpty(commentEntity.getComment())) {
                            getBinding().etContent.setText(commentEntity.getComment());
                        }

                        getBinding().etContent.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                commentEntity.setComment(getBinding().etContent.getText().toString());
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }

                    @Override
                    public SharemallItemVerificationCommentBinding getBinding() {
                        return (SharemallItemVerificationCommentBinding) super.getBinding();
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        commentEntity = new VerificationCommentEntity();
        mOrderEntity = (VerificationOrderDetailEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        if (mOrderEntity != null) {
            commentEntity.setOrder_id(mOrderEntity.getId());
            List<VerificationOrderDetailEntity> list = new ArrayList<>();
            list.add(mOrderEntity);
            adapter.setData(list);
            return;

        }
        ToastUtils.showShort(R.string.sharemall_not_comment_order_goods);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    commentEntity.setImg_url(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    commentEntity.setImg_url(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_comment) {
            if (TextUtils.isEmpty(commentEntity.getComment())) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_comment_conment));
                return;
            }
            if (commentEntity.getStar()==0){
                ToastUtils.showShort("请选择星级");
                return;
            }
            uploadCommentImg(0);

        }
    }

    private void uploadCommentImg(final int position) {
        if (position==1) {
            doSubmitComment();
            return;
        }
        if (commentEntity.getImg_url().isEmpty()) {
            uploadCommentImg(position + 1);
            return;
        }
        currentPosition=position;
        showProgress("");
        fileUploadPresenter.doUploadFiles(commentEntity.getImg_url(),true);
    }


    //提交订单评价
    private void doSubmitComment() {
        showProgress("");
        RetrofitApiFactory.createApi(VerificationApi.class)
                .doComment(commentEntity)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort(getString(R.string.sharemall_operation_success));
                            EventBus.getDefault().post(new OrderUpdateEvent(String.valueOf(mOrderEntity.getId()), ORDER_SUCCESS));
                            //结束订单详情页
                            MApplication.getInstance().appManager.finishActivity(VerificationMineOrderDetailsActivity.class);
                            finish();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {

        commentEntity.setImg_url(imgUrls);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (1 == currentPosition + 1) {
                    doSubmitComment();
                } else {
                    uploadCommentImg(currentPosition + 1);
                }
            }
        });
    }

    @Override
    public void fileUploadFail(String errMsg) {
        ToastUtils.showShort(getString(R.string.sharemall_upload_image_failed));
    }
}
