package com.liemi.seashellmallclient.ui.login;

import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.api.LoginApi;
import com.liemi.seashellmallclient.utils.CountDownTimerUtils;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.ImageCodeEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/27
 * 修改备注：
 */
public abstract class BaseImageCodeActivity<T extends ViewDataBinding> extends BaseActivity<T> {


    protected abstract String getPhone();

    protected abstract String getAuthType();

    private EditText etImageCode;

    private ImageView ivImageCode;

    private TextView tvGetCode;

    protected CountDownTimerUtils timerUtils;

    @Override
    protected void initData() {
        etImageCode = mBinding.getRoot().findViewById(R.id.et_image_code);
        ivImageCode = mBinding.getRoot().findViewById(R.id.iv_image_code);
        tvGetCode = mBinding.getRoot().findViewById(R.id.tv_get_code);
    }

    /**
     * 图形验证码输入框
     */
    protected EditText getEtImageCode() {
        return etImageCode == null ? new EditText(this) : etImageCode;
    }

    /**
     * 图形验证码
     */
    protected ImageView getIvImageCode() {
        return ivImageCode == null ? new ImageView(this) : ivImageCode;
    }

    /**
     * 验证码发送成功
     */
    protected void sendSMSOk(){
        if(tvGetCode != null) {
            startTimer(tvGetCode);
        }
    }

    public void startTimer(TextView textView) {
        timerUtils = new CountDownTimerUtils(textView);
        timerUtils.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerUtils != null) {
            timerUtils.cancel();
            timerUtils = null;
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_image_code) {
            doGetImageCode();
            return;
        }

        //获取验证码
        if (view.getId() == R.id.tv_get_code) {
            doGetCode();
        }
    }

    //请求获取登录验证码
    protected void doGetCode() {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doAuthCode(getPhone(), null, null, getAuthType())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        sendSMSOk();
                        showError(data.getErrmsg());
                    }
                });
    }

    //请求获取图形验证码
    private void doGetImageCode() {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getImageCode(getAuthType())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new FastObserver<BaseData<ImageCodeEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<ImageCodeEntity> data) {
                        GlideShowImageUtils.gifload(getContext(), data.getData().getUrl(), getIvImageCode());
                        getEtImageCode().setTag(R.id.tag_data, data.getData().getSign());
                    }
                });
    }

}
