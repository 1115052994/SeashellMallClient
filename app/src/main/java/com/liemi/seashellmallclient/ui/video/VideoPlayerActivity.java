package com.liemi.seashellmallclient.ui.video;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.source.UrlSource;
import com.aliyun.player.source.VidSts;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.video.StsTokenEntity;
import com.liemi.seashellmallclient.databinding.ActivityVideoPlayerBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

public class VideoPlayerActivity extends BaseActivity<ActivityVideoPlayerBinding> {

    public static final String VOD_REGION = "cn-shanghai";

    private static final String VIDEO_ID = "videoId";

    public static final String VIDEO_URL = "videoUrl";

    private AliPlayer aliyunVodPlayer;

    private StsTokenEntity tokenEntity;

    private ImageView mPlayIcon;

    /**
     * 是否点击暂停
     */
    private boolean isPause = false;

    public static void start(Context context, String videoId) {
        if (!TextUtils.isEmpty(videoId)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(VIDEO_ID, videoId);
            JumpUtil.overlay(context, VideoPlayerActivity.class, bundle);
        } else {
            ToastUtils.showShort(R.string.sharemall_no_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initUI() {

        aliyunVodPlayer = AliPlayerFactory.createAliListPlayer(getApplicationContext());
        mPlayIcon=findViewById(R.id.iv_play_icon);
        mBinding.svPlayer.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.redraw();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(null);
            }
        });

    }

    @Override
    protected void initData() {
        doGetStsInfo();
    }

    private void startVideo() {
        if (getIntent().hasExtra(VIDEO_ID)) {
            aliyunVodPlayer.setDataSource(getVidSts(getIntent().getStringExtra(VIDEO_ID)));
            aliyunVodPlayer.prepare();
            aliyunVodPlayer.start();
            return;
        }

        if (getIntent().hasExtra(VIDEO_URL)){
            aliyunVodPlayer.setDataSource(getUrlSource(getIntent().getStringExtra(VIDEO_URL)));
            aliyunVodPlayer.prepare();
            aliyunVodPlayer.start();
            return;
        }

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        switch (view.getId()) {
            case R.id.rl_content:
                onPauseClick();
                break;
        }
    }

    /**
     * 视频暂停/恢复的时候使用，
     */
    public void onPauseClick() {
        if (isPause) {
            resumePlay();
        } else {
            pausePlay();
        }
    }

    /**
     * 暂停播放
     */
    private void pausePlay() {
        isPause = true;
        mPlayIcon.setVisibility(View.VISIBLE);
        aliyunVodPlayer.pause();
    }

    /**
     * 恢复播放
     */
    private void resumePlay() {
        isPause = false;
        mPlayIcon.setVisibility(View.GONE);
        aliyunVodPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aliyunVodPlayer.release();
    }


    private UrlSource getUrlSource(String videoUrl) {
        UrlSource urlSource=new UrlSource();
        urlSource.setUri(videoUrl);
        return urlSource;
    }

    private VidSts getVidSts(String videoId) {
        VidSts vidSts = new VidSts();
        if (tokenEntity != null) {
            vidSts.setAccessKeyId(tokenEntity.getCredentials().getAccessKeyId());
            vidSts.setAccessKeySecret(tokenEntity.getCredentials().getAccessKeySecret());
            vidSts.setSecurityToken(tokenEntity.getCredentials().getSecurityToken());
            vidSts.setRegion(VOD_REGION);
        }
        vidSts.setVid(videoId);
        return vidSts;
    }

    private void doGetStsInfo() {
       /* RetrofitApiFactory.createApi(VideoApi.class)
                .getStsInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<StsTokenEntity>>() {
                    @Override
                    public void onSuccess(BaseData<StsTokenEntity> data) {
                        tokenEntity = data.getData();
                    }

                    @Override
                    public void onComplete() {
                        startVideo();
                    }
                });*/
    }

}
