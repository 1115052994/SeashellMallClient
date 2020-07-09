package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.order.RefundListEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletInfoEntity;
import com.liemi.seashellmallclient.data.entity.wallet.WalletPoundageEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WalletApi {

    /**
     * 钱包信息
     */
    @FormUrlEncoded
    @POST("/wallet/index/info")
    Observable<BaseData<WalletInfoEntity>> doWalletInfo(@Field("param") String param);

    /*
    * 提取手续费
    * */
    @FormUrlEncoded
    @POST("/wallet/index/query-fee")
    Observable<BaseData<WalletPoundageEntity>> doWalletPoundage(@Field("amount") String amount);

    /*
     * 提取
     * */
    @FormUrlEncoded
    @POST("/wallet/index/extract")
    Observable<BaseData> doWalletExtract(@Field("amount") String amount,
                                         @Field("address") String address,
                                         @Field("password") String password);

    /*
     * 转账
     * */
    @FormUrlEncoded
    @POST("/wallet/index/transfer")
    Observable<BaseData> doWalletTransfer(@Field("amount") String amount,
                                         @Field("share_code") String share_code,
                                          @Field("password") String password);
}
