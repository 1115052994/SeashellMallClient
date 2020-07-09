package com.liemi.seashellmallclient.ui.mine.order;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.CommendGoodEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderDetailedEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.event.OrderRefreshEvent;
import com.liemi.seashellmallclient.data.event.OrderUpdateEvent;
import com.liemi.seashellmallclient.databinding.SharemallFragmentXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemOfflineOrderBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemOrderBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopDetailActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.widget.decoration.ItemDivider;
import com.liemi.seashellmallclient.widget.decoration.QuickItemDecoration;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;
import static com.liemi.seashellmallclient.data.param.OrderParam.*;
import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_DETAILS_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 9:55
 * 修改备注：
 */
public class OrderModuleFragment extends BaseXRecyclerFragment<SharemallFragmentXrecyclerviewBinding, BaseEntity> {

    protected int orderState;

    private ClickOrderButtonListener orderButtonListener;

    protected PageEntity<BaseEntity> pageEntity = new PageEntity<>();

    public static OrderModuleFragment newInstance(int state, ClickOrderButtonListener orderButtonListener) {
        OrderModuleFragment f = new OrderModuleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ORDER_STATE, state);
        f.setArguments(bundle);
        f.setOrderButtonListener(orderButtonListener);
        return f;
    }

    public void setOrderButtonListener(ClickOrderButtonListener orderButtonListener) {
        this.orderButtonListener = orderButtonListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_xrecyclerview;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        initAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItem(position) instanceof CommendGoodEntity ? 1 : 2;
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingListener(this);
    }

    @Override
    protected void initData() {
        orderState = getArguments() != null ? getArguments().getInt(ORDER_STATE) : 0;
        xRecyclerView.refresh();
    }

    //初始化订单adapter
    protected void initAdapter() {
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext(), xRecyclerView) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 2) {
                    return R.layout.sharemall_item_order_recommend_good;
                } else if (viewType == 3) {
                    return R.layout.sharemall_item_order_empty;
                }
                return R.layout.sharemall_item_order;
            }

            @Override
            public int getItemViewType(int position) {
                return isGoods(position) ? 2 : getItem(position) instanceof OrderDetailedEntity ? (TextUtils.equals(((OrderDetailedEntity) getItem(position)).getType(), "12") ? 4 : 1) : 3;
            }

            private boolean isGoods(int position) {
                return getItem(position) instanceof CommendGoodEntity;
            }

            private OrderDetailedEntity getItemOrder(int position) {
                return isGoods(position) ? new OrderDetailedEntity() : (OrderDetailedEntity) getItem(position);
            }

            private CommendGoodEntity getItemGoods(int position) {
                return isGoods(position) ? (CommendGoodEntity) getItem(position) : new CommendGoodEntity();
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {

                    private int topPosition;

                    @Override
                    public void bindData(BaseEntity item) {
                        topPosition = position;
                        if (getBinding() instanceof SharemallItemOrderBinding) {
                            RecyclerView rvGoods = ((SharemallItemOrderBinding) getBinding()).rvGoods;
                            rvGoods.setNestedScrollingEnabled(false);
                            rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                            if (rvGoods.getItemDecorationCount() == 0) {
                                rvGoods.addItemDecoration(new QuickItemDecoration(new ItemDivider()
                                        .setColor(Color.parseColor("#eeeeee"))
                                        .setWidth(DensityUtils.dp2px(1))
                                        .setMarginRight(DensityUtils.dp2px(16))
                                        .setMarginLeft(DensityUtils.dp2px(16))));
                            }
                            BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter = new OrderGoodsAdapter(getContext(), true, false, (View view, OrderSkusEntity skusEntity) -> {
                                Bundle bundle = new Bundle();
                                bundle.putString(ORDER_DETAILS_ID, getItemOrder(topPosition).getMain_order_id());
                                JumpUtil.overlay(getContext(), MineOrderDetailsActivity.class, bundle);
                            });

                            rvGoods.setAdapter(goodAdapter);
                            goodAdapter.setData(getItemOrder(position).getGoods());
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.tv_order_function1) {
                            if (orderButtonListener != null) {
                                orderButtonListener.clickLeftButton(getItemOrder(position));
                            }
                        } else if (i == R.id.tv_order_function2) {
                            if (orderButtonListener != null) {
                                orderButtonListener.clickRightButton(getItemOrder(position));
                            }
                        } else if (i == R.id.tv_store_name) {
                            if (!TextUtils.isEmpty(getItemOrder(position).getMainOrder().getShop_id())) {
                                if (TextUtils.equals(getItemOrder(position).getType(), "12")) {
                                    //线下买单
                                    JumpUtil.overlay(getContext(), LocalLifeShopDetailActivity.class,SHOP_ID,getItemOrder(position).getMainOrder().getShop_id());
                                } else {
                                    StoreDetailActivity.start(getContext(), getItemOrder(position).getMainOrder().getShop_id());
                                }
                            }
                        } else if(i == R.id.tv_go_home){
                            MApplication.getInstance().backHome();
                        } else if (isGoods(position)) {
                            GoodDetailPageActivity.start(getContext(), getItemGoods(position).getItem_id(), null);
                        } else if (i == R.id.ll_good){
                            Bundle bundle = new Bundle();
                            bundle.putString(ORDER_DETAILS_ID, getItemOrder(position).getMain_order_id());
                            JumpUtil.overlay(getContext(), MineOfflineOrderDetailsActivity.class, bundle);
                        }
                    }
                };
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void orderUpdate(OrderUpdateEvent event) {
        if (adapter == null) return;
        //删除订单通知
        for (BaseEntity entity : adapter.getItems()) {
            if (TextUtils.equals(String.valueOf(getOrderDetails(entity).getId()), event.getMpid())) {
                //删除订单，从全部订单删除商品
                if (event.getStatus() == -1 && orderState == -1) {
                    adapter.remove(entity);
                    return;
                }
                //完成付款
                if (event.getStatus() == ORDER_WAIT_SEND) {
                    onRefresh();
                    return;
                }

                //取消订单
                if (event.getStatus() == ORDER_CANCEL) {
                    if (orderState == -1) {
                        onRefresh();
                    } else if (orderState == ORDER_WAIT_PAY) {
                        adapter.remove(entity);
                    }
                    return;
                }
                //确认收货
                if (event.getStatus() == ORDER_WAIT_COMMENT) {
                    if (orderState == -1) {
                        //全部订单页面需要刷新数据
                        getOrderDetails(entity).setStatus(ORDER_WAIT_COMMENT);
                        adapter.notifyDataSetChanged();
                    } else if (orderState == ORDER_WAIT_RECEIVE) {
                        //待收货页面需要删除数据
                        adapter.remove(entity);
                    } else if (orderState == ORDER_WAIT_COMMENT) {
                        //待评价页面需要重新请求数据
                        onRefresh();
                    }
                    return;
                }
                //订单完成
                if (event.getStatus() == ORDER_SUCCESS) {
                    if (orderState == -1) {
                        getOrderDetails(entity).setStatus(ORDER_SUCCESS);
                    } else if (orderState == ORDER_WAIT_COMMENT) {
                        adapter.remove(entity);
                    }
                }
            }
        }

        //取消订单通知：如果状态是取消，并且当前fragment是全部订单，那么就刷新数据
        if (event.getStatus() == ORDER_CANCEL && orderState == -1) {
            onRefresh();
            return;
        }
        //确认收货订单通知，遍历所有fragment中的订单，找到对应的订单然后修改状态
        if (event.getStatus() == ORDER_WAIT_COMMENT) {
            //在全部订单中修改状态
            if (orderState == -1) {
                for (BaseEntity entity : adapter.getItems()) {
                    if (TextUtils.equals(String.valueOf(getOrderDetails(entity).getId()), event.getMpid())) {
                        getOrderDetails(entity).setStatus(event.getStatus());
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            } else if (orderState == ORDER_WAIT_COMMENT) {
                //在待评价页面刷新数据
                onRefresh();
            }
        }

    }

    private OrderDetailedEntity getOrderDetails(BaseEntity entity) {
        return (OrderDetailedEntity) entity;
    }

    private boolean isEmpty() {
        return adapter == null || adapter.getItems().isEmpty() || !(adapter.getItems().get(0) instanceof OrderDetailedEntity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderRefresh(OrderRefreshEvent event) {
        Logs.i("退款/退款退货成功，刷新数据");
        mBinding.xrvData.refresh();
    }

    //定义两个接口
    public interface ClickOrderButtonListener {

        void clickLeftButton(OrderDetailedEntity entity);

        void clickRightButton(OrderDetailedEntity entity);
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
        RetrofitApiFactory.createApi(OrderApi.class)
                .listAllOrder(PageUtil.toPage(startPage), Constant.PAGE_ROWS, orderState > -1 ? orderState : null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageEntity<OrderDetailedEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<OrderDetailedEntity>> data) {
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
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getCommendGoods(null)
                .compose(RxSchedulers.compose())
                .compose(((RxFragment) this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<List<CommendGoodEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<CommendGoodEntity>> data) {
                        if (!Strings.isEmpty(data.getData())) {
                            pageEntity.getList().addAll(data.getData());
                            //显示6个猜你喜欢
                            pageEntity.setTotal_pages(6);
                        }
                        if (LOADING_TYPE == Constant.PULL_REFRESH) {
                            pageEntity.getList().add(0, new BaseEntity());
                        }
                        showData(pageEntity);
                    }
                });
    }

}
