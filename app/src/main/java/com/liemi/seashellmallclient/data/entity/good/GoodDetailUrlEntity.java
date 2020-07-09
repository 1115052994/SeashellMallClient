package com.liemi.seashellmallclient.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：富文本链接
 * 创建人：Simple
 * 创建时间：2017/7/25 20:37
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class GoodDetailUrlEntity extends BaseEntity {

    private String detailUri;
    private String rich_text;
    private String buy_rich_text;

    public String getBuy_rich_text() {
        return buy_rich_text;
    }

    public void setBuy_rich_text(String buy_rich_text) {
        this.buy_rich_text = buy_rich_text;
    }



    public GoodDetailUrlEntity(String rich_text) {
        this.rich_text = rich_text;
    }

    public GoodDetailUrlEntity(String rich_text, String detailUri) {
        this.rich_text = rich_text;
        this.detailUri = detailUri;
    }

    public GoodDetailUrlEntity(String rich_text, String detailUri, String buy_rich_text) {
        this.rich_text = rich_text;
        this.detailUri = detailUri;
        this.buy_rich_text = buy_rich_text;
    }

    public String getDetailUri() {
        return detailUri;
    }

    public void setDetailUri(String detailUri) {
        this.detailUri = detailUri;
    }

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }
}
