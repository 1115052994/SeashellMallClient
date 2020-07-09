package com.netmi.baselibrary.data;

import android.os.Environment;

import com.netmi.baselibrary.utils.AppUtils;

import java.io.File;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 18:05
 * 修改备注：
 */
public class Constant {

    /**
     * 服务器Api地址
     */
    public static final String BASE_API = AppUtils.getBaseApi();

    public static final String BASE_HTML = AppUtils.getBaseHtml();

    public static final String BASE_API_DEVELOP = "http://api-test.haobeitech.com/";

    public static final String BASE_API_DEBUG = "http://api-test.haobeitech.com/";//https://merchant-api-test.netmi.com.cn/

    public static final String BASE_API_RELEASE = "http://hb-api.haobeitech.com/";

    public static final String BASE_HTML_DEVELOP = BASE_API_DEVELOP;

    public static final String BASE_HTML_DEBUG = BASE_API_DEBUG;

    public static final String BASE_HTML_RELEASE = BASE_API_RELEASE;

    //基础电商API
//    public static final String BASE_MALL_API_RELEASE = "https://merchant-api.netmi.com.cn/";
    public static final String BASE_MALL_API_RELEASE = "http://hb-api.haobeitech.com/";

    public static final String BASE_MALL_HTML_RELEASE = BASE_MALL_API_RELEASE;

    /**
     * 分享商品
     */
    public static final String SHARE_GOOD = BASE_HTML + "netmi-shop-h5/dist/#/goods?goods_id=";

    /**
     * 分享文章
     */
    public static final String SHARE_ARTICLE = BASE_HTML + "/netmi-shop-h5/dist/#/newInfo?id=";

    //商家端APP下载地址
    public static final String BASE_SHOP_APP_DOWMLOAD_URL = "https://a.app.qq.com/o/simple.jsp?pkgname=com.netmi.seashellmallbuiness";

    /**
     * Http 请求成功
     */
    public static final int SUCCESS_CODE = 0;

    /**
     * token错误
     */
    public static final int TOKEN_FAIL = 10000;

    /**
     * token过期
     */
    public static final int TOKEN_OVERDUE = 10001;

    /**
     * 已自动登出，请重新登陆
     */
    public static final int TOKEN_OUT = 1200;

    /**
     * 项目文件缓存文件夹
     */
    private final static String FILE_PATH = "netmi";

    /**
     * 是否关闭推送
     */
    public static final String IS_PUSH = "is_push";

    /**
     * 平台类型（1:IOS,2:安卓,3:PC,4:移动H5）
     */
    public static final int PLATFORM_TYPE = 2;

    /**
     * 默认获取全部的条数
     */
    public static final int ALL_PAGES = 5000;

    /**
     * 默认一页的条数
     */
    public static final int PAGE_ROWS = 20;

    /**
     * 得到当前外部存储设备的目录
     */
    public static final String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILE_PATH + File.separator;

    /**
     * 压缩图片缓存文件夹
     */
    public static final String COMPRESS_IMAGE_CACHE_DIR = "compress_images";

    public static final String COMPRESS_CACHE_DIR = "cache_images";

    /**
     * 相机权限
     */
    public static final int CAMERA_REQUEST_CODE = 0x13;

    /**
     * 刷新
     */
    public static final int PULL_REFRESH = 0;

    /**
     * 加载更多
     */
    public static final int LOAD_MORE = 1;

    /**
     * 个推推送别名前缀
     */
    public static final String PUSH_PREFIX = "liemi_";

    /**
     * 是否第一次登录
     */
    public static final String FIRST_LOGIN = "firstLogin";

    /**
     * 默认倒计时时间 60 * 1000
     */
    public static final long COUNT_DOWN_TIME_DEFAULT = 60 * 1000;

}
