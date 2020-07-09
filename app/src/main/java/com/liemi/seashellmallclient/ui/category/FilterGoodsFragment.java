package com.liemi.seashellmallclient.ui.category;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ccj.poptabview.FilterConfig;
import com.ccj.poptabview.base.BaseFilterTabBean;
import com.ccj.poptabview.bean.FilterTabBean;
import com.ccj.poptabview.loader.ResultLoaderImp;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentFilterGoodsBinding;
import com.liemi.seashellmallclient.ui.good.GoodsListAdapter;
import com.liemi.seashellmallclient.ui.store.SearchKeyWord;
import com.liemi.seashellmallclient.widget.filter.GoodsPopLoaderImp;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;;import static com.liemi.seashellmallclient.data.param.GoodsParam.SORT_ASC;
import static com.liemi.seashellmallclient.data.param.GoodsParam.SORT_DESC;

public class FilterGoodsFragment extends BaseXRecyclerFragment<SharemallFragmentFilterGoodsBinding, GoodsListEntity> {

    private String mcid, isHot, isNew, sort_name, sort_type = SORT_ASC, storeId;

    private List<String> filters;

    public static FilterGoodsFragment newInstance(String mcid, String isHot, String isNew, String storeId) {
        FilterGoodsFragment f = new FilterGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GoodsParam.MC_ID, mcid);
        bundle.putString(GoodsParam.MC_HOT_GOODS, isHot);
        bundle.putString(GoodsParam.MC_NEW_GOODS, isNew);
        bundle.putString(GoodsParam.STORE_ID, storeId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_filter_goods;
    }

    @Override
    protected void initUI() {
        mBinding.setDoClick(this);

        if (getKeyWord() != null) {
            adapter = new GoodsListAdapter(getContext(), xRecyclerView, R.layout.baselib_include_no_data_view2,
                    new EmptyLayoutEntity(R.mipmap.sharemall_ic_goods_empty, getString(R.string.sharemall_search_no_result)));
        } else {
            adapter = new GoodsListAdapter(getContext());
        }

        xRecyclerView = mBinding.xrvGoods;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(true);
        addMyMethod();
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mcid = getArguments().getString(GoodsParam.MC_ID);
            isHot = getArguments().getString(GoodsParam.MC_HOT_GOODS);
            isNew = getArguments().getString(GoodsParam.MC_NEW_GOODS);
            storeId = getArguments().getString(GoodsParam.STORE_ID);
        }
        mBinding.ptvComprehensive.setClickedItem(0, 0);
        xRecyclerView.refresh();
    }

    private void addMyMethod() {
        filters = Arrays.asList(getString(R.string.sharemall_comprehensive),
                getString(R.string.sharemall_popularity),
                getString(R.string.sharemall_commission_asc),
                getString(R.string.sharemall_commission_desc));

        List<BaseFilterTabBean> filterTab = new ArrayList<>();
        filterTab.add(getTabBean(filters.get(0)));
        filterTab.add(getTabBean(filters.get(1)));

        if (ShareMallUserInfoCache.get().isVip()) {
            filterTab.add(getTabBean(filters.get(2)));
            filterTab.add(getTabBean(filters.get(3)));
        }

        mBinding.ptvComprehensive
                .setPopEntityLoader(new GoodsPopLoaderImp())
                .setResultLoader(new ResultLoaderImp())
                .addFilterItem(getString(R.string.sharemall_comprehensive), filterTab, FilterConfig.TYPE_POPWINDOW_SINGLE, FilterConfig.FILTER_TYPE_SINGLE)
                .setOnPopTabSetListener((int index, String lable, Object params, String value) -> {
                    if (TextUtils.isEmpty(value)) {
                        return;
                    }
                    if (TextUtils.equals(value, filters.get(0))) {
                        sort_type = null;
                        sort_name = null;
                    } else if (TextUtils.equals(value, filters.get(1))) {
                        sort_type = SORT_DESC;
                        sort_name = "popularity";
                    } else if (TextUtils.equals(value, filters.get(2))) {
                        sort_type = SORT_ASC;
                        sort_name = "commission";
                    } else if (TextUtils.equals(value, filters.get(3))) {
                        sort_type = SORT_DESC;
                        sort_name = "commission";
                    }
                    controlFilter(mBinding.llComprehensive);
                });
    }

    private FilterTabBean getTabBean(String title) {
        FilterTabBean tabBean = new FilterTabBean();
        tabBean.setTab_name(title);
        return tabBean;
    }

    public void resetParamSearch(String mcid) {
        this.mcid = mcid;
        refresh();
    }

    public void refresh() {
        xRecyclerView.refresh();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.ll_sales) {
            sort_type = mBinding.tvSales.isSelected() ? changeSortType() : SORT_DESC;
            sort_name = "deal_num";
            controlFilter(view);
        } else if (i == R.id.ll_price) {
            sort_type = mBinding.tvPrice.isSelected() ? changeSortType() : SORT_ASC;
            sort_name = "price";
            controlFilter(view);
        }
    }

    private String changeSortType() {
        if (TextUtils.equals(sort_type, SORT_ASC)) {
            sort_type = SORT_DESC;
        } else {
            sort_type = SORT_ASC;
        }
        return sort_type;
    }

    private void controlFilter(View view) {

        int selectColor = getResources().getColor(AppConfigCache.get().getAppTheme().getColor_price(R.color.bgColor)),
                normalColor = getResources().getColor(R.color.gray_99);

        mBinding.tvSales.setSelected(view.getId() == R.id.ll_sales);
        mBinding.tvPrice.setSelected(view.getId() == R.id.ll_price);

        mBinding.tvSales.setTextColor(view.getId() == R.id.ll_sales ? selectColor : normalColor);
        mBinding.tvPrice.setTextColor(view.getId() == R.id.ll_price ? selectColor : normalColor);

        mBinding.ivSales.setImageResource(R.mipmap.sharemall_ic_sort_price);
        mBinding.ivPrice.setImageResource(R.mipmap.sharemall_ic_sort_price);

        if (view.getId() != R.id.ll_comprehensive) {

            ImageView ivSort = view.getId() == R.id.ll_price ?
                    mBinding.ivPrice : mBinding.ivSales;

            if (TextUtils.equals(sort_type, SORT_ASC)) {
                ivSort.setImageResource(R.mipmap.sharemall_ic_sort_price_up);
            } else {
                ivSort.setImageResource(R.mipmap.sharemall_ic_sort_price_down);
            }

            resetPopFilterView();
        }

        refresh();
    }

    private void resetPopFilterView() {
        for (TextView tabView : mBinding.ptvComprehensive.getTextViewLists()) {
            tabView.setTextColor(getResources().getColor(R.color.gray_99));
            tabView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.sharemall_ic_sort_price, 0);
        }
    }

    private String getKeyWord() {
        return getActivity() instanceof SearchKeyWord ? ((SearchKeyWord) getActivity()).getEtSearchText() : null;
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, mcid,
                        getKeyWord(), null, null, null, null, storeId,
                        isHot, isNew, sort_name, sort_type, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<GoodsListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        showData(data.getData());
                    }
                });

    }


}
