package com.liemi.seashellmallclient.ui.mine.message;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.ArticleApi;
import com.liemi.seashellmallclient.data.entity.article.ShopCartArticleEntity;
import com.liemi.seashellmallclient.data.entity.article.ShopCartArticleTimeEntity;
import com.liemi.seashellmallclient.databinding.SharemallActivityXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemShopCartDynamicBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.entity.contacts.RecentContactEntity.MESSAGE_TYPE_ASSET;
import static com.liemi.seashellmallclient.data.entity.contacts.RecentContactEntity.MESSAGE_TYPE_SYSTEM;

public class NoticeMsgActivity extends BaseXRecyclerActivity<SharemallActivityXrecyclerviewBinding, ShopCartArticleEntity> {

    private static final String MESSAGE_TYPE = "messageType";

    private static final String MESSAGE_TITLE = "messageTitle";

    private int messageType = 1;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_xrecyclerview;
    }

    public static void start(Context context, String title, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_TITLE, title);
        bundle.putInt(MESSAGE_TYPE, type);
        JumpUtil.overlay(context, NoticeMsgActivity.class, bundle);
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra(MESSAGE_TITLE));
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<ShopCartArticleEntity, BaseViewHolder>(getContext()) {

            @Override
            public int layoutResId(int viewType) {
                if(viewType == MESSAGE_TYPE_ASSET) {
                    return R.layout.sharemall_item_asset_msg;
                } else {
                    return R.layout.sharemall_item_shop_cart_dynamic;
                }
            }

            @Override
            public int getItemViewType(int position) {
                return messageType;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<ShopCartArticleEntity>(binding) {

                    private String lastTime;

                    @Override
                    public void bindData(ShopCartArticleEntity item) {
                        if (getBinding() instanceof SharemallItemShopCartDynamicBinding) {
                            SharemallItemShopCartDynamicBinding binding = (SharemallItemShopCartDynamicBinding) getBinding();
                            String date = item.getMMDDDCreate_time();
                            if (!Strings.isEmpty(date) && !TextUtils.equals(date, lastTime)) {
                                lastTime = date;
                                binding.llTime.setVisibility(View.VISIBLE);
                            } else {
                                binding.llTime.setVisibility(View.GONE);
                            }
                        }
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.cardView) {
                            switch (getItem(position).getLink_type()) {
                                case 2:
                                case 3:
                                    String details = null;
                                    if (getItem(position).getNotice() != null) {
                                        //2017-09-21 13:45 | 阅读：352
                                        details = getItem(position).getMMDDHHMMCreate_time() + " | " + getString(R.string.sharemall_read) + "：" + getItem(position).getRead_num();
                                    }
                                    BaseWebviewActivity.start(getContext(),
                                            getItem(position).getTitle(),
                                            getItem(position).getUrl(), details);
                                    break;
                            }
                        }
                    }
                };
            }
        });
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    protected void initData() {
        messageType = getIntent().getIntExtra(MESSAGE_TYPE, MESSAGE_TYPE_SYSTEM);
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .getShopCartArticle(PageUtil.toPage(startPage), Constant.PAGE_ROWS, new String[]{String.valueOf(messageType)})
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<ShopCartArticleTimeEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<ShopCartArticleTimeEntity>> data) {
                        if (dataExist(data) && !Strings.isEmpty(data.getData().getList())) {
                            PageEntity<ShopCartArticleEntity> pageEntity = new PageEntity<>();
                            List<ShopCartArticleEntity> list = new ArrayList<>();
                            for (ShopCartArticleTimeEntity timeEntity : data.getData().getList()) {
                                if (!Strings.isEmpty(timeEntity.getList())) {
                                    list.addAll(timeEntity.getList());
                                }
                            }
                            pageEntity.setList(list);
                            pageEntity.setTotal_pages(data.getData().getTotal_pages());
                            showData(pageEntity);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (LOADING_TYPE == Constant.PULL_REFRESH) {
                            doSetReadAll();
                        }
                    }
                });
    }

    private void doSetReadAll() {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .setReadAll(String.valueOf(messageType))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {

                    }
                });
    }
}
