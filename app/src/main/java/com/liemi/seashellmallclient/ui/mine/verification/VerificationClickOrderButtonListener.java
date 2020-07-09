package com.liemi.seashellmallclient.ui.mine.verification;

import com.liemi.seashellmallclient.data.entity.verification.VerificationOrderDetailEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/9/4
 * 修改备注：
 */
public interface VerificationClickOrderButtonListener {

    void clickLeftButton(VerificationOrderDetailEntity entity);

    void clickRightButton(VerificationOrderDetailEntity entity);
}
