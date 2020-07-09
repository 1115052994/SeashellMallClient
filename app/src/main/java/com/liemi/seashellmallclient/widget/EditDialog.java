package com.liemi.seashellmallclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallDialogEditBinding;
import com.netmi.baselibrary.utils.Strings;


public class EditDialog extends Dialog {
    private String mTitle;
    private String mMessage;
    private String mCancel;
    private String mConfirm;
    private ClickCancelListener mClickCancelListener;
    private ClickConfirmListener mClickConfirmListener;

    private SharemallDialogEditBinding mBinding;

    public EditDialog(@NonNull Context context) {
        super(context, R.style.showDialog);
    }

    public EditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //设置title
    public void setTitle(String title){
        if(mBinding != null){
            mBinding.tvTitle.setText(title);
        }
    }

    //设置message
    public void setMessage(String message){
        if(mBinding != null){
            mBinding.etCode.setText(message);
        }
    }

    public String getMessage(){
        String s = "";
        if(mBinding != null){
            s = mBinding.etCode.getText().toString();
        }
        return s;
    }

    //设置取消按钮的文字
    public void setCancel(String cancel){
        if(mBinding != null){
            mBinding.tvCancel.setText(cancel);
        }
    }

    //设置确认按钮文字
    public void setConfirm(String confirm){
        if(mBinding != null){
            mBinding.tvConfirm.setText(confirm);
        }
    }

    //设置点击取消接口
    public void setClickCancelListener(ClickCancelListener listener){
        this.mClickCancelListener = listener;
    }

    //设置点击确认接口
    public void setClickConfirmListener(ClickConfirmListener listener){
        this.mClickConfirmListener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.sharemall_dialog_edit,null,false);
        setContentView(mBinding.getRoot());
        if(!Strings.isEmpty(mTitle)){
            mBinding.tvTitle.setText(mTitle);
        }
        if(!Strings.isEmpty(mMessage)){
            mBinding.tvTitle.setText(mMessage);
        }
        if(!Strings.isEmpty(mCancel)){
            mBinding.tvCancel.setText(mCancel);
        }
        if(!Strings.isEmpty(mConfirm)){
            mBinding.tvConfirm.setText(mConfirm);
        }

        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mClickCancelListener != null){
                    mClickCancelListener.clickCancel();
                }
            }
        });

        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mClickConfirmListener != null){
                    mClickConfirmListener.clickConfirm();
                }
            }
        });

    }

    //点击取消的接口
    public interface ClickCancelListener{
        void clickCancel();
    }
    //点击确认的接口
    public interface ClickConfirmListener{
        void clickConfirm();
    }
}
