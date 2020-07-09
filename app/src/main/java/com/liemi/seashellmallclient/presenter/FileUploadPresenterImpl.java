package com.liemi.seashellmallclient.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.contract.FileUploadContract;
import com.nanchen.compresshelper.CompressHelper;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.*;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.FileUrlEntity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.ImageUtils;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：图片压缩上传
 * 创建人：Simple
 * 创建时间：2019/4/8
 * 修改备注：
 */
public class FileUploadPresenterImpl implements FileUploadContract.Presenter {

    private FileUploadContract.View view;

    private List<String> imgUrls = new ArrayList<>();

    public FileUploadPresenterImpl(FileUploadContract.View view) {
        this.view = view;
    }

    @Override
    public void doUploadFiles(List<String> filePaths, boolean compress) {
        view.showProgress(ResourceUtil.getString(R.string.sharemall_updating));
        imgUrls.clear();
        if (compress) {
            compressFiles(filePaths);
        } else {
            uploadFiles(filePaths);
        }
    }

    private void compressFiles(final List<String> filePaths) {

        Observable<List<String>> observable = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) {
                try {
                    List<String> compressPaths = new ArrayList<>();
                    String saveFilePath;
                    for (String path : filePaths) {
                        if (path.contains("http")) {
                            compressPaths.add(path);
                        } else {
                            saveFilePath = AppManager.getApp().getCacheDir() + "/compress/" + new Date().getTime() + ".jpg";
                            if (ImageUtils.save(ImageUtils.compressByQuality(ImageUtils.getBitmap(path), 1024 * 1024L), saveFilePath, Bitmap.CompressFormat.JPEG)) {
//                                compressPaths.add(saveFilePath);
                                compressPaths.add(CompressHelper
                                        .getDefault((Context) view)
                                        .compressToFile(new File(saveFilePath)).getAbsolutePath());
                            } else {
                                showFail(ResourceUtil.getString(R.string.sharemall_failed_to_submit_pictures));
                                break;
                            }
                        }
                    }

                    e.onNext(compressPaths);
                    e.onComplete();
                } catch (Exception ex) {
                    System.gc();
                    e.onError(ex);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
        if (view instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) view).bindUntilEvent(ActivityEvent.DESTROY));
        } else {
            observable = observable.compose(((RxFragment) view).bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> strings) {
                if (!Strings.isEmpty(strings)) {
                    uploadFiles(strings);
                } else {
                    showFail(ResourceUtil.getString(R.string.sharemall_not_compress_image));
                }
            }

            @Override
            public void onError(Throwable e) {
                showFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void uploadFiles(List<String> filePaths) {
        if (!Strings.isEmpty(filePaths)) {
            MultipartBody.Part filePart = FilesUploadUtils.multiPartFiles(filePaths.get(0));
            if (filePart != null) {
                Observable<BaseData<FileUrlEntity>> observable = RetrofitApiFactory.createApi(CommonApi.class)
                        .uploadFiles(filePart);
                if (view instanceof RxAppCompatActivity) {
                    observable = observable.compose(((RxAppCompatActivity) view).bindUntilEvent(ActivityEvent.DESTROY));
                } else {
                    observable = observable.compose(((RxFragment) view).bindUntilEvent(FragmentEvent.DESTROY));
                }
                observable.compose(RxSchedulers.compose())
                        .subscribe(new BaseObserver<BaseData<FileUrlEntity>>() {
                            @Override
                            public void onNext(BaseData<FileUrlEntity> data) {
                                if (data.getErrcode() == Constant.SUCCESS_CODE) {
                                    if (data.getData() != null) {
                                        imgUrls.add(data.getData().getUrl());
                                    }
                                    filePaths.remove(0);
                                    uploadFiles(filePaths);
                                } else {
                                    showFail(data.getErrmsg());
                                }
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            protected void onError(ApiException ex) {
                                showFail(ex.getMessage());
                            }
                        });
            } else {
                imgUrls.add(filePaths.get(0));
                filePaths.remove(0);
                uploadFiles(filePaths);
            }
        } else {
            showSuccess();
        }
    }

    private void showSuccess() {
        if (view != null) {
            view.hideProgress();
            view.fileUploadResult(imgUrls);
        }
    }

    private void showFail(String errMsg) {
        if (view != null) {
            view.fileUploadFail(errMsg);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void onError(String message) {

    }
}
