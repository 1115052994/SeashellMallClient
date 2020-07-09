package com.liemi.seashellmallclient.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.gyf.barlibrary.ImmersionBar;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.user.HaiBeiConfidenceEntity;
import com.liemi.seashellmallclient.data.entity.user.HaiEntity;
import com.liemi.seashellmallclient.data.entity.user.MineGrowthEntity;
import com.liemi.seashellmallclient.data.entity.user.MineIntegralNumEntity;
import com.liemi.seashellmallclient.data.entity.user.OrderCountEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPIncomeInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPUserInfoEntity;
import com.liemi.seashellmallclient.data.event.RefreshChatUnreadNumEvent;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.FragmentMineBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressManageActivity;
import com.liemi.seashellmallclient.ui.mine.bbs.BBsActivity;
import com.liemi.seashellmallclient.ui.mine.bbs.MyCommentActivity;
import com.liemi.seashellmallclient.ui.mine.coupon.MineCouponActivity;
import com.liemi.seashellmallclient.ui.mine.message.RecentContactsActivity;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderActivity;
import com.liemi.seashellmallclient.ui.mine.refund.OrderRefundActivity;
import com.liemi.seashellmallclient.ui.mine.refund.RefundListActivity;
import com.liemi.seashellmallclient.ui.mine.setting.SettingActivity;
import com.liemi.seashellmallclient.ui.mine.userinfo.UserInfoActivity;
import com.liemi.seashellmallclient.ui.mine.verification.MyVerificationActivity;
import com.liemi.seashellmallclient.ui.mine.vip.PerformanceManagementActivity;
import com.liemi.seashellmallclient.ui.mine.vip.VIPFollowerActivity;
import com.liemi.seashellmallclient.ui.mine.vip.VIPShareActivity;
import com.liemi.seashellmallclient.ui.mine.wallet.WalletActivity;
import com.liemi.seashellmallclient.utils.MyTimeUtil;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.liemi.seashellmallclient.data.param.OrderParam.*;
import static com.netmi.baselibrary.data.Constant.BASE_SHOP_APP_DOWMLOAD_URL;

