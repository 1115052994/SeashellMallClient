package com.liemi.seashellmallclient.ui.shopcart;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityShopCartBinding;
import com.liemi.seashellmallclient.databinding.SharemallActivityFragmentBinding;
import com.netmi.baselibrary.ui.BaseActivity;

public class ShopCartActivity extends BaseActivity<ActivityShopCartBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_cart;
    }

    @Override
    protected void initUI() {

        mBinding.getRoot().findViewById(R.id.ll_title).setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, new ShopCartFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }
}
