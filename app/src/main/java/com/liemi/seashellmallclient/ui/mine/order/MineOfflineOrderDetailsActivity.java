package com.liemi.seashellmallclient.ui.mine.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewStub;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityMineOfflineOrderDetailsBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

public class MineOfflineOrderDetailsActivity extends BaseActivity<ActivityMineOfflineOrderDetailsBinding> {

    //需要上个页面传递过来订单id
    public static final String ORDER_DETAILS_ID = "orderDetailsId";

    public static final String ORDER_ENTITY = "order_entity";

    //订单id
    private String mOrderId;
    //订单详情数据
    private OrderDetailedEntity mOrderDetailsEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_offline_order_details;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_order_details));
    }

    @Override
    protected void initData() {
        mOrderId = getIntent().getStringExtra(ORDER_DETAILS_ID);
        if (TextUtils.isEmpty(mOrderId)) {
            showError(getString(R.string.sharemall_order_error_aguments));
            finish();
            return;
        }
        doGetOrderDetails();
    }

    //设置数据
    private void showData(OrderDetailedEntity entity) {
        this.mOrderDetailsEntity = entity;
        mBinding.setItem(entity);
        ShareMallUserInfoEntity userInfoEntity = UserInfoCache.get(ShareMallUserInfoEntity.class);
        mBinding.setUser(userInfoEntity);
    }

    private void doGetOrderDetails() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getOrderDetailed(mOrderId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<OrderDetailedEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        finish();
                    }

                    @Override
                    public void onNext(@NonNull BaseData<OrderDetailedEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null) {
                                showData(data.getData());
                            } else {
                                showError(getString(R.string.sharemall_lack_info));
                                finish();
                            }
                        } else {
                            showError(data.getErrmsg());
                            finish();
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
