package com.liemi.seashellmallclient.ui.good.order;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.RadioGroup;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.InvoiceEntity;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityInvoiceBinding;
import com.liemi.seashellmallclient.widget.InvoiceDialog;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class InvoiceActivity extends BaseActivity<ActivityInvoiceBinding> {

    private InvoiceEntity invoiceEntity = new InvoiceEntity();

    @Override
    protected int getContentView() {
        return R.layout.activity_invoice;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_invoice_information));
        getRightSetting().setText(getString(R.string.sharemall_invoice_notice));

        mBinding.rgContent.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            invoiceEntity.setInvoice_content(checkedId == R.id.rb_content_good ? getString(R.string.sharemall_good_detail) : "");
            toggleOpenInvoice(checkedId == R.id.rb_content_good);
        });
        mBinding.rgType.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            invoiceEntity.setType(checkedId == R.id.rb_company ? 2 : 1);
            toggleCompanyLayout(checkedId == R.id.rb_company);
        });
    }

    @Override
    protected void initData() {
        InvoiceEntity entity = (InvoiceEntity) getIntent().getSerializableExtra(OrderParam.INVOICE_ENTITY);
        if (entity != null) {
            this.invoiceEntity = entity;
            showData();
        } else {
            doGetInvoice();
        }
    }

    private void showData() {
        mBinding.setItem(invoiceEntity);
        mBinding.executePendingBindings();
    }

    private void toggleCompanyLayout(boolean isCompany) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mBinding.llContent, new Explode());
        }
        mBinding.llCompany.setVisibility(isCompany ? View.VISIBLE : View.GONE);
    }

    private void toggleOpenInvoice(boolean isOpen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mBinding.llContent, new Explode());
        }
        mBinding.llOpenInvoice.setVisibility(isOpen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            new InvoiceDialog(InvoiceActivity.this).show();
        } else if (view.getId() == R.id.bt_submit) {
            if (TextUtils.isEmpty(invoiceEntity.getInvoice_content())) {
                setResult(RESULT_OK);
                finish();
                return;
            }
            String phone = mBinding.etPhone.getText().toString();
            String mail = mBinding.etMail.getText().toString();
            String companyCode = mBinding.etCompanyCode.getText().toString();
            String companyName = mBinding.etCompanyName.getText().toString();
            if (!Strings.isPhone(phone)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_right_phone));
            } else if (!Strings.isEmail(mail)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_right_email));
            } else if (invoiceEntity.getType() == 2 && TextUtils.isEmpty(companyCode)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_taxpayer_identity_number));
            } else if (invoiceEntity.getType() == 2 && TextUtils.isEmpty(companyName)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_company_name));
            } else {
                invoiceEntity.setPhone(phone);
                invoiceEntity.setMail(mail);
                if (invoiceEntity.getType() == 2) {
                    invoiceEntity.setCompany_name(companyName);
                    invoiceEntity.setCompany_code(companyCode);
                }
                invoiceEntity.setInvoice_type(mBinding.tvType.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(OrderParam.INVOICE_ENTITY, invoiceEntity);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    protected void doGetInvoice() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getInvoice("")
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<InvoiceEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<InvoiceEntity> data) {
                        if (data.getData() != null) {
                            invoiceEntity = data.getData();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        showData();
                    }
                });
    }

}
