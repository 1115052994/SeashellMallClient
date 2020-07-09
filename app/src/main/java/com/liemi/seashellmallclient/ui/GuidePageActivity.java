package com.liemi.seashellmallclient.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityGuidePageBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;

public class GuidePageActivity extends BaseActivity<ActivityGuidePageBinding> {

    @Override
    public void setBarColor() {
        ImmersionBar.with(this)
                .reset()
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_guide_page;
    }

    @Override
    protected void initUI() {
        mBinding.vpGuide.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
               /* SharemallLayoutGuidePageBinding pageBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.sharemall_layout_guide_page, container, false);
                pageBinding.tvEnter.setOnClickListener((view) -> toMainAct());
                pageBinding.tvJump.setOnClickListener((view)-> toMainAct());
                switch (position) {
                    case 1:
                        pageBinding.ivBg.setImageResource(R.mipmap.sharemall_guide_page_2);
                        pageBinding.ivCircle1.setImageResource(R.mipmap.ic_guide_circle_normal);
                        pageBinding.ivCircle2.setImageResource(R.mipmap.ic_guide_circle_select);
                        pageBinding.ivCircle3.setImageResource(R.mipmap.ic_guide_circle_normal);
                        pageBinding.tvJump.setVisibility(View.GONE);
                        break;
                    case 2:
                        pageBinding.tvEnter.setVisibility(View.VISIBLE);
                        pageBinding.ivBg.setImageResource(R.mipmap.sharemall_guide_page_3);
                        pageBinding.ivCircle1.setImageResource(R.mipmap.ic_guide_circle_normal);
                        pageBinding.ivCircle2.setImageResource(R.mipmap.ic_guide_circle_normal);
                        pageBinding.ivCircle3.setImageResource(R.mipmap.ic_guide_circle_select);
                        pageBinding.tvJump.setVisibility(View.GONE);
                        break;
                }
                container.addView(pageBinding.getRoot());
                return pageBinding.getRoot();*/
                return null;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        mBinding.vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean isScroll;

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isScroll && position == 2) {//页面跳转
                    toMainAct();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    isScroll = mBinding.vpGuide.getCurrentItem() == 2;
                }
            }

        });

    }

    @Override
    protected void initData() {

    }

    private void toMainAct() {
        if (MApplication.getInstance().checkUserIsLogin()) {
            JumpUtil.overlay(getContext(), MainActivity.class);
        }
        finish();
    }
}
