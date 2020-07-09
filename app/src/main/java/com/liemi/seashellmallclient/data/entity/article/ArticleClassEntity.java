package com.liemi.seashellmallclient.data.entity.article;

/**
 * Created by Bingo on 2019/1/2.
 */

public class ArticleClassEntity {
    /**
     * "id": "12",                                ## 咨询主题主键
     "name": "了解推手",                        ## 咨询主题名称
     "pid": "0",                                ## 咨询主题上级主键
     "sort": 1,                                ## 排序优先级
     "img_url": "http://liemimofa                 ## 咨询主题图片
     */

    private String id;
    private String name;
    private String pid;
    private int sort;
    private String img_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
