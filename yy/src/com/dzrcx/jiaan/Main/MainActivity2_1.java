package com.dzrcx.jiaan.Main;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzrcx.jiaan.Bean.ADPhotoListBean;
import com.dzrcx.jiaan.Bean.ADPhotosBean;
import com.dzrcx.jiaan.Bean.BrandHelpMsgBean;
import com.dzrcx.jiaan.Bean.BrandHelpMsgVO;
import com.dzrcx.jiaan.Bean.MapcarfrgStationListReContentVo;
import com.dzrcx.jiaan.Bean.SysConfigBean;
import com.dzrcx.jiaan.Bean.UndoOrderBean;
import com.dzrcx.jiaan.Bean.UpdataBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Constans.YYBroadCastName;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.Order.PayActivity;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.R.layout;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.ApkDownLoad;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.ADDialog;
import com.dzrcx.jiaan.yyinterface.DrawerSlideHoldInterface;
import com.dzrcx.jiaan.yyinterface.DrawerSlideInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import pub.devrel.easypermissions.AppSettingsDialog;
//import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity2_1 extends YYBaseActivity implements
        RequestInterface, DrawerSlideHoldInterface,
        OnClickListener
//       , EasyPermissions.PermissionCallbacks
{
    private Context mContext;
    private long exitTime = 0;
    private LayoutInflater inflater;
    private DrawerLayout drawerLayout;
    private FrameLayout fl_content_draw, fl_left_draw;
    private MapCarFrg mapCarFrg;
    private LeftMenuFrg leftMenuFrg;
    private CarListFrg carListFrg;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerSlideInterface drawerSlideInterface;
    private MapcarfrgStationListReContentVo mapcarfrgStationListReContentVo;
    private Dialog updataDialog;
    private View updataView;
    private TextView tv_content, tv_updata_txt, tv_cancel_txt;
    private Dialog messageDlg, locFailDlg, locationDlg;

    private LoginSuccessBroadCast loginSuccessBroadCast;

    private ADDialog adDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ((YYApplication) getApplication()).addActivity(this);
        mContext = this;
        setContentView(R.layout.main_activity2_1);
        initView();
        initXGPush();
        if (!NetHelper.checkNetwork(MainActivity2_1.this)) {
            YYRunner.getData(YYConstans.TAG_GETUPDATA, YYRunner.Method_POST,
                    YYUrl.GETUPDATA, new HashMap<String, String>(), this);
        }
        getUserinfo();
        setDownOut(false);
        loginSuccessBroadCast = new LoginSuccessBroadCast(this, this);
        IntentFilter loginIntentFilter = new IntentFilter();
        loginIntentFilter.addAction(YYBroadCastName.BC_LoginSuccess);
        registerReceiver(loginSuccessBroadCast, loginIntentFilter);

        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBrandHelpMsg();
            }
        }, 350);

        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showADDialog();
            }
        }, 800);
//        refresh();//检测2好站是否有车辆

        //检查权限
