package com.liemi.seashellmallclient.ui.mine.wallet;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.good.CommendGoodEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPIncomeListEntity;
import com.liemi.seashellmallclient.databinding.ActivityWalletRecordBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemOrderBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity;
import com.liemi.seashellmallclient.ui.mine.order.OrderGoodsAdapter;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.widget.decoration.ItemDivider;
import com.liemi.seashellmallclient.widget.decoration.QuickItemDecoration;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.DensityUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_DETAILS_ID;

public class WalletRecordActivity extends BaseXRecyclerActivity<ActivityWalletRecordBinding, VIPIncomeListEntity> {

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_record;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_details_record_wallet));
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        initAdapter();
    }

    private void initAdapter() {
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<VIPIncomeListEntity, BaseViewHolder>(getContext(), xRecyclerView) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_wallet_record;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<VIPIncomeListEntity>(binding) {
                    @Override
                    public void bindData(VIPIncomeListEntity item) {
                        super.bindData(item);
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(VIPApi.class)
                .getVIPIncomeList(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null,null, 0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<VIPIncomeListEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<PageEntity<VIPIncomeListEntity>> data) {
                        showData(data.getData());
                    }

                });
    }
}
