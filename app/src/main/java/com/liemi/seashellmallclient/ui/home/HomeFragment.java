package com.liemi.seashellmallclient.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CouponApi;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.entity.HomeDialogEntity;
import com.liemi.seashellmallclient.data.entity.coupon.CouponEntity;
import com.liemi.seashellmallclient.data.entity.floor.FloorTypeEntity;
import com.liemi.seashellmallclient.databinding.FragmentHomeBinding;
import com.liemi.seashellmallclient.ui.category.CategoryActivity;
import com.liemi.seashellmallclient.ui.mine.message.RecentContactsActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.CaptureQRCodeActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.WalletTransferActivity;
import com.liemi.seashellmallclient.ui.shopcart.ShopCartActivity;
import com.liemi.seashellmallclient.widget.HomeDialog;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.PrefCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.widget.SlidingTextTabLayout;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    public static final String TAG = HomeFragment.class.getName();
    private static final String DIALOG_TIME = "dialog_time";

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        mBinding.setDoClick(this);
    }

    @Override
    protected void initData() {
        doGetFloorTypeData();
        doHomeDialog();
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
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.iv_message) {
            JumpUtil.overlay(getContext(), RecentContactsActivity.class);
        } else if (i == R.id.tv_search) {
            JumpUtil.overlay(getContext(), SearchActivity.class);
        } else if (i == R.id.iv_scan_qr) {
            Bundle bundle = new Bundle();
            JumpUtil.startForResult(getActivity(), CaptureQRCodeActivity.class, 1002,bundle);
        } else if (i == R.id.iv_shop_car) {
            JumpUtil.overlay(getContext(), ShopCartActivity.class);
        }
    }

    private void doGetFloorTypeData() {
        RetrofitApiFactory.createApi(HomeApi.class)
                .doGetFloorType("1", "0")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<FloorTypeEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<List<FloorTypeEntity>> data) {
                        String[] titles = new String[data.getData().size()];
                        final ArrayList<Fragment> fragments = new ArrayList<>(data.getData().size());
                        for (int i = 0; i < data.getData().size(); i++) {
                            FloorTypeEntity dataBean = data.getData().get(i);
                            titles[i] = dataBean.getName();
                            fragments.add(HomeCategoryFragment.newInstance(dataBean.getPosition_code(), null));
                        }
                        mBinding.vpContent.setOffscreenPageLimit(3);
                        mBinding.vpContent.setAdapter(new SlidingTextTabLayout.InnerPagerAdapter(getChildFragmentManager(), fragments, titles));
                        mBinding.stTitle.setViewPager(mBinding.vpContent);
                    }

                });

    }

    private void doHomeCouponDialog() {
        RetrofitApiFactory.createApi(CouponApi.class)
                .getCouponDialog("param")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<CouponEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<CouponEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            HomeCouponDialogFragment
                                    .newInstance((ArrayList<CouponEntity>) data.getData().getList())
                                    .show(getChildFragmentManager(), "coupon");
                        }
                    }
                });
    }

    private void doHomeDialog() {
        RetrofitApiFactory.createApi(HomeApi.class)
                .doHomeDialog("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<HomeDialogEntity>>() {

                    boolean loadCoupon = true;

                    @Override
                    public void onSuccess(BaseData<HomeDialogEntity> data) {
                        if (System.currentTimeMillis() - (long) PrefCache.getData(DIALOG_TIME, (long) 0) > DateUtil.DAY) {
                            PrefCache.putData(DIALOG_TIME, DateUtil.strToLong(DateUtil.getCurrentDate2()));
                            HomeDialog homeDialog = new HomeDialog.Builder(getContext())
                                    .setData(data.getData())
                                    .setCancelOutside(true)
                                    .create();
                            homeDialog.setOnDismissListener((DialogInterface dialogInterface) -> doHomeCouponDialog());
                            homeDialog.show();
                            loadCoupon = false;
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (loadCoupon) {
                            doHomeCouponDialog();
                        }
                    }

                });
    }
}
