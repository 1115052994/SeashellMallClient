package com.liemi.seashellmallclient.ui.sharemoment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallDialogShareSaveImageFinishBinding;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.ToastUtils;

public class DialogShareSaveImageFinish extends Dialog implements View.OnClickListener {
    public DialogShareSaveImageFinish(@NonNull Context context) {
        super(context, R.style.sharemall_CustomDialog);
    }

    public DialogShareSaveImageFinish(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogShareSaveImageFinish(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharemallDialogShareSaveImageFinishBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_dialog_share_save_image_finish, null, false);
        setContentView(binding.getRoot());
        binding.setDoClick(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_share_cancel) {
            dismiss();
            return;
        }
        if (v.getId() == R.id.tv_share_now) {
            //跳转到微信
            if (AppUtils.isAvilible(getContext(), "com.tencent.mm")) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                getContext().startActivity(intent);
            } else {
                ToastUtils.showShort(getContext().getString(R.string.sharemall_login_no_wechat_client));
            }
        }
    }
}
