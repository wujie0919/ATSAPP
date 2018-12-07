package com.dzrcx.jiaan.clicklistener;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.dzrcx.jiaan.tools.LG;
import com.dzrcx.jiaan.tools.SystemUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ClickBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LG.d(SystemUtils.isAppAlive(context, "com.dzrcx.jiaan") + "是否死了");
        //判断app进程是否存活
        if (SystemUtils.isAppAlive(context, "com.dzrcx.jiaan")) {
            //获取ActivityManager  
            ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            //获得当前运行的task  
            List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo rti : taskList) {
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台  
                if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mAm.moveTaskToFront(rti.id, Intent.FLAG_ACTIVITY_NEW_TASK);//恢复前台
                    } else {
                        restartApp(context);
                    }
                    return;
                }
            }
            restartApp(context);
        } else {//重新启动app
            restartApp(context);
        }

    }

    private void restartApp(Context context) {
        Intent intent_ded = context.getPackageManager().getLaunchIntentForPackage("com.dzrcx.jiaan");
        intent_ded.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent_ded);
    }
}
