package com.liemi.seashellmallclient.ui.good.order;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.CategoryApi;
import com.liemi.seashellmallclient.data.entity.good.GoodsListEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.databinding.ActivityPayResultBinding;
import com.liemi.seashellmallclient.databinding.SharemallLayoutPayResultTopBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.mine.order.GoodsListAdapter;
import com.liemi.seashellmallclient.ui.mine.order.MineOrderActivity;
import com.liemi.seashellmallclient.ui.vip.VIPGiftDetailActivity;
import com.liemi.seashellmallclient.ui.vip.VipTaskActivity;
import com.liemi.seashellmallclient.ui.vip.VipUpgradeActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.liemi.seashellmallclient.ui.mine.order.MineOrderDetailsActivity.ORDER_ENTITY;

public class PayResultActivity extends BaseXRecyclerActivity<ActivityPayResultBinding, GoodsListEntity> {

    private SharemallLayoutPayResultTopBinding topBinding;

    public static void start(Context context, OrderPayEntity payEntity,boolean isPayHai,String money) {
        if (payEntity != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ORDER_ENTITY, payEntity);
            bundle.putBoolean("isPayHai",isPayHai);
            bundle.putString("money",money);
            JumpUtil.overlay(context, PayResultActivity.class, bundle);
        } else {
            ToastUtils.showShort(R.string.sharemall_not_order_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initUI() {
        AppManager.getInstance().finishActivity(GoodDetailPageActivity.class);

        topBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.sharemall_layout_pay_result_top, mBinding.clContent, false);

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.addHeaderView(topBinding.getRoot());
        xRecyclerView.setAdapter(adapter = new GoodsListAdapter(getContext(), xRecyclerView));
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        //如果是成功购买VIP礼包，关闭购买的界面，避免重复下单
        if (AppManager.getInstance().existActivity(VIPGiftDetailActivity.class)) {
            AppManager.getInstance().finishActivity(VIPGiftDetailActivity.class);
            AppManager.getInstance().finishActivity(VipUpgradeActivity.class);
            AppManager.getInstance().finishActivity(VipTaskActivity.class);
        }
    }

    @Override
    protected void initData() {
        OrderPayEntity detailsEntity = (OrderPayEntity) getIntent().getSerializableExtra(ORDER_ENTITY);
        boolean isPayHai = getIntent().getBooleanExtra("isPayHai", false);
        String money = getIntent().getStringExtra("money");
        if (detailsEntity != null) {
            if (isPayHai){
                topBinding.tvPrice.setText((getString(R.string.sharemall_actual_payment) + money));
            }else {
                topBinding.tvPrice.setText((getString(R.string.sharemall_actual_payment) + detailsEntity.getShowPrice()));
            }
        } else {
            if (isPayHai) {
                topBinding.tvPrice.setText((getString(R.string.sharemall_actual_payment) + "0.00"));
            }else {
                topBinding.tvPrice.setText((getString(R.string.sharemall_actual_payment) + FloatUtils.formatMoney("0.00")));
            }

        }
        xRecyclerView.refresh();
    }

    @Override
    protected void doListData() {
        doListRecommendGoods();
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_read_order) {
            AppManager.getInstance().finishActivity(MineOrderActivity.class);
            JumpUtil.overlay(this, MineOrderActivity.class);
            finish();
        } else if (i == R.id.tv_back_home) {
            MApplication.getInstance().backHome();
        }
    }

    private void doListRecommendGoods() {
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(PageUtil.toPage(startPage), Constant.PAGE_ROWS, null, null, null,
                        null, null, null, null, null,
                        "1", null, null, null, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<GoodsListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            showData(data.getData());
                        }
                    }
                });
    }
}
