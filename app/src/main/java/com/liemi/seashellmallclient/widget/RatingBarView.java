package com.liemi.seashellmallclient.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.liemi.seashellmallclient.R;
import com.netmi.baselibrary.utils.Logs;

import java.math.BigDecimal;

/**
 * Created by Bingo on 2018/3/29.
 */

public class RatingBarView extends LinearLayout {

    public interface OnRatingListener {
        void onRating(Object bindObject, int RatingScore);
    }

    private boolean mClickable = true;
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;
    private int paddingLeft;
    private int paddingRight;
    private int starCount;
    private float starLevel;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable starHalfDrawable;
    private int mStarCount;

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        starImageSize = ta.getDimension(R.styleable.RatingBarView_starImageSize, 20);
        starCount = ta.getInteger(R.styleable.RatingBarView_starCount, 5);
        starLevel = ta.getFloat(R.styleable.RatingBarView_starLevel, 0.0f);
        starEmptyDrawable = ta.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = ta.getDrawable(R.styleable.RatingBarView_starFill);
        starHalfDrawable = ta.getDrawable(R.styleable.RatingBarView_starHalf);
        paddingLeft = (int) ta.getDimension(R.styleable.RatingBarView_padding_left, 0);
        paddingRight = (int) ta.getDimension(R.styleable.RatingBarView_padding_right, 0);
        mClickable = ta.getBoolean(R.styleable.RatingBarView_editable, true);
        ta.recycle();

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, attrs);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable) {
                        mStarCount = indexOfChild(v) + 1;
                        setStar(mStarCount, true);
                        if (onRatingListener != null) {
                            Logs.i("点击的星星："+mStarCount);
                            onRatingListener.onRating(bindObject, mStarCount);
                        }
                    }
                }
            });
            addView(imageView);
        }
        setStar(starLevel, false);
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs) {
        ImageView imageView = new ImageView(context);
        LayoutParams para = new LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        para.rightMargin = paddingRight;
        para.leftMargin = paddingLeft;
        imageView.setLayoutParams(para);
        // TODO:you can change gap between two stars use the padding
//        imageView.setPadding(0, 0, 40, 0);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        return imageView;
    }

    public void setStar(int starCount, boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            if (animation) {
                ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 1);
                getChildAt(i).startAnimation(sa);
            }
        }
        for (int i = this.starCount - 1; i >= starCount; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    public void setStar(float level, boolean animation) {

        // 1.判断评分是否大于星星等级
        level = (int)level > this.starCount ? this.starCount : level;
        //2.判断评分是否大于0
        level = (int)level <= 0 ? 0 : level;
        for (int i = 0; i < (int)level; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            if (animation) {
                ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 1);
                getChildAt(i).startAnimation(sa);
            }
        }
        //3.判断评分小数是否==0
        int fint = (int) level;
        BigDecimal b1 = new BigDecimal(Float.toString(level));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();
        if (fPoint==0){
            for (int i = this.starCount - 1; i >= (int)level; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }else {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);
            for (int i = this.starCount - 1; i > (int)level; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }

    }

    public void setStarShop(float level, boolean animation) {

        // 1.判断评分是否大于星星等级
        level = (int)level > this.starCount ? this.starCount : level;
        //2.判断评分是否大于0
        level = (int)level <= 0 ? 0 : level;
        int childCount = getChildCount();
        for (int i = 0; i < (int)level; ++i) {
            ((ImageView) getChildAt(childCount-1-i)).setImageDrawable(starFillDrawable);
            if (animation) {
                ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 1);
                getChildAt(i).startAnimation(sa);
            }
        }
        //3.判断评分小数是否==0
        int fint = (int) level;
        BigDecimal b1 = new BigDecimal(Float.toString(level));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();
        if (fPoint==0){
            for (int i = this.starCount - 1; i >= (int)level; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }else {
            ((ImageView) getChildAt(childCount-1-fint)).setImageDrawable(starHalfDrawable);
            for (int i = this.starCount - 1; i > (int)level; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }
        }

    }

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }

    public int getStarCount() {
        return mStarCount;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = startCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    /**
     * 这个回调，可以获取到用户评价给出的星星等级
     */
    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }
}
