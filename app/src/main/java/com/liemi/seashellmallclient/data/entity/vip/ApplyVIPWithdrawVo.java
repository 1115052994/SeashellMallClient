package com.liemi.seashellmallclient.data.entity.vip;

import java.io.Serializable;

/**
 * Created by Bingo on 2018/12/29.
 */

public class ApplyVIPWithdrawVo implements Serializable {
    /**
     * price_total	是	double	提现金额	50
     bank_name	是	string	银行名称	农业银行
     bank_code	是	string	银行id	1005
     bank_card	是	string	银行卡号	6228480078066962875
     name	是	string	用户真实姓名	李金炯
     phone	是	string	手机号	18856855449
     code	是	string	短信验证码	6666
     */
    private String price;
    private String bank_name;
    private String bank_code;
    private String bank_card;
    private String user_name;
    private String phone;
    private String code;

    public ApplyVIPWithdrawVo() {
    }

    public ApplyVIPWithdrawVo(String price, String bank_name, String bank_code, String bank_card,
                              String user_name, String phone, String code) {
        this.price = price;
        this.bank_name = bank_name;
        this.bank_code = bank_code;
        this.bank_card = bank_card;
        this.user_name = user_name;
        this.phone = phone;
        this.code = code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
