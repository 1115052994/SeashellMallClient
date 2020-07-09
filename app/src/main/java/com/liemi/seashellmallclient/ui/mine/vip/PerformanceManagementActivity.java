package com.liemi.seashellmallclient.ui.mine.vip;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.entity.order.OrderSkusEntity;
import com.liemi.seashellmallclient.data.entity.vip.MyVIPIncomeInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPIncomeListEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPIncomeOrderEntity;
import com.liemi.seashellmallclient.data.param.VipParam;
import com.liemi.seashellmallclient.databinding.ActivityPerformanceManagementBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemOrderSkusGoods2Binding;
import com.liemi.seashellmallclient.databinding.SharemallItemVipIncomeBinding;
import com.liemi.seashellmallclient.databinding.SharemallPopFilterTimeIncomeBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.EmptyLayoutEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.widget.BasePopupWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.netmi.baselibrary.utils.DateUtil.DF_YYYY_MM_DD_P;

public class PerformanceManagementActivity extends BaseXRecyclerActivity<ActivityPerformanceManagementBinding, BaseEntity> {

    private List<TextView> childTabs;
    private List<Integer> timeTabs;

    private int selectType = 1;     //1:订单  2:福利金
    private String orderNo;
    private String startDate;
    private String endDate;
    private String endTime;
    private String orderStatus; //(默认为0) 0:全部 1:退款 2:已结算 3:未结算
    private MyVIPIncomeInfoEntity incomeEntity;
    private String selectDate;
    private PopupWindow filterTimePop;

    private String splitDate = " ~ ";

