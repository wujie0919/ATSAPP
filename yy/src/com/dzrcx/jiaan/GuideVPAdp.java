package com.dzrcx.jiaan;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideVPAdp extends PagerAdapter {
    private ImageView[] imgs;

    public GuideVPAdp(Context context, ImageView[] imgs) {
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imgs[position]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imgs[position], 0);
        return imgs[position];
    }
}
