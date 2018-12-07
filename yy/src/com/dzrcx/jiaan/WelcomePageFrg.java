package com.dzrcx.jiaan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;


public class WelcomePageFrg extends YYBaseFragment {
    private View welcomView;
    private ViewPager mViewPager;
    private LinearLayout linPointGroup;
    private ImageView[] pointArry;
    private ImageView[] imgs;
    private int[] imgBgIDs;
    private int[] imgTagIDs;
    private GuideVPAdp adapter;
    private int padding;
    Intent intent = null;
    //    private TextView tv_nowstart;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (welcomView == null) {
            welcomView = inflater.inflate(R.layout.aty_welcomepager, null);
            initViews();
        }
        intent = getActivity().getIntent();
        return welcomView;
    }

    private void initViews() {
        padding = MyUtils.dip2px(mContext, 10);
//        tv_nowstart = (TextView) welcomView.findViewById(R.id.tv_nowstart);
        mViewPager = (ViewPager) welcomView.findViewById(R.id.vp_welcome);
        linPointGroup = (LinearLayout) welcomView.findViewById(R.id.ll_point);
        imgBgIDs = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
//        imgTagIDs = new int[]{R.drawable.tag_chun, R.drawable.tag_jie, R.drawable.tag_kuai,
//                R.drawable.tag_le};
//        initPointGroup();
        initImgs();
        adapter = new GuideVPAdp(mContext, imgs);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pointSetChange(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void pointSetChange(int position) {

//        for (int i = 0; i < pointArry.length; i++) {
//            if (i == position) {
//               /* pointArry[i].setPadding(5, 5, 5, 5);春节快乐时候使用
//                pointArry[i].setImageResource(imgTagIDs[i]);*/
//                pointArry[i].setPadding(padding, padding, padding, padding);
//                pointArry[i].setImageResource(R.drawable.point_pre);
//            } else {
//                pointArry[i].setPadding(padding, padding, padding, padding);
//                pointArry[i].setImageResource(R.drawable.point_n);
//            }
//        }
//        tv_nowstart.setVisibility(View.GONE);
        if (position == imgs.length - 1) {
            imgs[imgs.length - 1].setVisibility(View.VISIBLE);
            imgs[imgs.length - 1].setOnClickListener(new View.OnClickListener() {
                //            imgs[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (intent.getIntExtra("type", 0)) {
                        case 0:
                            intent.setClass(mContext, MainActivity2_1.class);
                            getActivity().startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.activity_up,
                                    R.anim.activity_push_no_anim);

                            mHandler.sendEmptyMessageDelayed(999, 1500);
                            break;
                        case 1:
                            intent.setClass(mContext, WebAty.class);
                            getActivity().startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.activity_up,
                                    R.anim.activity_push_no_anim);

                            mHandler.sendEmptyMessageDelayed(999, 1500);
                            break;
                        case -1://待定
//                            intent.setClass(mContext, OrderAty.class);
//                            getActivity().startActivity(intent);
//                            getActivity().overridePendingTransition(R.anim.activity_up,
//                                    R.anim.activity_push_no_anim);
//
//                            mHandler.sendEmptyMessageDelayed(999, 1500);
//                            break;
                        intent.setClass(mContext, MainActivity2_1.class);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);

                        mHandler.sendEmptyMessageDelayed(999, 1500);
                        break;
                    }
                }
            });
        }
    }

    private void initImgs() {
        imgs = new ImageView[imgBgIDs.length];
        for (int i = 0; i < imgs.length; i++) {
            ImageView img = new ImageView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setScaleType(ScaleType.FIT_XY);
            img.setImageResource(imgBgIDs[i]);
            imgs[i] = img;
        }
    }

    private void initPointGroup() {
        pointArry = new ImageView[imgBgIDs.length];
        for (int i = 0; i < pointArry.length; i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams paramsPoint = new LinearLayout.LayoutParams(MyUtils.dip2px(mContext, 28), MyUtils.dip2px(mContext, 28));
            point.setLayoutParams(paramsPoint);
            point.setScaleType(ScaleType.CENTER_INSIDE);
            pointArry[i] = point;
            if (i == 0) {
               /* pointArry[i].setPadding(5, 5, 5, 5);//节假日轮换字图标
                pointArry[i].setImageResource(imgTagIDs[0]);*/
                pointArry[i].setPadding(padding, padding, padding, padding);
                pointArry[i].setImageResource(R.drawable.point_pre);
            } else {
                pointArry[i].setPadding(padding, padding, padding, padding);
                pointArry[i].setImageResource(R.drawable.point_n);
            }
            linPointGroup.addView(pointArry[i]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imgs != null && imgs.length > 0) {
            for (int i = 0; i < imgs.length; i++) {
                Drawable drawable = imgs[i].getDrawable();
                if (drawable != null) {
                    imgs[i].setImageDrawable(null);
                    drawable.setCallback(null);
                }
                Drawable drawableb = imgs[i].getBackground();
                if (drawableb != null) {
                    drawableb.setCallback(null);
                }
            }
        }
        System.gc();

    }
}
