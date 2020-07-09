package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.databinding.ActivityLocalLifeShopCommentAllBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.seashellmallclient.ui.locallife.BaseShopDetailedFragment.ITEM_ID;

public class LocalLifeShopCommentAllActivity extends BaseActivity<ActivityLocalLifeShopCommentAllBinding> {

    private String itemId;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public LocalLifeShopCommentFragment f1;
    public LocalLifeShopCommentFragment f2;

    @Override
    protected int getContentView() {
        return R.layout.activity_local_life_shop_comment_all;
    }

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }


    @Override
    protected void initUI() {
        Intent intent = getIntent();
        itemId = intent.getStringExtra(ITEM_ID);
        String title = intent.getStringExtra("title");
        f1 = LocalLifeShopCommentFragment.newInstance(itemId, "");
        f2 = LocalLifeShopCommentFragment.newInstance(itemId, "1");
        mBinding.tvTitle.setText(title);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mBinding.rgCommit.check(R.id.rb_all);
        f1 = LocalLifeShopCommentFragment.newInstance(itemId, "");
        transaction.replace(R.id.frame, f1);
        transaction.commit();


        mBinding.rgCommit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                manager = getActivity().getSupportFragmentManager();
                transaction = manager.beginTransaction();
                int i = group.getCheckedRadioButtonId();
                if (i == R.id.rb_all) {
                    hideFragment(transaction);
                    f1 = LocalLifeShopCommentFragment.newInstance(itemId, "");
                    transaction.replace(R.id.frame, f1);
                    transaction.commit();

                } else if (i == R.id.rb_pic) {
                    hideFragment(transaction);
                    f2 = LocalLifeShopCommentFragment.newInstance(itemId, "1");
                    transaction.replace(R.id.frame, f2);
                    transaction.commit();

                }
            }
        });
    }

    @Override
    protected void initData() {
        doListComment();
    }



    private void hideFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.remove(f1);
        }
        if (f2 != null) {
            transaction.remove(f2);
        }
    }

    private void doListComment() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getCommentList(0, Constant.PAGE_ROWS,"",itemId,"0")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        mBinding.rbAll.setText(getString(R.string.sharemall_all) + "·" + data.getData().getSum_commet_num());
                        mBinding.rbPic.setText(getString(R.string.sharemall_patterned) + "·" + data.getData().getPic_commet_num());
                    }

                });
    }
}
