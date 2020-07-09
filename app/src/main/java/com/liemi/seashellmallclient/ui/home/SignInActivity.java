package com.liemi.seashellmallclient.ui.home;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.HomeApi;
import com.liemi.seashellmallclient.data.entity.SignInfoEntity;
import com.liemi.seashellmallclient.data.entity.SignRecordEntity;
import com.liemi.seashellmallclient.databinding.SharemallActivitySignInBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemSignRecordBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemSignTaskBinding;
import com.liemi.seashellmallclient.ui.good.SpecsTagAdapter;
import com.liemi.seashellmallclient.widget.SignRoleDialog;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends BaseActivity<SharemallActivitySignInBinding> {

    private SignInfoEntity signInfoEntity;

    private SpecsTagAdapter adapter;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.tranStatusBar(this, false);
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_sign_in;
    }

    @Override
    protected void initUI() {
        mBinding.rvTask.setNestedScrollingEnabled(false);
        mBinding.rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_sign_in) {
            doSign();
        } else if (i == R.id.iv_right) {
            new SignRoleDialog(SignInActivity.this).show();
        } else if (i == R.id.iv_back) {
            finish();
        }
    }

    @Override
    protected void initData() {
        doGetSignInfo();
    }

    private void initTask(List<SignInfoEntity.ListBean> tasks) {
        BaseRViewAdapter<SignInfoEntity.ListBean, BaseViewHolder> adapter
                = new BaseRViewAdapter<SignInfoEntity.ListBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_sign_task;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<SignInfoEntity.ListBean>(binding) {
                    @Override
                    public void bindData(SignInfoEntity.ListBean item) {
                        super.bindData(item);
                        if (position % 2 == 0) {
                            getBinding().ivIc.setImageResource(R.mipmap.sharemall_ic_points);
                        } else {
                            getBinding().ivIc.setImageResource(R.mipmap.sharemall_ic_growth);
                        }
                    }

                    @Override
                    public SharemallItemSignTaskBinding getBinding() {
                        return (SharemallItemSignTaskBinding) super.getBinding();
                    }
                };
            }
        };
        mBinding.rvTask.setAdapter(adapter);
        adapter.setData(tasks);
    }

    private void initContinueDay() {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(String.valueOf(i + 1));
        }
        mBinding.recordLabel.setAdapter(adapter = new SpecsTagAdapter<String>(numbers) {
            @Override
            public View getView(FlowLayout parent, int position, String text) {
                SharemallItemSignRecordBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.sharemall_item_sign_record, parent, false);
                binding.setText(text);
                binding.setIsEnable(Integer.parseInt(text) <= signInfoEntity.getContinue_day());
                return binding.getRoot();
            }
        });
    }

    private void refreshSignUI() {
        //已签到
        if (signInfoEntity.getIs_sign() == 1) {
            mBinding.tvSignIn.setEnabled(false);
            mBinding.tvSignIn.setBackgroundResource(R.mipmap.sharemall_bg_btn_gray_shadow);
            mBinding.tvSignIn.setText(getString(R.string.sharemall_format_sign_today_coin, signInfoEntity.getReceive_coin()));
        } else {
            mBinding.tvSignIn.setEnabled(true);
            mBinding.tvSignIn.setBackgroundResource(R.mipmap.sharemall_bg_btn_shadow);
            mBinding.tvSignIn.setText(getString(R.string.sharemall_sign_now));
        }
        if (adapter != null) {
            adapter.notifyDataChanged();
        }
    }

    private void doSign() {
        showProgress("");
        RetrofitApiFactory.createApi(HomeApi.class)
                .doSign(AccessTokenCache.get().getToken())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        doGetSignInfo();
                    }

                });
    }

    private void doGetSignRecord() {
        RetrofitApiFactory.createApi(HomeApi.class)
                .doGetSignRecord(AccessTokenCache.get().getToken())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<SignRecordEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<SignRecordEntity> data) {
                        List<Integer> signData = new ArrayList<>();
                        SignRecordEntity signRecordEntity = data.getData();
                        mBinding.setData(signRecordEntity);
                        for (SignRecordEntity.ListBean bean : signRecordEntity.getList()) {
                            if (bean.getStatus() == 1) {
                                signData.add(bean.getDay() - 1);//控件首位置为0，所以需要减一
                            }
                        }
                        mBinding.calendarView.setDays(signData);
                    }

                });
    }

    private void doGetSignInfo() {
        showProgress("");
        RetrofitApiFactory.createApi(HomeApi.class)
                .doGetSignInfo(AccessTokenCache.get().getToken())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<SignInfoEntity>>() {

                    @Override
                    public void onSuccess(BaseData<SignInfoEntity> data) {
                        if (dataExist(data)) {
                            signInfoEntity = data.getData();
                            mBinding.setItem(signInfoEntity);
                            refreshSignUI();
                            initContinueDay();
                            initTask(data.getData().getList());
                        }
                    }

                    @Override
                    public void onComplete() {
                        doGetSignRecord();
                    }

                });
    }
}
