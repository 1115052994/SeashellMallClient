package com.liemi.seashellmallclient.ui.good;

import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.StoreApi;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.data.entity.shopcar.ShopCartEntity;
import com.liemi.seashellmallclient.data.event.ShopCartEvent;
import com.liemi.seashellmallclient.data.param.GoodsBuyDialogParam;
import com.liemi.seashellmallclient.data.param.GoodsParam;
import com.liemi.seashellmallclient.databinding.SharemallDialogFragmentGoodsBuyBinding;
import com.liemi.seashellmallclient.databinding.SharemallItemGoodsSpecsBinding;
import com.liemi.seashellmallclient.ui.good.order.FillOrderActivity;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.cache.AppConfigCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.zhy.view.flowlayout.FlowLayout;
import org.greenrobot.eventbus.EventBus;

import java.util.*;

import static com.liemi.seashellmallclient.data.param.GoodsBuyDialogParam.GOODS_BUY_ADD_SHOPCART;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 18:20
 * 修改备注：
 */
public class GoodsBuyDialogFragment extends BaseDialogFragment<SharemallDialogFragmentGoodsBuyBinding> implements DialogInterface.OnKeyListener {

    private BaseRViewAdapter<SpecsEntity, BaseViewHolder> adapter;

    //一级规格名称
    private TextView tvProperty;

    private GoodsDetailedEntity goodEntity;

    private HashMap<String, SpecsEntity.ChildrenBean> choiceSpecs = new HashMap<>();

    private SpecsGroupEntity choicePrice;

    //弹出框类型
    private int buyType = GoodsBuyDialogParam.GOODS_BUY_IMMEDIATELY;

    public GoodsBuyDialogFragment setGoodsEntity(GoodsDetailedEntity goodsEntity) {
        this.goodEntity = goodsEntity;
        return this;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
        if (mBinding != null) {
            mBinding.tvAddShopCart.setVisibility(View.GONE);
            switch (buyType) {
                case GOODS_BUY_ADD_SHOPCART:
                    mBinding.tvConfirm.setText(R.string.sharemall_add_to_cart);
                    break;
                case GoodsBuyDialogParam.GOODS_BUY_IMMEDIATELY:
                    mBinding.tvConfirm.setText(R.string.sharemall_immediate_pay);
                    break;

                case GoodsBuyDialogParam.GOODS_BUY_SPEC_CHOICE:
                    mBinding.tvAddShopCart.setVisibility(View.VISIBLE);
                    mBinding.tvAddShopCart.setText(R.string.sharemall_add_to_cart);
                    mBinding.tvConfirm.setText(R.string.sharemall_immediate_pay);
                    break;
            }
        }
    }

    public void setTvProperty(TextView tvProperty) {
        this.tvProperty = tvProperty;
    }

    @Override
    public int getTheme() {
        return R.style.sharemall_CustomDialog;
    }

    private boolean hideDialog;

    public void setHideDialog(boolean hideDialog) {
        this.hideDialog = hideDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //避免页面返回自动弹出
        if (hideDialog) {
            onStop();
            setHideDialog(false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }

        getDialog().setOnKeyListener(this);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.sharemall_dialog_fragment_goods_buy;
    }

    @Override
    protected void initUI() {
        if (goodEntity == null) {
            ToastUtils.showShort(getString(R.string.sharemall_no_commodity_information));
            onStop();
        } else {
            mBinding.setDoClick(this);
            mBinding.setItem(goodEntity);
            //显示限购数量
            mBinding.tvLimit.setText(goodEntity.isSecKill() ? String.format(getString(R.string.sharemall_format_limit_buy), goodEntity.getSeckillItem().getNum())
                    : goodEntity.isGroupItem() ? String.format(getString(R.string.sharemall_format_limit_buy), goodEntity.getGroupItem().getNum()) : "");

            setBuyType(buyType);

            mBinding.tvNum.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (choicePrice==null){
                        ToastUtils.showShort(getString(R.string.sharemall_lack_commodity_specification));
                        mBinding.tvNum.setFocusable(false);
                        mBinding.tvNum.setFocusableInTouchMode(false);
                    }else {
                        mBinding.tvNum.setFocusableInTouchMode(true);
                        mBinding.tvNum.setFocusable(true);
                        mBinding.tvNum.requestFocus();
                    }
                    return false;
                }
            });

