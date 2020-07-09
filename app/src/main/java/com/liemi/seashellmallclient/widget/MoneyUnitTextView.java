package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import skin.support.widget.SkinCompatTextView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/23 13:59
 * 修改备注：
 */
public class MoneyUnitTextView extends SkinCompatTextView {

    public MoneyUnitTextView(Context context) {
        super(context);
    }

    public MoneyUnitTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text) && text.length() > 1) {
            int moneyIndex = text.toString().indexOf("¥");
            int pointIndex = text.toString().lastIndexOf(".");
            if (moneyIndex > -1) {
                SpannableString spannableString = new SpannableString(text);
                spannableString.setSpan(new RelativeSizeSpan(0.8f), moneyIndex, moneyIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (pointIndex > -1) {
                    spannableString.setSpan(new RelativeSizeSpan(0.7f), pointIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                super.setText(spannableString, BufferType.SPANNABLE);
                return;
            }
        }

        super.setText(text, type);
    }
}
