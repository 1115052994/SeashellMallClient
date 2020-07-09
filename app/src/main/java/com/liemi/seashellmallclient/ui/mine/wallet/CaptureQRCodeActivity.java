package com.liemi.seashellmallclient.ui.mine.wallet;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.databinding.ActivityCaptureQrcodeBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CaptureQRCodeActivity extends BaseActivity<ActivityCaptureQrcodeBinding> implements QRCodeView.Delegate {

    @Override
    protected int getContentView() {
        return R.layout.activity_capture_qrcode;
    }

    @Override
    protected void initUI() {
        mBinding.zxvScan.setDelegate(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Disposable disposable = new RxPermissions(this).requestEach(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            mBinding.zxvScan.startCamera();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            finish();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            finish();
                        }
                    }
                });
        mBinding.zxvScan.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        mBinding.zxvScan.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mBinding.zxvScan.onDestroy();
        super.onDestroy();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if(!TextUtils.isEmpty(result)){
            Intent intent = getIntent();
            Intent scanIntent = new Intent();
            scanIntent.putExtra("scan_result",result);
            setResult(10002,scanIntent);
            finish();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showError(getString(R.string.basemall_qrcode_scan_error));
    }
}
