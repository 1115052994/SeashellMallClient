package com.liemi.seashellmallclient.ui.mine.userinfo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.data.cache.ShareMallUserInfoCache;
import com.liemi.seashellmallclient.data.entity.user.ShareMallUserInfoEntity;
import com.liemi.seashellmallclient.databinding.ActivityUserInfoBinding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.ui.login.ForgetPassActivity;
import com.liemi.seashellmallclient.widget.EditDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.lzy.imagepicker.view.CropImageView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_TAKE;

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> implements FileUploadContract.View {

    //请求打开相机的requestCode
    protected static final int REQUEST_OPEN_CAMERA = 1001;
    //请求打开相册的requestCode
    protected static final int REQUEST_OPEN_ALBUM = 1002;

    private ChangeHeadSexDialog changeSexDialog;

    private EditDialog mDialog;

    private FileUploadPresenterImpl filePresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_user_info));
        mBinding.llPayPassword.setVisibility(ShareMallUserInfoCache.get().isVip() ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void initData() {
        showUserInfo(ShareMallUserInfoCache.get());
        basePresenter = filePresenter = new FileUploadPresenterImpl(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showUserInfo(ShareMallUserInfoCache.get());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            //共享元素动画的退出
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_head_image) {
            ImagePicker.getInstance().setStyle(CropImageView.Style.CIRCLE);
            ImagePicker.getInstance().setMultiMode(false);
            ImagePicker.getInstance().setCrop(true);
            startActivityForResult(new Intent(this, ImageGridActivity.class), REQUEST_CODE_TAKE);
            return;
        }
        if (view.getId() == R.id.ll_nick_name) {
            JumpUtil.overlay(getContext(), ChangeNickNameActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_sex) {
            showChangeSexDialog();
            return;
        }
        if (view.getId() == R.id.ll_phone) {
            JumpUtil.overlay(this, ChangePhoneAuthActivity.class);
            return;
        }
        if (view.getId() == R.id.ll_mine_invite_person){
            if (ShareMallUserInfoCache.get().getIs_invited()==0){
                if (mDialog == null) {
                    mDialog = new EditDialog(getContext());
                }
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                mDialog.setConfirm(getString(R.string.sharemall_confirm_ok));
                mDialog.setClickConfirmListener(() -> {
                    if (TextUtils.isEmpty(mDialog.getMessage())){
                        ToastUtils.showShort("请填写邀请码");
                    }else {
                        doBindInviteCode(mDialog.getMessage());
                    }

                });
            }
            return;
        }
        //点击账户安全
        if (view.getId() == R.id.ll_pay_password) {
            JumpUtil.overlay(this, AccountSafeActivity.class);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_TAKE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    filePresenter.doUploadFiles(ImageItemUtil.ImageItem2String(images), true);
                }
            }
        }
    }

    //显示用户更换性别的dialog
    private void showChangeSexDialog() {
        if (changeSexDialog == null) {
            changeSexDialog = new ChangeHeadSexDialog(this);
            changeSexDialog.setButtonStr(getString(R.string.sharemall_sex_man), getString(R.string.sharemall_sex_women));
        }
        if (!changeSexDialog.isShowing()) {
            changeSexDialog.showBottom();
        }
        changeSexDialog.setClickFirstItemListener((string) -> {
            if (ShareMallUserInfoCache.get().getSex() != ShareMallUserInfoEntity.SEX_MAN) {
                doUpdateUserInfo(null, null, String.valueOf(ShareMallUserInfoEntity.SEX_MAN), null);
            }
            changeSexDialog.dismiss();
        });
        changeSexDialog.setClickSecondItemListener((string) -> {
            if (ShareMallUserInfoCache.get().getSex() != ShareMallUserInfoEntity.SEX_WOMEN) {
                doUpdateUserInfo(null, null, String.valueOf(ShareMallUserInfoEntity.SEX_WOMEN), null);
            }
            changeSexDialog.dismiss();
        });
    }

    //显示用户个人信息
    private void showUserInfo(ShareMallUserInfoEntity userInfoEntity) {
        mBinding.setUserInfo(userInfoEntity);
        mBinding.executePendingBindings();
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {
        if (!Strings.isEmpty(imgUrls)) {
            doUpdateUserInfo(imgUrls.get(0), null, null, null);
        } else {
            fileUploadFail(getString(R.string.sharemall_upload_image_failed));
        }
    }

    @Override
    public void fileUploadFail(String errMsg) {
        showError(errMsg);
    }

    //请求个人信息
    private void doUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(1)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<BaseData<ShareMallUserInfoEntity>>() {
                    @Override
                    public void onNext(BaseData<ShareMallUserInfoEntity> userInfoEntityBaseData) {
                        if (userInfoEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            showUserInfo(userInfoEntityBaseData.getData());
                        } else {
                            showError(userInfoEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });

    }

    /*
     * 请求修改个人信息
     * */
    private void doUpdateUserInfo(final String headImage, final String nickName, final String sex, final String birthday) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserInfoUpdate(headImage, nickName, sex, birthday, null, null, null)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        if (!TextUtils.isEmpty(headImage)) {
                            ShareMallUserInfoCache.get().setHead_url(headImage);
                        }

                        if (!TextUtils.isEmpty(nickName)) {
                            ShareMallUserInfoCache.get().setNickname(nickName);
                        }

                        if (!TextUtils.isEmpty(sex)) {
                            ShareMallUserInfoCache.get().setSex(Strings.toInt(sex));
                        }

                        if (!TextUtils.isEmpty(birthday)) {
                            ShareMallUserInfoCache.get().setBirthday(birthday);
                        }
                        showUserInfo(ShareMallUserInfoCache.get());
                    }
                });

    }

    private void doBindInviteCode(String message) {
        RetrofitApiFactory.createApi(LoginApi.class)
                .doBindInvitationCode(message)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("绑定成功");
                        doUserInfo();
                    }
                });
    }
}
