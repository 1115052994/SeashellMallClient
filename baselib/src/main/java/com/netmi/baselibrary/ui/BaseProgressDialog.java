package com.netmi.baselibrary.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.LoadingDialog;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/8
 * 修改备注：
 */
public abstract class BaseProgressDialog extends Dialog implements BaseView {

    public BaseProgressDialog(@NonNull Context context) {
        super(context);
    }

    public BaseProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected Activity activity;

    public BaseProgressDialog setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    private LoadingDialog loadingDialog;

    public void showProgress(String message) {
        if (activity == null) return;

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
            loadingDialog.setHalfBlackBg(true);
        }
        loadingDialog.showProgress(message);
    }

    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.hideProgress();
        }
    }

    @Override
    public void showError(String message) {
        ToastUtils.showShort(message);
        hideProgress();
    }
}
