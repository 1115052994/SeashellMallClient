package com.liemi.seashellmallclient.ui.mine;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.user.MineCollectionStoreEntity;
import com.liemi.seashellmallclient.databinding.ActivityMyCollectionBinding;
import com.liemi.seashellmallclient.databinding.BasemallItemMineCollectionStoreBinding;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopDetailActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressDialog;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;
import static com.liemi.seashellmallclient.data.param.GoodsParam.STORE_ID;
import static com.netmi.baselibrary.ui.BaseRViewAdapter.EMPTY_DEFAULT_LAYOUT;

public class MyCollectionActivity extends BaseXRecyclerActivity<ActivityMyCollectionBinding, MineCollectionStoreEntity> {

    private AddressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.my_collect));
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initAdapter();
    }

    private void initAdapter() {
        adapter = new BaseRViewAdapter<MineCollectionStoreEntity, BaseViewHolder>(getActivity(), xRecyclerView, EMPTY_DEFAULT_LAYOUT) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.basemall_item_mine_collection_store;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MineCollectionStoreEntity>(binding) {
                    @Override
                    public void bindData(MineCollectionStoreEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public BasemallItemMineCollectionStoreBinding getBinding() {
                        return (BasemallItemMineCollectionStoreBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_cancle_collect) { //取消关注
                            if (mDialog == null) {
                                mDialog = new AddressDialog(getContext());
                            }
                            if (!mDialog.isShowing()) {
                                mDialog.show();
                            }

                            mDialog.setTitle(getString(R.string.sharemall_hint2));
                            mDialog.setMessage(getString(R.string.sharemall_confirm_cancel_follow));
                            mDialog.setConfirm(getString(R.string.sharemall_confirm_ok));
                            mDialog.setClickConfirmListener(new AddressDialog.ClickConfirmListener() {
                                @Override
                                public void clickConfirm() {
                                    List<String> list = new ArrayList<>();
                                    list.add(String.valueOf(getItem(position).getId()));
                                    doCancleStore(list);
                                }
                            });
                            return;
                        }
                        if (TextUtils.equals(getItem(position).getShop_type(), "1") || TextUtils.equals(getItem(position).getShop_type(), "3")) {
                            JumpUtil.overlay(context, StoreDetailActivity.class,
                                    new FastBundle().putString(STORE_ID, String.valueOf(getItem(position).getId())));
                        } else if (TextUtils.equals(getItem(position).getShop_type(), "2")) {
                            JumpUtil.overlay(context, LocalLifeShopDetailActivity.class,
                                    new FastBundle().putString(SHOP_ID, String.valueOf(getItem(position).getId())));
                        }

                    }

                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        doGetCollectStore();
    }

    private void doGetCollectStore() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineCollectionStore(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(this.<BaseData<PageEntity<MineCollectionStoreEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PageEntity<MineCollectionStoreEntity>>>compose())
                .subscribe(new BaseObserver<BaseData<PageEntity<MineCollectionStoreEntity>>>() {
                    @Override
                    public void onNext(BaseData<PageEntity<MineCollectionStoreEntity>> baseData) {
                        hideProgress();
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            showData(baseData.getData());
                        } else {
                            showError(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }

    private void doCancleStore(List<String> list) {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUnCollectionStore(list)
                .compose(this.<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData>compose())
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        xRecyclerView.refresh();
                    }

                });
    }

}
