package com.dzrcx.jiaan.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决上下滑与左右滑动冲突,添加点击事件
 */
public class MapcarViewpager extends ViewPager {
    
    public MapcarViewpager(Context context) {
        super(context);
    }

    public MapcarViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    PointF downPoint = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下时候的坐标
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                if (this.getChildCount() > 1) { // 有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.getChildCount() > 1) { // 有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                // 在up时判断是否按下和松手的坐标为一个点
                if (PointF.length(evt.getX() - downPoint.x, evt.getY()
                        - downPoint.y) < (float) 5.0) {
                    onSingleTouch(this.getCurrentItem());
                    return true;
                }
                break;
        }
        return super.onTouchEvent(evt);
    }

    public void onSingleTouch(int position) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(position);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(int position);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}
