package com.liemi.seashellmallclient.ui.mine.order;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.good.PayErrorGoods;
import com.liemi.seashellmallclient.data.entity.good.ShareImgEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.event.OrderUpdateEvent;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.ui.good.order.OrderPayOnlineActivity;
import com.liemi.seashellmallclient.ui.sharemoment.DialogShareImg;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.param.OrderParam.*;
import static com.liemi.seashellmallclient.ui.good.order.PayErrorActivity.PAY_FAIL_GOODS;
import static com.liemi.seashellmallclient.ui.mine.order.LogisticTrackActivity.MPID;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;
import static com.netmi.baselibrary.data.Constant.SUCCESS_CODE;

public abstract class BaseMineOrderActivity<T extends ViewDataBinding> extends BaseActivity<T> implements OrderModuleFragment.ClickOrderButtonListener {

    private String last_pay_channel;
    private List<String> shop_list;

    //点击item左边的按钮
    @Override
    public void clickLeftButton(OrderDetailedEntity entity) {
        int status = entity.getStatus();
        switch (status) {
            case OrderParam.ORDER_WAIT_PAY:
            case OrderParam.ORDER_WAIT_SEND:
                //取消订单
                showFunctionConfirm(getString(R.string.sharemall_order_cancel), entity, ORDER_DO_CANCEL);
                break;
            case OrderParam.ORDER_WAIT_RECEIVE:
                //查看物流
                //跳转到查看物流页面
                JumpUtil.overlay(getContext(), LogisticTrackActivity.class,
                        new FastBundle().putString(MPID, entity.getOrder_no()));
                break;
            case ORDER_WAIT_COMMENT:
                break;
        }
    }

    //点击右边的按钮
    @Override
    public void clickRightButton(OrderDetailedEntity entity) {
        last_pay_channel = entity.getLast_pay_channel();


        int status = entity.getStatus(), groupStatus = entity.isGroupOrder();

        //拼团失败
        if (entity.isGroupOrder() == GrouponParam.GROUPON_ORDER_FAIL) {
            showFunctionConfirm(getString(R.string.sharemall_order_delete), entity, ORDER_DO_DELETE);
            return;
        }

        switch (status) {
            case OrderParam.ORDER_WAIT_PAY:
                //去付款
                //路由跳转
                doGetPayEntity(entity);
                break;
            case OrderParam.ORDER_WAIT_SEND:

                //提醒发货
                doOrderUpdate(entity, ORDER_DO_REMIND);
                break;
            case OrderParam.ORDER_WAIT_RECEIVE:
                //确认收货
                showFunctionConfirm(getString(R.string.sharemall_order_receive_good), entity, ORDER_DO_ACCEPT);
                break;
            case ORDER_WAIT_COMMENT:
                //去评论
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(ORDER_ENTITY, entity);
                JumpUtil.overlay(getContext(), MineCommentActivity.class, bundle1);
                break;
            case ORDER_CANCEL:
            case OrderParam.ORDER_SUCCESS:
                //删除订单
                showFunctionConfirm(getString(R.string.sharemall_order_delete), entity, ORDER_DO_DELETE);
                break;
        }
    }

    //点击按钮的弹出框
    private void showFunctionConfirm(String info, final OrderDetailedEntity entity, final String scenario) {
        new ConfirmDialog(getContext())
                .setContentText(getString(R.string.sharemall_format_confirm, info))
                .setConfirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doOrderUpdate(entity, scenario);
                    }
                }).show();
    }

    private MineOrderDetailsActivity getCurrentActivity() {
        return getActivity() instanceof MineOrderDetailsActivity ? (MineOrderDetailsActivity) getActivity() : null;
    }

    private void doOrderUpdate(final OrderDetailedEntity entity, final String scenario) {
        showProgress("");
        //默认是取消订单
        Observable<BaseData> observable = RetrofitApiFactory.createApi(OrderApi.class).cancelOrder(String.valueOf(entity.getMain_order_id()));

        switch (scenario) {
            //提醒发货
            case ORDER_DO_REMIND:
                observable = RetrofitApiFactory.createApi(OrderApi.class).remindOrder(entity.getOrder_id());
                break;
            //确认收货
            case ORDER_DO_ACCEPT:
                observable = RetrofitApiFactory.createApi(OrderApi.class).confirmReceipt(entity.getOrder_id());
                break;
            //删除订单
            case ORDER_DO_DELETE:
                observable = RetrofitApiFactory.createApi(OrderApi.class).delOrder(String.valueOf(entity.getMain_order_id()));
                break;
        }

        observable.compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            switch (scenario) {
                                case ORDER_DO_DELETE:
                                    EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), -1));
                                    if (getCurrentActivity() != null) {
                                        finish();
                                    }
                                    break;
                                case ORDER_DO_CANCEL:
                                    //取消订单之后，直接从本地列表删除订单，同时发送状态，通知全部订单里面的数据进行刷新
                                    EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), ORDER_CANCEL));
                                    if (getCurrentActivity() != null) {
                                        getCurrentActivity().doGetOrderDetails();
                                    }
                                    break;
                                case ORDER_DO_REMIND:
                                    ToastUtils.showShort(getString(R.string.sharemall_operation_success));
                                    break;
                                case ORDER_DO_ACCEPT:
                                    EventBus.getDefault().post(new OrderUpdateEvent(entity.getId(), ORDER_WAIT_COMMENT));
                                    if (getCurrentActivity() != null) {
                                        getCurrentActivity().doGetOrderDetails();
                                    }
                                    break;
                            }
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


    private void doGetPayEntity(OrderDetailedEntity entity) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .doGetPayEntity(entity.getMain_order_id())
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<OrderPayEntity>>() {
                    @Override
                    public void onNext(BaseData<OrderPayEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE && data.getData() != null) {
                            hideProgress();
                            shop_list = new ArrayList<>();
                            for (OrderDetailedEntity.MainOrdersBean mainOrdersBean : entity.getMainOrders()) {
                                shop_list.add(mainOrdersBean.getShop_id());
                            }
                            Bundle bundle = new Bundle();
                            OrderPayEntity payEntity = data.getData();
                            payEntity.setLast_pay_channel(last_pay_channel);
                            bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                            bundle.putSerializable(PAY_FAIL_GOODS, new PayErrorGoods(payEntity).getGoodsList(entity.getGoods()));
                            bundle.putString(ORDER_MPID, String.valueOf(entity.getMain_order_id()));
                            bundle.putSerializable("shop_id", (Serializable) shop_list);
                            JumpUtil.overlay(getContext(), OrderPayOnlineActivity.class, bundle);
                        } else {
                            showError(data.getErrmsg());
                        }

                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    protected void onError(ApiException ex) {

                    }
                });
    }

}
