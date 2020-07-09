package com.liemi.seashellmallclient.ui.mine;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.user.HaiBeiConfidenceEntity;
import com.liemi.seashellmallclient.data.entity.user.HaiEntity;
import com.liemi.seashellmallclient.databinding.ActivityHaiBeiConfidenceBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.good.comment.CommentFragment;
import com.liemi.seashellmallclient.ui.home.SignInActivity;
import com.liemi.seashellmallclient.ui.mine.setting.SettingActivity;
import com.liemi.seashellmallclient.utils.MyTimeUtil;
import com.liemi.seashellmallclient.widget.SignRoleDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

public class HaiBeiConfidenceActivity extends BaseActivity<ActivityHaiBeiConfidenceBinding> {

    private String meth = "";
    private String meth1 = "";
    private float startx;
    private float starty;
    private float offsetx;
    private float offsety;
    private int i = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_hai_bei_confidence;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("海贝指数");
        getRightSettingImage().setVisibility(View.VISIBLE);
        getRightSettingImage().setImageResource(R.mipmap.ic_help);
        mBinding.rg.check(R.id.rb_day);
        mBinding.tvRemark1.setText("（近7日）");
        mBinding.tvRemark2.setText("（近7日）");
        doWebView(mBinding.wvEquity,meth1);
        doWebView(mBinding.wv,meth);
        mBinding.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int i = group.getCheckedRadioButtonId();
                if (i == R.id.rb_day) {
                    mBinding.tvRemark1.setText("（近7日）");
                    mBinding.tvRemark2.setText("（近7日）");
                    doGetHai(1);
                } else if (i == R.id.rb_week) {
                    mBinding.tvRemark1.setText("（近12周）");
                    mBinding.tvRemark2.setText("（近12周）");
                    doGetHai(2);
                } else if (i == R.id.rb_month) {
                    mBinding.tvRemark1.setText("（近12月）");
                    mBinding.tvRemark2.setText("（近12月）");
                    doGetHai(3);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }


    private void doWebView(WebView webView, String method) {

        //开启本地文件读取（默认为true，不设置也可以）
        webView.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/echart/echart.html");

        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx = Math.abs(event.getX() - startx);
                        offsety = Math.abs(event.getY() - starty);
                        if (offsetx > offsety) {
                            ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        } else {
                            ((WebView) v).requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mBinding.wvEquity.loadUrl(meth1);
                i++;
                if (i==2){
                    doGetHai(1);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_setting) {
            doAgreement(39);
            return;
        }
    }

    private void doGetHai(int type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .getMinHai(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<HaiBeiConfidenceEntity>>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(BaseData<HaiBeiConfidenceEntity> data) {
                        if (dataExist(data)) {
                            List<String> listY = new ArrayList<>();
                            List<String> listY1 = new ArrayList<>();
                            List<String> listX = new ArrayList<>();
                            if (data.getData().getList().size() > 0) {
                                for (int i = 0; i < data.getData().getList().size(); i++) {
                                    listY.add(data.getData().getList().get(i).getConfidence());
                                    listY1.add(data.getData().getList().get(i).getSynthesize());
//                                    listX.add(entity.getTime());
                                    if (type == 1) {
                                        listX.add(MyTimeUtil.getStringTime3(data.getData().getList().get(i).getTime()));
                                    } else if (type == 2) {   //按周显示
                                        listX.add("第" + (i + 1) + "周");
                                    } else if (type == 3) {   //按月显示
                                        listX.add("第" + (i + 1) + "月");
                                    }
                                }
                                String line = "line";
                                meth = "javascript:createChart('" + line + "','" + JSON.toJSONString(listY) + "','" + JSON.toJSONString(listX) + "')";
                                //综合分数
                                meth1 = "javascript:createChart('" + line + "','" + JSON.toJSONString(listY1) + "','" + JSON.toJSONString(listX) + "')";
                                doEcharts(mBinding.wvEquity,meth1);
                                doEcharts(mBinding.wv,meth);

                                /*mBinding.wvEquity.loadUrl(meth1);
                                mBinding.wv.loadUrl(meth);//信心指数*/
                            }
                            mBinding.setItem(data.getData());
                        }
                    }

                    @Override
                    public void onComplete() {
//                        doEquity();
//                        doConfidence();
                    }
                });


    }

    private void doEcharts(WebView webView, String method) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
           webView.loadUrl(method);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(method, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
            }

        }
    }

    //请求服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent(HaiBeiConfidenceActivity.this, BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, agreementEntityBaseData.getData().getTitle());
                            if (agreementEntityBaseData.getData().getLink_type() == 2) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, agreementEntityBaseData.getData().getContent());
                            startActivity(intent);
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }
}
