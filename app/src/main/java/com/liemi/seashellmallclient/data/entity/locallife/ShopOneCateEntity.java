package com.liemi.seashellmallclient.data.entity.locallife;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 19:39
 * 修改备注：
 */
public class ShopOneCateEntity extends BaseEntity {

    /**
     "mcid": "294",//分类ID
     "name": "美食",//名称
     "pid": "0",
     "level": "1",
     "sequence": "1",
     "is_home": "0",
     "is_total": "0",
     "second_category": [//二级分类列表
     ]
     }
     */
    private String mcid;
    private String name;
    private String pid;
    private String level;
    private String sequence;
    private String img_url;
    private String is_home;
    private boolean check;
    private String is_total;
    private List<ShopTwoCateEntity> second_category;

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

    public String getIs_total() {
        return is_total;
    }

    public void setIs_total(String is_total) {
        this.is_total = is_total;
    }

    public List<ShopTwoCateEntity> getSecond_category() {
        return second_category;
    }

    public void setSecond_category(List<ShopTwoCateEntity> second_category) {
        this.second_category = second_category;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
