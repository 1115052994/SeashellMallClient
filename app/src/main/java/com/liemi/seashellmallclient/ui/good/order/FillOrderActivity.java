package com.liemi.seashellmallclient.ui.good.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.api.OrderApi;
import com.liemi.seashellmallclient.data.api.VIPApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.AddressEntity;
import com.liemi.seashellmallclient.data.entity.InvoiceEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.MycoinEntity;
import com.liemi.seashellmallclient.data.entity.good.PayErrorGoods;
import com.liemi.seashellmallclient.data.entity.order.*;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.liemi.seashellmallclient.data.entity.user.IdCardEntity;
import com.liemi.seashellmallclient.data.entity.user.MineCouponEntity;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.data.entity.vip.VIPUserInfoEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.data.param.GrouponParam;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.liemi.seashellmallclient.databinding.ActivityFillOrderBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemFillOrderAuthBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemFillOrderDetailsBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemFillOrderFormDetailsBinding;
import com.liemi.seashellmallclient.ui.login.ForgetPassActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressAddActivity;
import com.liemi.seashellmallclient.ui.mine.address.AddressManageActivity;
import com.liemi.seashellmallclient.utils.CloneUtil;
import com.liemi.seashellmallclient.widget.PayDialog;
import com.liemi.seashellmallclient.widget.SwitchStateButton;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_NORMAL;
import static com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity.GOODS_VIP_GIFT;
import static com.liemi.seashellmallclient.data.entity.good.MycoinEntity.DEDUCTION_INTEGRAL;
import static com.liemi.seashellmallclient.data.param.CouponParam.*;
import static com.liemi.seashellmallclient.data.param.GrouponParam.ORDER_TYPE_GROUPON;
import static com.liemi.seashellmallclient.ui.good.order.PayErrorActivity.PAY_FAIL_GOODS;
import static com.liemi.seashellmallclient.ui.mine.address.AddressManageActivity.*;

public class FillOrderActivity extends BaseActivity<ActivityFillOrderBinding> {
    private BaseRViewAdapter<ShopCartEntity, BaseViewHolder> adapter;

    private ArrayList<ShopCartEntity> shopCartEntities;

    //所有商品总价
    private float sumGoodsPrice = 0,
    //总邮费
    sumPostage = 0,
    //可用积分和优惠券的商品总价
    sumAvailablePrice,
    //返利
    sumRebate,
    //砍价减免
    bargainReduction;

