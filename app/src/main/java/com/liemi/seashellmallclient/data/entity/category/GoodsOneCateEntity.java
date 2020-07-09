package com.liemi.seashellmallclient.data.entity.category;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 19:39
 * 修改备注：
 */
public class GoodsOneCateEntity extends BaseEntity {

    /**
     * "mcid": "42",//主键
     "name": "剪刀砧板",//分类名称
     "pid": "36",//上级主键
     "level": "2",//等级 1：1级 2：2级
     "sequence": "0",//排序值
     "img_url": "http://antbt.oss-cn-hangzhou.aliyuncs.com/backend_img/AFWXZNTHK0134569_1504861528.png",//展示图片
     "is_home": "2"//是否在首页展示 0：否 1：是
     */
    private String mcid;
    private String name;
    private String pid;
    private String level;
    private String sequence;
    private String img_url;
    private String is_home;
    private boolean check;
    private int total_pages;
    private List<GoodsTwoCateEntity> second_category;

    public String getMcid() {
        return mcid;
    }

    public void setMcid(String mcid) {
        this.mcid = mcid;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIs_home() {
        return is_home;
    }

    public void setIs_home(String is_home) {
        this.is_home = is_home;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<GoodsTwoCateEntity> getSecond_category() {
        return second_category;
    }

    public void setSecond_category(List<GoodsTwoCateEntity> second_category) {
        this.second_category = second_category;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
