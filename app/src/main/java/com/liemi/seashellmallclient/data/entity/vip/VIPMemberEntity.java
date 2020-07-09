package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/3/21
 * 修改备注：
 */
public class VIPMemberEntity extends BaseEntity {

    /**
     * id : 1
     * name : 高级推手
     * level : 1
     * num : 3
     * fee : 0.06
     */

    private String id;
    private String name;
    private String level;
    private String num;
    private String fee;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
