package com.liemi.seashellmallclient.ui.mine.bbs;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.ArticleApi;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.entity.article.ArticleClassEntity;
import com.liemi.seashellmallclient.data.entity.floor.FloorTypeEntity;
import com.liemi.seashellmallclient.databinding.ActivityBbsBinding;
import com.liemi.seashellmallclient.ui.home.HomeCategoryFragment;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.widget.SlidingTextTabLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

public class BBsActivity extends BaseActivity<ActivityBbsBinding> {

    private List<Fragment> fragments;

    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_bbs;
    }

    public void initImmersionBar() {
        ImmersionBarUtils.setStatusBar(this, true, R.color.white);
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        getTvTitle().setText(ResourceUtil.getString(R.string.sharemall_bbs));
        fragments = new ArrayList<>();
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        mBinding.vpContent.setAdapter(pagerAdapter);
    }

    @Override
    protected void initData() {
        doGetBBsTypeData();
    }

    private void doGetBBsTypeData() {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .getArticleClasses( "0")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<ArticleClassEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageEntity<ArticleClassEntity>> data) {
                        String[] titles = new String[data.getData().getList().size()];
                        fragments.clear();
                        for (int i = 0; i < data.getData().getList().size(); i++) {
                            ArticleClassEntity dataBean = data.getData().getList().get(i);
                            titles[i] = dataBean.getName();
                            fragments.add(BBsFragment.newInstance(dataBean.getId(),dataBean.getName()));
                        }
                        pagerAdapter.notifyDataSetChanged();
                        mBinding.vpContent.setOffscreenPageLimit(fragments.size());
                        mBinding.stTitle.setViewPager(mBinding.vpContent,titles);
                    }

                });
    }
}
