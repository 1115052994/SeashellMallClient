package com.liemi.seashellmallclient.ui.store;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityStoreSearchBinding;
import com.liemi.seashellmallclient.ui.category.FilterGoodsFragment;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;

public class StoreSearchActivity extends BaseActivity<ActivityStoreSearchBinding> implements SearchKeyWord {
    private FilterGoodsFragment goodsFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_store_search;
    }

    @Override
    protected void initUI() {
        mBinding.etKeyword.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideKeyboard(mBinding.etKeyword);
                String content = mBinding.etKeyword.getText().toString().trim();
                if (Strings.isEmpty(content)) {
                    return true;
                }
                doSearch();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void initData() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.rl_fragment_goods,
                goodsFragment = FilterGoodsFragment.newInstance(getIntent().getStringExtra(GoodsParam.MC_ID),
                        getIntent().getStringExtra(GoodsParam.MC_HOT_GOODS),
                        getIntent().getStringExtra(GoodsParam.MC_NEW_GOODS),
                        getIntent().getStringExtra(GoodsParam.STORE_ID)));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public String getEtSearchText() {
        return mBinding.etKeyword.getText().toString();
    }

    private void doSearch() {
        KeyboardUtils.hideKeyboard(mBinding.etKeyword);
        mBinding.etKeyword.setSelection(getEtSearchText().length());
        goodsFragment.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_category) {
            JumpUtil.startForResult(this, StoreCategoryActivity.class, 0, new FastBundle().putString(GoodsParam.STORE_ID, getIntent().getStringExtra(GoodsParam.STORE_ID)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            goodsFragment.resetParamSearch(data.getStringExtra(GoodsParam.MC_ID));
        }
    }
}
