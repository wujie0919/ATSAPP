package com.dzrcx.jiaan.base;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.dzrcx.jiaan.Main.PushDialogActivity;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.tools.InitSystemBarColorUtil;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SpeechUtil;
import com.dzrcx.jiaan.widget.NoNetDialog;
import com.dzrcx.jiaan.widget.YYProgresssDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class YYBaseActivity extends FragmentActivity {
    public YYProgresssDialog progresssDialog;
    private NoNetDialog noNetDialog;
    //    public boolean isFatherAllow = true;
    private boolean isDownOut = true;
    public Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };

    //动态创建广播
    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("pushBroadcastReceiver".equals(intent.getAction())){
                if(intent.getExtras()!=null && intent.getIntExtra("getReletRemainingSeconds",0)>0){
                    //String strTime = intent.getIntExtra("getReletRemainingSeconds",0)+"";
                    Intent i = new Intent(YYBaseActivity.this, PushDialogActivity.class);
                    i.putExtra("getReletRemainingSeconds",intent.getIntExtra("getReletRemainingSeconds",0));
                    YYBaseActivity.this.startActivity(i);

                    //Log.e("onReceive","进了广播有时间");
                }else{
                    Intent i = new Intent(YYBaseActivity.this, PushDialogActivity.class);
                    YYBaseActivity.this.startActivity(i);
                    Toast.makeText(YYBaseActivity.this,"接受到了推送信息",Toast.LENGTH_SHORT).show();
                    //Log.e("onReceive","进了广播没时间");
                }
            }
        }
    };
    //动态注册广播
    private void registRecevier(){
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("pushBroadcastReceiver");			//添加动态广播的Action
        registerReceiver(pushBroadcastReceiver, dynamic_filter);
    }
    //动态移除广播
    private void removeRecevier(){
        unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        YYApplication.getApplication().addActivity(this);
        InitSystemBarColorUtil.initSystemBar(this);//初始化状态栏
        initDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }

    private void initDialog() {
        progresssDialog = new YYProgresssDialog(this,
                R.style.customProgressDialog);
        progresssDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
        if (NetHelper.checkNetwork(this)) {
            showNoNetDlg();
        }
        requestLocation();
        MobclickAgent.onResume(this);
        registRecevier();//注册广播
    }

    public void requestLocation() {
        if (YYApplication.mLocationClient.isStarted())
            YYApplication.mLocationClient.requestLocation();
        else {
            YYApplication.mLocationClient.start();
            YYApplication.mLocationClient.requestLocation();
        }
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        removeRecevier();//移除广播
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (progresssDialog != null && progresssDialog.isShowing()) {
            progresssDialog.dismiss();
        }
        super.onStop();
        if (!isAppOnForeground()) {
            SpeechUtil.getInstance().stop(this);
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        YYApplication.getApplication().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

    public void dialogShow() {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dialogShow(String message) {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(message);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dismmisDialog() {
        if (progresssDialog != null && progresssDialog.isShowing()) {
            progresssDialog.dismiss();
        }
    }

    /**
     * 设置是否向下隐藏退出
     *
     * @param isDownOut
     */
    public void setDownOut(boolean isDownOut) {
        this.isDownOut = isDownOut;
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        if (!isDownOut) {
            return;
        }
        if (noNetDialog != null && noNetDialog.isShowing()) {
            noNetDialog.cancel();
        }
        overridePendingTransition(0, R.anim.activity_down);
    }

    public void showNoNetDlg() {
        if (noNetDialog == null) {
            noNetDialog = new NoNetDialog(this, R.style.MyDialog);
            noNetDialog.show();
        } else if (noNetDialog != null && !noNetDialog.isShowing()) {
            noNetDialog.show();
        }
    }

    /**
     * @param context
     * @return 返回true ==后台
     * 返回false==前台
     */
    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 　　 * 程序是否在前台运行
     * 　　 *
     * 　 * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
