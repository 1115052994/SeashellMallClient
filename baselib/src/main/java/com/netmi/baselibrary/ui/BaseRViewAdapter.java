package com.netmi.baselibrary.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.widget.XERecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类描述：RecyclerView 适配基类
 * 创建人：Simple
 * 创建时间：2017/9/6 17:48
 * 修改备注：
 */
public abstract class BaseRViewAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected final static int EMPTY_VIEW_TYPE = 0x1235;

    //不显示空数据的布局
    private final static int EMPTY_VIEW_NULL = 0;

    public final static int EMPTY_DEFAULT_LAYOUT = R.layout.baselib_include_no_data_view;

    protected List<T> items = new ArrayList<>();
    protected Context context;
    private boolean showEmpty;

    //传入RecyclerView可以实现局部更新的动画
    private XERecyclerView fRecyclerView;

    //空数据时显示的默认布局
    private int emptyDefaultLayoutId;
    private EmptyLayoutEntity emptyLayoutEntity;

    protected BaseRViewAdapter(Context context) {
        this(context, null);
    }

    protected BaseRViewAdapter(Context context, int emptyDefaultLayoutId) {
        this(context, null, emptyDefaultLayoutId, null);
    }

    protected BaseRViewAdapter(Context context, XERecyclerView recyclerView) {
        this(context, recyclerView, EMPTY_VIEW_NULL);
    }

    protected BaseRViewAdapter(Context context, XERecyclerView recyclerView, int emptyDefaultLayoutId) {
        this(context, recyclerView, emptyDefaultLayoutId, null);
    }

    protected BaseRViewAdapter(Context context, XERecyclerView recyclerView, int emptyDefaultLayoutId, EmptyLayoutEntity emptyLayoutEntity) {
        this.context = context;
        this.fRecyclerView = recyclerView;
        this.emptyDefaultLayoutId = emptyDefaultLayoutId;
        this.emptyLayoutEntity = emptyLayoutEntity;
        initDataObserver();
    }

    private void initDataObserver() {

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (items.isEmpty() && emptyDefaultLayoutId != EMPTY_VIEW_NULL) {
                    showEmpty = true;
                    notifyItemRangeChanged(0, getItemCount());
                } else {
                    showEmpty = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return isShowEmpty() ? 1 : items.size();
    }

    //避免重写getItemCount造成获取不到正确的数据数量
    public int getItemSize(){
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isShowEmpty() ? EMPTY_VIEW_TYPE : super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW_TYPE) {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), emptyDefaultLayoutId, parent, false);
            return initEmptyViewHolder(binding);
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), layoutResId(viewType), parent, false);
            return holderInstance(binding);
        }
    }

    public BaseViewHolder initEmptyViewHolder(ViewDataBinding binding) {
        return new BaseViewHolder(binding) {
            @Override
            public void bindData(Object item) {
                super.bindData(item);
                bindEmptyData(emptyLayoutEntity);
            }

            private void bindEmptyData(EmptyLayoutEntity entity) {
                if (entity != null) {
                    TextView tvEmpty = getBinding().getRoot().findViewById(R.id.tv_empty);
                    if (tvEmpty != null) {
                        tvEmpty.setText(entity.getTip());
                    }
                    ImageView ivEmpty = getBinding().getRoot().findViewById(R.id.iv_empty);
                    if (ivEmpty != null && entity.getResourceId() != null) {
                        ivEmpty.setImageResource(entity.getResourceId());
                    }
                }
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                emptyViewClick();
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (recyclerView instanceof XERecyclerView) {
            //在MyXRecyclerView中已经完成处理
            return;
        }
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isShowEmpty() ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    /**
     * @param viewType According to position return item layout id
     */
    public abstract int layoutResId(int viewType);

    /**
     * ViewHolder实例化
     */
    public abstract VH holderInstance(ViewDataBinding binding);

    public T getItem(int position) {
        return position < items.size() ? items.get(position) : null;
    }

    public void emptyViewClick() {

    }

    /**
     * 如果存在XRecyclerView 调用正确的更新
     */
    private boolean xrIsEmpty() {
        if (fRecyclerView == null) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private XERecyclerView getFRecyclerView() {
        return fRecyclerView;
    }

    public void setData(List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void insert(T item) {

        items.add(getItemCount(), item);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, getItemCount());
        }
    }

    public void insert(List<T> items) {

        this.items.addAll(getItemCount(), items);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, getItemCount());
        }
    }

    public void insert(int position, T item) {

        items.add(position, item);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, position);
        }
    }

    public void insert(int position, List<T> items) {

        this.items.addAll(position, items);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, position);
        }
    }

    public void remove(int position) {
        if (position > -1 && position < getItemCount()) {
            T remove = items.remove(position);
            if (remove != null) {
                if (!xrIsEmpty()) {
                    getFRecyclerView().notifyItemRemoved(items, position);
                }
            }
        }
    }

    public void remove(T item) {

        for (int i = 0; i < items.size(); i++) {
            T temp = items.get(i);
            if (temp.equals(item)) {
                remove(i);
                i--;
            }
        }
    }

    public void replace(int position, T item) {
        Collections.replaceAll(items, getItem(position), item);
        if (!xrIsEmpty()) {
            notifyDataSetChanged();
        }
    }

    public void notifyPosition(int position) {
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemChanged(position);
        }
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    /**
     * 绑定ItemView事件
     */
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (holder.itemView != null && !(holder.itemView instanceof AdapterView)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.doClick(view);
                }
            });
        }
        holder.position = position;
        holder.bindData(getItem(position));
    }
}