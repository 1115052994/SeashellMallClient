package com.liemi.seashellmallclient.ui.mine.verification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import cn.iwgang.countdownview.CountdownView;
import com.alipay.sdk.app.PayTask;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.PayContract;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.order.PayResult;
import com.liemi.seashellmallclient.data.entity.order.WXPayData;
import com.liemi.seashellmallclient.data.entity.pay.UnionPayEntity;
import com.liemi.seashellmallclient.data.entity.user.MineIntegralNumEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.data.event.VerificationOrderUpdateEvent;
import com.liemi.seashellmallclient.data.event.WXPayResultEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityVerificationOrderPayOnlineBinding;
import com.liemi.seashellmallclient.presenter.PayPresenterImpl;
import com.liemi.seashellmallclient.ui.good.order.OrderPayOnlineActivity;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopPayActivity;
import com.liemi.seashellmallclient.ui.login.ForgetPassActivity;
import com.liemi.seashellmallclient.ui.login.ForgetPwdActivity;
import com.liemi.seashellmallclient.ui.login.LoginHomeActivity;
import com.liemi.seashellmallclient.ui.login.SetPayPasswordActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.PayDialogFragment;
import com.liemi.seashellmallclient.ui.mine.wallet.WalletExtractConfirmDialogFragment;
import com.liemi.seashellmallclient.widget.PayDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.*;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_MPID;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;

public class VerificationOrderPayOnlineActivity extends BaseActivity<ActivityVerificationOrderPayOnlineBinding> implements PayContract.View, WalletExtractConfirmDialogFragment.OnDialogListener {

    public static final String ORDER_PAY_ENTITY = "orderPayEntity";
    private OrderPayEntity payEntity;
    private Context context;
    private String orderId;
    //用户输入资产密码弹出框
    private PayDialog mInputPasswordDialog;
    private String ethPrice;
    private int mUserUsableIntegralNum;
    private WalletExtractConfirmDialogFragment walletExtractConfirmDialogFragment;

    private static final int ORDER_RESET = 30006;

    private int resetCount = 0;

    private Disposable timerDisposable;

    private UnionPayEntity unionPayEntity;
    private int type;

    private PayPresenterImpl payPresenter;

