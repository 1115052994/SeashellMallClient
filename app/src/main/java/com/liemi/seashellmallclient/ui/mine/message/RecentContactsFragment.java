package com.liemi.seashellmallclient.ui.mine.message;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.contacts.RecentContactEntity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;

import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.SobotMsgCenterModel;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.seashellmallclient.data.entity.contacts.RecentContactEntity.MESSAGE_TYPE_SOBOT;


public class RecentContactsFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerviewBinding, RecentContactEntity> {

    public static final String TAG = RecentContactsFragment.class.getName();

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<RecentContactEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_notice_contact;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<RecentContactEntity>(binding) {

                    @Override
                    public void bindData(RecentContactEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        switch (getItem(position).getType()) { //1：系统公告； 2系统通知； 3商学院；4用户资产; 5客服消息
                            case 1:
                            case 3:
                            case 4:
                                NoticeMsgActivity.start(getContext(), getItem(position).getName(), getItem(position).getType());
                                break;
                            case 2:
                                JumpUtil.overlay(getContext(), MessageActivity.class);
                                break;
                            case MESSAGE_TYPE_SOBOT:
                                SobotApi.startMsgCenter(getContext(), UserInfoCache.get().getUid());
                                break;
                        }
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter.getItemCount() == 0) {
            xRecyclerView.refresh();
        } else {
            doListData();
        }
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getRecentContacts("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<List<RecentContactEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<RecentContactEntity>> data) {

                        /*RecentContactEntity entity = new RecentContactEntity();
                        entity.setType(MESSAGE_TYPE_SOBOT);
                        entity.setName(getString(R.string.sharemall_customer_service_news));
                        entity.setLogo_url("https://social-shop.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/AFXZMNTH01234589_1568084007.png");
                        entity.setAll_no_readnum(SobotApi.getUnreadMsg(getContext(), UserInfoCache.get().getUid()));*/
                        List<RecentContactEntity> list = new ArrayList<>();
                        List<SobotMsgCenterModel> msgCenterModels = SobotApi.getMsgCenterList(getContext(), UserInfoCache.get().getUid());
                        /*if (!Strings.isEmpty(msgCenterModels)) {
                            entity.setTitle(msgCenterModels.get(0).getLastMsg());
                            entity.setCreate_time(msgCenterModels.get(0).getLastDate());
                        }
                        list.add(entity);*/
                        if (!Strings.isEmpty(data.getData())) {
                            list.addAll(data.getData());
                        }
                        showData(list, list.size());
                    }
                });
    }

}
