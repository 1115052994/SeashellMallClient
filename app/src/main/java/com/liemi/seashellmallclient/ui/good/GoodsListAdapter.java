package com.liemi.seashellmallclient.ui.good;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallItemGoodsOtherBinding;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.widget.XERecyclerView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/4
 * 修改备注：
 */
public class GoodsListAdapter extends BaseRViewAdapter<GoodsListEntity, BaseViewHolder> {
    private boolean isVIP;

    public GoodsListAdapter(Context context) {
        this(context, null);
    }

    public GoodsListAdapter(Context context, XERecyclerView recyclerView) {
        this(context, recyclerView, com.netmi.baselibrary.R.layout.baselib_include_no_data_view2,
                new EmptyLayoutEntity(R.mipmap.sharemall_ic_goods_empty, context.getString(R.string.sharemall_no_goods)));
    }

    public GoodsListAdapter(Context context, XERecyclerView recyclerView, @LayoutRes int layoutId, EmptyLayoutEntity entity) {
        super(context, recyclerView, layoutId, entity);
        isVIP = ShareMallUserInfoCache.get().isVip();
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.sharemall_item_goods_other;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<GoodsListEntity>(binding) {

            @Override
            public void bindData(GoodsListEntity item) {

                getBinding().setIsVIP(isVIP);
                super.bindData(item);
            }


            @Override
            public SharemallItemGoodsOtherBinding getBinding() {
                return (SharemallItemGoodsOtherBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (view.getId() == R.id.tv_extend) {
                    GoodDetailPageActivity.start(context, getItem(position).getItem_id(), new FastBundle().putInt(GoodsParam.CURRENT_TAB, 1));
                } else {
                    GoodDetailPageActivity.start(context, getItem(position).getItem_id(), null);
                }
            }
        };
    }
}
