package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dzrcx.jiaan.Bean.ADPhotosBean;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zhangyu on 16-3-1.
 * 主页显示的广告弹窗
 */
public class ADDialog extends Dialog implements View.OnClickListener {


    private Context mContext;
    private ImageView IV_close, IV_iamge;
    private RelativeLayout RL_iamge;

    private RelativeLayout upLayout, downLayout;

    private Animation anim;

    private ADPhotosBean adPhotosBean;


    public ADDialog(Context context) {
        super(context, R.style.AreaDialog);
        this.mContext = context;
    }

    public ADDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public ADDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addialog);

        setCanceledOnTouchOutside(false);
        IV_close = (ImageView) findViewById(R.id.addialog_close);
        IV_iamge = (ImageView) findViewById(R.id.addialog_image);
        RL_iamge = (RelativeLayout) findViewById(R.id.addialog_imagelayout);

        upLayout = (RelativeLayout) findViewById(R.id.addialog_uplayout);
        downLayout = (RelativeLayout) findViewById(R.id.addialog_downlayout);

        IV_close.setOnClickListener(this);
        IV_iamge.setOnClickListener(this);

        if (adPhotosBean != null) {
            ImageLoader.getInstance().displayImage(adPhotosBean.getPhotoUrl(), IV_iamge);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addialog_close:
//                mHandler.sendEmptyMessageDelayed(999, 5);
                outAnimation();
                break;
            case R.id.addialog_image:

                if (adPhotosBean != null
                        && !TextUtils.isEmpty(adPhotosBean.getUrl())) {
                    Intent webIntent = null;
                    webIntent = new Intent(mContext,
                            WebAty.class);
                    webIntent.putExtra("title", "" + adPhotosBean.getName());
                    webIntent.putExtra("url", adPhotosBean.getUrl());
                    mContext.startActivity(webIntent);
                    this.getOwnerActivity().overridePendingTransition(R.anim.activity_up,
                            R.anim.activity_push_no_anim);
                    outAnimation();
                }


                break;

        }
    }


    @Override
    public void show() {
        super.show();
        inAnimation();
    }

    /**
     * 广告弹出
     */
    private void inAnimation() {
        anim = AnimationUtils.loadAnimation(mContext, R.anim.ad_anim_in);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) IV_iamge.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                IV_iamge.setLayoutParams(layoutParams);
                IV_iamge.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        IV_iamge.startAnimation(anim);
    }


    /**
     * 广告退出
     */
    private void outAnimation() {
        anim = AnimationUtils.loadAnimation(mContext, R.anim.ad_anim_in_sec);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ADDialog.this.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation animout = AnimationUtils.loadAnimation(mContext, R.anim.ad_anim_out);
        animout.setDuration(1000);
        animout.setFillAfter(true);
        downLayout.startAnimation(anim);
        upLayout.startAnimation(animout);
    }

    public ADPhotosBean getAdPhotosBean() {
        return adPhotosBean;
    }

    public void setAdPhotosBean(ADPhotosBean adPhotosBean) {
        this.adPhotosBean = adPhotosBean;
    }
}
