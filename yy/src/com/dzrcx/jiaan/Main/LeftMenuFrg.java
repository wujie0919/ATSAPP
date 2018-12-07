package com.dzrcx.jiaan.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ADPhotoListBean;
import com.dzrcx.jiaan.Bean.ADPhotosBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.User.AboutUsActy;
import com.dzrcx.jiaan.User.CompanyAccountAty;
import com.dzrcx.jiaan.User.IdentificationAty;
import com.dzrcx.jiaan.User.IdentificationReqAty;
import com.dzrcx.jiaan.User.LoginAty;
import com.dzrcx.jiaan.User.MessageCentreAty;
import com.dzrcx.jiaan.User.MyAccountAty;
import com.dzrcx.jiaan.User.PersonnalCenterAty;
import com.dzrcx.jiaan.User.TalkComplainAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PhotoUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.StringUtils;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.CustomRoundImageView;
import com.dzrcx.jiaan.yyinterface.DrawerSlideHoldInterface;
import com.dzrcx.jiaan.yyinterface.DrawerSlideInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeftMenuFrg extends YYBaseFragment implements OnClickListener,
        DrawerSlideInterface, RequestInterface {

    private DrawerSlideHoldInterface drawerSlideHoldInterface;

    private LinearLayout leftFrgView, menuLayout;
    private LayoutInflater inflater;
    // private MainActivity2_1 activity2_1;
    int a = 1;

    private CustomRoundImageView userImage;
    private LinearLayout loginLayout;
    private TextView userName, userbalances;
    private LinearLayout userbalanceslayout, ll_topersonal;
    private RelativeLayout layoutAuth, layoutOrder, layoutCall, layoutcoupon, layoutMessages,
            layoutEvaluate, layoutAbout, layoutAccout, layoutrecommend, layoutUserHelp, layoutTweet;
    private ImageView adImage;
    private View userLevel;
    private TextView layoutAccoutLable;
    private int margin = 300;

    private static final int TAG_uploadimg = 169503;
    private static final int TAG_getuserinfo = 169504;
    private static final int TAG_accountmessage = 169505;
    private Dialog updataDialog;
    private Dialog dialog;
    private String takePictureSavePath;
    private Uri imageUri;
    Handler leftHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.inflater = inflater;
        leftFrgView = (LinearLayout) inflater.inflate(R.layout.frg_leftmenu,
                null);
        initView();
        return leftFrgView;
    }

    private void initView() {

        userImage = (CustomRoundImageView) leftFrgView
                .findViewById(R.id.left_userimage);
        ll_topersonal = (LinearLayout) leftFrgView
                .findViewById(R.id.ll_topersonal);
        userName = (TextView) leftFrgView.findViewById(R.id.left_username);
        userLevel = leftFrgView.findViewById(R.id.userLevel);
//      userAuth = (TextView) leftFrgView.findViewById(R.id.left_userauth);
        userbalances = (TextView) leftFrgView.findViewById(R.id.left_userbalances);
        userbalanceslayout = (LinearLayout) leftFrgView.findViewById(R.id.left_userbalanceslayout);

        menuLayout = (LinearLayout) leftFrgView.findViewById(R.id.left_menu_layout);

        layoutAuth = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutauth);
        layoutOrder = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutorder);
        layoutAccout = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutaccount);
        layoutCall = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutcall);
        layoutcoupon = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutcoupon);
        layoutMessages = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutmessages);
//        layoutHelp = (RelativeLayout) leftFrgView
//                .findViewById(R.id.left_layouthelp);
        layoutEvaluate = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutevaluate);
        layoutAbout = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_layoutabout);
//        layoutAccout = (RelativeLayout) leftFrgView
//                .findViewById(R.id.left_layoutcomapnyaccout);
        layoutrecommend = (RelativeLayout) leftFrgView
                .findViewById(R.id.left_recommend);

        layoutUserHelp = (RelativeLayout) leftFrgView.findViewById(R.id.left_layoutuserhelp);
        layoutTweet = (RelativeLayout) leftFrgView.findViewById(R.id.left_layouttweet);

        loginLayout = (LinearLayout) leftFrgView.findViewById(R.id.left_login);
        adImage = (ImageView) leftFrgView.findViewById(R.id.left_adimageview);
        layoutAccoutLable = (TextView) leftFrgView.findViewById(R.id.left_account_lable);


        userImage.setOnClickListener(this);
        // userName.setOnClickListener(this);
        layoutAuth.setOnClickListener(this);
        layoutOrder.setOnClickListener(this);
        layoutAccout.setOnClickListener(this);
        layoutCall.setOnClickListener(this);
        layoutcoupon.setOnClickListener(this);
        layoutMessages.setOnClickListener(this);
