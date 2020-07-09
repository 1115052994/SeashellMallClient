package com.liemi.seashellmallclient.ui.mine.wallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.cache.VipUserInfoCache;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.ApplyVIPWithdrawVo;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.data.param.LoginParam;
import com.liemi.seashellmallclient.databinding.ActivityWalletExtractBinding;
import com.liemi.seashellmallclient.ui.good.GoodsBuyDialogFragment;
import com.liemi.seashellmallclient.ui.login.ForgetPwdActivity;
import com.liemi.seashellmallclient.ui.login.SetPayPasswordActivity;
import com.liemi.seashellmallclient.widget.PayDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class WalletExtractActivity extends BaseActivity<ActivityWalletExtractBinding> implements TextViewBindingAdapter.AfterTextChanged, WalletExtractConfirmDialogFragment.OnDialogListener {

    private WalletExtractConfirmDialogFragment walletExtractConfirmDialogFragment;
    private String hand_balance;
    private String price;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_extract;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_extract));
    }

    @Override
    protected void initData() {
        mBinding.setAfterTextListener(this);
        String extract_remark = getIntent().getStringExtra("text");
        mBinding.tvRemark.setText(extract_remark);
        doGetWalletMessage();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_withdraw) { //提现
            applyWithdraw(mBinding.etPrice.getText().toString().trim());

        } else if (i == R.id.tv_all_withdrawal) { //全部
            mBinding.etPrice.setText(hand_balance);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (verifyInput(mBinding.etPrice.getText().toString().trim(),mBinding.etAddress.getText().toString().trim())) {
            mBinding.tvWithdraw.setEnabled(true);
            mBinding.tvWithdraw.setBackgroundResource(R.drawable.sharemall_radius_4dp_gradient_ef574c);
        } else {
            mBinding.tvWithdraw.setEnabled(false);
            mBinding.tvWithdraw.setBackgroundResource(R.drawable.basemall_radius_4dp_c4c4c4);
        }
    }

    private boolean verifyInput(String price,String address) {
        if (Strings.isEmpty(price) || !Strings.isNumber(price) || Strings.isEmpty(address)) {
            return false;
        }
        return true;
    }

    //申请提现
    private void applyWithdraw(String priceTotal) {

        if (Strings.isEmpty(priceTotal) || !Strings.isNumber(priceTotal)) { //提现金额
            ToastUtils.showShort(getString(R.string.sharemall_please_input_amount));
            return;
        }
        double price = Double.parseDouble(priceTotal);

        if ((!TextUtils.isEmpty(hand_balance) && price > Strings.toDouble(hand_balance)) || price <=0 ) {
            ToastUtils.showShort(R.string.sharemall_vip_withdraw_cash_not_enough);
            return;
        }

        if (walletExtractConfirmDialogFragment == null) {
            walletExtractConfirmDialogFragment = new WalletExtractConfirmDialogFragment().setWalletExtract(priceTotal,"extract");
            walletExtractConfirmDialogFragment.show(getSupportFragmentManager(), TAG);
        } else {
            walletExtractConfirmDialogFragment.setWalletExtract(priceTotal,"extract");
            walletExtractConfirmDialogFragment.onStart();
        }
        walletExtractConfirmDialogFragment.setOnDialogListener(this);

    }

    //提现注意事项
    private void doInitAgreement() {
        RetrofitApiFactory.createApi(LoginApi.class)
                .getAgreement(LoginParam.PROTOCOL_TYPE_WITHDRAW)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<AgreementEntity>>() {

                    @Override
                    public void onSuccess(BaseData<AgreementEntity> data) {
                        if (data.getData()!=null) {
                            // android 5.0及以上默认不支持Mixed Content
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mBinding.wvGuide.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                            }
                            if (!Strings.isEmpty(data.getData().getContent())) {
                                mBinding.wvGuide.loadData(data.getData().getContent(), "text/html; charset=UTF-8", null);
                            } else {
                                mBinding.wvGuide.loadUrl(Constant.BASE_HTML + data.getData().getParam());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        doGetWalletMessage();
                    }
                });
    }

    private void doGetWalletMessage() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<WalletInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<WalletInfoEntity> data) {
                        if (dataExist(data)){
                            hand_balance = data.getData().getHand_balance();
                        }
                    }
                });
    }

    @Override
    public void onDialogClick(String wallet) {
        if (UserInfoCache.get(ShareMallUserInfoEntity.class).getIs_set_paypassword() == 1) {
            createPayDialog(wallet);
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.sharemall_good_remind)
                    .setMessage(R.string.sharemall_not_set_balance_pay_password)
                    .setPositiveButton(R.string.sharemall_to_set_up, (DialogInterface dialog, int which) ->
//                            JumpUtil.overlay(getContext(), ForgetPwdActivity.class,"forget","pay"))
                            JumpUtil.overlay(getContext(), SetPayPasswordActivity.class))
                    .setNegativeButton(R.string.sharemall_cancel, null)
                    .show();
        }
    }

    private void createPayDialog(String wallet) {
        price = wallet;
        PayDialogFragment payDialogFragment = new PayDialogFragment("请输入交易密码");
        payDialogFragment.setPasswordCallback((String password) -> doCheckPayPWD(password, payDialogFragment));
        payDialogFragment.show(getSupportFragmentManager(), "payFragment");
/*        final PayDialog payDialog = new PayDialog(this);
        payDialog.setTitle("请输入交易密码");
        payDialog.setMoney(wallet);
        payDialog.getTvForgetPwd().setVisibility(View.INVISIBLE);
        payDialog.setPasswordCallback((String password) -> doCheckPayPWD(password, payDialog));
        payDialog.setOnDismissListener((DialogInterface dialogInterface) -> {
        });
        payDialog.setOnCancelListener((DialogInterface dialogInterface) -> {
        });
        payDialog.clearPasswordText();
        payDialog.show();*/
    }

    //验证支付密码
    private void doCheckPayPWD(final String password, final PayDialogFragment payDialog) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .checkPayPWD(MD5.GetMD5Code(password))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        payDialog.dismiss();
                        doWalletExtract(MD5.GetMD5Code(password),mBinding.etAddress.getText().toString().trim(),mBinding.etPrice.getText().toString().trim());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        payDialog.clearPasswordText();
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }
                });
    }

    private void doWalletExtract(String password, String address, String price) {
        showProgress("");
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletExtract(price,address,password)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type","extract");
                        if (TextUtils.equals(data.getErrmsg(),"success.")){
                            bundle.putBoolean("success", true);
                        }else {
                            bundle.putBoolean("success", false);
                        }
                        JumpUtil.overlay(getContext(),WalletExtractResultActivity.class,bundle);
                        finish();
                    }

                });
    }

}
