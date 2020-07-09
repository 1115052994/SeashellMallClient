package com.netmi.baselibrary.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.LoadingDialog;
import com.netmi.baselibrary.widget.XERecyclerView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：Activity 基类
 * 创建人：Simple
 * 创建时间：2017/9/4 17:47
 * 修改备注：
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends RxAppCompatActivity implements BaseView {

    /**
     * 日志使用TAG
     */
    protected String TAG;

    /**
     * 业务基类
     */
    protected BasePresenter basePresenter;

    /**
     * dataBinding
     */
    protected T mBinding;

    /**
     * 需要在子类中初始化
     */
    protected
    XERecyclerView xRecyclerView;

    protected BaseActivity instance;

    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            BACKGROUND_LOCATION_PERMISSION
    };
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private static final int PERMISSON_REQUESTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, getContentView());
        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                    BACKGROUND_LOCATION_PERMISSION
            };
        }
        setBarColor();
        initContent();
    }

    public void initContent() {
        initUI();
        initData();
        initTAG(this);
    }

    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (basePresenter != null) {
            basePresenter.destroy();
        }
        if (xRecyclerView != null) {
            xRecyclerView.destroy();
            xRecyclerView = null;
        }
        if (loadingDialog != null) {
            loadingDialog.destroy();
            loadingDialog = null;
        }

        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (basePresenter != null) {
            basePresenter.resume();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkNotificationEnabled();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (basePresenter != null) {
            basePresenter.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (basePresenter != null) {
            basePresenter.stop();
        }
    }

    /**
     * 日志TAG初始化
     *
     * @param context 上下文对象
     */
    protected void initTAG(Context context) {
        TAG = context.getClass().getSimpleName();
    }

    /**
     * 获取布局
     */
    protected abstract int getContentView();

    /**
     * 界面初始化
     */
    protected abstract void initUI();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    public Context getContext() {
        return instance;
    }

    public BaseActivity getActivity() {
        return instance;
    }

    protected LoadingDialog loadingDialog;

    @Override
    public void showProgress(String message) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.showProgress(message);
    }

    @Override
    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.hideProgress();
        }
    }

    @Override
    public void showError(String message) {
        hideProgress();
        ToastUtils.showShort(message);
    }

    public void doClick(View view) {
        if (view.getId() == R.id.ll_back || view.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }

    /**
     * 获取标题控件
     **/
    public TextView getTvTitle() {
        return findViewById(R.id.tv_title);
    }

    public TextView getRightSetting() {
        return findViewById(R.id.tv_setting);
    }

    //获取右边的图片按钮
    public ImageView getRightSettingImage() {
        return findViewById(R.id.iv_setting);
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if (!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog(0);
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     * type:0.普通权限 1.消息通知权限
     */
    private void showMissingPermissionDialog(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(type == 0 ? "当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。"
                : "检测到您的通知权限已关闭，暂无法接收通知消息，请前往开启");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(type);
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkNotificationEnabled() {
        boolean isEnabled = isNotificationEnabled(this);
        Log.i(TAG, "is notification enabled: " + isEnabled);
        if (!isEnabled) {
            showMissingPermissionDialog(1);
        }else {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                if (isNeedCheck) {
                    checkPermissions(needPermissions);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings(int type) {
        if (type == 0) {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            } else {
                // 其他
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
