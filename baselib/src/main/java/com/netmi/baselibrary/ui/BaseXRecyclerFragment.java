package com.netmi.baselibrary.ui;

import android.databinding.ViewDataBinding;

import com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;

import java.util.List;

/**
 * 类描述：Activity 基类
 * 创建人：Simple
 * 创建时间：2017/9/4 17:47
 * 修改备注：
 */
public abstract class BaseXRecyclerFragment<T extends ViewDataBinding, D extends BaseEntity> extends BaseFragment<T> implements LoadingListener {

    /**
     * 数据类型
     */
    protected D data;

    /**
     * 页数
     */
    protected int startPage = 0;

    /**
     * 总条数
     */
    protected int totalCount;

    /**
     * 列表加载数据方式
     */
    protected int LOADING_TYPE = Constant.PULL_REFRESH;


    protected boolean loadMoreEnabled = true;

    protected BaseRViewAdapter<D, BaseViewHolder> adapter;

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListData();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListData();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();

        if (xRecyclerView == null) {
            return;
        }

        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }

        if (adapter != null && adapter.getItemSize() <= 0) {
            xRecyclerView.setNoMore(false);
        } else if (startPage >= totalCount && loadMoreEnabled) {
            xRecyclerView.setNoMore(true);
        }
    }

    public void showData(PageEntity<D> pageEntity) {
        if (adapter == null) {
            showError("请先初始化适配器");
            return;
        }

        if (pageEntity == null) return;

        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(loadMoreEnabled);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemSize(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemSize();
    }


    public void showData(List<D> list, int totalPages) {
        if (adapter == null) {
            showError("请先初始化适配器");
            return;
        }

        totalCount = totalPages;
        startPage = adapter.getItemSize();

        if (list == null) return;

        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (!list.isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(loadMoreEnabled);
            }
            adapter.setData(list);
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (!list.isEmpty()) {
                adapter.insert(adapter.getItemSize(), list);
            }
        }
        startPage = adapter.getItemSize();
    }

    protected abstract void doListData();

}
