package com.netmi.baselibrary.utils;

import android.text.TextUtils;

import com.netmi.baselibrary.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 9:53
 * 修改备注：
 */
public class FloatUtils {

    private static final double WAN = 10000;

    private static DecimalFormat format;

    /**
     * 格式化小数点后八位
     *
     * @param source     需要格式化的字符串
     * @param ignoreZero 对0的处理，小数点后不够八位的处理，如果为true，就有几位取几位，如果为false，就使用0代替
     * @return
     */
    public static String eightDecimal(String source, boolean ignoreZero) {
        if (TextUtils.isEmpty(source)) {
            if (ignoreZero) {
                return "0";
            }
            return "0.00000000";
        }
        try {
            DecimalFormat format = new DecimalFormat();
            BigDecimal bigDecimal = new BigDecimal(source);
            if (ignoreZero) {
                format.applyPattern("#.########");
            } else {
                format.applyPattern("0.00000000");
            }
            return format.format(bigDecimal);
        } catch (NumberFormatException ex) {
            if (ignoreZero) {
                return "0";
            }
            return "0.00000000";
        }
    }

    public static String twoDecimal(String source, boolean ignoreZero) {
        if (TextUtils.isEmpty(source)) {
            if (ignoreZero) {
                return "0";
            }
            return "0.00";
        }
        return formatDouble(source).replace(ignoreZero ? ".00" : "", "");
    }


    /**
     * 单位为万
     */
    public static String toBigUnit(String num) {
        if (!TextUtils.isEmpty(num) && Strings.toDouble(num) >= WAN) {
            double number = Strings.toDouble(num) / WAN;
            String unit = ResourceUtil.getString(R.string.baselib_ten_thousand);
            if (number >= WAN) {
                number = number / WAN;
                unit = ResourceUtil.getString(R.string.baselib_one_hundred_million);
            }
            String result = String.valueOf(number);
            //保留一位小数
            return (result.contains(".") ? result.substring(0, result.indexOf(".") + 2) : result) + unit;
        }
        return num;
    }

    /**
     * Float 数据保留两位小数
     *
     * @param source
     * @return float
     */
    public static float twoDecimalFloat(float source) {
        return (float) (Math.round(source * 100)) / 100;
    }

    /**
     * Float 数据保留两位小数
     */
    public static String twoDecimal(float source) {
        return formatDouble(String.valueOf(source));
    }

    /**
     * Double 数据保留2位小数
     */
    public static String formatDouble(double source) {
        if (format == null) {
            format = new DecimalFormat("###0.00");
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        return format.format(source);
    }

    public static String formatDouble(String source) {
        return formatDouble(Strings.toDouble(source));
    }

    public static String formatMoney(double source) {
        return "¥" + formatDouble(source);
    }

    public static String formatMoney(String source) {
        return "¥" + formatDouble(source);
    }

    //String转Float
    public static float string2Float(String value){
        if(Strings.isEmpty(value)){
            return 0;
        }
        try{
            return Float.parseFloat(value);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public static int floatToInt(float f){
        int i = 0;
        if(f>0) //正数
            i = (int) ((f*10 + 5)/10);
        else if(f<0) //负数
            i = (int) ((f*10 - 5)/10);
        else i = 0;
        return i;

    }

}
