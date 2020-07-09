package com.liemi.seashellmallclient.ui.mine.refund;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundListEntity;
import com.liemi.seashellmallclient.data.event.OrderRefundEvent;
import com.liemi.seashellmallclient.databinding.SharemallActivityXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemRefundOrderListBinding;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.utils.SobotApiUtils;
import com.liemi.seashellmallclient.widget.countdown.CountDownFixUtils;
import com.netmi.baselibrary.data.Constant;
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
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity.REFUND_ID;

public class RefundListActivity extends BaseXRecyclerActivity<SharemallActivityXrecyclerviewBinding, RefundListEntity> {

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_xrecyclerview;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                return R.layout.sharemall_item_refund_order_list;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<RefundListEntity>(binding) {

                    @Override
                    public void bindData(RefundListEntity item) {
                        SharemallItemRefundOrderListBinding refundBinding = (SharemallItemRefundOrderListBinding) getBinding();
                        if (item.getRefund().getSecond() > 0) {
                            CountDownFixUtils
                                    .getInstance()
                                    .fixCountDownView(refundBinding.cvTime, item,
                                            cv -> xRecyclerView.refresh());
                            refundBinding.cvTime.start(item.getMillisecond());
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_store_name) {
                            RefundListEntity entity = getItem(position);
                            if (entity.getRefund() != null && entity.getRefund().getShop() != null) {
                                StoreDetailActivity.start(getContext(), entity.getRefund().getShop().getId());
                                return;
                            }
                        } else if (i == R.id.tv_contact_service) {
                            doGetSobotInfo();
                            return;
                        }
                        JumpUtil.overlay(getContext(), RefundDetailedActivity.class, REFUND_ID, getItem(position).getId());
                    }

                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateRefundData(OrderRefundEvent event) {
        if (xRecyclerView != null) {
            xRecyclerView.refresh();
        }
    }

    private boolean first = false;

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listOrderRefund(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<RefundListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<RefundListEntity>> data) {
                        if (data.getData() != null
                                && !Strings.isEmpty(data.getData().getList())
                                && !first) {
                            data.getData().getList().get(0).getRefund().setSecond(5);
                            first = true;
                        }
                        showData(data.getData());
                    }
                });
    }


    private void doGetSobotInfo() {
       /* String liemi_intel_tel = AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel();
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
