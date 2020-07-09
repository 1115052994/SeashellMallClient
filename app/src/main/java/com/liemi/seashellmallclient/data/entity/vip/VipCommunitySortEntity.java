package com.liemi.seashellmallclient.data.entity.vip;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/27
 * 修改备注：
 */
public class VipCommunitySortEntity implements Serializable {


    //用于筛选： 用户等级，关系，收益范围
    private String level, relation, min_income, max_income;

    private List<String> push_uids;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMin_income() {
        return min_income;
    }

    public void setMin_income(String min_income) {
        this.min_income = min_income;
    }

    public String getMax_income() {
        return max_income;
    }

    public void setMax_income(String max_income) {
        this.max_income = max_income;
    }

    public List<String> getPush_uids() {
        return push_uids;
    }

    public void setPush_uids(List<String> push_uids) {
        this.push_uids = push_uids;
    }
}
