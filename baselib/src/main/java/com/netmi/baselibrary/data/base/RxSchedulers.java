package com.netmi.baselibrary.data.base;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 15:09
 * 修改备注：
 */
public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> compose() {
        return observable ->
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

}
