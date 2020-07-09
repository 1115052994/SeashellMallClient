package com.liemi.seashellmallclient.ui.mine.order;

import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.order.LogisticEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.data.param.RefundParam;
import com.liemi.seashellmallclient.databinding.ActivityLogisticTrackBinding;
import com.liemi.seashellmallclient.databinding.LayoutLogisticTrackItemBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemLogisticTrackBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.refund.ApplyRefundTypeActivity;
import com.liemi.seashellmallclient.ui.mine.refund.RefundDetailedActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

public class LogisticTrackActivity extends BaseXRecyclerActivity<ActivityLogisticTrackBinding, LogisticEntity> {

    public static final String MPID = "mpid";
    private List<String> expressList = new ArrayList<>();
    private List<LogisticEntity> entity = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_logistic_track;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_logistics_detail));
//        getRightSetting().setText("物流选择");
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<LogisticEntity, BaseViewHolder>(this) {

            @Override
            public int layoutResId(int position) {
                return R.layout.layout_logistic_track_item;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<LogisticEntity>(binding) {

                    @Override
                    public void bindData(LogisticEntity item) {
                        BaseRViewAdapter<LogisticEntity.ListBean, BaseViewHolder> goodAdapter;
                        getBinding().xrvData.setLayoutManager(new LinearLayoutManager(getContext()));
                        getBinding().xrvData.setAdapter(goodAdapter = new BaseRViewAdapter<LogisticEntity.ListBean, BaseViewHolder>(getContext()) {

                            @Override
                            public int layoutResId(int position) {
                                return R.layout.sharemall_item_logistic_track;
                            }

                            @Override
                            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                return new BaseViewHolder<LogisticEntity.ListBean>(binding) {

                                    @Override
                                    public void bindData(LogisticEntity.ListBean item) {

                                        SharemallItemLogisticTrackBinding binding = getBinding();

                                        binding.tvLogisticsDate.setText(DateUtil.formatDateTime(DateUtil.strToDate(item.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_MM_DD));
                                        binding.tvLogisticsTime.setText(DateUtil.formatDateTime(DateUtil.strToDate(item.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_HH_MM));
                                        if (position == getItemCount() - 1) {
                                            binding.viewBottom.setVisibility(View.VISIBLE);
                                            binding.viewLine.setVisibility(View.GONE);
                                        } else {
                                            binding.viewBottom.setVisibility(View.GONE);
                                            binding.viewLine.setVisibility(View.VISIBLE);
                                        }

                                        if (position == 0) {
                                            binding.viewTop.setVisibility(View.VISIBLE);
                                            binding.ivIcon.setImageResource(R.drawable.sharemall_oval_40dp_1d1e1f);
                                        } else {
                                            binding.viewTop.setVisibility(View.GONE);
                                            binding.ivIcon.setImageResource(R.drawable.sharemall_oval_40dp_cad5de);
                                        }

                                        super.bindData(item);
                                    }

                                    @Override
                                    public SharemallItemLogisticTrackBinding getBinding() {
                                        return (SharemallItemLogisticTrackBinding) super.getBinding();
                                    }
                                };
                            }
                        });
                        goodAdapter.setData(item.getList());
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_copy) {
                            KeyboardUtils.putTextIntoClip(getContext(), getBinding().tvLogisticsNo.getText().toString());
                        }
                    }

                    @Override
                    public LayoutLogisticTrackItemBinding getBinding() {
                        return (LayoutLogisticTrackItemBinding) super.getBinding();
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
        if (view.getId() == R.id.tv_copy) {
//            KeyboardUtils.putTextIntoClip(this, mBinding.tvLogisticsNo.getText().toString());
        } else if (view.getId() == R.id.spinner) {
//            showExpressCompany();
        }
    }

    /*public void showData(LogisticEntity logisticEntity) {
        if (logisticEntity != null
                && logisticEntity.getList() != null) {
            adapter.setData(logisticEntity.getList());
        }
        mBinding.setItem(logisticEntity);
        mBinding.executePendingBindings();
    }*/

    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getLogistic(getIntent().getStringExtra(MPID))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<LogisticEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<List<LogisticEntity>> data) {
                        if (dataExist(data)) {
                            showData(data.getData());
                            /*entity = data.getData();
                            expressList.clear();
                            int i = 0;
                            for (LogisticEntity logisticEntity : data.getData()) {
                                expressList.add(logisticEntity.getCompany());
                                if (!Strings.isEmpty(logisticEntity.getList())&&i==0){
                                    i++;
                                    showData(logisticEntity);
                                }
                            }
                            if (i == 0){
                                showData(data.getData().get(0));
                            }*/
                        }
                    }

                    @Override
                    public void onFail(BaseData<List<LogisticEntity>> data) {

                    }
                });
    }

   /* private void showExpressCompany() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                showData(entity.get(options1));
            }
        }).build();
        pvOptions.setPicker(expressList);
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }
*/
    /*
    * xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<LogisticEntity, BaseViewHolder>(this) {

            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_logistic_track;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<LogisticEntity.ListBean>(binding) {

                    @Override
                    public void bindData(LogisticEntity.ListBean item) {

                        SharemallItemLogisticTrackBinding binding = getBinding();

                        binding.tvLogisticsDate.setText(DateUtil.formatDateTime(DateUtil.strToDate(item.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_MM_DD));
                        binding.tvLogisticsTime.setText(DateUtil.formatDateTime(DateUtil.strToDate(item.getTime(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS), DateUtil.DF_HH_MM));
                        if (position == getItemCount() - 1) {
                            binding.viewBottom.setVisibility(View.VISIBLE);
                            binding.viewLine.setVisibility(View.GONE);
                        } else {
                            binding.viewBottom.setVisibility(View.GONE);
                            binding.viewLine.setVisibility(View.VISIBLE);
                        }

                        if (position == 0) {
                            binding.viewTop.setVisibility(View.VISIBLE);
                            binding.ivIcon.setImageResource(R.drawable.sharemall_oval_40dp_1d1e1f);
                        } else {
                            binding.viewTop.setVisibility(View.GONE);
                            binding.ivIcon.setImageResource(R.drawable.sharemall_oval_40dp_cad5de);
                        }

                        super.bindData(item);
                    }

                    @Override
                    public SharemallItemLogisticTrackBinding getBinding() {
                        return (SharemallItemLogisticTrackBinding) super.getBinding();
                    }
                };
            }
        });
    *
    * */
}