    //计算运费
    private FillExpressFeeEntity fillExpressFeeEntity = new FillExpressFeeEntity();
    //获取可用优惠券
    private FillCouponEntity fillCouponEntity = new FillCouponEntity();
    //所有未使用的优惠券列表
    private ArrayList<MineCouponEntity> unusedCoupons = new ArrayList<>();
    //跨境购身份信息
    private IdCardEntity idCardEntity;
    //VIP的余额
    private VIPUserInfoEntity vipInfoEntity;
    //积分和优惠券数量
    private MycoinEntity coinEntity;
    //选中的地址
    private AddressEntity choiceAddress = new AddressEntity();
    //店铺发票
    private InvoiceEntity choiceInvoice;
    //优惠券
    private MineCouponEntity useCouponEntity;
    //余额支付的密码
    private String balancePassword;
    //商家id集合
    private List<String> shopIdList = new ArrayList<>();
    public static void start(Context context, ArrayList<ShopCartEntity> shopCartEntities) {
        if (!Strings.isEmpty(shopCartEntities)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(GoodsParam.SHOP_CARTS, shopCartEntities);
            JumpUtil.overlay(context, FillOrderActivity.class, bundle);
        } else {
            ToastUtils.showShort(R.string.sharemall_not_order_data);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fill_order;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_commodity_settlement));
        mBinding.setDoClick(this::doClick);
        mBinding.etIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                idCardEntity.setCard_no(s.toString());
            }
        });

        initGoodsData();
    }

    @Override
    protected void initData() {
        shopCartEntities = (ArrayList<ShopCartEntity>) getIntent().getSerializableExtra(GoodsParam.SHOP_CARTS);
        if (Strings.isEmpty(shopCartEntities)) {
            ToastUtils.showShort(R.string.sharemall_no_commodity_information);
            finish();
            return;
        }

        //加载优惠券、余额支付
        boolean loadCoupon = false, loadBalance = true;
        shopIdList.clear();
        //计算总价
        for (ShopCartEntity shopCartEntity : shopCartEntities) {

            float disabledSumPrice = 0, availableSumPrice = 0;
            for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                //选取店铺下商品最高的邮费
                if (Strings.toFloat(entity.getPostage()) > Strings.toFloat(shopCartEntity.getPostage())) {
                    shopCartEntity.setPostage(entity.getPostage());
                }

                //不可用积分和优惠券的商品总价
                if (entity.getItem_type() == GOODS_VIP_GIFT || entity.getActivity_type() != GOODS_NORMAL) {
                    disabledSumPrice += (Strings.toFloat(entity.getRealPrice()) * Strings.toFloat(entity.getNum()));
                }
                //可用积分和优惠券的商品总价
                else {
                    loadCoupon = true;
                    availableSumPrice += (Strings.toFloat(entity.getRealPrice()) * Strings.toFloat(entity.getNum()));
                }

                //是否包含跨境购商品
                if (entity.getIs_abroad() == 1 && idCardEntity == null) {
                    idCardEntity = new IdCardEntity();
                }

                //如果订单中有VIP礼包，隐藏余额支付和优惠券
                if (entity.getItem_type() == GOODS_VIP_GIFT) {
                    loadBalance = false;
                }

                sumRebate += Strings.toFloat(entity.isGroupItem() ? entity.getGroupItem().getGroup_share() : entity.getShare()) * Strings.toFloat(entity.getNum());
                fillExpressFeeEntity.getItem_list().add(new FillExpressFeeEntity.ItemListBean(entity.getItem_id(), entity.getNum()));
                fillCouponEntity.getItem_data().add(new FillCouponEntity.ItemDataBean(entity.getIvid(), entity.getNum()));
            }
            sumGoodsPrice += disabledSumPrice + availableSumPrice;
            sumAvailablePrice += availableSumPrice;
            shopCartEntity.setSumPrice(disabledSumPrice + availableSumPrice);
            shopCartEntity.setDisabledSumPrice(disabledSumPrice);
            shopCartEntity.setAvailableSumPrice(availableSumPrice);
            shopIdList.add(shopCartEntity.getShop().getId());
        }

        resetPrice();
        doListAddress();
        doGetMyCoinInfo();

        if (loadCoupon) {
            mBinding.setShowCoupon(true);
            doGetFillOrderCoupon();
        }
        if (loadBalance && ShareMallUserInfoCache.get().isVip()) {
            doGetVIPUserInfo();
            mBinding.setShowBalance(true);
        }
    }

    private void initGoodsData() {
        mBinding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvData.setAdapter(adapter = new BaseRViewAdapter<ShopCartEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.sharemall_item_fill_order_form_details;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder<ShopCartEntity>(binding) {

                    @Override
                    public void bindData(ShopCartEntity item) {
                        SharemallItemFillOrderFormDetailsBinding detailsBinding = (SharemallItemFillOrderFormDetailsBinding) getBinding();

                        //买家留言
                        detailsBinding.etRemark.setText(getItem(position).getRemark());
                        detailsBinding.etRemark.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                getItem(position).setRemark(editable.toString());
                            }
                        });

                        //商品列表
                        detailsBinding.rvGoods.setLayoutManager(new LinearLayoutManager(context));
                        BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> goodAdapter;
                        detailsBinding.rvGoods.setAdapter(goodAdapter = new GoodsAdapter(context));
                        goodAdapter.setData(item.getList());
                        super.bindData(item);
                    }

                };
            }

            class GoodsAdapter extends BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> {

                private GoodsAdapter(Context context) {
                    super(context);
                }

                @Override
                public int layoutResId(int position) {
                    return R.layout.sharemall_item_fill_order_goods;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder(binding) {
                    };
                }
            }

        });
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.rl_address) {
            //地址选择
            jumpChoiceAddress();
        } else if (view.getId() == R.id.ll_invoice) {
            //发票填写
            Bundle bundle = new Bundle();
            if (choiceInvoice != null) {
                bundle.putSerializable(OrderParam.INVOICE_ENTITY, choiceInvoice);
            }
            JumpUtil.startForResult(getActivity(), InvoiceActivity.class, OrderParam.REQUEST_INVOICE, bundle);
        } else if (view.getId() == R.id.ll_coupon) {
            //优惠券选择
            CouponDialog.newInstance(useCouponEntity, sumGoodsPrice, unusedCoupons)
                    .setChoiceCouponListener((MineCouponEntity entity) -> {
                        useCouponEntity = entity;
                        resetPrice();
                    }).show(getSupportFragmentManager(), TAG);
        } else if (view.getId() == R.id.tv_cross_border_tips) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.sharemall_good_remind)
                    .setMessage(R.string.sharemall_cross_border_message)
                    .setPositiveButton(R.string.sharemall_confirm, null)
                    .show();
        } else if (view.getId() == R.id.tv_payment) {
            if (TextUtils.isEmpty(choiceAddress.getMaid())) {
                ToastUtils.showShort(getString(R.string.sharemall_please_set_the_address_first));
                return;
            }

            //不满足购买要求
            if (!fillExpressFeeEntity.isBuy()) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.sharemall_warm_reminder)
                        .setMessage(R.string.sharemall_address_does_not_support_delivery)
                        .setPositiveButton(R.string.sharemall_change_receive_address, (DialogInterface dialog, int which) -> jumpChoiceAddress())
                        .setNegativeButton(R.string.sharemall_confirm, null)
                        .show();
                return;
            }

            FillOrderEntity orderCommand = new FillOrderEntity();

            orderCommand.setAddress_id(choiceAddress.getMaid());

            //平台优惠券
            if (useCouponEntity != null) {
                orderCommand.setP_cuid(String.valueOf(useCouponEntity.getCu_id()));
            }
            //如果包含跨境购，做简单的校验
            if (idCardEntity != null) {
                if (!Strings.isIDCard(idCardEntity.getCard_no())) {
                    ToastUtils.showShort(R.string.sharemall_please_input_true_id_card);
                    return;
                }
                orderCommand.setCard_name(idCardEntity.getCard_name());
                orderCommand.setCard_no(idCardEntity.getCard_no());
            }

            //最终支付的价格
            orderCommand.setAmount(String.valueOf(getResultPayPrice()));
            List<FillOrderEntity.SectionsBean> sectionsBeans = new ArrayList<>();

            for (ShopCartEntity shopCartEntity : shopCartEntities) {
                //商品列表
                List<FillOrderEntity.Good> list = new ArrayList<>();
                //店铺信息
                FillOrderEntity.SectionsBean sectionsBean = new FillOrderEntity.SectionsBean();
                for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                    FillOrderEntity.Good good = new FillOrderEntity.Good();
                    if (!TextUtils.isEmpty(entity.getCart_id())) {
                        good.setCart_id(entity.getCart_id());
                    }
                    good.setIvid(entity.getIvid());
                    good.setNum((int) Strings.toFloat(entity.getNum()));
                    good.setItem_type(entity.getItem_type());
                    list.add(good);
                }
                sectionsBean.setItem_data(list);
                sectionsBean.setRemark(shopCartEntity.getRemark());
                sectionsBeans.add(sectionsBean);
            }
            orderCommand.setInvoice(choiceInvoice);
            orderCommand.setSections(sectionsBeans);
            doOrderCreate(orderCommand);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //收货地址
            if (requestCode == OrderParam.REQUEST_ADDRESS) {
                AddressEntity addressEntity = (AddressEntity) data.getSerializableExtra(ADDRESS_ENTITY);
                if (addressEntity == null) {
                    addressEntity = new AddressEntity();
                }
                choiceAddress = addressEntity;
                mBinding.setAddress(choiceAddress);
                doCalculatingFreight();
            } else if (requestCode == OrderParam.REQUEST_INVOICE) {
                choiceInvoice = data != null ? (InvoiceEntity) data.getSerializableExtra(OrderParam.INVOICE_ENTITY) : null;
                mBinding.setInvoice(choiceInvoice);
            }
        }
    }

    private void jumpChoiceAddress() {
        Bundle addressBundle = new Bundle();
        addressBundle.putInt(CHOICE_ADDRESS, 1);
        addressBundle.putString(CHOICE_ADDRESS_MAID, choiceAddress.getMaid());
        JumpUtil.startForResult(getActivity(), AddressManageActivity.class, OrderParam.REQUEST_ADDRESS, addressBundle);
    }

    //总价计算规则：商品总价 - 优惠券 - 积分抵扣  + 总邮费(sumPostage) - 余额
    //积分抵扣规则：最多只能抵扣（商品总价 - 店铺优惠券 - 平台优惠券）/ 2 的金额

    //商品总价 - 优惠券 - 积分抵扣  + 总邮费(sumPostage)
    private float getUnusedBalancePrice() {
        return sumGoodsPrice
                - getDiscountPrice()
                + sumPostage;
    }

    //优惠券 + 积分抵扣
    private float getDiscountPrice() {
        return (useCouponEntity != null ? Strings.toFloat(useCouponEntity.getDiscount_num()) : 0);
    }

    //余额支付
    private float getBalancePayPrice() {
        return vipInfoEntity == null ? 0 : Strings.toFloat(vipInfoEntity.getBalance()) > getUnusedBalancePrice() ? getUnusedBalancePrice() : Strings.toFloat(vipInfoEntity.getBalance());
    }

    //商品总价 - 优惠券 - 积分抵扣  + 总邮费(sumPostage)
    private float getResultPayPrice() {
        return getUnusedBalancePrice();
    }

    //刷新价格的显示
    private void resetPrice() {

        //优惠券的显示
        if (useCouponEntity != null) {
            mBinding.tvCoupon.setText(String.format(getString(R.string.sharemall_discount_price), FloatUtils.formatMoney(useCouponEntity.getDiscount_num())));
        } else {
            mBinding.tvCoupon.setText(String.format(getString(R.string.sharemall_format_available), String.valueOf(unusedCoupons.size())));
        }

        //积分
        resetIntegral();

        //商品价格、运费
        mBinding.tvPriceGoods.setText(FloatUtils.formatMoney(sumGoodsPrice));
        mBinding.tvFreight.setText(String.format(getString(R.string.sharemall_format_plus_num), FloatUtils.formatMoney(sumPostage)));

        //优惠金额
        float disCountPrice = getDiscountPrice(),
                //余额抵扣
                balancePrice =  0;
        mBinding.llPriceCoupon.setVisibility(disCountPrice > 0 ? View.VISIBLE : View.GONE);
        mBinding.tvPriceCoupon.setText(String.format(getString(R.string.sharemall_discount_price), FloatUtils.formatMoney(disCountPrice)));

        //优惠和返利的显示
//        mBinding.tvDiscountTips.setVisibility(View.VISIBLE);
        //已优惠加上砍价减免的
        disCountPrice += bargainReduction;
       /* if (disCountPrice > 0 && sumRebate > 0) {
            mBinding.tvDiscountTips.setText(String.format(getString(R.string.sharemall_format_fill_order_discount_and_rebate), FloatUtils.formatMoney(disCountPrice), FloatUtils.formatMoney(sumRebate)));
        } else if (disCountPrice > 0) {
            mBinding.tvDiscountTips.setText(String.format(getString(R.string.sharemall_format_fill_order_discount), FloatUtils.formatMoney(disCountPrice)));
        } else if (sumRebate > 0) {
            mBinding.tvDiscountTips.setText(String.format(getString(R.string.sharemall_format_fill_order_rebate), FloatUtils.formatMoney(sumRebate)));
        } else {
            mBinding.tvDiscountTips.setVisibility(View.GONE);
        }*/

        //应付款
        mBinding.tvPricePay.setText(FloatUtils.formatMoney(getResultPayPrice()));
    }

    //可用优惠券和积分的商品总价 - 优惠券
    private float getAvailableAllCouponSumPrice() {
        float allCouponPrice = sumAvailablePrice - (useCouponEntity != null ? Strings.toFloat(useCouponEntity.getDiscount_num()) : 0);
        return allCouponPrice > 0 ? allCouponPrice : 0;
    }

    //刷新积分的显示
    private void resetIntegral() {
        if (coinEntity == null) return;
        mBinding.setShowIntegral(sumAvailablePrice > 0);
    }

    private void showData() {
        adapter.setData(shopCartEntities);
        hideProgress();
    }

    //1、地址列表
    private void doListAddress() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .getDefaultAddress("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<AddressEntity>>() {

                    @Override
                    public void onSuccess(BaseData<AddressEntity> data) {
                        if (data.getData() != null) {
                            choiceAddress = data.getData();
                            mBinding.setAddress(choiceAddress);
                        }
                    }

                    @Override
                    public void onComplete() {
                        //存在默认地址
                        if (!TextUtils.isEmpty(choiceAddress.getMaid())) {
                            doCalculatingFreight();
                        }
                        //包含跨境购商品
                        else if (idCardEntity != null) {
                            doGetIdCard();
                        } else {
                            showData();
                        }
                    }
                });
    }

    //2、计算邮费
    private void doCalculatingFreight() {
        fillExpressFeeEntity.setAddress_id(choiceAddress.getMaid());
        RetrofitApiFactory.createApi(OrderApi.class)
                .listExpressFee(fillExpressFeeEntity)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<ExpressFeeEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<List<ExpressFeeEntity>> data) {
                        if (!Strings.isEmpty(data.getData())) {
                            sumPostage = 0;
                            for (ShopCartEntity shopCartEntity : shopCartEntities) {
                                for (int i = 0; i < data.getData().size(); i++) {
                                    ExpressFeeEntity feeEntity = data.getData().get(i);
                                    if (TextUtils.equals(feeEntity.getShop_id(), shopCartEntity.getShop().getId())) {
                                        shopCartEntity.setPostage(feeEntity.getExpress());
                                        sumPostage += Strings.toDouble(feeEntity.getExpress());
                                        data.getData().remove(i);
                                        break;
                                    }
                                }
                            }
                            resetPrice();
                        }
                    }

                    @Override
                    public void onComplete() {
                        doListGoodsDelivery();
                    }
                });
    }

    //3、判断商品是否在配送区域内
    private void doListGoodsDelivery() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .listGoodsDelivery(fillExpressFeeEntity.getAddress_id(), fillExpressFeeEntity.getGoodsIds())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<GoodsDeliveryEntity>>>(this) {

                    private boolean canBuy = false;

                    @Override
                    public void onSuccess(BaseData<List<GoodsDeliveryEntity>> data) {
                        if (!Strings.isEmpty(data.getData())) {
                            boolean isBuy = true;
                            for (ShopCartEntity shopCartEntity : shopCartEntities) {
                                for (GoodsDetailedEntity goods : shopCartEntity.getList()) {
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        GoodsDeliveryEntity deliveryEntity = data.getData().get(i);
                                        if (TextUtils.equals(deliveryEntity.getItem_id(), goods.getItem_id())) {
                                            if (deliveryEntity.getIs_buy() == 0) {
                                                isBuy = false;
                                            }
                                            goods.setCan_buy(deliveryEntity.getIs_buy() == 1 ? "1" : null);
                                            break;
                                        }
                                    }
                                }
                            }
                            canBuy = isBuy;
                        }
                    }

                    @Override
                    public void onComplete() {
                        fillExpressFeeEntity.setBuy(canBuy);
                        if (idCardEntity != null
                                && TextUtils.isEmpty(idCardEntity.getCard_no())) {
                            doGetIdCard();
                        } else {
                            showData();
                        }
                    }
                });
    }

    //4、获取跨境购的身份信息
    private void doGetIdCard() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getIdCard("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<IdCardEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<IdCardEntity> data) {
                        idCardEntity = data.getData();
                        mBinding.etIdCard.setText(idCardEntity.getCard_no());
                        mBinding.setShowCrossBorder(true);
                    }

                    @Override
                    public void onComplete() {
                        showData();
                    }
                });
    }

    private void doGetFillOrderCoupon() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getFillOrderCoupon(fillCouponEntity)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<MineCouponEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<MineCouponEntity>> data) {
                        if (sumAvailablePrice > 0) {
                            unusedCoupons.addAll(data.getData().getList());
                            if (!unusedCoupons.isEmpty()) {
                                useCouponEntity = data.getData().getList().get(0);
                            }
                        }
                        resetPrice();
                    }
                });
    }

    private void doGetMyCoinInfo() {
        RetrofitApiFactory.createApi(OrderApi.class)
                .getMyCoin(AccessTokenCache.get().getToken())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<MycoinEntity>>() {
                    @Override
                    public void onSuccess(BaseData<MycoinEntity> data) {
                        coinEntity = data.getData();
                        resetIntegral();
                    }
                });
    }


    //获取余额相关信息
    private void doGetVIPUserInfo() {
        if (ShareMallUserInfoCache.get().isVip()) {
            RetrofitApiFactory.createApi(VIPApi.class)
                    .getVIPUserInfo(null)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new FastObserver<BaseData<VIPUserInfoEntity>>() {

                        @Override
                        public void onSuccess(BaseData<VIPUserInfoEntity> data) {
                            if (dataExist(data)) {
                                vipInfoEntity = data.getData();
                                resetPrice();
                            }
                        }

                    });
        }
    }

    //验证支付密码
    private void doCheckPayPWD(final String password, final PayDialog payDialog) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .checkPayPWD(MD5.GetMD5Code(password))
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        balancePassword = MD5.GetMD5Code(password);
                        resetPrice();
                        payDialog.dismiss();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        payDialog.clearPasswordText();
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                        payDialog.pwdError();
                    }
                });
    }


    private void doOrderCreate(final FillOrderEntity orderCommand) {
        showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .createOrder(orderCommand)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<OrderPayEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        EventBus.getDefault().post(new ShopCartEvent());

                        if (dataExist(data)) {
                            if (Strings.toDouble(data.getData().getPay_amount()) > 0) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(OrderParam.ORDER_PAY_ENTITY, data.getData());
                                bundle.putSerializable(PAY_FAIL_GOODS, new PayErrorGoods(data.getData()).getGoodsListByShopCart(shopCartEntities));
                                bundle.putSerializable("shop_id", (Serializable) shopIdList);
                                JumpUtil.overlay(getContext(), OrderPayOnlineActivity.class, bundle);
                                finish();
                            }
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onFail(BaseData<OrderPayEntity> data) {
                        super.onFail(data);
                    }
                });
    }
}
