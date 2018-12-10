//package com.dzrcx.jiaan.Order;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.dzrcx.jiaan.Constans.YYConstans;
//import com.dzrcx.jiaan.R;
//import com.dzrcx.jiaan.YYApplication;
//import com.dzrcx.jiaan.base.YYBaseActivity;
//import com.dzrcx.jiaan.tools.MyUtils;
//import com.dzrcx.jiaan.widget.StatusView;
//import com.baidu.navisdk.adapter.BNRouteGuideManager;
//import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
//import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
//import com.baidu.navisdk.adapter.BNRoutePlanNode;
//import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 引导界面
// *
// * @author sunhao04
// */
//public class BNDemoGuideActivity extends YYBaseActivity {
//
//    private BNRoutePlanNode mBNRoutePlanNode = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        createHandler();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//        }
//        View view = BNRouteGuideManager.getInstance().onCreate(this,
//                new OnNavigationListener() {
//
//                    @Override
//                    public void onNaviGuideEnd() {
//                        finish();
//                    }
//
//                    @Override
//                    public void notifyOtherAction(int actionType, int arg1,
//                                                  int arg2, Object obj) {
////                        Log.e("BNDemoGuideActivity_notifyOtherAction",
////                                "actionType:" + actionType + "arg1:" + arg1
////                                        + "arg2:" + arg2 + "obj:"
////                                        + obj.toString());
//                    }
//
//                });
//        View statusView = new StatusView(this);
//        statusView.setBackgroundColor(Color.parseColor("#2e3242"));
//        statusView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyUtils.dip2px(this, 14)));
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.addView(statusView);
//        linearLayout.addView(view);
//        if (linearLayout != null) {
//            setContentView(linearLayout);
//        }
//        Intent intent = getIntent();
//        if (intent != null) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                String str_mBNRoutePlanNode =  bundle
//                        .getString(YYConstans.ROUTE_PLAN_NODE);//序列化错误
//                Gson gsong = new Gson();
//                mBNRoutePlanNode = gsong.fromJson(str_mBNRoutePlanNode,BNRoutePlanNode.class);
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        BNRouteGuideManager.getInstance().onResume();
//        super.onResume();
//        if (hd != null) {
//            hd.sendEmptyMessageAtTime(MSG_SHOW, 2000);
//        }
//    }
//
//    protected void onPause() {
//        super.onPause();
//        BNRouteGuideManager.getInstance().onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        BNRouteGuideManager.getInstance().onDestroy();
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStop() {
//        BNRouteGuideManager.getInstance().onStop();
//        super.onStop();
//    }
//
//    @Override
//    public void onBackPressed() {
//        BNRouteGuideManager.getInstance().onBackPressed(false);
//    }
//
//    public void onConfigurationChanged(
//            android.content.res.Configuration newConfig) {
//        BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
//        super.onConfigurationChanged(newConfig);
//    }
//
//    ;
//
//    private void addCustomizedLayerItems() {
//        List<CustomizedLayerItem> items = new ArrayList<CustomizedLayerItem>();
//        CustomizedLayerItem item1 = null;
//        if (mBNRoutePlanNode != null) {
//            item1 = new CustomizedLayerItem(mBNRoutePlanNode.getLongitude(),
//                    mBNRoutePlanNode.getLatitude(),
//                    mBNRoutePlanNode.getCoordinateType(), getResources()
//                    .getDrawable(R.drawable.ic_launcher),
//                    CustomizedLayerItem.ALIGN_CENTER);
//            items.add(item1);
//            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
//        }
//        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
//    }
//
//    private static final int MSG_SHOW = 1;
//    private static final int MSG_HIDE = 2;
//    private static final int MSG_RESET_NODE = 3;
//    private Handler hd = null;
//
//    private void createHandler() {
//        if (hd == null) {
//            hd = new Handler(getMainLooper()) {
//                public void handleMessage(android.os.Message msg) {
//                    if (msg.what == MSG_SHOW) {
//                        addCustomizedLayerItems();
//                    } else if (msg.what == MSG_HIDE) {
//                        BNRouteGuideManager.getInstance().showCustomizedLayer(
//                                false);
//                    } else if (msg.what == MSG_RESET_NODE) {
//                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
//                                new BNRoutePlanNode(YYApplication.Longitude, YYApplication.Latitude,
//                                        YYApplication.LocaAdrrName, null, CoordinateType.GCJ02));
//                    }
//                }
//
//                ;
//            };
//        }
//    }
//}
