package com.liemi.seashellmallclient.ui.mine.address;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.entity.AddressEntity;
import com.liemi.seashellmallclient.databinding.ActivityAddressManageBinding;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class AddressManageActivity extends BaseXRecyclerActivity<ActivityAddressManageBinding, AddressEntity> {

    public static final String ADDRESS_ENTITY = "address_entity";
    public static final String CHOICE_ADDRESS = "choice_address";
    public static final String CHOICE_ADDRESS_MAID = "choice_address_maid";

    private final static int ADDRESS_ADD = 0x11;
    private final static int ADDRESS_EDIT = 0x12;

    private AddressDialog mDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_address_manage;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_mine_address));
        getRightSetting().setText("新增");
        xRecyclerView = mBinding.xrvData;
        adapter = new BaseRViewAdapter<AddressEntity, BaseViewHolder>(this, xRecyclerView, R.layout.sharemall_layout_address_empty) {
            @Override
            public int layoutResId(int position) {
                return R.layout.sharemall_item_address;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if (view.getId() == R.id.tv_eidt) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(ADDRESS_ENTITY, adapter.getItem(position));
                            JumpUtil.startForResult(getActivity(), AddressAddActivity.class, ADDRESS_EDIT, bundle);
                        } else if (view.getId() == R.id.tv_delete) {
                            showDeleteDialog(adapter.getItem(position));
                        } else if (view.getId() == R.id.rb_default_address) {
                            AddressEntity entity = adapter.getItem(position);
                            doAddressEdit(entity, entity.getIs_top() == 0 ? 1 : 0);
                        } else if (getIntent().getIntExtra(CHOICE_ADDRESS, 0) > 0) {
                            Intent intent = new Intent();
                            intent.putExtra(ADDRESS_ENTITY, adapter.getItem(position));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                };
            }
        };

        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_ADD) {
                xRecyclerView.refresh();
            } else if (requestCode == ADDRESS_EDIT) {
                AddressEntity addressEntity = (AddressEntity) data.getSerializableExtra(ADDRESS_ENTITY);
                if (addressEntity != null) {
                    for (AddressEntity entity : adapter.getItems()) {
                        if (entity.getMaid().equals(addressEntity.getMaid())) {
                            adapter.replace(adapter.getItems().indexOf(entity), addressEntity);
                            break;
                        }
                    }
                } else {
                    xRecyclerView.refresh();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        String maid = getIntent().getStringExtra(CHOICE_ADDRESS_MAID);
        if (!TextUtils.isEmpty(maid) || getIntent().getIntExtra(CHOICE_ADDRESS, 0) > 0) {
            AddressEntity resultAddress = null;
            for (AddressEntity entity : adapter.getItems()) {
                if (TextUtils.equals(entity.getMaid(), maid)) {
                    resultAddress = entity;
                    break;
                }
            }
            if (resultAddress == null
                    && adapter.getItemCount() > 0) {
                resultAddress = adapter.getItem(0);
            }
            Intent intent = new Intent();
            intent.putExtra(ADDRESS_ENTITY, resultAddress);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        //点击设置按钮，跳转到添加地址页面
        if (view.getId() == R.id.tv_setting) {
            JumpUtil.overlay(this, AddressAddActivity.class);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefresh();
    }

    //显示询问用户是否删除的dialog
    private void showDeleteDialog(final AddressEntity entity) {
        if (mDialog == null) {
            mDialog = new AddressDialog(this);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialog.setClickConfirmListener(() -> doAddressDel(entity));
    }


    //获取收货地址列表
    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doAddressList(PageUtil.toPage(startPage), Constant.PAGE_ROWS)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData<PageEntity<AddressEntity>>>(this) {

                    @Override
                    public void onSuccess(BaseData<PageEntity<AddressEntity>> data) {
                        showData(data.getData());
                    }

                });
    }

    private void doAddressDel(final AddressEntity addressEntity) {
        RetrofitApiFactory.createApi(MineApi.class)
                .doDeleteAddress(addressEntity.getMaid())
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        adapter.remove(addressEntity);
                    }

                });

    }

    private void doAddressEdit(final AddressEntity entity, final int isTop) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUpdateAddress(entity.getMaid(), entity.getName(), entity.getP_id(), entity.getC_id(), entity.getD_id(), entity.getTel(), entity.getAddress(), isTop, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {

                    @Override
                    public void onSuccess(BaseData data) {
                        for (AddressEntity addressEntity : adapter.getItems()) {
                            addressEntity.setIs_top(0);
                        }
                        entity.setIs_top(isTop);
                        adapter.notifyDataSetChanged();
                    }

                });

    }

}
