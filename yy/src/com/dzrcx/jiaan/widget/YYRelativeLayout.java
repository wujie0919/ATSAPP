package com.dzrcx.jiaan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.dzrcx.jiaan.R;

/**
 * Created by zhangyu on 16-2-29.
 */
public class YYRelativeLayout extends RelativeLayout {

    private static final float DEFAULT_MAX_RATIO = -1f;
    private static final float DEFAULT_MAX_HEIGHT = -1f;

    private float mMaxHeight = DEFAULT_MAX_HEIGHT;//优先级高
    private float mMaxRatio = DEFAULT_MAX_RATIO;//优先级低

    public YYRelativeLayout(Context context) {
        super(context);
        initMaxHeiht(context, null);
    }

    public YYRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMaxHeiht(context, attrs);
    }

    public YYRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMaxHeiht(context, attrs);
    }

    private void initMaxHeiht(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YYRelativeLayout);

        final int count = a.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.YYRelativeLayout_maxHeightRatio:
                    mMaxRatio = a.getFloat(attr, DEFAULT_MAX_RATIO);
                    break;
                case R.styleable.YYRelativeLayout_maxHeightDimen:
                    mMaxHeight = a.getDimension(attr, DEFAULT_MAX_HEIGHT);
                    break;
            }
        }
        a.recycle();
        if (mMaxHeight < 0) {
            mMaxHeight = (mMaxRatio > 0 ? mMaxRatio : 0) * (float) getScreenHeight(context);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        Log.d("wang", "onMeasure heightSize is " + heightSize + " | mMaxHeight is " + mMaxHeight);

        if (heightMode == View.MeasureSpec.EXACTLY) {
            Log.d("wang", "heightMode == View.MeasureSpec.EXACTLY");
            heightSize = heightSize <= (mMaxHeight <= 0 ? heightSize : mMaxHeight) ? heightSize : (int) mMaxHeight;
        }

        if (heightMode == View.MeasureSpec.UNSPECIFIED) {
            Log.d("wang", "heightMode == View.MeasureSpec.UNSPECIFIED");
            heightSize = heightSize <= (mMaxHeight <= 0 ? heightSize : mMaxHeight) ? heightSize : (int) mMaxHeight;
        }
        if (heightMode == View.MeasureSpec.AT_MOST) {
            Log.d("wang", "heightMode == View.MeasureSpec.AT_MOST");
            heightSize = heightSize <= (mMaxHeight <= 0 ? heightSize : mMaxHeight) ? heightSize : (int) mMaxHeight;
        }

        int maxHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }


    /**
     * 获取屏幕高度
     *
     * @param context
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @param outDimension
     */
    public static void getScreenDimension(Context context, int[] outDimension) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        outDimension[0] = wm.getDefaultDisplay().getWidth();
        outDimension[1] = wm.getDefaultDisplay().getHeight();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        setBackgroundResource(R.drawable.leftmenue_selector);
    }
}
