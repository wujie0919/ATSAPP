package com.dzrcx.jiaan.SearchCar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Order.BNDemoGuideActivity;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.distance.DistancePointVo;
import com.dzrcx.jiaan.tools.MyUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MapFrg extends YYBaseFragment implements
        OnGetRoutePlanResultListener, OnMarkerClickListener, OnMapClickListener, OnClickListener {
    private long exitTime = 0;
    private BitmapDescriptor bitmapDescriptor = null;
    private BitmapDescriptor starBitmapDescriptor = null;
    private TextView tv_title, tv_location, tv_location_style;
    private ImageView map_navi, iv_left_raw;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private InfoWindow mInfoWindow;
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    boolean useDefaultIcon = true;
    private int juli, time;
    private LatLng startLl, endLl;
    public Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };
    private DistancePointVo pointVo;
    private boolean isBackCar = false;
    private View mapFrgView;
    private Intent intent;
    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mapFrgView == null) {
            this.inflater = inflater;
            mapFrgView = inflater.inflate(R.layout.aty_map, null);
            initview();
            //pointVo = (DistancePointVo) intent.getStringExtra("pointVo");//序列化
            String strPointVo = intent.getExtras().getString("pointVo");//序列化
//            Log.e("strPointVo",strPointVo);
            Gson gsong = new Gson();
            pointVo = gsong.fromJson(strPointVo,DistancePointVo.class);
            isBackCar = intent.getBooleanExtra("isBackCar", false);

            if (isBackCar) {
                tv_title.setText("还车导航");
            }
//            if (!BaiduNaviManager.isNaviInited()) {
//                if (((MapAty) getActivity()).initDirs()) {
//                    ((MapAty) getActivity()).initNavi();
//                }
//            }
            //路线规划 路径规划 步行路线规划 步行路径规划
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub n
                    if ("orderfrg".equals(intent.getStringExtra("name"))) {
                        PlanNode stNode = null;
                        if (isBackCar) {
                            tv_location_style.setText("还车位置:");
                            tv_location.setText(pointVo.getTarAdrrName());
                            startLl = new LatLng(pointVo.getLocationLat(),
                                    pointVo.getLocationLng());
                            stNode = PlanNode.withLocation(startLl);
                            endLl = new LatLng(pointVo.getTargetLat(), pointVo.getTargetLng());
                        } else {
                            tv_location_style.setText("车辆位置:");
                            tv_location.setText(pointVo.getTarAdrrName());
                            startLl = new LatLng(YYApplication.Latitude,
                                    YYApplication.Longitude);
                            stNode = PlanNode.withLocation(startLl);

                            endLl = new LatLng(Double.parseDouble(intent
                                    .getStringExtra("latitude")), Double
                                    .parseDouble(intent.getStringExtra("longitude")));
                        }

                        PlanNode enNode = PlanNode.withLocation(endLl);
                        LatLng midlleLl = new LatLng(
                                (YYApplication.Latitude + Double.parseDouble(intent
                                        .getStringExtra("latitude"))) / 2,
                                (YYApplication.Longitude + Double
                                        .parseDouble(intent
                                                .getStringExtra("longitude"))) / 2);
                        MapStatusUpdate u = MapStatusUpdateFactory
                                .newLatLng(midlleLl);
                        mBaiduMap.animateMapStatus(u);
                        baseHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                                        .zoomTo(13));
                            }
                        }, 330);
                        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(
                                stNode).to(enNode));
                    } else if ("CarListFrg"
                            .equals(intent.getStringExtra("name"))) {
                        tv_location_style.setText("租赁站位置:");
                        tv_location.setText(pointVo.getTarAdrrName());
                        startLl = new LatLng(YYApplication.Latitude,
                                YYApplication.Longitude);
                        PlanNode stNode = PlanNode.withLocation(startLl);

                        endLl = new LatLng(Double.parseDouble(intent
                                .getStringExtra("latitude")), Double
                                .parseDouble(intent.getStringExtra("longitude")));
                        PlanNode enNode = PlanNode.withLocation(endLl);

                        LatLng midlleLl = new LatLng(
                                (YYApplication.Latitude + Double.parseDouble(intent
                                        .getStringExtra("latitude"))) / 2,
                                (YYApplication.Longitude + Double
                                        .parseDouble(intent
                                                .getStringExtra("longitude"))) / 2);
                        MapStatusUpdate u = MapStatusUpdateFactory
                                .newLatLng(midlleLl);
                        mBaiduMap.animateMapStatus(u);

                        baseHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (mBaiduMap != null) {
                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                                            .zoomTo(13));
                                }
                            }
                        }, 330);
                        if (mSearch.walkingSearch((new WalkingRoutePlanOption()).from(
                                stNode).to(enNode))) {
                        } else {
                            MyUtils.showToast(mContext, "路线规划失败，请重试");
                        }
                    }
                }
            }, 800);
            MobclickAgent.onEvent(mContext, "open_map");


        }
        return mapFrgView;
    }

    private void initview() {
        tv_title = (TextView) mapFrgView.findViewById(R.id.tv_title);
        tv_title.setText("步行找车");
        iv_left_raw = (ImageView) mapFrgView.findViewById(R.id.iv_left_raw);
        tv_location = (TextView) mapFrgView.findViewById(R.id.tv_location);
        tv_location_style = (TextView) mapFrgView.findViewById(R.id.tv_location_style);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(this);
        map_navi = (ImageView) mapFrgView.findViewById(R.id.map_navi);
        map_navi.setOnClickListener(this);
        if ("CarListFrg"
                .equals(intent.getStringExtra("name"))) {
            tv_title.setText(intent
                    .getStringExtra("className"));
        }
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        mMapView = (MapView) mapFrgView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);// 不显示缩放控件
        mMapView.showScaleControl(false);// 不显示比例尺
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().zoom(13).build()));// 设定地图缩放比例百度地图缩放范围（3-19），12两公里
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                getStationView(null), MyUtils.dip2px(mContext, 40),
                MyUtils.dip2px(mContext, 40)));
        starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                getLocationView(), MyUtils.dip2px(mContext, 21),
                MyUtils.dip2px(mContext, 32)));
    }

    /**
     * 获取当前站点View
     */
    private View getStationView(Integer number, boolean isSelected) {
        View view = inflater.inflate(R.layout.mapcarfrg_carlocation_icon_n, null);
        TextView tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
        ImageView iv_station_bg = (ImageView) view.findViewById(R.id.iv_station_bg);
        if (number == null) {
            tv_car_number.setText("");
            iv_station_bg.setImageResource(R.drawable.mapcarfrg_havecar_bg);
            return view;
        } else {
            tv_car_number.setText(number + "");
        }
        if (isSelected) {
            tv_car_number.setTextColor(Color.parseColor("#ffffff"));
            iv_station_bg.setImageResource(R.drawable.mapcarfrg_selectcar_bg);
        } else if (number == 0) {
            tv_car_number.setTextColor(Color.parseColor("#cccccc"));
            iv_station_bg.setImageResource(R.drawable.mapcarfrg_nocar_bg);
        } else if (number > 0) {
            tv_car_number.setTextColor(getResources().getColor(R.color.titlebar_background));
            iv_station_bg.setImageResource(R.drawable.mapcarfrg_havecar_bg);
        }
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        baseHandler.removeCallbacksAndMessages(null);
        mMapView.onDestroy();
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);
        }
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView = null;
        if (bitmapDescriptor != null)
            bitmapDescriptor.recycle();
        if (starBitmapDescriptor != null)
            starBitmapDescriptor.recycle();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            final WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
                    mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            result.getRouteLines().get(0).getStarting();
            juli = result.getRouteLines().get(0).getDistance();// 获取路线距离
            time = result.getRouteLines().get(0).getDuration();// 获取路线耗时
            overlay.addToMap();
            String startStr = "<font color='#13bb86'>" + "步行约" + juli + "米,约"
                    + time / 60 + "分钟" + "</font>";
            if ("orderfrg"
                    .equals(intent.getStringExtra("name")) && isBackCar) {
                startStr = "<font color='#13bb86'>" + "约" + juli + "米" + "</font>";
            }
            // String startStr = "<font color='#000000'>" + "步行约" + "</font>"
            // + "<font color='#13bb87'>" + juli + "米" + "</font>"
            // + "<font color='#000000'>" + "约" + "</font>"
            // + "<font color='#ff5a00'>" + time / 60 + "分钟" + "</font>";
            showInfowindow(startStr, startLl, 32);
            // overlay.zoomToSpan();
            baseHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    float zoomnum = getZoomNum();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                            .zoomTo(zoomnum));
                }
            }, 800);
            overlay.addToMap();
            overlay.zoomToSpan();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.map_navi:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    boolean isInit = BaiduNaviManager.isNaviInited();
                    if (BaiduNaviManager.isNaviInited() && pointVo != null) {
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL, pointVo);
                        exitTime = System.currentTimeMillis();
                    }
                }
                break;

        }
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, DistancePointVo disVo) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(disVo.getLocationLng(), disVo.getLocationLat(), disVo.getLocaAdrrName(), null,
                        coType);
                eNode = new BNRoutePlanNode(disVo.getTargetLng(), disVo.getTargetLat(), disVo.getTarAdrrName(), null,
                        coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(disVo.getLocationLng(), disVo.getLocationLat(), disVo.getLocaAdrrName(), null,
                        coType);
                eNode = new BNRoutePlanNode(disVo.getTargetLng(), disVo.getTargetLat(), disVo.getTarAdrrName(), null,
                        coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(disVo.getLocationLng(), disVo.getLocationLat(), disVo.getLocaAdrrName(), null, coType);
                eNode = new BNRoutePlanNode(disVo.getTargetLng(), disVo.getTargetLat(), disVo.getTarAdrrName(), null,
                        coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(disVo.getLocationLng(), disVo.getLocationLat(), disVo.getLocaAdrrName(), null, coType);
                eNode = new BNRoutePlanNode(disVo.getTargetLng(), disVo.getTargetLat(), disVo.getTarAdrrName(), null, coType);
                break;
            }
            default:
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list,
                    1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
			 */

            Intent intent = new Intent(mContext, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putSerializable(YYConstans.ROUTE_PLAN_NODE,
//                    mBNRoutePlanNode);//序列化错误
            Gson gson = new Gson();
            String strJson = gson.toJson(mBNRoutePlanNode);
            intent.putExtra(YYConstans.ROUTE_PLAN_NODE, strJson);

            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            MyUtils.showToast(mContext, "算路失败,请重试");
        }
    }

    // 定制RouteOverly
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub

        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return starBitmapDescriptor;
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if ("CarListFrg"
                    .equals(intent.getStringExtra("name"))) {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                        getStationView(Integer.parseInt(intent.getStringExtra("carnum")), false), MyUtils.dip2px(mContext, 38), MyUtils.dip2px(mContext, 43)));
            } else if ("orderfrg"
                    .equals(intent.getStringExtra("name")) && isBackCar) {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                        getStationView(null, false), MyUtils.dip2px(mContext, 40), MyUtils.dip2px(mContext, 40)));
            } else if ("orderfrg"
                    .equals(intent.getStringExtra("name")) && !isBackCar) {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                        getCarView(), MyUtils.dip2px(mContext, 53), MyUtils.dip2px(mContext, 24)));
            }
            if (useDefaultIcon) {
                return bitmapDescriptor;
            }
            return null;
        }
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng ll = arg0.getPosition();
        if (starBitmapDescriptor == arg0.getIcon()) {
            String startStr = "<font color='#13bb86'>" + "步行约" + juli + "米,约"
                    + time / 60 + "分钟" + "</font>";
            if (isBackCar) {
                startStr = "<font color='#13bb86'>" + "约" + juli + "米,约"
                        + time / 60 + "分钟" + "</font>";
            }
            // String startStr = "<font color='#000000'>" + "步行约" + "</font>"
            // + "<font color='#13bb87'>" + juli + "米" + "</font>"
            // + "<font color='#000000'>" + "约" + "</font>"
            // + "<font color='#ff5a00'>" + time / 60 + "分钟" + "</font>";
            showInfowindow(startStr, ll, 32);
        } else if (bitmapDescriptor.equals(arg0.getIcon())) {
            showInfowindow("<font color='#13bb86'>" + "终点位置" + "</font>", ll,
                    13);
        }

        return false;
    }

    private View showInfoView;

    @SuppressLint("NewApi")
    private void showInfowindow(String str, LatLng arg0, int padinghight) {
        if (showInfoView == null) {
            showInfoView = LayoutInflater.from(mContext).from(mContext)
                    .inflate(R.layout.showinfo_layout, null);
        }
        TextView textView = (TextView) showInfoView
                .findViewById(R.id.showinfo_text);
        // TextView textView = new TextView(getApplicationContext());
        // textView.setBackgroundResource(R.drawable.location_bg);
        int pading = MyUtils.dip2px(mContext, 5);
        textView.setPadding(pading, pading, pading, pading);
        textView.setText(Html.fromHtml(str));
        mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(showInfoView), arg0,
                -MyUtils.dip2px(mContext, padinghight), null);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        // TODO Auto-generated method stub

        return false;
    }

    private float getZoomNum() {
        float zoomnum = 13;
        float num = 0.26f;
        double dis = DistanceUtil.getDistance(startLl, endLl);
        if (dis >= 0 && dis < 20) {
            zoomnum = 20;
        } else if (dis >= 20 && dis < 50) {
            zoomnum = 19 + num * 1;
        } else if (dis >= 50 && dis < 100) {
            zoomnum = 18 + num * 2;
        } else if (dis >= 100 && dis < 200) {
            zoomnum = 17 + num * 3;

        } else if (dis >= 200 && dis < 500) {
            zoomnum = 16 + num * 4;

        } else if (dis >= 500 && dis < 1000) {
            zoomnum = 15 + num * 5;

        } else if (dis >= 1000 && dis < 2000) {
            zoomnum = 14 + num * 6;

        } else if (dis >= 2000 && dis < 5000) {
            zoomnum = 13 + num * 7;

        } else if (dis >= 5 * 1000 && dis < 10 * 1000) {
            zoomnum = 12 + num * 8;

        } else if (dis >= 10 * 1000 && dis < 20 * 1000) {
            zoomnum = 11 + num * 9;

        } else if (dis >= 20 * 1000 && dis < 25 * 1000) {
            zoomnum = 10 + num * 10;

        } else if (dis >= 25 * 1000 && dis < 50 * 1000) {
            zoomnum = 9 + num * 11;

        } else if (dis >= 50 * 1000 && dis < 100 * 1000) {
            zoomnum = 8 + num * 12;

        } else if (dis >= 100 * 1000 && dis < 200 * 1000) {
            zoomnum = 7 + num * 13;

        } else if (dis >= 200 * 1000 && dis < 500 * 1000) {
            zoomnum = 6 + num * 14;
        } else if (dis >= 500 * 1000 && dis < 1000 * 1000) {
            zoomnum = 5 + num * 15;
        } else if (dis >= 1000 * 1000 && dis < 2000 * 1000) {
            zoomnum = 4 + num * 16;
        }
        return zoomnum;
    }


    /**
     * 获取当前站点View
     *
     * @param number
     * @return
     */
    private View getStationView(Integer number) {
        View view;
        view = inflater.inflate(R.layout.car_location_icon_y, null);
        TextView tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
        if (number == null) {
            tv_car_number.setText("");
        } else {
            tv_car_number.setText(number + "");
        }

        return view;
    }

    /**
     * 获取bitmap
     *
     * @param
     * @param
     * @return
     */

    private Bitmap ViewToBitMap(View view, int with, int hight) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    /**
     * 获取站点图标View
     */
    private View getLocationView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        ImageView iv_guide = new ImageView(mContext);
        iv_guide.setImageResource(R.drawable.my_location);
//        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.addView(iv_guide);
        return layout;
    }

    /**
     * 获取站点图标View
     */
    private View getCarView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        ImageView iv_guide = new ImageView(mContext);
        iv_guide.setImageResource(R.drawable.location_car);
//        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.addView(iv_guide);
        return layout;
    }
}






