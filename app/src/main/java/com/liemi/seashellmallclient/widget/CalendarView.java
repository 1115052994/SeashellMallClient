package com.liemi.seashellmallclient.widget;
/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.liemi.seashellmallclient.R;
import com.liemi.seashellmallclient.utils.DateUtils;
import com.liemi.seashellmallclient.utils.DensityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author airsaid
 * <p>
 * 自定义可多选日历 View.
 */
public class CalendarView extends View {

    /**
     * 默认的日期格式化格式
     */
    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

    /**
     * 默认文字颜色
     */
    private int mTextColor;
    /**
     * 选中后文字颜色
     */
    private int mSelectTextColor;
    /**
     * 默认文字大小
     */
    private float mTextSize;
    /**
     * 选中后文字大小
     */
    private float mSelectTextSize;
    /**
     * 默认天的背景
     */
    private Drawable mDayBackground;

    /**
     * 选中天的背景
     */
    private Drawable mSelectDayBackground;
    /**
     * 日期格式化格式
     */
    private String mDateFormatPattern;
    /**
     * 字体
     */
    private Typeface mTypeface;
    /**
     * 日期状态是否能够改变
     */
    private boolean mIsChangeDateStatus;

    /**
     * 每列宽度
     */
    private int mColumnWidth;
    /**
     * 每行高度
     */
    private int mRowHeight;
    /**
     * 已选择日期数据
     */
    private List<String> mSelectDate;
    /**
     * 选中后的小图片
     */
    private Drawable mSelecterIcon;


    private SimpleDateFormat mDateFormat;
    private Calendar mSelectCalendar;
    private Calendar mCalendar;
    private Paint mPaint;
    private List<Integer> days;


    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSelectCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectDate = new ArrayList<>();
        setClickable(true);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        int textColor = a.getColor(R.styleable.CalendarView_cv_textColor, Color.BLACK);
        setTextColor(textColor);

        int selectTextColor = a.getColor(R.styleable.CalendarView_cv_selectTextColor, Color.BLACK);
        setSelectTextColor(selectTextColor);

        float textSize = a.getDimension(R.styleable.CalendarView_cv_textSize, sp2px(14));
        setTextSize(textSize);

        float selectTextSize = a.getDimension(R.styleable.CalendarView_cv_selectTextSize, sp2px(14));
        setSelectTextSize(selectTextSize);

        Drawable dayBackground = a.getDrawable(R.styleable.CalendarView_cv_dayBackground);
        setDayBackground(dayBackground);

        Drawable selectDayBackground = a.getDrawable(R.styleable.CalendarView_cv_selectDayBackground);
        setSelectDayBackground(selectDayBackground);

        String pattern = a.getString(R.styleable.CalendarView_cv_dateFormatPattern);
        setDateFormatPattern(pattern);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mColumnWidth = getWidth() / 7;
        mRowHeight = getHeight() / 5;
        mPaint.setTextSize(mTextSize);

        int year = mCalendar.get(Calendar.YEAR);
        // 获取的月份要少一月, 所以这里 + 1
        int month = mCalendar.get(Calendar.MONTH) + 1;
        // 获取当月的天数
        int monthDays = DateUtils.getMonthDays(year, month);

        for (int day = 0; day < monthDays; day++) {
            int column = day % 7;
            int row = (day - day % 7) / 7;

            String dayStr = String.valueOf(day+1);
                float textWidth = mPaint.measureText(dayStr);
                int x = (int) (mColumnWidth * column + (mColumnWidth - textWidth) / 2);
                int y = (int) (mRowHeight * row + mRowHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
                int icon_x = (mColumnWidth * column + (mColumnWidth - (DensityUtils.dp2px(10))) / 2);
                int icon_y = (mRowHeight * row + mRowHeight - (DensityUtils.dp2px(10)) );
//                if(mSelectDate == null || mSelectDate.size() == 0 || !mSelectDate.contains(getFormatDate(year, month - 1, day-1))){
//                    // 未选中绘制默认背景和文字颜色
//                    mPaint.setColor(Color.parseColor("#F9F9F9") );
//                    drawBackground(canvas, column, row);
//                    drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
//                }else{
//                    // 否则绘制选择后的背景和文字颜色
//                    mPaint.setColor(Color.parseColor("#BA8D49") );
//                    drawBackground(canvas,  column, row);
//                    drawIcon(canvas,mSelectDayBackground,icon_x,icon_y);
//                    drawText(canvas, dayStr, mSelectTextColor, mTextSize, x, y);
//
//                }
            if(days == null || days.size() == 0 || !days.contains(day)){
                    // 未选中绘制默认背景和文字颜色
                    mPaint.setColor(Color.parseColor("#F9F9F9") );
                    drawBackground(canvas,mDayBackground, column, row);
                    drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
                }else{
                    // 否则绘制选择后的背景和文字颜色
                    mPaint.setColor(Color.parseColor("#BA8D49") );
                    drawBackground(canvas,mSelectDayBackground,  column, row);
//                    drawIcon(canvas,mSelectDayBackground,icon_x,icon_y);
                    drawText(canvas, dayStr, mSelectTextColor, mTextSize, x, y);

                }
            }
        }

