package com.dzrcx.jiaan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;

/**
 * 画虚线
 * Created by zhangyu on 16-1-4.
 */
class DashedLine extends View {

    float wid = 8;

    public DashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        wid = MyUtils.dip2px(context, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.sp_line));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, 900);
        PathEffect effects = new DashPathEffect(new float[]{wid, wid, wid, wid}, 2);
        paint.setPathEffect(effects);
        canvas.drawCircle(wid, wid, wid, paint);
        canvas.drawPath(path, paint);
    }
}
