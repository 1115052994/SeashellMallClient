package com.liemi.seashellmallclient.data.entity.article;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;

public class MyCommentEntity extends BaseEntity {
    /*
    * "id": "9",
                "user_id": "3419",
                "user_type": "1",
                "inid": "10",
                "content": "沙度飞斧的地方东方饭店",
                "imgs": null,
                "status": "1",
                "create_time": "2020-03-09 13:51:51",//评论时间
                "is_check": "0",
                "to_comment_id": "0",
                "nickname": "4444",//昵称
                "head_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/backend/frontend_15397899666447.png",//头像
                "in": {
                    "id": "10",
                    "shop_id": "0",
                    "title": "从IT到社交电商，中年宝妈的职场危机突围战",
                    "img_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMNTHK034567_1552653670.jpg"/,###文章图片
                    "content": "<blockquote><p style=\"margin-top: 0px; margin-bottom: 0px; font-stretch: normal; font-size: 13px; font-family: "He行业发展报告》数据，2018年中国社交电商市场规模预计达到11397亿元，同比2017年增长66.73%。</span></p><p><br/></p></blockquote>",//文章内容
                    "url": "",
                    "create_time": "2018-12-25 17:11:39",
                    "status": "2",
                    "type": "1",
                    "sort": "0",
                    "is_top": "1",
                    "comment_num": "0",
                    "read_num": "0",
                    "support_num": "0",
                    "collect_num": "0",
                    "thid": "15",
                    "thid_low": null,
                    "is_hot": "1",
                    "remark": "从IT到社交电商，中年宝妈的职场危机突围战",
                    "update_time": "2019-05-28 16:23:05",
                    "param": "/information/information/info?id=10"
                },
                "is_delete": 1
    * */
    private String id;
    private String user_id;
    private String user_type;
    private String inid;
    private String content;
    private String imgs;
    private String status;
    private String create_time;
    private String is_check;
    private String to_comment_id;
    private String nickname;
    private String head_url;
    private In in;
    private int is_delete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getInid() {
        return inid;
    }

    public void setInid(String inid) {
        this.inid = inid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_check() {
        return is_check;
    }

    public void setIs_check(String is_check) {
        this.is_check = is_check;
    }

    public String getTo_comment_id() {
        return to_comment_id;
    }

    public void setTo_comment_id(String to_comment_id) {
        this.to_comment_id = to_comment_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public class In implements Serializable {
        /*
        *  "id": "10",
                    "shop_id": "0",
                    "title": "从IT到社交电商，中年宝妈的职场危机突围战",
                    "img_url": "https://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/AFWXZMNTHK034567_1552653670.jpg"/,###文章图片
                    "content": "<blockquote><p style=\"margin-top: 0px; margin-bottom: 0px; font-stretch: normal; font-size: 13px; font-family: "He行业发展报告》数据，2018年中国社交电商市场规模预计达到11397亿元，同比2017年增长66.73%。</span></p><p><br/></p></blockquote>",//文章内容
                    "url": "",
                    "create_time": "2018-12-25 17:11:39",
                    "status": "2",
                    "type": "1",
                    "sort": "0",
                    "is_top": "1",
                    "comment_num": "0",
                    "read_num": "0",
                    "support_num": "0",
                    "collect_num": "0",
                    "thid": "15",
                    "thid_low": null,
                    "is_hot": "1",
                    "remark": "从IT到社交电商，中年宝妈的职场危机突围战",
                    "update_time": "2019-05-28 16:23:05",
                    "param": "/information/information/info?id=10"
        * */
        private String id;
        private String shop_id;
        private String title;
        private String img_url;
        private String content;
        private String url;
        private String create_time;
        private String status;
        private String type;
        private String sort;
        private String is_top;
        private String comment_num;
        private String read_num;
        private String support_num;
        private String collect_num;
        private String thid;
        private String thid_low;
        private String is_hot;
        private String remark;
        private String update_time;
        private String param;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getRead_num() {
            return read_num;
        }

        public void setRead_num(String read_num) {
            this.read_num = read_num;
        }

        public String getSupport_num() {
            return support_num;
        }

        public void setSupport_num(String support_num) {
            this.support_num = support_num;
        }

        public String getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(String collect_num) {
            this.collect_num = collect_num;
        }

        public String getThid() {
            return thid;
        }

        public void setThid(String thid) {
            this.thid = thid;
        }

        public String getThid_low() {
            return thid_low;
        }

        public void setThid_low(String thid_low) {
            this.thid_low = thid_low;
        }

        public String getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(String is_hot) {
            this.is_hot = is_hot;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }
}
