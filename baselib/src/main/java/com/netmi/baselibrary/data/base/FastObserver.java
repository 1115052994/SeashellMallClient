package com.netmi.baselibrary.data.base;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseView;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * 类描述：对服务器返回错误码进行统一处理
 * 创建人：Sherlock
 * 创建时间：2019/1/28
 * 修改备注：
 */
public abstract class FastObserver<T extends BaseData> extends BaseObserver<T> {

    /**
     * token empty
     */
    private static final int TOKEN_EMPTY = 10000;

    /**
     * token out
     */
    private static final int TOKEN_OUT = 10001;
    /**
     *
     */
    private static final int TOKEN_END = 10002;
    /**
     * 异地登陆
     */
    private static final int TOKEN_REFRESH = 10004;

    private BaseView context;

    protected FastObserver() {
    }

    protected FastObserver(BaseView context) {
        this.context = context;
    }

    @Override
    public void onNext(T t) {
        if (t.getErrcode() == TOKEN_OUT || t.getErrcode() == TOKEN_EMPTY) {
            ToastUtils.showShort(R.string.the_certificate_expires_please_relogin);
            MApplication.getInstance().gotoLogin();
        } else if (t.getErrcode() == TOKEN_REFRESH) {
            ToastUtils.showShort(R.string.your_account_is_logged_in_elsewhere_please_log_in_again);
            MApplication.getInstance().gotoLogin();
        } else if (t.getErrcode() == Constant.SUCCESS_CODE) {
            onSuccess(t);
        } else {
            onFail(t);
        }
    }

    public void onFail(T data) {
        ToastUtils.showShort(data.getErrmsg());
    }

    public abstract void onSuccess(T data);

    protected boolean dataExist(T data) {
        if (data.getData() == null) {
            ToastUtils.showShort(R.string.baselib_not_data);
            return false;
        }
        return true;
    }

    @Override
    public void onComplete() {
        if (context != null) {
            context.hideProgress();
        }
    }

    @Override
    protected void onError(ApiException ex) {
        if (context != null) {
            context.showError(ex.getMessage());
        }
        onComplete();
    }
}