            mBinding.tvNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        setNumber(1);
                    } else if (choicePrice != null
                            && !s.toString().equals(Strings.twoDecimal(getNum()))) {
                        if (Strings.toFloat(s.toString()) > choicePrice.getStock()) {
                            ToastUtils.showShort(getString(R.string.sharemall_buying_quantity_exceed_inventory));
                            setNumber((int) choicePrice.getStock());
                        }
                    }
                }
            });

            mBinding.rvNumber.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.rvNumber.setAdapter(adapter = new BaseRViewAdapter<SpecsEntity, BaseViewHolder>(getContext()) {
                @Override
                public int layoutResId(int viewType) {
                    return R.layout.sharemall_item_goods_specs;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder<SpecsEntity>(binding) {

                        int specsPosition;

                        @Override
                        public void bindData(SpecsEntity item) {
                            specsPosition = position;
                            final List<SpecsEntity.ChildrenBean> specsList = adapter.getItem(specsPosition).getChildren();
                            //流式布局的规格
                            getBinding().tflSpecs.setAdapter(new SpecsTagAdapter<SpecsEntity.ChildrenBean>(specsList) {
                                @Override
                                public View getView(FlowLayout parent, int position, SpecsEntity.ChildrenBean skuBean) {
                                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.sharemall_item_dialog_goods_buy_sku,
                                            getBinding().tflSpecs, false);

                                    int color = getResources().getColor(R.color.white);

                                    tv.setTextColor(choiceSpecs.containsKey(skuBean.getValue_id()) ?
                                            color : skuBean.getOption() == 0 ?
                                            Color.parseColor("#80333333") : Color.parseColor("#333333"));
                                    tv.setText(skuBean.getValue_name());
                                    return tv;
                                }

                                @Override
                                public boolean setSelected(int position, SpecsEntity.ChildrenBean childrenBean) {
                                    return choiceSpecs.containsKey(childrenBean.getValue_id());
                                }

                                @Override
                                public void unSelected(int position, View view) {
                                    choiceSpecs.remove(getItem(position).getValue_id());
                                    doListGoodsSpecs();
                                }

                            });

                            getBinding().tflSpecs.setOnTagClickListener((View view, int position, FlowLayout parent) -> specsList.get(position).getOption() == 0);

                            getBinding().tflSpecs.setOnSelectListener((Set<Integer> selectPosSet) -> {
                                Iterator<Integer> iterator = selectPosSet.iterator();
                                if (iterator.hasNext()) {
                                    SpecsEntity.ChildrenBean choiceBean = specsList.get(iterator.next());

                                    //相同分类的规格，只保留一个
                                    Iterator<SpecsEntity.ChildrenBean> keyIterator = choiceSpecs.values().iterator();
                                    while (keyIterator.hasNext()) {
                                        SpecsEntity.ChildrenBean bean = keyIterator.next();
                                        if (TextUtils.equals(bean.getProp_id(), choiceBean.getProp_id())) {
                                            choiceSpecs.remove(bean.getValue_id());
                                            break;
                                        }
                                    }

                                    choiceSpecs.put(choiceBean.getValue_id(), choiceBean);

                                    doListGoodsSpecs();
                                }
                            });
                            super.bindData(item);
                        }

                        @Override
                        public SharemallItemGoodsSpecsBinding getBinding() {
                            return (SharemallItemGoodsSpecsBinding) super.getBinding();
                        }
                    };
                }
            });

            mBinding.executePendingBindings();

            doListSpecs();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onStop();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_minus) {
            setNumber(getNum() - 1);

        } else if (i == R.id.tv_plus) {
            if (choicePrice == null) {
                ToastUtils.showShort(getString(R.string.sharemall_lack_commodity_specification));
                return;
            }
            setNumber(getNum() + 1);

        } else if (i == R.id.tv_confirm) {

            if (!checkBuy()) return;

            if (buyType == GOODS_BUY_ADD_SHOPCART) {
                doAddShopCart();
            } else {
                if (buyType == GoodsBuyDialogParam.GROUP_BUY_TYPE_LAUNCH) {
                    goodEntity.getGroupItem().setTeam_id("");
                }
                ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();
                ShopCartEntity addShopCart = new ShopCartEntity();
                addShopCart.setShop(goodEntity.getShop());
                List<GoodsDetailedEntity> list = new ArrayList<>();
                goodEntity.setNum(String.valueOf(getNum()));
                goodEntity.setIvid(choicePrice.getIvid());
                goodEntity.setPrice(choicePrice.getPrice());
                goodEntity.setItem_type(choicePrice.getItem_type());
                goodEntity.setValue_names(getString(R.string.sharemall_format_goods_specs_tip, choicePrice.getValue_names()));
                list.add(goodEntity);
                addShopCart.setList(list);
                shopCartEntities.add(addShopCart);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GoodsParam.SHOP_CARTS, shopCartEntities);
                JumpUtil.overlay(getContext(), FillOrderActivity.class, bundle);
                onStop();
            }
        } else if (i == R.id.tv_add_shop_cart) {
            if (checkBuy()) {
                doAddShopCart();
            }
        } else if (i == R.id.view_bg) {
            onStop();
        } else if (i == R.id.iv_cancel) {
            onStop();
        }
    }

    private boolean checkBuy() {
        if (choicePrice == null) {
            ToastUtils.showShort(getString(R.string.sharemall_lack_commodity_specification));
            return false;
        }

        if (buyType != GOODS_BUY_ADD_SHOPCART && (goodEntity.isSecKill() || goodEntity.isGroupItem()) && !TextUtils.isEmpty(goodEntity.getActivityStatus())) {
            ToastUtils.showShort(goodEntity.getActivityStatus());
            return false;
        }

        if (getNum() <= 0) {
            ToastUtils.showShort(getString(R.string.sharemall_good_num_greater_than_0));
            return false;
        }
        return true;
    }

    private int getNum() {
        return Strings.toInt(mBinding.tvNum.getText().toString());
    }

    private void setNumber(int number) {
        mBinding.tvNum.setText(String.valueOf(number));
        mBinding.tvMinus.setEnabled(number > 1);
        mBinding.tvPlus.setEnabled(choicePrice != null && choicePrice.enablePlus(goodEntity, number));
    }

    private boolean completeChoice() {
        return choiceSpecs.size() == adapter.getItemCount();
    }


    private void doListGoodsSpecs() {

        List<SpecsEntity.ChildrenBean> specs = null;
        if (!choiceSpecs.isEmpty()) {
            specs = new ArrayList<>(choiceSpecs.values());
        }

        if (specs == null || specs.isEmpty()) {
            adapter.setData(allSpecsList);
            if (completeChoice()) {
                doGetGoodsPrice();
            }else {
                choicePrice = null;
                mBinding.tvChoicePrice.setText(goodEntity.getShowPrice());
                mBinding.tvName.setText(String.format(getString(R.string.sharemall_format_stock_tips), String.valueOf(goodEntity.getStock())));
                if (!TextUtils.isEmpty(goodEntity.getImg_url())) {
                    GlideShowImageUtils.displayNetImage(getContext(), goodEntity.getImg_url(), mBinding.ivGoodPic);
                }
            }
            return;
        }


        for (SpecsEntity entity : allSpecsList) {

            //遍历所有规格
            for (SpecsEntity.ChildrenBean bean : entity.getChildren()) {

                bean.setOption(1);

                //逻辑是，判断该规格， 是否在已选规格组合中存在
                for (SpecsEntity.ChildrenBean choice : specs) {

                    if (TextUtils.equals(bean.getProp_id(), choice.getProp_id())) {
                        continue;
                    }

                    StringBuilder canChoiceIds = new StringBuilder();

                    for (SpecsGroupEntity groupEntity : allGroupList) {
                        //拿到已选规格的所有其它可选规格
                        if (groupEntity.getValue_ids().contains(choice.getValue_id())) {
                            if (groupEntity.getStock() > 0) {
                                canChoiceIds.append(groupEntity.getValue_ids());
                            }
                        }
                    }

                    if (!canChoiceIds.toString().contains(bean.getValue_id())) {
                        bean.setOption(0);
                        break;
                    }

                }
            }

        }

        adapter.setData(allSpecsList);

        //判断是否将规格选择完全了
        if (completeChoice()) {
            //筛选出完成规格选择的商品
            doGetGoodsPrice();
        }

    }


    /*获得最终的规格组合*/
    private void doGetGoodsPrice() {

        List<SpecsEntity.ChildrenBean> specs = null;
        if (!choiceSpecs.isEmpty()) {
            specs = new ArrayList<>(choiceSpecs.values());
        }

        //遍历所有组合
        for (SpecsGroupEntity groupEntity : allGroupList) {
            StringBuffer value = new StringBuffer();
            //已选的规格
            for (int i = 0; i < specs.size(); i++) {

                //组合如果包含该规格，则继续遍历
                if (groupEntity.getValue_ids().contains(specs.get(i).getValue_id())) {
                     value.append(groupEntity.getValue_names());
                    //直到满足所有已选的规格，则为选中的组合
                    if (i == specs.size() - 1) {
                        groupEntity.setItem_type(goodEntity.getItem_type());
                        choicePrice = groupEntity;
                        mBinding.tvChoicePrice.setText(choicePrice.getShowPrice());
                        mBinding.tvStock.setText(value.append(groupEntity.getValue_names()));
                        mBinding.tvStock.setText(value.toString());
                        mBinding.tvName.setText(String.format(getString(R.string.sharemall_format_stock_tips), String.valueOf(groupEntity.getStock())));
                        if (!TextUtils.isEmpty(choicePrice.getImg_url())) {
                            GlideShowImageUtils.displayNetImage(getContext(), choicePrice.getImg_url(), mBinding.ivGoodPic);
                        }
                        setNumber(getNum() > choicePrice.getStock() ? (int) choicePrice.getStock() : getNum());
                        if (tvProperty != null) {
                            tvProperty.setText(mBinding.tvStock.getText());
                        }
                        return;
                    }

                }
                // 有一个规格不符合，就不是该组合
                else {
                    break;
                }
            }

        }

        choicePrice = null;

    }


    private List<SpecsEntity> allSpecsList = new ArrayList<>();

    private void doListSpecs() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .listGoodsSpecs(goodEntity.getItem_id())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<SpecsEntity>>>() {

                    @Override
                    public void onSuccess(BaseData<List<SpecsEntity>> data) {
                        allSpecsList = data.getData();
//                        adapter.setData(allSpecsList);
                        doListSpecsGroup();
                    }

                    @Override
                    public void onFail(BaseData<List<SpecsEntity>> data) {
                        super.onFail(data);
                        onStop();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismiss();
                    }

                });

    }


    private List<SpecsGroupEntity> allGroupList = new ArrayList<>();

    private void doListSpecsGroup() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listSpecsGroup(goodEntity.getItem_id())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<SpecsGroupEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<List<SpecsGroupEntity>> data) {
                        allGroupList = data.getData();
                        for (SpecsEntity entity : allSpecsList) {

                            //遍历所有规格
                            for (SpecsEntity.ChildrenBean bean : entity.getChildren()) {

                                for (SpecsGroupEntity groupEntity : allGroupList) {
                                    //拿到已选规格的所有其它可选规格
                                    if (groupEntity.getValue_ids().contains(bean.getValue_id())) {
                                        if (groupEntity.getStock() > 0) {
//                                            bean.setStock(groupEntity.getStock());
                                            bean.setOption(1);
                                        } else {
//                                            if (bean.getStock() <= 0) {
                                                bean.setOption(0);
//                                            }
                                        }
                                    }
                                }
                            }

                        }
                        adapter.setData(allSpecsList);
                    }

                });

    }

    private void doAddShopCart() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .addShopCart(choicePrice.getIvid(), mBinding.tvNum.getText().toString())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort(getString(R.string.sharemall_operation_success));
                        EventBus.getDefault().post(new ShopCartEvent());
                        onStop();
                    }
                });

    }


}
