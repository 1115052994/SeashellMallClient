package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import skin.support.widget.SkinCompatTextView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/23 17:07
 * 修改备注：
 */
public class StrikeTextView extends SkinCompatTextView {

    public StrikeTextView(Context context) {
        super(context);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
