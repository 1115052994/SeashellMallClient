package com.liemi.seashellmallclient.ui.good.comment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentGoodCommetBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;

import com.trello.rxlifecycle2.android.FragmentEvent;

public class GoodCommentFragment extends BaseFragment<SharemallFragmentGoodCommetBinding> {
    private String itemId;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public CommentFragment f1;
    public CommentFragment f2;


    public static GoodCommentFragment newInstance(String goodId) {
        GoodCommentFragment fragment = new GoodCommentFragment();
        Bundle args = new Bundle();
        args.putString(GoodsParam.ITEM_ID, goodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_good_commet;
    }

    @Override
    protected void initUI() {
        Bundle bundle = getArguments();
        itemId = bundle.getString(GoodsParam.ITEM_ID);
        f1 = CommentFragment.newInstance(itemId, "0");
        f2 = CommentFragment.newInstance(itemId, "1");
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mBinding.rgCommit.check(R.id.rb_all);
        f1 = CommentFragment.newInstance(itemId, "0");
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
                    f1 = CommentFragment.newInstance(itemId, "0");
                    transaction.replace(R.id.frame, f1);
                    transaction.commit();

                } else if (i == R.id.rb_pic) {
                    hideFragment(transaction);
                    f2 = CommentFragment.newInstance(itemId, "1");
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
        RetrofitApiFactory.createApi(OrderApi.class)
                .listComment(0, Constant.PAGE_ROWS, itemId, "0")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        mBinding.rbAll.setText(getString(R.string.sharemall_all) + "·" + data.getData().getSum_commet_num());
                        mBinding.rbPic.setText(getString(R.string.sharemall_patterned) + "·" + data.getData().getPic_commet_num());
                    }
                });
    }
}
