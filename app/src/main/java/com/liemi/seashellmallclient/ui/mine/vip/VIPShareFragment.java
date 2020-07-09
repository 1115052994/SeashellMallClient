package com.liemi.seashellmallclient.ui.mine.vip;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPMemberEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPMemberPageEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPShareImgEntity;
import com.liemi.seashellmallclient.data.event.VipFollowerNumEvent;
import com.liemi.seashellmallclient.databinding.LayouShareItemVipBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

public class VIPShareFragment extends BaseFragment<LayouShareItemVipBinding> {
    public static final String SHARE_TYPE = "share_type";
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static VIPShareFragment newInstance(int type) {
        VIPShareFragment f = new VIPShareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SHARE_TYPE, type);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getContentView() {
        return R.layout.layou_share_item_vip;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
       doGetImgUrl();
    }

    private void doGetImgUrl() {
        showProgress("");
        Observable<BaseData<VIPShareImgEntity>> observable;

        if (getArguments().getInt(SHARE_TYPE)==1) {  //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            observable = RetrofitApiFactory.createApi(VIPApi.class)
                    .VIPInviteFriend(null);
        }else{
            observable = RetrofitApiFactory.createApi(VIPApi.class)
                    .getStoreSharePoster(null);
        }
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<VIPShareImgEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        String img_url = data.getData().getImg_url();
                        setImage(img_url);
                        GlideShowImageUtils.displayNetImage(getContext(), img_url, mBinding.ivBanner,
                                R.drawable.baselib_bg_null);

                    }
                });

    }

}
