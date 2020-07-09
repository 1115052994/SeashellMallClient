package com.liemi.seashellmallclient.ui.mine.refund;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.order.RefundDetailsEntity;
import com.liemi.seashellmallclient.data.event.OrderRefreshEvent;
import com.liemi.seashellmallclient.data.event.OrderRefundEvent;
import com.liemi.seashellmallclient.databinding.*;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressDialog;
import com.liemi.seashellmallclient.ui.mine.order.OrderGoodsAdapter;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.utils.SobotApiUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import cn.iwgang.countdownview.CountdownView;
import io.reactivex.annotations.NonNull;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.liemi.seashellmallclient.data.entity.order.RefundDetailsEntity.REFUND_STATUS_LOGISTICS_FILLED;
import static com.liemi.seashellmallclient.ui.mine.refund.RefundApplyLogisticActivity.SUB_ORDER_DETAILED;

public class RefundDetailedActivity extends BaseActivity<ActivityRefundDetailedBinding> {
    public static final String REFUND_ID = "REFUND_ID";
    private String refundId;
    private RefundDetailsEntity detailedEntity;
    private BaseRViewAdapter<String, BaseViewHolder> hintAdapter;
    private BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter;
    private AddressDialog mDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_detailed;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_refund_detail));
        EventBus.getDefault().register(this);
        //退款订单id
        refundId = getIntent().getStringExtra(REFUND_ID);

        if (TextUtils.isEmpty(refundId)) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_parameters));
            finish();
            return;
        }

        //请求退款详情
        doGetRefundDetailed();
        hintAdapter = new BaseRViewAdapter<String, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_refund_hint;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                };
            }
        };
        mBinding.rvHint.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvHint.setNestedScrollingEnabled(false);
        mBinding.rvHint.setAdapter(hintAdapter);

        goodAdapter = new OrderGoodsAdapter(getContext(), new OrderGoodsAdapter.GoodsClickListener() {
            @Override
            public void doClick(View view, OrderSkusEntity item) {
                GoodDetailPageActivity.start(getContext(), item.getItem_id(), null);
            }
        });
        mBinding.rvRefundGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvRefundGoods.setNestedScrollingEnabled(false);
        mBinding.rvRefundGoods.setAdapter(goodAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        //取消申请
        if (i == R.id.tv_refund_details_wait_deal_cancel_apply || i == R.id.tv_refund_details_input_no_cancel_apply) {
            showCancelApplyDialog();
            return;
        }
        //修改申请信息
        if (i == R.id.tv_refund_details_wait_deal_update_apply) {
            //已填写物流的话，就显示修改物流
            if (detailedEntity.getRefund_status() == REFUND_STATUS_LOGISTICS_FILLED) {
                Bundle updateApplyBundle = new Bundle();
                updateApplyBundle.putSerializable(SUB_ORDER_DETAILED, detailedEntity);
                JumpUtil.overlay(this, RefundApplyLogisticActivity.class, updateApplyBundle);
            }
            //修改申请
            else {
                ApplyForRefundActivity.start(getContext(), null, detailedEntity.getType(), detailedEntity);
            }
            return;
        }
        //填写订单号
        if (i == R.id.tv_refund_details_input_no) {
            Bundle updateApplyBundle = new Bundle();
            updateApplyBundle.putSerializable(SUB_ORDER_DETAILED, detailedEntity);
            JumpUtil.overlay(this, RefundApplyLogisticActivity.class, updateApplyBundle);
            return;
        }
        //重新申请
        if (i == R.id.tv_refund_details_refuse_again_apply) {
            ApplyForRefundActivity.start(getContext(), null, detailedEntity.getType(), detailedEntity);
            return;
        }

        if (i == R.id.tv_constant_shop) {
            doGetSobotInfo();
            return;
        }

        if (i == R.id.tv_store_name) {
            StoreDetailActivity.start(getContext(), detailedEntity.getShop_id());
        }
    }

    //显示取消申请的对话框
    private void showCancelApplyDialog() {
        if (mDialog == null) {
            mDialog = new AddressDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialog.setTitle(getString(R.string.sharemall_hint2));
        mDialog.setMessage(getString(R.string.sharemall_confirm_cancel_refund_apply));

        mDialog.setClickConfirmListener(this::doCancelRefundApply);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void updateRefundData(OrderRefundEvent event) {
        if (!TextUtils.isEmpty(refundId)
                && !TextUtils.equals(AppManager.getInstance().currentActivity().getClass().getName(), getClass().getName())) {
            doGetRefundDetailed();
        }
    }


    private void getRefundDetailSuccess(RefundDetailsEntity detailedEntity) {
        controlRefundStep(detailedEntity);      //控制退款的进度显示
        goodAdapter.setData(detailedEntity.getGoods());
        hintAdapter.setData(detailedEntity.getHints());
        if (detailedEntity.getSecond() > 0) {
            mBinding.llLeftTime.setVisibility(View.VISIBLE);
            mBinding.cvTime.start(detailedEntity.initMillisecond());
            mBinding.cvTime.setOnCountdownEndListener((CountdownView cv) -> doGetRefundDetailed());
        } else {
            mBinding.llLeftTime.setVisibility(View.GONE);
        }
    }

    private void controlRefundStep(RefundDetailsEntity detailedEntity) {
        mBinding.vsRefundDetailsWaitDeal.getRoot().setVisibility(View.GONE);
        mBinding.vsRefundDetailsSuccess.getRoot().setVisibility(View.GONE);
        mBinding.vsRefundDetailsRefuse.getRoot().setVisibility(View.GONE);
        mBinding.vsRefundDetailsFail.getRoot().setVisibility(View.GONE);
        if (detailedEntity.getType() == 1) {
            switch (detailedEntity.getStatus()) {//退款状态 0: 已取消退款退货 1：发起退款
                // 2：完成退款 3、拒绝退款4.取消申请  5退款失败（退款退货成功，打款失败）
                case 1:
                    mBinding.vsRefundDetailsWaitDeal.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mBinding.vsRefundDetailsSuccess.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mBinding.vsRefundDetailsRefuse.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mBinding.vsRefundDetailsFail.getRoot().setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            switch (detailedEntity.getRefund_status()) {//0.取消退款申请 1.发起退款退货申请
                // 2、卖家同意退货
                // 3、已填写物流单号，等待卖家审核  4、卖家拒绝 5、退款完成
                case 1:
                    //已填写物流单号，等待处理
                case 3:
                    mBinding.vsRefundDetailsWaitDeal.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 2:
                    //卖家同意退货，填写物流信息
                    mBinding.vsRefundDetailsInputNo.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 4:
                    //卖家拒绝
                    mBinding.vsRefundDetailsRefuse.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 5:
                    //退款完成
                    mBinding.vsRefundDetailsSuccess.getRoot().setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    //请求退款详情信息
    private void doGetRefundDetailed() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getRefundDetailed(refundId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<RefundDetailsEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<RefundDetailsEntity> data) {
                        if(dataExist(data)) {
                            detailedEntity = data.getData();
                            mBinding.setItem(data.getData());
                            mBinding.executePendingBindings();
                            getRefundDetailSuccess(detailedEntity);
                        } else {
                            finish();
                        }
                    }
                });
    }


    //取消退款/退款退货申请
    private void doCancelRefundApply() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .cancelRefundApply(refundId, detailedEntity.getType())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort(getString(R.string.sharemall_operation_success));
                        EventBus.getDefault().post(new OrderRefreshEvent());
                        finish();
                    }
                });
    }

    private void doGetSobotInfo() {
      /*  String liemi_intel_tel = AppConfigCache.get().getPlatformEntity().getLiemi_intel_tel();
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
