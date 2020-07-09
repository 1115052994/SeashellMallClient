package com.liemi.seashellmallclient.ui.mine.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundListEntity;
import com.liemi.seashellmallclient.data.event.OrderRefundEvent;
import com.liemi.seashellmallclient.databinding.ActivityOrderRefundBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemRefundOrderBinding;
import com.liemi.seashellmallclient.ui.mine.order.OrderGoodsAdapter;
import com.liemi.seashellmallclient.utils.SobotApiUtils;
import com.liemi.seashellmallclient.widget.MyRecyclerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.IntentUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.widget.ConfirmDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.annotations.NonNull;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity.REFUND_ID;

public class OrderRefundActivity extends BaseXRecyclerActivity<ActivityOrderRefundBinding, RefundListEntity> {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_order_refund;
    }


    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_refund_type_money_good));
        EventBus.getDefault().register(this);

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<RefundListEntity, BaseViewHolder>(this, xRecyclerView, R.layout.baselib_include_no_data_view) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_refund_order;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_contact_service) {
                            doGetSobotInfo();
                        } else if (i == R.id.tv_order_see) {
                            JumpUtil.overlay(getContext(), RefundDetailedActivity.class, REFUND_ID, getItem(position).getId());
                        }

                    }

                    @Override
                    public SharemallItemRefundOrderBinding getBinding() {
                        return (SharemallItemRefundOrderBinding) super.getBinding();
                    }

                    @Override
                    public void bindData(Object item) {
                        MyRecyclerView rvGoods = getBinding().rvGoods;
                        rvGoods.setNestedScrollingEnabled(false);
                        rvGoods.setLayoutManager(new LinearLayoutManager(context));
                        BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter = new OrderGoodsAdapter(getContext());

                        List<OrderSkusEntity> meOrders = new ArrayList<>();
                        OrderSkusEntity goodEntity = new OrderSkusEntity();
                        goodEntity.setImg_url(getItem(position).getItem_img());
                        goodEntity.setSpu_name(getItem(position).getSpu_name());
                        goodEntity.setSku_price(getItem(position).getSku_price());
                        goodEntity.setValue_names(getItem(position).getValue_name());
                        goodEntity.setNum(getItem(position).getNum());
                        goodEntity.setValue_names(getItem(position).getValue_name());
                        goodEntity.setActivity_type(getItem(position).getActivity_type());
                        meOrders.add(goodEntity);
                        rvGoods.setAdapter(goodAdapter);
                        goodAdapter.setData(meOrders);
                        super.bindData(item);
                        //设置字体颜色
                        switch (getItem(position).getStatusToString()) {
                            case "申请中":
                                getBinding().tvRefundStaus.setTextColor(getResources().getColor(R.color.gray_99));
                                break;
                            case "退款成功":
                                getBinding().tvRefundStaus.setTextColor(getResources().getColor(R.color.sharemall_green_1eb090));
                                break;
                            default:
                                getBinding().tvRefundStaus.setTextColor(getResources().getColor(R.color.bgColor));
                                break;
                        }
                    }
                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            new ConfirmDialog(this)
                    .setContentText(getString(R.string.sharemall_service_phone) + "：" + AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel())
                    .setConfirmText(getString(R.string.sharemall_call))
                    .setConfirmListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(IntentUtils.getDialIntent(AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel()));
                        }
                    }).show();

        } else {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateRefundData(OrderRefundEvent event) {
        if (xRecyclerView != null) {
            xRecyclerView.refresh();
        }
    }

    @Override
    public void showData(PageEntity<RefundListEntity> pageEntity) {
        if (adapter == null) {
            showError(getString(R.string.sharemall_initialize_adapter_first));
            return;
        }

        if (pageEntity == null) return;

        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(loadMoreEnabled);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listOrderRefund(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.<BaseData<PageEntity<RefundListEntity>>>compose())
                .compose((this).<BaseData<PageEntity<RefundListEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<PageEntity<RefundListEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<PageEntity<RefundListEntity>> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            showData(data.getData());
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


    private void doGetSobotInfo() {
        /*String liemi_intel_tel = AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + liemi_intel_tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doGetSobotInfo(0, null, null)
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