//        layoutHelp.setOnClickListener(this);
        layoutEvaluate.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        leftFrgView.setOnClickListener(this);
        layoutAccout.setOnClickListener(this);
        layoutrecommend.setOnClickListener(this);
        layoutUserHelp.setOnClickListener(this);
        layoutTweet.setOnClickListener(this);

        ll_topersonal.setOnClickListener(this);

        margin = (int) (MyUtils.getScreenWidth(mContext) * 0.8);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        updateUserInfo();
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        // activity2_1 = (MainActivity2_1) activity;
    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(
                    menuVisible ? getView().VISIBLE : getView().GONE);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.ll_topersonal:
            case R.id.left_userimage:
                String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                if (TextUtils.isEmpty(skey)) {
                    leftHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            startActivity(LoginAty.class, null);
                        }
                    }, 300);

                } else {
//                    showBottomDialog();
                    leftHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            startActivity(PersonnalCenterAty.class, null);
                        }
                    }, 300);
                }

                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;

            case R.id.left_login:
                String skeys = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                if (skeys.isEmpty()) {
                    leftHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            startActivity(LoginAty.class, null);
                        }
                    }, 300);
                    if (drawerSlideHoldInterface != null) {
                        drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                    }
                }
                break;
            case R.id.left_layoutauth:

                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (TextUtils.isEmpty(skey)) {
                            startActivity(LoginAty.class, null);
                        } else {

                            switch (YYConstans.getUserInfoBean().getReturnContent().getUser()
                                    .getUserState()) {
                                case -1:
                                    startActivity(IdentificationAty.class, null);
                                    break;
                                case 0:
//
                                    startActivity(IdentificationReqAty.class, null);
                                    //showMessageDialog("您提交的证件照片正在审核中，请耐心等待！");
                                    break;
                                case 1:
                                    break;

                                default:
                                    break;
                            }

                        }
                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }

                break;
            case R.id.left_layoutorder:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(MyRunsListAty.class, null);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutaccount:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(MyAccountAty.class, null);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutcoupon:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", "LeftMenuFrg");
                            startActivity(CouponListAty.class, bundle);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutmessages:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", "LeftMenuFrg");
                            startActivity(MessageCentreAty.class, bundle);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutcall:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showCallDialog();
                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
