package com.liemi.seashellmallclient.ui.store;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 15:36
 * 修改备注：
 */
public class StoreGoodsFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerviewBinding, GoodsListEntity> {

    public static final String TAG = StoreGoodsFragment.class.getName();

    private String storeId;

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview;
    }

    public static StoreGoodsFragment newInstance(String storeId) {
        StoreGoodsFragment f = new StoreGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GoodsParam.STORE_ID, storeId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected void initUI() {
        storeId = getArguments().getString(GoodsParam.STORE_ID);
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<GoodsListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_store_goods;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        GoodDetailPageActivity.start(getContext(), getItem(position).getItem_id(), null);
                    }
                };
            }
        });

        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, null, null,
                        null, null, null, null, storeId,
                        null, null, null, null, null)
                .compose(RxSchedulers.compose())
                .compose(((RxFragment) this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageEntity<GoodsListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

}
