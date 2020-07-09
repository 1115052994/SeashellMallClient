package com.liemi.seashellmallclient.widget.filter;

import com.ccj.poptabview.base.BaseFilterTabBean;
import com.ccj.poptabview.base.SuperListener;
import com.ccj.poptabview.filter.single.SingleFilterAdapter;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/30
 * 修改备注：
 */
public class GoodsFilterAdapter extends SingleFilterAdapter {


    public GoodsFilterAdapter(List<BaseFilterTabBean> beanList, SuperListener listener, int single2mutiple) {
        super(beanList, listener, single2mutiple);
    }

    @Override
    protected void onItemClickEvent(int pos) {
        clearChecked();
        super.onItemClickEvent(pos);
    }
}
