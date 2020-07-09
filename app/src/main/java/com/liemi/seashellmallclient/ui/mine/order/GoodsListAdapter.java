package com.liemi.seashellmallclient.ui.mine.order;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.databinding.SharemallItemMallGoodBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.widget.XERecyclerView;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/4
 * 修改备注：
 */
public class GoodsListAdapter extends BaseRViewAdapter<GoodsListEntity, BaseViewHolder> {

    public GoodsListAdapter(Context context) {
        super(context);
    }

    public GoodsListAdapter(Context context, XERecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.sharemall_item_mall_good;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<GoodsListEntity>(binding) {

            @Override
            public SharemallItemMallGoodBinding getBinding() {
                return (SharemallItemMallGoodBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                GoodDetailPageActivity.start(context, getItem(position).getItem_id(), null);
            }
        };
    }
}
