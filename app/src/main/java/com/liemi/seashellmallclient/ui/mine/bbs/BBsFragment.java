package com.liemi.seashellmallclient.ui.mine.bbs;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.ArticleApi;
import com.liemi.seashellmallclient.data.entity.ShareMallPageEntity;
import com.liemi.seashellmallclient.data.entity.article.ArticleListEntity;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.BaselibFragmentXrecyclerview1Binding;
import com.liemi.seashellmallclient.databinding.FragmentBbsBinding;
import com.liemi.seashellmallclient.ui.home.SearchActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.databinding.BaselibFragmentXrecyclerviewBinding;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.annotations.NonNull;

import static com.liemi.seashellmallclient.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;

public class BBsFragment extends BaseXRecyclerFragment<BaselibFragmentXrecyclerview1Binding, ArticleListEntity> {

    private static final String CLASS_ID = "class_id";

    private String classId;
    private String title;

    @Override
    protected int getContentView() {
        return R.layout.baselib_fragment_xrecyclerview1;
    }

    public static BBsFragment newInstance(String classId,String title) {
        BBsFragment fragment = new BBsFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_ID, classId);
        args.putString(VipParam.title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getString(CLASS_ID);
        }
    }

    @Override
    protected void initUI() {
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        adapter=new BaseRViewAdapter<ArticleListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType==0){
                    return R.layout.item_first_article;
                }else{
                    return R.layout.item_home_child_article;
                }
            }

            @Override
            public int getItemViewType(int position) {
                return isShowEmpty() ? EMPTY_VIEW_TYPE : getViewType(position);
            }

            private int getViewType(int position) {
                return position==0?0:1;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<ArticleListEntity>(binding) {
                    @Override
                    public void bindData(ArticleListEntity item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Log.e("html============", getItem(position).getUrl());
                        Intent intent = new Intent(getActivity(), ArticleWebActivity.class);
                        intent.putExtra(WEBVIEW_TYPE, 2);
                        intent.putExtra(WEBVIEW_CONTENT, getItem(position).getType() == 1 ?
                                Constant.BASE_API + getItem(position).getParam() : getItem(position).getUrl());
                        intent.putExtra(WEBVIEW_TITLE, getItem(position).getTitle());
                        intent.putExtra(VipParam.time, getItem(position).getMMDDHHMMCreate_time());
                        intent.putExtra(VipParam.readNum, getItem(position).getRead_num());
                        intent.putExtra("zan", getItem(position).getComment_num());
                        intent.putExtra(VipParam.title, getArguments().getString(VipParam.title));
                        intent.putExtra("image", getItem(position).getImg_url());
                        intent.putExtra("id", getItem(position).getId());
                        startActivity(intent);
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        mBinding.xrvData.refresh();
    }

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(ArticleApi.class)
                .getArticleList(PageUtil.toPage(startPage), Constant.PAGE_ROWS,classId)
                .compose(RxSchedulers.<BaseData<PageEntity<ArticleListEntity>>>compose())
                .compose((this).<BaseData<PageEntity<ArticleListEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<PageEntity<ArticleListEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<PageEntity<ArticleListEntity>> data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            showData(data.getData());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
