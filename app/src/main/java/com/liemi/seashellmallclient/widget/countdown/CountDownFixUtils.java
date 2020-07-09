package com.liemi.seashellmallclient.widget.countdown;

import android.view.View;
import cn.iwgang.countdownview.CountdownView;

/**
 * 类描述：倒计时停止问题修复
 * 创建人：Simple
 * 创建时间：2019/4/15
 * 修改备注：
 */
public class CountDownFixUtils {

    public static CountDownFixUtils getInstance() {
        return CountDownFixUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final CountDownFixUtils instance = new CountDownFixUtils();
    }

    public void fixCountDownView(CountdownView countdownView, CountDownMillisecond millisecond, CountdownView.OnCountdownEndListener onCountdownEndListener) {

        countdownView.setOnCountdownIntervalListener(1000L, (CountdownView cv, long remainTime) -> {
            if (millisecond != null) {
                millisecond.setMillisecond(remainTime);
            }
        });

        View.OnAttachStateChangeListener stateChangeListener = new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (millisecond != null) {
                    ((CountdownView) v).start(millisecond.getMillisecond() - (System.currentTimeMillis() - stopTime));
                }
            }

            private long stopTime;

            @Override
            public void onViewDetachedFromWindow(View v) {
                stopTime = System.currentTimeMillis();
            }
        };

        countdownView.setOnCountdownEndListener((CountdownView cv) -> {
            countdownView.removeOnAttachStateChangeListener(stateChangeListener);
            if (onCountdownEndListener != null) {
                onCountdownEndListener.onEnd(cv);
            }
        });

        countdownView.addOnAttachStateChangeListener(stateChangeListener);
    }

}
