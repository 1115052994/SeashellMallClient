package com.liemi.seashellmallclient.widget.filter;

import android.content.Context;
import android.widget.PopupWindow;
import com.ccj.poptabview.listener.OnFilterSetListener;
import com.ccj.poptabview.loader.PopEntityLoader;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/30
 * 修改备注：
 */
public class GoodsPopLoaderImp implements PopEntityLoader {

    @Override
    public PopupWindow getPopEntity(Context context, List data, OnFilterSetListener filterSetListener, int filterType, int singleOrMultiply) {
        return new GoodsFilterWindow(context, data, filterSetListener,filterType,singleOrMultiply);
    }
}
