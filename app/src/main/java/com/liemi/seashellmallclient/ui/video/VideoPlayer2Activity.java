package com.liemi.seashellmallclient.ui.video;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityVideoPlayer2Binding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.seashellmallclient.ui.video.VideoPlayerActivity.VIDEO_URL;

public class VideoPlayer2Activity extends BaseActivity<ActivityVideoPlayer2Binding> {
    private JzvdStd jVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_video_player2;
    }

    @Override
    protected void initUI() {
        if (!getIntent().hasExtra(VIDEO_URL)) {
            ToastUtils.showShort(getString(R.string.lack_param));
            finish();
            return;
        }
        jVideo=mBinding.videoplayer;
        jVideo.setUp(getIntent().getStringExtra(VIDEO_URL), "", Jzvd.SCREEN_WINDOW_FULLSCREEN);
        mBinding.videoplayer.fullscreenButton.setVisibility(View.GONE);
        mBinding.videoplayer.backButton.setOnClickListener(this::doClick);
//        jVideo.onStatePlaying();
    }


    @Override
    public void setBarColor() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId()==R.id.back){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        //Change these two variables back
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
