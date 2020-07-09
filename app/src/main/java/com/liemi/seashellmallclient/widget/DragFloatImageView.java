package com.liemi.seashellmallclient.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import com.liemi.seashellmallclient.utils.Densitys;
import com.netmi.baselibrary.utils.Logs;

public class DragFloatImageView extends android.support.v7.widget.AppCompatImageView {
    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;  //此处包含标题栏的高度
    private int lastX;
    private int lastY;

    private boolean isDrag;

    public DragFloatImageView(Context context) {
        this(context,null);
    }

    public DragFloatImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragFloatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        screenWidth= Densitys.getScreenWidth(getContext());
        screenWidthHalf=screenWidth/2;
        screenHeight= Densitys.getScreenHeight(getContext())-Densitys.dp2px(getContext(),100);
        statusHeight=Densitys.getStatusHeight(getContext())+Densitys.dp2px(getContext(),94);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        setTop(screenHeight-DensityUtils.dip2px(getContext(),100)-getHeight());
//        setLeft(screenWidth-DensityUtils.dip2px(getContext(),6)-getWidth());
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag=false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX=rawX;
                lastY=rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag=true;
                //计算手指移动了多少
                int dx=rawX-lastX;
                int dy=rawY-lastY;
                //这里修复一些华为手机无法触发点击事件的问题
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                if(distance==0){
                    isDrag=false;
                    break;
                }
                float x=getX()+dx;
                float y=getY()+dy;
                //检测是否到达边缘 左上右下
                x=x<0?0:x>screenWidth-getWidth()?screenWidth-getWidth():x;
                y=y<statusHeight?statusHeight:y+getHeight()>screenHeight?screenHeight-getHeight():y;
                setX(x);
                setY(y);
                lastX=rawX;
                lastY=rawY;
                //Log.i("getX="+getX()+";getY="+getY()+";screenHeight="+screenHeight);
                break;
            case MotionEvent.ACTION_UP:
                if(isDrag){
                    //恢复按压效果
                    setPressed(false);
                    Logs.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
                    if(rawX>=screenWidthHalf){
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(screenWidth-getWidth()-getX())
                                .start();
                    }else {
                        ObjectAnimator oa= ObjectAnimator.ofFloat(this,"x",getX(),0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                }
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }



}
