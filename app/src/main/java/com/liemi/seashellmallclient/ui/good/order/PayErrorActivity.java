package com.liemi.seashellmallclient.ui.good.order;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.good.PayErrorGoods;
import com.liemi.seashellmallclient.databinding.ActivityPayErrorBinding;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderActivity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;

public class PayErrorActivity extends BaseActivity<ActivityPayErrorBinding> {
    public static final String PAY_FAIL_GOODS = "payFailGoods";

    private BaseRViewAdapter<PayErrorGoods, BaseViewHolder> adapter;

    private PayErrorGoods payErrorGoods;

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_error;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_pay_failure));

        mBinding.tvTipDes.setText(Html.fromHtml(getString(R.string.sharemall_pay_failure_hint)));

        payErrorGoods = (PayErrorGoods) getIntent().getSerializableExtra(PAY_FAIL_GOODS);


        mBinding.rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseRViewAdapter<PayErrorGoods, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_pay_error_goods;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                };
            }
        };
        mBinding.rvGoods.setAdapter(adapter);
        if (payErrorGoods != null
                && !Strings.isEmpty(payErrorGoods.getGoodsList())) {
            adapter.setData(payErrorGoods.getGoodsList());
        }

    }

    @Override
    protected void initData() {
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_read_order) {
            Class<? extends Activity> cls = MineOrderActivity.class;
            AppManager.getInstance().finishActivity(cls);
            JumpUtil.overlay(this, cls);
            finish();
        } else if (i == R.id.tv_back_home) {
            MApplication.getInstance().backHome();
        }
    }
}
