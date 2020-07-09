package com.liemi.seashellmallclient.data.entity;

import com.liemi.seashellmallclient.data.entity.article.ShopCartArticleEntity;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.utils.DateUtil;

import java.util.List;

/**
 * 用户公告信息
 */
public class UserNoticeEntity extends PageEntity<ShopCartArticleEntity> {

    private ReadDataEntity read_data;

    public ReadDataEntity getRead_data() {
        return read_data;
    }

    public void setRead_data(ReadDataEntity read_data) {
        this.read_data = read_data;
    }

    //公告详情
    public static class NoticeDetailsEntity extends BaseEntity {
        private int id;
        private int notice_id;
        private int is_read;
        private int send_id;
        private int type;
        private String title;
        private String content;
        private String create_time;
        private String update_time;
        private String remark;
        private int link_type;
        private String param;
        private int read_num;   //已读数

        public String getMMDDDCreate_time() {
            return DateUtil.strToMMDDDate(create_time);
        }

        public String getMMDDHHMMCreate_time() {
            return DateUtil.strToMMDDHHMMDate(create_time);
        }


        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public int getSend_id() {
            return send_id;
        }

        public void setSend_id(int send_id) {
            this.send_id = send_id;
        }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getLink_type() {
            return link_type;
        }

        public void setLink_type(int link_type) {
            this.link_type = link_type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

    //公告未读数据
    public static class ReadDataEntity {
        private int all_no_readnum;

        public int getAll_no_readnum() {
            return all_no_readnum;
        }

        public void setAll_no_readnum(int all_no_readnum) {
            this.all_no_readnum = all_no_readnum;
        }
    }
}
