package com.liemi.seashellmallclient.data.entity.vip;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/3/21
 * 修改备注：
 */
public class VipRuleEntity {


    /**
     * level_name : 销售主管
     * level : 2
     * progress : 2.6
     * list : [{"type":1,"title":"销售佣金","task_name":"销售佣金任务","num":"7.60","total_num":"100.00"},{"type":2,"title":"累计订单数","task_name":"累计订单数任务","num":3,"total_num":"50"},{"type":3,"title":"邀请关注","task_name":"邀请关注任务","num":0,"total_num":"6"},{"type":4,"title":"直属有效销售代表","task_name":"直属有效销售代表任务","num":0,"total_num":"2"},{"type":4,"title":"团队有效销售代表","task_name":"团队有效销售代表任务","num":0,"total_num":"3"}]
     */

    private String level_name;
    private String level;
    private float progress;
    //是否是最高级 1是 0否
    private int top_level;
    //升到最高级方式 0 购买礼包；1做任务
    private int update_type;
    private List<ListBean> list;

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public int getTop_level() {
        return top_level;
    }

    public void setTop_level(int top_level) {
        this.top_level = top_level;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int update_type) {
        this.update_type = update_type;
    }

    public static class ListBean {
        /**
         * type : 1
         * title : 销售佣金
         * task_name : 销售佣金任务
         * num : 7.60
         * total_num : 100.00
         */

        private int type;
        private String title;
        private String task_name;
        private float num;
        private float total_num;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public float getNum() {
            return num;
        }

        public void setNum(float num) {
            this.num = num;
        }

        public float getTotal_num() {
            return total_num;
        }

        public void setTotal_num(float total_num) {
            this.total_num = total_num;
        }
    }
}
