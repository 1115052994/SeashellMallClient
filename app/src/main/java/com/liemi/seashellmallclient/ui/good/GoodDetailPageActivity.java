package com.liemi.seashellmallclient.ui.good;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.jzvd.Jzvd;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.user.NotReadNumEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityGoodDetailPageBinding;
import com.liemi.seashellmallclient.ui.good.comment.GoodCommentFragment;
import com.liemi.seashellmallclient.ui.mine.message.RecentContactsActivity;
import com.liemi.seashellmallclient.ui.vip.VIPGiftDetailActivity;
import com.liemi.seashellmallclient.utils.CustomPopWindow;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;

public class GoodDetailPageActivity extends BaseActivity<ActivityGoodDetailPageBinding> {
    private GoodDetailFragment goodDetailFragment;

    private CustomPopWindow customPopWindow;

    private String itemId;

    public static void start(Context context, String itemId, Bundle bundle) {
        if (!TextUtils.isEmpty(itemId)) {
            int itemType = 0;
            if (bundle != null) {
                itemType = bundle.getInt(GoodsParam.ITEM_TYPE, 0);
            } else {
                bundle = new Bundle();
            }
            bundle.putString(GoodsParam.ITEM_ID, itemId);
            JumpUtil.overlay(context,
                    itemType == GoodsDetailedEntity.GOODS_VIP_GIFT ? VIPGiftDetailActivity.class : GoodDetailPageActivity.class,
                    bundle);
        } else {
            ToastUtils.showShort(R.string.baselib_not_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_good_detail_page;
    }

    @Override
    protected void initUI() {
        itemId = getIntent().getStringExtra(GoodsParam.ITEM_ID);
        if (TextUtils.isEmpty(itemId)) {
            ToastUtils.showShort(R.string.baselib_not_data);
            finish();
            return;
        }
        mBinding.stTabTitle.setViewPager(mBinding.vpContainer, getTitles(), this, getFragment());
        mBinding.stTabTitle.setCurrentTab(getIntent().getIntExtra(GoodsParam.CURRENT_TAB, 0));
    }

    private String[] getTitles() {

        return new String[]{getString(R.string.sharemall_good), getString(R.string.sharemall_comment)};

    }

    private ArrayList<Fragment> getFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(goodDetailFragment = new GoodDetailFragment());
        fragments.add(GoodCommentFragment.newInstance(itemId));
        return fragments;
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        //Change these two variables back
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (goodDetailFragment != null) {
            goodDetailFragment.onRestart();
        }
    }

    public GoodsDetailedEntity getGoodEntity() {
        if (goodDetailFragment != null) {
            return goodDetailFragment.getGoodEntity();
        }
        return null;
    }

    public void switchTab(int position) {
        mBinding.stTabTitle.setCurrentTab(position >= mBinding.stTabTitle.getTabCount() ? mBinding.stTabTitle.getTabCount() - 1 : position);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBackPressed();
        }
    }

}
