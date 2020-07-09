package com.liemi.seashellmallclient.ui.mine.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.liemi.seashellmallclient.data.api.MineApi;
import com.liemi.seashellmallclient.databinding.ActivitySuggestionFeedbackBinding;
import com.liemi.seashellmallclient.presenter.FileUploadPresenterImpl;
import com.liemi.seashellmallclient.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.netmi.baselibrary.data.base.FastObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;

public class SuggestionFeedbackActivity extends BaseActivity<ActivitySuggestionFeedbackBinding> implements FileUploadContract.View  {
    private PhotoAdapter photoAdapter;

    private FileUploadPresenterImpl fileUploadPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_suggestion_feedback;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getString(R.string.sharemall_suggestion_feedback));
        mBinding.rvPic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.rvPic.setNestedScrollingEnabled(false);
        photoAdapter = new PhotoAdapter(getContext());
        photoAdapter.setMax(6);
        mBinding.rvPic.setAdapter(photoAdapter);
    }

    @Override
    protected void initData() {
        basePresenter = fileUploadPresenter = new FileUploadPresenterImpl(this);
    }

    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.bt_submit) {
            final String content = mBinding.etContent.getText().toString();
            if (content.isEmpty()) {
                showError(getString(R.string.sharemall_please_input_suggestion_content));
                return;
            }
            if (photoAdapter.getItemSize() > 0) {
                fileUploadPresenter.doUploadFiles(photoAdapter.getItems(), true);
            } else {
                doFeedBack(content, mBinding.etPhone.getText().toString(),null);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case ImagePicker.RESULT_CODE_ITEMS:
                //添加图片返回
                if (data != null && requestCode == REQUEST_CODE_SELECT) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
            case ImagePicker.RESULT_CODE_BACK:
                //预览图片返回
                if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                    if (images != null) {
                        photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                    }
                }
                break;
        }
    }

    @Override
    public void fileUploadResult(List<String> imgUrls) {
        doFeedBack(mBinding.etContent.getText().toString(), mBinding.etPhone.getText().toString(),imgUrls);
    }

    @Override
    public void fileUploadFail(String errMsg) {
        showError(errMsg);
    }


    private void doFeedBack(String remark,String phone, List<String> imgs) {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doSuggestionBack(remark, phone, imgs, null)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new FastObserver<BaseData>(this) {
                    @Override
                    public void onSuccess(BaseData data) {
                        showError(data.getErrmsg());
                        finish();
                    }
                });
    }

}
