package com.liemi.seashellmallclient.ui.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.CouponApi;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.databinding.*;
import com.liemi.seashellmallclient.ui.good.GoodsCouponDialogFragment;
import com.liemi.seashellmallclient.ui.good.GoodsListAdapter;
import com.liemi.seashellmallclient.ui.home.floor.FloorClickUtils;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.widget.BannerViewHolder;
import com.liemi.seashellmallclient.widget.ChildAutoHeightViewPager;
import com.liemi.seashellmallclient.widget.MyTextBannerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.*;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.LoadingDialog;
import com.netmi.baselibrary.widget.ResizableImageView;
import com.netmi.baselibrary.widget.XERecyclerView;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;
import static com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity.*;
import static com.netmi.baselibrary.utils.DateUtil.DF_YYYY_MM_DD_HH_MM_SS;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/4
 * 修改备注：
 */
public class FloorAdapter extends BaseRViewAdapter<NewFloorEntity, BaseViewHolder> {

    //秒杀模块用到
    private FragmentManager fragmentManager;
    //生命周期
    private RxFragment lifecycleFragment;
    //店铺的楼层
    private String shopId;
    //加载框监听
    private HideProgressListener hideProgressListener;

    public FloorAdapter(Context context) {
        super(context);
    }

    public FloorAdapter(Context context, FragmentManager fragmentManager, XERecyclerView recyclerView) {
        super(context, recyclerView);
        this.fragmentManager = fragmentManager;
    }

    public FloorAdapter setLifecycleFragment(RxFragment lifecycleFragment) {
        this.lifecycleFragment = lifecycleFragment;
        return this;
    }

    public FloorAdapter setHideProgressListener(HideProgressListener hideProgressListener) {
        this.hideProgressListener = hideProgressListener;
        return this;
    }


    public FloorAdapter setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    private Context getContext() {
        return context;
    }

    //数据请求完成后统一刷新，避免闪烁
    private int requestCount = 0;

    private void requestResult() {
        requestCount--;
        if (requestCount == 0) {
            super.setData(floorItems);
            if (hideProgressListener != null) {
                hideProgressListener.hideProgressByCallBack();
            }
        }
    }

    private List<NewFloorEntity> floorItems = new ArrayList<>();

    public void requestData(List<NewFloorEntity> items) {
        floorItems.addAll(items);
        for (NewFloorEntity entity : items) {
            if (entity.getType() == FLOOR_GOODS_HOT
                    || entity.getType() == FLOOR_GOODS_NEWS
                    || entity.getType() == FLOOR_CUSTOM_GOODS) {
                requestCount++;
                doListGoodsData(entity);
            } else if (entity.getType() == FLOOR_STORE) {
                requestCount++;
                doListStore(entity);
            } else if (entity.getType() == FLOOR_COUPON) {
                requestCount++;
                doListCoupon(entity, false);
            }
        }
        if (requestCount == 0) {
            requestCount++;
            requestResult();
        }
    }

    @Override
    public void setData(List<NewFloorEntity> items) {
        floorItems.clear();
        requestData(items);
    }

    @Override
    public void insert(int position, List<NewFloorEntity> items) {
        if (items != null && !items.isEmpty()) {
            this.items.addAll(position, items);
            requestData(items);
        } else {
            super.insert(position, items);
        }
    }

