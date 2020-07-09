package com.liemi.seashellmallclient.ui.mine.vip;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileDownloadContract;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityVipshareBinding;
import com.liemi.seashellmallclient.databinding.LayouShareItemVipBinding;
import com.liemi.seashellmallclient.presenter.FileDownloadPresenterImpl;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ControlShareUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.SlidingTextTabLayout;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class VIPShareActivity extends BaseActivity<ActivityVipshareBinding> implements PlatformActionListener, FileDownloadContract.View {
    private String url;
    private String type;       //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
    private ShareEntity entity;
    private String inviteCode;
    private List<String> imageList;
    private List<String> image = new ArrayList<>();
    private ArrayList<Fragment> fragmentList;

    @Override
    protected int getContentView() {
        return R.layout.activity_vipshare;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra("title"));
        String[] titles=new String[]{getString(R.string.sharemall_vip_invite_friend),
                getString(R.string.sharemall_vip_invite_shop)};
        fragmentList = new ArrayList<>();
        fragmentList.add(VIPShareFragment.newInstance(1));
        fragmentList.add(VIPShareFragment.newInstance(2));
        mBinding.vp.setOffscreenPageLimit(2);
        mBinding.vp.setAdapter(new SlidingTextTabLayout.InnerPagerAdapter(getSupportFragmentManager(), fragmentList, titles));
        mBinding.stlTitle.setViewPager(mBinding.vp);
        mBinding.vp.setCurrentItem(0);
        mBinding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(fragmentList != null
                        && !fragmentList.isEmpty()) {
                    url = ((VIPShareFragment)fragmentList.get(position)).getImage();
                    entity.setLinkUrl(url);
                    entity.setImgUrl(url);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        basePresenter = new FileDownloadPresenterImpl(this, getContext());
    }

    @Override
    protected void initData() {
        type=getIntent().getStringExtra(VipParam.shareType);
        //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报

        switch (type) {
            case "1":
                entity=new ShareEntity();
                entity.setTitle("客商e宝");
                entity.setContent("一站式商家营销服务 超优惠用户消费体系 接入全网平台海量优惠券 为商家、消费者提供最舒适的商业服务");
                break;
            default:
                entity=new ShareEntity();
                entity.setTitle("客商e宝");
                entity.setContent("一站式商家营销服务 超优惠用户消费体系 接入全网平台海量优惠券 为商家、消费者提供最舒适的商业服务");
                break;
        }
        doGetImgUrl();
    }

    private void shareToPlatform(String platformName) {
        if (entity != null) {
            showProgress("");
            Platform.ShareParams shareParams = new Platform.ShareParams();
            shareParams.setShareType(Platform.SHARE_IMAGE);
            shareParams.setTitle(entity.getTitle());
            shareParams.setUrl(entity.getLinkUrl());
            shareParams.setText(entity.getContent());
            if (Strings.isEmpty(entity.getImgUrl())){
                shareParams.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo));       //微信专用
            }else {
                shareParams.setImageUrl(entity.getImgUrl());
            }
            Platform platform = ShareSDK.getPlatform(platformName);
            platform.setPlatformActionListener(this);
            platform.share(shareParams);
        } else {
            Toast.makeText(getContext(), getString(R.string.sharemall_initialize_data_first), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//        doResult(1, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }

    private void doResult(int resultCode, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        doResult(-1, "分享失败了T.T");
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_save_pic) {
            if (!TextUtils.isEmpty(url)) {
                new RxPermissions(getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    imageList = new ArrayList<>();
                                    imageList.add(url);
                                    ((FileDownloadPresenterImpl) basePresenter).doDownloadFiles(imageList);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                } else {
                                    // 用户拒绝了该权限，并且选中『不再询问』
                                }
                            }
                        });
            } else {
                ToastUtils.showShort(getString(R.string.sharemall_lack_save_pic));
            }

        } else if (i == R.id.tv_share_moment) {
            ControlShareUtil.controlWechatMomentsShare(false);
                shareToPlatform(WechatMoments.NAME);

        } else if (i == R.id.tv_share_wechat) {
            ControlShareUtil.controlWechatShare(false);
            shareToPlatform(Wechat.NAME);
        }else if(i==R.id.tv_copy){
            KeyboardUtils.putTextIntoClip(getContext(),inviteCode);
        }
    }

    private void doGetImgUrl() {
        showProgress("");
        Observable<BaseData<VIPShareImgEntity>> observable;

        if ("1".equals(type)) {  //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            observable = RetrofitApiFactory.createApi(VIPApi.class)
                    .VIPInviteFriend(null);
        }else{
            observable = RetrofitApiFactory.createApi(VIPApi.class)
                    .getStoreSharePoster(null);
        }
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<VIPShareImgEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        if ("1".equals(type)){ //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
                            mBinding.rlInviteCode.setVisibility(View.VISIBLE);
                            inviteCode=data.getData().getInvite_code();
                            mBinding.tvInviteCode.setText(inviteCode);
                        }
                        url = data.getData().getImg_url();
                        entity.setLinkUrl(url);
                        entity.setImgUrl(url);
                        /*GlideShowImageUtils.displayNetImage(getContext(), url, mBinding.ivShare,
                                R.drawable.baselib_bg_null);*/

                    }
                });

    }

    @Override
    public void fileDownloadResult(List<String> imgUrls) {
        if (imgUrls != null) {
            ToastUtils.showShort("图片已保存至相册");
        } else {
            ToastUtils.showShort("没有要保存的图片");
        }
    }

    @Override
    public void fileDownloadProgress(int percentage) {

    }

    @Override
    public void fileDownloadFail(String errMsg) {
        ToastUtils.showShort(errMsg);
    }
}
