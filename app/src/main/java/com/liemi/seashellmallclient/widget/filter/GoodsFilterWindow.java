package com.liemi.seashellmallclient.widget.filter;

import android.content.Context;
import com.ccj.poptabview.base.SuperAdapter;
import com.ccj.poptabview.filter.single.SingleFilterWindow;
import com.ccj.poptabview.listener.OnFilterSetListener;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/30
 * 修改备注：
 */
public class GoodsFilterWindow extends SingleFilterWindow {

    public GoodsFilterWindow(Context context, List data, OnFilterSetListener listener, int filterType, int singleOrMultiply) {
        super(context, data, listener, filterType, singleOrMultiply);
    }

    @Override
    public SuperAdapter setAdapter() {
        return new GoodsFilterAdapter(getData(), this, getSingleOrMultiply());
    }
}
