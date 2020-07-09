package com.liemi.seashellmallclient.ui.mine.refund;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundDetailsEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundPriceEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundReasonEntity;
import com.liemi.seashellmallclient.data.param.RefundParam;
import com.liemi.seashellmallclient.databinding.ActivityApplyRefundBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemSelect2Binding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.widget.MyBaseDialog;
import com.liemi.seashellmallclient.widget.MyRecyclerView;
import com.liemi.seashellmallclient.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_WAIT_RECEIVE;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;
import static com.liemi.seashellmallclient.ui.mine.refund.RefundApplySuccessActivity.REFUND_TIP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class ApplyForRefundActivity extends BaseActivity<ActivityApplyRefundBinding> implements FileUploadContract.View {

    private PhotoAdapter photoAdapter;

    private FileUploadPresenterImpl filePresenter;
    //退款原因列表
    private List<String> refundReasons = new ArrayList<>();

    //货物状态列表
    private List<String> goodsStatusList = new ArrayList<>();

    private OrderSkusEntity skusEntity;

    private RefundDetailsEntity refundEntity;

    private double maxPrice, postage;

    private int applyType;

    public static void start(Context context, OrderSkusEntity skusEntity, int type, RefundDetailsEntity refundEntity) {
        if (skusEntity != null || refundEntity != null) {
            Bundle bundle = new Bundle();
            if (skusEntity != null) {
                bundle.putSerializable(RefundParam.SKU_ENTITY, skusEntity);
            } else {
                bundle.putSerializable(RefundParam.REFUND_ENTITY, refundEntity);
            }
            bundle.putInt(RefundParam.REFUND_APPLY_FOR_TYPE, type);
            JumpUtil.overlay(context, ApplyForRefundActivity.class, bundle);
        } else {
            ToastUtils.showShort(R.string.sharemall_not_order_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_refund;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_apply_refund));
        applyType = getIntent().getIntExtra(RefundParam.REFUND_APPLY_FOR_TYPE, RefundParam.REFUND_TYPE_ONLY_CASH);

        skusEntity = (OrderSkusEntity) getIntent().getSerializableExtra(RefundParam.SKU_ENTITY);

        refundEntity = (RefundDetailsEntity) getIntent().getSerializableExtra(RefundParam.REFUND_ENTITY);

        if (skusEntity == null && isUpdate()) {
            skusEntity = refundEntity.getOrderSku().get(0);
            skusEntity.setShopName(refundEntity.getShop().getName());
        }

        if (skusEntity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_info));
            finish();
            return;
        }

        mBinding.setItem(skusEntity);
        mBinding.setData(refundEntity);
        mBinding.executePendingBindings();
        if (isUpdate()) {
            resetRefundPrice(Strings.toDouble(refundEntity.getPrice_total()));
        }

        initPhotoAdapter();

        //初始化货物状态
        initGoodsStatus();

        basePresenter = filePresenter = new FileUploadPresenterImpl(this);

        doGetRefundPrice(skusEntity.getId());
    }

    @Override
    protected void initData() {
    }

    private void initPhotoAdapter() {
        mBinding.rvImg.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.rvImg.setNestedScrollingEnabled(false);
        photoAdapter = new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvImg.setAdapter(photoAdapter);

        if (isUpdate() && refundEntity.getMeRefundImgs() != null) {
            List<String> imgUrls = new ArrayList<>();
            for (RefundDetailsEntity.RefundImgsBean refundImgsBean : refundEntity.getMeRefundImgs()) {
                imgUrls.add(refundImgsBean.getImg_url());
            }
            photoAdapter.setData(imgUrls);
            photoAdapter.notifyDataSetChanged();
        }

        mBinding.etCustomPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Strings.toDouble(s.toString()) > maxPrice) {
                    resetRefundPrice(maxPrice);
                }
            }
        });
    }

    private void initGoodsStatus() {
        goodsStatusList.add(getString(R.string.sharemall_goods_not_received));
        goodsStatusList.add(getString(R.string.sharemall_goods_received));
        if (isUpdate() && refundEntity.getState() < goodsStatusList.size()) {
            mBinding.tvGoodStatus.setText(goodsStatusList.get(refundEntity.getState()));
        }
        mBinding.tvGoodStatus.setVisibility(applyType == RefundParam.REFUND_TYPE_GOODS_AND_CASH ? View.VISIBLE : View.GONE);
    }

    private void resetRefundPrice(double maxPrice) {
        mBinding.etCustomPrice.setText(FloatUtils.formatDouble(maxPrice));
        mBinding.etCustomPrice.setSelection(mBinding.etCustomPrice.getText().toString().length());
    }

    private boolean isUpdate() {
        return refundEntity != null;
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        //货物状态
        if (view.getId() == R.id.tv_good_status) {
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (int options1, int option2, int options3, View v) ->
                    mBinding.tvGoodStatus.setText(goodsStatusList.get(options1))).build();
            pvOptions.setPicker(goodsStatusList);
            pvOptions.setSelectOptions(goodsStatusList.indexOf(mBinding.tvGoodStatus.getText().toString()));
            pvOptions.show();
        }
        //退款原因
        else if (view.getId() == R.id.tv_refund_reason) {
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (int options1, int option2, int options3, View v) ->
                    mBinding.tvRefundReason.setText(refundReasons.get(options1))).build();
            pvOptions.setPicker(refundReasons);
            pvOptions.setSelectOptions(refundReasons.indexOf(mBinding.tvRefundReason.getText().toString()));
            pvOptions.show();
        }
        //提交
        else if (view.getId() == R.id.tv_confirm) {

            String price = mBinding.etCustomPrice.getText().toString();
            String reasons = mBinding.tvRefundReason.getText().toString();
            String goodsInfo = mBinding.tvGoodStatus.getText().toString();

            if (Strings.toDouble(price) <= 0) {
                ToastUtils.showShort(R.string.sharemall_refund_money_must_gt_zero);
            } else if (applyType == RefundParam.REFUND_TYPE_GOODS_AND_CASH && TextUtils.isEmpty(goodsInfo)) {
                ToastUtils.showShort(R.string.sharemall_please_select_cargo_status);
            } else if (TextUtils.isEmpty(reasons)) {
                ToastUtils.showShort(R.string.sharemall_please_select_back_money_reason);
            } else {
                if (Strings.isEmpty(photoAdapter.getItems())) {
                    applyForRefund(null);
                } else {
                    filePresenter.doUploadFiles(photoAdapter.getItems(), true);
                }
            }
        }

    }

    private void applyForRefund(List<String> imgs) {
        if (isUpdate()) {
            doUpdateRefund(imgs);
            return;
        }
        if (applyType == RefundParam.REFUND_TYPE_GOODS_AND_CASH) {
            doApplyRefundGood(imgs);
        } else {
            doApplyRefund(imgs);
        }
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {
        applyForRefund(imgUrls);
    }

    @Override
    public void fileUploadFail(String errMsg) {
        showError(errMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_BACK
                || resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(requestCode == REQUEST_CODE_SELECT ? ImagePicker.EXTRA_RESULT_ITEMS : ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
        }
    }

    //获取退款金额和邮费
    private void doGetRefundPrice(String orderId) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getRefundPrice(orderId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<RefundPriceEntity>>() {
                    @Override
                    public void onSuccess(BaseData<RefundPriceEntity> data) {
                        if (dataExist(data)) {
                            postage = Strings.toDouble(data.getData().getPostage());
                            maxPrice = Strings.toDouble(data.getData().getRefund_price());
                            maxPrice = data.getData().hasPostage() ? maxPrice + postage : maxPrice;
                            if (!isUpdate()) {
                                resetRefundPrice(maxPrice);
                            }
                            mBinding.tvRefundPriceDescribe.setText(getString(R.string.sharemall_format_refund_price_describe,
                                    FloatUtils.formatMoney(maxPrice), data.getData().hasPostage() ? "" : getString(R.string.sharemall_do_not), FloatUtils.formatMoney(postage)));
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        doListReason();
                    }
                });
    }

    /**
     * 获取后台配置申请理由列表
     */
    private void doListReason() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listRefundReason(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<RefundReasonEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<RefundReasonEntity>> data) {
                        if (!Strings.isEmpty(data.getData())) {
                            refundReasons.clear();
                            for (RefundReasonEntity entity : data.getData()) {
                                refundReasons.add(entity.getName());
                            }
                        }
                    }
                });
    }

    //仅退款
    private void doApplyRefund(List<String> imgs) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .applyRefund(skusEntity.getId(),
                        mBinding.tvRefundReason.getText().toString(),
                        mBinding.etRefundRemark.getText().toString(),
                        mBinding.etCustomPrice.getText().toString(), imgs)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        JumpUtil.overlay(getContext(), RefundApplySuccessActivity.class, REFUND_TIP, getString(R.string.sharemall_refund_apply_success));
                        finish();
                    }
                });
    }

    //退货并退款
    private void doApplyRefundGood(List<String> imgs) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .applyRefundGood(skusEntity.getId(),
                        mBinding.tvRefundReason.getText().toString(),
                        mBinding.etRefundRemark.getText().toString(),
                        mBinding.etCustomPrice.getText().toString(),
                        imgs, goodsStatusList.indexOf(mBinding.tvGoodStatus.getText().toString()))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        JumpUtil.overlay(getContext(), RefundApplySuccessActivity.class, REFUND_TIP, getString(R.string.sharemall_refund_apply_success2));
                        finish();
                    }
                });
    }

    //修改申请
    private void doUpdateRefund(List<String> imgs) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .updateApplyRefundGood(skusEntity.getId(),
                        mBinding.tvRefundReason.getText().toString(),
                        mBinding.etRefundRemark.getText().toString(),
                        mBinding.etCustomPrice.getText().toString(),
                        imgs, refundEntity.getType(),
                        applyType == RefundParam.REFUND_TYPE_GOODS_AND_CASH ? String.valueOf(goodsStatusList.indexOf(mBinding.tvGoodStatus.getText().toString())) : null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        MApplication.getInstance().appManager.finishActivity(RefundDetailedActivity.class);
                        JumpUtil.overlay(getContext(), RefundApplySuccessActivity.class, REFUND_TIP, getString(R.string.sharemall_refund_apply_success2));
                        finish();
                    }
                });
    }
}
