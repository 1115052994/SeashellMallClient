package com.liemi.seashellmallclient.ui.mine.message;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.UserNoticeEntity;
import com.liemi.seashellmallclient.data.entity.article.ShopCartArticleEntity;
import com.liemi.seashellmallclient.databinding.SharemallActivityXrecyclerviewBinding;
import com.liemi.seashellmallclient.ui.BaseWebviewActivity;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity;
import com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity.REFUND_ID;

public class MessageActivity extends BaseXRecyclerActivity<SharemallActivityXrecyclerviewBinding, ShopCartArticleEntity> {

    @Override
    protected int getContentView() {
        return R.layout.sharemall_activity_xrecyclerview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_message));
        getRightSetting().setText(getString(R.string.sharemall_set_message_all_read));
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<ShopCartArticleEntity, BaseViewHolder>(getContext(), R.layout.sharemall_view_message_empty) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_message;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<UserNoticeEntity.NoticeDetailsEntity>(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        switch (items.get(position).getLink_type()) {
                            case 2:
                            case 3:
                                BaseWebviewActivity.start(getContext(), getItem(position).getTitle(), getItem(position).getUrl(), null);
                                break;
                            case 4:
                                //跳转订单
                                Bundle bundle = new Bundle();
                                bundle.putString(MineOrderDetailsActivity.ORDER_DETAILS_ID, items.get(position).getParam());
                                JumpUtil.overlay(getContext(), MineOrderDetailsActivity.class, bundle);
                                break;
                            case 5:
                                //跳转退款订单详情
                                JumpUtil.overlay(getContext(), RefundDetailedActivity.class, REFUND_ID, items.get(position).getParam());
                                break;
                            case 6:
                                //跳转商品详情
                                GoodDetailPageActivity.start(getContext(), getItem(position).getParam(), null);
                                break;
                        }
                        //设置已读
                        doNoticeRead(getItem(position).getNotice_id(), position);
                    }
                };
            }

        });
    }


    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.tv_setting) {
            doReadAll();
        }
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"2"}, null, PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<UserNoticeEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        showData(data.getData());
                    }
                });
    }

    //请求公告已读
    private void doNoticeRead(String noticeId, final int position) {
        RetrofitApiFactory.createApi(MineApi.class)
                .doSetNoticeRead(Strings.toInt(noticeId))
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        adapter.getItem(position).setIs_read(1);
                        adapter.notifyPosition(position);
                    }
                });
    }

    //系统通知全部已读
    private void doReadAll() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doNoticeAllRead("param")
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        for (ShopCartArticleEntity entity : adapter.getItems()) {
                            entity.setIs_read(1);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
