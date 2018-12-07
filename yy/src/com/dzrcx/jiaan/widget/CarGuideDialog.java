package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 车辆操作引导dialog
 * CarGuideDialog carGuideDialog = new CarGuideDialog(getActivity(), YYConstans.CAR_JIANGHUAI);
 * carGuideDialog.show();
 */
public class CarGuideDialog extends Dialog implements View.OnClickListener {
    private ImageView iv_close;
    private RelativeLayout rl_carguidedetail;
    private ViewPager cvp_carguidedetail;
    private TextView tv_next;
    private ArrayList<String> imgs;
    private Context mContext;
    private int i = 0;
    private String Car_tag = "guide";
    private int with;

    public CarGuideDialog(Context context, String car_tag, ArrayList<String> imgs) {
        super(context, R.style.ActionSheet);
        this.mContext = context;
        this.Car_tag = car_tag;
        this.imgs = imgs;
        with = MyUtils.getScreenWidth(mContext) / 10 * 8;
        if (imgs == null || imgs.size() == 0) {
            return;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_carguide);
        setCanceledOnTouchOutside(false);
        rl_carguidedetail = (RelativeLayout) findViewById(R.id.rl_carguidedetail);
        rl_carguidedetail.setLayoutParams(new RelativeLayout.LayoutParams(with, with * 473 / 333));
        iv_close = (ImageView) findViewById(R.id.iv_close);
        cvp_carguidedetail = (ViewPager) findViewById(R.id.cvp_carguidedetail);
        RelativeLayout.LayoutParams cvp_carguidedetailLP = new RelativeLayout.LayoutParams(with - MyUtils.dip2px(mContext, 35), with * 473 / 333 - MyUtils.dip2px(mContext, 35));
        cvp_carguidedetailLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        cvp_carguidedetail.setLayoutParams(cvp_carguidedetailLP);
        cvp_carguidedetail.setAdapter(new CarGuideVPAdp(mContext, imgs));
        cvp_carguidedetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                       @Override
                                                       public void onPageScrolled(int i, float v, int i1) {

                                                       }

                                                       @Override
                                                       public void onPageSelected(int i) {
                                                           if (i == imgs.size() - 1) {
                                                               tv_next.setVisibility(View.VISIBLE);
                                                           } else {
                                                               tv_next.setVisibility(View.GONE);
                                                           }
                                                       }

                                                       @Override
                                                       public void onPageScrollStateChanged(int i) {

                                                       }
                                                   }

        );
        tv_next = (TextView) findViewById(R.id.tv_next);
        RelativeLayout.LayoutParams tv_nextLP = ((RelativeLayout.LayoutParams) tv_next.getLayoutParams());
        tv_nextLP.setMargins(0, 0, 0, (with * 473 / 333) * 90 / 530);
        tv_next.setLayoutParams(tv_nextLP);
        tv_next.setText("完成");
        MyUtils.setViewsOnClick(this, iv_close, tv_next);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_next:
                dismiss();
                break;
        }
    }


    @Override
    public void show() {
        if ("guide".equals(Car_tag)) {
            return;
        }
        if (imgs == null || imgs.size() == 0) {
            return;
        }
        String tag = SharedPreferenceTool.getPrefString(mContext, Car_tag, "");
        if (!tag.isEmpty()) {
            return;
        } else {
            SharedPreferenceTool.setPrefString(mContext, Car_tag, this.Car_tag);
        }
        super.show();
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = with;
        onWindowAttributesChanged(lp);


    }

    private class CarGuideVPAdp extends PagerAdapter {

        private ArrayList<String> imgs;

        public CarGuideVPAdp(Context context, ArrayList<String> imgs) {
            this.imgs = imgs;
        }

        @Override
        public int getCount() {
            return imgs == null ? 0 : imgs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ImageLoader.getInstance().displayImage(imgs.get(position),
                    imageView, YYOptions.Option_CARITEM);
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }
    }
}
