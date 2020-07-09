package com.liemi.seashellmallclient.ui.mine.vip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.order.OrderRebateEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityPerformanceManagementDetailBinding;
import com.liemi.seashellmallclient.ui.mine.order.OrderGoodsAdapter;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class PerformanceManagementDetailActivity extends BaseActivity<ActivityPerformanceManagementDetailBinding> {
    private String orderNo;
    private BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_performance_management_detail;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_vip_income_detail));
        mBinding.rvGoods.setAdapter(goodAdapter = new OrderGoodsAdapter(getContext()));
        mBinding.rvGoods.setNestedScrollingEnabled(false);
        mBinding.rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initGoodUI(OrderRebateEntity entity) {
        goodAdapter.setData(entity.getList());
    }

    @Override
    protected void initData() {
        doGetIncomeDetail(getIntent().getStringExtra(VipParam.awardId));
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_copy) {
            KeyboardUtils.putTextIntoClip(getContext(), orderNo);
        }
    }

    private void doGetIncomeDetail(String orderId) {
        if (Strings.isEmpty(orderId)) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_parameters));
            finish();
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(VIPApi.class)
                .getOrderRebate(orderId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderRebateEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<OrderRebateEntity> data) {
                        if (data.getData() == null) {
                            ToastUtils.showShort(R.string.sharemall_lack_info);
                            finish();
                        }
                        orderNo = data.getData().getOrder_no();
                        mBinding.setData(data.getData());
                        initGoodUI(data.getData());
                    }

                    @Override
                    public void onFail(BaseData<OrderRebateEntity> data) {
                        super.onFail(data);
                        finish();
                    }

                });
    }
}
