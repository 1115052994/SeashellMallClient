package com.liemi.seashellmallclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.DensityUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/19 17:05
 * 修改备注：
 */
public class SignRoleDialog extends Dialog {

    public SignRoleDialog(Context context) {
        super(context, R.style.sharemall_transparentDialog);
        initUI();
    }

    public SignRoleDialog(Context context, int themeResId) {
        super(context, themeResId);
        initUI();
    }

    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvTitle;
    private ImageView ivCancel;
    private String content;

    /**
     * 对话框布局初始化
     */
    private void initUI() {
        doGetContent();
        setContentView(R.layout.sharemall_dialog_sign_rule);
        tvContent = findViewById(R.id.tv_content);
        tvCancel = findViewById(R.id.tv_cancel);
        ivCancel = findViewById(R.id.iv_cancel);
        tvTitle = findViewById(R.id.tv_title);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvCancel.setOnClickListener((View view) -> dismiss());
        ivCancel.setOnClickListener((View view) -> dismiss());
        setDialogPosition();
    }

    private void setDialogPosition() {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = -DensityUtils.dp2px(80);
            dialogWindow.setAttributes(lp);
        }
    }

    public SignRoleDialog setContentText(String content) {
        tvContent.setText(content);
        return this;
    }

    public SignRoleDialog setCancelText(String content) {
        tvCancel.setText(content);
        return this;
    }

    private void doGetContent() {
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(LoginParam.PROTOCOL_TYPE_SIGN_IN)
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<AgreementEntity>>() {

                    @Override
                    public void onSuccess(BaseData<AgreementEntity> data) {
                        if (dataExist(data)) {
                            content = data.getData().getContent();
                            CharSequence charSequence = Html.fromHtml(content);
                            tvContent.setText(charSequence);
                            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
                            tvTitle.setText(data.getData().getTitle());
                        }
                    }
                });
    }
}
