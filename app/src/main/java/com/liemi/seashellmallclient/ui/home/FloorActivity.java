package com.liemi.seashellmallclient.ui.home;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallActivityFragmentBinding;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.seashellmallclient.ui.home.floor.FloorClickUtils.FLOOR_PARAM;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/23
 * 修改备注：
 */
public class FloorActivity extends BaseActivity<SharemallActivityFragmentBinding> {

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_fragment;
    }

    @Override
    protected void initUI() {
        String usePosition = getIntent().getStringExtra(FLOOR_PARAM);

        if (TextUtils.isEmpty(usePosition)) {
            ToastUtils.showShort(R.string.sharemall_no_data);
            finish();
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, HomeCategoryFragment.newInstance(usePosition, getIntent().getStringExtra(GoodsParam.STORE_ID)));
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

}
