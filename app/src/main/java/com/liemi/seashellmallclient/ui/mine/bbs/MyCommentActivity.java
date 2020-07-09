package com.liemi.seashellmallclient.ui.mine.bbs;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.ArticleApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.article.MyCommentEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.databinding.ActivityMyCommentBinding;
import com.liemi.seashellmallclient.databinding.MycommentItemCommentBinding;
import com.liemi.seashellmallclient.ui.mine.address.AddressDialog;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

public class MyCommentActivity extends BaseXRecyclerActivity<ActivityMyCommentBinding, MyCommentEntity> {

    private AddressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_comment;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("我的评论");
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        initAdapter();
    }

    private void initAdapter() {
        adapter=new BaseRViewAdapter<MyCommentEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.mycomment_item_comment;
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (holder.itemView != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (isCommentItem(position)) {
                                holder.doLongClick(v);
                            }
                            return true;
                        }
                    });
                }
            }

            public boolean isCommentItem(int position) {
                return getItem(position) instanceof MyCommentEntity;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<MyCommentEntity>(binding) {
                    @Override
                    public void bindData(MyCommentEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                    }

                    @Override
                    public void doLongClick(View view) {
                        super.doLongClick(view);
                        if (mDialog == null) {
                            mDialog = new AddressDialog(getContext());
                        }
                        if (!mDialog.isShowing()) {
                            mDialog.show();
                        }

                        mDialog.setTitle(getString(R.string.sharemall_hint2));
                        mDialog.setMessage("确定删除该评论吗？");
                        mDialog.setConfirm(getString(R.string.sharemall_confirm_ok));
                        mDialog.setClickConfirmListener(new AddressDialog.ClickConfirmListener() {
                            @Override
                            public void clickConfirm() {
                                doDelComment(getItem(position).getId());
                            }
                        });
                    }

                    @Override
                    public MycommentItemCommentBinding getBinding() {
                        return (MycommentItemCommentBinding) super.getBinding();
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .getMyArticleCommentList(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.<BaseData<PageEntity<MyCommentEntity>>>compose())
                .compose((this).<BaseData<PageEntity<MyCommentEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<MyCommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<MyCommentEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            showData(data.getData());
                        }
                    }

                });
    }

    private void doDelComment(String id) {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .doDelComment(id)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError("删除成功");
                        xRecyclerView.refresh();
                    }

                });
    }
}
