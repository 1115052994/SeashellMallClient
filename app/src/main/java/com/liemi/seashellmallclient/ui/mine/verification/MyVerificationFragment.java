package com.liemi.seashellmallclient.ui.mine.verification;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.VerificationApi;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.liemi.seashellmallclient.data.event.VerificationOrderUpdateEvent;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopDetailActivity;
import com.liemi.seashellmallclient.ui.mine.order.MineOfflineOrderDetailsActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;

import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_DETAILS_ID;

public class MyVerificationFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerviewBinding, BaseEntity> {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "order_type";

    private String orderType;       //""：全部 0：待付款 1：待核销 3：待评价

    public static final String ALL="";
    public static final String WAIT_PAY="0";
    public static final String WAIT_VERIFICATION="1";
    public static final String WAIT_COMMENT="3";
    private PageEntity<BaseEntity> pageEntity = new PageEntity<>();

    private VerificationClickOrderButtonListener orderButtonListener;

    public MyVerificationFragment() {

    }

    public static MyVerificationFragment newInstance(String orderType, VerificationClickOrderButtonListener orderButtonListener) {
        MyVerificationFragment fragment = new MyVerificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, orderType);
        fragment.setArguments(args);
        fragment.setOrderButtonListener(orderButtonListener);
        return fragment;
    }

    public void setOrderButtonListener(VerificationClickOrderButtonListener orderButtonListener) {
        this.orderButtonListener = orderButtonListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            orderType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        initAdapter();
    }

    private void initAdapter() {
        adapter=new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType==1){
                    return R.layout.verification_item_order;
                }else if (viewType == 4) {
                    return R.layout.sharemall_item_offline_order;
                }
                return R.layout.sharemall_item_order_empty1;
            }

            @Override
            public int getItemViewType(int position) {
                return getItem(position) instanceof VerificationOrderDetailEntity ? (TextUtils.equals(((VerificationOrderDetailEntity) getItem(position)).getType(), "12") ? 4 : 1):0;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {
                    @Override
                    public void bindData(BaseEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (getItem(position) instanceof VerificationOrderDetailEntity){
                            if (i == R.id.tv_order_function1) { //删除
                                if (orderButtonListener != null) {
                                    orderButtonListener.clickLeftButton((VerificationOrderDetailEntity) getItem(position));
                                }
                            } else if (i == R.id.tv_order_function2) {  //评价、去付款
                                if (orderButtonListener != null) {
                                    orderButtonListener.clickRightButton((VerificationOrderDetailEntity) getItem(position));
                                }
                            } else if (i == R.id.tv_store_name) {
                                //跳转至店铺详情
                                JumpUtil.overlay(getContext(), LocalLifeShopDetailActivity.class, ParamConstant.SHOP_ID, ((VerificationOrderDetailEntity)getItem(position)).getShop().getId());
                            }else {
                                if (TextUtils.equals(((VerificationOrderDetailEntity) getItem(position)).getType(),"12")){
                                    Bundle bundle = new Bundle();
                                    bundle.putString(ORDER_DETAILS_ID, ((VerificationOrderDetailEntity) getItem(position)).getOrder_main_id());
                                    JumpUtil.overlay(getContext(), MineOfflineOrderDetailsActivity.class, bundle);
                                }else {
                                    JumpUtil.overlay(getContext(),VerificationMineOrderDetailsActivity.class,VerificationMineOrderDetailsActivity.ORDER_DETAILS_ID,((VerificationOrderDetailEntity) getItem(position)).getId());
                                }
                            }
                        }
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        orderType = getArguments() != null ? getArguments().getString(ARG_TYPE) : "0";
        xRecyclerView.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdate(VerificationOrderUpdateEvent event) {
        hideProgress();
        if (isEmpty()) return;
        //删除订单
        if (event.getStatus() == -1) {
            adapter.remove(getOrderDetailsById(event.getMpid()));
            return;
        }

    }

    private boolean isEmpty() {
        return adapter == null || adapter.getItems().isEmpty() ;
    }

    private VerificationOrderDetailEntity getOrderDetailsById(String id) {
        for (BaseEntity entity : adapter.getItems()) {
            if (entity instanceof VerificationOrderDetailEntity && TextUtils.equals(String.valueOf(((VerificationOrderDetailEntity)entity).getId()), id)) {
                return (VerificationOrderDetailEntity) entity;
            }
        }
        return new VerificationOrderDetailEntity();
    }

    @Override
    public void onRefresh() {
        pageEntity.getList().clear();
        super.onRefresh();
    }

    @Override
    public void onLoadMore() {
        if (isEmpty()) {
            LOADING_TYPE = Constant.LOAD_MORE;
            pageEntity.getList().clear();
            doListRecommendGoods();
        } else {
            super.onLoadMore();
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(VerificationApi.class)
                .getVerificationOrderList(PageUtil.toPage(startPage), Constant.PAGE_ROWS, orderType)
                .compose(RxSchedulers.<BaseData<PageEntity<VerificationOrderDetailEntity>>>compose())
                .compose((this).<BaseData<PageEntity<VerificationOrderDetailEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<PageEntity<VerificationOrderDetailEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<PageEntity<VerificationOrderDetailEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            pageEntity.getList().addAll(data.getData().getList());
                            pageEntity.setTotal_pages(data.getData().getTotal_pages());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (pageEntity.getList().isEmpty()) {
                            doListRecommendGoods();
                        } else {
                            showData(pageEntity);
                            hideProgress();
                        }
                    }
                });
    }

    protected void doListRecommendGoods() {
        //显示6个猜你喜欢
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            pageEntity.getList().add(0, new BaseEntity());
            pageEntity.setTotal_pages(6);
        }
        showData(pageEntity);
        hideProgress();
    }
}
