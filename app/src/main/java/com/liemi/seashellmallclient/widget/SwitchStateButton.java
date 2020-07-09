package com.liemi.seashellmallclient.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.liemi.seashellmallclient.R;

/**
 * 切换状态的Button
 */
public class SwitchStateButton extends View {
    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //可用的宽度和高度
    private float mUsableWidth = 0;
    private float mUsableHeight = 0;

    //指示点的半径
    private float indicateCircleR = 0;

    //记录当前圆心的位置
    private float indicateCircleCenter = 0;

    //默认状态，未选中
    private boolean mCurrentState = true;
    //矩形圆角
    private float mDefaultRectRound = 10;
    //选中背景颜色，默认灰色
    private int mSelectBgColor = Color.GRAY;
    //未选中状态背景颜色，默认灰色
    private int mUnSelectBgColor = Color.GRAY;
    //选中状态指示点的颜色
    private int mSelectPointColor = Color.WHITE;
    //选中状态指示点颜色
    private int mUnSelectPointColor = Color.WHITE;

    //移动状态中的背景颜色
    private int moveBgColor;

    private int movePointColor;

    private ChangeStateListener changeStateListener;

    //设置当前圆心的坐标的get/set方法
    public float getIndicateCircleCenter() {
        return indicateCircleCenter;
    }

    public void setIndicateCircleCenter(float indicateCircleCenter) {
        this.indicateCircleCenter = indicateCircleCenter;
        invalidate();
    }

    public SwitchStateButton(Context context) {
        this(context, null);
    }

    public SwitchStateButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchStateButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchStateButton);
        mSelectBgColor = array.getColor(R.styleable.SwitchStateButton_select_bg_color, Color.GRAY);
        mUnSelectBgColor = array.getColor(R.styleable.SwitchStateButton_un_select_bg_color, Color.GRAY);
        mSelectPointColor = array.getColor(R.styleable.SwitchStateButton_select_indicate_color, Color.WHITE);
        mUnSelectPointColor = array.getColor(R.styleable.SwitchStateButton_unselect_indicate_color, Color.WHITE);
        mCurrentState = array.getBoolean(R.styleable.SwitchStateButton_select_state, true);
        setMoveColor();
        array.recycle();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchStateButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setChangeStateListener(ChangeStateListener changeStateListener) {
        this.changeStateListener = changeStateListener;
    }

    //设置移动状态下的颜色值
    private void setMoveColor() {
        if (mCurrentState) {
            moveBgColor = mSelectBgColor;
            movePointColor = mSelectPointColor;
        } else {
            moveBgColor = mUnSelectBgColor;
            movePointColor = mUnSelectPointColor;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(handleMeasureSize(widthMeasureSpec, 200), handleMeasureSize(heightMeasureSpec, 50));
    }

    //处理View的宽和高
    private int handleMeasureSize(int measureSpec, int defaultSize) {
        int size = defaultSize;
        int specModel = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specModel) {
            case MeasureSpec.EXACTLY:
                //固定大小
                size = specSize;
                break;
            case MeasureSpec.AT_MOST:
                //推荐大小
                size = Math.min(defaultSize, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                size = specSize;
                break;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取可用的宽度和高度
        mUsableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mUsableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        indicateCircleR = mUsableWidth > mUsableHeight ? mUsableHeight / 2 : mUsableWidth / 2;

        if (mCurrentState) {
            if (indicateCircleCenter <= 0) {
                indicateCircleCenter = mUsableWidth - getPaddingRight() - indicateCircleR;
            }
            drawSelectView(canvas);
        } else {
            if (indicateCircleCenter <= 0) {
                indicateCircleCenter = indicateCircleR + getPaddingLeft();
            }
            drawUnSelectView(canvas);
        }
    }

    /**
     * 绘制未选中状态的View
     *
     * @param canvas 画布
     */
    private void drawUnSelectView(Canvas canvas) {
        //绘制圆角矩形
        mPaint.setColor(moveBgColor);
        canvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()),
                indicateCircleR, indicateCircleR, mPaint);

        //绘制圆形
        mPaint.setColor(movePointColor);
        canvas.drawCircle(indicateCircleCenter, getHeight() / 2, indicateCircleR - 5, mPaint);
    }

    //绘制选中状态的View
    private void drawSelectView(Canvas canvas) {
        mPaint.setColor(moveBgColor);
        canvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()),
                indicateCircleR, indicateCircleR, mPaint);

        //绘制圆形
        mPaint.setColor(movePointColor);
        canvas.drawCircle(indicateCircleCenter, getHeight() / 2, indicateCircleR - 5, mPaint);
    }

    //设置选中状态
    private void moveToSelectState() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "indicateCircleCenter", indicateCircleCenter, getWidth() - getPaddingRight() - indicateCircleR);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentState = !mCurrentState;
                if (changeStateListener != null) {
                    changeStateListener.changeState(mCurrentState);
                }
                setMoveColor();
            }
        });
        animator.start();
    }

    //设置未选中状态
    private void moveToUnSelectState() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "indicateCircleCenter", indicateCircleCenter, indicateCircleR + getPaddingLeft());
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentState = !mCurrentState;
                if (changeStateListener != null) {
                    changeStateListener.changeState(mCurrentState);
                }
                setMoveColor();
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;
            case MotionEvent.ACTION_UP:
                if (mCurrentState) {
                    moveToUnSelectState();
                } else {
                    moveToSelectState();
                }

                performClick();
                return true;
        }
        return super.onTouchEvent(event);
    }

    //获取当前的状态
    public boolean getCurrentState() {
        return mCurrentState;
    }

    //设置当前的状态
    public void setmCurrentState(boolean mCurrentState) {
        this.mCurrentState = mCurrentState;
        setMoveColor();
        invalidate();
    }

    public void changeState() {
        if (mCurrentState) {
            moveToUnSelectState();
        } else {
            moveToSelectState();
        }
    }

    //定义一个接口，用于状态改变之后的回调
    public interface ChangeStateListener {
        void changeState(boolean state);
    }
}
