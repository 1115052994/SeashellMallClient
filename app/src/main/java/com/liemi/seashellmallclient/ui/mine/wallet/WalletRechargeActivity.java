package com.liemi.seashellmallclient.ui.mine.wallet;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityWalletRechargeBinding;
import com.liemi.seashellmallclient.utils.QRCodeUtils;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.Date;

import io.reactivex.functions.Consumer;

public class WalletRechargeActivity extends BaseActivity<ActivityWalletRechargeBinding> {

    public static final String RECHARGE_ADDRESS = "RechargeAddress";
    public static final String RECHARGE_ADDRESS_CODE = "RechargeAddressCode";
    private String rechargeAddress;
    private String rechargeAddressCode;
    private String type;
    private String remark;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_recharge;
    }

    @Override
    protected void initUI() {
        type = getIntent().getStringExtra("type");
        if (TextUtils.equals(type,"recharge")){
            getTvTitle().setText("充入");
        }else {
            getTvTitle().setText("接收");
        }

    }

    @Override
    protected void initData() {
        doGetWalletMessage();
        ShareMallUserInfoEntity entity = UserInfoCache.get(ShareMallUserInfoEntity.class);
        mBinding.setItem(entity);
        mBinding.executePendingBindings();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_copy){ //复制
            copy(rechargeAddress);
            return;
        }else if (view.getId() == R.id.tv_save){ //保存至相册
            if (!TextUtils.isEmpty(rechargeAddressCode)) {
                new RxPermissions(getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    showProgress(getString(R.string.sharemall_downloading));
                                    Glide.with(getActivity()).asBitmap().load(rechargeAddressCode).into(new SimpleTarget<Bitmap>() {
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            saveBitmapToFile(resource);
                                        }
                                    });
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                } else {
                                    // 用户拒绝了该权限，并且选中『不再询问』
                                }
                            }
                        });
            } else {
                ToastUtils.showShort(getString(R.string.sharemall_lack_save_pic));
            }
            return;
        }
    }

    /*
     * 复制到剪切板
     * */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showError("已复制");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveBitmapToFile(Bitmap croppedImage) {
        hideProgress();
        try {
            String path = MediaStore.Images.Media.insertImage(getBaseContext().getContentResolver(), croppedImage, String.valueOf(new Date().getTime()), "");

            if (Strings.isEmpty(path)){
                ToastUtils.showShort(getString(R.string.sharemall_save_failure));
                return;
            }

            ToastUtils.showShort(getString(R.string.sharemall_saved_path,path.substring(0, path.lastIndexOf(File.separator))));

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse(path);
            intent.setData(uri);
            getBaseContext().sendBroadcast(intent);
        }catch (Exception e){
            ToastUtils.showShort(getString(R.string.sharemall_save_failure));
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
                            if (TextUtils.equals(type,"recharge")){ //充值
                                rechargeAddress = data.getData().getAddress();
                                rechargeAddressCode = data.getData().getAddress_qrcode();
                                remark = data.getData().getRecharge_remark_user();
                            }else {
                                rechargeAddress = data.getData().getShare_code();
                                rechargeAddressCode = data.getData().getShare_code_qrcode();
                                remark = data.getData().getReceive_remark_user();
                            }
                            mBinding.setRemark(remark);
                            mBinding.setAddress(rechargeAddress);
                            mBinding.setCode(rechargeAddressCode);
                            if (TextUtils.equals(type,"recharge")){
                                mBinding.tvAddress.setText(rechargeAddress);
                            }else {
                                mBinding.tvAddress.setText("ID："+rechargeAddress);
                            }
                            Bitmap codeBitmap = QRCodeUtils.createQRCodeBitmap(rechargeAddress, 600, 600,"UTF-8","H","2", Color.BLACK,Color.WHITE);
                            mBinding.ivQrcode.setImageBitmap(codeBitmap);
                        }
                    }
                });
    }

}
