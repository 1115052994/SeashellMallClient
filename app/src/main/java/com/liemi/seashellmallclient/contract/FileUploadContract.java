package com.liemi.seashellmallclient.contract;

import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/4/8
 * 修改备注：
 */
public interface FileUploadContract {


    interface View extends BaseView {

        void fileUploadResult(List<String> imgUrls);

        void fileUploadFail(String errMsg);
    }

    interface Presenter extends BasePresenter {

        void doUploadFiles(List<String> filePaths, boolean compress);
    }

}
