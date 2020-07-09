package com.liemi.seashellmallclient.ui.mine.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallActivityFragmentBinding;
import com.netmi.baselibrary.ui.BaseActivity;

public class RecentContactsActivity extends BaseActivity<SharemallActivityFragmentBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_recent_contacts;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_system_notify));
    }

    @Override
    protected void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment, Fragment.instantiate(this, RecentContactsFragment.TAG));
        fragmentTransaction.commitAllowingStateLoss();
    }
}
