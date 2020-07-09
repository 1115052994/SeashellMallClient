package com.liemi.seashellmallclient.data.helper;

import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.data.param.OrderParam;
import com.netmi.baselibrary.utils.ResourceUtil;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/8
 * 修改备注：
 */
public class PayChannelHelper {

    public static String getTextByType(int channel) {
        /*
        * :0-微信支付1-支付宝支付2-Apple Pay3-积分支付4-ETH,10-余额支付等直接完成支付 SMALL_PAY = 6; //小程序支付
        * */
        switch (channel) {
            case 6:
            case OrderParam.PAY_CHANNEL_WECHAT:
                return ResourceUtil.getString(R.string.sharemall_wx_pay);
            case OrderParam.PAY_CHANNEL_ALIPAY:
                return ResourceUtil.getString(R.string.sharemall_ali_pay);
            case 2:
                return "Apple Pay";
            case 3:
                return "积分支付";
            case 4:
                return "ETH";
            case OrderParam.PAY_CHANNEL_BALANCE:
                return ResourceUtil.getString(R.string.sharemall_balance_payment);
        }
        return "";
    }

}
