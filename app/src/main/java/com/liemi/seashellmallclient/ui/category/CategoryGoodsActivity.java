package com.liemi.seashellmallclient.ui.category;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityCategoryGoodsBinding;
import com.liemi.seashellmallclient.ui.home.SearchActivity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class CategoryGoodsActivity extends BaseActivity<ActivityCategoryGoodsBinding> {



    @Override
    protected int getContentView() {
        return R.layout.activity_category_goods;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra(GoodsParam.MC_NAME));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment,
                FilterGoodsFragment.newInstance(getIntent().getStringExtra(GoodsParam.MC_ID),
                        getIntent().getStringExtra(GoodsParam.MC_HOT_GOODS),
                        getIntent().getStringExtra(GoodsParam.MC_NEW_GOODS),
                        getIntent().getStringExtra(GoodsParam.STORE_ID)));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_search) {
            JumpUtil.overlay(getContext(), SearchActivity.class);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
