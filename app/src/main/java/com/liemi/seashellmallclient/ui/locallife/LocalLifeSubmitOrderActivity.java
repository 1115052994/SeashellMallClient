package com.liemi.seashellmallclient.ui.locallife;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.liemi.seashellmallclient.MainActivity;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.ParamConstant;
import com.liemi.seashellmallclient.data.api.LocalLifeApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.data.entity.locallife.LocalLifeGoodsDetailEntity;
import com.liemi.seashellmallclient.data.entity.order.FillOrderEntity;
import com.liemi.seashellmallclient.data.entity.order.OrderPayEntity;
import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.LocalLifeActivitySubmitOrderBinding;
import com.liemi.seashellmallclient.ui.good.order.PayResultActivity;
import com.liemi.seashellmallclient.ui.mine.verification.VerificationOrderPayOnlineActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class LocalLifeSubmitOrderActivity extends BaseActivity<LocalLifeActivitySubmitOrderBinding> implements View.OnClickListener {

    private LocalLifeGoodsDetailEntity shopDetailGoodsEntity;
    private VerificationOrderDetailEntity verificationOrderDetailEntity;
    private SpecsGroupEntity choicePrice;
    private String good_id;
    //创建订单
    private FillOrderEntity orderCommand = new FillOrderEntity();
    private OrderPayEntity payEntity;
    @Override
    public void setBarColor() {
        ImmersionBarUtils.whiteStatusBar(getActivity(), true);
    }

    @Override
    protected int getContentView() {
        return R.layout.local_life_activity_submit_order;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        good_id = intent.getStringExtra(ParamConstant.GOOD_ID);
        String title = intent.getStringExtra("title");
        mBinding.tvTitle.setText(title);
        mBinding.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mBinding.setDoClick(this);
        UserInfoEntity userInfoEntity = UserInfoCache.get();
        String phone = userInfoEntity.getPhone();
        mBinding.tvPhone.setText(phone);
    }

    @Override
    protected void initData() {
        doGoodsDetails();

        mBinding.tvNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (choicePrice != null
                        && !s.toString().equals(Strings.twoDecimal(getNum()))) {
                    if (Strings.toFloat(s.toString()) > choicePrice.getStock()) {
                        ToastUtils.showShort(getString(R.string.sharemall_buying_quantity_exceed_inventory));
                        setNumber((int) choicePrice.getStock());
                    }
                }
            }
        });
    }

    private void setNumber(int number) {
        mBinding.tvNum.setText(String.valueOf(number));
        double money = Double.valueOf(shopDetailGoodsEntity.getPrice()) * number;
        mBinding.tvSumMoney.setText(FloatUtils.formatMoney(money));
        mBinding.tvRealPay.setText(FloatUtils.formatMoney(money));
        mBinding.tvMinus.setEnabled(number > 1);
//        mBinding.tvMinus.setEnabled(true);
        mBinding.tvPlus.setEnabled(choicePrice != null&&number<=choicePrice.getStock());
//        mBinding.tvPlus.setEnabled(true);
    }

    private int getNum() {
        return Strings.toInt(mBinding.tvNum.getText().toString());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_minus) {
            setNumber(getNum() - 1);

        } else if (i == R.id.tv_plus) {
            if (choicePrice == null) {
                ToastUtils.showShort(getString(R.string.sharemall_lack_commodity_specification));
                return;
            }
            setNumber(getNum() + 1);

        } else if (i == R.id.tv_buy) {
            int num = Strings.toInt(mBinding.tvNum.getText().toString().trim());
            if (num>0){
                //最终支付的价格
                String amount = mBinding.tvRealPay.getText().toString().trim();
                orderCommand.setAmount(amount.substring(amount.indexOf("¥") + 1));
                orderCommand.setOrder_type("11");
                List<FillOrderEntity.SectionsBean> sectionsBeans = new ArrayList<>();
                //商品列表
                List<FillOrderEntity.Good> list = new ArrayList<>();
                //店铺信息
                FillOrderEntity.SectionsBean sectionsBean = new FillOrderEntity.SectionsBean();
                FillOrderEntity.Good good = new FillOrderEntity.Good();
                good.setItem_id(shopDetailGoodsEntity.getItem_id());
                good.setNum(num);
                list.add(good);
                sectionsBean.setItem_data(list);
                sectionsBeans.add(sectionsBean);

                orderCommand.setSections(sectionsBeans);
                doOrderCreate(orderCommand);
            }else {
                ToastUtils.showShort("库存不足！");
            }

        }
    }

    private void doGoodsDetails() {
        RetrofitApiFactory.createApi(LocalLifeApi.class)
                .getGoodsDetails(good_id, MainActivity.getLongitude(),MainActivity.getLatitude())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<LocalLifeGoodsDetailEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<LocalLifeGoodsDetailEntity> data) {
                        if (dataExist(data)){
                            shopDetailGoodsEntity = data.getData();
                            mBinding.setItem(shopDetailGoodsEntity);
                            double money = Double.valueOf(shopDetailGoodsEntity.getPrice());
                            mBinding.tvSumMoney.setText(FloatUtils.formatMoney(money));
                            mBinding.tvRealPay.setText(FloatUtils.formatMoney(money));
                            choicePrice = new SpecsGroupEntity();
                            choicePrice.setStock(Long.valueOf(shopDetailGoodsEntity.getStock()));
                            if (Long.valueOf(shopDetailGoodsEntity.getStock())>0){
                                setNumber(1);
                            }else {
                                setNumber(0);
                            }
                            String start_date = shopDetailGoodsEntity.getStart_date();
                            String startTime = start_date.replace("-", "/");
                            String end_date = shopDetailGoodsEntity.getEnd_date();
                            String endTime = end_date.replace("-", "/");
                            String validity = "有效期：" + startTime + "-" + endTime;
                            mBinding.tvValidity.setText(validity);
                        }else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void doOrderCreate(final FillOrderEntity orderCommand) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .createOrder(orderCommand)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        EventBus.getDefault().post(new ShopCartEvent());
                        if (dataExist(data)) {
                            payEntity = data.getData();
                            if (!TextUtils.isEmpty(data.getData().getPay_order_no())) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("type",1);
                                bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, payEntity);
                                JumpUtil.overlay(getContext(), VerificationOrderPayOnlineActivity.class, bundle);
                                finish();
                            }
                        } else {
                            finish();
                        }
                    }
                });
    }
}