//        checkAndRequestPermissions();
//        gpsStatusReceiver = new GpsStatusReceiver();
//        registGpsListenter();
    }


    public boolean isTopActivity() {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains("MainActivity2_1")) {
            isTop = true;
        }
        return isTop;
    }

    /**
     * 改变到某一个fragment
     * 在fragment界面需要添加setMenuVisibility方法
     *
     * @param index 0=leftmenu;1=mapCarFrg;2=CarListFrg;
     */
    public void changeToFragment(int index) {
        FrameLayout frameLayout = fl_content_draw;
        if (index == 0) {
            frameLayout = fl_left_draw;
        }
        Fragment fragment = (Fragment) fragments.instantiateItem(frameLayout,
                index);
        fragments.setPrimaryItem(frameLayout, 0, fragment);
        fragments.finishUpdate(frameLayout);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapCarFrg.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        SharedPreferenceTool.setPrefInt(YYApplication.getApplication().getApplicationContext(), "versoincode", YYApplication.versionCode);
        getUserinfo();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mapCarFrg != null && mapCarFrg.isLl_bottomIsVisible()) {//防止跳进当前界面请求站点列表回来时候在地图界面被拦截传递
                    mapCarFrg.initCreateStatu();
                }
                mapCarFrg.requestLocationData(false);
            }
        }, 500);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void getUserinfo() {
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());

        YYRunner.getData(1009, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, this);

        Map<String, String> sysparams = new HashMap<String, String>();
        sysparams.put("lng", YYApplication.Longitude + "");
        sysparams.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(1010, YYRunner.Method_POST, YYUrl.GETSYSCONFIG,
                sysparams, this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mapCarFrg.fragmentOnRestart();
//    }

    private void initView() {
        // TODO Auto-generated method stub
//        YYApplication.setLocationIFC(this);
        inflater = LayoutInflater.from(this);
        updataView = inflater.inflate(layout.dlg_updata, null);
        tv_content = (TextView) updataView.findViewById(R.id.tv_content);
        tv_updata_txt = (TextView) updataView.findViewById(R.id.tv_updata_txt);
        tv_cancel_txt = (TextView) updataView.findViewById(R.id.tv_cancel_txt);
        MyUtils.setViewsOnClick(this, tv_content, tv_updata_txt, tv_cancel_txt);
        fl_left_draw = (FrameLayout) findViewById(R.id.fl_left_draw);
        fl_content_draw = (FrameLayout) findViewById(R.id.fl_content_draw);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// 允许手势侧滑
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.personal, R.string.alert_cancel,
                R.string.alert_cancel) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                if (drawerSlideInterface != null) {
                    drawerSlideInterface.onDrawerShow();
                }
                getUserinfo();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // TODO Auto-generated method stub
                if (drawerSlideInterface != null) {
                    drawerSlideInterface.onDrawerSlide(slideOffset);
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // TODO Auto-generated method stub
                super.onDrawerStateChanged(newState);
            }

        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        changeToFragment(0);
        changeToFragment(1);
    }

    /**
     * 设置左边侧滑菜单是否可滑出
     *
     * @param status ：-1不可测出，并且处于处于关闭状态；0表示可以侧滑
     */
    public void setDrawerLayoutStatus(int status) {
        switch (status) {
            case -1:
                drawerLayout
                        .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 允许手势侧滑
                break;

            default:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);// 允许手势侧滑
                break;
        }

    }

    /**
     * 打开侧滑
     */
    public void changeDrawerLayoutStatus() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }

    }

    private FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(
            getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (leftMenuFrg == null) {
                        leftMenuFrg = new LeftMenuFrg();
                        setDrawerSlideInterface(leftMenuFrg);
                        leftMenuFrg
                                .setDrawerSlideHoldInterface(MainActivity2_1.this);
                    }
                    return leftMenuFrg;
                case 1:
                    if (mapCarFrg == null) {
                        mapCarFrg = new MapCarFrg();
                    }
                    return mapCarFrg;
                case 2:
                    if (carListFrg == null) {
                        carListFrg = new CarListFrg();
                        carListFrg.setPageInterface(mapCarFrg);
                    }

                    return carListFrg;
            }
            return null;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 将ActionBar上的图标与Drawer结合起来
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 需要将ActionDrawerToggle与DrawerLayout的状态同步
        // 将ActionBarDrawerToggle中的drawer图标，设置为ActionBar中的Home-Button的Icon
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public DrawerSlideInterface getDrawerSlideInterface() {
        return drawerSlideInterface;
    }

    public void setDrawerSlideInterface(
            DrawerSlideInterface drawerSlideInterface) {
        this.drawerSlideInterface = drawerSlideInterface;
    }

    private void initXGPush() {
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
        // XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        Context context = getApplicationContext();
        XGPushManager.registerPush(context, "TEST", new XGIOperateCallback() {

            @Override
            public void onSuccess(Object arg0, int arg1) {
                // TODO Auto-generated method stub
                SharedPreferenceTool.setPrefString(MainActivity2_1.this,
                        SharedPreferenceTool.KEY_XGTOKEN, arg0.toString());
            }

            @Override
            public void onFail(Object arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub
                System.err.println("xgpush register faluse");
            }
        });
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        dismmisDialog();
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        switch (tag) {
            case YYConstans.TAG_GETUPDATA:
                UpdataBean bean = (UpdataBean) GsonTransformUtil.fromJson(json,
                        UpdataBean.class);
                if (bean != null && bean.getVersion() > MyUtils.getVersionCode(this)) {
                    showUpdataDialog(bean);
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
            case 1001:
//                是否有正在进行的订单
                UndoOrderBean undoOrderBean = (UndoOrderBean) GsonTransformUtil.fromJson(json,
                        UndoOrderBean.class);
                if (undoOrderBean != null && undoOrderBean.getErrno() == 0
                        && undoOrderBean.getReturnContent() != null
                        && undoOrderBean.getReturnContent().getFeeDetail() != null) {
//                    YYConstans.hasUnFinishOrder = true;
//                    Intent toIntent = new Intent(this, OrderAty.class);
//                    toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(toIntent);
//                    overridePendingTransition(R.anim.activity_up,
//                            R.anim.activity_push_no_anim);
//                    finish();

                } else {

                }

                if (undoOrderBean.getReturnContent().getTimeMode() == 1 && undoOrderBean.getReturnContent().getInCharge() == 0) {//日租并且没有进入到分时
                    //编写进行
                    switch (undoOrderBean.getReturnContent().getDailyState()) {
                        case 1:
                            Intent toOrder = new Intent(mContext, DayShareActivity.class);
                            toOrder.putExtra("orderId", undoOrderBean.getReturnContent().getOrderId());
                            toOrder.putExtra("PushDialogActivity", true);//是续租
                            toOrder.putExtra("isFromOrderList", true);//是待支付
                            mContext.startActivity(toOrder);
                            overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 2:
                            YYConstans.hasUnFinishOrder = true;
                            Intent toOrder1 = new Intent(mContext, OrderAty.class);
                            toOrder1.putExtra("orderId", undoOrderBean.getReturnContent().getOrderId());
                            mContext.startActivity(toOrder1);
                            overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                    }
                } else {//时租状态 或者 日租进入分时
                    switch (undoOrderBean.getReturnContent().getOrderState()) {
                        case 1://等待取车
                        case 2://正在用车
                            YYConstans.hasUnFinishOrder = true;
                            Intent toOrder1 = new Intent(mContext, OrderAty.class);
                            toOrder1.putExtra("orderId", undoOrderBean.getReturnContent().getOrderId());
                            mContext.startActivity(toOrder1);
                            overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 3:
                            Bundle bundle3 = new Bundle();
                            // 个人账号
                            bundle3.putString("orderId",
                                    undoOrderBean.getReturnContent().getOrderId() + "");
                            bundle3.putString("totalprice", undoOrderBean.getReturnContent().getFeeDetail().getTotalPrice() + "");
                            bundle3.putString("allPrice", undoOrderBean.getReturnContent().getFeeDetail().getAllPrice() + "");
                            bundle3.putString("benefitPrice", undoOrderBean.getReturnContent().getFeeDetail().getBenefitPrice() + "");
                            bundle3.putString("payPrice", undoOrderBean.getReturnContent().getFeeDetail().getPayPrice() + "");
                            //startActivityForResult(PayActivity.class, bundle3, 1001);

                            Intent toIntent = new Intent(mContext, PayActivity.class);
                            if (bundle3 != null) {
                                toIntent.putExtras(bundle3);
                            }
                            toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivityForResult(toIntent, 1001);
                            overridePendingTransition(R.anim.activity_up,
                                    R.anim.activity_push_no_anim);
                            break;

                    }
                }
                break;
            case 2001:
                BrandHelpMsgBean helpMsgBean = (BrandHelpMsgBean) GsonTransformUtil
                        .fromJson(json, BrandHelpMsgBean.class);
                if (helpMsgBean != null && helpMsgBean.getErrno() == 0) {
                    SharedPreferenceTool.setPrefString(this, SharedPreferenceTool.KEY_BRANDHELPMSG, json);
                    YYConstans.brandHelpMsgVOs = helpMsgBean.getReturnContent();
                    loadHelpPhoto(YYConstans.brandHelpMsgVOs);
                }
                break;

//            case 80080:
//                parseData(json);
//                break;
        }
    }

    @Override
    public void onChangeDrawerLayoutStatus() {
        // TODO Auto-generated method stub
        changeDrawerLayoutStatus();
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (carListFrg != null && carListFrg.getView() != null && carListFrg.getView().getVisibility() == View.VISIBLE) {
                changeToFragment(1);
                return true;
            }
            if (mapCarFrg != null && mapCarFrg.isLl_bottomIsVisible()) {
                mapCarFrg.initCreateStatu();
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MyUtils.showToast(MainActivity2_1.this, getResources()
                        .getString(R.string.app_exit_tip));
                exitTime = System.currentTimeMillis();
            } else {
                YYApplication.getApplication().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    /**
//     * 打开拨打客服电话弹窗
//     */
//    public void showMessageDialog(String messge) {
//        if (messageDlg == null) {
//            messageDlg = new Dialog(this, R.style.MyDialog);
//            View mDlgCallView = LayoutInflater.from(this).inflate(
//                    R.layout.dlg_message, null);
//            TextView tv_cancel_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_do_txt);
//            tv_cancel_txt.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    messageDlg.dismiss();
//                }
//            });
//            final TextView tv_call_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_call_txt);
//            final TextView tv_number = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_message);
//            tv_number.setText(messge);
//            messageDlg.setCanceledOnTouchOutside(false);
//            messageDlg.setContentView(mDlgCallView);
//        }
//        messageDlg.show();
//        Window dlgWindow = messageDlg.getWindow();
//        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.width = MyUtils.getScreenWidth(this) / 10 * 7;
//        dlgWindow.setAttributes(lp);
//    }

//    /**
//     * 打开拨打客服电话弹窗
//     */
//    public void showLocationDialog(String messge) {
//        if (locationDlg == null) {
//            locationDlg = new Dialog(this, R.style.MyDialog);
//            View mDlgCallView = LayoutInflater.from(this).inflate(
//                    R.layout.dlg_message, null);
//            TextView tv_cancel_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_do_txt);
//            tv_cancel_txt.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    locationDlg.dismiss();
//                }
//            });
//            final TextView tv_call_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_call_txt);
//            final TextView tv_number = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_message);
//            tv_number.setText(messge);
//            locationDlg.setCanceledOnTouchOutside(false);
//            locationDlg.setContentView(mDlgCallView);
//        }
//        locationDlg.show();
//
//        Window dlgWindow = locationDlg.getWindow();
//        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.width = MyUtils.getScreenWidth(this) / 10 * 7;
//        dlgWindow.setAttributes(lp);
//
//    }

    private void showUpdataDialog(final UpdataBean bean) {
        if (updataDialog == null) {
            updataDialog = new Dialog(this, R.style.MyDialog);
            updataDialog.setContentView(updataView);
            updataDialog.setCanceledOnTouchOutside(false);
        }
        if (bean.getUpdateForce() == 1) {
            tv_cancel_txt.setVisibility(View.GONE);// 强制
        } else {
            tv_cancel_txt.setVisibility(View.VISIBLE);
        }
        tv_content.setText(bean.getDescription());
        tv_updata_txt.setTag(bean);
        Window dlgWindow = updataDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(this) / 5 * 4;
        dlgWindow.setAttributes(lp);
        updataDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (bean.getUpdateForce() == 1) {
                        return true;// 强制
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
        updataDialog.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_updata_txt:
                UpdataBean bean = (UpdataBean) v.getTag();
                new ApkDownLoad(this, bean.getAndroidUrl(), "星辰出行", "版本升级")
                        .execute();
            case R.id.tv_cancel_txt:
                updataDialog.dismiss();
                break;

            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baseHandler != null) {
            baseHandler.removeCallbacksAndMessages(null);
        }
        if (adDialog != null && adDialog.isShowing()) {
            adDialog.dismiss();
        }
        if (loginSuccessBroadCast != null) {
            unregisterReceiver(loginSuccessBroadCast);
        }
        if (refreshThread != null && refreshThread.isAlive()) {
            refreshThread.interrupt();
            refreshThread = null;
        }
//        unregistGpsListenter();
    }

//    /**
//     * 定位失败弹窗
//     */
//    public void showLocFailDialog(String msg) {
//        if (locFailDlg == null) {
//            locFailDlg = new Dialog(this, R.style.MyDialog);
//            View mDlgCallView = LayoutInflater.from(this).inflate(
//                    R.layout.dlg_call, null);
//            TextView tv_cancel_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_cancel_txt);
//            tv_cancel_txt.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    locFailDlg.dismiss();
//                }
//            });
//            final TextView tv_call_txt = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_call_txt);
//            tv_call_txt.setText("重试");
//            final TextView tv_number = (TextView) mDlgCallView
//                    .findViewById(R.id.tv_number);
//            tv_number.setText(msg);
//            tv_call_txt.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stubt
//                    getLocationData(true);
//                    locFailDlg.dismiss();
//                }
//            });
//            locFailDlg.setCanceledOnTouchOutside(true);
//            locFailDlg.setContentView(mDlgCallView);
//        }
//        locFailDlg.show();
//        Window dlgWindow = locFailDlg.getWindow();
//        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.width = MyUtils.getScreenWidth(this) / 10 * 7;
//        dlgWindow.setAttributes(lp);
//    }


    private class LoginSuccessBroadCast extends BroadcastReceiver {


        private RequestInterface requestInterface;
        private Context mContext;

        public LoginSuccessBroadCast(Context mContext, RequestInterface requestInterface) {
            this.mContext = mContext;
            this.requestInterface = requestInterface;
        }

        @Override
        public void onReceive(Context context, Intent intent) {


            Map<String, String> params = new HashMap<String, String>();
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(1001, YYRunner.Method_POST,
                    YYUrl.GETUNDOORDEDETAIL, params,
                    requestInterface);

        }
    }

    private void showADDialog() {

        String ads = SharedPreferenceTool.getPrefString(this,
                SharedPreferenceTool.KEY_ADS_MAIN, "");
        String lastDate = SharedPreferenceTool.getPrefString(this,
                SharedPreferenceTool.KEY_LASTSHOWAD_DATE, "");

        long tempTime = System.currentTimeMillis();

        if (TextUtils.isEmpty(lastDate) || !lastDate.equals(TimeDateUtil.formatTime(tempTime, "yyyy-MM-dd"))) {
            //当天没展示过
        } else {
            //当天展示过
            return;
        }

        if (!TextUtils.isEmpty(ads) && !YYConstans.hasShowMainAD) {
            ADPhotoListBean adPhotoListBean = (ADPhotoListBean) GsonTransformUtil
                    .fromJson(ads, ADPhotoListBean.class);
            if (adPhotoListBean != null && adPhotoListBean.getErrno() == 0 && adPhotoListBean.getPhotos() != null) {
                for (int i = 0; i < adPhotoListBean.getPhotos().size(); i++) {


                    ADPhotosBean adPhotosBean = adPhotoListBean.getPhotos().get(i);
                    if (adPhotosBean != null && tempTime > adPhotosBean.getStartTime()
                            && tempTime < adPhotosBean.getEndTime()) {

                        if (adDialog == null) {
                            YYConstans.hasShowMainAD = true;
                            SharedPreferenceTool.setPrefString(this, SharedPreferenceTool.KEY_LASTSHOWAD_DATE, TimeDateUtil.formatTime(tempTime, "yyyy-MM-dd"));
                            adDialog = new ADDialog(this);
                            adDialog.setOwnerActivity(this);
                            adDialog.setAdPhotosBean(adPhotosBean);
                            adDialog.show();
                        }
                        break;
                    }

                }
            }
        }
    }


    private void getBrandHelpMsg() {
        Map<String, String> getBrandHelp = new HashMap<String, String>();
        YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.GETBRANDHELPMSG, getBrandHelp,
                this);
    }


    private void loadHelpPhoto(ArrayList<BrandHelpMsgVO> brandHelpMsgVOs) {

        if (brandHelpMsgVOs != null) {
            for (int i = 0; brandHelpMsgVOs != null && i < brandHelpMsgVOs.size(); i++) {

                BrandHelpMsgVO temp = brandHelpMsgVOs.get(i);
                for (int j = 0; temp.getHelpPhoto() != null && j < temp.getHelpPhoto().size(); j++) {
                    ImageLoader.getInstance().loadImage(temp.getHelpPhoto().get(j), null);
                }
            }
        }
    }

    private Thread refreshThread;

//    private void refresh() {
//        refreshThread = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                while (true) {
//                    Date oldDate = new Date();
//                    oldDate.setTime(System.currentTimeMillis());
//                    //判断时间
//                    if (oldDate.getHours() >= 17 && oldDate.getHours() < 20) {
//                        //判断登录
//                        if (!TextUtils.isEmpty(YYConstans.getUserInfoBean().getSkey()) && YYConstans.getUserInfoBean().getUser() != null && ("18911535957".equals(YYConstans.getUserInfoBean().getUser().getMobile()) || "15090657731".equals(YYConstans.getUserInfoBean().getUser().getMobile()))) {
//                            //刷新数据
//                            baseHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    refreshData();
//                                }
//                            }, 30000);
//                        }
//                    }
//                    try {
//                        Thread.sleep(30000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        refreshThread.start();
//
//    }

//    private void parseData(String json) {
//        MapcarfrgStationListReContentVo tempReturnContent = (MapcarfrgStationListReContentVo) GsonTransformUtil.fromJson(json,
//                MapcarfrgStationListReContentVo.class);
//        if (tempReturnContent != null && tempReturnContent.getReturnContent() != null && tempReturnContent.getReturnContent().getCurrentCity() != null && tempReturnContent.getReturnContent().getCurrentCity().getStationList() != null) {
//            boolean hasFind = false;
////            for (int i = 0; i < tempReturnContent.getReturnContent().getCurrentCity().getStationList().size(); i++) {
////                if (tempReturnContent.getReturnContent().getCurrentCity().getStationList().get(i).getId() == 2) {
////                    if (tempReturnContent.getReturnContent().getCurrentCity().getStationList().get(i).getCarList() != null && tempReturnContent.getReturnContent().getCurrentCity().getStationList().get(i).getCarList().size() > 0) {
////                        SpeechUtil.getInstance().peechMessage(this, "嗨，嗨，主淫，楼下有车啦！！！嗨，嗨，主淫，楼下有车啦！！！");
////                        hasFind = true;
////                    }
////                }
////            }
//
//            if (!hasFind) {
//                SpeechUtil.getInstance().peechMessage(this, "主淫，楼下的汽车都被那群孙子们开走了");
//            }
//
//        } else {
//            SpeechUtil.getInstance().peechMessage(this, "全京城木有车");
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        // Forward results to EasyPermissions
//        //将结果传入EasyPermissions中
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    /**
//     * 请求权限成功
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        mapCarFrg.requestLocationData(false);
//    }
//
//    /**
//     * 请求权限失败
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        /**
//         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
//         * 这时候，需要跳转到设置界面去，让用户手动开启。
//         */
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this)
//                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
//                    .setTitle("必需权限")
//                    .build()
//                    .show();
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            //当从软件设置界面，返回当前程序时候重新检查权限
//            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
//                checkAndRequestPermissions();
//                break;
//        }
//    }
//
//    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
//
//    private boolean hasPermissions() {
//
//        return EasyPermissions.hasPermissions(this, perms);
//    }
//
//    /**
//     * 检查百度地图所需的权限
//     */
//    public void checkAndRequestPermissions() {
//        if (hasPermissions()) {
//            if(!isOPen(this)){
//                currentGPSState = false;
//                //TODO 未打开GPS的业务处理逻辑
//                Toast.makeText(this,"请打开GPS",Toast.LENGTH_LONG).show();
//            }else{
//                currentGPSState = true;
//            }
//        } else {
//            // Ask for one permission
//            EasyPermissions.requestPermissions(
//                    this,
//                    "申请权限   ",
//                    0,
//                    perms);
//        }
//    }
//
//    /**
//     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
//     * @param context
//     * @return true 表示开启
//     */
//    public static final boolean isOPen(final Context context) {
//        LocationManager locationManager
//                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
//        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (gps || network) {
//            return true;
//        }
//
//        return false;
//    }
//
//    boolean currentGPSState = false;
//
//    GpsStatusReceiver gpsStatusReceiver ;
//
//    /**
//     * 注册监听广播
//     */
//    public void registGpsListenter(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//        this.registerReceiver(gpsStatusReceiver, filter);
//    }
//
//    /**
//     * 移除监听广播
//     */
//    public void unregistGpsListenter(){
//        this.unregisterReceiver(gpsStatusReceiver);
//        gpsStatusReceiver = null;
//    }
//
//
//    /**
//     * 监听GPS 状态变化广播
//     */
//    public class GpsStatusReceiver extends BroadcastReceiver {
//
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
//                currentGPSState = getGPSState(context);
//                if(currentGPSState){
//                    //TODO 监听到打开GPS的逻辑
//                }
//            }
//        }
//
//        /**
//         * 获取ＧＰＳ当前状态
//         *
//         * @param context
//         * @return
//         */
//        private boolean getGPSState(Context context) {
//            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            boolean on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            return on;
//        }
//
//
//
//    }

}
