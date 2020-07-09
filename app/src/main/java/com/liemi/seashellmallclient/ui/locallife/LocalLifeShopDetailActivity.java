package com.liemi.seashellmallclient.ui.locallife;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import cn.jzvd.Jzvd;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityLocalLifeShopDetailBinding;
import com.liemi.seashellmallclient.ui.BaseBlackTitleActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;

public class LocalLifeShopDetailActivity extends BaseBlackTitleActivity<ActivityLocalLifeShopDetailBinding> {


    @Override
    protected int getContentView() {
        return R.layout.activity_local_life_shop_detail;
    }

    public static void start(Context context, String shopId) {
        if (!TextUtils.isEmpty(shopId)) {
            JumpUtil.overlay(context, LocalLifeShopDetailActivity.class, GoodsParam.STORE_ID, shopId);
        } else {
            ToastUtils.showShort(R.string.baselib_not_data);
        }
    }

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this,true);
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        String shop_id = intent.getStringExtra(SHOP_ID);
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID,shop_id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LocalLifeShopDetailFragment localLifeShopDetailFragment = new LocalLifeShopDetailFragment();
        localLifeShopDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fl_content,localLifeShopDetailFragment);
        fragmentTransaction.commitAllowingStateLoss();
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
}