    @Override
    public int layoutResId(int viewType) {
        switch (viewType) {
            case FLOOR_BANNER:
                return R.layout.sharemall_item_floor_banner;
            case FLOOR_NOTICE:
                return R.layout.sharemall_item_floor_notice;
            case FLOOR_NAVIGATION:
                return R.layout.sharemall_item_floor_navigation;
            case FLOOR_IMAGE:
                return R.layout.sharemall_item_floor_multi_pic;
            case FLOOR_GOODS_HOT:
            case FLOOR_GOODS_NEWS:
            case FLOOR_STORE:
            case FLOOR_CUSTOM_GOODS:
                return R.layout.sharemall_item_floor_goods;
            case FLOOR_COUPON:
                return R.layout.sharemall_item_floor_coupon;
            default:
                return R.layout.sharemall_layout_empty;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }


    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {

        return new BaseViewHolder<NewFloorEntity>(binding) {
            //屏幕宽度，用于计算图片比例
            private float screenWidth;

            //同一个页面的ViewPager的Id不能共享，否则导致滑动卡顿、不加载
            private int getViewPagerId(int position) {
                return position + 100;
            }

            private void floorClick(NewFloorEntity.FloorDataBean entity) {
                FloorClickUtils.getInstance().floorDataClick(getContext(), entity, shopId);
            }

            @Override
            public void bindData(final NewFloorEntity item) {
                //楼层Banner的显示
                if (getBinding() instanceof SharemallItemFloorBannerBinding) {
                    SharemallItemFloorBannerBinding binding = (SharemallItemFloorBannerBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    if (item.getFloor_data() != null
                            && !item.getFloor_data().isEmpty()) {
                        final List<String> list = new ArrayList<>();

                        for (NewFloorEntity.FloorDataBean entity : item.getFloor_data()) {
                            list.add(entity.getImg_url());
                        }
                        //轮播间隔
                        binding.cbBanner.setDelayedTime(6000);
                        //滑动速度
                        binding.cbBanner.setDuration(1800);
                        //指示器隐藏
                        binding.cbBanner.setIndicatorVisible(true);
                        // 可在project下查看lambda记录.txt
                        binding.cbBanner.setBannerPageClickListener((View view, int i) -> floorClick(item.getFloor_data().get(i)));
                        // 设置数据
                        binding.cbBanner.setPages(list, BannerViewHolder::new);

                        binding.cbBanner.start();
                    }
                }
                //楼层公告的显示
                else if (getBinding() instanceof SharemallItemFloorNoticeBinding) {
                    SharemallItemFloorNoticeBinding binding = (SharemallItemFloorNoticeBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    List<MyTextBannerView.TextBannerEntity> list = new ArrayList<>();
                    for (NewFloorEntity.FloorDataBean entity : item.getFloor_data()) {
                        list.add(new MyTextBannerView.TextBannerEntity(entity.getTitle(), entity.getTime()));
                    }
                    binding.tvBanner.setItemOnClickListener((String data, int position) -> floorClick(item.getFloor_data().get(position)));
                    binding.tvBanner.stopViewAnimator();
                    binding.tvBanner.setData(list);
                    binding.tvBanner.startViewAnimator();
                }
                //分类导航栏的显示
                else if (getBinding() instanceof SharemallItemFloorNavigationBinding) {
                    SharemallItemFloorNavigationBinding binding = (SharemallItemFloorNavigationBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
                    binding.rvFloor.setBackgroundColor(Color.WHITE);
                    binding.rvFloor.setLayoutManager(gridLayoutManager);
                    BaseRViewAdapter<NewFloorEntity.FloorDataBean, BaseViewHolder> picAdapter = new BaseRViewAdapter<NewFloorEntity.FloorDataBean, BaseViewHolder>(getContext()) {

                        @Override
                        public int layoutResId(int viewType) {
                            return R.layout.sharemall_item_home_nav;
                        }

                        @Override
                        public BaseViewHolder holderInstance(ViewDataBinding binding) {
                            return new BaseViewHolder<NewFloorEntity.FloorDataBean>(binding) {
                                @Override
                                public void doClick(View view) {
                                    super.doClick(view);
                                    floorClick(item.getFloor_data().get(position));
                                }
                            };
                        }
                    };

                    binding.rvFloor.setAdapter(picAdapter);
                    if (!Strings.isEmpty(item.getFloor_data())) {
                        picAdapter.setData(item.getFloor_data());
                    }
                }
                //楼层多图的显示
                else if (getBinding() instanceof SharemallItemFloorMultiPicBinding) {
                    SharemallItemFloorMultiPicBinding binding = (SharemallItemFloorMultiPicBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    if (screenWidth == 0) {
                        screenWidth = ScreenUtils.getScreenWidth();
                    }
                    FlexboxLayout flexboxLayout = binding.flexboxLayout;
                    flexboxLayout.removeAllViews();
                    flexboxLayout.setFlexWrap(FlexWrap.NOWRAP);
                    for (final NewFloorEntity.FloorDataBean bean : item.getFloor_data()) {
                        ImageView imageView;
                        int beanWidth = ViewGroup.LayoutParams.MATCH_PARENT;
                        int beanHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                        if (bean.getImg_width() > 0) {
                            imageView = new ImageView(getContext());
                            beanWidth = (int) (screenWidth / item.getFloor_data().size());
                            beanHeight = (int) (beanWidth * (bean.getImg_height() / bean.getImg_width()));
                        } else {
                            imageView = new ResizableImageView(getContext());
                        }
                        imageView.setImageResource(R.drawable.baselib_bg_default_pic);
                        imageView.setOnClickListener((View view) -> floorClick(bean));
                        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(beanWidth, beanHeight);
                        flexboxLayout.addView(imageView, lp);
                        GlideShowImageUtils.gifload(getContext(), bean.getImg_url(), imageView);
                    }
                }
                //楼层热门商品的显示
                else if (getBinding() instanceof SharemallItemFloorGoodsBinding) {
                    SharemallItemFloorGoodsBinding binding = (SharemallItemFloorGoodsBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    binding.tvAll.setVisibility(View.VISIBLE);
                    switch (item.getType()) {
                        case FLOOR_GOODS_HOT:
                            binding.tvTitle.setText(context.getString(R.string.sharemall_hot_commodity));
                            break;
                        case FLOOR_GOODS_NEWS:
                            binding.tvTitle.setText(context.getString(R.string.sharemall_new_arrivals));
                            break;
                        case FLOOR_STORE:
                            binding.tvAll.setVisibility(View.GONE);
                            binding.tvTitle.setText(context.getString(R.string.sharemall_recommend_shop));
                            break;
                        case FLOOR_CUSTOM_GOODS:
                            binding.tvAll.setVisibility(View.GONE);
                            binding.tvTitle.setText(item.getTitle());
                            break;
                        default:
                            break;
                    }

                    binding.rvFloor.setLayoutManager(new LinearLayoutManager(getContext()));

                    //店铺列表
                    if (item.getType() == FLOOR_STORE) {
                        BaseRViewAdapter<StoreEntity, BaseViewHolder> storeAdapter = new BaseRViewAdapter<StoreEntity, BaseViewHolder>(getContext()) {
                            @Override
                            public int layoutResId(int position) {
                                return R.layout.sharemall_item_home_store;
                            }

                            @Override
                            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                return new BaseViewHolder<StoreEntity>(binding) {

                                    @Override
                                    public void bindData(StoreEntity item) {
                                        super.bindData(item);
                                    }

                                    @Override
                                    public void doClick(View view) {
                                        super.doClick(view);
                                        StoreDetailActivity.start(getContext(), getItem(position).getId());
                                    }
                                };
                            }
                        };
                        binding.rvFloor.setAdapter(storeAdapter);
                        if (!Strings.isEmpty(item.getShops_list())) {
                            storeAdapter.setData(item.getShops_list());
                        }
                    }
                    //商品列表
                    else {
                        GoodsListAdapter goodsAdapter = new GoodsListAdapter(getContext());
                        binding.rvFloor.setAdapter(goodsAdapter);
                        if (!Strings.isEmpty(item.getGoods_data())) {
                            goodsAdapter.setData(item.getGoods_data());
                        }
                    }
                }
                //优惠券显示
                else if (getBinding() instanceof SharemallItemFloorCouponBinding) {
                    SharemallItemFloorCouponBinding binding = (SharemallItemFloorCouponBinding) getBinding();
                    setMargins(binding.getRoot(), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getTop()), com.netmi.baselibrary.utils.DensityUtils.dp2px(item.getBottom()));
                    FlexboxLayout flexboxLayout = binding.flCoupon;
                    flexboxLayout.removeAllViews();
                    flexboxLayout.setFlexWrap(FlexWrap.NOWRAP);
                    if (!Strings.isEmpty(item.getCouponList())) {
                        for (CouponEntity bean : item.getCouponList()) {

                            SharemallItemFloorCouponItemBinding
                                    couponBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_item_floor_coupon_item, flexboxLayout, false);
                            couponBinding.tvCouponName.setText(String.format(getContext().getString(R.string.sharemall_format_coupon_info), bean.getCondition_num(), bean.getDiscount_num()));
                            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT);
                            if (flexboxLayout.getFlexItemCount() == 0) {
                                //第一个完全显示
                                lp.setMinWidth((int) couponBinding.tvCouponName.getPaint().measureText(couponBinding.tvCouponName.getText().toString()) + DensityUtils.dp2px(22));
                            }
                            flexboxLayout.addView(couponBinding.getRoot(), lp);

                            //只显示2个
                            if (flexboxLayout.getFlexItemCount() > 1) break;
                        }
                    }

                }
                super.bindData(item);
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (view.getId() == R.id.ll_coupon) {
                    doListCoupon(getItem(position), true);
                } else if (view.getId() == R.id.tv_all) {
                    NewFloorEntity.FloorDataBean dataBean = new NewFloorEntity.FloorDataBean();
                    switch (getItem(position).getType()) {
                        case FLOOR_GOODS_HOT:
                            dataBean.setParam(shopId);
                            dataBean.setType(String.valueOf(FLOOR_TYPE_GOODS_HOT));
                            break;
                        case FLOOR_GOODS_NEWS:
                            dataBean.setParam(shopId);
                            dataBean.setType(String.valueOf(FLOOR_TYPE_GOODS_NEWS));
                            break;
                        case FLOOR_STORE:
                            dataBean.setType(String.valueOf(FLOOR_TYPE_GOODS_RECOMMEND));
                            break;
                        default:
                            break;
                    }
                    if (dataBean.getType() > 0) {
                        floorClick(dataBean);
                    }
                }
            }
        };
    }

    private void setMargins(View v, int t, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(0, t, 0, b);
            v.requestLayout();
        }
    }

    public void showError(String message) {
        hideProgress();
        ToastUtils.showShort(message);
    }

    private LoadingDialog loadingDialog;

    public void showProgress() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
        }
        loadingDialog.showProgress("");
    }

    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.hideProgress();
            loadingDialog = null;
        }
    }

    public interface HideProgressListener {
        void hideProgressByCallBack();
    }

    private void doListGoodsData(final NewFloorEntity entity) {
        if (lifecycleFragment == null) {
            ToastUtils.showShort(context.getString(R.string.sharemall_initialize_page_first));
        }
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(0, entity.getNums() > 0 ? entity.getNums() : 2 * Constant.PAGE_ROWS, null, null, null,
                        null, null, null, null, shopId,
                        entity.getType() == FLOOR_GOODS_HOT ? "1" : "0", entity.getType() == FLOOR_GOODS_NEWS ? "1" : "0", null, null,
                        entity.getType() == FLOOR_CUSTOM_GOODS ? entity.getGoods_list() : null)
                .compose(RxSchedulers.compose())
                .compose(((RxFragment) lifecycleFragment).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageEntity<GoodsListEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            entity.setGoods_data(data.getData().getList());
                        }
                    }

                    @Override
                    public void onComplete() {
                        requestResult();
                    }
                });
    }

    private void doListStore(final NewFloorEntity entity) {
        if (lifecycleFragment == null) {
            ToastUtils.showShort("请先初始化页面");
        }
        RetrofitApiFactory.createApi(StoreApi.class)
                .listStore(0, entity.getNums() > 0 ? entity.getNums() : 2 * Constant.PAGE_ROWS, "2", null)
                .compose(RxSchedulers.compose())
                .compose((lifecycleFragment).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageEntity<StoreEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<StoreEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            entity.setShops_list(data.getData().getList());
                        }
                    }

                    @Override
                    public void onComplete() {
                        requestResult();
                    }
                });
    }

    //优店铺惠券
    private void doListCoupon(final NewFloorEntity entity, boolean showDialog) {
        if (showDialog) {
            showProgress();
        }
        RetrofitApiFactory.createApi(CouponApi.class)
                .listCoupon("shop", shopId, null, 0, Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((lifecycleFragment).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<PageEntity<CouponEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        onComplete();
                    }

                    @Override
                    public void onNext(@NonNull BaseData<PageEntity<CouponEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                                entity.setCouponList(data.getData().getList());
                            }
                        } else {
//                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (showDialog) {
                            hideProgress();
                            GoodsCouponDialogFragment.newInstance((ArrayList<CouponEntity>) entity.getCouponList()).show(fragmentManager, "Coupon");
                        } else {
                            requestResult();
                        }
                    }
                });
    }

}
