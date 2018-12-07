package com.dzrcx.jiaan.Order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.dzrcx.jiaan.Bean.UndoOrderBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.clicklistener.ClickBroadcastReceiver;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.SpeechUtil;
import com.dzrcx.jiaan.yyinterface.FragmentCallActivity;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class OrderAty extends YYBaseActivity implements RequestInterface,
        YYApplication.OnGetLocationlistener, FragmentCallActivity {

    private OrderFrg orderFrg;

    private ScreenBroadcastReceiver screenBroadcastReceiver;
    private UndoOrderBean undoOrderBean;
    private long exitTime = 0;
    boolean hasNot = false;// 50米提醒次数
    public Handler orderHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 1001:
                    if (YYApplication.mLocationClient != null
                            && YYApplication.mLocationClient.isStarted()) {
                        YYApplication.mLocationClient.requestLocation();
                    } else {
                        YYApplication.mLocationClient.start();
                        YYApplication.mLocationClient.requestLocation();
                    }

                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        orderFrg = new OrderFrg();
        setContentView(R.layout.yy_base_act);
        undoOrderBean = (UndoOrderBean) getIntent().getSerializableExtra(
                "UndoOrderBean");
        if (undoOrderBean != null && undoOrderBean.getReturnContent() != null) {
            orderFrg.setOrderDetailVo(undoOrderBean.getReturnContent());
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("isFirst", false)) {
            orderFrg.setIsFirst(true);
        }
        orderFrg.setFragmentCallActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, orderFrg);
        transaction.commit();

        initView();
        hasNot = false;
        setDownOut(false);
        if (screenBroadcastReceiver == null) {
            screenBroadcastReceiver = new ScreenBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            registerReceiver(screenBroadcastReceiver, filter);
        }
    }


    private void initView() {
        // TODO Auto-generated method stub
        YYApplication.setLocationIFC(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        YYApplication.isRequestDataLocation = true;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        orderHandler.removeMessages(1001);
        hasNot = false;


        if (screenBroadcastReceiver != null) {
            unregisterReceiver(screenBroadcastReceiver);
            screenBroadcastReceiver = null;
        }
        // BaiduNaviManager.getInstance().uninit();
        super.onDestroy();
    }


    @Override
    public void getBdlocation(BDLocation location) {
        // TODO Auto-generated method stub

        if (location != null && YYConstans.getUseCarLng() != null && !hasNot) {
            LatLng latLng = new LatLng(location.getLatitude(),
                    location.getLongitude());
            if (DistanceUtil.getDistance(latLng, YYConstans.getUseCarLng()) <= 50) {
                orderHandler.removeMessages(1001);
                showNOT();
                hasNot = true;
            } else {
                orderHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        orderHandler.sendEmptyMessageDelayed(1001, 2000);
                    }
                }, 10000);
            }

        }

    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {


            Intent toIntent = new Intent(this, MainActivity2_1.class);
            toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            toIntent.putExtra("update", true);
            startActivity(toIntent);
            overridePendingTransition(R.anim.activity_up,
                    R.anim.activity_push_no_anim);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    boolean hasgetLocation = false;

    @Override
    public void onCall(int type, Bundle bundle) {
        // TODO Auto-generated method stub
        switch (type) {
            case 1:
                orderHandler.removeMessages(1001);
                orderHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (!hasNot) {
                            orderHandler.sendEmptyMessageDelayed(1001, 2000);
                        }
                    }
                }, 1000);
                break;

            default:
                break;
        }
    }

    /**
     * 弹出通知告知用户离车距离在50米内
     */
    @SuppressLint("NewApi")
    private void showNOT() {
        //  设置自定义样式属性，该属性对对应的编号生效，指定后不能修改。
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0,
                new Intent(this, ClickBroadcastReceiver.class).setAction("android.intent.action.OrderAty_distance"), PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify2 = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("TickerText:" + "您距离车辆50米，可在接近车辆时尝试“鸣笛找车”寻找车辆。")// 设置在status
                // bar上显示的提示文字
                .setContentTitle("星辰出行")// 设置在下拉status
                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText("您距离车辆50米，可在接近车辆时尝试“鸣笛找车”寻找车辆。")// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        notify2.defaults |= Notification.DEFAULT_ALL;
        manager.notify(11, notify2);

    }

    //以下是百度导航需要
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    public static List<Activity> activityList = new LinkedList<Activity>();

    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    private String mSDCardPath = null;

    private void initNavi() {
        // BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
        // "/BaiduNaviSDK_SO");


        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败 " + msg;
                    OrderAty.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(OrderAty.this, authinfo, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            public void initSuccess() {
                YYConstans.hasInitNavi = true;
            }

            public void initStart() {
            }

            public void initFailed() {
                Toast.makeText(OrderAty.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                YYConstans.hasInitNavi = false;
            }

        }, null/* null mTTSCallback */);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                SpeechUtil.getInstance().stop(OrderAty.this);
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            }
        }
    }


}
