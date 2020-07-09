package com.liemi.seashellmallclient.ui.mine.wallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.adapters.TextViewBindingAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivityWalletExtractBinding;
import com.liemi.seashellmallclient.databinding.ActivityWalletTransferBinding;
import com.liemi.seashellmallclient.ui.locallife.LocalLifeShopPayActivity;
import com.liemi.seashellmallclient.ui.login.ForgetPwdActivity;
import com.liemi.seashellmallclient.ui.login.SetPayPasswordActivity;
import com.liemi.seashellmallclient.widget.PayDialog;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;

public class WalletTransferActivity extends BaseActivity<ActivityWalletTransferBinding> implements TextViewBindingAdapter.AfterTextChanged, WalletExtractConfirmDialogFragment.OnDialogListener {
    private WalletExtractConfirmDialogFragment walletExtractConfirmDialogFragment;
    private String hand_balance;
    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_transfer;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_transfer));
        getRightSettingImage().setVisibility(View.VISIBLE);
        getRightSettingImage().setImageResource(R.mipmap.sharemall_ic_scan_qr);
        String transfer_remark = getIntent().getStringExtra("remark");
        mBinding.tvRemark.setText(transfer_remark);
    }

    @Override
    protected void initData() {
        mBinding.setAfterTextListener(this);
        String address = getIntent().getStringExtra("address");
        if (!TextUtils.isEmpty(address)){
            mBinding.etAddress.setText(address);
        }
        doGetWalletMessage();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_withdraw) { //转账
            applyWithdraw(mBinding.etPrice.getText().toString().trim());
            return;
        }else if (i == R.id.iv_setting){
            Bundle bundle = new Bundle();
            JumpUtil.startForResult(getActivity(), CaptureQRCodeActivity.class, 1002,bundle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == 10002 && data != null) {
            String scanResult = data.getStringExtra("scan_result");
            if (scanResult.contains("shop_id")) { //线下买单
                String shop_id = scanResult.substring(scanResult.indexOf("=") + 1, scanResult.indexOf("&"));
                String shop_name = scanResult.substring(scanResult.lastIndexOf("=")+1, scanResult.length());
                if (MApplication.getInstance().checkUserIsLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SHOP_ID, shop_id);
                    bundle.putString("title", shop_name);
                    JumpUtil.overlay(getActivity(), LocalLifeShopPayActivity.class, bundle);
                }
            }else {
                if (AppManager.getInstance().existActivity(CaptureQRCodeActivity.class)) {
                    AppManager.getInstance().finishActivity(CaptureQRCodeActivity.class);
                }
                mBinding.etAddress.setText(scanResult);
            }
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

    //申请转账
    private void applyWithdraw(String priceTotal) {

        if (Strings.isEmpty(priceTotal) || !Strings.isNumber(priceTotal)) { //转账金额
            ToastUtils.showShort(getString(R.string.sharemall_transfer_limit));
            return;
        }
        double price = Double.parseDouble(priceTotal);

        if ((!TextUtils.isEmpty(hand_balance) && price > Strings.toDouble(hand_balance)) || price <=0 ) {
            ToastUtils.showShort("钱包余额不足！");
            return;
        }

        if (walletExtractConfirmDialogFragment == null) {
            walletExtractConfirmDialogFragment = new WalletExtractConfirmDialogFragment()
                    .setWalletExtract(priceTotal,"transfer")
                    .setWalletTransfer(mBinding.etAddress.getText().toString().trim(),"确认发送",getString(R.string.sharemall_transfer_limit),false);
            walletExtractConfirmDialogFragment.show(getSupportFragmentManager(), TAG);
        } else {
            walletExtractConfirmDialogFragment.setWalletExtract(priceTotal,"transfer");
            walletExtractConfirmDialogFragment.setWalletTransfer(mBinding.etAddress.getText().toString().trim(),"确认发送",getString(R.string.sharemall_transfer_limit),false);
            walletExtractConfirmDialogFragment.onStart();
        }
        walletExtractConfirmDialogFragment.setOnDialogListener(this);

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
                            mBinding.setBalance(hand_balance);
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
        String price = wallet;
        PayDialogFragment payDialogFragment = new PayDialogFragment("请输入交易密码");
        payDialogFragment.setPasswordCallback((String password) -> doCheckPayPWD(password, payDialogFragment));
        payDialogFragment.show(getSupportFragmentManager(), "payFragment");
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
                        String price1 = mBinding.etPrice.getText().toString().trim();
                        doWalletExtract(mBinding.etAddress.getText().toString().trim(),price1,MD5.GetMD5Code(password));
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

    private void doWalletExtract(String address, String price,String password) {
        showProgress("");
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletTransfer(price,address,password)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type","transfer");
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
