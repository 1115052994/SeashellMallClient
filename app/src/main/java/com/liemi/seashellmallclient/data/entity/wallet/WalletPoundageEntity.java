package com.liemi.seashellmallclient.data.entity.wallet;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class WalletPoundageEntity extends BaseEntity {
    /*
    * "fee": "1.00",
         "amount_final": "9.00"
    * */
    private String fee;
    private String amount_final;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount_final() {
        return amount_final;
    }

    public void setAmount_final(String amount_final) {
        this.amount_final = amount_final;
    }
}
