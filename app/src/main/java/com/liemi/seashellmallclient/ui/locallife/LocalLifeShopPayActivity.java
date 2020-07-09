package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.VerificationApi;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityLocalLifeShopPayBinding;
import com.liemi.seashellmallclient.ui.mine.verification.VerificationOrderPayOnlineActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.WalletExtractConfirmDialogFragment;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.text.DecimalFormat;

public class LocalLifeShopPayActivity extends BaseActivity<ActivityLocalLifeShopPayBinding> implements View.OnClickListener {

    private String score;
    private String shop_id;
    private OrderPayEntity payEntity;
    @Override
    protected int getContentView() {
        return R.layout.activity_local_life_shop_pay;
    }

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }


    @Override
    protected void initUI() {
        Intent intent = getIntent();
        shop_id = intent.getStringExtra(ParamConstant.SHOP_ID);
        String title = intent.getStringExtra("title");
//        score = intent.getStringExtra("score");
        mBinding.tvTitle.setText(title);
        InputFilter[] filters = {new MoneyValueFilter()};
        mBinding.tvMoney.setText(FloatUtils.formatMoney("0"));
        mBinding.etMoney.setFilters(filters);
        mBinding.setDoClick(this);
        mBinding.etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tvMoney.setText(FloatUtils.formatMoney(s.toString()));
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_buy){
            if (!TextUtils.isEmpty(mBinding.etMoney.getText().toString().trim())){
                doBuy(mBinding.etMoney.getText().toString().trim());
            }else {
                ToastUtils.showShort("支付金额不能为空！");
            }
        }
    }

    private void doBuy(String money) {
        showProgress("");
//        String money = mBinding.etMoney.getText().toString().trim();
        RetrofitApiFactory.createApi(VerificationApi.class)
                .doBuy(money,shop_id)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE){
                            Bundle bundle = new Bundle();
                            payEntity = data.getData();
                            bundle.putInt("type",0);
                            bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                            bundle.putString("shop_id",shop_id);
                            JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                        }else if (data.getErrcode()==999999){
                            showError(data.getErrmsg());
                        }
                    }

                });
    }

}
