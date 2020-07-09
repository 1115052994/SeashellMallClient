package com.liemi.seashellmallclient.data.entity.user;

import java.io.Serializable;

/**
 * 我的成长值信息
 */
public class MineGrowthEntity implements Serializable {
    private int growth;
    private int less_growth;
    private int role;
    private String time;
    private int level;
    private String level_name;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }

    public int getLess_growth() {
        return less_growth;
    }

    public void setLess_growth(int less_growth) {
        this.less_growth = less_growth;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}
