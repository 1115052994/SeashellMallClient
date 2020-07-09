package com.liemi.seashellmallclient.utils;

import android.os.CountDownTimer;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.data.Constant;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/30
 * 修改备注：
 */
public class CountDownTimerUtils extends CountDownTimer {

    private TextView mTextView;

    public CountDownTimerUtils(TextView textView) {
        this(textView, Constant.COUNT_DOWN_TIME_DEFAULT, 1000L);
    }

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    /**
     * 倒计时期间会调用
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setEnabled(false); //设置不可点击
        mTextView.setText((millisUntilFinished / 1000 + mTextView.getContext().getString(R.string.sharemall_reacquire_verification_code))); //设置倒计时时间
    }

    /**
     * 倒计时完成后调用
     */
    @Override
    public void onFinish() {
        mTextView.setText(R.string.sharemall_get_verification_code);
        mTextView.setEnabled(true);//重新获得点击
    }

}