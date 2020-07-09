package com.netmi.baselibrary.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.R;

/**
 * 类描述：使用示例可移步http://doc.netmi.com.cn/web/#/32?page_id=3297
 * 创建人：Simple
 * 创建时间：2019/2/16
 * 修改备注：
 */
public class ImmersionBarUtils {

    /**
     * 设置白色的状态栏
     */
    public static void whiteStatusBar(Activity activity, boolean fits) {
        setStatusBar(activity, fits, R.color.white);
    }

    public static void setStatusBar(Activity activity, boolean fits, @ColorRes int color) {
        setStatusBar(activity, null, fits, color);
    }

    public static void setStatusBar(Fragment fragment, boolean fits, @ColorRes int color) {
        setStatusBar(null, fragment, fits, color);
    }

    /**
     * 设置特定颜色的状态栏
     *
     * @param fits 解决布局与状态栏重叠问题，true基于状态栏开始显示，false基于顶部开始显示
     */
    private static void setStatusBar(Activity activity, Fragment fragment, boolean fits, @ColorRes int color) {
        ImmersionBar immersionBar = activity == null ? ImmersionBar.with(fragment) : ImmersionBar.with(activity);
        immersionBar
                .statusBarColor(color)
                .fitsSystemWindows(fits);
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            immersionBar.statusBarDarkFont(true);
        } else {
            immersionBar.statusBarDarkFont(true, 0.2f);
        }
        immersionBar.init();
    }


    //透明状态栏
    public static void tranStatusBar(Activity activity, boolean fits) {
        ImmersionBar immersionBar = ImmersionBar
                .with(activity)
                .statusBarColor(android.R.color.transparent)
                .fitsSystemWindows(fits);
        immersionBar.init();
    }

}
