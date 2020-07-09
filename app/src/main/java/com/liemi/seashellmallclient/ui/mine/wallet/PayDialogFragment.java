package com.liemi.seashellmallclient.ui.mine.wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.widget.PayDialog;
import com.liemi.seashellmallclient.widget.PwdKeyboardView;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * Created by Administrator on 2018/3/25.
 */

@SuppressLint("ValidFragment")
public class PayDialogFragment extends DialogFragment {

    private static final String TAG = "PayDialogFragment";
    private String title;
    private EditText editText;

    @SuppressLint("ValidFragment")
    public PayDialogFragment(String title) {
        this.title = title;
    }

    public PayDialogFragment() {
    }

    public interface PasswordCallback {
        void callback(String password);
    }

    public PayDialog.PasswordCallback mPasswordCallback;

    /**
     * 设置回调
     *
     * @param passwordCallback
     */
    public void setPasswordCallback(PayDialog.PasswordCallback passwordCallback) {
        this.mPasswordCallback = passwordCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        //去掉dialog的标题，需要在setContentView()之前
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.layout_pay_dialog, null);
        ImageView exitImgView = view.findViewById(R.id.iv_exit);
        exitImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialogFragment.this.dismiss();
            }
        });
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(TextUtils.isEmpty(title)?"请输入支付密码":tvTitle.getText().toString().trim());
        editText = view.findViewById(R.id.et_pwd);
        TextView tvEnter = view.findViewById(R.id.tv_enter);
        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString().trim())){
                    if (mPasswordCallback != null)
                        mPasswordCallback.callback(editText.getText().toString().trim());
                }else {
                    ToastUtils.showShort("请输入交易密码");
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                return true;
            }
        });
        PwdKeyboardView keyboardView = view.findViewById(R.id.key_board);
        keyboardView.setOnKeyListener(new PwdKeyboardView.OnKeyListener() {
            @Override
            public void onInput(String text) {
                Log.d(TAG, "onInput: text = " + text);
                editText.append(text);
                String content = editText.getText().toString();
                Log.d(TAG, "onInput: content = " + content);
            }

            @Override
            public void onDelete() {
                Log.d(TAG, "onDelete: ");
                String content = editText.getText().toString();
                if (content.length() > 0) {
                    editText.setText(content.substring(0, content.length() - 1));
                    editText.setSelection(editText.getText().toString().length());
                }
            }
        });
        return view;
    }

    /**
     * 重制
     */
    public void clearPasswordText() {
        editText.setText("");
        requestFocus();
    }

    private void requestFocus() {
        editText.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = R.style.DialogFragmentAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        //去掉dialog默认的padding
//        window.getDecorView().setPadding(0, 0, 0, 0);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

}
