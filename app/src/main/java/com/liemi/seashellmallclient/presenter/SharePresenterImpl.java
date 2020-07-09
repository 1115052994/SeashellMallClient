package com.liemi.seashellmallclient.presenter;

import com.liemi.seashellmallclient.contract.ShareContract;
import com.liemi.seashellmallclient.data.api.ShareApi;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import io.reactivex.Observable;

/**
 * Created by Bingo on 2019/1/28.
 */

public class SharePresenterImpl implements ShareContract.Presenter {

    private ShareContract.View beforeShareView;

    public SharePresenterImpl(ShareContract.View view) {
        this.beforeShareView = view;
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
        beforeShareView = null;
    }

    @Override
    public void onError(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void readyShare() {
        beforeShareView.showProgress("");
        Observable<BaseData> observable = RetrofitApiFactory.createApi(ShareApi.class)
                .beforeShare(null)
                .compose(RxSchedulers.<BaseData>compose());
        if (beforeShareView instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) beforeShareView).<BaseData>bindUntilEvent(ActivityEvent.DESTROY));
        } else {
            observable = observable.compose(((RxFragment) beforeShareView).<BaseData>bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new BaseObserver<BaseData>() {
            @Override
            protected void onError(ApiException ex) {
                beforeShareView.hideProgress();
                beforeShareView.readyFailure(ex.getMessage());
            }

            @Override
            public void onNext(BaseData data) {
                if (data.getErrcode() == Constant.SUCCESS_CODE) {
                    beforeShareView.readySuccess();
                } else {
                    beforeShareView.readyFailure(data.getErrmsg());
                }
            }

            @Override
            public void onComplete() {
                beforeShareView.hideProgress();
            }
        });
    }
}
