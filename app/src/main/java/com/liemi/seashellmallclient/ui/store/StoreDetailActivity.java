package com.liemi.seashellmallclient.ui.store;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityStoreDetailBinding;
import com.liemi.seashellmallclient.ui.home.HomeCategoryFragment;
import com.liemi.seashellmallclient.ui.home.SearchActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import jp.wasabeef.glide.transformations.BlurTransformation;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class StoreDetailActivity extends BaseActivity<ActivityStoreDetailBinding> {
    private StoreEntity storeEntity;

    private String storeId;

    public static void start(Context context, String shopId) {
        if (!TextUtils.isEmpty(shopId)) {
            JumpUtil.overlay(context, StoreDetailActivity.class, GoodsParam.STORE_ID, shopId);
        } else {
            ToastUtils.showShort(R.string.baselib_not_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_store_detail;
    }

    @Override
    public void setBarColor() {
        ImmersionBar.with(this).reset().statusBarView(R.id.view_top).init();
    }


    @Override
    protected void initUI() {
        storeId = getIntent().getStringExtra(GoodsParam.STORE_ID);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeCategoryFragment.newInstance(null, storeId));
        fragmentList.add(StoreGoodsFragment.newInstance(storeId));
        mBinding.tlGroup.setViewPager(mBinding.vpGroup,
                new String[]{getString(R.string.sharemall_store_home), getString(R.string.sharemall_store_all_goods)}, this, fragmentList);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(storeId)) {
            doGetStoreInfo();
        } else {
            ToastUtils.showShort(R.string.sharemall_store_not_param);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_follow) {
            if (mBinding.tvFollow.isSelected()) {
                doCollectionDel();
            } else {
                doCollection();
            }
        } else if (i == R.id.tv_search) {
            JumpUtil.overlay(getContext(), SearchActivity.class, GoodsParam.STORE_ID, storeId);
        } else if (i == R.id.tv_category) {
            JumpUtil.overlay(this, StoreCategoryActivity.class, GoodsParam.STORE_ID, storeId);
        }
    }

    private void showData(StoreEntity storeEntity) {
        this.storeEntity = storeEntity;
        mBinding.setItem(storeEntity);
        Glide.with(getContext())
                .asBitmap()
                .load(storeEntity.getLogo_url())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(14, 3)))
                .into(mBinding.ivStoreBg);
        mBinding.executePendingBindings();
        mBinding.tvFollow.setSelected(storeEntity.getIs_collection() == 1);
    }


    private void doGetStoreInfo() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .getStoreDetail(getIntent().getStringExtra(GoodsParam.STORE_ID))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<StoreEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<StoreEntity> data) {
                        showData(data.getData());
                    }
                });
    }


    private void doCollection() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollection(storeId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        storeEntity.setSum_collection(storeEntity.getSum_collection() + 1);
                        storeEntity.setIs_collection(1);
                        mBinding.tvFollow.setSelected(true);
                        mBinding.tvFollow.setText(R.string.sharemall_followed);
                        mBinding.tvFollowNumber.setText(String.format(getString(R.string.sharemall_format_follow), storeEntity.getSum_collection()));
                    }

                });
    }

    private void doCollectionDel() {
        showProgress("");
        List<String> list = new ArrayList<>();
        list.add(storeId);
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCollectionDel(list)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        storeEntity.setSum_collection(storeEntity.getSum_collection() - 1);
                        storeEntity.setIs_collection(0);
                        mBinding.tvFollow.setSelected(false);
                        mBinding.tvFollow.setText(R.string.sharemall_follow);
                        mBinding.tvFollowNumber.setText(String.format(getString(R.string.sharemall_format_follow), storeEntity.getSum_collection()));
                    }
                });
    }
}
