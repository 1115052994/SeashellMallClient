package com.liemi.seashellmallclient.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.entity.floor.FloorPageEntity;
import com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.ShareMallParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.trello.rxlifecycle2.android.FragmentEvent;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/22 10:49
 * 修改备注：
 */
public class HomeCategoryFragment extends BaseXRecyclerFragment<SharemallFragmentXrecyclerviewBinding, NewFloorEntity> {

    public static final String TAG = HomeCategoryFragment.class.getName();

    //位置编号
    private static final String USE_POSITION = "usePosition";

    //位置编号，默认1为首页
    private String usePosition = "1", storeId;

    public static HomeCategoryFragment newInstance(String usePosition, String storeId) {
        HomeCategoryFragment f = new HomeCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USE_POSITION, usePosition);
        bundle.putString(GoodsParam.STORE_ID, storeId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_xrecyclerview;
    }

    @Override
    protected void initUI() {
        if (getArguments() != null) {
            storeId = getArguments().getString(GoodsParam.STORE_ID);
            String position = getArguments().getString(USE_POSITION);
            if (!TextUtils.isEmpty(position)) {
                usePosition = position;
            }
        }

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new FloorAdapter(getContext(), getChildFragmentManager(), xRecyclerView)
                .setShopId(storeId)
                .setLifecycleFragment(this)
                .setHideProgressListener(this::hideProgress));

        //秒杀拼团特殊样式，设置不复用，避免刷新后出现错乱
        xRecyclerView.setItemViewCacheSize(ShareMallParam.FLOOR_VIEW_CACHE);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(HomeApi.class)
                .doListFloors(usePosition, storeId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<FloorPageEntity<NewFloorEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<FloorPageEntity<NewFloorEntity>> data) {
                        if (data.getData().getContent() != null) {
                            if (getActivity() instanceof FloorActivity) {
                                ((FloorActivity) getActivity()).getTvTitle().setText(data.getData().getTitle());
                            }
                            showData(data.getData());
                        }
                    }

                    @Override
                    public void onFail(BaseData<FloorPageEntity<NewFloorEntity>> data) {
                        //楼层还未配置，不需要提示
                        if (data.getErrcode() != 999999) {
                            super.onFail(data);
                        }
                    }

                });
    }


}
