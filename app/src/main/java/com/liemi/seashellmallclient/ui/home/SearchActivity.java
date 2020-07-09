package com.liemi.seashellmallclient.ui.home;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.google.gson.Gson;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallActivityMallSearchBinding;
import com.liemi.seashellmallclient.ui.category.FilterGoodsFragment;
import com.liemi.seashellmallclient.ui.store.SearchKeyWord;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.PrefCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class SearchActivity extends BaseActivity<SharemallActivityMallSearchBinding> implements SearchKeyWord {

    private SearchRecordAdapter searchAdapter;
    private List<String> searchRecords;
    private String storeId;
    private FilterGoodsFragment goodsFragment;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_mall_search;
    }

    @Override
    protected void initUI() {
        mBinding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mBinding.svSearchRecord.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.etKeyword.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideKeyboard(mBinding.etKeyword);
                String content = mBinding.etKeyword.getText().toString().trim();
                if (Strings.isEmpty(content)) {
                    return true;
                }

                reqSearch();
                return true;
            }
            return false;
        });

        storeId = getIntent().getStringExtra(GoodsParam.STORE_ID);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.rl_fragment_goods,
                goodsFragment = FilterGoodsFragment.newInstance(getIntent().getStringExtra(GoodsParam.MC_ID),
                        getIntent().getStringExtra(GoodsParam.MC_HOT_GOODS),
                        getIntent().getStringExtra(GoodsParam.MC_NEW_GOODS),
                        storeId));
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void reqSearch() {
        String keyword = getEtSearchText();
        if (Strings.isEmpty(keyword)) {
            return;
        }
        Iterator<String> iterator = searchRecords.iterator();
        while (iterator.hasNext()) {
            if (keyword.equals(iterator.next())) {
                iterator.remove();
            }
        }
        searchRecords.add(0, keyword);
        if (searchRecords.size() > 6) {
            searchRecords = searchRecords.subList(0, 6);
        }
        notifySearchHistoryAdapter();
        PrefCache.putData((UserInfoCache.get(ShareMallUserInfoEntity.class)).getUid() + "searchHistory", new Gson().toJson(searchRecords));
        KeyboardUtils.hideKeyboard(mBinding.etKeyword);
        mBinding.etKeyword.setSelection(getEtSearchText().length());
        mBinding.svSearchRecord.setVisibility(View.GONE);

        goodsFragment.refresh();
    }


    @Override
    protected void initData() {

        String historyJson = (String) PrefCache.getData((UserInfoCache.get(ShareMallUserInfoEntity.class)).getUid() + "searchHistory", "");
        if (Strings.isEmpty(historyJson)) {
            searchRecords = new ArrayList<>();
        } else {
            searchRecords = new Gson().fromJson(historyJson, ArrayList.class);
        }
        searchAdapter = new SearchRecordAdapter(searchRecords);
        mBinding.idLabelHistory.setAdapter(searchAdapter);
        mBinding.idLabelHistory.setOnTagClickListener((View view, int position, FlowLayout parent) -> {
            mBinding.etKeyword.setText(searchAdapter.getItem(position));
            reqSearch();
            return false;
        });
        notifySearchHistoryAdapter();
        doListHotLabel();
    }

    private void notifySearchHistoryAdapter() {
        searchAdapter.notifyDataChanged();
        mBinding.tvClearHistory.setVisibility(searchAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public String getEtSearchText() {
        return mBinding.etKeyword.getText().toString().trim();
    }


    private void initHotLabel(List<String> data) {
        if (Strings.isEmpty(data)) return;

        mBinding.idLabelHot.setAdapter(new SearchRecordAdapter(data));
        mBinding.idLabelHot.setOnTagClickListener((View view, int position, FlowLayout parent) -> {
            mBinding.etKeyword.setText(data.get(position));
            reqSearch();
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        mBinding.etKeyword.setText("");
        KeyboardUtils.hideKeyboard(mBinding.etKeyword);
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_clear_history) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.sharemall_dialog_clear_record))
                    .setPositiveButton(getString(R.string.sharemall_confirm_ok), (DialogInterface dialog, int which) -> clearSearchKeyword())
                    .setNegativeButton(getString(R.string.sharemall_cancel), null)
                    .show();

        } else if (i == R.id.iv_search) {
            reqSearch();
        }
    }

    private void clearSearchKeyword() {
        if (searchRecords.isEmpty()) {
            return;
        }
        searchRecords.clear();
        PrefCache.putData((UserInfoCache.get(ShareMallUserInfoEntity.class)).getUid() + "searchHistory", new Gson().toJson(searchRecords));
        searchAdapter.notifyDataChanged();
    }

    protected void doListHotLabel() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getHotSearchList(storeId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<String>>>() {
                    @Override
                    public void onSuccess(BaseData<List<String>> data) {
                        initHotLabel(data.getData());
                    }
                });
    }

}
