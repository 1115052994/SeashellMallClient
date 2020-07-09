package com.liemi.seashellmallclient.data.entity;

/**
 * 类描述：智齿客服系统
 * 创建人：Simple
 * 创建时间：2018/1/18 11:03
 * 修改备注：
 */
public class SobotSystemEntity {

    private TransferActionBean transferAction;
    private String url;

    public TransferActionBean getTransferAction() {
        return transferAction;
    }

    public void setTransferAction(TransferActionBean transferAction) {
        this.transferAction = transferAction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class TransferActionBean {
        /**
         * actionType : to_group
         * deciId  : 2d812e28b2fb4e64949236229a93a7ec
         * optionId : 3
         * spillId : 5
         */

        private String actionType;
        private String deciId;
        private int optionId;
        private int spillId;

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getDeciId() {
            return deciId;
        }

        public void setDeciId(String deciId) {
            this.deciId = deciId;
        }

        public int getOptionId() {
            return optionId;
        }

        public void setOptionId(int optionId) {
            this.optionId = optionId;
        }

        public int getSpillId() {
            return spillId;
        }

        public void setSpillId(int spillId) {
            this.spillId = spillId;
        }
    }
}
