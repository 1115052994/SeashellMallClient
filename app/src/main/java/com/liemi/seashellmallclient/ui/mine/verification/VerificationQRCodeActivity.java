package com.liemi.seashellmallclient.ui.mine.verification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityVerificationQrcodeBinding;
import com.liemi.seashellmallclient.utils.QRCodeUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.Strings;

public class VerificationQRCodeActivity extends BaseActivity<ActivityVerificationQrcodeBinding> {

    //需要上个页面传递过来核销id
    public static final String VERIFICATION_ID = "verificationId";
    private String verificationCode;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_qrcode;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("二维码");
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        verificationCode = intent.getStringExtra(VERIFICATION_ID);
        String time = intent.getStringExtra("time");
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");
        if (Strings.toInt(status)!= OrderParam.VERIFICATION_WAIT_USE){
            if (Strings.toInt(status)== OrderParam.VERIFICATION_REFUND_SUCCESS){
                //TODO：退款成功
//                mBinding.ivStatus.setBackgroundResource(R.mipmap.ic_code_expired);
            }else {
                mBinding.ivStatus.setBackgroundResource(R.drawable.ic_code_used);
            }
            mBinding.rlDismiss.setVisibility(View.VISIBLE);
        }else {
            mBinding.rlDismiss.setVisibility(View.GONE);
        }
        mBinding.tvValidity.setText(time);
        mBinding.tvName.setText(name);
        Bitmap codeBitmap = QRCodeUtils.createQRCodeBitmap(verificationCode, 600, 600,"UTF-8","H","2", Color.BLACK,Color.WHITE);
        mBinding.ivQrcode.setImageBitmap(codeBitmap);
        String verificationCode = getVerificationCode(this.verificationCode);
        mBinding.tvVerificationCode.setText(verificationCode);
    }

    public String getVerificationCode(String code) {
        StringBuffer b=new StringBuffer();
        int i=0;
        int j=4;
        while (i<code.length()){
            String c;
            if (j>=code.length()){
                c = code.substring(i);
            }else {
                c = code.substring(i,j);
            }
            boolean digitsOnly = TextUtils.isDigitsOnly(c);
            if(digitsOnly){
                String s1 = c + " ";
                b.append(s1);
            }else{
                b.append(c);
            }
            i+=4;
            j+=4;
        }

        return String.valueOf(b);
    }
}