    private String money;
    private WalletInfoEntity walletInfoEntity;
    private String haiMoney;
    private String shop_id;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        MApplication.getInstance().appManager.finishAllActivity(MainActivity.class);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (timerDisposable != null) {
            timerDisposable.dispose();
            timerDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_order_pay_online;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_confirm_pay2));
        payPresenter = new PayPresenterImpl(this);
        payEntity = (OrderPayEntity) getIntent().getSerializableExtra(ORDER_PAY_ENTITY);
        shop_id = getIntent().getStringExtra("shop_id");
        if (TextUtils.equals(payEntity.getLast_pay_channel(), "1")) {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(true);
            mBinding.cbPayDigital.setChecked(false);
            mBinding.tvPrice.setText(payEntity.getShowPrice());
            mBinding.tvDigitalPrice.setVisibility(View.GONE);
            mBinding.tvConfirm.setText("确认支付"+payEntity.getShowPrice());
        } else {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(false);
            mBinding.cbPayDigital.setChecked(true);
            mBinding.tvPrice.setText(payEntity.getShowPrice());
            mBinding.tvDigitalPrice.setVisibility(View.VISIBLE);
            mBinding.tvDigitalPrice.setText(money);
            mBinding.tvConfirm.setText("确认支付"+payEntity.getShowPrice());
        }
        type = getIntent().getIntExtra("type",0);
        orderId = getIntent().getStringExtra(ORDER_MPID);
        if (payEntity == null && TextUtils.isEmpty(orderId)) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_order_info));
            finish();
            return;
        }

        if (payEntity==null) {
            doGetOrderPayInfo();
        } else {
            showData(payEntity);
        }

    }

    @Override
    protected void initData() {
        doGetMoney(payEntity.getPay_amount());
    }

    private void showData(OrderPayEntity payEntity) {
        long lastTime = DateUtil.strToLong(payEntity.getEnd_time()) - Calendar.getInstance().getTimeInMillis();
        if (lastTime > 0) {
            mBinding.cvTime.start(lastTime);
            mBinding.cvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    mBinding.cvTime.setVisibility(View.INVISIBLE);
                    mBinding.tvTip.setText(getString(R.string.sharemall_order_payment_overtime));
                }
            });
        } else {
            mBinding.cvTime.setVisibility(View.INVISIBLE);
            mBinding.tvTip.setText(getString(R.string.sharemall_order_payment_overtime));
        }
        mBinding.setItem(payEntity);
        mBinding.executePendingBindings();
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.cb_pay_wechat) {
            mBinding.cbPayWechat.setChecked(true);
            mBinding.cbPayAli.setChecked(false);
            mBinding.cbPayDigital.setChecked(false);
            mBinding.tvPrice.setText(payEntity.getShowPrice());
            mBinding.tvDigitalPrice.setVisibility(View.GONE);
            mBinding.tvConfirm.setText("确认支付"+payEntity.getShowPrice());
        } else if (i == R.id.cb_pay_ali) {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(true);
            mBinding.cbPayDigital.setChecked(false);
            mBinding.tvPrice.setText(payEntity.getShowPrice());
            mBinding.tvDigitalPrice.setVisibility(View.GONE);
            mBinding.tvConfirm.setText("确认支付"+payEntity.getShowPrice());
        }else if (i == R.id.cb_pay_digital) {
            mBinding.cbPayWechat.setChecked(false);
            mBinding.cbPayAli.setChecked(false);
            mBinding.cbPayDigital.setChecked(true);
            mBinding.tvPrice.setText(payEntity.getShowPrice());
            mBinding.tvDigitalPrice.setVisibility(View.VISIBLE);
            mBinding.tvDigitalPrice.setText(money);
            mBinding.tvConfirm.setText("确认支付"+payEntity.getShowPrice());
        } else if (i == R.id.tv_confirm) {
            if (Strings.toDouble(payEntity.getPay_amount()) > 0) {
                if (mBinding.cbPayAli.isChecked()) {
                    doIsPay("1","");

                } else if (mBinding.cbPayDigital.isChecked()) {
                    doIsPay("3",haiMoney);

                } else{
                    doIsPay("2","");

                }
            } else {
                doIsPay("3",haiMoney);
            }
        }
    }

    /*
    * 判断是否限额
    * */
    private void doIsPay(String type1,String hai) {
        List<String> list = new ArrayList<>();
        list.add(shop_id);
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .isPay(list,type1,payEntity.getPay_order_no(),hai)
                .compose(RxSchedulers.<BaseData<String>>compose())
                .compose((this).<BaseData<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<String>>(this) {
                    @Override
                    public void onSuccess(BaseData<String> data) {
                        switch (type1){
                            case "1":
                                payPresenter.payAli(payEntity.getPay_order_no());
                                break;
                            case "2":
                                payPresenter.payWeChat(payEntity.getPay_order_no());
                                break;
                            case "3":
                                if (type==0){
                                    if (walletExtractConfirmDialogFragment == null) {
                                        walletExtractConfirmDialogFragment = new WalletExtractConfirmDialogFragment()
                                                .setWalletExtract(haiMoney,"buy")
                                                .setWalletTransfer("","确认买单","支付金额",true);
                                        walletExtractConfirmDialogFragment.show(getSupportFragmentManager(), TAG);
                                    } else {
                                        walletExtractConfirmDialogFragment.setWalletExtract(haiMoney,"buy");
                                        walletExtractConfirmDialogFragment.setWalletTransfer("","确认买单","支付金额",true);
                                        walletExtractConfirmDialogFragment.onStart();
                                    }
                                    walletExtractConfirmDialogFragment.setOnDialogListener(VerificationOrderPayOnlineActivity.this);
                                }else {
                                    showInputPasswordDialog();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(BaseData<String> data) {
                        if (data.getErrcode()==999){    //不能用海贝支付
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.sharemall_good_remind)
                                    .setMessage(data.getErrmsg())
                                    .setPositiveButton(R.string.sharemall_confirm_ok, null)
                                    .show();
                        }else if (data.getErrcode()==111){  //不产生分红
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.sharemall_good_remind)
                                    .setMessage(data.getErrmsg())
                                    .setPositiveButton(R.string.sharemall_confirm_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (type1){
                                                case "1":
                                                    payPresenter.payAli(payEntity.getPay_order_no());
                                                    break;
                                                case "2":
                                                    payPresenter.payWeChat(payEntity.getPay_order_no());
                                                    break;
                                                case "3":
                                                    if (type==0){
                                                        if (walletExtractConfirmDialogFragment == null) {
                                                            walletExtractConfirmDialogFragment = new WalletExtractConfirmDialogFragment()
                                                                    .setWalletExtract(haiMoney,"buy")
                                                                    .setWalletTransfer("","确认买单","支付金额",true);
                                                            walletExtractConfirmDialogFragment.show(getSupportFragmentManager(), TAG);
                                                        } else {
                                                            walletExtractConfirmDialogFragment.setWalletExtract(haiMoney,"buy");
                                                            walletExtractConfirmDialogFragment.setWalletTransfer("","确认买单","支付金额",true);
                                                            walletExtractConfirmDialogFragment.onStart();
                                                        }
                                                        walletExtractConfirmDialogFragment.setOnDialogListener(VerificationOrderPayOnlineActivity.this);
                                                    }else {
                                                        showInputPasswordDialog();
                                                    }
                                                    break;
                                            }
                                        }
                                    })
                                    .setNegativeButton(R.string.sharemall_cancel, null)
                                    .show();
                        }
                    }
                });
    }


    //提醒用户输入支付密码的弹出框
    private void showInputPasswordDialog() {
        if (UserInfoCache.get(ShareMallUserInfoEntity.class).getIs_set_paypassword() == 1) {
            createPayDialog();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.sharemall_good_remind)
                    .setMessage(R.string.sharemall_not_set_balance_pay_password)
                    .setPositiveButton(R.string.sharemall_to_set_up, (DialogInterface dialog, int which) ->
//                          JumpUtil.overlay(getContext(), ForgetPwdActivity.class,"forget","pay"))
                            JumpUtil.overlay(getContext(), SetPayPasswordActivity.class))
                    .setNegativeButton(R.string.sharemall_cancel, null)
                    .show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showWXPayResult(WXPayResultEvent wxPayResultEvent) {

        //修复部分机型无法直接跳转
        new Handler().postDelayed(() -> {
            hideProgress();
            toResultAct(wxPayResultEvent.errorCode == 0);
        }, 250);

    }

    private void createPayDialog() {
        PayDialogFragment payDialogFragment = new PayDialogFragment();
        payDialogFragment.setPasswordCallback((String password) -> doCheckPayPWD(password, payDialogFragment));
        payDialogFragment.show(getSupportFragmentManager(), "payFragment");
    }

    private void toResultAct(boolean success) {
        Bundle bundle = new Bundle();
        if (type==0){   //门店买单
            bundle.putBoolean("success",success);
            bundle.putInt("type",0);
            bundle.putSerializable(ORDER_ENTITY,payEntity);
            JumpUtil.overlay(getContext(), VerificationMinOrderPaySuccessActivity.class, bundle);
            MApplication.getInstance().appManager.finishActivity(LocalLifeShopPayActivity.class);
        }else if (type==1){
            if (success) {
                if (!TextUtils.isEmpty(orderId)) {
                    EventBus.getDefault().post(new VerificationOrderUpdateEvent(orderId, OrderParam.VERIFICATION_WAIT_USE));
                }
            }
            bundle.putBoolean("success",success);
            bundle.putInt("type",1);
            bundle.putSerializable(ORDER_ENTITY,payEntity);
            JumpUtil.overlay(getContext(), VerificationMinOrderPaySuccessActivity.class, bundle);
        }
        finish();
    }

    @Override
    public void showAlipayResult(PayResult payResult) {
        toResultAct(payResult != null && TextUtils.equals(payResult.getResultStatus(), "9000"));
    }

    @Override
    public void onDialogClick(String wallet) {
        showInputPasswordDialog();
    }

    class TimerObserver implements Observer<Long> {

        private TimerCallback callback;

        private TimerObserver(TimerCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onSubscribe(Disposable d) {
            timerDisposable = d;
        }

        @Override
        public void onNext(Long aLong) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            callback.onComplete();
        }
    }

    interface TimerCallback {
        void onComplete();
    }

    private void doGetOrderPayInfo() {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .getOrderPayInfo(orderId)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        payEntity = data.getData();
                        showData(data.getData());
                    }
                });

    }

    //验证支付密码
    private void doCheckPayPWD(final String password, final PayDialogFragment payDialog) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .checkPayPWD(MD5.GetMD5Code(password))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        payDialog.dismiss();
                        doPayHai(MD5.GetMD5Code(password));
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        payDialog.clearPasswordText();
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }
                });
    }

    private void doPayHai(String password) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayHai(payEntity.getPay_order_no(), password,"SCORE_PAY",type==0?"12":"")
                .compose(RxSchedulers.compose())
                .compose(((RxAppCompatActivity) this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<String>>(this) {

                    @Override
                    public void onSuccess(BaseData<String> data) {
                        if (dataExist(data)){
                            if (TextUtils.equals(data.getData(),"支付成功")){
                                toResultAct(true);
                            }else {
                                toResultAct(false);
                            }
                        }else {
                            toResultAct(false);
                        }

                    }

                    @Override
                    public void onFail(BaseData data) {
                        if (data.getErrcode() == ORDER_RESET && resetCount < 3) {
                            Observable
                                    .timer(1, TimeUnit.SECONDS)
                                    .subscribe(new TimerObserver(() -> {
                                        resetCount++;
                                        doPayHai(password);
                                    }));
                            return;
                        }
                        resetCount = 0;
                        showError(data.getErrmsg());
                    }

                });
    }

    private void doGetMoney(String pay_amount) {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getMoney(pay_amount)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        haiMoney = (String) data.getData();
                        money = "="+(String)data.getData()+"海贝";
                        if (mBinding.cbPayDigital.isChecked()){
                            mBinding.tvDigitalPrice.setText(money);
                        }
                    }

                    @Override
                    public void onComplete() {
                        doGetWalletMessage();
                    }
                });
    }

    private void doGetWalletMessage() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<WalletInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<WalletInfoEntity> data) {
                        if (dataExist(data)){
                            walletInfoEntity = data.getData();
                            mBinding.cbPayDigital.setText(Html.fromHtml(getString(R.string.sharemall_title_digital_price) + "<font color=\"#999999\">" + "（余额：" + walletInfoEntity.getHand_balance() + "≈"+walletInfoEntity.getMoney()+"元）" + "</font>"));
                        }
                    }
                });
    }
}
