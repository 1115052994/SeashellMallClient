package com.liemi.seashellmallclient.ui.locallife;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.CheckBox;
import com.jcodecraeer.xrecyclerview.ProgressStyle;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeShopEntity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.liemi.seashellmallclient.data.ParamConstant.SHOP_ID;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/21 10:57
 * 修改备注：
 */
public abstract class BaseShopDetailedFragment<T extends ViewDataBinding> extends BaseXRecyclerFragment<T, LocalLifeShopEntity> {

    public static final String ITEM_ID = "item_id";

    public static final String CURRENT_TAB = "current_tab";

    protected LocalLifeShopEntity shopDetailEntity;
    private String shop_id;

    protected abstract MyXRecyclerView getXrvData();

    protected abstract CheckBox getCbCollect();

    @Override
    protected void initUI() {
        Bundle arguments = getArguments();
        shop_id = arguments.getString(SHOP_ID);
        xRecyclerView = getXrvData();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {

    }

    public void showData(LocalLifeShopEntity shopDetailEntity) {

    }


    private void loadFail() {
        getActivity().finish();
    }

    @Override
    protected void doListData() {
        //获取店铺详情
       doShopDetails();
    }

    private void doShopDetails() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getShopDetails(shop_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<LocalLifeShopEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<LocalLifeShopEntity> data) {
                        if (dataExist(data)){
                            showData(data.getData());
                            shopDetailEntity = data.getData();
                        }else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        doListComment();
                    }
                });
    }
    private void doListComment() {

        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getCommentList(0, 1,"",shop_id,null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        if (dataExist(data)){
                            if (!Strings.isEmpty(data.getData().getList())) {
                                CommentEntity firstComment = data.getData().getList().get(0);
                                firstComment.setNum_commet(data.getData().getSum_commet_num());
                                shopDetailEntity.setComment(firstComment);
                            } else {
                                CommentEntity firstComment = new CommentEntity();
                                firstComment.setContent(getString(R.string.sharemall_no_comment_now));
                                firstComment.setTotal_level("100%");
                                firstComment.setNum_commet("0");
                                firstComment.setLevel("5");
                                shopDetailEntity.setComment(firstComment);
                            }
                        }else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                       showData(shopDetailEntity);
                    }
                });
    }




}
