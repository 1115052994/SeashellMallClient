package com.liemi.seashellmallclient.presenter;

import android.app.Activity;
import android.content.Context;
import com.alipay.sdk.app.PayTask;
import com.liemi.seashellmallclient.contract.PayContract;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.order.PayResult;
import com.liemi.seashellmallclient.data.entity.order.WXPayData;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.ToastUtils;
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

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/3
 * 修改备注：
 */
public class PayPresenterImpl implements PayContract.Presenter {

    private static final int ORDER_RESET = 30006;

    private PayContract.View view;

    private int resetCount = 0;

    private Disposable timerDisposable;

    public PayPresenterImpl(PayContract.View view) {
        this.view = view;
    }

    @Override
    public void payWeChat(String pay_order_no) {
        view.showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayWechat(pay_order_no, "WeChat")
                .compose(RxSchedulers.compose())
                .compose(((RxAppCompatActivity) view).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<WXPayData>>() {

                    @Override
                    public void onSuccess(BaseData<WXPayData> data) {
                        doWxPay(data.getData());
                    }

                    @Override
                    public void onFail(BaseData<WXPayData> data) {
                        if (data.getErrcode() == ORDER_RESET && resetCount < 3) {
                            Observable
                                    .timer(1, TimeUnit.SECONDS)
                                    .subscribe(new TimerObserver(() -> {
                                        resetCount++;
                                        payWeChat(pay_order_no);
                                    }));
                            return;
                        }
                        resetCount = 0;
                        view.showError(data.getErrmsg());
                    }

                });
    }


    //微信支付调起
    private void doWxPay(WXPayData entity) {
        if (entity != null) {
            PayReq req = new PayReq();
            req.appId = entity.getAppid();
            req.partnerId = entity.getPartnerid();
            req.prepayId = entity.getPrepayid();
            req.nonceStr = entity.getNoncestr();
            req.timeStamp = entity.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = entity.getSign();
            req.extData = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用将应用注册到微信
            IWXAPI api = WXAPIFactory.createWXAPI((Context) view, req.appId);
            api.sendReq(req);
        } else {
            view.hideProgress();
        }
    }

    @Override
    public void payAli(String pay_order_no) {
        view.showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderPayAli(pay_order_no, "ALi")
                .compose(RxSchedulers.compose())
                .compose(((RxAppCompatActivity) view).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<String>>() {


                    @Override
                    public void onSuccess(BaseData<String> data) {
                        doAliPay(data.getData());
                    }

                    @Override
                    public void onFail(BaseData<String> data) {
                        if (data.getErrcode() == ORDER_RESET && resetCount < 3) {
                            Observable
                                    .timer(1, TimeUnit.SECONDS)
                                    .subscribe(new TimerObserver(() -> {
                                        resetCount++;
                                        doAliPay(pay_order_no);
                                    }));
                            return;
                        }
                        resetCount = 0;
                        view.showError(data.getErrmsg());
                    }
                });
    }

    private void doAliPay(final String key) {
        Observable.create(new ObservableOnSubscribe<PayResult>() {
            public void subscribe(ObservableEmitter<PayResult> oe) throws Exception {
                try {
                    PayTask alipay = new PayTask((Activity) view);
                    Map<String, String> result = alipay.payV2(key, true);
                    oe.onNext(new PayResult(result));
                    oe.onComplete();
                } catch (Exception e) {
                    oe.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((RxAppCompatActivity) view).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<PayResult>() {
                    public void onSubscribe(Disposable d) {

                    }

                    public void onNext(PayResult payResult) {
                        view.showAlipayResult(payResult);
                    }

                    public void onError(Throwable e) {
                        view.showAlipayResult(null);
                    }

                    public void onComplete() {
                        view.hideProgress();
                    }
                });

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        if (timerDisposable != null) {
            timerDisposable.dispose();
            timerDisposable = null;
        }
    }

    @Override
    public void onError(String message) {
        ToastUtils.showShort(message);
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

}
