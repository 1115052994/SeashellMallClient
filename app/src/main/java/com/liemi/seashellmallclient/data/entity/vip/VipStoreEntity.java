package com.liemi.seashellmallclient.data.entity.vip;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/5/6
 * 修改备注：
 */
public class VipStoreEntity {


    /**
     * id : 6
     * introduction :
     * if_introduction : 0
     * nickname : 陈小蓓
     * products_count : 10
     * banner : [{"banner_id":"3","img_url":"https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1539065220787.jpg","update_time":"2019-04-29 15:13:20"},{"banner_id":"4","img_url":"https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1539065220787.jpg","update_time":"2019-04-29 15:11:20"}]
     */

    private String id;
    private String introduction;
    private String if_introduction;
    private String nickname;
    private String products_count;
    private List<BannerBean> banner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIf_introduction() {
        return if_introduction;
    }

    public void setIf_introduction(String if_introduction) {
        this.if_introduction = if_introduction;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProducts_count() {
        return products_count;
    }

    public void setProducts_count(String products_count) {
        this.products_count = products_count;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean extends BaseEntity {
        /**
         * banner_id : 3
         * img_url : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_1539065220787.jpg
         * update_time : 2019-04-29 15:13:20
         */

        private String banner_id;
        private String img_url;
        private String update_time;

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
