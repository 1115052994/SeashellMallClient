package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/24
 * 修改备注：
 */
public class VipLevelEntity extends BaseEntity {

    private String level;
    private String name;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
