package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.ShareUtils;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 首次租赁车过后会出现的分享按钮
 */
public class ShareDialog2 extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageView IV_close;
    private RelativeLayout RL_iamgelayout, RL_iamge;
    private RelativeLayout upLayout, downLayout;
    private Animation anim;
    private UIImageView uiv_weixin, uiv_weixin_circle;
    private IWXAPI iwxapi;
    private TextView tv_sendmoney;
    private String url;
    private String title;
    private String description;
    private Bitmap bitmap;
    private String transaction;
    private String money;

    private String message;

    public ShareDialog2(Context context) {
        super(context, R.style.AreaDialog);
        this.mContext = context;
        iwxapi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        iwxapi.registerApp(Constants.APP_ID);
    }

    public void setMoney(String money) {
        this.money = money;
        String str = "<font color='#2f3335'>" + "分享给好友得"
                + "</font>" + "<font color='#13bb86'>" + money + "元"
                + "</font>" + "<font color='#2f3335'>" + "现金大奖"
                + "</font>";
        message = str;
        //tv_sendmoney.setText(Html.fromHtml(str));
    }

    public void setData(String url, String title, String description, Bitmap bitmap, String transaction, String money) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
        this.transaction = transaction;
        this.money = money;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_sharetoother);
        setCanceledOnTouchOutside(false);
        IV_close = (ImageView) findViewById(R.id.addialog_close);
        tv_sendmoney = (TextView) findViewById(R.id.tv_sendmoney);
        tv_sendmoney.setText(message);
        RL_iamge = (RelativeLayout) findViewById(R.id.addialog_image);
        RL_iamgelayout = (RelativeLayout) findViewById(R.id.addialog_imagelayout);
        uiv_weixin = (UIImageView) findViewById(R.id.uiv_weixin);
        uiv_weixin_circle = (UIImageView) findViewById(R.id.uiv_weixin_circle);
        upLayout = (RelativeLayout) findViewById(R.id.addialog_uplayout);
        downLayout = (RelativeLayout) findViewById(R.id.addialog_downlayout);
        MyUtils.setViewsOnClick(this, IV_close, uiv_weixin_circle, uiv_weixin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addialog_close:
                outAnimation();
                break;
            case R.id.uiv_weixin_circle:
                ShareUtils.getIntance(mContext).shareToWeiXinCircle(mContext, url, title, description, bitmap, transaction);
                dismiss();
                break;
            case R.id.uiv_weixin:
                ShareUtils.getIntance(mContext).shareToWeiXin(mContext, url, title, description, bitmap, transaction);
                dismiss();
                break;
        }
    }


    @Override
    public void show() {
        super.show();
        Window dlgWindow = getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 8;
        lp.height = MyUtils.getScreenHeigth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
        String str = "<font color='#2f3335'>" + "分享给好友得"
                + "</font>" + "<font color='#13bb86'>" + money + "元"
                + "</font>" + "<font color='#2f3335'>" + "现金大奖"
                + "</font>";
        tv_sendmoney.setText(Html.fromHtml(str));
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
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) RL_iamge.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                RL_iamge.setLayoutParams(layoutParams);
                RL_iamge.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        RL_iamge.startAnimation(anim);
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
                ShareDialog2.this.cancel();
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

}
