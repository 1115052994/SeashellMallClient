package com.liemi.seashellmallclient.ui.shopcart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.good.CommendGoodEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentShopCartBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemShopCarTopBinding;
import com.liemi.seashellmallclient.ui.good.order.FillOrderActivity;
import com.liemi.seashellmallclient.widget.ShopCartCallback;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import io.reactivex.disposables.Disposable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/25
 * 修改备注：
 */
public class ShopCartFragment extends BaseXRecyclerFragment<SharemallFragmentShopCartBinding, CommendGoodEntity> implements ShopCartCallback {

    public static final String TAG = ShopCartFragment.class.getName();

    private SharemallItemShopCarTopBinding topBinding;

    private ShopGoodAdapter goodsAdapter;

    private int selectCount;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_shop_cart;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBarUtils.setStatusBar(this, true, R.color.white);
    }

    @Override
    protected void initUI() {
        if (getActivity() instanceof ShopCartActivity) {
            mBinding.ivBack.setVisibility(View.VISIBLE);
            mBinding.ivBack.setOnClickListener(v -> getActivity().finish());
        }
        initImmersionBar();
        mBinding.setDoClick(this);
        EventBus.getDefault().register(this);
        topBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_item_shop_car_top, mBinding.rlContent, false);

        topBinding.rvShopCart.setLayoutManager(new LinearLayoutManager(getContext()));
        topBinding.rvShopCart.setNestedScrollingEnabled(false);
        topBinding.rvShopCart.setAdapter(goodsAdapter = new ShopGoodAdapter(getContext(), this));

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setAdapter(adapter = new CommendGoodAdapter(getContext()));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.addHeaderView(topBinding.getRoot());
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void childCheck() {
        calcuCount();
    }

    private void calcuCount() {
        double totalPrice = 0;
        selectCount = 0;

        boolean isAllSelect = true, isStoreSelect = true;
        if (goodsAdapter.getItems().size() > 0) {
            StoreEntity preStore = goodsAdapter.getStoreItem(0);
            preStore.setForbidden(true);
            for (BaseEntity entity : goodsAdapter.getItems()) {
                if (entity instanceof GoodsDetailedEntity) {
                    GoodsDetailedEntity goodsEntity = (GoodsDetailedEntity) entity;
                    if (goodsEntity.isChecked()) {
                        totalPrice += (Strings.toFloat(goodsEntity.getRealPrice()) * Strings.toFloat(goodsEntity.getNum()));
                        selectCount++;
                    }
                    if (!goodsEntity.unableBuy(isEdit()) && !goodsEntity.isChecked()) {
                        isAllSelect = false;
                        isStoreSelect = false;
                    }
                    //只要有一个商品没有被禁用，店铺就不会被禁选
                    if (!goodsEntity.unableBuy(isEdit())) {
                        preStore.setForbidden(false);
                    }
                } else {
                    if (preStore != entity) {
                        preStore.setChecked(isStoreSelect);
                        preStore = (StoreEntity) entity;
                        preStore.setForbidden(true);
                        isStoreSelect = true;
                    }
                }

            }

            preStore.setChecked(isStoreSelect);
            goodsAdapter.notifyDataSetChanged();
        }

        if (isAllSelect && selectCount > 0) {
            mBinding.ivAll.setSelected(true);
            mBinding.ivAll.setImageResource(R.drawable.sharemall_icon_mine_coupon_un_select);
        } else {
            mBinding.ivAll.setSelected(false);
            mBinding.ivAll.setImageResource(R.drawable.sharemall_icon_mine_coupon_select);
        }

        mBinding.tvTotalPrice.setText(FloatUtils.formatMoney(totalPrice));
//        mBinding.tvEdit.setVisibility(goodsAdapter.getItemSize() > 0 ? View.VISIBLE : View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void shopCartUpdate(ShopCartEvent event) {
        doListShopCart();
    }

    private boolean isEdit() {
        return TextUtils.equals(mBinding.tvEdit.getText().toString(), getString(R.string.sharemall_finish));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.ll_all) {
            if (adapter.getItemCount() <= 0) {
                return;
            }
            mBinding.ivAll.setSelected(!mBinding.ivAll.isSelected());
            mBinding.ivAll.setImageResource(mBinding.ivAll.isSelected() ? R.drawable.sharemall_icon_mine_coupon_un_select : R.drawable.sharemall_icon_mine_coupon_select);

            for (BaseEntity entity : goodsAdapter.getItems()) {
                if (entity instanceof GoodsDetailedEntity) {
                    GoodsDetailedEntity goodsEntity = (GoodsDetailedEntity) entity;
                    //禁用的商品和库存不足的商品,不允许被选
                    if (goodsEntity.unableBuy(isEdit())) {
                        continue;
                    }
                    goodsEntity.setChecked(mBinding.ivAll.isSelected());
                }
            }
            goodsAdapter.notifyDataSetChanged();
            calcuCount();
        } else if (view.getId() == R.id.tv_edit) {
            //切换到下单
            if (isEdit()) {
                mBinding.tvEdit.setText(R.string.sharemall_manager);
                mBinding.tvConfirm.setText(R.string.sharemall_to_create_order);
                mBinding.llTotal.setVisibility(View.VISIBLE);
            }
            //切换到管理购物车
            else {
                mBinding.tvEdit.setText(R.string.sharemall_finish);
                mBinding.tvConfirm.setText(R.string.sharemall_delete);
                mBinding.llTotal.setVisibility(View.INVISIBLE);
            }
            goodsAdapter.setEdit(isEdit());
        } else if (view.getId() == R.id.tv_confirm) {
            if (selectCount < 1) {
                ToastUtils.showShort(getString(R.string.sharemall_please_select_goods));
                return;
            }

            ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();
            ShopCartEntity addShopCart = new ShopCartEntity();
            for (BaseEntity entity : goodsAdapter.getItems()) {
                if (entity instanceof StoreEntity) {
                    if (!addShopCart.getList().isEmpty()) {
                        shopCartEntities.add(addShopCart);
                    }
                    addShopCart = new ShopCartEntity();
                    addShopCart.setShop((StoreEntity) entity);
                } else {
                    GoodsDetailedEntity goods = (GoodsDetailedEntity) entity;
                    if (goods.isChecked()) {
                        addShopCart.getList().add(goods);
                    }
                }
            }
            if (!addShopCart.getList().isEmpty()) {
                shopCartEntities.add(addShopCart);
            }

            //删除
            if (isEdit()) {
                List<GoodsDetailedEntity> list = new ArrayList<>();
                for (ShopCartEntity shopCartEntity : shopCartEntities) {
                    list.addAll(shopCartEntity.getList());
                }
                doDelete(list);
            }
            //去下单
            else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(GoodsParam.SHOP_CARTS, shopCartEntities);
                JumpUtil.overlay(getContext(), FillOrderActivity.class, bundle);
            }
        }
    }


    @Override
    protected void doListData() {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            doListShopCart();
        } else {
            doListRecommendGoods();
        }
    }

    private void doListShopCart() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listShopCart(null, 0, Constant.ALL_PAGES)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageEntity<ShopCartEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<ShopCartEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            List<BaseEntity> entities = new ArrayList<>();
                            for (ShopCartEntity shopCartEntity : data.getData().getList()) {
                                entities.add(shopCartEntity.getShop());
                                entities.addAll(shopCartEntity.getList());
                            }
                            goodsAdapter.setData(entities);
                            calcuCount();
                        } else {
                            goodsAdapter.setData(null);
                            calcuCount();
                        }
                    }

                    @Override
                    public void onComplete() {
                        doListRecommendGoods();
                    }
                });
    }

    private void doListRecommendGoods() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getCommendGoods(null)
                .compose(RxSchedulers.compose())
                .compose(((RxFragment) this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<List<CommendGoodEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<CommendGoodEntity>> data) {
                        if (!Strings.isEmpty(data.getData())) {
                            topBinding.tvTitleLike.setVisibility(View.VISIBLE);
                            showData(data.getData(), data.getData().size());
                        }
                    }
                });
    }


    //多次操作，以最后一次为准
    private Disposable lastDisposable;
    //用于回滚
    private int lastUpdatePosition = -1;

    @Override
    public void doUpdateCartNum(final GoodsDetailedEntity goodEntity, final float num) {

        int currentPosition = goodsAdapter.getItems().indexOf(goodEntity);

        if (lastDisposable != null) {
            lastDisposable.dispose();
            lastDisposable = null;

            if (lastUpdatePosition >= 0 && lastUpdatePosition != currentPosition) {
                goodsAdapter.notifyPosition(lastUpdatePosition);
            }
        }

        lastUpdatePosition = currentPosition;
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCartUpdate(goodEntity.getCart_id(), String.valueOf(num))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        lastDisposable = d;
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        goodEntity.setNum(Strings.twoDecimal(num));
                        calcuCount();
                    }

                    @Override
                    public void onComplete() {
                        doResult();
                    }

                    private void doResult() {
                        lastDisposable = null;
                        lastUpdatePosition = -1;
                        goodsAdapter.notifyPosition(lastUpdatePosition);
                    }

                });
    }


    @Override
    public void doDelete(final int position) {
        List<GoodsDetailedEntity> list = new ArrayList<>(1);
        list.add(goodsAdapter.getGoodsItem(position));
        doDelete(list);
    }

    public void doDelete(List<GoodsDetailedEntity> list) {
        List<String> cardIds = new ArrayList<>(list.size());
        for (GoodsDetailedEntity entity : list) {
            cardIds.add(entity.getCart_id());
        }
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCartDel(cardIds)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        for (GoodsDetailedEntity entity : list) {
                            //如果店铺下只有一个商品被删除了，连带删除店铺
                            int position = goodsAdapter.getItems().indexOf(entity);
                            int remove = position;
                            if (!goodsAdapter.isGoodsItem(position - 1)
                                    && !goodsAdapter.isGoodsItem(position + 1)) {
                                remove = position - 1;
                                goodsAdapter.remove(remove);
                            }
                            goodsAdapter.remove(remove);
                        }
                        calcuCount();
                    }
                });
    }


}
