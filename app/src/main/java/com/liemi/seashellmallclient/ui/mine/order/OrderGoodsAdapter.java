package com.liemi.seashellmallclient.ui.mine.order;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.databinding.SharemallItemOrderSkusGoodsBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/8
 * 修改备注：
 */
public class OrderGoodsAdapter extends BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> {

    private GoodsClickListener goodsClickListener;

    //showAfterSale是否显示售后按钮，  paddingTop 每个item间隔16dp
    private boolean showAfterSale, paddingTop;

    public OrderGoodsAdapter(Context context) {
        super(context);
    }


    public OrderGoodsAdapter(Context context, GoodsClickListener goodsClickListener) {
        super(context);
        this.goodsClickListener = goodsClickListener;
    }


    public OrderGoodsAdapter(Context context, boolean paddingTop, boolean showAfterSale, GoodsClickListener goodsClickListener) {
        super(context);
        this.paddingTop = paddingTop;
        this.showAfterSale = showAfterSale;
        this.goodsClickListener = goodsClickListener;
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.sharemall_item_order_skus_goods;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<OrderSkusEntity>(binding) {

            @Override
            public void bindData(OrderSkusEntity item) {
                getBinding().setAfterSale(showAfterSale);
                super.bindData(item);

                //用于paddingTop
                if (paddingTop) {
                    getBinding().setPosition(0);
                }
            }

            @Override
            public SharemallItemOrderSkusGoodsBinding getBinding() {
                return (SharemallItemOrderSkusGoodsBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (goodsClickListener != null) {
                    goodsClickListener.doClick(view, getItem(position));
                }
            }
        };
    }

    public interface GoodsClickListener {
        void doClick(View view, OrderSkusEntity item);
    }
}
