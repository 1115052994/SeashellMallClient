package com.liemi.seashellmallclient.ui.store;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.entity.category.GoodsTwoCateEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityStoreCategoryBinding;
import com.liemi.seashellmallclient.ui.category.CategoryGoodsActivity;
import com.liemi.seashellmallclient.ui.home.SearchActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class StoreCategoryActivity extends BaseXRecyclerActivity<ActivityStoreCategoryBinding, GoodsTwoCateEntity> {
    private String storeId;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_category;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("商品分类");
        xRecyclerView = mBinding.xrvData;

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_store_category;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        doResult(getItem(position));
                    }
                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.iv_setting) {
            JumpUtil.overlay(this, SearchActivity.class, GoodsParam.STORE_ID, storeId);
        } else if (i == R.id.tv_all_good) {     //全部商品
            doResult(null);
        }
    }

    private void doResult(GoodsTwoCateEntity cateEntity) {
        if (AppManager.getInstance().existActivity(StoreSearchActivity.class)) {
            Intent intent = new Intent();
            intent.putExtra(GoodsParam.MC_ID, cateEntity == null ? "" : cateEntity.getMcid());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Bundle bundle = new Bundle();
            if (cateEntity == null) {
                bundle.putString(GoodsParam.MC_NAME, mBinding.tvAllGood.getText().toString());
            } else {
                bundle.putString(GoodsParam.MC_ID, cateEntity.getMcid());
                bundle.putString(GoodsParam.MC_NAME, cateEntity.getName());
            }
            if (!TextUtils.isEmpty(storeId)) {
                bundle.putString(GoodsParam.STORE_ID, storeId);
            }
            JumpUtil.overlay(getActivity(), CategoryGoodsActivity.class, bundle);
        }
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra(GoodsParam.STORE_ID);
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        doListCategory();
    }

    private void doListCategory() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getSecondCategory(storeId, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<GoodsTwoCateEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<GoodsTwoCateEntity>> data) {
                        showData(data.getData());
                    }
                });
    }
}
