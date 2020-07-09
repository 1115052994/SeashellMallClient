package com.liemi.seashellmallclient.ui.mine.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.order.LogisticEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.event.OrderRefreshEvent;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.data.param.RefundParam;
import com.liemi.seashellmallclient.databinding.ActivityMineOrderDetailsBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemOrderDetailsBinding;
import com.liemi.seashellmallclient.databinding.SharemallLayoutMineOrderDetailsLogisticsBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.refund.ApplyRefundTypeActivity;
import com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.utils.SobotApiUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.annotations.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity.REFUND_ID;

public class MineOrderDetailsActivity extends BaseMineOrderActivity<ActivityMineOrderDetailsBinding> {
    //需要上个页面传递过来订单id
    public static final String ORDER_DETAILS_ID = "orderDetailsId";

    public static final String ORDER_ENTITY = "order_entity";

    //根据需求查看是否加载物流信息View
    private ViewStub mLogisticsViewStub;
    //物流信息
    private String mLogisticsInfo;
    //订单id
    private String mOrderId;
    //订单详情数据
    private OrderDetailedEntity mOrderDetailsEntity;
    //订单列表
    private BaseRViewAdapter<OrderDetailedEntity.MainOrdersBean, BaseViewHolder> orderAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_order_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_order_details));
        mLogisticsViewStub = mBinding.vsLogistics.getViewStub();
        mLogisticsViewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                SharemallLayoutMineOrderDetailsLogisticsBinding logisticsBinding = DataBindingUtil.bind(inflated);
                logisticsBinding.setLogisticsInfo(mLogisticsInfo);
            }
        });

        mBinding.rvOrder.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvOrder.setAdapter(orderAdapter = new BaseRViewAdapter<OrderDetailedEntity.MainOrdersBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_order_details;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<OrderDetailedEntity.MainOrdersBean>(binding) {
                    @Override
                    public void bindData(OrderDetailedEntity.MainOrdersBean item) {
                        BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter;
                        getBinding().rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                        getBinding().rvGoods.setAdapter(goodAdapter = new OrderGoodsAdapter(getContext(),
                                false, true, (View view, OrderSkusEntity skusEntity) -> {
                            if (view.getId() == R.id.tv_apply_after_sales) {
                                //跳转到申请售后页面
                                if (skusEntity.getStatus() == OrderParam.ORDER_WAIT_SEND || skusEntity.getStatus() == OrderParam.ORDER_WAIT_RECEIVE) {
                                    Bundle bundle = new Bundle();
                                    if (getItem(position).getShop() != null) {
                                        skusEntity.setShopName(getItem(position).getShop().getName());
                                    }
                                    bundle.putSerializable(RefundParam.SKU_ENTITY, skusEntity);
                                    JumpUtil.overlay(getActivity(), ApplyRefundTypeActivity.class, bundle);
                                } else {
                                    //跳转到退款详情页
                                    Bundle bundle = new Bundle();
                                    bundle.putString(REFUND_ID, String.valueOf(skusEntity.getId()));
                                    JumpUtil.overlay(getActivity(), RefundDetailedActivity.class, bundle);
                                }
                            } else {
                                GoodDetailPageActivity.start(getContext(), skusEntity.getItem_id(), new FastBundle().putInt(GoodsParam.ITEM_TYPE, skusEntity.getItem_type()));
                            }
                        }));
                        goodAdapter.setData(item.getOrderSkus());
                        super.bindData(item);
                    }

                    @Override
                    public SharemallItemOrderDetailsBinding getBinding() {
                        return (SharemallItemOrderDetailsBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        if (view.getId() == R.id.tv_store_name) {
                            if (!TextUtils.isEmpty(getItem(position).getShop_id())) {
                                StoreDetailActivity.start(getContext(), getItem(position).getShop_id());
                            }
                        }
                    }
                };
            }
        });
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

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_order_function1) {
            clickLeftButton(mOrderDetailsEntity);
            return;
        }
        if (view.getId() == R.id.tv_order_function2) {
            clickRightButton(mOrderDetailsEntity);
            return;
        }
        if (view.getId() == R.id.ll_logistics) {

            Bundle bundle = new Bundle();
            bundle.putString(LogisticTrackActivity.MPID, mOrderDetailsEntity.getOrder_no());
            JumpUtil.overlay(this, LogisticTrackActivity.class, bundle);

            return;
        }
        if (view.getId() == R.id.tv_store_name) {
            if (!TextUtils.isEmpty(mOrderDetailsEntity.getMainOrder().getShop_id())) {
                StoreDetailActivity.start(getContext(), mOrderDetailsEntity.getMainOrder().getShop_id());
            }
            return;
        }

        //联系客服
        if (view.getId() == R.id.tv_contact_service) {
            doGetSobotInfo();
        }

    }

    //EventBus如果退款退货成功，就刷新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderRefresh(OrderRefreshEvent event) {
        Logs.i("退款/退款退货成功，订单详情页面刷新数据");
        doGetOrderDetails();
    }

    //设置数据
    private void showData(OrderDetailedEntity entity) {
        this.mOrderDetailsEntity = entity;
        mBinding.setItem(entity);
        orderAdapter.setData(entity.getMainOrders());
        if (entity.getStatus() == OrderParam.ORDER_WAIT_RECEIVE || entity.getStatus() == OrderParam.ORDER_WAIT_COMMENT || entity.getStatus() == OrderParam.ORDER_SUCCESS) {
            //待收货，待评价，已完成订单会显示物流信息
            //请求物流信息
            doGetLogistic(entity.getOrder_no());
        }
    }


    //请求订单详情
    public void doGetOrderDetails() {
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

    //请求物流详情
    private void doGetLogistic(String orderNo) {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLogistic(orderNo)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<LogisticEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<List<LogisticEntity>> data) {
                        if (dataExist(data) && !Strings.isEmpty(data.getData().get(0).getList())) {
                            mLogisticsInfo = data.getData().get(0).getList().get(0).getContent();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mLogisticsViewStub.getParent() != null) {
                            mLogisticsViewStub.inflate();
                        }
                    }
                });
    }

    //联系客服
    private void doGetSobotInfo() {
        /*String liemi_intel_tel = AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + liemi_intel_tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doGetSobotInfo(0, mOrderDetailsEntity.getMainOrder().getShop_id(), null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<SobotSystemEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<SobotSystemEntity> data) {
                        SobotApiUtils.getInstance().toCustomServicePage(getContext(), UserInfoCache.get(), null, data.getData());
                    }
                });
    }

}
