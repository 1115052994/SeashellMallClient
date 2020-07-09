package com.liemi.seashellmallclient.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.BannerJumpEntity;
import com.liemi.seashellmallclient.databinding.ActivitySplashBinding;
import com.liemi.seashellmallclient.ui.login.LoginHomeActivity;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    public static final String GUIDE_DISPLAY = "guideDisplay";
    private boolean isIn;

    @Override
    public void setBarColor() {
        ImmersionBar.with(this)
                .reset()
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUI() {
        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    protected void initData() {
        doGetAdvertising();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_jump) {
            disposable();
            toMainActivity();
        }
    }

    private void disposable() {
        if (countdownDisposable != null) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disposable();
    }


    private void toMainActivity() {
        if (isIn) {
            return;
        }
      /*  if (!TextUtils.equals((String) SPs.get(getContext(), GUIDE_DISPLAY, ""), AppUtils.getAppVersionName())) {
            SPs.put(getContext(), GUIDE_DISPLAY, AppUtils.getAppVersionName());
            JumpUtil.overlay(getContext(), GuidePageActivity.class);
        } else {*/
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), MainActivity.class);
            }
//        }

        finish();
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        isIn = true;
    }

    private Disposable countdownDisposable;

    private void countdown(int time) {
        if (time <= 0) {
            toMainActivity();
            return;
        }

        setTextTimeCount(time);

        final int countTime = time;
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .map((aLong) -> countTime - aLong.intValue())
                .take(countTime + 1)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        countdownDisposable = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        setTextTimeCount(integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        toMainActivity();
                    }
                });
    }

    private void setTextTimeCount(int time) {
        mBinding.tvJump.setText((time + getString(R.string.sharemall_jump)));
    }

    private void doGetAdvertising() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getAdvertising("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<BannerEntity>>() {
                    @Override
                    public void onSuccess(BaseData<BannerEntity> data) {
                        if (dataExist(data)) {
                            GlideShowImageUtils.displayNetImage(getContext(), data.getData().getImg_url(), mBinding.ivBg);
                            mBinding.ivLogo.setVisibility(View.GONE);
                            mBinding.tvJump.setVisibility(View.VISIBLE);
                            int time = Strings.toInt(data.getData().getRemark());
                            countdown(time);
                            mBinding.ivBg.setOnClickListener(v -> {
                                toMainActivity();
                                new BannerJumpEntity().toJump(getContext(), data.getData());
                            });
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mBinding.tvJump.getVisibility() == View.GONE) {
                            toMainActivity();
                        }
                    }
                });
    }
}
