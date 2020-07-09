package com.liemi.seashellmallclient.contract;

import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

/**
 * Created by Bingo on 2019/1/28.
 */

public interface ShareContract {

    interface View extends BaseView {
        void readySuccess();

        void readyFailure(String errMsg);
    }

    interface Presenter extends BasePresenter {
        void readyShare();
    }

}
