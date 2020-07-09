package com.liemi.seashellmallclient.data.entity.good;

import com.netmi.baselibrary.data.entity.PageEntity;

/**
 * Created by Bingo on 2019/2/16.
 */

public class PageCommentEntity<T> extends PageEntity<T> {
    private String sum_commet_num;
    private String pic_commet_num;

    public String getSum_commet_num() {
        return sum_commet_num;
    }

    public void setSum_commet_num(String sum_commet_num) {
        this.sum_commet_num = sum_commet_num;
    }

    public String getPic_commet_num() {
        return pic_commet_num;
    }

    public void setPic_commet_num(String pic_commet_num) {
        this.pic_commet_num = pic_commet_num;
    }

}
