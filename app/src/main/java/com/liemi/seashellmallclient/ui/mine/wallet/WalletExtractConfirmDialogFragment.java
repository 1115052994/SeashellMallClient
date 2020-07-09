package com.liemi.seashellmallclient.ui.mine.wallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.api.WalletApi;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletPoundageEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.GoodsBuyDialogParam;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallDialogFragmentGoodsBuyBinding;
import com.liemi.seashellmallclient.databinding.SharemallDialogFragmentWalletExtractConfirmBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemGoodsSpecsBinding;
import com.liemi.seashellmallclient.ui.good.SpecsTagAdapter;
import com.liemi.seashellmallclient.ui.good.order.FillOrderActivity;
import com.liemi.seashellmallclient.ui.login.ForgetPwdActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.zhy.view.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.liemi.seashellmallclient.data.param.GoodsBuyDialogParam.GOODS_BUY_ADD_SHOPCART;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 18:20
 * 修改备注：
 */
public class WalletExtractConfirmDialogFragment extends BaseDialogFragment<SharemallDialogFragmentWalletExtractConfirmBinding> implements DialogInterface.OnKeyListener {

    private String walletExtract;

    private String poundage;

    private String id;
    private String title;
    private String limit;
    private boolean isVis;
    private String type;

    public OnDialogListener mlistener;

    public interface OnDialogListener {
        void onDialogClick(String wallet);
    }
    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mlistener = dialogListener;
    }

    public WalletExtractConfirmDialogFragment setWalletExtract(String walletExtract,String type) {
        this.walletExtract = walletExtract;
        this.type = type;
        return this;
    }

    public WalletExtractConfirmDialogFragment setWalletTransfer(String id,String title,String limit,boolean isVis) {
        this.id = id;
        this.title = title;
        this.limit = limit;
        this.isVis = isVis;
        return this;
    }

    @Override
    public int getTheme() {
        return R.style.sharemall_CustomDialog;
    }

    private boolean hideDialog;

    public void setHideDialog(boolean hideDialog) {
        this.hideDialog = hideDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //避免页面返回自动弹出
        if (hideDialog) {
            onStop();
            setHideDialog(false);
        }else {
            doWalletPoundage(walletExtract);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        getDialog().setOnKeyListener(this);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBinding == null)
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),getContentView(),container,false);
        return mBinding.getRoot();
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_dialog_fragment_wallet_extract_confirm;
    }

    @Override
    protected void initUI() {
        mBinding.setDoClick(this);
    }

    @Override
    protected void initData() {
        doWalletPoundage(walletExtract);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onStop();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_confirm) {
            //交易密码
            if (!TextUtils.equals(type,"buy")){
                double money = Double.valueOf(walletExtract) + Double.valueOf(poundage);
                mlistener.onDialogClick(FloatUtils.formatDouble(money));
            }else {
                mlistener.onDialogClick(walletExtract);
            }
            onStop();
        } else if (i == R.id.view_bg) {
            onStop();
        } else if (i == R.id.iv_cancel) {
            onStop();
        }
    }

    private void doWalletPoundage(String walletExtract) {
        RetrofitApiFactory.createApi(WalletApi.class)
                .doWalletInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<WalletInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<WalletInfoEntity> data) {
                        String extract_fee_rate = "";
                        if (dataExist(data)){
                            if (TextUtils.equals(type,"extract")){ //提取
                                extract_fee_rate = data.getData().getExtract_fee_rate();
                            }else {
                                //转账
                                extract_fee_rate = AppConfigCache.get().getPlatformEntity().getCharge();
                            }
                            Double fee_rate = Double.valueOf(extract_fee_rate);
                            double fee = Double.valueOf(walletExtract) * fee_rate;
                            poundage = FloatUtils.formatDouble(fee);

                            mBinding.setWalletExtract(walletExtract);
                            mBinding.setPoundage(poundage);

                            mBinding.setId(id);
                            if (TextUtils.isEmpty(title)){

                            }else {
                                mBinding.tvTitle.setText(title);
                                mBinding.tvLimit.setText(limit);
                                if (isVis) {
                                    mBinding.llId.setVisibility(View.GONE);
                                    mBinding.view.setVisibility(View.GONE);
                                } else {
                                    mBinding.llId.setVisibility(View.VISIBLE);
                                    mBinding.view.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(BaseData<WalletInfoEntity> data) {
                        super.onFail(data);
                        onStop();
                    }
                });
    }

}
