package com.liemi.seashellmallclient.utils;

import android.content.Context;
import android.text.TextUtils;
import com.liemi.seashellmallclient.data.entity.SobotSystemEntity;
import com.liemi.seashellmallclient.data.entity.good.GoodsDetailedEntity;
import com.liemi.seashellmallclient.data.param.ShareMallParam;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/24
 * 修改备注：
 */
public class SobotApiUtils {


    public static SobotApiUtils getInstance() {
        return SobotApiUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final SobotApiUtils instance = new SobotApiUtils();
    }


    //跳转到智齿客服页面
    public void toCustomServicePage(Context context, UserInfoEntity userInfo, GoodsDetailedEntity goodEntity, SobotSystemEntity systemEntity) {

        Information information = new Information();
        information.setAppkey(ShareMallParam.SOBOT_KEY);
        //设置返回时弹出用户满意度评价
        information.setShowSatisfaction(true);
        //设置要咨询的商品信息
        if (goodEntity != null) {
            ConsultingContent content = new ConsultingContent();
            //咨询内容标题
            content.setSobotGoodsTitle(goodEntity.getTitle());
            //咨询内容图片
            content.setSobotGoodsImgUrl(goodEntity.getImg_url());
            //咨询来源页
            if (systemEntity != null && !TextUtils.isEmpty(systemEntity.getUrl())) {
                content.setSobotGoodsFromUrl(systemEntity.getUrl());
            } else {
                content.setSobotGoodsFromUrl(Constant.SHARE_GOOD + goodEntity.getItem_id());
            }
            //描述
            content.setSobotGoodsDescribe(goodEntity.getRemark());
            //标签
            content.setSobotGoodsLable(goodEntity.getShowPrice());
            information.setConsultingContent(content);
        }

        //设置用户信息
        if (userInfo != null) {
            information.setUid(userInfo.getUid());
            information.setUname(userInfo.getNickname());
            information.setFace(userInfo.getHead_url());
            information.setTel(userInfo.getPhone());
        }

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        information.setTranReceptionistFlag(0);
        if (systemEntity != null
                && systemEntity.getTransferAction() != null
                && !TextUtils.isEmpty(systemEntity.getTransferAction().getDeciId())) {
            information.setReceptionistId(systemEntity.getTransferAction().getDeciId());
        }

        //1仅机器人 2仅人工 3机器人优先 4人工优先
        information.setInitModeType(4);
        //可转入其它客服
        SobotApi.startSobotChat(context, information);
    }


}
