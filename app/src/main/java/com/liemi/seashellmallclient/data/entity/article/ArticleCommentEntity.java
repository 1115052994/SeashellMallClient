package com.liemi.seashellmallclient.data.entity.article;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public class ArticleCommentEntity extends BaseEntity {
    /*
    *  "id": "91",                            ## 评论主键
                "user_id": "924",                    ## 用户主键
                "user_type": "1",                    ## 用户类型
                "inid": "63",                        ## 咨询主键
                "content": "我真心认为网红推手是一份很有爱的事业，能通过自己分享传。。。\n太棒了",            ##评论内容
                "imgs": null,                        ## 评论图片
                "status": "1",
                "create_time": "2018-05-27 18:33:00",                ## 创建时间
                "is_check": "1",                ## 是否审核；0否1是
                "to_comment_id": "0",            ## 大于0时，表示回复评论的主键
                "head_url": "http://thirdwx.qlogo.cn/mmopen/vi_32/rQOn22bNV0lyQ9XZB5ERibqxCWdEv83Itt8aqTpxDE9YfbAO4NFZRvxCLyvdVFWAgkiayuTuAWt1QgV6JarXSmyQ/132",                        ## 评论人头像
                "callback": [                        ## 该评论回复的列表
                    {
                        "id": "90",
                        "user_id": "1708",
                        "user_type": "1",
                        "inid": "63",
                        "content": "特别好。   ",
                        "imgs": null,
                        "status": "1",
                        "create_time": "2018-05-25 22:13:16",
                        "is_check": "0",
                        "to_comment_id": "91",                    ## 大于0时，表示回复评论的主键
                        "is_delete": "1"                ## 是否可以删除 0：否 1：是
                    }
                ]
    *
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
    private String head_url;
    private String nickname;
    private List<U> callback;
    private String good_num;
    private String is_zan;

    public String getGood_num() {
        return good_num;
    }

    public void setGood_num(String good_num) {
        this.good_num = good_num;
    }

    public String getIs_zan() {
        return is_zan;
    }

    public void setIs_zan(String is_zan) {
        this.is_zan = is_zan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public List<U> getCallback() {
        return callback;
    }

    public void setCallback(List<U> callback) {
        this.callback = callback;
    }

    public class U implements Serializable {
        /*
        * "id": "90",
                        "user_id": "1708",
                        "user_type": "1",
                        "inid": "63",
                        "content": "特别好。   ",
                        "imgs": null,
                        "status": "1",
                        "create_time": "2018-05-25 22:13:16",
                        "is_check": "0",
                        "to_comment_id": "91",                    ## 大于0时，表示回复评论的主键
                        "is_delete": "1"
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
        private String is_delete;

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

        public String getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }
    }
}
