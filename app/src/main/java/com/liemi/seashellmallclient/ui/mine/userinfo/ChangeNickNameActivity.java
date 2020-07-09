package com.liemi.seashellmallclient.ui.mine.userinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.databinding.ActivityChangeNickNameBinding;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class ChangeNickNameActivity extends BaseActivity<ActivityChangeNickNameBinding> {

    @Override
    protected int getContentView() {
        return R.layout.activity_change_nick_name;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_change_nick_name));
        mBinding.etNickname.setText(UserInfoCache.get().getNickname());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_save) {
            if (TextUtils.isEmpty(mBinding.etNickname.getText().toString())) {
                showError(getString(R.string.sharemall_input_new_nick_name));
                return;
            }
            if (mBinding.etNickname.getText().toString().equals(UserInfoCache.get().getNickname())) {
                showError(getString(R.string.sharemall_input_new_nick_name));
                return;
            }
            if (TextUtils.isEmpty(mBinding.etNickname.getText().toString().trim())) {
                showError(getString(R.string.sharemall_nick_name_not_all_space));
                return;
            }
            if (mBinding.etNickname.getText().toString().startsWith(" ") || mBinding.etNickname.getText().toString().endsWith(" ")) {
                showError(getString(R.string.sharemall_nick_name_not_with_space));
                return;
            }
            doUpdateUserInfo(mBinding.etNickname.getText().toString());
        }
    }

    private void doUpdateUserInfo(String nickName) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserInfoUpdate(null, nickName, null, null, null, null, null)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        hideProgress();
                        UserInfoCache.get().setNickname(nickName);
                        finish();
                    }
                });
    }
}
