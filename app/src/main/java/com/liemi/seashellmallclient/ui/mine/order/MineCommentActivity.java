package com.liemi.seashellmallclient.ui.mine.order;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.comment.CommentItemEntity;
import com.liemi.seashellmallclient.data.entity.comment.MuchCommentEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.event.OrderUpdateEvent;
import com.liemi.seashellmallclient.databinding.ActivityMineCommentBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemMineCommentBinding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_REFUND_SUCCESS;
import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_SUCCESS;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class MineCommentActivity extends BaseActivity<ActivityMineCommentBinding> implements FileUploadContract.View {

    private BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> adapter;

    private MuchCommentEntity muchCommentEntity;

    private OrderDetailedEntity mOrderEntity;

    private int editPosition;

    private FileUploadPresenterImpl fileUploadPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_comment;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_order_comment));
        basePresenter = fileUploadPresenter = new FileUploadPresenterImpl(this);
        mBinding.rvGoods.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvGoods.setAdapter(adapter = new BaseRViewAdapter<OrderSkusEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_mine_comment;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<OrderSkusEntity>(binding) {

                    @Override
                    public void bindData(OrderSkusEntity item) {
                        super.bindData(item);
                        //设置添加图片的按钮
                        RecyclerView rvPic = getBinding().rvPic;
                        rvPic.setNestedScrollingEnabled(false);
                        final CommentItemEntity itemEntity = muchCommentEntity.getList().get(position);
                        rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                        PhotoAdapter photoAdapter = new PhotoAdapter(getContext());
                        photoAdapter.setOnClickListener((View v) -> {
                            if (v.getId() == R.id.iv_delete) {
                                muchCommentEntity.getList().get(position).setImgs(photoAdapter.getItems());
                                return;
                            }
                            editPosition = position;
                        });
                        rvPic.setAdapter(photoAdapter);

                        if (itemEntity.getImgs() != null) {
                            photoAdapter.setData(itemEntity.getImgs());
                        }

                        //根据用户设置的星数设置提示的文字
                        getBinding().rbStarServer.setOnRatingListener((Object bindObject, int RatingScore) -> {
                            itemEntity.setLevel(RatingScore);
                            getBinding().setCommentStr(RatingScore > 0 ? getResources().getStringArray(R.array.sharemall_comment_array)[RatingScore - 1] : "");
                        });

                        if (!Strings.isEmpty(itemEntity.getContent())) {
                            getBinding().etContent.setText(itemEntity.getContent());
                        }

                        getBinding().etContent.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                muchCommentEntity.getList().get(position).setContent(getBinding().etContent.getText().toString());
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                    }

                    @Override
                    public SharemallItemMineCommentBinding getBinding() {
                        return (SharemallItemMineCommentBinding) super.getBinding();
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        muchCommentEntity = new MuchCommentEntity();
        mOrderEntity = (OrderDetailedEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        if (mOrderEntity != null) {
            List<OrderSkusEntity> goodsList = mOrderEntity.getGoods();

            muchCommentEntity.setOrder_id(mOrderEntity.getOrder_id());
            for (int i = 0; i < goodsList.size(); i++) {
                OrderSkusEntity bean = goodsList.get(i);
                //移除已完成退款的子订单
                if (bean.getStatus() == ORDER_REFUND_SUCCESS) {
                    goodsList.remove(bean);
                    i--;
                } else {
                    muchCommentEntity.getList().add(new CommentItemEntity(String.valueOf(bean.getId())));
                }
            }
            if (!goodsList.isEmpty()) {
                adapter.setData(goodsList);
                return;
            }
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
                    muchCommentEntity.getList().get(editPosition).setImgs(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    muchCommentEntity.getList().get(editPosition).setImgs(ImageItemUtil.ImageItem2String(images));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_comment) {
            for (CommentItemEntity itemEntity : muchCommentEntity.getList()) {
                if (TextUtils.isEmpty(itemEntity.getContent()) || itemEntity.getLevel() <= 0) {
                    ToastUtils.showShort(getString(R.string.sharemall_please_input_comment_conment));
                    return;
                }
            }
            uploadCommentImg(0);

        }
    }

    private int uploadPosition = 0;

    private void uploadCommentImg(final int position) {
        uploadPosition = position;
        if (muchCommentEntity.getList().size() <= position) {
            doSubmitComment();
            return;
        }
        if (muchCommentEntity.getList().get(position).getImgs().isEmpty()) {
            uploadCommentImg(position + 1);
            return;
        }
        fileUploadPresenter.doUploadFiles(muchCommentEntity.getList().get(position).getImgs(), true);
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {
        muchCommentEntity.getList().get(uploadPosition).setImgs(imgUrls);
        if (muchCommentEntity.getList().size() == uploadPosition + 1) {
            doSubmitComment();
        } else {
            uploadCommentImg(uploadPosition + 1);
        }
    }

    @Override
    public void fileUploadFail(String errMsg) {
        showError(getString(R.string.sharemall_upload_image_failed));
    }

    //提交订单评价
    private void doSubmitComment() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .submitComment(muchCommentEntity)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        showError(getString(R.string.sharemall_comment_success));
                        EventBus.getDefault().post(new OrderUpdateEvent(String.valueOf(mOrderEntity.getId()), ORDER_SUCCESS));
                        //结束订单详情页
                        AppManager.getInstance().finishActivity(MineOrderDetailsActivity.class);
                        finish();
                    }
                });
    }
}
