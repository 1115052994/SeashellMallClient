package com.liemi.seashellmallclient.data.entity;

import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.io.Serializable;

public class InvoiceEntity implements Serializable {

    //发票内容：“商品明细”
    private String invoice_content;
    //发票类型 “电子发票
    private String invoice_type;
    //发票抬头 默认1个人，2单位
    private int type;
    private String company_name;
    private String company_code;
    private String phone;
    private String mail;

    public String getFormatType() {
        switch (type){
            case 1:
                return ResourceUtil.getString(R.string.sharemall_personal);
            case 2:
                return ResourceUtil.getString(R.string.sharemall_company);
            default:
                return ResourceUtil.getString(R.string.sharemall_unknown_type);
        }
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

