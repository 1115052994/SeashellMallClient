package com.liemi.seashellmallclient.ui.mine.order;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.SharemallActivityTabViewpagerBinding;
import com.netmi.baselibrary.utils.ImmersionBarUtils;

import java.util.ArrayList;

import static com.liemi.seashellmallclient.data.param.OrderParam.ORDER_STATE;

public class MineOrderActivity extends BaseMineOrderActivity<SharemallActivityTabViewpagerBinding> {

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
        getTvTitle().setText(getString(R.string.sharemall_my_order));

        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OrderModuleFragment.newInstance(-1, this));
        fragmentList.add(OrderModuleFragment.newInstance(OrderParam.ORDER_WAIT_PAY, this));
        fragmentList.add(OrderModuleFragment.newInstance(OrderParam.ORDER_WAIT_SEND, this));
        fragmentList.add(OrderModuleFragment.newInstance(OrderParam.ORDER_WAIT_RECEIVE, this));
        fragmentList.add(OrderModuleFragment.newInstance(OrderParam.ORDER_WAIT_COMMENT, this));
        mBinding.tlGroup.setViewPager(mBinding.vpGroup,
                new String[]{getString(R.string.sharemall_all_order), getString(R.string.sharemall_order_wait_pay),
                        getString(R.string.sharemall_order_wait_send), getString(R.string.sharemall_order_wait_receive),
                        getString(R.string.sharemall_order_wait_appraise)}, this, fragmentList);
        mBinding.tlGroup.setCurrentTab(getIntent().getIntExtra(ORDER_STATE, -1) + 1);
        mBinding.vpGroup.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //解决Fragment中使用EventBus的通知RecyclerView的adapter.notifyDataSetChanged只有拖动才刷新的问题
                View view = fragmentList.get(position).getView();
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

}
