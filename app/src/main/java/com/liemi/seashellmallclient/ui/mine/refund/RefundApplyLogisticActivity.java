package com.liemi.seashellmallclient.ui.mine.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.order.LogisticCompanyEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundDetailsEntity;
import com.liemi.seashellmallclient.databinding.ActivityRefundApplyLogisticBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemSelect2Binding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.widget.MyBaseDialog;
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
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.ui.mine.refund.RefundApplySuccessActivity.REFUND_TIP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class RefundApplyLogisticActivity extends BaseActivity<ActivityRefundApplyLogisticBinding> implements FileUploadContract.View {
    public static final String SUB_ORDER_DETAILED = "subOrder";

    private List<String> company;
    private RefundDetailsEntity detailedEntity;
    private MyBaseDialog companyDialog;

    private String companyName;
    private String companyCode;

    private ArrayList<ImageItem> images;

    private PhotoAdapter photoAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply_logistic;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_refund_detail));
        mBinding.rvImg.setLayoutManager(new GridLayoutManager(getContext(), 3));
        photoAdapter = new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvImg.setAdapter(photoAdapter);
    }

    @Override
    protected void initData() {
        doListCompany();

        detailedEntity = (RefundDetailsEntity) getIntent().getSerializableExtra(SUB_ORDER_DETAILED);
        if (detailedEntity == null || Strings.isEmpty(detailedEntity.getGoods())) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_parameters));
            finish();
            return;
        }

        companyName = detailedEntity.getMail_name();
        companyCode = detailedEntity.getMail_code();

        if (!Strings.isEmpty(detailedEntity.getMail_no()) && detailedEntity.getImgs() != null) {
            images = new ArrayList<>();
            List<String> imgUrls = new ArrayList<>();
            for (RefundDetailsEntity.RefundImgsBean refundImgsBean : detailedEntity.getMeRefundImgs()) {
                ImageItem item = new ImageItem();
                item.path = refundImgsBean.getImg_url();
                imgUrls.add(item.path);
                images.add(item);
            }
            photoAdapter.setData(imgUrls);
        }

        mBinding.setItem(detailedEntity.getOrderSku().get(0));
        mBinding.setData(detailedEntity);
        basePresenter = new FileUploadPresenterImpl(this);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.rl_choose_logistic_company) {
            showCompanyDialog(company);
        } else if (i == R.id.tv_submit) {       //确定
            if (validateData(detailedEntity.getOrderSku().get(0).getId(),
                    mBinding.etLogisticNo.getText().toString().trim(),
                    companyName,
                    companyCode, null, 2)) {
                if (images == null || images.size() <= 0) {
                    doUpdateApplyLogistic(null);
                } else {
                    ((FileUploadPresenterImpl) basePresenter).doUploadFiles(ImageItemUtil.ImageItem2String(images), true);
                }
            }
        } else if (i == R.id.tv_close) {
            if (companyDialog != null) {
                companyDialog.dismiss();
            }
        }
    }

    private boolean validateData(String id,
                                 String logisticsNo,
                                 String logisticsCompany,
                                 String companyCode,
                                 List<String> imgs,
                                 int refundStatus) {
        if (Strings.isEmpty(id)) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_parameters));
            return false;
        }
        if (Strings.isEmpty(logisticsNo)) {
            ToastUtils.showShort(getString(R.string.sharemall_please_input_logistic_no));
            return false;
        }

        if (Strings.isEmpty(logisticsCompany)) {
            ToastUtils.showShort(getString(R.string.sharemall_please_choose_logistic_company));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case ImagePicker.RESULT_CODE_ITEMS:
                //添加图片返回
                if (data != null && requestCode == REQUEST_CODE_SELECT) {
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
            case ImagePicker.RESULT_CODE_BACK:
                //预览图片返回
                if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
        }
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {
        doUpdateApplyLogistic(imgUrls);
    }

    @Override
    public void fileUploadFail(String errMsg) {
        showError(errMsg);
    }

    private void showCompanyDialog(List<String> list) {
        if (companyDialog == null) {
            companyDialog = MyBaseDialog.getDialog(getContext(), R.layout.sharemall_dialog_level_altitude_list);
            ((TextView) companyDialog.findViewById(R.id.tv_title)).setText(getString(R.string.sharemall_logistic_company));
            companyDialog.findViewById(R.id.tv_close).setOnClickListener(this::doClick);
            BaseRViewAdapter<String, BaseViewHolder> menuAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {

                private int selectIndex = -1;

                @Override
                public int layoutResId(int viewType) {
                    return R.layout.sharemall_item_select2;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder(binding) {
                        @Override
                        public void bindData(Object item) {
                            super.bindData(item);
                            if (selectIndex == position) {
                                getBinding().ivSelected.setImageResource(R.drawable.sharemall_radius_15dp_gradient_ef574c);
                            } else {
                                getBinding().ivSelected.setImageResource(R.drawable.sharemall_radius_20dp_ff_stroke_1dp_55);
                            }
                        }

                        @Override
                        public SharemallItemSelect2Binding getBinding() {
                            return (SharemallItemSelect2Binding) super.getBinding();
                        }

                        @Override
                        public void doClick(View view) {
                            super.doClick(view);
                            notifyDataSetChanged();
                            if (companyDialog != null) {
                                companyDialog.dismiss();
                            }

                            selectIndex = position;
                            if (position < 0 || position >= companyList.size()) {
                                return;
                            }
                            companyName = companyList.get(position).getName();
                            companyCode = companyList.get(position).getId();
                            mBinding.tvLogisticCompany.setText(companyName);
                        }
                    };
                }
            };
            RecyclerView recyclerView = ((RecyclerView) companyDialog.findViewById(R.id.rv_list));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            menuAdapter.setData(list);
            recyclerView.setAdapter(menuAdapter);
        }
        companyDialog.showBottom();
    }

    private List<LogisticCompanyEntity> companyList;

    private void doListCompany() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listLogisticCompany(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<LogisticCompanyEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<LogisticCompanyEntity>> data) {
                        if (data.getData() != null && !data.getData().isEmpty()) {
                            companyList = data.getData();
                            company = new ArrayList<>();

                            for (LogisticCompanyEntity entity : companyList) {
                                company.add(entity.getName());
                            }
                        }
                    }
                });

    }

    private void doUpdateApplyLogistic(List<String> img_urls) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .updateApplyLogistic(detailedEntity.getOrderSku().get(0).getId(),
                        mBinding.etLogisticNo.getText().toString().trim(),
                        companyName,
                        companyCode, img_urls)
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
