package com.liemi.seashellmallclient.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.*;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.SharemallActivityWebviewBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 10:29
 * 修改备注：
 */
public class BaseWebviewActivity extends BaseActivity<SharemallActivityWebviewBinding> {

    public static final String WEBVIEW_TITLE = "webview_title";

    //2跳转链接， 3跳转富文本
    public static final String WEBVIEW_TYPE = "webview_type";

    public static final String WEBVIEW_CONTENT = "webview_content";

    private static final String WEBVIEW_DETAILS = "webviewDetails";

    public static final int WEBVIEW_TYPE_URL = 2;

    public static final int WEBVIEW_TYPE_CONTENT = 3;

    private static final int FILE_CHOOSER_RESULT_CODE = 0x112;

    public static void start(Context context, String title, String content, String details) {
        if (!TextUtils.isEmpty(content)) {
            Bundle bundle = new Bundle();
            bundle.putString(WEBVIEW_TITLE, title);
            bundle.putInt(WEBVIEW_TYPE, content.startsWith("http") ? WEBVIEW_TYPE_URL : WEBVIEW_TYPE_CONTENT);
            bundle.putString(WEBVIEW_CONTENT, content);
            if (!TextUtils.isEmpty(details)) {
                bundle.putString(WEBVIEW_DETAILS, details);
            }
            JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
        } else {
            ToastUtils.showShort(R.string.sharemall_please_look_forward_to_it);
        }
    }

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_webview;
    }

    @Override
    protected void initUI() {
        String details = getIntent().getStringExtra(WEBVIEW_DETAILS);
        if (!TextUtils.isEmpty(details)) {
            getTvTitle().setText(R.string.sharemall_detail);
            mBinding.tvSubTitle.setText(getIntent().getStringExtra(WEBVIEW_TITLE));
            mBinding.tvDetails.setText(details);
            mBinding.llDetails.setVisibility(View.VISIBLE);
        } else {
            getTvTitle().setText(getIntent().getStringExtra(WEBVIEW_TITLE));
        }

        WebView webView = mBinding.wvWeb;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(getTvTitle().getText())) {
                    getTvTitle().setText(title);
                }
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING); //支持内容重新布局
        else
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.addJavascriptInterface(new AppInterfaceJS(), "App");

        //支持http和https的混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (getIntent().getIntExtra(WEBVIEW_TYPE, WEBVIEW_TYPE_URL) == WEBVIEW_TYPE_URL) {
            webView.loadUrl(getIntent().getStringExtra(WEBVIEW_CONTENT));
        } else {
            webSettings.setDefaultFontSize(28);
            String extra = getIntent().getStringExtra(WEBVIEW_CONTENT);
            StringBuilder builder = new StringBuilder();
            builder.append(getHtmlData(extra));
            webView.loadData(builder.toString(), "text/html; charset=UTF-8", null);
        }

    }

    /**
     * 富文本适配
     */
    private String getHtmlData(String bodyHTML) {
        String css = "<style> img{width:100% !important;max-width:100% !important;height:auto !important;min-height:10px;margin:0 !important;padding:0 !important;}</style>";
        String html = "<html><header>" + css + "</header><body style='margin:0 ;padding:0 '>" +bodyHTML + "</body></html>";
        return html;
    }

    @Override
    protected void initData() {

    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    class AppInterfaceJS {

        @JavascriptInterface
        public void back() {
            AppManager.getInstance().finishActivity();
        }
    }



}
