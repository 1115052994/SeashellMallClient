package com.liemi.seashellmallclient.ui.mine.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityWalletExtractBinding;
import com.liemi.seashellmallclient.databinding.ActivityWalletExtractResultBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;

public class WalletExtractResultActivity extends BaseActivity<ActivityWalletExtractResultBinding> {


    private String type;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_extract_result;
    }

    @Override
    protected void initUI() {
        type = getIntent().getStringExtra("type");
        boolean success = getIntent().getBooleanExtra("success", false);
        if (success) {
            if (TextUtils.equals(type,"extract")){
                mBinding.tvPayError.setText("提取成功");
            }else {
                mBinding.tvPayError.setText("转赠成功");
            }

            mBinding.ivError.setImageResource(R.mipmap.sharemall_ic_result_success);
        } else {
            if (TextUtils.equals(type,"extract")){
                mBinding.tvPayError.setText("提取失败");
            }else {
                mBinding.tvPayError.setText("转赠失败");
            }
            mBinding.ivError.setImageResource(R.mipmap.sharemall_ic_result_fail);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_finish) {
            finish();
        }
    }
}
