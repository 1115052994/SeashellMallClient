package com.liemi.seashellmallclient.ui.mine.verification;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityMyVerificationBinding;
import com.netmi.baselibrary.utils.ImmersionBarUtils;

import java.util.ArrayList;

public class MyVerificationActivity extends BaseMyVerificationActivity<ActivityMyVerificationBinding> {

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_verification;
    }

    @Override
    protected void initUI() {
        String[] titles=new String[]{getString(R.string.sharemall_all),
                getString(R.string.sharemall_order_wait_pay),getString(R.string.wait_verification),getString(R.string.sharemall_order_wait_appraise)};
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(MyVerificationFragment.newInstance(MyVerificationFragment.ALL,this));
        fragments.add(MyVerificationFragment.newInstance(MyVerificationFragment.WAIT_PAY,this));
        fragments.add(MyVerificationFragment.newInstance(MyVerificationFragment.WAIT_VERIFICATION,this));
        fragments.add(MyVerificationFragment.newInstance(MyVerificationFragment.WAIT_COMMENT,this));
        mBinding.tlVerification.setViewPager(mBinding.vpVerification,titles,this,fragments);
        mBinding.vpVerification.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //解决Fragment中使用EventBus的通知RecyclerView的adapter.notifyDataSetChanged只有拖动才刷新的问题
                View view = fragments.get(position).getView();
                if (view != null) {
                    view.requestLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

   /* @Override
    public void clickLeftButton(VerificationOrderEntity entity) {
        //跳转至评价页面
        showError("去评价");
    }

    @Override
    public void clickRightButton(VerificationOrderEntity entity) {
        showError("去付款");
    }*/
}
