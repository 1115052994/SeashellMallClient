package com.netmi.baselibrary.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:49
 * 修改备注：
 */
public class DateUtil {


    static SimpleDateFormat format;

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式：yyyy.MM.dd
     **/
    public static final String DF_YYYY_MM_DD_P = "yyyy.MM.dd";
    /**
     * 日期格式：MM-dd
     **/
    public static final String DF_MM_DD = "MM-dd";
    /**
     * 日期格式：yyyy-MM
     **/
    public static final String DF_YYYY_MM = "yyyy-MM";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";

    /**
     * 日期格式：yyyy年MM月dd日
     **/
    public static final String DF_YYYY_MM_DD_CHINA = "yyyy年MM月dd日";

    public static final String DF_MM_DD_HH_CHINA = "MM月dd日 HH点";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    public final static long DAY = 24 * hour;// 1天
    private final static long month = 31 * DAY;// 月
    private final static long YEAR = 12 * month;// 年

    /**
     * Log输出标识
     **/
    private static final String TAG = DateUtil.class.getSimpleName();

    public DateUtil() {

    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param formater 日期
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(Date date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(date);
    }

    /**
     * 将string转date
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date strToDate(String strDate, String formater) {
        SimpleDateFormat formatter = new SimpleDateFormat(formater);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将2017-10-10转2017年10月10日
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String strToChinaDate(String strDate) {
        if (TextUtils.isEmpty(strDate))
            return "";
        return formatDateTime(strToDate(strDate, DF_YYYY_MM_DD), DF_YYYY_MM_DD_CHINA);
    }

    /**
     * 将2017-10-10 10:10:10转2017-10-10
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String strToMMDDDate(String strDate) {
        if (TextUtils.isEmpty(strDate))
            return "";
        return formatDateTime(strToDate(strDate, DF_YYYY_MM_DD_HH_MM_SS), DF_YYYY_MM_DD);
    }

    /**
     * 将2017-10-10 10:10:10转2017.10.10
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String strToMMDDPointDate(String strDate) {
        if (TextUtils.isEmpty(strDate))
            return "";
        try {
            return formatDateTime(new Date(strToLong(strDate)), DF_YYYY_MM_DD_P);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将2017-10-10 10:10:10转2017-10-10 10:10
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String strToMMDDHHMMDate(String strDate) {
        if (TextUtils.isEmpty(strDate))
            return "";
        return formatDateTime(strToDate(strDate, DF_YYYY_MM_DD_HH_MM_SS), DF_YYYY_MM_DD_HH_MM);
    }

    /**
     * 将string转long时间戳
     *
     * @param strDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long strToLong(String strDate) {
        if (TextUtils.isEmpty(strDate)) return 0;

        SimpleDateFormat formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        if (strtodate == null) {
            formatter = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM);
            strtodate = formatter.parse(strDate, pos);
        }
        if (strtodate == null) {
            formatter = new SimpleDateFormat(DF_YYYY_MM_DD);
            strtodate = formatter.parse(strDate, pos);
        }
        if (strtodate == null) {
            formatter = new SimpleDateFormat(DF_YYYY_MM_DD_P);
            strtodate = formatter.parse(strDate, pos);
        }
        if (strtodate == null) {
            formatter = new SimpleDateFormat(DF_YYYY_MM);
            strtodate = formatter.parse(strDate, pos);
        }
        if (strtodate == null)
            return 0;
        return strtodate.getTime();
    }

    /**
     * 将YYYY_MM_DD_HH_MM_SS转聊天时间
     *
     * @param strDate
     * @return
     */
    public static String getChatTime(String strDate) {
        if (TextUtils.isEmpty(strDate))
            return "";
        return getNewChatTime(strToLong(strDate));
    }

    /**
     * 时间戳格式转换
     */
    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getNewChatTime(long timesamp) {
        String result = "";
        if (timesamp <= 0)
            return result;
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }
        timeFormat = "M月d日 " + am_pm + "HH:mm";
        yearTimeFormat = "yyyy年M月d日 ";//+ am_pm +"HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getHourAndMin(timesamp);
                            } else {
                                result = getTime(timesamp, timeFormat);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }

    /**
     * 当前时间 如: 2013-04-22 10:37:00
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 当前时间 如: 2013-04-22 10:37:00
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate2() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(System.currentTimeMillis()));
    }

    //获取当前(上，下)周的日期范围如：...,-1上一周，0本周，1下一周...
    public static String getWeekBeginDate(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // getTimeInterval(sdf);

        Calendar calendar1 = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);

        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayOfWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 获得当前日期是一个星期的第几天
        int day = calendar1.get(Calendar.DAY_OF_WEEK);

        //获取当前日期前（下）x周同星几的日期
        calendar1.add(Calendar.DATE, 7 * i);

        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);


        String beginDate = sdf.format(calendar1.getTime());
        calendar1.add(Calendar.DATE, 6);

        String endDate = sdf.format(calendar1.getTime());

        System.out.println(beginDate + " 到 " + endDate);
        return beginDate;
    }
}
