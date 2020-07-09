package com.liemi.seashellmallclient.ui;

import android.databinding.ViewDataBinding;

import com.netmi.baselibrary.ui.BaseActivity;

public abstract class BaseBlackTitleActivity<T extends ViewDataBinding> extends BaseActivity<T> {


    @Override
    protected void initUI() {
    }

    //设置头部文字
    protected void initTitleStr(String titleInfo) {
        getTvTitle().setText(titleInfo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
