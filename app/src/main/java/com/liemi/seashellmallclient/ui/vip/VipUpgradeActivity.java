package com.liemi.seashellmallclient.ui.vip;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.ShareContract;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.cache.VipUserInfoCache;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPGiftEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPUserInfoEntity;
import com.liemi.seashellmallclient.data.event.ShareEvent;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.ShareMallParam;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityVipUpgradeBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemUpgradeVipGoodsBinding;
import com.liemi.seashellmallclient.presenter.SharePresenterImpl;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.*;
import com.netmi.baselibrary.widget.ShareDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VipUpgradeActivity extends BaseXRecyclerActivity<ActivityVipUpgradeBinding, VIPGiftEntity> implements ShareContract.View {
    private boolean isVIP;      //是否为推手
    private int handLevel;  //推手等级

    private SharePresenterImpl sharePresenter;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.setStatusBar(this, true, R.color.bgColor);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_vip_upgrade;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);

        isVIP = ShareMallUserInfoCache.get().isVip() && !VipUserInfoCache.getInstance().get().isStuckVip();
        if (!isVIP) {
            mBinding.tvNowShare.setVisibility(View.GONE);
        }

        xRecyclerView = mBinding.rvContent;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        adapter = new BaseRViewAdapter<VIPGiftEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_upgrade_vip_goods;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<VIPGiftEntity>(binding) {
                    @Override
                    public void bindData(VIPGiftEntity item) {
                        getBinding().setIsVIP(isVIP);

                        item.setHiddenBottom(item.getGift_level() <= handLevel);

                        if (item.isHiddenBottom()) {
                            getBinding().tvUpgrade.setVisibility(View.GONE);
                        } else {
                            getBinding().tvUpgrade.setVisibility(View.VISIBLE);
                        }
                        super.bindData(item);
                    }

                    @Override
                    public SharemallItemUpgradeVipGoodsBinding getBinding() {
                        return (SharemallItemUpgradeVipGoodsBinding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (getItem(position).isHiddenBottom()) {
                            JumpUtil.overlay(getContext(), VIPGiftDetailActivity.class, GoodsParam.ITEM_ID,
                                    getItem(position).getItem_id(), VipParam.hideBottom, "1");
                        } else {
                            JumpUtil.overlay(getContext(), VIPGiftDetailActivity.class, GoodsParam.ITEM_ID, getItem(position).getItem_id());
                        }
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        basePresenter = sharePresenter = new SharePresenterImpl(this);
        handLevel = VipUserInfoCache.getInstance().get().isStuckVip() ? 0 : VipUserInfoCache.getInstance().get().getLevel();
        xRecyclerView.refresh();
    }

    @Override
    public void doClick(android.view.View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_now_share) {
            sharePresenter.readyShare();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareEvent(ShareEvent event) {
        sharePresenter.readyShare();
    }

    @Override
    public void readySuccess() {
        /**
         * https://shop-api.netmi.com.cn/netmi-shop-h5/dist/#/newGiftNew?
         * item_type= + 礼包类型+&theName=+分享人姓名+'&head_url='+分享人头像图片地址
         * +'&share_code='+分享人邀请码'+'&code='+推手编号
         */
        ShareMallUserInfoEntity userInfoEntity = (UserInfoCache.get(ShareMallUserInfoEntity.class));
        VIPUserInfoEntity vipUserInfoEntity = VipUserInfoCache.getInstance().get();
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setActivity(this);
        StringBuilder linkUrl = new StringBuilder(ShareMallParam.SHARE_VIP_GIFT);
        linkUrl.append(vipUserInfoEntity.getShare_code());
        linkUrl.append("&code=").append(vipUserInfoEntity.getHand_number());
        String phone = UserInfoCache.get().getPhone();
        if (!Strings.isEmpty(phone)) {
            linkUrl.append("&phone=").append(phone);
        }
        linkUrl.append("&theName=").append(userInfoEntity.getNickname());
        linkUrl.append("&head_url=").append(userInfoEntity.getHead_url());
        int shareType = 4;  //礼包类型（4：vip会员礼包,5：经理礼包,6：代理商礼包）
        linkUrl.append("&item_type=").append(shareType);
        shareEntity.setLinkUrl(linkUrl.toString());
        shareEntity.setTitle(getString(R.string.sharemall_vip_share_title));
        shareEntity.setContent(getString(R.string.sharemall_vip_share_content));
        shareEntity.setImgRes(R.mipmap.app_logo);
        new ShareDialog(this, shareEntity).showBottomOfDialog();
    }

    @Override
    public void readyFailure(String errMsg) {
        ToastUtils.showShort(errMsg);
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(VIPApi.class)
                .getVIPGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, "4")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<VIPGiftEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<PageEntity<VIPGiftEntity>> data) {
                        showData(data.getData());
                    }

                });
    }

}
