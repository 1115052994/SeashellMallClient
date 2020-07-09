package com.liemi.seashellmallclient.ui.mine.bbs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.ArticleApi;
import com.liemi.seashellmallclient.data.entity.article.ArticleClassEntity;
import com.liemi.seashellmallclient.data.entity.article.ArticleCommentEntity;
import com.liemi.seashellmallclient.data.entity.article.ArticleListEntity;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityArticleWebBinding;
import com.liemi.seashellmallclient.databinding.ItemArticleAllCommentBinding;
import com.liemi.seashellmallclient.utils.Densitys;
import com.liemi.seashellmallclient.widget.MyBaseDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.ShareDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;

public class ArticleWebActivity extends BaseActivity<ActivityArticleWebBinding> implements View.OnClickListener, ShareDialog.ShareCallback {

    private MyBaseDialog menuDialog;
    private MyBaseDialog menuDialog1;
    private AlertDialog alertDialog;
    private ArticleCommentEntity commentEntity;
    private ItemArticleAllCommentBinding commentBinding;
    private int pId;
    private BaseRViewAdapter<ArticleCommentEntity, BaseViewHolder> adapter;
    private String id;
    private String title;
    private String image;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(this, true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_article_web;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("课程详情");
        getRightSettingImage().setVisibility(View.VISIBLE);
        getRightSettingImage().setImageResource(R.mipmap.ic_bbs_share);
        title = getIntent().getStringExtra(WEBVIEW_TITLE);
        image = getIntent().getStringExtra("image");
        mBinding.setTitle(title);
        mBinding.setTime(getIntent().getStringExtra(VipParam.time));
        mBinding.setReadNum(getIntent().getStringExtra(VipParam.readNum));
        mBinding.setZanNum(getIntent().getStringExtra("zan"));

        id = getIntent().getStringExtra("id");

        WebView webView = mBinding.wvWeb;
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
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        webSettings.setDefaultZoom(zoomDensity);

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        //支持http和https的混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (getIntent().getIntExtra(WEBVIEW_TYPE, WEBVIEW_TYPE_URL) == WEBVIEW_TYPE_URL) {
            webView.loadUrl(getIntent().getStringExtra(WEBVIEW_CONTENT));
        } else {
            webView.loadData(getIntent().getStringExtra(WEBVIEW_CONTENT), "text/html; charset=UTF-8", null);
        }
        //输入法到底部的间距(按需求设置)
        final int paddingBottom = Densitys.dp2px(this, 5);
        mBinding.rlBody.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mBinding.rlBody.getWindowVisibleDisplayFrame(r);
                //r.top 是状态栏高度
                int screenHeight = mBinding.rlBody.getRootView().getHeight();
                int softHeight = screenHeight - r.bottom;
                if (softHeight - getNavigationBarHeight(getContext()) > 100) {//当输入法高度大于100判定为输入法打开了
                    mBinding.rlBody.scrollTo(0, softHeight + paddingBottom);
                } else {//否则判断为输入法隐藏了
                    mBinding.rlBody.scrollTo(0, paddingBottom);
             /*       mBinding.etCommentContent.setHint("输入评论内容");
                    mBinding.etCommentContent.setText("");*/
                    pId = 0;
                }
            }
        });

        alertDialog = new AlertDialog.Builder(getContext())
                .setView(R.layout.dialog_report_comment_success)
                .create();

        mBinding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseRViewAdapter<ArticleCommentEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_article_all_comment;
            }

            @Override
            public int getItemViewType(int position) {
                return isShowEmpty() ? EMPTY_VIEW_TYPE : getViewType(position);
            }

            private int getViewType(int position) {
                return position;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<ArticleCommentEntity>(binding) {
                    @Override
                    public void bindData(ArticleCommentEntity item) {
                        if (getBinding() instanceof ItemArticleAllCommentBinding) {
                            commentBinding = (ItemArticleAllCommentBinding) getBinding();
                            commentEntity = getItem(position);
//                            if (TextUtils.isEmpty(commentEntity.getFrom_content())) {
                            commentBinding.tvCommentContent.setText(commentEntity.getContent());
                           /* } else {
                                Spanned spanned = Html.fromHtml("<font color='#FF0000'> @" + commentEntity.getFrom_content() + "</font> " +
                                        "<font color='#ff555555'>" + commentEntity.getContent() + "</font>");
                                commentBinding.tvCommentContent.setText(spanned);
                            }*/
                            commentBinding.cbGiveLike.setSelected(TextUtils.equals(commentEntity.getIs_zan(), "1"));
                            commentBinding.cbGiveLike.setText(TextUtils.isEmpty(commentEntity.getGood_num()) ? "0" : commentEntity.getGood_num());
                        }
                        super.bindData(item);

                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_report) {  //举报
                            showReportDialog(getItem(position).getContent());
                        } else if (view.getId() == R.id.cb_give_like) {
                            if (MApplication.getInstance().checkUserIsLogin()) {
                                if (getItem(position) != null) {
                                    //点赞活动
                                    if (TextUtils.equals(getItem(position).getIs_zan(), "0")) {//未赞状态
                                        doAddLove(getItem(position));

                                    }

                                    //取消点赞
                                    if (TextUtils.equals(getItem(position).getIs_zan(), "1")) {//赞状态
                                        doDelLove(getItem(position));
                                    }
                                }
                            }
                        } /*else {
                            KeyboardUtils.showKeyboard(mBinding.etCommentContent);
                            mBinding.etCommentContent.setHint("@" + getItem(position).getUserData().getNickname());
                            pId = Strings.toInt(getItem(position).getId());
                        }*/
                    }
                };
            }
        };
        mBinding.rvData.setAdapter(adapter);

    }

    /**
     * 获取是否有虚拟按键
     * 通过判断是否有物理返回键反向判断是否有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey & !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (checkDeviceHasNavigationBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }


    @Override
    protected void initData() {
        articleAddLikeOrCollect(id, 1);
        //获取全部评论
        doGetComment();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        switch (view.getId()) {
            case R.id.iv_setting:
                showMenuDialog();
                break;
            case R.id.tv_comment:
                if (!TextUtils.isEmpty(mBinding.etCommentContent.getText().toString())) {
                    //发表评论
                    doCreatComment(mBinding.etCommentContent.getText().toString());
                } else {
                    ToastUtils.showShort("评论内容不能为空！");
                }
                break;
        }
    }

    private void showMenuDialog() {
        if (menuDialog == null) {
            menuDialog = MyBaseDialog.getDialog(getActivity(), R.layout.menu_article_share);
            menuDialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
            menuDialog.findViewById(R.id.tv_share).setOnClickListener(this);
        }
        menuDialog.showBottom();
    }

    private void showReportDialog(String content) {
        if (menuDialog1 == null) {
            menuDialog1 = MyBaseDialog.getDialog(getActivity(), R.layout.menu_comment_report);
            menuDialog1.findViewById(R.id.tv_cancel).setOnClickListener(this);
            TextView tvReportComment = menuDialog1.findViewById(R.id.tv_report_comment);
            menuDialog1.findViewById(R.id.tv_improper_remarks).setOnClickListener(this);
            menuDialog1.findViewById(R.id.tv_advertising).setOnClickListener(this);
            tvReportComment.setText(getString(R.string.intelligence_service_report_comment) + "\"" + content + "\"");
        }
        menuDialog1.showBottom();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (menuDialog != null) menuDialog.dismiss();
                if (menuDialog1 != null) menuDialog1.dismiss();
                break;
            case R.id.tv_share:
                if (menuDialog != null) menuDialog.dismiss();
                if (!TextUtils.isEmpty(id)) {
                    ShareEntity shareEntity = new ShareEntity();
                    shareEntity.setActivity(this);
                    shareEntity.setLinkUrl(Constant.SHARE_ARTICLE + id);
                    shareEntity.setTitle(title);
                    shareEntity.setContent("来自客商e宝");
//                    shareEntity.setImgUrl(image);
                    shareEntity.setImgRes(R.mipmap.app_logo);
                    new ShareDialog(this, shareEntity, this).showBottomOfDialog();
                }
                break;
            case R.id.tv_improper_remarks:
                if (menuDialog1 != null) menuDialog1.dismiss();
                //举报
                doActivityReport("1");
                break;
            case R.id.tv_advertising:
                if (menuDialog1 != null) menuDialog1.dismiss();
                doActivityReport("2");
                break;
        }
    }

    @Override
    public void startShare() {
    }

    @Override
    public void shareFinish() {

    }

    @Override
    public void shareFailure() {

    }

    private void showSuccessDialog() {
        alertDialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高     
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes(); //获取对话框当前的参数值      
        p.width = (int) (d.getWidth() * 0.3); //宽度设置为屏幕的0.5   
        p.height = (int) (d.getWidth() * 0.3);//  
        alertDialog.getWindow().setAttributes(p);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_radius2_color3a3a3a);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1200);
    }

    /**
     * res/drawable(mipmap)/xxx.png::::uri－－－－>url
     *
     * @return
     */
    private String imageTranslateUri(int resId) {

        Resources r = getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));

        return uri.toString();
    }

    private void doGetComment() {
        showProgress("");
        RetrofitApiFactory.createApi(ArticleApi.class)
                .getArticleCommentList(PageUtil.toPage(0), Constant.ALL_PAGES, id)
                .compose(this.<BaseData<PageEntity<ArticleCommentEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<PageEntity<ArticleCommentEntity>>>compose())
                .subscribe(new FastObserver<BaseData<PageEntity<ArticleCommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<ArticleCommentEntity>> data) {
                        if (dataExist(data)) {
                            adapter.setData(data.getData().getList());
                        }
                    }
                });
    }

    private void doCreatComment(String content) {
        showProgress("");
        RetrofitApiFactory.createApi(ArticleApi.class)
                .doArticleComment(id, content, "default")
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showError("评论成功");
                            mBinding.etCommentContent.setText("");
                            doGetComment();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                        pId = 0;
                    }
                });
    }

    private void doAddLove(ArticleCommentEntity entity) {
        if (entity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_please_wait_load_success));
            return;
        }
        showProgress("");

        RetrofitApiFactory.createApi(ArticleApi.class)
                .activityZan(entity.getId(), "1")
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            entity.setIs_zan("1");
                            entity.setGood_num(Strings.toInt(entity.getGood_num()) + 1 + "");
                            adapter.notifyDataSetChanged();
                            ToastUtils.showShort(getString(R.string.sharemall_operation_success));

                        } else {

                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doDelLove(ArticleCommentEntity entity) {
        if (entity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_please_wait_load_success));
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(ArticleApi.class)
                .activityZan(entity.getId(), "2")
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            entity.setIs_zan("0");
                            entity.setGood_num(Strings.toInt(entity.getGood_num()) - 1 + "");
                            adapter.notifyDataSetChanged();
                            ToastUtils.showShort(getString(R.string.sharemall_operation_success));
                        } else {
                            showError(data.getErrmsg());

                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private void doActivityReport(String type) {
        if (commentBinding.getItem() == null) {
            ToastUtils.showShort(getString(R.string.sharemall_please_wait_load_success));
            return;
        }
        showProgress("");
        RetrofitApiFactory.createApi(ArticleApi.class)
                .activityReport(commentBinding.getItem().getId(), type)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showSuccessDialog();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    /**
     * 阅读/点赞/收藏资讯API
     * inid	int
     * 资讯主键
     * <p>
     * type	int
     * 操作类型 1：阅读 2：点赞 3：收藏
     */
    private void articleAddLikeOrCollect(String articleId, int type) {
        if (Strings.isEmpty(articleId)) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_info));
            return;
        }
        RetrofitApiFactory.createApi(ArticleApi.class)
                .articleAddLikeOrCollect(articleId, type)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() != Constant.SUCCESS_CODE) {

                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
