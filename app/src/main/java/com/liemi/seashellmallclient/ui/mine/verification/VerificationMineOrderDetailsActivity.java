package com.liemi.seashellmallclient.ui.mine.verification;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.api.VerificationApi;
import com.liemi.seashellmallclient.data.entity.order.FillOrderEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.event.VerificationOrderUpdateEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityVerificationMineOrderDetailsBinding;
import com.liemi.seashellmallclient.ui.good.order.PayResultActivity;
import com.liemi.seashellmallclient.utils.QRCodeUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_MPID;

public class VerificationMineOrderDetailsActivity extends BaseMyVerificationActivity<ActivityVerificationMineOrderDetailsBinding> {

    //需要上个页面传递过来订单id
    public static final String ORDER_DETAILS_ID = "orderDetailsId";

    //订单id
    private String mOrderId;
    //订单详情数据
    private VerificationOrderDetailEntity mOrderDetailsEntity;
    private String validity;
    private OrderPayEntity payEntity;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_verification_mine_order_details;
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
        super.initUI();
        finishPage = true;

    }

    @Override
    protected void initData() {
        if (TextUtils.equals(getIntent().getStringExtra(ORDER_DETAILS_ID), "-1")) {
            showError(getString(R.string.sharemall_order_error_aguments));
            finish();
            return;
        }
        mOrderId = getIntent().getStringExtra(ORDER_DETAILS_ID);
        doGetOrderDetails();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        doGetOrderDetails();
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_pay) {
            //付款
//            JumpUtil.overlay(getContext(), VerificationMinOrderPaySuccessActivity.class);
            doGetPayEntity(Strings.toInt(mOrderDetailsEntity.getOrder_main_id()));
            return;
        }
        if (view.getId() == R.id.tv_order_function2) {  //去评价
            clickRightButton(this.mOrderDetailsEntity);
            return;
        }
        if (view.getId() == R.id.tv_copy_order_no) {
            copy(this.mOrderDetailsEntity.getOrder_no());
        }
        if (view.getId() == R.id.iv_code) {
            Bundle bundle = new Bundle();
            bundle.putString("status",this.mOrderDetailsEntity.getStatus());
            bundle.putString(VerificationQRCodeActivity.VERIFICATION_ID, mOrderDetailsEntity.getCode());
            bundle.putString("time",validity);
            bundle.putString("name",mOrderDetailsEntity.getTitle());
            JumpUtil.overlay(getContext(), VerificationQRCodeActivity.class,bundle);
        }

    }

    //设置数据
    private void showData(VerificationOrderDetailEntity entity) {
        this.mOrderDetailsEntity = entity;
        mBinding.setItem(entity);
        String start_date = entity.getStart_date();
        String startTime = start_date.replace("-", "/");
        String end_date = entity.getEnd_date();
        String endTime = end_date.replace("-", "/");
        validity = "有效期：" + startTime + "-" + endTime;
        mBinding.tvValidity.setText(validity);
        mBinding.tvGoodValidity.setText(validity);

        if (!TextUtils.isEmpty(mOrderDetailsEntity.getCode())){
            String verificationCode = getVerificationCode(mOrderDetailsEntity.getCode());
            mBinding.tvVerificatonCode.setText(verificationCode);
            Bitmap codeBitmap = QRCodeUtils.createQRCodeBitmap(mOrderDetailsEntity.getCode(), 120, 120, "UTF-8", "H", "2", Color.GRAY,Color.parseColor("#f6f6f7"));
            mBinding.ivCode.setImageBitmap(codeBitmap);
        }
        if (Strings.toInt(this.mOrderDetailsEntity.getStatus())!= OrderParam.VERIFICATION_WAIT_USE){
            mBinding.tvVerificatonCode.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public String getVerificationCode(String code) {
        StringBuffer b = new StringBuffer();
        int i = 0;
        int j = 4;
        while (i < code.length()) {
            String c;
            if (j >= code.length()) {
                c = code.substring(i);
            } else {
                c = code.substring(i, j);
            }
            boolean digitsOnly = TextUtils.isDigitsOnly(c);
            if (digitsOnly) {
                String s1 = c + " ";
                b.append(s1);
            } else {
                b.append(c);
            }
            i += 4;
            j += 4;
        }

        return String.valueOf(b);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdate(VerificationOrderUpdateEvent event) {
        hideProgress();
        if (event.getStatus() == 1) {
            doGetOrderDetails();
            return;
        }

    }

    /*
     * 复制到剪切板
     * */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showError("已复制");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //请求订单详情
    private void doGetOrderDetails() {
        showProgress("");
        RetrofitApiFactory.createApi(VerificationApi.class)
                .getVerificationOrderDetail(String.valueOf(mOrderId))
                .compose(RxSchedulers.<BaseData<VerificationOrderDetailEntity>>compose())
                .compose((this).<BaseData<VerificationOrderDetailEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<VerificationOrderDetailEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<VerificationOrderDetailEntity> data) {
                        if (dataExist(data)) {
                            showData(data.getData());
                        } else {
                            showError(data.getErrmsg());
                            finish();
                        }
                    }

                });
    }

    private void doOrderCreate(final FillOrderEntity orderCommand) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .createOrder(orderCommand)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        EventBus.getDefault().post(new ShopCartEvent());

                        hideProgress();
                        if (dataExist(data)) {
                            payEntity = data.getData();

                            if (!TextUtils.isEmpty(data.getData().getPay_order_no())) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("type",1);
                                bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                                bundle.putString(ORDER_MPID, mOrderDetailsEntity.getOrder_no());
                                JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                                finish();
                            }

                        } else {
                            finish();
                        }

                    }

                });
    }

    private void doGetPayEntity(int main_order_id) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .doGetPayEntity(main_order_id+"")
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE){
                            Bundle bundle = new Bundle();
                            payEntity = data.getData();
                            bundle.putInt("type",1);
                            bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                            bundle.putString(ORDER_MPID, mOrderDetailsEntity.getOrder_no());
                            bundle.putString("shop_id",mOrderDetailsEntity.getShop_id());
                            JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                        }else if (data.getErrcode()==999999){
                            showError(data.getErrmsg());
                        }
                    }

                });
    }
}
