package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsEntity;
import com.liemi.seashellmallclient.data.entity.order.FillOrderEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityLocalLifeGoodsAllBinding;
import com.liemi.seashellmallclient.databinding.LocallifeItemShopGoodsOtherBinding;
import com.liemi.seashellmallclient.ui.good.order.PayResultActivity;
import com.liemi.seashellmallclient.ui.mine.verification.VerificationOrderPayOnlineActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.ImmersionBarUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class LocalLifeGoodsAllActivity extends BaseXRecyclerActivity<ActivityLocalLifeGoodsAllBinding, LocalLifeGoodsEntity> {

    private String shop_id;
    //创建订单
    private FillOrderEntity orderCommand = new FillOrderEntity();
    private OrderPayEntity payEntity;

    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_local_life_goods_all;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        shop_id = intent.getStringExtra(ParamConstant.SHOP_ID);
        xRecyclerView = mBinding.xrvGoods;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseRViewAdapter<LocalLifeGoodsEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.locallife_item_shop_goods_other;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<LocalLifeGoodsEntity>(binding) {
                    @Override
                    public void bindData(LocalLifeGoodsEntity item) {
                        getBinding().tvSales.setText("销量 "+item.getDeal_num());
                        super.bindData(item);
                        getBinding().tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        /*if (view.getId()==R.id.tv_buy_now){
                            //提交订单

                            orderCommand.setAmount(getItem(position).getPrice());
                            orderCommand.setOrder_type("11");
                            List<FillOrderEntity.SectionsBean> sectionsBeans = new ArrayList<>();
                            //商品列表
                            List<FillOrderEntity.Good> list = new ArrayList<>();
                            //店铺信息
                            FillOrderEntity.SectionsBean sectionsBean = new FillOrderEntity.SectionsBean();
                            FillOrderEntity.Good good = new FillOrderEntity.Good();
                            good.setItem_id(getItem(position).getItem_id());
                            good.setNum(1);
                            list.add(good);
                            sectionsBean.setItem_data(list);
                            sectionsBeans.add(sectionsBean);

                            orderCommand.setSections(sectionsBeans);
                            doOrderCreate(orderCommand);
                        }else {*/
                            //查看商品详情
                            JumpUtil.overlay(getActivity(), LocalLifeGoodsDetailActivity.class, ParamConstant.GOOD_ID, getItem(position).getItem_id());
//                        }
                    }

                    @Override
                    public LocallifeItemShopGoodsOtherBinding getBinding() {
                        return (LocallifeItemShopGoodsOtherBinding) super.getBinding();
                    }
                };
            }
        };
        xRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        mBinding.xrvGoods.refresh();
    }

    @Override
    protected void doListData() {
        showProgress("");
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getGoodsList(PageUtil.toPage(startPage), Constant.PAGE_ROWS,shop_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<LocalLifeGoodsEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<LocalLifeGoodsEntity>> data) {
                        if (dataExist(data)){
                            showData(data.getData());
                        }else {
                            showError(data.getErrmsg());
                        }
                    }

                });
    }

}
