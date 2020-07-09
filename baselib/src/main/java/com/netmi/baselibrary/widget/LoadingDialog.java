package com.netmi.baselibrary.widget;

import android.content.Context;
import android.text.TextUtils;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/8
 * 修改备注：
 */
public class LoadingDialog {

    private Context context;

    private KProgressHUD progressDialog;

    private boolean halfBlackBg = false;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void setHalfBlackBg(boolean halfBlackBg) {
        this.halfBlackBg = halfBlackBg;
    }

    public boolean isShowing() {
        if (progressDialog == null) {
            createProgress();
        }
        return progressDialog.isShowing();
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            createProgress();
        }
        if (!TextUtils.isEmpty(message)) {
            progressDialog.setLabel(message.length() < 6 ? "   " + message + "   " : message);
        }
        progressDialog.show();
    }

    private void createProgress() {
        progressDialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (halfBlackBg) {
            progressDialog.setDimAmount(0.8f);
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void destroy() {
        hideProgress();
        progressDialog = null;
    }

}
