package com.dzrcx.jiaan.Main;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dzrcx.jiaan.Bean.CreateOrderVO;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.yyinterface.MyOrientationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by chenh on 2017/2/8.
 */

public class BaseBaiduMapFragment extends YYBaseFragment {

    // 定位相关
    public LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    //BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    public MapView mMapView;
    public BaiduMap mBaiduMap;

    // UI相关
    //OnCheckedChangeListener radioButtonListener;
    //Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位


    // 搜索相关
    RoutePlanSearch mSearch = RoutePlanSearch.newInstance();   // 搜索模块，也可去掉地图模块独立使用

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initLocationBaiduMap(View rootView, int viewId, CreateOrderVO createOrderVO) {
        initLoaction(rootView, viewId);//初始地图
        initOritationListener();//初始化方向传感器
        loadUserImage();//初始化用户头像
        initRoutePlanSearch(createOrderVO);
        addMarker(createOrderVO);
    }

    private void addMarker(CreateOrderVO createOrderVO) {
        LatLng ll = new LatLng(createOrderVO.getLat(), createOrderVO.getLng());
        OverlayOptions oo = new MarkerOptions().position(ll)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_station)).zIndex(9).draggable(false);
        mBaiduMap.addOverlay(oo);
    }

    private WalkingRouteOverlay currentOverlay = null; // 规划路线;

    public void initRoutePlanSearch(CreateOrderVO createOrderVO) {
        PlanNode stNode = PlanNode.withLocation(new LatLng(
                YYApplication.Latitude, YYApplication.Longitude));
        PlanNode enNode = PlanNode
                .withLocation(new LatLng(createOrderVO.getLat(), createOrderVO.getLng()));
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
                enNode));
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                currentOverlay = new MyWalkingRouteOverlay(mBaiduMap);
                RouteNode routeNode = result.getRouteLines().get(0).getAllStep().get(result.getRouteLines().get(0).getAllStep().size() - 1).getExit();
                mBaiduMap.setOnMarkerClickListener(currentOverlay);
                currentOverlay.setData(result.getRouteLines().get(0));
                currentOverlay.addToMap();
                currentOverlay.zoomToSpan();// 显示到屏幕适当的界面；
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

        });
    }

    // 定制RouteOverly
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getLineColor() {
            return Color.parseColor("" +
                    "#00ACFF");
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromBitmap(ViewToBitMap(new LinearLayout(mContext), 1, 1));
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromBitmap(ViewToBitMap(new LinearLayout(mContext), 1, 1));
        }
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
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    private void initLoaction(final View rootView, int viewId) {
        // 地图初始化
        mMapView = (MapView) rootView.findViewById(viewId);
        mMapView.showZoomControls(false);// 不显示缩放控件
        mMapView.showScaleControl(false);// 不显示比例尺
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mBaiduMap = mMapView.getMap();
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);//设置是否可以手势俯视模式
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


        //普通
//        mCurrentMode = LocationMode.FOLLOWING;
//        mBaiduMap
//                .setMyLocationConfigeration(new MyLocationConfiguration(
//                        mCurrentMode, true, mCurrentMarker));

//        //自定义mark
//        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
//
//        mBaiduMap
//                .setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
    }


    /**
     * 定位SDK监听函数
     */
    private int direction;

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())//这个是定位图层外面的圈圈
                    .accuracy(0)//这个是定位图层外面的圈圈 设置为0
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(direction)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 初始化方向传感器
     */
    private MyOrientationListener orientationListener;

    private void initOritationListener() {
        if (orientationListener == null) {
            orientationListener = new MyOrientationListener(
                    mContext);
        }
        orientationListener.start();
        orientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
//                        starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap2(
//                                getLocationView(null, x)));
                        direction = (int) x;
                        mBaiduMap.setMyLocationEnabled(true);
                        MyLocationData myLocationData = new MyLocationData.Builder().direction(direction).latitude(YYApplication.Latitude).longitude(YYApplication.Longitude).build();
                        mBaiduMap.setMyLocationData(myLocationData);
//                        Log.e("direction","x="+x);
//                        MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, starBitmapDescriptor);
//                        mBaiduMap.setMyLocationConfigeration(configeration);
                    }
                });
    }

    //获取头像图片
    private BitmapDescriptor starBitmapDescriptor = null;

    private void loadUserImage() {

        if (YYConstans.getUserInfoBean() != null && YYConstans.getUserInfoBean().getReturnContent().getUser() != null && YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb() != null) {

            ImageLoader.getInstance().loadImage(YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                            getLocationView(null, 0)));

                    MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, starBitmapDescriptor);
                    mBaiduMap.setMyLocationConfigeration(configeration);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                            getLocationView(bitmap, 0)));

                    MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, starBitmapDescriptor);
                    mBaiduMap.setMyLocationConfigeration(configeration);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
        } else {
            starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                    getLocationView(null, 0)));

            MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, starBitmapDescriptor);
            mBaiduMap.setMyLocationConfigeration(configeration);
        }
    }

    /**
     * 获取定位图标View
     */
    private View mLoctionView;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private View getLocationView(Bitmap bitmap, float x) {
//        if (mLoctionView == null) {
        mLoctionView = getActivity().getLayoutInflater().inflate(R.layout.item_mlocation, null);
//        }
//        Matrix matrix = new Matrix();
//        matrix.setRotate(-x);
//        ((ImageView) mLoctionView.findViewById(R.id.mlocation_user)).setRotation(0);
//        ((ImageView) mLoctionView.findViewById(R.id.mlocation_user)).setRotation(-x);
        if (bitmap == null) {
            ((ImageView) mLoctionView.findViewById(R.id.mlocation_user)).setImageResource(R.drawable.location_user_bg);
        } else {
            ((ImageView) mLoctionView.findViewById(R.id.mlocation_user)).setImageBitmap(bitmap);
        }

        return mLoctionView;
    }

    /**
     * @param view
     * @return
     */
    private Bitmap ViewToBitMapOritation(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
//    @Override
//    public void onPause() {
//        mMapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        mMapView.onResume();
//        super.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        // 退出时销毁定位
//        mLocClient.stop();
//        // 关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);
//        mMapView.onDestroy();
//        mMapView = null;
//        super.onDestroy();
//    }
}
