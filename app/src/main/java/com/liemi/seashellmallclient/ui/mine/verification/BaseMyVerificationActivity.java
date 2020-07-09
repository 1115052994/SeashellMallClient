package com.liemi.seashellmallclient.ui.mine.verification;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.api.VerificationApi;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDelEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.liemi.seashellmallclient.data.event.VerificationOrderUpdateEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.ui.BaseBlackTitleActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_DO_DELETE;
import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_MPID;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;

public abstract class BaseMyVerificationActivity<T extends ViewDataBinding> extends BaseBlackTitleActivity<T> implements VerificationClickOrderButtonListener {

    protected boolean finishPage = false;

    private String last_pay_channel;

    //点击item左边的按钮
    @Override
    public void clickLeftButton(VerificationOrderDetailEntity entity) {
        int status = Strings.toInt(entity.getStatus());
        switch (status) {   //删除
            case OrderParam.VERIFICATION_WAIT_COMMENT:
            case OrderParam.VERIFICATION_REFUND_SUCCESS:
            case OrderParam.VERIFICATION_SUCCESS:
                //删除订单
                showFunctionConfirm(getString(R.string.sharemall_delete), entity, ORDER_DO_DELETE);
                break;

        }
    }

    //点击右边的按钮
    @Override
    public void clickRightButton(VerificationOrderDetailEntity entity) {

        int status = Strings.toInt(entity.getStatus());

        switch (status) {   //评价 去付款
            case OrderParam.VERIFICATION_WAIT_PAY:
                //去付款
                //路由跳转
                doGetPayEntity(Strings.toInt(entity.getOrder_main_id()),entity);
                break;
            case OrderParam.VERIFICATION_WAIT_COMMENT:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(ORDER_ENTITY, entity);
                JumpUtil.overlay(getContext(), VerificationMineCommentActivity.class, bundle1);
                break;

        }
    }

    //点击按钮的弹出框
    private void showFunctionConfirm(String info, final VerificationOrderDetailEntity entity, final String scenario) {
        new ConfirmDialog(getContext())
                .setContentText(getString(R.string.sharemall_format_confirm, info))
                .setConfirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doOrderUpdate(entity, scenario);
                    }
                }).show();
    }

    private void doOrderUpdate(final VerificationOrderDetailEntity entity, final String scenario) {
        showProgress("");
        Observable<BaseData> observableDel = null;
        switch (scenario) {
            //删除订单
            case ORDER_DO_DELETE:
                observableDel = RetrofitApiFactory.createApi(VerificationApi.class).doDelVerificationOrder(entity.getId());
                break;
        }

        observableDel.compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        if (dataExist(data)){
                            EventBus.getDefault().post(new VerificationOrderUpdateEvent(entity.getId(), -1));
                        }else {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }


    private void doGetPayEntity(int main_order_id,VerificationOrderDetailEntity entity) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .doGetPayEntity(main_order_id+"")
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<OrderPayEntity>>() {
                    @Override
                    public void onNext(BaseData<OrderPayEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE){
                            Bundle bundle = new Bundle();
                            OrderPayEntity payEntity = data.getData();
                            payEntity.setLast_pay_channel(last_pay_channel);
                            bundle.putInt("type",1);
                            bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                            bundle.putString(ORDER_MPID, entity.getOrder_no());
                            bundle.putString("shop_id",entity.getShop().getId());
                            JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                        }else if (data.getErrcode()==999999){
                            showError(data.getErrmsg());
                        }

                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {

                    }
                });
    }
}
