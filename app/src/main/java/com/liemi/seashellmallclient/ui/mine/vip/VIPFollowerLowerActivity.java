package com.liemi.seashellmallclient.ui.mine.vip;

import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPMemberEntity;
import com.liemi.seashellmallclient.data.event.VipFollowerNumEvent;
import com.liemi.seashellmallclient.data.event.VipFollowerSecondNumEvent;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityVipfollowerLowerBinding;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class VIPFollowerLowerActivity extends BaseActivity<ActivityVipfollowerLowerBinding> {

    private String uid;

    @Override
    protected int getContentView() {
        return R.layout.activity_vipfollower_lower;
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
        getTvTitle().setText("成员昵称");
        uid = getIntent().getStringExtra("uid");
        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        //1:直系会员列表 2:店铺列表
        fragmentList.add(VIPFollowerSecondFragment.newInstance(uid));
        fragmentList.add(VIPSecondShopFragment.newInstance(uid));
        mBinding.tlTitle.setViewPager(mBinding.vpContent,
                new String[]{"直推用户" , "直推商家"},
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
    public void switchTab(VipFollowerSecondNumEvent event) {
        mBinding.tlTitle.showNum(event.position,event.num);
    }

}
