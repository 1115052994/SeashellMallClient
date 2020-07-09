package com.liemi.seashellmallclient.ui.category;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.UserNoticeEntity;
import com.liemi.seashellmallclient.data.entity.category.GoodsOneCateEntity;
import com.liemi.seashellmallclient.data.entity.category.GoodsTwoCateEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.ActivityCategoryBinding;
import com.liemi.seashellmallclient.ui.home.SearchActivity;
import com.liemi.seashellmallclient.ui.mine.message.RecentContactsActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.DensityUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

public class CategoryActivity extends BaseActivity<ActivityCategoryBinding> {
    private BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder> cateAdapter;

    private BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder> childAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_category;
    }

    @Override
    protected void initUI() {
        mBinding.rvGoodsCate.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvGoodsCate.setAdapter(cateAdapter = new BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_category_one;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        GoodsOneCateEntity entity = cateAdapter.getItem(position);
                        for (GoodsOneCateEntity cateEntity : cateAdapter.getItems()) {
                            if (cateEntity == entity)
                                cateEntity.setCheck(true);
                            else
                                cateEntity.setCheck(false);
                        }
                        cateAdapter.notifyDataSetChanged();
                        setChildAdapter(entity);
                    }
                };
            }
        });

        mBinding.rvGoods.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RecyclerViewDivider.with(this)
                .color(getResources().getColor(R.color.gray_EE))
                .size(DensityUtils.dp2px(1.5f))
                .build()
                .addTo(mBinding.rvGoods);
        mBinding.rvGoods.setAdapter(childAdapter = new BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_category_two;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Bundle bundle = new Bundle();
                        bundle.putString(GoodsParam.MC_ID, childAdapter.getItem(position).getMcid());
                        bundle.putString(GoodsParam.MC_NAME, childAdapter.getItem(position).getName());
                        JumpUtil.overlay(getActivity(), CategoryGoodsActivity.class, bundle);
                    }
                };
            }
        });
    }

    @Override
    protected void initData() {
        doListCategory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!loading && cateAdapter.getItemCount() == 0) {
            doListCategory();
        }
        if (AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doNoticeData();
        }
    }

    public void showData(List<GoodsOneCateEntity> pageEntity) {
        if (pageEntity != null
                && !pageEntity.isEmpty()) {
            GoodsOneCateEntity first = pageEntity.get(0);
            first.setCheck(true);
            cateAdapter.setData(pageEntity);
            setChildAdapter(first);
        }
    }

    private void setChildAdapter(GoodsOneCateEntity categoryEntity) {
        if (categoryEntity == null) {
            return;
        }
        childAdapter.setData(categoryEntity.getSecond_category());
    }


    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.ll_search) {
            JumpUtil.overlay(getContext(), SearchActivity.class);
        } else if (i == R.id.iv_message) {
            JumpUtil.overlay(getContext(), RecentContactsActivity.class);
        }
    }

    private boolean loading = false;

    private void doListCategory() {
        loading = true;
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listTotalCategory("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<GoodsOneCateEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<List<GoodsOneCateEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                    }
                });
    }

    private void doNoticeData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"}, null, 0, 10)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<UserNoticeEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        if (data.getData() != null
                                && data.getData().getRead_data() != null
                                && data.getData().getRead_data().getAll_no_readnum() > 0) {
                            mBinding.ivMessage.setImageResource(R.mipmap.sharemall_ic_message_gray_point);
                        } else {
                            mBinding.ivMessage.setImageResource(R.mipmap.sharemall_ic_message_gray_not);
                        }
                    }
                });
    }
}
