package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.liemi.seashellmallclient.R;
import skin.support.widget.SkinCompatTextView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/16
 * 修改备注：
 */
public class GoodsTitleNormalTextView extends SkinCompatTextView {

    private String[] titles = new String[2];

    public GoodsTitleNormalTextView(Context context) {
        this(context, null);
    }

    public GoodsTitleNormalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public GoodsTitleNormalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        titles[0] = getResources().getString(R.string.sharemall_title_cross_border_purchase);
        titles[1] = getResources().getString(R.string.sharemall_title_group_buying);
    }

    private String getNormalTitle(String content) {
        if (TextUtils.isEmpty(content)) return "";

        for (String title : titles) {
            if (content.contains(title)) {
                content = content.replaceAll(title, "");
            }
        }
        return content;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        //标题包含跨境购，拼团字样，进行清除
        super.setText(getNormalTitle(text.toString()), type);
    }
}
