package com.liemi.seashellmallclient.ui.good.comment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.CommentEntity;
import com.liemi.seashellmallclient.data.entity.good.PageCommentEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallFragmentXrecyclerviewBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemCommentBinding;
import com.liemi.seashellmallclient.widget.MyRecyclerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/25 15:43
 * 修改备注：
 */
public class CommentFragment extends BaseXRecyclerFragment<SharemallFragmentXrecyclerviewBinding, CommentEntity> {

    private String itemId, flag;

    @Override
    protected int getContentView() {
        return R.layout.sharemall_fragment_xrecyclerview;
    }

    public static CommentFragment newInstance(String itemId, String flag) {
        CommentFragment f = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GoodsParam.ITEM_ID, itemId);
        bundle.putString(GoodsParam.COMMENT_FLAG, flag);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<CommentEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_comment;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<CommentEntity>(binding) {
                    @Override
                    public void bindData(CommentEntity item) {
                        getBinding().rbStarServer.setStar(Float.valueOf(item.getLevel()), false);
                        MyRecyclerView rvPic = getBinding().rvImg;
                        rvPic.setNestedScrollingEnabled(false);
                        if (getItem(position).getMeCommetImgs() != null
                                && !getItem(position).getMeCommetImgs().isEmpty()) {
                            rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                            final BaseRViewAdapter<String, BaseViewHolder> imgAdapter = new BaseRViewAdapter<String, BaseViewHolder>(context) {

                                @Override
                                public int layoutResId(int position) {
                                    return R.layout.sharemall_item_multi_pic_show;
                                }

                                @Override
                                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                    return new BaseViewHolder(binding) {

                                        @Override
                                        public void doClick(View view) {
                                            super.doClick(view);
                                            JumpUtil.overlayImagePreview(getActivity(), getItems(), 0);
                                        }
                                    };
                                }
                            };

                            rvPic.setAdapter(imgAdapter);
                            imgAdapter.setData(getItem(position).getMeCommetImgs());
                        }
                        super.bindData(item);
                    }

                    @Override
                    public SharemallItemCommentBinding getBinding() {
                        return (SharemallItemCommentBinding) super.getBinding();
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            itemId = getArguments().getString(GoodsParam.ITEM_ID);
            flag = getArguments().getString(GoodsParam.COMMENT_FLAG);
        }
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listComment(PageUtil.toPage(startPage), Constant.PAGE_ROWS, itemId, flag)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new FastObserver<BaseData<PageCommentEntity<CommentEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageCommentEntity<CommentEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

}