    @Override
    public void setBarColor() {
        ImmersionBar.with(this)
                .reset()
                .titleBar(R.id.top_view)
                .init();
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_performance_management;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_my_vip_about_earns));

        xRecyclerView = mBinding.rvContent;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setNoMore(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);

        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(getContext(),xRecyclerView, R.layout.baselib_include_no_data_view2,
                new EmptyLayoutEntity("暂无相关明细")) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.sharemall_item_vip_income;
                }
                return R.layout.sharemall_item_vip_rebate;
            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position) instanceof VIPIncomeOrderEntity) {
                    return 1;
                }else if (getItem(position) instanceof VIPIncomeListEntity){
                    return 2;
                }else {
                    return 0x1235;
                }

            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<BaseEntity>(binding) {
                    @Override
                    public void bindData(BaseEntity item) {
                        super.bindData(item);
                        if (getBinding() instanceof SharemallItemVipIncomeBinding){
                            SharemallItemVipIncomeBinding vipIncomeBinding = (SharemallItemVipIncomeBinding) getBinding();
                            BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> goodAdapter = initOrderSkuAdapter((VIPIncomeOrderEntity) getItem(position));
                            vipIncomeBinding.rvGoods.setAdapter(goodAdapter);
                            vipIncomeBinding.rvGoods.setNestedScrollingEnabled(false);
                            vipIncomeBinding.rvGoods.setLayoutManager(new LinearLayoutManager(getContext()));
                            goodAdapter.setData(((VIPIncomeOrderEntity)item).getOrderSkus());
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (getBinding() instanceof SharemallItemVipIncomeBinding){
                            JumpUtil.overlay(getContext(), PerformanceManagementDetailActivity.class,
                                    VipParam.awardId, ((VIPIncomeOrderEntity) getItem(position)).getHand_log_id());
                        }

                    }
                };
            }
        });

        mBinding.etOrderNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard(mBinding.etOrderNo);
                    if (Strings.isEmpty(orderNo) && Strings.isEmpty(mBinding.etOrderNo.getText().toString().trim())) {
                        return true;
                    }
                    orderNo = mBinding.etOrderNo.getText().toString().trim();
                    doListData();
                    return true;
                }
                return false;
            }
        });

        mBinding.appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {    //折叠状态
                    mBinding.llFilter.setBackgroundColor(0xffffffff);
                } else {
                    mBinding.llFilter.setBackgroundResource(R.drawable.sharemall_radius_ltrt12dp_ff);
                }
            }
        });
    }

    private BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> initOrderSkuAdapter(VIPIncomeOrderEntity item) {
        BaseRViewAdapter<OrderSkusEntity, BaseViewHolder> adapter = new BaseRViewAdapter<OrderSkusEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_order_skus_goods2;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<OrderSkusEntity>(binding) {

                    @Override
                    public void bindData(OrderSkusEntity item) {
                        super.bindData(item);

                    }

                    @Override
                    public SharemallItemOrderSkusGoods2Binding getBinding() {
                        return (SharemallItemOrderSkusGoods2Binding) super.getBinding();
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        JumpUtil.overlay(getContext(), PerformanceManagementDetailActivity.class,
                                VipParam.awardId, item.getHand_log_id());
                    }
                };
            }
        };
        return adapter;
    }

    @Override
    protected void initData() {
        childTabs = new ArrayList<>();
        childTabs.add(mBinding.tvAll);
        childTabs.add(mBinding.tvRefund);
        childTabs.add(mBinding.tvFinish);
        childTabs.add(mBinding.tvNotFinish);
        childTabs.add(mBinding.tvRebate);

        timeTabs = new ArrayList<>();
        timeTabs.add(R.id.tv_today);
        timeTabs.add(R.id.tv_current_week);
        timeTabs.add(R.id.tv_current_month);
        timeTabs.add(R.id.tv_count);
        getMyVIPIncomeInfo();

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_all) {
            //(默认为0) 0:全部 1:退款 2:已结算 3:未结算
            if (((TextView) view).getTextSize() == 18) return;
            orderStatus = null;
            controlOrderStatus(view);

        } else if (i == R.id.tv_refund) {
            if (((TextView) view).getTextSize() == 18) return;
            orderStatus = "1";
            controlOrderStatus(view);

        } else if (i == R.id.tv_finish) {
            if (((TextView) view).getTextSize() == 18) return;
            orderStatus = "2";
            controlOrderStatus(view);

        } else if (i == R.id.tv_not_finish) {
            if (((TextView) view).getTextSize() == 18) return;
            orderStatus = "3";
            controlOrderStatus(view);

        } else if (i == R.id.tv_rebate) {
            if (((TextView) view).getTextSize() == 18) return;
            controlOrderStatus(view);

        } else if (i == R.id.tv_filter_time) {
            if (Strings.isEmpty(selectDate)) {
                showDate1();
            } else {
                selectDate = null;
                mBinding.tvFilterTime.setText(getString(R.string.sharemall_vip_filter_time));
                mBinding.tvFilterTime.setTextColor(Color.parseColor("#333333"));
                Drawable dra = getResources().getDrawable(R.mipmap.sharemall_ic_filter_time);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                mBinding.tvFilterTime.setCompoundDrawables(dra,
                        null, null, null);
                mBinding.tvFilterTime.setBackgroundResource(R.drawable.sharemall_radius_4dp_solid_f6f6f7);
                mBinding.rvContent.refresh();
            }

        } else if (i == R.id.tv_search) {
            if (Strings.isEmpty(orderNo) && Strings.isEmpty(mBinding.etOrderNo.getText().toString().trim())) {
                return;
            }
            orderNo = mBinding.etOrderNo.getText().toString().trim();

            doListData();
        } else if (i == R.id.tv_date_name) {
            showFilterTimePop();
        }
    }

    private void filterClick(int id) {
        if (id == R.id.tv_today) {
            startDate = incomeEntity.getToday().getStart_time();
            endDate = null;
            if (incomeEntity != null && incomeEntity.getToday() != null) {
                mBinding.tvIncome.setText(incomeEntity.getToday().getIncome());
                mBinding.tvMoney.setText("≈"+incomeEntity.getToday().getMoney()+"元");
            }
            mBinding.tvFilterTime.setVisibility(View.GONE);
            controlOrderDate(getString(R.string.sharemall_today));
        } else if (id == R.id.tv_current_week) {
            startDate = incomeEntity.getWeek().getStart_time();
            endDate = incomeEntity.getWeek().getEnd_time();
            if (incomeEntity != null && incomeEntity.getWeek() != null) {
                mBinding.tvIncome.setText(incomeEntity.getWeek().getIncome());
                mBinding.tvMoney.setText("≈"+incomeEntity.getWeek().getMoney()+"元");
            }
            mBinding.tvFilterTime.setVisibility(View.GONE);
            controlOrderDate(getString(R.string.sharemall_current_week));
        } else if (id == R.id.tv_current_month) {
            //yyyy-MM-dd
            startDate = incomeEntity.getMonth().getStart_time();
            endDate = incomeEntity.getMonth().getEnd_time();
            if (incomeEntity != null && incomeEntity.getMonth() != null) {
                mBinding.tvIncome.setText(incomeEntity.getMonth().getIncome());
                mBinding.tvMoney.setText("≈"+incomeEntity.getMonth().getMoney()+"元");
            }
            mBinding.tvFilterTime.setVisibility(View.GONE);
            controlOrderDate(getString(R.string.sharemall_current_month));
        } else if (id == R.id.tv_count) {
            mBinding.tvFilterTime.setVisibility(View.VISIBLE);
            startDate = incomeEntity.getTotal().getStart_time();
            endDate = incomeEntity.getTotal().getEnd_time();
            if (incomeEntity != null && incomeEntity.getTotal() != null) {
                mBinding.tvIncome.setText(incomeEntity.getTotal().getIncome());
                mBinding.tvMoney.setText("≈"+incomeEntity.getTotal().getMoney()+"元");
            }
            controlOrderDate(getString(R.string.sharemall_cumulative));
        } else {
            if (filterTimePop != null) {
                filterTimePop.dismiss();
            }
        }
    }

    private void showFilterTimePop() {
        if (filterTimePop == null) {
            filterTimePop = new BasePopupWindow().setActivity(this);
            filterTimePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            filterTimePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            filterTimePop.setFocusable(true);
            filterTimePop.setOutsideTouchable(true);
            SharemallPopFilterTimeIncomeBinding binding = DataBindingUtil.inflate(
                    getLayoutInflater(), R.layout.sharemall_pop_filter_time_income, null, false);
            binding.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterClick(view.getId());
                }
            });
            filterTimePop.setContentView(binding.getRoot());
        }
        filterTimePop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private Calendar getCalendarOffset(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return calendar;
    }

    private void showDate1(){
        String openVIPTime = getIntent().getStringExtra(VipParam.openVIPTime);
        Date strtodate = null;
        if (!Strings.isEmpty(openVIPTime)) {
            SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(openVIPTime, pos);
        }
        Calendar firstCalendar = Calendar.getInstance();
        String currentTime = mBinding.tvFilterTime.getText().toString();
        if (!TextUtils.isEmpty(currentTime) && currentTime.contains(splitDate)) {
            String[] time = currentTime.split(splitDate);
            if (time.length > 1 && time[0].length() > 4) {
                firstCalendar.setTime(DateUtil.strToDate(time[0], DF_YYYY_MM_DD_P));
            }
        }else {
            firstCalendar.set(1900, 1, 1);
        }

        //时间选择器
        new TimePickerView.Builder(getContext(), (final Date firstDate, View v) -> {

            Calendar nextCalendar = Calendar.getInstance();
            nextCalendar.setTimeInMillis(firstDate.getTime() + DateUtil.DAY);

            new TimePickerView.Builder(getContext(), (Date date, View subView) -> {
                selectDate = DateUtil.formatDateTime(firstDate, DF_YYYY_MM_DD_P);
                mBinding.tvFilterTime.setText((DateUtil.formatDateTime(firstDate, DF_YYYY_MM_DD_P)
                        + splitDate + DateUtil.formatDateTime(date, DF_YYYY_MM_DD_P)));
                mBinding.tvFilterTime.setTextColor(Color.parseColor("#B52902"));
                Drawable dra = getResources().getDrawable(R.mipmap.sharemall_ic_red_close);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                mBinding.tvFilterTime.setCompoundDrawables(dra,
                        null, null, null);
                mBinding.tvFilterTime.setBackgroundResource(R.drawable.sharemall_radius_4dp_1aff688f_stroke_1dp_ff688f);
                mBinding.rvContent.refresh();
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setSubCalSize(14)
                    .setTitleText(getString(R.string.sharemall_end_date))
                    .setRangDate(nextCalendar, Calendar.getInstance())
                    .setDate(nextCalendar)
                    .build()
                    .show();
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setSubCalSize(14)
                .setTitleText(getString(R.string.sharemall_start_date))
                .setSubmitText(getString(R.string.sharemall_next_step))
                .setRangDate(firstCalendar, getCalendarOffset(-1))
                .setDate(getCalendarOffset(-365))
                .build()
                .show();
    }

    //显示日期的view
    private void showDate() {
        String openVIPTime = getIntent().getStringExtra(VipParam.openVIPTime);
        Date strtodate = null;
        if (!Strings.isEmpty(openVIPTime)) {
            SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(openVIPTime, pos);
        }

        Calendar startDate = Calendar.getInstance();
        if (strtodate != null) {
            startDate.set(strtodate.getYear(), strtodate.getMonth(), 1);
        } else {
            if (TextUtils.isEmpty(selectDate)){
                startDate.set(1900, 1, 1);
            }else {
                String[] day = selectDate.split(" ")[0].split("-");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(selectDate);
                    startDate.set(date.getYear(),date.getMonth(),date.getDay());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
        Calendar endDate = Calendar.getInstance();
        if (TextUtils.isEmpty(selectDate)){
            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DATE,-1);
            endDate.setTime(cal.getTime());
        }
        TimePickerView dateView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (TextUtils.isEmpty(selectDate)){
                    selectDate = DateUtil.formatDateTime(date, "yyyy-MM-dd");
                    showDate();
                }else {
                    endTime = DateUtil.formatDateTime(date, "yyyy-MM-dd");
                    mBinding.tvFilterTime.setText(selectDate + " ~ " + endTime);
                    mBinding.tvFilterTime.setTextColor(Color.parseColor("#B52902"));
                    Drawable dra = getResources().getDrawable(R.mipmap.sharemall_ic_red_close);
                    dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                    mBinding.tvFilterTime.setCompoundDrawables(dra,
                            null, null, null);
                    mBinding.tvFilterTime.setBackgroundResource(R.drawable.sharemall_radius_4dp_1aff688f_stroke_1dp_ff688f);
                    mBinding.rvContent.refresh();
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText(getString(R.string.sharemall_cancel))
                .setSubmitText(getString(R.string.sharemall_confirm))
                .setTitleText(TextUtils.isEmpty(selectDate)?"开始时间":"结束时间")
                .setSubmitColor(this.getResources().getColor(R.color.gray_1B252D))
                .setCancelColor(this.getResources().getColor(R.color.gray_1B252D))
                .setOutSideCancelable(true)
                .setRangDate(startDate, endDate)
                .setDate(endDate)
                .isCyclic(false)
                .isCenterLabel(false)
                .build();
        dateView.show();
    }

    private String getChoiceDate(int type) {
        String currentTime = mBinding.tvFilterTime.getText().toString();
        if (!TextUtils.isEmpty(currentTime) && currentTime.contains(splitDate)) {
            String[] time = currentTime.split(splitDate);
            if (time.length > type && time[type].length() > 4) {
                return time[type];
            }
        }
        return null;
    }

    private void controlOrderDate(String text) {
        if (filterTimePop != null) {
            filterTimePop.dismiss();
        }
        mBinding.tvDateName.setText(text);
        doClick(childTabs.get(0));
        if (endDate == null) {
            mBinding.tvDateInterval.setText(startDate);
        } else {
            if (startDate == null) {
                mBinding.tvDateInterval.setText(endDate);
            } else {
                mBinding.tvDateInterval.setText(startDate + " ~ " + endDate);
            }
        }
    }

    private void controlOrderStatus(View view) {
        for (TextView childTab : childTabs) {
            if (childTab.getId() == view.getId()) {
                childTab.setTextSize(15);
                childTab.setTextColor(0xff1D1E1F);
            } else {
                childTab.setTextSize(14);
                childTab.setTextColor(0xff999999);
            }
        }

        if (view.getId() == R.id.tv_rebate) {
            selectType = 2; //1:订单  2:福利金
            mBinding.rlOrderSearch.setVisibility(View.GONE);
            mBinding.tvSearch.setVisibility(View.GONE);
        } else {
            selectType = 1; //1:订单  2:福利金
            mBinding.rlOrderSearch.setVisibility(View.VISIBLE);
            mBinding.tvSearch.setVisibility(View.VISIBLE);
        }
        adapter.clear();
        mBinding.rvContent.refresh();
    }

    //获取当天的结束时间
    private String getEndDate() {
        return TextUtils.isEmpty(endDate) ? null : DateUtil.formatDateTime(new Date(DateUtil.strToLong(endDate) + DateUtil.DAY), DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
    }

    @Override
    protected void doListData() {
        if (selectType == 1) { //1:订单  2:福利金
            doGetVIPIncomeOrder();
        } else {
            doGetVIPIncomeList();
        }
    }

    //(默认为0) 0:全部 1:退款 2:已结算 3:未结算
    private void doGetVIPIncomeOrder() {
        RetrofitApiFactory.createApi(VIPApi.class)
                .getVIPIncomeOrder(PageUtil.toPage(startPage), Constant.PAGE_ROWS, orderNo,
                        (Strings.isEmpty(selectDate) || mBinding.tvFilterTime.getVisibility() == View.GONE)
                                ? startDate : getChoiceDate(0), mBinding.tvFilterTime.getVisibility() == View.GONE?getEndDate():getChoiceDate(1), orderStatus)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<VIPIncomeOrderEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<PageEntity<VIPIncomeOrderEntity>> data) {
                        PageEntity<BaseEntity> pageEntity = new PageEntity<>();
                        List<BaseEntity> list = new ArrayList<>();
                        list.addAll(data.getData().getList());
                        pageEntity.setList(list);
                        pageEntity.setTotal_pages(data.getData().getTotal_pages());
                        showData(pageEntity);

                    }

                });
    }

    //默认为0, 0:全部 1:福利金(全部收入)
    private void doGetVIPIncomeList() {
        RetrofitApiFactory.createApi(VIPApi.class)
                .getVIPIncomeList(PageUtil.toPage(startPage), Constant.PAGE_ROWS,
                        (Strings.isEmpty(selectDate) || mBinding.tvFilterTime.getVisibility() == View.GONE)
                                ? startDate : selectDate, getEndDate(), 1)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<VIPIncomeListEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<PageEntity<VIPIncomeListEntity>> data) {
                        PageEntity<BaseEntity> pageEntity = new PageEntity<>();
                        List<BaseEntity> list = new ArrayList<>();
                        list.addAll(data.getData().getList());
                        pageEntity.setList(list);
                        pageEntity.setTotal_pages(data.getData().getTotal_pages());
                        showData(pageEntity);

                    }

                });
    }

    //获取预计收益
    private void getMyVIPIncomeInfo() {
        RetrofitApiFactory.createApi(VIPApi.class)
                .getMyVIPIncomeInfo(null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<MyVIPIncomeInfoEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<MyVIPIncomeInfoEntity> data) {
                        incomeEntity = data.getData();
                        if (getIntent().hasExtra(VipParam.type)) {      //切换
                            filterClick(timeTabs.get(Integer.parseInt(getIntent().getStringExtra(VipParam.type)) - 1));
                        } else {
                            mBinding.rvContent.refresh();
                        }
                    }
                });
    }
}