    private void drawIcon(Canvas canvas, Drawable background, int icon_x, int icon_y){
        if(background != null){
            canvas.save();
//            int dx = mColumnWidth * column + mColumnWidth-(DensityUtil.dp2px(10))/2;
//            int dy = mRowHeight * column + mRowHeight-(DensityUtil.dp2px(10))/2;
            int dx = icon_x;
            int dy = icon_y;
            canvas.translate(dx, dy);
            background.draw(canvas);
            canvas.restore();
        }
    }



    private void drawBackground(Canvas canvas, Drawable background, int column, int row) {
        canvas.save();
        int dx = (mColumnWidth * column) + (mColumnWidth / 2);
        int dy = (mRowHeight * row) + (mRowHeight / 2);
        int radius=DensityUtils.dp2px(15);
        background.setBounds(dx-radius,dy-radius,dx+radius,dy+radius);
        background.draw(canvas);
        canvas.restore();
    }


    private void drawText(Canvas canvas, String text, @ColorInt int color, float size, int x, int y) {
        mPaint.setColor(color);
        mPaint.setTextSize(size);
        if (mTypeface != null) {
            mPaint.setTypeface(mTypeface);
        }
        canvas.drawText(text, x, y, mPaint);
    }


    /**
     * 获取当前年份.
     *
     * @return year.
     */
    public int getYear() {
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份.
     *
     * @return month. (思考后, 决定这里直接按 Calendar 的 API 进行返回, 不进行 +1 处理)
     */
    public int getMonth() {
        return mCalendar.get(Calendar.MONTH);
    }

    /**
     * 设置文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setTextColor(@ColorInt int textColor) {
        this.mTextColor = textColor;
    }

    /**
     * 设置选中后的的文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setSelectTextColor(@ColorInt int textColor) {
        this.mSelectTextColor = textColor;
    }

    /**
     * 设置文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    /**
     * 设置选中后的的文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setSelectTextSize(float textSize) {
        this.mSelectTextSize = textSize;
    }

    /**
     * 设置天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setDayBackground(Drawable background) {
        if (background != null && mDayBackground != background) {
            this.mDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mDayBackground);
        }
    }

    /**
     * 设置选择后天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setSelectDayBackground(Drawable background) {
        if (background != null && mSelectDayBackground != background) {
            this.mSelectDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mSelectDayBackground);
            invalidate();
        }
    }

    /**
     * 设置日期格式化格式.
     *
     * @param pattern 格式化格式, 如: yyyy-MM-dd.
     */
    public void setDateFormatPattern(String pattern) {
        if (!TextUtils.isEmpty(pattern)) {
            this.mDateFormatPattern = pattern;
        } else {
            this.mDateFormatPattern = DATE_FORMAT_PATTERN;
        }
        this.mDateFormat = new SimpleDateFormat(mDateFormatPattern, Locale.CHINA);
    }



    /**
     * 设置字体.
     *
     * @param typeface {@link Typeface}.
     */
    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        invalidate();
    }



    /**
     * 设置选中的日期数据.
     *
     * @param days 日期数据, 日期格式为 {@link #setDateFormatPattern(String)} 方法所指定,
     * 如果没有设置则以默认的格式 {@link #DATE_FORMAT_PATTERN} 进行格式化.
     */
    public void setSelectDate(List<String> days){
        this.mSelectDate = days;
        invalidate();
    }

    public void setDays(List<Integer> days){
        this.days = days;
        invalidate();
    }



    /**
     * 根据指定的年月日按当前日历的格式格式化后返回.
     *
     * @param year  年.
     * @param month 月.
     * @param day   日.
     * @return 格式化后的日期.
     */
    public String getFormatDate(int year, int month, int day) {
        mSelectCalendar.set(year, month, day);
        return mDateFormat.format(mSelectCalendar.getTime());
    }

    private void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, DensityUtils.dp2px(10), DensityUtils.dp2px(10));
        }
    }

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getContext().getResources().getDisplayMetrics());
    }
}