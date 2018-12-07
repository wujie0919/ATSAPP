package com.dzrcx.jiaan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ADPhotoListBean;
import com.dzrcx.jiaan.Bean.ADPhotosBean;
import com.dzrcx.jiaan.Bean.BrandHelpMsgBean;
import com.dzrcx.jiaan.Bean.SysConfigBean;
import com.dzrcx.jiaan.Bean.UndoOrderBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.CustomRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.Map;

public class StartPageFrg extends YYBaseFragment implements
        RequestInterface, OnClickListener {

    private LinearLayout timeLayout;
    private TextView tv_tiem, tv_tonext, tv_name;
    private CustomRoundImageView cv_userimage;
    private ImageView adsImageView;
    private UndoOrderBean undoOrderBean;
    private Animation mAnimation;
    public final int MSG_FINISH_STARTPAGEACTIVITY = 0x11;
    private boolean hasClick = false;
    private int time = 5;
    // 1.0.5
    private View startPageView;
    private Activity context;
    private boolean needToWelcomAt = false;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_STARTPAGEACTIVITY:
                    // 跳转到MainActivity，并结束当前的StartPageActivity
                    toNextScreen(0, null);
                    break;
                case 998:
                    time--;
                    if (time > 0) {
                        tv_tiem.setText(time + "");
                        mHandler.sendEmptyMessageDelayed(998, 1000);
                    } else {
                        mHandler.removeMessages(998);
                        toNextScreen(0, null);
                    }
                    break;
                case 999:
                    context.finish();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        // YYApplication.mLocationClient.stop();
        // YYApplication.setLocationIFC(null);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (startPageView == null) {
            startPageView = inflater.inflate(R.layout.startpage_activity, null);
            // 设置为全屏状态
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            timeLayout = (LinearLayout) startPageView.findViewById(R.id.startpage_timelayout);
            tv_name = (TextView) startPageView.findViewById(R.id.tv_name);
            cv_userimage = (CustomRoundImageView) startPageView.findViewById(R.id.cv_userimage);
            if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                tv_name.setText("欢迎您");
                cv_userimage.setImageResource(R.drawable.start_userphoto_nor);
            } else {
                tv_name.setText("欢迎您  " + YYConstans.getUserInfoBean().getReturnContent().getUser().getName());
                ImageLoader.getInstance().displayImage(YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb(),
                        cv_userimage, YYOptions.Option_StartPageUserPhoto);
            }
            tv_tiem = (TextView) startPageView.findViewById(R.id.startpage_time);
            tv_tonext = (TextView) startPageView.findViewById(R.id.startpage_tonext);
            adsImageView = (ImageView) startPageView.findViewById(R.id.startpage_ad);
            cv_userimage.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.VISIBLE);
            showAndGetAD();
            YYApplication.mLocationClient.start();
            Map<String, String> sysparams = new HashMap<String, String>();
            YYRunner.getData(1010, YYRunner.Method_POST, YYUrl.GETSYSCONFIG,
                    sysparams, this);

            // 先查询是否有正在执行的订单
            if (!(TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey()))) {

                Map<String, String> wxparams = new HashMap<String, String>();
                wxparams.put("skey",
                        YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                                : YYConstans.getUserInfoBean().getReturnContent().getSkey());
                wxparams.put("lng", YYApplication.Longitude + "");
                wxparams.put("lat", YYApplication.Latitude + "");
                YYRunner.getData(1009, YYRunner.Method_POST, YYUrl.GETUSERINFO,//获取用户信息
                        wxparams, this);

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                String xgtoken = SharedPreferenceTool.getPrefString(mContext,
                        SharedPreferenceTool.KEY_XGTOKEN, "");
                map1.put("phoneId", xgtoken + "");
                map1.put("mobile", YYConstans.getUserInfoBean().getReturnContent().getUser()
                        .getMobile());
                map1.put("phoneType", "2");
                map1.put("scene", "login");
                YYRunner.getData(1099, YYRunner.Method_POST, YYUrl.ADDPHONEUSE,//获取手机信息用户信息
                        map1, this);

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                        YYRunner.getData(1001, YYRunner.Method_POST,
                                YYUrl.GETUNDOORDEDETAIL, params,
                                StartPageFrg.this);//获取正在执行中的订单
                    }
                }, 1500);

            } else {
                // 停留3秒后发送消息，跳转到MainActivity
                mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY, 3500);
            }
            tv_tonext.setOnClickListener(this);
        }
        judgeoWelcomeAty();
        getBrandHelpMsg();
        return startPageView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_FINISH_STARTPAGEACTIVITY);
        if (adsImageView != null && adsImageView.getDrawable() != null) {
            Bitmap oldBitmap = ((BitmapDrawable) adsImageView.getDrawable()).getBitmap();
            adsImageView.setImageDrawable(null);
            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY, 5000);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.startpage_tonext:
                toNextScreen(0, null);//跳过
                break;
            case R.id.startpage_ad:
                ADPhotosBean adPhotosBean = (ADPhotosBean) v.getTag();
                toNextScreen(1, adPhotosBean);//点击全屏图片

                break;
            default:
                break;
        }

    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        switch (tag) {
            case 1001://获取正在执行中的订单
                undoOrderBean = (UndoOrderBean) GsonTransformUtil.fromJson(json,
                        UndoOrderBean.class);
                if (undoOrderBean != null && undoOrderBean.getErrno() == 0
                        && undoOrderBean.getReturnContent() != null
                        && undoOrderBean.getReturnContent().getFeeDetail() != null) {

                    mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY,
                            5500);
                } else {
                    undoOrderBean = null;
                    mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY,
                            5500);
                }

                break;
            case 1002:
                ADPhotoListBean adPhotoListBean = (ADPhotoListBean) GsonTransformUtil
                        .fromJson(json, ADPhotoListBean.class);

                if (adPhotoListBean != null && adPhotoListBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(mContext,
                            SharedPreferenceTool.KEY_ADS, json);
                    for (int i = 0; i < adPhotoListBean.getPhotos().size(); i++) {
                        // 缓存图片
                        ImageLoader.getInstance().loadImage(
                                adPhotoListBean.getPhotos().get(i).getPhotoUrl(),
                                null);
                    }
                }

                break;
            case 10022:
                //左侧菜单页小广告，提前获取
                ADPhotoListBean mainadPhotoListBean = (ADPhotoListBean) GsonTransformUtil
                        .fromJson(json, ADPhotoListBean.class);

                if (mainadPhotoListBean != null && mainadPhotoListBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(mContext,
                            SharedPreferenceTool.KEY_ADS_MAIN, json);
                    for (int i = 0; i < mainadPhotoListBean.getPhotos().size(); i++) {
                        // 缓存图片
                        ImageLoader.getInstance().loadImage(
                                mainadPhotoListBean.getPhotos().get(i).getPhotoUrl(),
                                null);
                    }
                }
                break;

            case 10023:
                //左侧菜单页小广告，提前获取
                ADPhotoListBean leftadPhotoListBean = (ADPhotoListBean) GsonTransformUtil
                        .fromJson(json, ADPhotoListBean.class);

                if (leftadPhotoListBean != null && leftadPhotoListBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(mContext,
                            SharedPreferenceTool.KEY_ADS_LEFT, json);
                    for (int i = 0; i < leftadPhotoListBean.getPhotos().size(); i++) {
                        // 缓存图片
                        ImageLoader.getInstance().loadImage(
                                leftadPhotoListBean.getPhotos().get(i).getPhotoUrl(),
                                null);
                    }
                }

                break;
            case 1009:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
                }
                break;

            case 1010:
                SysConfigBean sysConfigBean = (SysConfigBean) GsonTransformUtil
                        .fromJson(json, SysConfigBean.class);
                if (sysConfigBean != null && sysConfigBean.getErrno() == 0) {
                    YYConstans.setSysConfig(sysConfigBean);
                }
                break;
            case 2001:
                BrandHelpMsgBean helpMsgBean = (BrandHelpMsgBean) GsonTransformUtil
                        .fromJson(json, BrandHelpMsgBean.class);
                if (helpMsgBean != null && helpMsgBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_BRANDHELPMSG, json);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

    /**
     * 判断是否需要跳转到引导页
     */
    private void judgeoWelcomeAty() {

        int version = SharedPreferenceTool.getPrefInt(mContext, SharedPreferenceTool.KEY_VERSION, 20);
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
            int currentVersion = info.versionCode;
            if (currentVersion > version) {
                SharedPreferenceTool.setPrefInt(mContext, SharedPreferenceTool.KEY_VERSION, currentVersion);
                SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_USEDCARMODE, "");
                needToWelcomAt = true;
            } else {
                needToWelcomAt = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public synchronized void toNextScreen(int type, ADPhotosBean adPhotosBean) {
        mHandler.removeMessages(998);
        mHandler.removeMessages(MSG_FINISH_STARTPAGEACTIVITY);//获取正在执行中的订单
        if (!hasClick) {
            hasClick = true;
            switch (type) {
                case 0://跳过
                    mHandler.removeMessages(MSG_FINISH_STARTPAGEACTIVITY);
                    if (undoOrderBean == null) {
                        Intent intent = null;
                        if (needToWelcomAt) {
                            intent = new Intent(mContext,
                                    WelcomePageAty.class);
                        } else {
                            intent = new Intent(mContext,
                                    MainActivity2_1.class);
                        }
                        intent.putExtra("type", type);
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);

                        mHandler.sendEmptyMessageDelayed(999, 1500);
                    } else {
                        Intent intent = null;
                        if (needToWelcomAt) {
                            intent = new Intent(mContext,
                                    WelcomePageAty.class);
                        } else {//待定
                            intent = new Intent(mContext,
                                    MainActivity2_1.class);
//                            intent = new Intent(mContext,
//                                    OrderAty.class);
                        }
                        intent.putExtra("type", -1);
                        intent.putExtra("UndoOrderBean", undoOrderBean);
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);
                        mHandler.sendEmptyMessageDelayed(999, 1500);
                    }
                    break;
                case 1://点击全屏图片
                    if (adPhotosBean != null
                            && !TextUtils.isEmpty(adPhotosBean.getUrl())) {
                        mHandler.removeMessages(MSG_FINISH_STARTPAGEACTIVITY);
                        Intent webIntent = null;
                        if (needToWelcomAt) {
                            webIntent = new Intent(mContext,
                                    WelcomePageAty.class);
                        } else {
                            webIntent = new Intent(mContext,
                                    WebAty.class);
                        }
                        webIntent.putExtra("type", type);
                        webIntent.putExtra("title", adPhotosBean.getName());
                        webIntent.putExtra("url", adPhotosBean.getUrl());
                        webIntent.putExtra("isStartPate", true);
                        webIntent.putExtra("UndoOrderBean", undoOrderBean);
                        context.startActivity(webIntent);
                        context.overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);
                        mHandler.sendEmptyMessageDelayed(999, 1500);
                    } else {
                        hasClick = false;
                    }
                    break;

                default:
                    hasClick = false;
                    break;
            }

        }
    }

    private void showAndGetAD() {
        String ads = SharedPreferenceTool.getPrefString(mContext,
                SharedPreferenceTool.KEY_ADS, "");
        if (!TextUtils.isEmpty(ads)) {

            ADPhotoListBean adPhotoListBean = (ADPhotoListBean) GsonTransformUtil
                    .fromJson(ads, ADPhotoListBean.class);

            if (adPhotoListBean != null && adPhotoListBean.getErrno() == 0) {
                //
                boolean isAD = false;
                a:
                for (int i = 0; i < adPhotoListBean.getPhotos().size(); i++) {

                    long tempTime = System.currentTimeMillis();

                    if (tempTime > adPhotoListBean.getPhotos().get(i)
                            .getStartTime()
                            && tempTime < adPhotoListBean.getPhotos().get(i)
                            .getEndTime()) {
                        timeLayout.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(998, 1000);
                        adsImageView.setTag(adPhotoListBean.getPhotos().get(i));
                        adsImageView.setOnClickListener(this);

                        ImageLoader.getInstance().displayImage(
                                adPhotoListBean.getPhotos().get(i)
                                        .getPhotoUrl(), adsImageView,
                                new ImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(
                                            String imageUri, View view) {
                                        // TODO Auto-generated method stub
                                        mAnimation = AnimationUtils
                                                .loadAnimation(
                                                        mContext,
                                                        R.anim.ad_anim);
                                    }

                                    @Override
                                    public void onLoadingFailed(
                                            String imageUri, View view,
                                            FailReason failReason) {
                                        // TODO Auto-generated method stub
                                        adsImageView
                                                .setImageResource(R.drawable.layout_startpage_background);
                                        cv_userimage.setVisibility(View.VISIBLE);
                                        tv_name.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onLoadingComplete(
                                            String imageUri, final View view,
                                            Bitmap loadedImage) {
                                        // TODO Auto-generated method stub


                                        tv_name.setVisibility(View.GONE);
                                        cv_userimage.setVisibility(View.GONE);

                                        adsImageView
                                                .setImageDrawable(null);
                                        adsImageView
                                                .setBackgroundDrawable(null);
                                        final BitmapDrawable bbb = new BitmapDrawable(
                                                loadedImage);
                                        adsImageView
                                                .setBackgroundDrawable(bbb);
                                        cv_userimage.setVisibility(View.GONE);
                                        tv_name.setVisibility(View.GONE);
                                        mAnimation.setFillAfter(true);

                                        mAnimation
                                                .setAnimationListener(new AnimationListener() {

                                                    @Override
                                                    public void onAnimationStart(
                                                            Animation animation) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        adsImageView
                                                                .setBackgroundDrawable(bbb);
                                                        cv_userimage.setVisibility(View.GONE);
                                                        tv_name.setVisibility(View.GONE);
                                                        // iv_mainfirst
                                                        // .setVisibility(View.VISIBLE);

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(
                                                            Animation animation) {
                                                        // TODO Auto-generated
                                                        // method stub

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(
                                                            Animation animation) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        adsImageView
                                                                .clearAnimation();
                                                    }
                                                });
                                        adsImageView.startAnimation(mAnimation);

                                    }

                                    @Override
                                    public void onLoadingCancelled(
                                            String imageUri, View view) {
                                        // TODO Auto-generated method stub
                                        cv_userimage.setVisibility(View.VISIBLE);
                                        tv_name.setVisibility(View.VISIBLE);
                                        adsImageView
                                                .setImageResource(R.drawable.layout_startpage_background);
                                    }
                                });
                        isAD = true;
                        break a;
                    }

                }
                if (!isAD) {
                    adsImageView
                            .setImageResource(R.drawable.layout_startpage_background);
                    cv_userimage.setVisibility(View.VISIBLE);
                    tv_name.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY, 3000);
                }
            } else {
                cv_userimage.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                adsImageView
                        .setImageResource(R.drawable.layout_startpage_background);
                mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY, 3000);
            }

        } else {
            cv_userimage.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_FINISH_STARTPAGEACTIVITY);
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_STARTPAGEACTIVITY, 3000);
            adsImageView
                    .setImageResource(R.drawable.layout_startpage_background);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", 2 + "");
        params.put("size", 1 + "");
        YYRunner.getData(1002, YYRunner.Method_POST, YYUrl.GETADPHOTOS, params,
                StartPageFrg.this);

        Map<String, String> adparams23 = new HashMap<String, String>();
        adparams23.put("type", 2 + "");
        adparams23.put("size", 1 + "");
        adparams23.put("adType", 3 + "");
        YYRunner.getData(10023, YYRunner.Method_POST, YYUrl.GETADPHOTOS, adparams23,
                StartPageFrg.this);

        Map<String, String> adparams22 = new HashMap<String, String>();
        adparams22.put("type", 2 + "");
        adparams22.put("size", 1 + "");
        adparams22.put("adType", 2 + "");
        YYRunner.getData(10022, YYRunner.Method_POST, YYUrl.GETADPHOTOS, adparams22,
                StartPageFrg.this);

    }


    private void getBrandHelpMsg() {
        Map<String, String> getBrandHelp = new HashMap<String, String>();
        YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.GETBRANDHELPMSG, getBrandHelp,
                StartPageFrg.this);
    }


}