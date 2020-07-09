package com.liemi.seashellmallclient.ui.shopcart;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.good.CommendGoodEntity;
import com.liemi.seashellmallclient.databinding.SharemallItemMallGoodBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.widget.XERecyclerView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/9
 * 修改备注：
 */
public class CommendGoodAdapter extends BaseRViewAdapter<CommendGoodEntity, BaseViewHolder> {

    public CommendGoodAdapter(Context context) {
        super(context);
    }

    public CommendGoodAdapter(Context context, XERecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.sharemall_item_recommend_good;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<CommendGoodEntity>(binding) {

            @Override
            public void bindData(CommendGoodEntity item) {
                super.bindData(item);
            }

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
