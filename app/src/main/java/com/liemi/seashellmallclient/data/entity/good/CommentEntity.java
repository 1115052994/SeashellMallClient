package com.liemi.seashellmallclient.data.entity.good;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/24 15:43
 * 修改备注：
 */
public class CommentEntity extends BaseEntity implements Serializable {
    /**
     * commet_id : 290
     * item_id : 1278
     * value_names : 天空蓝
     * uid : 961
     * level : 4
     * content : 挺好的哈哈哈哈哈哈真的挺好哒哈哈哈哈哈哈 看看噢噢噢噢么么么么哒！你的手机没有流量了
     * flag : 1
     * create_time : 2019-01-09 15:29:44
     * to_commet_id : 0
     * u : {"uid":"961","nickname":"可可🌺@冰淇淋","head_url":"https://wx.qlogo.cn/mmopen/vi_32/yAKoqbO0BqdHsxZA3dRN1via0XTKd5HSEiajatXrz1oKvNsSxdjaqzj0cNUibVEjJv5lFwoPvM6FA7cHG5KRK9SCQ/132","level":"0","sex":"3","sign_name":null,"phone":"13235253625"}
     * meCommetImgs : ["http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/ABFXZMHK01346789_1547018981.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/ABFXMNTHK0124789_1547018981.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/AFWZMNTK01456789_1547018981.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/BWXZNTH023456789_1547018981.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/ABXMNTK012356789_1547018981.jpg","http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/jpg/ABFWXZMTHK246789_1547018981.jpg"]
     * to_commet : null
     */

    private String commet_id;
    private String item_id;
    private String value_names;
    private String uid;
    private String level;
    private String content;
    private String flag;
    private String create_time;
    private ToCommetBean to_commet;
    private UBean u;

    private List<String> meCommetImgs;
    private String total_level;
    private String num_commet;
    private String video_url;

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public ToCommetBean getTo_commet() {
        return to_commet;
    }

    public void setTo_commet(ToCommetBean to_commet) {
        this.to_commet = to_commet;
    }

    public String getNum_commet() {
        return num_commet;
    }

    public void setNum_commet(String num_commet) {
        this.num_commet = num_commet;
    }

    public String getTotal_level() {
        return total_level;
    }

    public void setTotal_level(String total_level) {
        this.total_level = total_level;
    }

    public String getCommet_id() {
        return commet_id;
    }

    public void setCommet_id(String commet_id) {
        this.commet_id = commet_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getValue_names() {
        return value_names;
    }

    public void setValue_names(String value_names) {
        this.value_names = value_names;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public UBean getU() {
        return u;
    }

    public void setU(UBean u) {
        this.u = u;
    }


    public List<String> getMeCommetImgs() {
        return meCommetImgs;
    }

    public void setMeCommetImgs(List<String> meCommetImgs) {
        this.meCommetImgs = meCommetImgs;
    }

    public static class UBean implements Serializable {
        /**
         * uid : 961
         * nickname : 可可🌺@冰淇淋
         * head_url : https://wx.qlogo.cn/mmopen/vi_32/yAKoqbO0BqdHsxZA3dRN1via0XTKd5HSEiajatXrz1oKvNsSxdjaqzj0cNUibVEjJv5lFwoPvM6FA7cHG5KRK9SCQ/132
         * level : 0
         * sex : 3
         * sign_name : null
         * phone : 13235253625
         */

        private String uid;
        private String nickname;
        private String head_url;
        private String level;
        private String sex;
        private Object sign_name;
        private String phone;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getSign_name() {
            return sign_name;
        }

        public void setSign_name(Object sign_name) {
            this.sign_name = sign_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class ToCommetBean implements Serializable {

        private String commet_id;
        private String item_id;
        private String uid;
        private String content;
        private String flag;
        private String create_time;
        private String to_commet_id;
        private List<String> meCommetImgs;

        public String getCommet_id() {
            return commet_id;
        }

        public void setCommet_id(String commet_id) {
            this.commet_id = commet_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTo_commet_id() {
            return to_commet_id;
        }

        public void setTo_commet_id(String to_commet_id) {
            this.to_commet_id = to_commet_id;
        }

        public List<String> getMeCommetImgs() {
            return meCommetImgs;
        }

        public void setMeCommetImgs(List<String> meCommetImgs) {
            this.meCommetImgs = meCommetImgs;
        }
    }
}


