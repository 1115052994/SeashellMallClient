package com.liemi.seashellmallclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.user.NotReadNumEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.event.RefreshChatUnreadNumEvent;
import com.liemi.seashellmallclient.databinding.ActivityMainBinding;
import com.liemi.seashellmallclient.ui.home.HomeFragment;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeFragment;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopPayActivity;
import com.liemi.seashellmallclient.ui.mine.MineFragment;
import com.liemi.seashellmallclient.ui.mine.wallet.CaptureQRCodeActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.WalletTransferActivity;
import com.liemi.seashellmallclient.utils.PushUtils;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PlatformEntity;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements CompoundButton.OnCheckedChangeListener {

    public static double latitude;
    public static double longitude;


    private CompoundButton selectView;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initLocationOption();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appexitTask != null) {
            appexitTask.dispose();
            appexitTask = null;
        }
        EventBus.getDefault().unregister(this);
        locationClient.stop();
    }

    /**
     * 初始化定位参数配置
     */
    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(5000);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
             latitude = location.getLatitude();
            //获取经度信息
             longitude = location.getLongitude();
            Log.e("latitude========:" + latitude, "longitude======:" + longitude);
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
            Log.e("latitude========:" + errorCode, "*****" );
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUI() {
        mBinding.setCheckListener(this);
        mBinding.rbHome.setTag(HomeFragment.TAG);
        mBinding.rbLocalLife.setTag(LocalLifeFragment.TAG);
        mBinding.rbMine.setTag(MineFragment.TAG);
        mBinding.executePendingBindings();
        mBinding.rbHome.setChecked(true);

        //检查版本更新
        Beta.checkUpgrade(false, true);

        //TODO:注册信鸽推送
//        PushUtils.registerPush();
        //TODO:信鸽推送状态栏显示的View
//        XGPushManager.setNotifactionCallback(this::checkXgMessage);
    }

    //TODO:信鸽设置需要跳转的页面
    /*private void checkXgMessage(XGNotifaction xgNotifaction) {
        Intent intent = new Intent();
        String message = "";
        Gson gson = new Gson();
        PushMessageEntity pushMessageEntity;
        try {
            pushMessageEntity = gson.fromJson(xgNotifaction.getContent(), PushMessageEntity.class);
        } catch (Exception e) {
            //如果解析出错，就当作普通消息处理
            pushMessageEntity = new PushMessageEntity();
            pushMessageEntity.setType(PushMessageEntity.APP_NORMAL_MESSAGE);
            PushMessageEntity.PushContent pushContent = new PushMessageEntity.PushContent();
            pushContent.setContent(xgNotifaction.getContent());
            pushMessageEntity.setData(pushContent);
        }

        switch (pushMessageEntity.getType()) {
            case PushMessageEntity.APP_NORMAL_MESSAGE:
                //普通消息
                message = pushMessageEntity.getData().getContent();
                intent.setClass(this, MainActivity.class);
                break;
            case PushMessageEntity.APP_UPDATE_MESSAGE:
                message = getString(R.string.sharemall_new_version_tips);
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(pushMessageEntity.getData().getContent()));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                break;
            case PushMessageEntity.APP_ORDER_MESSAGE:
                //我的订单详情
                intent.setClass(this, MineOrderDetailsActivity.class);
                intent.putExtra(MineOrderDetailsActivity.ORDER_DETAILS_ID, pushMessageEntity.getData().getId());
                break;
        }
        NotificationUtils.sendNotification(NotificationUtils.MESSAGE_NORMAL,
                xgNotifaction.getNotifyId(),
                xgNotifaction.getTitle(),
                message,
                PendingIntent.getActivity(MApplication.getAppContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }*/

    @Override
    protected void initData() {
        //绑定个推别名
        PushUtils.bindPush();
        doPlatformHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetUnreadNum();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doGetUserInfo();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }


    private void showFragment(CompoundButton curView) {
        if (selectView != null) {
            selectView.setChecked(false);
        }
        selectView = curView;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = Fragment.instantiate(this, tag);
            fragmentTransaction.add(R.id.fl_content, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            boolean isHide = fragment.isHidden();
            if (isHide) {
                fragmentTransaction.show(fragment);
                fragmentTransaction.commitAllowingStateLoss();
                fragment.onResume(); //切换Fragment，实时刷新数据
            }
        }
    }

    private void hideFragment(CompoundButton lastView) {
        if (lastView != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = (String) lastView.getTag();
            if (fragmentManager.findFragmentByTag(tag) != null) {
                boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
                if (!isHide) {
                    fragmentTransaction.hide(fragmentManager.findFragmentByTag(tag));
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        }
    }


    /**
     * 切换Tab
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchTab(SwitchTabEvent event) {
        if (event.rbId == R.id.rb_home || event.rbId == R.id.rb_local_life || event.rbId == R.id.rb_mine) {
            ((RadioButton) findViewById(event.rbId)).setChecked(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doGetUserInfo();
    }

    public static String getLongitude(){
        return String.valueOf(longitude);
    }

    public static String getLatitude(){
        return String.valueOf(latitude);
    }

    /**
     * 防退出误操作
     */
    private boolean canFinish = false;
    private Disposable appexitTask;

    @Override
    public void onBackPressed() {
        if (canFinish) {
            super.onBackPressed();
        } else {
            canFinish = true;
            ToastUtils.showShort(R.string.sharemall_quickly_click_twice_to_exit_the_app);
            appexitTask = Observable.timer(1, TimeUnit.SECONDS).subscribe((Long aLong) -> canFinish = false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == 10002 && data != null) {
            String scanResult = data.getStringExtra("scan_result");
            if (scanResult.contains("shop_id")) { //线下买单
                String shop_id = scanResult.substring(scanResult.indexOf("=") + 1, scanResult.indexOf("&"));
                String shop_name = scanResult.substring(scanResult.lastIndexOf("="), scanResult.length());
                if (MApplication.getInstance().checkUserIsLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SHOP_ID, shop_id);
                    bundle.putString("title", shop_name);
                    JumpUtil.overlay(getActivity(), LocalLifeShopPayActivity.class, bundle);
                }
            }else {
                if (AppManager.getInstance().existActivity(CaptureQRCodeActivity.class)){
                    AppManager.getInstance().finishActivity(CaptureQRCodeActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("address",scanResult);
                JumpUtil.overlay(getContext(), WalletTransferActivity.class,bundle);
            }
        }
    }

    //获取用户信息
    public void doGetUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<ShareMallUserInfoEntity>>() {

                    @Override
                    public void onSuccess(BaseData<ShareMallUserInfoEntity> data) {
                        if (dataExist(data)) {
                            UserInfoCache.put(data.getData());
                        }
                    }
                });

    }

    private void doGetUnreadNum() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getAllUnreadNum("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<NotReadNumEntity>>() {
                    @Override
                    public void onSuccess(BaseData<NotReadNumEntity> data) {
                        if (dataExist(data)) {
                            EventBus.getDefault().removeAllStickyEvents();
                            EventBus.getDefault().postSticky(new RefreshChatUnreadNumEvent(data.getData().getAll_no_readnum()));
                        }
                    }
                });
    }

    //请求平台客服
    private void doPlatformHelper() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getPlatformInfo("")
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<PlatformEntity>>() {

                    @Override
                    public void onSuccess(BaseData<PlatformEntity> data) {
                        if (dataExist(data)) {
                            AppConfigCache.get().setPlatformEntity(data.getData());
                        }
                    }
                });
    }
}
