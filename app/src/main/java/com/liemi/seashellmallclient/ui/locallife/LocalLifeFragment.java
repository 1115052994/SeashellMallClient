package com.liemi.seashellmallclient.ui.locallife;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.entity.BannerJumpEntity;
import com.liemi.seashellmallclient.data.entity.floor.NewFloorEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeShopEntity;
import com.liemi.seashellmallclient.data.entity.locallife.ShopOneCateEntity;
import com.liemi.seashellmallclient.data.entity.locallife.ShopTwoCateEntity;
import com.liemi.seashellmallclient.databinding.FragmentLocallifeBinding;
import com.liemi.seashellmallclient.databinding.ItemLocalLifeBinding;
import com.liemi.seashellmallclient.ui.home.floor.FloorClickUtils;
import com.liemi.seashellmallclient.widget.BannerViewHolder;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LocalLifeFragment extends BaseXRecyclerFragment<FragmentLocallifeBinding, LocalLifeShopEntity> {
    public static final String TAG = LocalLifeFragment.class.getName();
    private BaseRViewAdapter<ShopOneCateEntity, BaseViewHolder> cateAdapter;

    private BaseRViewAdapter<ShopTwoCateEntity, BaseViewHolder> childAdapter;

    private String category_id = "";
    private PopupWindow window;
    private BannerEntity bannerEntity;
    @Override
    protected int getContentView() {
        return R.layout.fragment_locallife;
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        mBinding.setListener(this);
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseRViewAdapter<LocalLifeShopEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_local_life;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<LocalLifeShopEntity>(binding) {
                    @Override
                    public void bindData(LocalLifeShopEntity item) {
                        super.bindData(item);
                        if (TextUtils.equals(item.getStar(),"0")){
                            getBinding().rbStarServer.setVisibility(View.GONE);
                        }else {
                            getBinding().rbStarServer.setVisibility(View.VISIBLE);
                            float level = FloatUtils.string2Float(item.getStar());
                            Float star;
                            if (TextUtils.isEmpty(item.getStar())){
                                star = 0f;
                            }else {
                                star = Float.valueOf(item.getStar());
                            }
                            getBinding().rbStarServer.setStarShop(star,false);
                        }
                        if (TextUtils.isEmpty(item.getFull_name())){
                            getBinding().tvAddress.setText("未开启定位");
                        }
                        String distance = item.getDistance();
                        double d = Strings.toDouble(distance);
                        if (d < 100) {
                            getBinding().tvDistance.setText(new DecimalFormat("0.0").format(d) + "m");
                        } else {
                            String format = new DecimalFormat("0.0").format(d / 1000);
                            getBinding().tvDistance.setText(format + "km");
                        }

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        JumpUtil.overlay(getActivity(), LocalLifeShopDetailActivity.class, ParamConstant.SHOP_ID, getItem(position).getId());
                    }

                    @Override
                    public ItemLocalLifeBinding getBinding() {
                        return (ItemLocalLifeBinding) super.getBinding();
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    private void floorClick(NewFloorEntity.FloorDataBean entity) {
        FloorClickUtils.getInstance().floorDataClick(getContext(), entity, "");
    }

    public void initImmersionBar() {
        ImmersionBarUtils.setStatusBar(this, true, R.color.bgColor);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initImmersionBar();
        }

    }

    @Override
    protected void initData() {
        mBinding.xrvData.refresh();
        doBanner();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_distance:  //距离
            case R.id.ll_sales_volume:  //销量
            case R.id.ll_high_praise:   //好评
                controlFilter(view);
                break;
            case R.id.ll_all:   //全部
                mBinding.llAll.setSelected(view.getId() == R.id.ll_all);
                showFilter();
                doListCategory();
                break;

        }
    }


    private void showFilter() {

        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.sharemall_item_locallife_category, null, false);
        RecyclerView rvGoodsCate = contentView.findViewById(R.id.rv_goods_cate);
        rvGoodsCate.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoodsCate.setAdapter(cateAdapter = new BaseRViewAdapter<ShopOneCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.locallife_item_category_one;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        mBinding.tvAll.setText(cateAdapter.getItem(position).getName());
                        category_id = cateAdapter.getItem(position).getMcid();
                        onRefresh();
                        ShopOneCateEntity entity = cateAdapter.getItem(position);
                        for (ShopOneCateEntity cateEntity : cateAdapter.getItems()) {
                            if (cateEntity == entity)
                                cateEntity.setCheck(true);
                            else
                                cateEntity.setCheck(false);
                        }
                        cateAdapter.notifyDataSetChanged();
                        if (Strings.isEmpty(entity.getSecond_category())|| entity.getSecond_category().size()<1){
                            onRefresh();
                            window.dismiss();
                        }else {
                            setChildAdapter(entity);
                            mBinding.tvAll.setText(cateAdapter.getItem(position).getName());
                            category_id = cateAdapter.getItem(position).getMcid();
                            onRefresh();
                        }

                    }
                };
            }
        });
        MyXRecyclerView rvGoods = contentView.findViewById(R.id.rv_goods);
        rvGoods.setPullRefreshEnabled(false);
        rvGoods.setLoadingMoreEnabled(false);
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoods.setAdapter(childAdapter = new BaseRViewAdapter<ShopTwoCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.locallife_item_category_two;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        mBinding.tvAll.setText(childAdapter.getItem(position).getName());
                        category_id = childAdapter.getItem(position).getMcid();
                        onRefresh();
                        window.dismiss();
                    }
                };
            }
        });
        window = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中： 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(mBinding.llSort, 0, 0);


    }

    public void showCategoryData(List<ShopOneCateEntity> pageEntity) {
        if (pageEntity != null
                && !pageEntity.isEmpty()) {
            ShopOneCateEntity first = pageEntity.get(0);
            first.setCheck(true);
            cateAdapter.setData(pageEntity);
            setChildAdapter(first);
        }
    }

    private void setChildAdapter(ShopOneCateEntity shopOneCateEntity) {
        if (shopOneCateEntity == null) {
            return;
        }
        childAdapter.setData(shopOneCateEntity.getSecond_category());
    }

    /*
     * 根据条件列表排序
     * */
    private void controlFilter(View view) {
        mBinding.tvDistance.setSelected(view.getId() == R.id.tv_distance);
        mBinding.llSalesVolume.setSelected(view.getId() == R.id.ll_sales_volume);
        mBinding.llHighPraise.setSelected(view.getId() == R.id.ll_high_praise);
        onRefresh();
    }

    @Override
    protected void doListData() {
        if (mBinding.tvDistance.isSelected()){
            doSortShopList("SORT_ASC","distance",category_id);
        }else if (mBinding.llSalesVolume.isSelected()){
            doSortShopList("SORT_DESC","order_num",category_id);
        }else if (mBinding.llHighPraise.isSelected()){
            doSortShopList("SORT_DESC","star",category_id);
        }else {
            doSortShopList("SORT_DESC","",category_id);
        }

    }

    private void doSortShopList(String sort,String sort_type,String category_id) {
        //((MainActivity)getActivity()).getLongitude(),((MainActivity)getActivity()).getLatitude()
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getShopList(PageUtil.toPage(startPage), Constant.PAGE_ROWS,((MainActivity)getActivity()).getLongitude(),((MainActivity)getActivity()).getLatitude(),sort,sort_type,category_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<LocalLifeShopEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<LocalLifeShopEntity>> data) {
                        if (dataExist(data)){
                            showData(data.getData());
                        }else {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }

    /*
     * 获取分类
     * */
    private void doListCategory() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getShopCategory("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<ShopOneCateEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<ShopOneCateEntity>> data) {
                        if (dataExist(data)){
                            List<ShopOneCateEntity> list = new ArrayList<>();
                            ShopOneCateEntity shopOneCateEntity = new ShopOneCateEntity();
                            List<ShopTwoCateEntity> secondList = new ArrayList<>();
                            ShopTwoCateEntity shopTwoCateEntity = new ShopTwoCateEntity();
                            shopTwoCateEntity.setName("全部");
                            secondList.add(shopTwoCateEntity);
//                            shopOneCateEntity.setSecond_category(secondList);
                            shopOneCateEntity.setName("全部");
                            list.add(shopOneCateEntity);
                            list.addAll(data.getData());
                            showCategoryData(list);
                        }else {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }

    private void doBanner() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .listBanner(127)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<BannerEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<BannerEntity>> data) {
                        if(data.getData() != null
                                && !Strings.isEmpty(data.getData().getList())) {
                            List<String> list = new ArrayList<>();
                            for (BannerEntity entity : data.getData().getList()) {
                                list.add(entity.getImg_url());
                            }
                            //轮播间隔
                            mBinding.cbBanner.setDelayedTime(6000);
                            //滑动速度
                            mBinding.cbBanner.setDuration(1800);
                            //指示器隐藏
                            mBinding.cbBanner.setIndicatorVisible(true);
                            // 可在project下查看lambda记录.txt
                            mBinding.cbBanner.setBannerPageClickListener((View view, int i) -> new BannerJumpEntity().toJump(getContext(), data.getData().getList().get(i)));
                            // 设置数据
                            mBinding.cbBanner.setPages(list, BannerViewHolder::new);
                            mBinding.cbBanner.start();
                        }
                    }
                });

    }
}
