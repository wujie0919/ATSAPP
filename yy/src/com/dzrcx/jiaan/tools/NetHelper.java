package com.dzrcx.jiaan.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @文件名：NetWorkTool.java
 * @包名：com.lky.xuqiuwang.tools
 * @作�?�：小贾 xiaojia1680@foxmail.com
 * @创建时间�?2015-1-7 下午2:56:24
 * @描述：判断网�?
 */
public class NetHelper {

    /**
     * @param context
     * @return false 表示 有网
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return true;
        } else {
            // final android.net.NetworkInfo wifi = cm
            // .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            // final android.net.NetworkInfo mobile = cm
            // .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return false;
            }
        }
        return true;
    }
}
