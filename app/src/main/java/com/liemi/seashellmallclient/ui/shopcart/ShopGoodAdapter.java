package com.liemi.seashellmallclient.ui.shopcart;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.entity.StoreEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.entity.good.SpecsGroupEntity;
import com.liemi.seashellmallclient.databinding.SharemallItemShopCartGoodBinding;
import com.liemi.seashellmallclient.ui.good.GoodDetailPageActivity;
import com.liemi.seashellmallclient.ui.store.StoreDetailActivity;
import com.liemi.seashellmallclient.widget.ShopCartCallback;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

/**
 * Created by Bingo on 2018/11/27.
 */

public class ShopGoodAdapter extends BaseRViewAdapter<BaseEntity, BaseViewHolder> {

    private ShopCartCallback shopCartCallback;

    private boolean isEdit;

    public ShopGoodAdapter(Context context, @NonNull ShopCartCallback shopCartCallback) {
        super(context, null, R.layout.sharemall_item_shop_car_empty);
        this.shopCartCallback = shopCartCallback;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    @Override
    public int layoutResId(int viewType) {
        if (viewType == -1) {
            return R.layout.sharemall_item_shop_cart_store;
        }
        return R.layout.sharemall_item_shop_cart_good;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof StoreEntity ? -1 : super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder.itemView != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (isGoodsItem(position)) {
                        holder.doLongClick(v);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void emptyViewClick() {
        super.emptyViewClick();
        MApplication.getInstance().backHome();
    }

    public boolean isGoodsItem(int position) {
        return getItem(position) instanceof GoodsDetailedEntity;
    }

    public GoodsDetailedEntity getGoodsItem(int position) {
        return isGoodsItem(position) ? (GoodsDetailedEntity) super.getItem(position) : null;
    }

    public StoreEntity getStoreItem(int position) {
        return !isGoodsItem(position) ? (StoreEntity) super.getItem(position) : null;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<BaseEntity>(binding) {

            private SpecsGroupEntity choicePrice = new SpecsGroupEntity();

            private SharemallItemShopCartGoodBinding getGoodsBinding() {
                return (SharemallItemShopCartGoodBinding) super.getBinding();
            }

            private void setNum(float num) {
                getGoodsBinding().tvMinus.setEnabled(num > 1);
                choicePrice.setStock(Strings.toLong(getGoodsItem(position).getStock()));
                getGoodsBinding().tvPlus.setEnabled(choicePrice.enablePlus(getGoodsItem(position), (int) num));
                getGoodsBinding().tvCalculate.setText(Strings.twoDecimal(num));
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (view.getId() == R.id.tv_minus) {
                    setNum(Strings.toFloat(getGoodsBinding().tvCalculate.getText().toString()) - 1);
                } else if (view.getId() == R.id.tv_plus) {
                    setNum(Strings.toFloat(getGoodsBinding().tvCalculate.getText().toString()) + 1);
                } else if (view.getId() == R.id.cb_good) {
                    getGoodsItem(position).setChecked(getGoodsBinding().cbGood.isChecked());
                    shopCartCallback.childCheck();
                } else if (view.getId() == R.id.cb_store) {
                    getStoreItem(position).setChecked(((CheckBox) view).isChecked());
                    //将店铺下的所有商品设置为同一状态
                    for (int i = position + 1; i < getItemCount(); i++) {
                        if (isGoodsItem(i)) {
                            if (getGoodsItem(i).unableBuy(isEdit)) {
                                continue;
                            }
                            getGoodsItem(i).setChecked(((CheckBox) view).isChecked());
                        } else {
                            break;
                        }
                    }
                    shopCartCallback.childCheck();
                } else if (view.getId() == R.id.tv_store_name) {
                    StoreDetailActivity.start(context, getStoreItem(position).getId());
                } else if (isGoodsItem(position)) {
                    GoodDetailPageActivity.start(context, getGoodsItem(position).getItem_id(), null);
                }
            }

            PopupWindow window;

            @Override
            public void doLongClick(View view) {
                super.doLongClick(view);

                if (isEdit) return;

                // 用于PopupWindow的View
                View contentView = LayoutInflater.from(context).inflate(R.layout.sharemall_item_shop_cart_delete, null, false);

                contentView.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                        shopCartCallback.doDelete(position);
                    }
                });

                contentView.findViewById(R.id.ll_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });
                window = new PopupWindow(contentView, getGoodsBinding().rlContent.getWidth(), getGoodsBinding().rlContent.getHeight(), true);
                // 设置PopupWindow的背景
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // 设置PopupWindow是否能响应外部点击事件
                window.setOutsideTouchable(true);
                // 设置PopupWindow是否能响应点击事件
                window.setTouchable(true);
                // 显示PopupWindow，其中： 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
                window.showAsDropDown(getGoodsBinding().rlContent, 0, -getGoodsBinding().rlContent.getHeight());
            }


            @Override
            public void bindData(BaseEntity item) {
                if (getBinding() instanceof SharemallItemShopCartGoodBinding) {
                    final SharemallItemShopCartGoodBinding goodBinding = getGoodsBinding();
                    goodBinding.setIsEdit(isEdit);
                    GoodsDetailedEntity goodsItem = getGoodsItem(position);
                    setNum(Strings.toFloat(goodsItem.getNum()));
                    final TextView etCalculate = goodBinding.tvCalculate;

                    if (isEdit) {
                        goodBinding.llOver.setVisibility(View.GONE);
                    } else {
                        boolean activityOver = goodsItem.isSecKill() && goodsItem.getServiceTime() > goodsItem.getSecKillEndTime();

                        //已售罄、活动已过期
                        goodBinding.tvOver.setText(activityOver ? R.string.sharemall_activity_over : Strings.toInt(goodsItem.getStock()) <= 0 ? R.string.sharemall_goods_seller_over : R.string.nothing);
                        goodBinding.llOver.setVisibility(Strings.toInt(goodsItem.getStock()) <= 0 || activityOver ? View.VISIBLE : View.GONE);
                    }

                    etCalculate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (TextUtils.isEmpty(s.toString())) {
                                setNum(1f);
                            } else if (!s.toString().equals(Strings.twoDecimal(getGoodsItem(position).getNum()))) {
                                if (Strings.toFloat(s.toString()) > Strings.toDouble(getGoodsItem(position).getStock())) {
                                    ToastUtils.showShort(context.getString(R.string.sharemall_buying_quantity_exceed_inventory));
                                    setNum(Strings.toFloat(getGoodsItem(position).getStock()));
                                } else {
                                    shopCartCallback.doUpdateCartNum(getGoodsItem(position), Strings.toFloat(s.toString()));
                                }
                            }
                        }
                    });

                }
                super.bindData(item);
            }


        };
    }
}
