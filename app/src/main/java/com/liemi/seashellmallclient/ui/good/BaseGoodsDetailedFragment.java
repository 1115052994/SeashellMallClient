package com.liemi.seashellmallclient.ui.good;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.trello.rxlifecycle2.android.FragmentEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/9/21 10:57
 * 修改备注：
 */
public abstract class BaseGoodsDetailedFragment<T extends ViewDataBinding> extends BaseXRecyclerFragment<T, GoodsDetailedEntity> {

    protected String itemId;

    protected GoodsDetailedEntity goodEntity;

    protected abstract MyXRecyclerView getXrvData();

    @Override
    protected void initUI() {
        xRecyclerView = getXrvData();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
    }

    @Override
    protected void initData() {
        itemId = requireActivity().getIntent().getStringExtra(GoodsParam.ITEM_ID);
    }

    public void showData(GoodsDetailedEntity goodEntity) {

    }

    private void loadFail(String itemId) {
        if (requestSelf(itemId)) {
            requireActivity().finish();
        }
    }


    //在当前商品页面，加载其他商品信息
    protected boolean requestSelf(String itemId) {
        return TextUtils.equals(this.itemId, itemId);
    }

    @Override
    protected void doListData() {
        doGetGoodsDetailed(itemId);
    }

    protected void doGetGoodsDetailed(String itemId) {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .getGoodsDetailed(itemId)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<GoodsDetailedEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        loadFail(itemId);
                    }

                    @Override
                    public void onSuccess(BaseData<GoodsDetailedEntity> data) {
                        if (data.getData() == null) {
                            ToastUtils.showShort(getString(R.string.sharemall_no_commodity_information));
                            loadFail(itemId);
                            return;
                        }
                        if (data.getData().getStatus() != 5) {
                            //状态：1上传中 2上架待审核 3待定价 4待上架
                            // 5已上架 6 下架待审核 7已下架'
                            ToastUtils.showShort(getString(R.string.sharemall_goods_not_on_the_shelf));
                            loadFail(itemId);
                            return;
                        }

                        goodEntity = data.getData();
                        doListComment();
                    }

                    @Override
                    public void onFail(BaseData<GoodsDetailedEntity> data) {
                        super.onFail(data);
                        loadFail(itemId);
                    }

                });
    }

    private void doListComment() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listComment(0, 1, goodEntity.getItem_id(), null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            CommentEntity firstComment = data.getData().getList().get(0);
                            firstComment.setNum_commet(data.getData().getSum_commet_num());
                            firstComment.setTotal_level(goodEntity.getTotal_level() + "%");
                            goodEntity.setMeCommet(firstComment);
                        } else {
                            CommentEntity firstComment = new CommentEntity();
                            firstComment.setContent(getString(R.string.sharemall_no_comment_now));
                            firstComment.setTotal_level("100%");
                            firstComment.setNum_commet("0");
                            firstComment.setLevel("5");
                            goodEntity.setMeCommet(firstComment);
                        }
                    }

                    @Override
                    public void onComplete() {
                        showData(goodEntity);
                    }
                });
    }


}