//            case R.id.left_layouthelp:
//                leftHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        startActivity(new Intent(mContext, WebAty.class)
//                                .putExtra("title", "租车指南").putExtra("url",
//                                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=help"));
//                        mContext.overridePendingTransition(
//                                R.anim.activity_up, R.anim.activity_push_no_anim);
//                    }
//                }, 300);
//                if (drawerSlideHoldInterface != null) {
//                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
//                }
//                break;
            case R.id.left_recommend:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(RecommendAty.class, null);
                        }
                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutevaluate:

                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Uri uri = Uri.parse("market://details?id="
                                + mContext.getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        List<ResolveInfo> localList = mContext.getPackageManager()
                                .queryIntentActivities(intent,
                                        PackageManager.GET_INTENT_FILTERS);

                        if ((localList != null) && (localList.size() > 0)) {
                            startActivity(intent);
                            mContext.overridePendingTransition(
                                    R.anim.activity_up,
                                    R.anim.activity_push_no_anim);
                        } else {

                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }

                break;
            case R.id.left_layoutuserhelp:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(HelpCenterAty.class, null);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layouttweet:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(TalkComplainAty.class, null);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutabout:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        startActivity(AboutUsActy.class, null);
                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            case R.id.left_layoutcomapnyaccout:

                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
                        if (skey.isEmpty()) {
                            startActivity(LoginAty.class, null);
                        } else {
                            startActivity(CompanyAccountAty.class, null);
                        }

                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }

                break;
            case R.id.left_adimageview:
                leftHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        Uri uri = Uri.parse(((ADPhotosBean) adImage.getTag()).getUrl());
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
                        Intent webIntent = null;
                        webIntent = new Intent(mContext,
                                WebAty.class);
                        webIntent.putExtra("title", "" + ((ADPhotosBean) adImage.getTag()).getName());
                        webIntent.putExtra("url", ((ADPhotosBean) adImage.getTag()).getUrl());
                        mContext.startActivity(webIntent);
                        getActivity().overridePendingTransition(R.anim.activity_up,
                                R.anim.activity_push_no_anim);
                    }
                }, 300);
                if (drawerSlideHoldInterface != null) {
                    drawerSlideHoldInterface.onChangeDrawerLayoutStatus();
                }
                break;
            default:
                break;
        }

    }

    public void setDrawerSlideHoldInterface(
            DrawerSlideHoldInterface drawerSlideHoldInterface) {
        this.drawerSlideHoldInterface = drawerSlideHoldInterface;
    }

    @Override
    public void onDrawerSlide(float slideOffset) {
        // TODO Auto-generated method stub
        View tempView;
        LayoutParams layoutParams;
        int tempMargin;
        int childCount = menuLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            tempView = null;
            tempView = menuLayout.getChildAt(i);
            if (tempView != null) {
                layoutParams = (LayoutParams) tempView.getLayoutParams();
                tempMargin = (int) (((1 - slideOffset) * margin) * (i * 0.2));
                layoutParams.setMargins(0, 0, tempMargin, 0);
                tempView.setLayoutParams(layoutParams);
            }
        }
        tempView = null;
        tempView = leftFrgView.getChildAt(leftFrgView.getChildCount() - 1);
        if (tempView != null) {
            layoutParams = (LayoutParams) tempView.getLayoutParams();
            tempMargin = (int) (((1 - slideOffset) * margin) * (childCount * 0.2));
            layoutParams.setMargins(0, 0, tempMargin, 0);
            tempView.setLayoutParams(layoutParams);
        }


    }

    @Override
    public void onDrawerShow() {
        // TODO Auto-generated method stub
        getAccountMessge();
        updateUserInfo();
    }

    private void updateUserInfo() {

        String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
        if (skey.isEmpty()) {
            userName.setText(null);
            userLevel.setBackgroundResource(0);
//            userAuth.setVisibility(View.GONE);
            userbalanceslayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    "", userImage, YYOptions.Option_USERPHOTO);
        } else {
            loginLayout.setVisibility(View.GONE);
//            userbalanceslayout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb(),
                    userImage, YYOptions.Option_USERPHOTO);
            userName.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getName() == null ? "未设置"
                    : StringUtils.formatPhone(YYConstans.getUserInfoBean()
                    .getReturnContent().getUser().getName()));
            switch (YYConstans.getUserInfoBean().getReturnContent().getUser().getLevel()) {
//                case 0:
//                    userLevel.setBackgroundResource(0);
//                    break;
//                case 1:
//                    userLevel.setBackgroundResource(R.drawable.icon_userstate_2);
//                    break;
//                case 2:
//                    userLevel.setBackgroundResource(R.drawable.icon_userstate_1);
//                    break;
//                case 3:
//                    userLevel.setBackgroundResource(R.drawable.icon_userstate_3);
//                    break;
                case 10:
                    userLevel.setBackgroundResource(R.drawable.vip);
                    userLevel.setVisibility(View.VISIBLE);
                    break;
                default:
                    userLevel.setBackgroundResource(0);
                    userLevel.setVisibility(View.GONE);
                    break;
            }
            userbalances.setText("账户余额 ：" + YYConstans.getUserInfoBean().getReturnContent().getUser().getBalance() + "元");

            switch (YYConstans.getUserInfoBean().getReturnContent().getUser().getUserState()) {
                case -1://未审核
//                    userAuth.setVisibility(View.VISIBLE);
                    // userAuth.setText("快速认证");
                    userbalances.setText("未认证");
                    layoutAuth.setVisibility(View.VISIBLE);
                    break;
                case 0://审核中
//                    userAuth.setVisibility(View.GONE);
                    layoutAuth.setVisibility(View.VISIBLE);
                    userbalances.setText("认证中");
                    // userAuth.setText("资料认证中");
                    // userAuth.setClickable(false);
                    break;
                case 1://审核通过
//                    userAuth.setVisibility(View.GONE);
                    layoutAuth.setVisibility(View.GONE);
                    break;

                default:

                    break;
            }
        }
        showLeftAD();
    }

    /**
     * 展示广告
     */
    private void showLeftAD() {
        String ads = SharedPreferenceTool.getPrefString(mContext,
                SharedPreferenceTool.KEY_ADS_LEFT, "");
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
                        adImage.setVisibility(View.VISIBLE);
//                        mHandler.sendEmptyMessageDelayed(998, 1000);
                        adImage.setTag(adPhotoListBean.getPhotos().get(i));
                        adImage.setOnClickListener(this);

                        ImageLoader.getInstance().displayImage(
                                adPhotoListBean.getPhotos().get(i)
                                        .getPhotoUrl(), adImage,
                                new ImageLoadingListener() {

                                    @Override
                                    public void onLoadingStarted(
                                            String imageUri, View view) {
                                        // TODO Auto-generated method stub
//                                        mAnimation = AnimationUtils
//                                                .loadAnimation(
//                                                        mContext,
//                                                        R.anim.ad_anim);
                                    }

                                    @Override
                                    public void onLoadingFailed(
                                            String imageUri, View view,
                                            FailReason failReason) {
                                        // TODO Auto-generated method stub
                                        adImage
                                                .setImageResource(R.drawable.icon_ali);
                                    }

                                    @Override
                                    public void onLoadingComplete(
                                            String imageUri, final View view,
                                            Bitmap loadedImage) {
                                        // TODO Auto-generated method stub

                                        adImage
                                                .setImageDrawable(null);
                                        adImage
                                                .setBackgroundDrawable(null);
                                        final BitmapDrawable bbb = new BitmapDrawable(
                                                loadedImage);
                                        adImage
                                                .setBackgroundDrawable(bbb);
//                                        mAnimation.setFillAfter(true);
//
//                                        mAnimation
//                                                .setAnimationListener(new Animation.AnimationListener() {
//
//                                                    @Override
//                                                    public void onAnimationStart(
//                                                            Animation animation) {
//                                                        // TODO Auto-generated
//                                                        // method stub
//                                                        ((ImageView) adImage)
//                                                                .setBackgroundDrawable(bbb);
//                                                        // iv_mainfirst
//                                                        // .setVisibility(View.VISIBLE);
//
//                                                    }
//
//                                                    @Override
//                                                    public void onAnimationRepeat(
//                                                            Animation animation) {
//                                                        // TODO Auto-generated
//                                                        // method stub
//
//                                                    }
//
//                                                    @Override
//                                                    public void onAnimationEnd(
//                                                            Animation animation) {
//                                                        // TODO Auto-generated
//                                                        // method stub
//                                                        adImage
//                                                                .clearAnimation();
//                                                    }
//                                                });
//                                        adImage.startAnimation(mAnimation);

                                    }

                                    @Override
                                    public void onLoadingCancelled(
                                            String imageUri, View view) {
                                        // TODO Auto-generated method stub
                                        adImage
                                                .setImageResource(R.drawable.icon_ali);
                                    }
                                });
                        isAD = true;
                        break a;
                    }

                }
                if (!isAD) {
                    adImage
                            .setImageDrawable(null);
                    adImage.setVisibility(View.GONE);
                }
            }


        }
    }

    public void showBottomDialog() {
        if (dialog == null) {
            dialog = new Dialog(mContext, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_useraty, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            TextView tv_camera_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_camera_txt);
            TextView tv_album_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_album_txt);
            TextView tv_exit_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_exit_txt);
            TextView cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.cancel_txt);

            tv_camera_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        showNoNetDlg();
                        MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                        return;
                    }
                    String saveRootPath = YYApplication.sdCardRootPath;
                    if (!TextUtils.isEmpty(saveRootPath)) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        String timeStamp = new SimpleDateFormat(
                                "yyyyMMdd_HHmmss").format(new Date());
                        File dirFil = new File(saveRootPath
                                + "/yiyi/image/imageCach/");
                        if (!dirFil.exists()) {
                            dirFil.mkdirs();
                        }
                        File makeFile = new File(saveRootPath
                                + "/yiyi/image/imageCach/", "xiaoma_"
                                + timeStamp + ".jpeg");
                        takePictureSavePath = makeFile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(makeFile));
                        LeftMenuFrg.this.startActivityForResult(intent, 2);
                    } else {
                        MyUtils.showToast(mContext, "！存储设备部不可用");
                    }
                    dialog.dismiss();
                }
            });
            tv_album_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetHelper.checkNetwork(mContext)) {
                        showNoNetDlg();
                        MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    LeftMenuFrg.this.startActivityForResult(intent, 1);
                    dialog.dismiss();

                }
            });
            tv_exit_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SharedPreferenceTool.setPrefString(mContext,
                            SharedPreferenceTool.KEY_USERINFO, "");
                    ImageLoader.getInstance().displayImage("", userImage,
                            YYOptions.Option_USERPHOTO);
                    YYConstans.setUserInfoBean(null);
                    // UserActivity.this.onResume();
                    dialog.dismiss();
                    updateUserInfo();
                }
            });
            cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            Window w = dialog.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            final int cMakeBottom = -1000;
            lp.y = cMakeBottom;
            lp.gravity = Gravity.BOTTOM;
            dialog.onWindowAttributesChanged(lp);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(mDlgCallView);
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case 1:
                    if (data != null) {
                        imageUri = data.getData();
                        if (Build.VERSION.SDK_INT >= 19) {
                            imageUri = Uri.fromFile(new File(PhotoUtil.getPath(
                                    mContext, imageUri)));
                        }
                        imageUri = startPhotoZoom(imageUri);
                    }

                    break;
                // 如果是调用相机拍照时
                case 2:
                    if (takePictureSavePath != null) {
                        imageUri = Uri.fromFile(new File(takePictureSavePath));
                        imageUri = startPhotoZoom(imageUri);
                    }
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        if (bitmap != null) {
                            userImage.setImageBitmap(bitmap);
                            uploadImg(bitmap);
                        }
                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void uploadImg(Bitmap bitmap) {
        dialogShow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final byte bytes[] = baos.toByteArray();
        AjaxParams params = new AjaxParams();
        params.put("file", new ByteArrayInputStream(bytes));
        params.put("businessobj", "userFacePhoto_"
                + YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId());
        params.put("appid", "195");
        params.put("filename", "businessobj");
        params.put("mimeType", "image/jpeg");
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                : YYConstans.getUserInfoBean().getReturnContent().getSkey());
//        YYRunner.uploadImg(TAG_uploadimg, YYUrl.GETUPLOADIMG, params, this);
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        switch (tag) {
            case TAG_uploadimg:
                json2Upload(json);
                break;
            case TAG_getuserinfo:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
                }
                break;
            case TAG_accountmessage:


                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean != null && baseResBean.getErrno() == 0) {
                    layoutAccoutLable.setText(baseResBean.getError());
                }

                break;
            default:
                break;
        }

    }

    private void upUser(UserInfoBean userInfoBean) {
        updateUserInfo();

    }

    @SuppressLint("NewApi")
    private void json2Upload(String json) {
        if (!json.isEmpty())
            try {
                JSONObject jsonObject = new JSONObject(json);
                String stateMsg = jsonObject.getString("stateMsg");
                String stateCode = jsonObject.getString("stateCode");
                if (!"0".equals(stateCode)) {
                    MyUtils.showToast(mContext,
                            jsonObject.has("stateMsg") ? stateMsg : "服务器错误");
                } else {
                    JSONObject resourceInfo = jsonObject
                            .getJSONObject("resourceInfo");

                    String fileUrl = resourceInfo.getString("fileUrl");

                    YYConstans.getUserInfoBean().getReturnContent().getUser().setThumb(fileUrl);

                    ImageLoader.getInstance().displayImage(fileUrl, userImage,
                            YYOptions.Option_USERPHOTO);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    /**
     * 头像裁剪 图片方法实现
     *
     * @param uriFrom
     */
    public Uri startPhotoZoom(Uri uriFrom) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
        return uriFrom;

    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }


    private void getAccountMessge() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "sidebarDoc");
        YYRunner.getData(TAG_accountmessage, YYRunner.Method_POST, YYUrl.GETDOCUMENT, map,
                LeftMenuFrg.this);
    }


}
