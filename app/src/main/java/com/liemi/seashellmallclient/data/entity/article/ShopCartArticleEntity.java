package com.liemi.seashellmallclient.data.entity.article;

import android.text.TextUtils;

import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.utils.DateUtil;

/**
 * Created by Bingo on 2019/1/9.
 */

public class ShopCartArticleEntity extends AgreementEntity {
    /**
     * "id": "11972",
     * "notice_id": "594",
     * "is_read": "0",//是否已读  0：否 1：是
     * "send_id": "921",
     * "type": "1",
     * "title": "用户公告",//主标题
     * "content": "<p>111111</p>",//富文本
     * "create_time": "2019-01-05 16:23:01",
     * "update_time": "2019-01-05 17:01:01",
     * "remark": "这个公告",
     * "link_type": "1",//展现方式 1：无点击效果 2：点击跳转链接 3：点击跳转富文本
     * "param": "",
     * "notice": {
     * "id": "594",
     * "title": "用户公告",
     * "content": "<p>111111</p>",
     * "create_time": "2019-01-05 16:23:01",
     * "update_time": "2019-01-05 17:01:01",
     * "send_status": "2",
     * "type": "1",
     * "remark": "这个公告",
     * "link_type": "1",
     * "status": "1",
     * "send_num": "0",
     * "read_num": "0",
     * "param": "",
     * "show_img": "http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/AFWXZNK012345689_1546676598.jpg" //列表展示图
     * },
     * "read_num": 0
     */

    private String notice_id;
    private int is_read;
    private String send_id;
    private String create_time;
    private String update_time;
    private NoticeBean notice;
    private String read_num;

    public String getMMDDDCreate_time() {
        return DateUtil.strToMMDDDate(create_time);
    }

    public String getMMDDHHMMCreate_time() {
        return DateUtil.strToMMDDHHMMDate(create_time);
    }

    public String getRead_num() {
        if (TextUtils.isEmpty(read_num) && notice != null) {
            read_num = notice.getRead_num();
        }
        return read_num;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
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

    public NoticeBean getNotice() {
        return notice;
    }

    public void setNotice(NoticeBean notice) {
        this.notice = notice;
    }


    public void setRead_num(String read_num) {
        this.read_num = read_num;
    }

    public static class NoticeBean {
        /**
         * id : 594
         * title : 用户公告
         * content : <p>111111</p>
         * create_time : 2019-01-05 16:23:01
         * update_time : 2019-01-05 17:01:01
         * send_status : 2
         * type : 1
         * remark : 这个公告
         * link_type : 1
         * status : 1
         * send_num : 0
         * read_num : 0
         * param :
         * show_img : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpeg/AFWXZNK012345689_1546676598.jpg
         */

        private String id;
        private String title;
        private String content;
        private String create_time;
        private String update_time;
        private String send_status;
        private String type;
        private String remark;
        private String link_type;
        private String status;
        private String send_num;
        private String read_num;
        private String param;
        private String show_img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSend_status() {
            return send_status;
        }

        public void setSend_status(String send_status) {
            this.send_status = send_status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSend_num() {
            return send_num;
        }

        public void setSend_num(String send_num) {
            this.send_num = send_num;
        }

        public String getRead_num() {
            return read_num;
        }

        public void setRead_num(String read_num) {
            this.read_num = read_num;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getShow_img() {
            return show_img;
        }

        public void setShow_img(String show_img) {
            this.show_img = show_img;
        }
    }
}
