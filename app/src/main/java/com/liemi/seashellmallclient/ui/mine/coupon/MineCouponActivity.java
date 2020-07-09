package com.liemi.seashellmallclient.ui.mine.coupon;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.CouponParam;
import com.liemi.seashellmallclient.databinding.SharemallActivityTabViewpagerBinding;
import com.netmi.baselibrary.ui.BaseActivity;

import java.util.ArrayList;

public class MineCouponActivity extends BaseActivity<SharemallActivityTabViewpagerBinding> {

    @Override
    public void setBarColor() {
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.bgColor)
                .titleBar(R.id.top_view)
                .init();
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_tab_viewpager;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.person_my_coupon));
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MineCouponFragment.newInstance(CouponParam.COUPON_STATUS_NOT_USED_SHARING));
        fragmentList.add(MineCouponFragment.newInstance(CouponParam.COUPON_STATUS_TIMED));
        fragmentList.add(MineCouponFragment.newInstance(CouponParam.COUPON_STATUS_USED));

        mBinding.tlGroup.setViewPager(mBinding.vpGroup, new String[]{getString(R.string.sharemall_mine_coupon_type_un_use),
                getString(R.string.sharemall_mine_coupon_type_out_of_date),
                getString(R.string.sharemall_mine_coupon_type_uesd)}, getActivity(), fragmentList);
        mBinding.tlGroup.setTabSpaceEqual(true);
    }

    @Override
    protected void initData() {

    }
}
