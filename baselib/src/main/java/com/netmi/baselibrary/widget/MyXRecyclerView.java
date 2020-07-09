package com.netmi.baselibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.utils.DensityUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/1 12:17
 * 修改备注：
 */
public class MyXRecyclerView extends XERecyclerView {

    public MyXRecyclerView(Context context) {
        this(context, null);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View mFootView = getDefaultFootView();
        if (mFootView != null) {
            mFootView.setPadding(0, DensityUtils.dp2px(8), 0, DensityUtils.dp2px(8));
        }
        setFootViewText("正在加载...", " 一  我是有底线的  一 ");

        //全局设置默认显示的刷新和加载更多样式
        setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    private void setWhiteStyle() {
        //白色的头部文字
        ArrowRefreshHeader headerView = getDefaultRefreshHeaderView();
        if (headerView != null) {
            TextView statusTv = headerView.findViewById(R.id.refresh_status_textview);
            statusTv.setTextColor(Color.WHITE);
            headerView.setArrowImageView(R.mipmap.baselib_ic_pulltorefresh_arrow);
        }

        ViewGroup mFootView = getDefaultFootView();
        if (mFootView != null) {
            //白色的底部文字
            int count = mFootView.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = mFootView.getChildAt(i);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.WHITE);
                    break;
                }
            }
        }
    }

}
