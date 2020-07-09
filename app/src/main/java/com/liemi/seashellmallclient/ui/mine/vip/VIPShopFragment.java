package com.liemi.seashellmallclient.ui.mine.vip;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPMemberEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPMemberPageEntity;
import com.liemi.seashellmallclient.data.event.VipFollowerNumEvent;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

public class VIPShopFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerviewBinding, MyVIPMemberEntity> {
    public static final String MEMBER_TYPE = "member_type";
    public static VIPShopFragment newInstance(int type) {
        VIPShopFragment f = new VIPShopFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MEMBER_TYPE, type);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<MyVIPMemberEntity, BaseViewHolder>(getContext(),
                xRecyclerView, R.layout.baselib_include_no_data_view2,
                new EmptyLayoutEntity(R.mipmap.sharemall_ic_goods_empty,getString(R.string.sharemall_no_data))) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_my_vip_shop;
            }

            @Override
            public BaseViewHolder<MyVIPMemberEntity> holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MyVIPMemberEntity>(binding) {
                    @Override
                    public void bindData(MyVIPMemberEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
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
        Observable<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>> observable;
        observable = RetrofitApiFactory.createApi(VIPApi.class)
                    .getMyVIPFollowers(PageUtil.toPage(startPage), Constant.PAGE_ROWS,"2");
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<VIPMemberPageEntity<MyVIPMemberEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPMemberPageEntity<MyVIPMemberEntity>> data) {

                        EventBus.getDefault().post(new VipFollowerNumEvent(1, Strings.toInt(data.getData().getTotal_count())));
                        showData(data.getData());
                    }
                });
    }
}
