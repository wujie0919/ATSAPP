package com.dzrcx.jiaan.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/1/13.
 */
public class StatusView extends View {

    public StatusView(Context context) {
        super(context);
        initVisable();
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVisable();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVisable();
    }

    private void initVisable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }
}
