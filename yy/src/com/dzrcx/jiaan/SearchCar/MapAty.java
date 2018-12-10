package com.dzrcx.jiaan.SearchCar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
//import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
//import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MapAty extends YYBaseActivity {
    private MapFrg MapFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
//        if (!BaiduNaviManager.isNaviInited() && initDirs()) {
//            initNavi();
//        }
        MapFrg = new MapFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, MapFrg);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // BaiduNaviManager.getInstance().uninit();
        super.onDestroy();
    }

    //以下是百度导航需要
    public boolean initDirs() {
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

//    public void initNavi() {
//        // BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
//        // "/BaiduNaviSDK_SO");
//
//
//        BNOuterTTSPlayerCallback ttsCallback = null;
//        try {
//            BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
//                @Override
//                public void onAuthResult(int status, String msg) {
//                    if (0 == status) {
//                        authinfo = "key校验成功!";
//                    } else {
//                        authinfo = "key校验失败 " + msg;
//                        MapAty.this.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Toast.makeText(MapAty.this, authinfo, Toast.LENGTH_LONG).show();
//                            }
//                        });
//                    }
//
//                }
//
//                public void initSuccess() {
//                    YYConstans.hasInitNavi = true;
////                Toast.makeText(MapAty.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//                }
//
//                public void initStart() {
//                }
//
//                public void initFailed() {
//                    Toast.makeText(MapAty.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
//                    YYConstans.hasInitNavi = false;
//                }
//
//            }, null/* null mTTSCallback */);
//        } catch (Exception e) {
//
//        }
//
//    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}