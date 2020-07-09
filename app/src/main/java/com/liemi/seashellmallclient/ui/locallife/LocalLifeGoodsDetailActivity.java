package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityLocalLifeGoodsDetailBinding;
import com.liemi.seashellmallclient.ui.BaseBlackTitleActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;

import static com.liemi.seashellmallclient.data.ParamConstant.GOOD_ID;

public class LocalLifeGoodsDetailActivity extends BaseBlackTitleActivity<ActivityLocalLifeGoodsDetailBinding> {

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_local_life_goods_detail;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        String good_id = intent.getStringExtra(GOOD_ID);
        Bundle bundle = new Bundle();
        bundle.putString(GOOD_ID,good_id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LocalLifeGoodsDetailFragment localLifeGoodsDetailFragment = new LocalLifeGoodsDetailFragment();
        localLifeGoodsDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fl_content,localLifeGoodsDetailFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }
}