public class MineFragment extends BaseFragment<FragmentMineBinding> {
    public static final String TAG = MineFragment.class.getName();
    //用户可用的御币数量
    private int mUserUsableIntegralNum;
    private MyVIPIncomeInfoEntity vipIncomeInfoEntity;
    private ShareMallUserInfoEntity entity;
    private MineGrowthEntity growthEntity;
    private String meth = "";

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initUI() {
        initImmersionBar();
        EventBus.getDefault().register(this);
        mBinding.setDoClick(this);
        doWebView(mBinding.wv, meth);
        loadUserInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this)
                .reset()
                .statusBarColor(R.color.bgColor)
                .init();
    }

    private void loadUserInfo() {
        entity = UserInfoCache.get(ShareMallUserInfoEntity.class);
        mBinding.setItem(entity);
        if (entity.getRole() != 1) {
            mBinding.tvInviteShop.setVisibility(View.INVISIBLE);
        }
        mBinding.executePendingBindings();
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected void initData() {
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void refreshChatUnreadNum(final RefreshChatUnreadNumEvent event) {
        new Handler().post(() -> {
            setMessageCount(event.unreadNum);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doGetUserInfo();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int clickId = view.getId();
        switch (clickId) {
            case R.id.iv_message://消息
                JumpUtil.overlay(getContext(), RecentContactsActivity.class);
                break;
            case R.id.iv_setting://设置
                JumpUtil.startSceneTransition(getActivity(), SettingActivity.class, null,
                        new Pair<>((View) mBinding.ivAvatar, getString(R.string.person_transition_avatar)),
                        new Pair<>((View) mBinding.tvNickname, getString(R.string.person_transition_name)));
                break;
            case R.id.rl_avatar://个人信息
                //跳转到个人信息页面
                JumpUtil.startSceneTransition(getActivity(), UserInfoActivity.class, null,
                        new Pair<>((View) mBinding.ivAvatar, getString(R.string.person_transition_avatar)),
                        new Pair<>((View) mBinding.tvNickname, getString(R.string.person_transition_name)));
                break;
            case R.id.ll_order://查看全部订单
                JumpUtil.overlay(getContext(), MineOrderActivity.class);
                break;
            case R.id.tv_wait_pay://待付款
                startOrder(ORDER_WAIT_PAY);
                break;
            case R.id.tv_wait_send://待发货
                startOrder(ORDER_WAIT_SEND);
                break;
            case R.id.tv_wait_receive://待收货
                startOrder(ORDER_WAIT_RECEIVE);
                break;
            case R.id.tv_wait_comment://待评价
                startOrder(ORDER_WAIT_COMMENT);
                break;
            case R.id.tv_refund://售后/退款
                //退款状态的订单
                JumpUtil.overlay(getContext(), RefundListActivity.class);
                break;
            case R.id.ll_verification://我的核销
                JumpUtil.overlay(getContext(), MyVerificationActivity.class);
                break;
            case R.id.fl_coupon: //我的卡券
                JumpUtil.overlay(getContext(), MineCouponActivity.class);
                break;
            case R.id.tv_merchants_settled://商家入驻
                //BASE_SHOP_APP_DOWMLOAD_URL
                JumpUtil.overlay(getContext(), MerchantsSettledActivity.class);

                break;
            case R.id.tv_copy://复制邀请码
                KeyboardUtils.putTextIntoClip(getContext(), entity.getShare_code());
                break;
            case R.id.tv_my_address://我的地址
                JumpUtil.overlay(getActivity(), AddressManageActivity.class);
                break;
            case R.id.tv_bbs: //论坛
                JumpUtil.overlay(getContext(), BBsActivity.class);
                break;
            case R.id.tv_my_comments://我的评论
                JumpUtil.overlay(getContext(), MyCommentActivity.class);
                break;
            case R.id.tv_invite_friends:
                //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
                JumpUtil.overlay(getContext(), VIPShareActivity.class,
                        VipParam.shareType, "1", VipParam.title, getString(R.string.sharemall_vip_invite_friend));
                break;
            case R.id.tv_invite_shop:
                //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
               /* if (entity.getRole()==1) {
                    JumpUtil.overlay(getContext(), VIPShareActivity.class,
                            VipParam.shareType, "2", VipParam.title, getString(R.string.sharemall_vip_invite_shop));
                } else {
                    ToastUtils.showShort("抱歉，您还不是推广员！");
                }*/
                break;
            case R.id.tv_team_management:
                JumpUtil.overlay(getActivity(), VIPFollowerActivity.class);
                break;
            case R.id.ll_my_earnings://我的收益
 /*               Bundle bundle = new Bundle();
                bundle.putString(VipParam.type, "4");
                JumpUtil.overlay(getContext(), PerformanceManagementActivity.class, bundle);*/
                JumpUtil.overlay(getContext(), WalletActivity.class);
                break;
           /* case R.id.ll_wallet://我的钱包
                JumpUtil.overlay(getContext(), WalletActivity.class);
                break;*/
            case R.id.tv_my_collect://我的关注
                JumpUtil.overlay(getContext(), MyCollectionActivity.class);
                break;
            case R.id.ll_hai: //海贝综合
                JumpUtil.overlay(getContext(), HaiBeiConfidenceActivity.class);
                break;
            case R.id.rl_member:
                doAgreement(100);
                break;
        }
    }

    private void setMessageCount(int unreadNum) {
        if (unreadNum > 0) {
            mBinding.viewUnread.setVisibility(View.VISIBLE);
        } else {
            mBinding.viewUnread.setVisibility(View.GONE);
        }
    }

    //获取用户信息
    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.<BaseData<ShareMallUserInfoEntity>>compose())
                .compose((this).<BaseData<ShareMallUserInfoEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData<ShareMallUserInfoEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<ShareMallUserInfoEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            loadUserInfo();
                            doGetOrderCount();
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

    private void startOrder(int state) {
        JumpUtil.overlay(getContext(), MineOrderActivity.class, new FastBundle().putInt(ORDER_STATE, state));
    }


    //请求订单角标显示的信息
    private void doGetOrderCount() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getOrderCount(0)
                .compose(RxSchedulers.<BaseData<OrderCountEntity>>compose())
                .compose((this).<BaseData<OrderCountEntity>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<OrderCountEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<OrderCountEntity> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            mBinding.setOrderCount(data.getData());
                            doMineIntegralNum();
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
     * 获取我的积分数量和优惠券数量
     */
    private void doMineIntegralNum() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineIntegralNum("default")
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<MineIntegralNumEntity>>() {
                    @Override
                    public void onSuccess(BaseData<MineIntegralNumEntity> data) {
                        mBinding.setMyNum(data.getData());
                        mUserUsableIntegralNum = data.getData().getCoin();
                        doMineGrowthInfo();
                    }
                });
    }

    //请求当前个人成长值信息
    private void doMineGrowthInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doMineGrowthInfo("defaultData")
                .compose(RxSchedulers.compose())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<MineGrowthEntity>>() {
                    @Override
                    public void onSuccess(BaseData<MineGrowthEntity> data) {
                        growthEntity = data.getData();
                        doGetVIPUserInfo();
                    }

                });
    }

    //请求注册协议
    protected void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new BaseObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onNext(BaseData<AgreementEntity> baseData) {
                        if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                            if (baseData.getData() != null) {
                                Intent intent = new Intent(getContext(), BaseWebviewActivity.class);
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, baseData.getData().getTitle());
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, baseData.getData().getLink_type() == 2 ?
                                        BaseWebviewActivity.WEBVIEW_TYPE_URL : BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, baseData.getData().getContent());
                                startActivity(intent);
                            } else {
                                showError("暂无相关信息！");
                            }
                        } else {
                            showError(baseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });
    }

    //获取余额相关信息
    private void doGetVIPUserInfo() {
        if (ShareMallUserInfoCache.get().isVip()) {
            RetrofitApiFactory.createApi(VIPApi.class)
                    .getMyVIPIncomeInfo(null)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new FastObserver<BaseData<MyVIPIncomeInfoEntity>>() {

                        @Override
                        public void onSuccess(BaseData<MyVIPIncomeInfoEntity> data) {
                            if (dataExist(data)) {
                                vipIncomeInfoEntity = data.getData();
                                mBinding.setVip(vipIncomeInfoEntity);
                            }
                        }

                    });
        }
        doGetHai();
    }

    private void doGetHai() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getMinHai(1)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<HaiBeiConfidenceEntity>>() {

                    @Override
                    public void onSuccess(BaseData<HaiBeiConfidenceEntity> data) {
                        if (dataExist(data)) {
                            mBinding.setSynthesize(data.getData().getSynthesize());
                            mBinding.setSynthesizeYesterday(data.getData().getSynthesize_yesterday());
                            mBinding.setSynthesizeMatch(data.getData().getSynthesize_match());
                            Log.e("weng",data.getData().getSynthesize_match());
                            Log.e("weng",mBinding.getSynthesizeMatch());
                            List<String> listY = new ArrayList<>();
                            List<String> listX = new ArrayList<>();

                            if (data.getData().getList().size() > 0) {
                                for (HaiEntity entity : data.getData().getList()) {
                                    listY.add(entity.getSynthesize());
                                    listX.add(MyTimeUtil.getStringTime3(entity.getTime()));
                                }
                                String line = "line";
                                meth = "javascript:createChart('" + line + "','" + JSON.toJSONString(listY) + "','" + JSON.toJSONString(listX) + "')";
//                                mBinding.wv.loadUrl(meth);//信心指数
                                doEcharts(mBinding.wv, meth);
                            }

                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void doWebView(WebView webView, String method) {
        //开启本地文件读取（默认为true，不设置也可以）
        webView.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/echart/echart.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mBinding.wvEquity.loadUrl(meth1);
                doEcharts(webView, method);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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

}
