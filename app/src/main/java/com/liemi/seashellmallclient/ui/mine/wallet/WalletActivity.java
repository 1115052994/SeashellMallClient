package com.liemi.seashellmallclient.ui.mine.wallet;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivityWalletBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.seashellmallclient.ui.mine.wallet.WalletRechargeActivity.RECHARGE_ADDRESS;
import static com.liemi.seashellmallclient.ui.mine.wallet.WalletRechargeActivity.RECHARGE_ADDRESS_CODE;

public class WalletActivity extends BaseActivity<ActivityWalletBinding> {

    private String extract_remark; //提取备注
    private String recharge_remark; // 充值备注
    private String transfer_remark; //转账备注
    private WalletInfoEntity walletInfoEntity;
    private String receive_remark;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_wallet));
    }

    @Override
    protected void initData() {
        doGetWalletMessage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        doGetWalletMessage();
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        switch (id) {
            case R.id.tv_details://明细
                JumpUtil.overlay(getContext(), WalletRecordActivity.class);
                break;
            case R.id.ll_top_up://充值
                if (walletInfoEntity != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(RECHARGE_ADDRESS, walletInfoEntity.getAddress());
                    bundle1.putString(RECHARGE_ADDRESS_CODE, walletInfoEntity.getAddress_qrcode());
                    bundle1.putString("remark", recharge_remark);
                    bundle1.putString("type", "recharge");
                    JumpUtil.overlay(getContext(), WalletRechargeActivity.class, bundle1);
                }
                break;
            case R.id.ll_extract://提取
                Bundle bundle = new Bundle();
                bundle.putString("text", extract_remark);
                JumpUtil.overlay(getContext(), WalletExtractActivity.class, bundle);
                break;
            case R.id.ll_transfer://转账
                Bundle bundle3 = new Bundle();
                bundle3.putString("address", "");
                bundle3.putString("remark", transfer_remark);
                JumpUtil.overlay(getContext(), WalletTransferActivity.class, bundle3);
                break;
            case R.id.ll_receipt://收款
                if (walletInfoEntity != null) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(RECHARGE_ADDRESS, walletInfoEntity.getShare_code());
                    bundle2.putString(RECHARGE_ADDRESS_CODE, walletInfoEntity.getShare_code_qrcode());
                    bundle2.putString("remark", receive_remark);
                    bundle2.putString("type", "receipt");
                    JumpUtil.overlay(getContext(), WalletRechargeActivity.class, bundle2);
                }

                break;
        }
    }

    private void doGetWalletMessage() {
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<WalletInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<WalletInfoEntity> data) {
                        if (dataExist(data)) {
                            walletInfoEntity = data.getData();
                            mBinding.setMoney(data.getData().getMoney());
                            mBinding.setHai(data.getData().getHand_balance());
                            mBinding.setWait(TextUtils.isEmpty(data.getData().getFreeze_price()) ? "0.00" : data.getData().getFreeze_price());
                            mBinding.setWaitMoney("≈" + data.getData().getFreeze_money() + "元");
                            mBinding.setEarn("海贝兑换预估：" + data.getData().getHand_yugu() + "元");
                            transfer_remark = data.getData().getTransfer_remark_user();
                            receive_remark = data.getData().getReceive_remark_user();
                            extract_remark = data.getData().getExtract_remark_user();
                            recharge_remark = data.getData().getRecharge_remark_user();
                        }
                    }
                });
    }
}
