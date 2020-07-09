package com.liemi.seashellmallclient.data.entity.article;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.DateUtil;

/**
 * Created by Bingo on 2019/1/2.
 */

public class ArticleListEntity extends BaseEntity {

    /**
     * "id": "40",                    ## 咨询主键
     * "shop_id": "1",                    ## 店铺主键
     * "title": "网红推手新推手招募推广课",                    ## 标题
     * "img_url": "http://lianshidai.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15265489325818.jpg",                ## 展示图片
     * "content": "<span style=\"font-family:微软雅黑 Light\">好了，谢谢各位伙伴今晚的认知收听</span></span></p><p><span style=\";font-family:'微软雅黑 Light';font-size:14px\"><span style=\"font-family:微软雅黑 Light\">今天的课程就到这里结束了，感谢大家的收听</span>~</span></p><p><br/></p>",                    ## 富文本
     * "url": "",                    ## 跳转链接
     * "create_time": "2018-05-17 16:11:18",                    ## 创建时间
     * "status": "1",                        ## '是否显示 0：否 1：是'
     * "type": "1",                        ## 展示类型 1：富文本 2：链接
     * "sort": "0",                        ## 排序： 值越大越靠前
     * "is_top": "1",                        ## 是否置顶：1否 2:是
     * "comment_num": "2",                    ## 评论数
     * "read_num": "0",                    ## 阅读数
     * "support_num": "0",                    ## 点赞数
     * "collect_num": "0",                    ## 收藏数
     * "thid": "12",                        ## 一级分类主键
     * "thid_low": null,                    ## 二级分类主键
     * "is_hot": "1",                        ## 是否推荐：1:否 2：是
     * "remark": "旨在帮助大家理清问题，通过总结优秀推手的经验帮助大家建立自己的思路，以及相应的支持，顺利解决出首单问题，继而把推手做起来。",                        ## 概述
     * "update_time": "2018-05-31 14:55:05",                    ## 更新时间
     * "param": "/information/information/info?id=40",                ## 富文本参数 app兼容使用
     * "is_collect": 1,            ## 是否标记为已收藏 1:否 2：是
     * "is_support": 1,            ## 是否标记为已读 1:否 2：是
     * "is_read": 1                ## 是否标记为已点赞 1:否 2：是
     */

    private String id;
    private String shop_id;
    private String title;
    private String img_url;
    private String content;
    private String url;
    private String create_time;
    private String status;
    private int type;
    private String sort;
    private String is_top;
    private String comment_num;
    private String read_num;
    private String support_num;
    private String collect_num;
    private String thid;
    private Object thid_low;
    private String is_hot;
    private String remark;
    private String update_time;
    private String param;
    private int is_collect;
    private int is_support;
    private int is_read;
    private String good_num;

    public String getGood_num() {
        return good_num;
    }

    public void setGood_num(String good_num) {
        this.good_num = good_num;
    }

    public String getMMDDDCreate_time() {
        return DateUtil.strToMMDDDate(create_time);
    }

    public String getMMDDHHMMCreate_time() {
        return DateUtil.strToMMDDHHMMDate(create_time);
    }

    public String getCreate_time() {
        return DateUtil.strToMMDDDate(create_time);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getRead_num() {
        return read_num;
    }

    public void setRead_num(String read_num) {
        this.read_num = read_num;
    }

    public String getSupport_num() {
        return support_num;
    }

    public void setSupport_num(String support_num) {
        this.support_num = support_num;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getThid() {
        return thid;
    }

    public void setThid(String thid) {
        this.thid = thid;
    }

    public Object getThid_low() {
        return thid_low;
    }

    public void setThid_low(Object thid_low) {
        this.thid_low = thid_low;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
