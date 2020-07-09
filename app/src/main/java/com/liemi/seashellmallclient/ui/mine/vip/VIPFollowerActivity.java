package com.liemi.seashellmallclient.ui.mine.vip;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.cache.VipUserInfoCache;
import com.liemi.seashellmallclient.data.event.VipFollowerNumEvent;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityVipfollowerBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class VIPFollowerActivity extends BaseActivity<ActivityVipfollowerBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_vipfollower;
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
    protected void initUI() {
        getTvTitle().setText("团队管理");

        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        //1:直系会员列表 2:店铺列表
        fragmentList.add(VIPFollowerFragment.newInstance(1));
        fragmentList.add(VIPShopFragment.newInstance(2));
        mBinding.tlTitle.setViewPager(mBinding.vpContent,
                new String[]{"推广用户" , "推广商家"},
                this, fragmentList);
        if (getIntent().hasExtra(VipParam.showFans)) {  //显示粉丝
            mBinding.vpContent.setCurrentItem(1);
        }
    }

    @Override
    protected void initData() {

    }

    /**
     * 刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchTab(VipFollowerNumEvent event) {
        mBinding.tlTitle.showNum(event.position,event.num);
    }

}
