package com.netmi.baselibrary.data.entity;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/3/26
 * 修改备注：
 */
public class AppThemeEntity extends BaseEntity {


    /**
     * shop_name : 猎米社交电商
     * logo_url : https://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/default/ABFWXZMNTK023789_1553245330.png
     * user_bg :
     * vip_bg :
     * color_theme : 0
     * icons : [{"icon":"","icon_act":""},{"icon":"","icon_act":""},{"icon":"","icon_act":""},{"icon":"","icon_act":""},{"icon":"","icon_act":""}]
     */

    private String shop_name;
    private String logo_url;
    private String user_bg;
    private String vip_bg;
    private String vip_bg2;
    private int color_theme;
    private List<IconsBean> icons;
    private int color_price;
    private int color_main;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getUser_bg() {
        return user_bg;
    }

    public void setUser_bg(String user_bg) {
        this.user_bg = user_bg;
    }

    public String getVip_bg() {
        return vip_bg;
    }

    public String getVip_bg2() {
        return vip_bg2;
    }

    public void setVip_bg2(String vip_bg2) {
        this.vip_bg2 = vip_bg2;
    }

    public void setVip_bg(String vip_bg) {
        this.vip_bg = vip_bg;
    }

    public int getColor_theme() {
        return color_theme;
    }

    public void setColor_theme(int color_theme) {
        this.color_theme = color_theme;
    }

    public List<IconsBean> getIcons() {
        return icons;
    }

    public void setIcons(List<IconsBean> icons) {
        this.icons = icons;
    }

    public int getColor_price() {
        return color_price;
    }

    public int getColor_price(int defaultColorRes) {
        if (color_price == 0) {
            return defaultColorRes;
        }
        return color_price;
    }

    public void setColor_price(int color_price) {
        this.color_price = color_price;
    }

    public int getColor_main() {
        return color_main;
    }

    public void setColor_main(int color_main) {
        this.color_main = color_main;
    }

    public static class IconsBean {
        /**
         * icon :
         * icon_act :
         */

        private String icon;
        private String icon_act;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon_act() {
            return icon_act;
        }

        public void setIcon_act(String icon_act) {
            this.icon_act = icon_act;
        }
    }
}
