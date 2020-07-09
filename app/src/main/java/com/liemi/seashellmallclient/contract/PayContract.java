package com.liemi.seashellmallclient.contract;

import com.liemi.seashellmallclient.data.entity.order.PayResult;
import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/8/3 10:52
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface PayContract {


    //View
    interface View extends BaseView {

        /**
         * 微信购买返回通过EventBus
         *
         * @param payResult
         */
//        void showWechatPayResult(WXPayResultEvent payResult);

        /**
         * 官方支付宝返回
         *
         * @param payResult
         */
        void showAlipayResult(PayResult payResult);

    }

    //Presenter
    interface Presenter extends BasePresenter {

        /**
         * 微信购买
         *
         * @param pay_order_no
         */
        void payWeChat(String pay_order_no);

        /**
         * 支付宝购买
         *
         * @param pay_order_no
         */
        void payAli(String pay_order_no);


    }

}
