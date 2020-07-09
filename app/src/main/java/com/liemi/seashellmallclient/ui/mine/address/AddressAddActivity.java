package com.liemi.seashellmallclient.ui.mine.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.AddressEntity;
import com.liemi.seashellmallclient.databinding.ActivityAddressAddBinding;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.CityPickerView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import static com.liemi.seashellmallclient.ui.mine.address.AddressManageActivity.ADDRESS_ENTITY;

public class AddressAddActivity extends BaseActivity<ActivityAddressAddBinding> {

    private AddressEntity addressEntity;
    private CityPickerView cityPickerView;
    private AddressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void initUI() {
        addressEntity = (AddressEntity) getIntent().getSerializableExtra(ADDRESS_ENTITY);
        if (addressEntity != null) {
            getTvTitle().setText(getString(R.string.sharemall_modify_address));
            mBinding.btDel.setVisibility(View.VISIBLE);
            mBinding.tvBelongArea.setText(addressEntity.getFull_name().replace(addressEntity.getAddress(), ""));
            mBinding.sbSetDefaultAddress.setmCurrentState(addressEntity.getIs_top() == 1);
            mBinding.setItem(addressEntity);
        } else {
            getTvTitle().setText(getString(R.string.sharemall_address_new_add2));
            mBinding.btDel.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.tv_setting)).setText(getString(R.string.sharemall_save));
    }

    @Override
    protected void initData() {
        cityPickerView = new CityPickerView(this);
        doGetProvince();
    }

    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
            return;
        }
        //点击省市区
        if (view.getId() == R.id.tv_belong_area) {
            if (!cityPickerView.getProvinceList().isEmpty()) {
                KeyboardUtils.hideKeyboard(view);
                cityPickerView.show((int options1, int option2, int options3, View v) -> {
                    if (addressEntity != null) {
                        CityChoiceEntity pEntity = cityPickerView.getChoiceProvince();
                        CityChoiceEntity.CListBean cEntity = cityPickerView.getChoiceCity();
                        CityChoiceEntity.CListBean.DListBean dEntity = cityPickerView.getChoiceArea();
                        addressEntity.setP_id(pEntity.getId());
                        addressEntity.setC_id(cEntity.getId());
                        addressEntity.setD_id(dEntity.getId());
                        addressEntity.setPname(pEntity.getName());
                        addressEntity.setCname(cEntity.getName());
                        addressEntity.setDname(dEntity.getName());
                    }
                    StringBuilder builder = new StringBuilder();
                    if (cityPickerView.getChoiceProvince() != null) {
                        builder.append(cityPickerView.getChoiceProvince().getName());
                    }
                    if (cityPickerView.getChoiceCity() != null) {
                        builder.append("-").append(cityPickerView.getChoiceCity().getName());
                    }
                    if (cityPickerView.getChoiceArea() != null) {
                        builder.append("-").append(cityPickerView.getChoiceArea().getName());
                    }
                    mBinding.tvBelongArea.setText(builder.toString());
                });
            } else {
                //城市加载中
                showProgress("");
                if (loadProvinceError)
                    doGetProvince();
            }
        } else if (view.getId() == R.id.tv_setting) {
            //点击确认
            String name = mBinding.etAddressAddConsignee.getText().toString();
            String phone = mBinding.etAddressAddPhone.getText().toString();
            String city = mBinding.tvBelongArea.getText().toString();
            String address = mBinding.etDetailAddress.getText().toString();
            if (TextUtils.isEmpty(name.trim())) {
                ToastUtils.showShort(getString(R.string.sharemall_please_enter_contact_person_first));
            } else if (TextUtils.isEmpty(phone)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_enter_phone_number_first));
            } else if (TextUtils.isEmpty(city)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_choose_area_first));
            } else if (TextUtils.isEmpty(address)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_address));
            } else if (!Strings.isPhone(phone)) {
                ToastUtils.showShort(getString(R.string.sharemall_please_input_right_phone));
            } else {
                AddressEntity entity = new AddressEntity();
                entity.setName(name);
                entity.setIs_top(mBinding.sbSetDefaultAddress.getCurrentState() ? 1 : 0);
                if (cityPickerView.getChoiceProvince() != null) {
                    entity.setP_id(cityPickerView.getChoiceProvince().getId());
                }
                if (cityPickerView.getChoiceCity() != null) {
                    entity.setC_id(cityPickerView.getChoiceCity().getId());
                }
                if (cityPickerView.getChoiceArea() != null) {
                    entity.setD_id(cityPickerView.getChoiceArea().getId());
                }
                entity.setTel(phone);
                entity.setAddress(address);
                entity.setFull_name(mBinding.tvBelongArea.getText() + entity.getAddress());
                showConfirmAddDialog(entity, view);
            }

        } else if (view.getId() == R.id.bt_del) {
            showConfirmAddDialog(addressEntity, view);
        }

    }

    //显示用户确认添加地址的弹框
    private void showConfirmAddDialog(final AddressEntity entity, final View view) {
        if (mDialog == null) {
            mDialog = new AddressDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        if (view.getId() == R.id.tv_setting) {
            mDialog.setTitle(getString(R.string.sharemall_hint));
            if (addressEntity == null) {
                mDialog.setMessage(getString(R.string.sharemall_confirm_add_address));
            } else {
                mDialog.setMessage(getString(R.string.sharemall_confirm_update_address));
            }
        } else if (view.getId() == R.id.bt_del) {
            mDialog.setMessage(getString(R.string.sharemall_confirm_delete_address));
        }
        mDialog.setClickConfirmListener(() -> {
            if (view.getId() == R.id.tv_setting) {
                if (addressEntity == null) {
                    doAddressSave(entity.getName(),
                            cityPickerView.getChoiceProvince() != null ? cityPickerView.getChoiceProvince().getId() : null,
                            cityPickerView.getChoiceCity() != null ? cityPickerView.getChoiceCity().getId() : null,
                            cityPickerView.getChoiceArea() != null ? cityPickerView.getChoiceArea().getId() : null,
                            entity.getTel(), entity.getAddress(), mBinding.sbSetDefaultAddress.getCurrentState() ? 1 : 0);
                } else {
                    addressEntity.setFull_name(mBinding.tvBelongArea.getText() + entity.getAddress());
                    doAddressEdit(entity.getName(), addressEntity.getP_id(),
                            addressEntity.getC_id(), addressEntity.getD_id(),
                            entity.getTel(), entity.getAddress(), mBinding.sbSetDefaultAddress.getCurrentState() ? 1 : 0);
                }
            } else if (view.getId() == R.id.bt_del) {
                doAddressDel(entity);
            }
        });
    }

    //加载失败后， 点击可再次加载
    private boolean loadProvinceError = false;

    //加载省市区
    private void doGetProvince() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .listCity(1)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<List<CityChoiceEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<List<CityChoiceEntity>> data) {
                        //组装数据
                        cityPickerView.loadCityData(data);
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            hideProgress();
                            mBinding.tvBelongArea.performClick();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        loadProvinceError = true;
                    }

                    @Override
                    public void onFail(BaseData<List<CityChoiceEntity>> data) {
                        super.onFail(data);
                        loadProvinceError = true;
                    }

                });
    }

    //保存新地址
    private void doAddressSave(String name, String pid, String cid, String did, String tel, String address, int isTop) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doAddNewAddress(name, pid, cid, did, tel, address, isTop)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<AddressEntity>>(this) {

                    @Override
                    public void onSuccess(BaseData<AddressEntity> data) {
                        hideProgress();
                        Intent intent = new Intent();
                        intent.putExtra(ADDRESS_ENTITY, data.getData());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                });

    }

    //保存编辑后的地址
    private void doAddressEdit(final String name, String pid, String cid, String did, final String tel, final String address, int isTop) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateAddress(addressEntity.getMaid(), name, pid, cid, did, tel, address, isTop, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        hideProgress();
                        addressEntity.setName(name);
                        addressEntity.setTel(tel);
                        addressEntity.setAddress(address);
                        Intent intent = new Intent();
                        intent.putExtra(ADDRESS_ENTITY, addressEntity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                });

    }

    private void doAddressDel(final AddressEntity addressEntity) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doDeleteAddress(addressEntity.getMaid())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        showError(getString(R.string.sharemall_delete_success));
                        finish();
                    }

                });

    }

}
