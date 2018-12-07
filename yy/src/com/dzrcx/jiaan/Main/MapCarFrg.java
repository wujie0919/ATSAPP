package com.dzrcx.jiaan.Main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzrcx.jiaan.Bean.CreateOrderVO;
import com.dzrcx.jiaan.Bean.EnterpriseCenterBean;
import com.dzrcx.jiaan.Bean.EnterpriseItemInfo;
import com.dzrcx.jiaan.Bean.MapcarListStationAndCarsVo;
import com.dzrcx.jiaan.Bean.MapcarfrgCarVo;
import com.dzrcx.jiaan.Bean.MapcarfrgCarlistReContentVo;
import com.dzrcx.jiaan.Bean.MapcarfrgOprateTimeReContentVo;
import com.dzrcx.jiaan.Bean.MapcarfrgOprateTimeVo;
import com.dzrcx.jiaan.Bean.MapcarfrgStationListReContentVo;
import com.dzrcx.jiaan.Bean.MapcarfrgStationVo;
import com.dzrcx.jiaan.Bean.OrderListBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Bean.RenterStateBean;
import com.dzrcx.jiaan.Bean.RenterStateReturnContentBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.Order.PayActivity;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.User.IdentificationAty;
import com.dzrcx.jiaan.User.LoginAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.MapCarFrgDialog;
import com.dzrcx.jiaan.widget.MapcarViewpager;
import com.dzrcx.jiaan.widget.YYOneButtonDialog;
import com.dzrcx.jiaan.yyinterface.ChangePageInterface;
import com.dzrcx.jiaan.yyinterface.MyOrientationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页车辆地图页
 */
public class MapCarFrg extends YYBaseFragment implements OnClickListener,
        OnMarkerClickListener, OnMapClickListener, OnGetRoutePlanResultListener, RequestInterface
        , ChangePageInterface, YYApplication.OnGetLocationlistener {
    private EnterpriseCenterBean enterPriseBean;

    private View mapCarFrgView;
    private LayoutInflater inflater;
    private MainActivity2_1 activity2_1;
    private ImageView iv_location;//定位按钮;
    /**
     * title内容
     */
    private ImageView iv_left, iv_right;
    private TextView tv_title;
    /**
     * 我的地理定位
     */
    private BitmapDescriptor bitmapDescriptor = null;
    private BitmapDescriptor starBitmapDescriptor = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private WalkingRouteOverlay currentOverlay = null; // 规划路线;
    /**
     * 顶部位置与距离信息+帮助;
     **/
    private RelativeLayout rl_top_location;//顶部站点详细地址容器；
    private TextView tv_morestation_remind;//点击查看附近站点;
    private TextView tv_location;//顶部站点详细地址；
    private TextView tv_time;//顶部到目的地的时间与米数；
    private TextView tv_content;//帮助中心内容；
    private RelativeLayout rl_content;//帮助入口提示信息;
    private RelativeLayout rl_help;//帮助布局容器;
    private View v_help_point;//帮助红点点击提示;
    /**
     * 底部有车无车layout
     **/
    private LinearLayout ll_bottom_lay;//底部有车无车容器
    private View view_havecar, view_nocarnoremind, view_loading, view_nocar_remind;//底部有车与无车layout以及加载中状态;
    private ProgressBar progress_circle;//加载状态框;view_loading
    private TextView progress_circle_message;//加载状态提示信息;
    private TextView tv_remindme, tv_nomindhavecarstation;//,没有车没有提醒时候的两个按钮（有车提醒我+看看有车站点）
    private TextView tv_cancelremindme, tv_mindhavecarstation, tv_remindmsg;//,没有车有提醒时候的两个按钮（有车提醒我+看看有车站点）
    private ImageView iv_rentthisnow;//有车时候立即租车按钮，
    private TextView tv_gavestyle;//,//站点还车类型
    private TextView tv_carnum;//,//n辆车可以租
    private TextView tv_stationname;//,//站点名称
    private MapcarViewpager vp_cardetail;
    private LinearLayout ll_rentthiscar;//立即租用按钮
    private LinearLayout ll_pointgroup;//point
    private ImageView[] pointArry;
    private int padding;
    private RelativeLayout rl_fastrentcar;//快速租车按钮
    private ImageView iv_fastrentcar;//快速租车按钮转转图片;
    /**
     * 底部正在用车layout
     */
    private RelativeLayout rl_iscaring; //底部正在用车layout 底部横条显示车辆状态布局
    private ImageView iv_carimg;//底部正在用车图片
    private TextView tv_carnumber;//底部正在用车车牌号码及类型
    private TextView tv_elctric;//底部正在用车剩余里程
    private TextView tv_order_statu;//底部正在用车订单状态
    /**
     * 其他
     */
    private Dialog remindTimeDlg;//提醒15分钟dialog
    private Dialog alertDlg;//提醒15分钟dialog
    private Dialog messageDlg;//提醒取消订单两次dialog
    private int ll_bottomlayHight;//下滑最大距离
    private int v_locationHight;//上滑最高距离
    private MapCarVPAdp mapCarVPAdp;
    private static final int TAG_ADDORDER = 16906;
    private static final int TAG_AUTHOR = 16907;
    private final int TAG_GETSTATIONCARLIST = 11001;
    private final int TAG_GETCARSBYIDS = 11002;
    private final int TAG_GETOPRATEREMAINTIME = 11003;
    private final int TAG_GETREMAINTIME = 11004;
    private final int TAG_GETHELP = 11005;
    private final int TAG_GETENTERPRISE = 11007;
    private final int TAG_ORDERLISTATY2_1 = 11008;
    private final int TAG_ADDCARREMIND = 11009;
    private final int TAG_GETUSERINFO = 11010;
    //private final int TAG_REQUESTRAILINFO = 11011;//获取电子围栏信息
    private List<Marker> otherCitycarStationMarkers;
    private List<Marker> currentCityStationMarkers;
    private Marker firstHaveCarMark;//有车的最近的站点VO;
    private Marker currentMark;//当前选中的站点;
    private boolean isClickMarker = true;  //底部layout是否弹出来
    private boolean ll_bottomIsVisible = false;  //底部layout是否弹出来
    OrderListBean listBean;

    private MyOrientationListener orientationListener;
    private View mLoctionView;
private int no_login_count = 1;

    private RenterStateReturnContentBean tempStateReturnContentBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.inflater = inflater;
        mapCarFrgView = inflater.inflate(R.layout.frg_mapcar_3_3, null);
        initView();//这里有网络请求更新页面状态
        moveToMiddle(YYApplication.Latitude, YYApplication.Longitude);
        requestLocationData(true);
        requestPageHelp(false);
        requestData(false);
//        requestRailInfo(true);
        return mapCarFrgView;
    }

    Runnable orderRunnable = new Runnable() {
        @Override
        public void run() {
            requestData(false);
        }
    };

    //获取订单信息
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("status", "0");
        params.put("pageNo", 1 + "");
        params.put("pageSize", "10");
        YYRunner.getData(TAG_ORDERLISTATY2_1, YYRunner.Method_POST,
                YYUrl.GETORDERLIST, params, this);
    }

//    /**
//     * 获取电子围栏数据
//     */
//    public void requestRailInfo(boolean isShowDialog){
//        if (NetHelper.checkNetwork(mContext)) {
//            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
//            return;
//        }
//        if (isShowDialog)
//            dialogShow();
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
//        params.put("userId", YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId()+"");
//        params.put("cityId", "9527");//这里先写死,稍后再讨论
//        YYRunner.getData(TAG_REQUESTRAILINFO, YYRunner.Method_POST,
//                YYUrl.GETRAILINFO, params, this);
//    }

    /**
     * 增加mark并显示在地图上
     *
     * @param latitude
     * @param longitude
     * @param bitmapDescriptor
     * @return Marker
     */

    private Marker addMarker(double latitude, double longitude,
                             BitmapDescriptor bitmapDescriptor, boolean isLocationMark) {
        LatLng ll = new LatLng(latitude, longitude);
        OverlayOptions oo = new MarkerOptions().position(ll)
                .icon(bitmapDescriptor).zIndex(9).draggable(false);
        Marker marker = null;
        if (isLocationMark) {
            mBaiduMap.setMyLocationEnabled(true);
            MyLocationData myLocationData = new MyLocationData.Builder().direction(100).latitude(latitude).longitude(longitude).build();
            mBaiduMap.setMyLocationData(myLocationData);
            MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
            mBaiduMap.setMyLocationConfigeration(configeration);
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("isLocationMark", isLocationMark);
//            marker.setExtraInfo(bundle);
        } else {
            marker = ((Marker) mBaiduMap.addOverlay(oo));
        }
        return marker;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
        requestData(false);
        YYApplication.setLocationIFC(this);//注册定位回调接口
        loadUserImage();
        showImageAnim(YYConstans.hasUnFinishOrder);
    }

    public void onRestart() {
//        requestLocationData(false);
//        initCreateStatu();
        requestStationData();
//        requestData(false);
    }

    ;

    /**
     * 开始规划路线
     *
     * @param Latitude
     * @param Longitude
     */
    private void planningRoute(double Latitude, double Longitude) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            return;
        }
        PlanNode stNode = PlanNode.withLocation(new LatLng(
                YYApplication.Latitude, YYApplication.Longitude));
        PlanNode enNode = PlanNode
                .withLocation(new LatLng(Latitude, Longitude));
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
                enNode));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (orientationListener != null) {
            orientationListener.stop();
            orientationListener = null;
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // mBaiduMap.setMyLocationEnabled(false);
//        activity2_1.addDataBackInterface(MapCarFrg.class.getName(), null);
        mMapView.onDestroy();
        mSearch.destroy();
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
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        activity2_1 = (MainActivity2_1) activity;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 10060) {
            initCreateStatu();
            YYOneButtonDialog oneButtonDialog = new YYOneButtonDialog(mContext, "提示", "下手太 慢了，车已被其他用户抢走了，下次一定要快哦~", "朕知道了");
            oneButtonDialog.setOnDialogClick(new YYOneButtonDialog.DialogClick() {
                @Override
                public void buttonClick(View v, Dialog dialog) {
                    initCreateStatu();
                }
            });
            oneButtonDialog.show();
        }
        if (resultCode == 10061 && requestCode == 10060) {
            initCreateStatu();
            YYOneButtonDialog oneButtonDialog = new YYOneButtonDialog(mContext, "提示", "下手太 慢了，车已被其他用户抢走了，下次一定要快哦~", "朕知道了");
            oneButtonDialog.setOnDialogClick(new YYOneButtonDialog.DialogClick() {
                @Override
                public void buttonClick(View v, Dialog dialog) {
                    initCreateStatu();
                }
            });
            oneButtonDialog.show();
        }

    }

//    //在activity里面调用 刷新页面的
//    public void fragmentOnRestart(){
//        Log.e("fragmentOnRestart","走了fragmentOnRestart");
//        initView();
//        moveToMiddle(YYApplication.Latitude, YYApplication.Longitude);
//        requestLocationData(true);
//        requestPageHelp(false);
//        requestData(false);
//    }


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
        if (menuVisible) {
            YYApplication.setLocationIFC(this);//注册定位回调接口
        } else {
            YYApplication.setLocationIFC(null);//注册定位回调接口
        }
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
        dismmisDialog();
        if (currentOverlay != null) {
            currentOverlay.removeFromMap();
        }
        if (!ll_bottomIsVisible) {
            return;
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            MyUtils.showToast(mContext, "抱歉，未找到路线结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            int juli = result.getRouteLines().get(0).getDistance();// 获取路线距离
            int time = result.getRouteLines().get(0).getDuration();
            currentOverlay = new MyWalkingRouteOverlay(mBaiduMap);
//            showInfowindow("<font color='#54585b'>" + "步行约" + "</font>" + "<font color='#13bb86'>" + juli + "</font>" + "<font color='#54585b'>" + "米,约" + "</font>"
//                    + "<font color='#13bb86'>" + time / 60 + "</font>" + "<font color='#54585b'>" + "分钟" + "</font>", new LatLng(
//                    YYApplication.Latitude, YYApplication.Longitude), 33);
            tv_time.setText("步行约" + juli + " 米,约" + (time / 60) + "分钟");
            RouteNode routeNode = result.getRouteLines().get(0).getAllStep().get(result.getRouteLines().get(0).getAllStep().size() - 1).getExit();
//            showNumAnim("共有" + currentMapcarfrgStationVo.getCarList().size() + "辆车可租", new LatLng(routeNode.getLocation().latitude, routeNode.getLocation().longitude), -2);
            mBaiduMap.setOnMarkerClickListener(currentOverlay);
            currentOverlay.setData(result.getRouteLines().get(0));
            currentOverlay.addToMap();
            currentOverlay.zoomToSpan();// 显示到屏幕适当的界面；
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(mBaiduMap
                    .getMapStatus().zoom - 1));
            baseHandler.postDelayed(new Runnable() {
                //
//                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    int y = MyUtils.px2dip(mContext, MyUtils.getScreenHeigth(mContext) - getStatusHeight(mContext) - ll_bottomlayHight) - 61 - 55;
                    Point point = new Point(
                            MyUtils.getScreenWidth(mContext) / 2, MyUtils.dip2px(mContext, (y / 2)) + ll_bottomlayHight);
                    LatLng latLng = mBaiduMap.getProjection()
                            .fromScreenLocation(point);
                    moveToMiddle(latLng.latitude, latLng.longitude);
                }
            }, 200);
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isLl_bottomIsVisible()) {
            initCreateStatu();
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void getBdlocation(BDLocation location) {
        if (location != null) {
            if (location.getLocType() == BDLocation.TypeNetWorkLocation
                    || location.getLocType() == BDLocation.TypeOffLineLocation
                    || location.getLocType() == BDLocation.TypeGpsLocation) {
                if (activity2_1.isTopActivity()) {
                    //Log.e("OnChange","走了getBdlocation");
                    requestStationData();
                }
            } else {
                dismmisDialog();
                MyUtils.showToast(mContext, "定位失败，请重试");
            }
        } else {
            dismmisDialog();
            MyUtils.showToast(mContext, "定位失败，请重试");
        }
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
     * 获取当天还可以取消订单次数
     *
     * @return
     */
    private int getCancelNum() {
        int num = 0;
        String data = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CANCELORDER_DATE, "");
        if (!TextUtils.isEmpty(data)) {
            String[] datas = data.split(";");
            if (datas != null && datas.length > 0) {
                String now = TimeDateUtil.formatTime(System.currentTimeMillis(), "yyyy-MM-dd");
                for (String s : datas) {
                    if (now.equals(s)) {
                        num++;
                    }
                }
                if (num == 0) {
                    SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CANCELORDER_DATE, "");
                }
            }
        }
        return num;
    }

    /**
     * 展示提醒时间dialog
     */
    public void showRemindTimeDialog(final String[] strArray) {
        if (remindTimeDlg == null) {
            remindTimeDlg = new Dialog(mContext, R.style.MyDialog);
        }
        View mDlgCallView = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_mapcarfrg_remindtime, null);
        final LinearLayout ll_time1 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time1);
        final LinearLayout ll_time2 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time2);
        final LinearLayout ll_time3 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time3);
        TextView tv_time1 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time1);
        TextView tv_time2 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time2);
        TextView tv_time3 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time3);
        tv_time1.setText(strArray[0] + "小时");
        ll_time1.setTag(strArray[0]);
        tv_time2.setText(strArray[1] + "小时");
        ll_time2.setTag(strArray[1]);
        tv_time3.setText(strArray[2] + "小时");
        ll_time3.setTag(strArray[2]);
        final TextView tv_left_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_left_txt);
        TextView tv_right_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_right_txt);
        ll_time1.setSelected(true);
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_time1:
                    case R.id.ll_time2:
                    case R.id.ll_time3:
                        reSetSelectedData(v);
                        break;
                    case R.id.tv_left_txt:
                        remindTimeDlg.dismiss();
                        break;
                    case R.id.tv_right_txt:
                        if (ll_time1.isSelected()) {
                            requestOprateremaintime(1, ll_time1.getTag() + "", false);
                        } else if (ll_time2.isSelected()) {
                            requestOprateremaintime(1, ll_time2.getTag() + "", false);
                        } else if (ll_time3.isSelected()) {
                            requestOprateremaintime(1, ll_time3.getTag() + "", false);
                        } else {
                            MyUtils.showToast(mContext, "请选择提醒时间");
                            break;
                        }
                        remindTimeDlg.dismiss();
                        break;
                }
            }

            private void reSetSelectedData(View v) {
                ll_time1.setSelected(false);
                ll_time2.setSelected(false);
                ll_time3.setSelected(false);
                v.setSelected(true);
            }

        };
        MyUtils.setViewsOnClick(onClickListener, ll_time1, ll_time2, ll_time3, tv_left_txt, tv_right_txt);
        remindTimeDlg.setCanceledOnTouchOutside(false);
        remindTimeDlg.setContentView(mDlgCallView);
        remindTimeDlg.show();
        Window dlgWindow = remindTimeDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 8;
        dlgWindow.setAttributes(lp);
    }

    /**
     * 展示取消订单超过两次dialog
     */
    public void showMessageDialog(String messge, String buttonstr) {
        if (messageDlg == null) {
            messageDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_message, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            tv_cancel_txt.setText(buttonstr);
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //  TODO Auto-generated method stub
                    messageDlg.dismiss();
                    if ("Identification".equals(activity)) {
                        mContext.finish();
                    }
                }
            });
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setContentView(mDlgCallView);
        }
        messageDlg.show();
        Window dlgWindow = messageDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
    }

    long tempTime_sure = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_top_location:
                activity2_1.changeToFragment(2);
                break;
            case R.id.ll_rentthiscar://点击立即用车的时候
                rentThisCarNow();
                break;
            case R.id.iv_carphoto_float:
                ArrayList<MapcarfrgCarVo> mapcarfrgCarVos = mapCarVPAdp.getCarsData();
                if (mapcarfrgCarVos == null) {
                    return;
                }
                MapcarfrgCarVo mapcarfrgCarVo = mapcarfrgCarVos.get(vp_cardetail.getCurrentItem());
                Bundle bundle = new Bundle();
                bundle.putString("CarMainPhoto", mapcarfrgCarVo.getCarMainPhoto());
                bundle.putString("Brand", mapcarfrgCarVo.getBrand() + mapcarfrgCarVo.getSeries());
                bundle.putDouble("DayPrice", mapcarfrgCarVo.getWorkDayPrice());
                bundle.putDouble("MileagePrice", mapcarfrgCarVo.getMileagePrice());
                bundle.putString("StartPrice", mapcarfrgCarVo.getStartPrice() + "");
                bundle.putString("Discount", MyUtils.formatPriceShort(mapcarfrgCarVo.getDiscount()).trim() + "");
                startActivity(ValuationAty.class, bundle);
                break;
            case R.id.iv_left:
                activity2_1.changeDrawerLayoutStatus();
                break;
            case R.id.iv_right:
                showCallDialog();
                break;
            case R.id.iv_location:
                requestLocationData(false);
                initCreateStatu();
                break;
            case R.id.rl_fastrentcar://底部原形汽车图标
            case R.id.tv_mindhavecarstation:
            case R.id.tv_nomindhavecarstation:
                if (currentCityStationMarkers == null || currentCityStationMarkers.size() == 0) {
                    showMessageDialog("您所在的城市还未开通站点，若有疑问，可以点击客服头像来帮忙哦~");
                    return;
                }
                if (firstHaveCarMark == null) {
                    final MapCarFrgDialog mapCarFrgDialog = new MapCarFrgDialog(mContext, "一会再来", "有车提醒", "今天来晚了，所有站点的车都在出租中。给您带来的不便，请您谅解，谢谢~", "温馨提示");
                    mapCarFrgDialog.setOnDialogClick(new MapCarFrgDialog.DialogClick() {
                        @Override
                        public void leftClick() {
                            mapCarFrgDialog.dismiss();
                        }

                        @Override
                        public void Rightclick() {
                            if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                                startActivity(LoginAty.class, null);
                            } else {
                                requesHomeRemind(true);
                                mapCarFrgDialog.dismiss();
                            }
                        }
                    });
                    mapCarFrgDialog.show();
                    return;
                }
                fillMarkerBottomData(firstHaveCarMark);
                break;
            case R.id.rl_help:
                SharedPreferenceTool.setPrefBoolean(mContext, "isclicked_help", true);
                setHelpStatu();
                rl_content.setVisibility(View.GONE);
                startActivity(HelpCenterAty.class, null);
                break;
            case R.id.tv_remindme:
                if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                    startActivity(LoginAty.class, null);
                } else {
                    requestTimes(true);
                }
                break;
            case R.id.tv_cancelremindme:
                if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                    startActivity(LoginAty.class, null);
                } else {
                    requestOprateremaintime(0, "", false);
                }
                break;
            case R.id.rl_iscaring://点击正在用车按钮的手 外面的布局
            case R.id.tv_order_statu://点击正在用车按钮的手 等待取车 等状态的按钮
//                if (YYConstans.currentOrderDetailVo != null) {
//                    switch (YYConstans.currentOrderDetailVo.getOrderState()) {
//                        case 1:
//                        case 2:
//                            Intent toOrder = new Intent(mContext, OrderAty.class);
//                            toOrder.putExtra("orderId", YYConstans.currentOrderDetailVo.getOrderId());
//                            mContext.startActivity(toOrder);
//                            mContext.overridePendingTransition(
//                                    R.anim.activity_up, R.anim.activity_push_no_anim);
//                            break;
//                        case 3:
//                            Bundle bundle3 = new Bundle();
//                            // 个人账号
//                            bundle3.putString("orderId",
//                                    YYConstans.currentOrderDetailVo.getOrderId());
//                            bundle3.putString("totalprice", YYConstans.currentOrderDetailVo.getFeeDetail().getTotalPrice() + "");
//                            bundle3.putString("allPrice", YYConstans.currentOrderDetailVo.getFeeDetail().getAllPrice() + "");
//                            bundle3.putString("benefitPrice", YYConstans.currentOrderDetailVo.getFeeDetail().getBenefitPrice() + "");
//                            bundle3.putString("payPrice", YYConstans.currentOrderDetailVo.getFeeDetail().getPayPrice() + "");
//                            startActivityForResult(PayActivity.class, bundle3, 1001);
//                            break;
//                    }
//                }
                if (listBean == null && listBean.getReturnContent() == null
                        && listBean.getReturnContent().getOrderList() == null
                        && listBean.getReturnContent().getOrderList().get(0) == null) {
                    return;
                }
                OrderListItemBean orderListItemBean = listBean.getReturnContent().getOrderList().get(0);
                if (orderListItemBean.getTimeMode() == 1 && orderListItemBean.getInCharge() == 0) {//日租并且没有进入到分时
                    //编写进行
                    switch (orderListItemBean.getDailyState()) {
                        case 1:
                            Intent toOrder = new Intent(mContext, DayShareActivity.class);
                            toOrder.putExtra("orderId", orderListItemBean.getOrderId());
                            toOrder.putExtra("PushDialogActivity", true);//是续租
                            toOrder.putExtra("isFromOrderList", true);//是待支付
                            mContext.startActivity(toOrder);
                            mContext.overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 2:
                            Intent toOrder1 = new Intent(mContext, OrderAty.class);
                            toOrder1.putExtra("orderId", orderListItemBean.getOrderId());
                            mContext.startActivity(toOrder1);
                            mContext.overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                    }
                } else {//时租状态 或者 日租进入分时
                    switch (orderListItemBean.getOrderState()) {
                        case 1://等待取车
                        case 2://正在用车
                            Intent toOrder1 = new Intent(mContext, OrderAty.class);
                            toOrder1.putExtra("orderId", orderListItemBean.getOrderId());
                            mContext.startActivity(toOrder1);
                            mContext.overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 3:
                            Bundle bundle3 = new Bundle();
                            // 个人账号
                            bundle3.putString("orderId",
                                    orderListItemBean.getOrderId() + "");
                            bundle3.putString("totalprice", orderListItemBean.getFeeDetail().getTotalPrice() + "");
                            bundle3.putString("allPrice", orderListItemBean.getFeeDetail().getAllPrice() + "");
                            bundle3.putString("benefitPrice", orderListItemBean.getFeeDetail().getBenefitPrice() + "");
                            bundle3.putString("payPrice", orderListItemBean.getFeeDetail().getPayPrice() + "");
                            startActivityForResult(PayActivity.class, bundle3, 1001);
                            break;

                    }
                }
                break;
            default:
                break;
        }
    }

    private MapcarfrgStationVo getMapcarfrgStationVo(Marker marker) {
        if (marker == null || marker.getExtraInfo() == null
                || !marker.getExtraInfo().containsKey("mapcarfrgStationVo")) {
            return new MapcarfrgStationVo();
        } else {
            return (MapcarfrgStationVo) marker.getExtraInfo().get("mapcarfrgStationVo");
        }
    }

    /*
    立即租用当前车辆 立即用车
    点击立即用车的时候 如果有未完成订单 就进入订单页面
     */
    private void rentThisCarNow() {
        long t = System.currentTimeMillis();
        if (t - tempTime_sure > 1000) {
            ArrayList<MapcarfrgCarVo> mapcarfrgCarVos = mapCarVPAdp.getCarsData();
            if (mapcarfrgCarVos == null) {
                return;
            }
            MapcarfrgCarVo mapcarfrgCarVo = mapcarfrgCarVos.get(vp_cardetail.getCurrentItem());
            MapcarfrgStationVo mapcarfrgStationVo = getMapcarfrgStationVo(currentMark);
            if (mapcarfrgCarVo == null || mapcarfrgStationVo == null) {
                return;
            }
            if (getCancelNum() >= 2) {
                showMessageDialog("抱歉，当天取消租车超过2次\n无法继续租车，明天恢复。", "明天再来");
                return;
            }
            if (NetHelper.checkNetwork(mContext)) {
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                return;
            }
            if (YYConstans.hasUnFinishOrder) {
                startActivity(OrderAty.class, null);
                MyUtils.showToast(mContext, "存在未完成订单");
                return;
            }
            if (mapcarfrgCarVo == null) {
                MyUtils.showToast(mContext, "数据错误");
                return;
            }
            if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                startActivity(LoginAty.class, null);
                return;
            }
            dialogShow();
            Map<String, String> map = new HashMap<String, String>();
            map.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(TAG_AUTHOR, YYRunner.Method_POST,
                    YYUrl.GETRENTERSTATE, map, MapCarFrg.this);
            createOrderVO = new CreateOrderVO();
            createOrderVO.setCarId(mapcarfrgCarVo.getId());
            createOrderVO.setFromStationId(mapcarfrgStationVo.getStationId());
            createOrderVO.setStayType(mapcarfrgStationVo.getStayType());
            createOrderVO.setFromStationName(mapcarfrgStationVo.getStationName());
            createOrderVO.setFromStationAddress(mapcarfrgStationVo.getAddress());
            createOrderVO.setDistance(mapcarfrgStationVo.getDistance());
            createOrderVO.setParkingNum(mapcarfrgStationVo.getParkingNum());
            createOrderVO.setCarId(mapcarfrgCarVo.getId());
            createOrderVO.setPricePerMileage(mapcarfrgCarVo.getMileagePrice());
            createOrderVO.setPrice(mapcarfrgCarVo.getWorkDayPrice());
            createOrderVO.setStartPrice(mapcarfrgCarVo.getStartPrice());
            createOrderVO.setDiscount(mapcarfrgCarVo.getDiscount());
            createOrderVO.setEv(mapcarfrgCarVo.getMileage());
            createOrderVO.setLat(mapcarfrgStationVo.getLatitude());
            createOrderVO.setLng(mapcarfrgStationVo.getLongitude());
            createOrderVO.setParkingNum(1);
            createOrderVO.setMake(mapcarfrgCarVo.getBrand());
            createOrderVO.setModel(mapcarfrgCarVo.getSeries());
            createOrderVO.setLicense(mapcarfrgCarVo.getLicense());
            createOrderVO.setDailyRentalPrice(mapcarfrgCarVo.getDailyRentalPrice());
            createOrderVO.setFreedomMultiple(mapcarfrgStationVo.getFreedomMultiple());//当更换为自由还车的时候的价钱倍数
            createOrderVO.setCanChangeFreedomMode(mapcarfrgStationVo.getCanChangeFreedomMode());//是否可以更换成自由还车类型
            carid = mapcarfrgCarVo.getId();
        } else {
        }
        tempTime_sure = t;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        fillMarkerBottomData(arg0);
        return false;
    }

    //底部
    private void fillMarkerBottomData(Marker arg0) {
        // TODO Auto-generated method stub
        if (arg0.getExtraInfo() == null
                || !arg0.getExtraInfo().containsKey("mapcarfrgStationVo"))//不带参数的站点图标不准有动作
            return;
        if (currentMark != null && getMapcarfrgStationVo(currentMark).getStationId() == getMapcarfrgStationVo(arg0).getStationId()) {//点击同一个 站点图标无操作
            return;
        }
        isClickMarker = true;
        clearLastMarkStatu();
        final MapcarfrgStationVo mapcarfrgStationVo = getMapcarfrgStationVo(arg0);
        if (mapcarfrgStationVo == null) {
            return;
        }
        /**
         * 切换图标
         */
        currentMark = arg0;
        currentMark.setIcon((BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                getStationView(mapcarfrgStationVo.getCarCount(), true), MyUtils.dip2px(mContext, 38),
                MyUtils.dip2px(mContext, 43)))));
        if (isLl_bottomIsVisible()) {
            startBottomDownUpAnim(200, 200);
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MarkToShowViewFromCarnum(mapcarfrgStationVo);
                }
            }, 200);
        } else {
            startBottomDownUpAnim(0, 200);
            MarkToShowViewFromCarnum(mapcarfrgStationVo);
        }
        setTopLocationstatu(isLl_bottomIsVisible());
//        rl_fastrentcar.setVisibility(View.GONE);没写
    }

    private void MarkToShowViewFromCarnum(MapcarfrgStationVo mapcarfrgStationVo) {
        bottomViewInit();
        if (mapcarfrgStationVo.getCarCount() == 0) {
            ToshowRemindViewFromFlag(mapcarfrgStationVo);
            if ((currentCityStationMarkers != null && currentCityStationMarkers.size() > 0) && !mapcarfrgStationVo.getCity().equals(getMapcarfrgStationVo(currentCityStationMarkers.get(0)).getCity())) {
                return;
            }
            planningRoute(mapcarfrgStationVo.getLatitude(), mapcarfrgStationVo.getLongitude());
        } else {
            requestCarlistData(mapcarfrgStationVo);
            if ((currentCityStationMarkers != null && currentCityStationMarkers.size() > 0) && !mapcarfrgStationVo.getCity().equals(getMapcarfrgStationVo(currentCityStationMarkers.get(0)).getCity())) {
                return;
            }
            planningRoute(mapcarfrgStationVo.getLatitude(), mapcarfrgStationVo.getLongitude());
        }
    }

    private void ToshowRemindViewFromFlag(MapcarfrgStationVo mapcarfrgStationVo) {
        if (mapcarfrgStationVo.getFlag() == 0) {
            addBottomView(view_nocarnoremind);
        } else if (mapcarfrgStationVo.getFlag() == 1) {
            addBottomView(view_nocar_remind);
            String msg = "<font color='#3D3F42'>在</font>" + "<font color='#04b575'>" + mapcarfrgStationVo.getRemainTimeDesc() + "</font>" + "<font color='#3D3F42'>之前，如有车我们将短信提醒您~</font>";
            tv_remindmsg.setText(Html.fromHtml(msg));
        }
    }


    private InfoWindow mInfoWindow, numAnimWindow;

    private View showInfoView, numAnimView;

    private void showInfowindow(String str, LatLng arg0, int padhight) {

        if (showInfoView == null) {
            showInfoView = LayoutInflater.from(mContext).inflate(
                    R.layout.showinfo_layout, null);
        }
        TextView textView = (TextView) showInfoView
                .findViewById(R.id.showinfo_text);
        // textView.setBackgroundResource(R.drawable.location_bg);
        int pading = MyUtils.dip2px(mContext, 5);
        textView.setPadding(pading, pading, pading, pading);
        textView.setText(Html.fromHtml(str));
        mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(showInfoView), arg0,
                -MyUtils.dip2px(mContext, padhight), null);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }


    /**
     * 动画的过程中强制刷新界面
     */
    AnimatorUpdateListener AnimatorUpdateListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
            ll_bottom_lay.postInvalidate();
            ll_bottom_lay.invalidate();
            iv_location.postInvalidate();
            iv_location.invalidate();
        }
    };

    private boolean runAble = true;

    /**
     * @描述 创建layout动画, 设置过程监听，设置动画时间
     */
    private ObjectAnimator getBottomLayAnim(int time, int distance) {
        ll_bottomIsVisible = distance < ll_bottomlayHight;
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_bottom_lay,
                "TranslationY", distance);
        if (animator == null) {
            return null;
        }
        animator.setDuration(time);
        animator.addUpdateListener(AnimatorUpdateListener);
        return animator;
    }

    /**
     * @描述 创建定位图标动画, 设置过程监听，设置动画时间
     */
    private ObjectAnimator getLocationAnim(int time, int distance) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_location,
                "TranslationY", distance);
        if (animator == null) {
            return null;
        }
        animator.setDuration(time);
        animator.addUpdateListener(AnimatorUpdateListener);
        return animator;
    }

    /**
     * 点击mark上下动画
     */
    private void startBottomDownUpAnim(int downTime, int upTime) {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animatorDown = getBottomLayAnim(downTime, ll_bottomlayHight);
        ObjectAnimator animatorUp = getBottomLayAnim(upTime, 0);
        animSet.play(animatorUp).after(
                animatorDown);
        animSet.start();
        startLocationDownUpAnim(downTime, upTime);
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showImageAnim(YYConstans.hasUnFinishOrder);
            }
        }, downTime + upTime);

    }

    /**
     * 定位图标上下动画
     */
    private void startLocationDownUpAnim(int downTime, int upTime) {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animatorDown = getLocationAnim(downTime, 0);
        ObjectAnimator animatorUp = getLocationAnim(upTime, v_locationHight);
        animSet.play(animatorUp).after(
                animatorDown);
        animSet.start();
    }


    /**
     * 底部layout下动画
     */
    private void startBottomDownAnim(int time, int bottomlaydistance, int locationDistance) {
        try {
            getBottomLayAnim(time, bottomlaydistance).start();
            getLocationAnim(time, locationDistance).start();
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showImageAnim(YYConstans.hasUnFinishOrder);
                }
            }, time);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 为所有站点添加marck
     */
    private void addMarkersAll(List<MapcarfrgStationVo> currentCityStationVos, List<MapcarfrgStationVo> OtherCityStationVos) {
        mBaiduMap.clear();
        if (otherCitycarStationMarkers == null) {
            otherCitycarStationMarkers = new ArrayList<Marker>();
        }
        if (currentCityStationMarkers == null) {
            currentCityStationMarkers = new ArrayList<Marker>();
        }
        otherCitycarStationMarkers.clear();
        currentCityStationMarkers.clear();

        this.firstHaveCarMark = null;//初始化第一个有车辆的站点
        for (int i = 0; currentCityStationVos != null && i < currentCityStationVos.size(); i++) {
            MapcarfrgStationVo mapcarfrgStationVo = currentCityStationVos.get(i);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getStationView(mapcarfrgStationVo.getCarCount(), false), MyUtils.dip2px(mContext, 38),
                    MyUtils.dip2px(mContext, 43)));
            Marker marker = addMarker(mapcarfrgStationVo.getLatitude(),
                    mapcarfrgStationVo.getLongitude(), bitmapDescriptor, false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("mapcarfrgStationVo", mapcarfrgStationVo);
            marker.setExtraInfo(bundle);
            currentCityStationMarkers.add(marker);
            if (this.firstHaveCarMark == null && mapcarfrgStationVo.getCarIds() != null && mapcarfrgStationVo.getCarIds().size() > 0) {
                this.firstHaveCarMark = marker;
            }
        }
        for (int i = 0; OtherCityStationVos != null && i < OtherCityStationVos.size(); i++) {
            MapcarfrgStationVo mapcarfrgStationVo = OtherCityStationVos.get(i);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getStationView(mapcarfrgStationVo.getCarCount(), false), MyUtils.dip2px(mContext, 38),
                    MyUtils.dip2px(mContext, 43)));
            Marker marker = addMarker(mapcarfrgStationVo.getLatitude(),
                    mapcarfrgStationVo.getLongitude(), bitmapDescriptor, false);
            Bundle bundle = new Bundle();
            bundle.putSerializable("mapcarfrgStationVo", mapcarfrgStationVo);
            marker.setExtraInfo(bundle);
            otherCitycarStationMarkers.add(marker);
        }
        addMarker(YYApplication.Latitude, YYApplication.Longitude,//保证定位图标在上层，防止被覆盖掉
                starBitmapDescriptor, true);
    }

    /**
     * 移动到以当前坐标为中心的画面
     *
     * @param latitude
     * @param longitude
     */
    private void moveToMiddle(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        // 移动到以当前坐标为中心的画面
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mMapView.getMap().animateMapStatus(u);
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        if (tag == TAG_GETCARSBYIDS || tag == TAG_GETOPRATEREMAINTIME) {
            progress_circle.setVisibility(View.GONE);
            progress_circle_message.setVisibility(View.VISIBLE);
            progress_circle_message.setText("数据传输错误，请重试");
        } else {
            dismmisDialog();
            MyUtils.showToast(mContext, "数据传输错误，请重试");
        }
    }

    /**
     * 通过更新用户信息来更新企业账号信息，判断企业账号是否余额不足
     */
    private void updateUserInfo() {
        dialogShow();
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("lng", YYApplication.Longitude + "");
        wxparams.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(TAG_GETUSERINFO, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, this);
    }


    private int carid;
    private int payMode, entId;
    private CreateOrderVO createOrderVO;

    /**
     * 15分钟取车提示dialog
     *
     * @param payModes
     * @param entIds
     */
    public void showOrderTimeDialog(int payModes, int entIds) {
        this.payMode = payModes;
        this.entId = entIds;
        if (alertDlg == null) {
            alertDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_login, null);
            TextView tv_title = (TextView) mDlgCallView
                    .findViewById(R.id.tv_title);
            tv_title.setText("提示");
            TextView tv_msg = (TextView) mDlgCallView.findViewById(R.id.tv_msg);
            tv_msg.setText("请在15分钟内取车，该时段不计费");
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("取消");
            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    alertDlg.dismiss();
                }
            });
            final TextView tv_login_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_login_txt);
            tv_login_txt.setText("确认");
            tv_login_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    dialogShow();
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("payMode", payMode + "");
                    if (payMode == 1) {
                        params.put("entId", entId + "");
                    }
                    params.put("fromStationId", createOrderVO.getFromStationId() + "");
                    params.put("carId", carid + "");
                    params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    YYRunner.getData(TAG_ADDORDER, YYRunner.Method_POST,
                            YYUrl.GETADDORDER, params, MapCarFrg.this);
                    alertDlg.dismiss();
                }
            });
            alertDlg.setCanceledOnTouchOutside(false);
            alertDlg.setContentView(mDlgCallView);
        }
        alertDlg.show();
        Window dlgWindow = alertDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 5 * 4;
        dlgWindow.setAttributes(lp);
    }

    private RenterStateReturnContentBean renterStateReturnContentBean;

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        dismmisDialog();
        switch (tag) {
            case TAG_AUTHOR:
                dismmisDialog();
                RenterStateBean renterStateBean = (RenterStateBean) GsonTransformUtil.fromJson(json, RenterStateBean.class);
                if (renterStateBean != null && renterStateBean.getErrno() == 0) {
                    renterStateReturnContentBean = renterStateBean.getReturnContent();
                    if (renterStateReturnContentBean != null && renterStateReturnContentBean.getUserState() == -1) {
                        startActivity(IdentificationAty.class, null);
                        MyUtils.showToast(mContext, "您未提交认证或认证正在审核");
                        return;
                    } else if (renterStateReturnContentBean != null && renterStateReturnContentBean.getUserState() == 0) {
                        showChooseDialog("");
                        // MyUtils.showToast(mContext, "您的资料正在审核");
                        return;
                    }
                    requestEnterprise();
                } else if (renterStateBean != null) {
                    MyUtils.showToast(mContext, renterStateBean.getError());
                }
//                json2State(json);
                break;
            case TAG_ADDORDER:
                dismmisDialog();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String errno = jsonObject.getString("errno");
                    if (!"0".equals(errno)) {
                        if ("10011".equals(errno)) {
//                            closeView();
                            startActivity(OrderAty.class, null);
                            getActivity().finish();
                        }
                        MyUtils.showToast(
                                mContext,
                                jsonObject.has("error") ? jsonObject
                                        .getString("error") : "服务器错误");
                    } else {
//                        closeView();
                        YYApplication.TAGorder = "notcomplete";
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isFirst", true);
                        startActivity(OrderAty.class, bundle);
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            case TAG_GETUSERINFO:
                dismmisDialog();
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
//                    showPayDialog(userInfoBean.getReturnContent().getUser().getEnterpriseList());
                }
                break;
            case TAG_ADDCARREMIND:
                dismmisDialog();
                YYBaseResBean yyBaseResBean = GsonTransformUtil
                        .fromJson(json, YYBaseResBean.class);
                if (yyBaseResBean != null && yyBaseResBean.getErrno() == 0) {
                    MyUtils.showToast(mContext, "5公里内有车我们将短信通知您");
                }
                break;
            case TAG_GETSTATIONCARLIST://获取站点车辆列表
                dismmisDialog();
                MapcarfrgStationListReContentVo mapcarfrgStationListReContentVo = (MapcarfrgStationListReContentVo) GsonTransformUtil.fromJson(json,
                        MapcarfrgStationListReContentVo.class);
                if (mapcarfrgStationListReContentVo == null || mapcarfrgStationListReContentVo.getErrno() != 0) {
                    showMessageDialog(mapcarfrgStationListReContentVo == null ? "数据传输错误，请重试" : mapcarfrgStationListReContentVo.getError() + "");
                } else {
                    if (mapcarfrgStationListReContentVo.getReturnContent() != null
                            && mapcarfrgStationListReContentVo.getReturnContent().getCurrentCity() != null
                            && mapcarfrgStationListReContentVo.getReturnContent().getOtherCity() != null
                            ) {
                        if (!ll_bottomIsVisible) {//底部正在显示时不刷新地图界面
                            initCreateStatu();
                            addMarkersAll(mapcarfrgStationListReContentVo.getReturnContent().getCurrentCity(), mapcarfrgStationListReContentVo.getReturnContent().getOtherCity());
                        }
                    }
                }
                break;
            case TAG_GETCARSBYIDS://车辆信息list获取
                MapcarfrgCarlistReContentVo
                        mapcarfrgCarlistReContentVo = (MapcarfrgCarlistReContentVo) GsonTransformUtil.fromJson(json,
                        MapcarfrgCarlistReContentVo.class);
                if (mapcarfrgCarlistReContentVo == null || mapcarfrgCarlistReContentVo.getErrno() != 0) {
                    boottomViewFail(mapcarfrgCarlistReContentVo == null ? "数据传输错误,请重试" : mapcarfrgCarlistReContentVo.getError());
                } else {
                    addBottomView(view_havecar);
                    AnimationDrawable animationDrawable = (AnimationDrawable) iv_rentthisnow.getBackground();
                    animationDrawable.start();
                    mapCarVPAdp.replace((ArrayList<MapcarfrgCarVo>) mapcarfrgCarlistReContentVo.getReturnContent());
                    mapCarVPAdp.notifyDataSetChanged();
                    initPoint();
                    setCurrentPoint(0);
                    vp_cardetail.setCurrentItem(0);
                    addBottomViewData(getMapcarfrgStationVo(currentMark));
                }
                break;
            case TAG_GETREMAINTIME:
                dismmisDialog();
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean == null || baseResBean.getErrno() != 0) {
                    MyUtils.showToast(mContext, baseResBean.getError());
                } else {
                    String[] strArray = null;
                    strArray = baseResBean.getError().split(","); //拆分字符为"," ,然后把结果交给数组strArray
                    showRemindTimeDialog(strArray);
                }
                break;
            case TAG_GETOPRATEREMAINTIME:
                MapcarfrgOprateTimeReContentVo mapcarfrgOprateTimeReContentVo = (MapcarfrgOprateTimeReContentVo) GsonTransformUtil.fromJson(json, MapcarfrgOprateTimeReContentVo.class);
                if (mapcarfrgOprateTimeReContentVo == null || mapcarfrgOprateTimeReContentVo.getErrno() != 0) {
                    boottomViewFail(mapcarfrgOprateTimeReContentVo == null ? "数据传输错误,请重试" : mapcarfrgOprateTimeReContentVo.getError());
                } else {
                    Bundle bundle = new Bundle();
                    if (currentMark != null) {
                        MapcarfrgStationVo mapcarfrgStationVo = getMapcarfrgStationVo(currentMark);
                        if (mapcarfrgStationVo.getStationId() != getMapcarfrgStationVo(currentMark).getStationId()) {
                            return;
                        }
                        if (mapcarfrgOprateTimeReContentVo.getReturnContent() != null) {
                            MapcarfrgOprateTimeVo mapcarfrgOprateTimeVo = mapcarfrgOprateTimeReContentVo.getReturnContent();
                            mapcarfrgStationVo.setFlag(mapcarfrgOprateTimeVo.getFlag());
                            mapcarfrgStationVo.setRemainTimeDesc(mapcarfrgOprateTimeVo.getRemainTimeDesc());
                            ToshowRemindViewFromFlag(mapcarfrgStationVo);
                        }
                        bundle.putSerializable("mapcarfrgStationVo", mapcarfrgStationVo);
                        currentMark.setExtraInfo(bundle);
                    }
                }
                break;
            case TAG_GETHELP:
                YYBaseResBean baseResBean2 = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean2 == null || baseResBean2.getErrno() != 0) {
                    rl_content.setVisibility(View.GONE);
                } else {
                    if (SharedPreferenceTool.getPrefString(mContext, "helpcontent", "").equals(baseResBean2.getError())) {
                        rl_content.setVisibility(View.GONE);
                    } else {
                        v_help_point.setVisibility(View.VISIBLE);
                        tv_content.setText(baseResBean2.getError());
                        rl_content.setVisibility(View.VISIBLE);
                        baseHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rl_content.setVisibility(View.GONE);
                            }
                        }, 10000);
                        SharedPreferenceTool.setPrefBoolean(mContext, "isclicked_help", false);
                        SharedPreferenceTool.setPrefString(mContext, "helpcontent", baseResBean2.getError());
                    }
                }
                break;
            case TAG_GETENTERPRISE://请求企业账户信息
                enterPriseBean = (EnterpriseCenterBean) GsonTransformUtil.fromJson(json, EnterpriseCenterBean.class);
                if (enterPriseBean != null && enterPriseBean.getErrno() == 0) {
                    if (enterPriseBean.getReturnContent() != null && enterPriseBean.getReturnContent().getEnterpriseCenterList() != null && enterPriseBean.getReturnContent().getEnterpriseCenterList().size() > 0) {
                        tempStateReturnContentBean = renterStateReturnContentBean;
                        updateUserInfo();

                        //showPayDialog(enterPriseBean.getReturnContent().getEnterpriseCenterList());
                        createOrderVO.setPayMode(0);
                        createOrderVO.setEntId(0);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("RenterStateReturnContentBean", tempStateReturnContentBean);
                        bundle.putSerializable("CreateOrderVO", createOrderVO);
                        bundle.putSerializable("enterPriseBean", enterPriseBean);
                        startActivityForResult(CreateOrderAty.class, bundle, 10060);
                    } else {
                        createOrderVO.setPayMode(0);
                        createOrderVO.setEntId(0);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CreateOrderVO", createOrderVO);
                        bundle.putSerializable("RenterStateReturnContentBean", renterStateReturnContentBean);
                        bundle.putSerializable("enterPriseBean", enterPriseBean);
                        startActivityForResult(CreateOrderAty.class, bundle, 10060);
                    }
                }
                break;
            case TAG_ORDERLISTATY2_1://订单列表
                listBean = (OrderListBean) GsonTransformUtil.fromJson(
                        json, OrderListBean.class);
                if (listBean == null || listBean.getErrno() != 0) {
//                    String errorr = "";
//                    String skey = YYConstans.getUserInfoBean().getReturnContent().getSkey();
//                    if(skey.isEmpty()){
//                        errorr = "您未登录";
//                    }else{
//                        errorr = listBean.getError();
//                    }
//                    MyUtils.showToast(mContext,
//                            listBean == null ? "数据传输错误，请重试" : "");
                    if(listBean == null){
                        MyUtils.showToast(mContext, "数据传输错误，请重试" );
                    }else{
                        if(no_login_count == 1){
                            MyUtils.showToast(mContext, listBean.getError() );
                            no_login_count ++ ;
                        }
                    }
                    return;
                }
                if (listBean.getReturnContent().getOrderList().size() > 0) {
                    baseHandler.postDelayed(orderRunnable, 60 * 1000);
                    fillCaringData(listBean.getReturnContent().getOrderList().get(0));
                } else {
                    baseHandler.removeCallbacks(orderRunnable);
                }
                break;
//            case TAG_REQUESTRAILINFO://获取电子围栏信息
//                Log.e("返回的json:",json);
//                break;
            default:
                break;
        }
    }

    private void addBottomViewData(MapcarfrgStationVo mapcarfrgStationVo) {
        switch (getMapcarfrgStationVo(currentMark).getStayType()) {
            case 1:
                tv_gavestyle.setText("原站还车");
                break;
            case 2:
                tv_gavestyle.setText("站点还车");
                break;
            case 3:
                tv_gavestyle.setText("自由还车");
                break;
        }
        tv_carnum.setText(mapCarVPAdp.getCount() + "辆车可用");
        tv_stationname.setText(mapcarfrgStationVo.getStationName());
    }

    /**
     * 可能是这里 判断是否进入订单页面 和根据订单状态进行显示
     * 增加一个状态
     * 1—等待取车
     * 2—用车中
     * 3—等待支付
     * 4—订单完成
     * 5—订单取消
     * 6—订单中止
     */
    private void fillCaringData(OrderListItemBean orderListItemBean) {
        if (!isLl_bottomIsVisible()) {
            rl_fastrentcar.setVisibility(View.GONE);//没写
            rl_iscaring.setVisibility(View.VISIBLE);
        } else {
            rl_fastrentcar.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().displayImage(orderListItemBean.getCarPhoto(),
                iv_carimg, YYOptions.Option_CARITEM);
        tv_carnumber.setText((orderListItemBean.getMake() + " " + orderListItemBean.getModel() + " · " + orderListItemBean.getLicense()));
        tv_elctric.setText(orderListItemBean.getLeftMileage() + "公里");

        if (orderListItemBean.getTimeMode() == 1 && orderListItemBean.getInCharge() == 0) {//日租并且没有进入到分时

            switch (orderListItemBean.getDailyState()) {
                case 1:
                    tv_order_statu.setEnabled(true);
                    tv_order_statu.setText("等待支付");
                    break;
                case 2:
                    tv_order_statu.setEnabled(true);
                    tv_order_statu.setText("正在用车");
                    break;
            }
        } else {//时租状态 或者 日租进入分时
            switch (orderListItemBean.getOrderState()) {
                case 1:
                    tv_order_statu.setText("等待取车");
                    tv_order_statu.setEnabled(true);
                    break;
                case 2:
                    tv_order_statu.setEnabled(true);
                    tv_order_statu.setText("正在用车");
                    break;
                case 3:
                    tv_order_statu.setEnabled(true);
                    tv_order_statu.setText("等待支付");
                    break;
                case 4:
                    tv_order_statu.setEnabled(false);
                    tv_order_statu.setText("订单完成");
                    break;
                case 5:
                    tv_order_statu.setEnabled(false);
                    tv_order_statu.setText("订单取消");
                    break;
                case 6:
                    tv_order_statu.setEnabled(false);
                    tv_order_statu.setText("订单中止");
                    break;
            }
        }

    }

    /**
     * 刚进页面时初始化
     */
    public void initCreateStatu() {
        startBottomDownAnim(200, ll_bottomlayHight, 0);
        setTopLocationstatu(isLl_bottomIsVisible());
        setHelpStatu();
        clearLastMarkStatu();
        moveToMiddle(YYApplication.Latitude, YYApplication.Longitude);
    }

    /**
     * 清除当前图标及路线状态
     */
    private void clearLastMarkStatu() {
        if (currentMark != null && currentMark.getExtraInfo() != null//点击下一个图标 更换上一个图标样式
                && currentMark.getExtraInfo().containsKey("mapcarfrgStationVo")) {
            MapcarfrgStationVo mapcarfrgStationVo = getMapcarfrgStationVo(currentMark);
            currentMark.setIcon((BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getStationView(mapcarfrgStationVo.getCarCount(), false), MyUtils.dip2px(mContext, 38),
                    MyUtils.dip2px(mContext, 43)))));
            currentMark = null;
        }
        if (currentOverlay != null) {//移除上一条路线
            currentOverlay.removeFromMap();
            currentOverlay = null;
        }
        mBaiduMap.hideInfoWindow();
    }

    private void setHelpStatu() {
        if (!SharedPreferenceTool.getPrefBoolean(mContext, "isclicked_help", false)) {
            v_help_point.setVisibility(View.VISIBLE);
        } else {
            v_help_point.setVisibility(View.GONE);
        }
    }

    /**
     * 根据是否有车两位置显示顶部位置布局的状态
     */
    private void setTopLocationstatu(boolean isLl_bottomIsVisible) {
        tv_time.setText("");
        tv_location.setText("");
        if (isLl_bottomIsVisible) {
            tv_location.setVisibility(View.VISIBLE);
            tv_location.setText(currentMark != null ? getMapcarfrgStationVo(currentMark).getAddress() : "");
            tv_time.setVisibility(View.VISIBLE);
            tv_morestation_remind.setVisibility(View.GONE);
        } else {
            tv_location.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
            tv_morestation_remind.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }


    @Override
    public void OnChange(MapcarListStationAndCarsVo mapcarListStationAndCarsVo, int carNo) {
        // TODO Auto-generated method stub
        //Log.e("OnChange","走了OnChange");
        isClickMarker = false;
        initCreateStatu();
        for (Marker marker : currentCityStationMarkers) {
            if (marker != null && getMapcarfrgStationVo(marker).getStationId() == mapcarListStationAndCarsVo.getStationId()) {//点击同一个 站点图标无操作
                //Log.e("OnChange","走了OnChange的if里面");
                marker.setIcon((BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                        getStationView(mapcarListStationAndCarsVo.getCarCount(), true), MyUtils.dip2px(mContext, 38),
                        MyUtils.dip2px(mContext, 43)))));
                currentMark = marker;
                startBottomDownUpAnim(0, 200);
                setTopLocationstatu(isLl_bottomIsVisible());
//                rl_fastrentcar.setVisibility(View.GONE);没写
                addBottomView(view_havecar);
                AnimationDrawable animationDrawable = (AnimationDrawable) iv_rentthisnow.getBackground();
                animationDrawable.start();
                mapCarVPAdp.replace((ArrayList<MapcarfrgCarVo>) mapcarListStationAndCarsVo.getCarList());
                mapCarVPAdp.notifyDataSetChanged();
                initPoint();
                setCurrentPoint(carNo);
                vp_cardetail.setCurrentItem(carNo);
                addBottomViewData(getMapcarfrgStationVo(currentMark));
                planningRoute(getMapcarfrgStationVo(marker).getLatitude(), getMapcarfrgStationVo(marker).getLongitude());
                break;
            } else {
                rl_fastrentcar.setVisibility(View.VISIBLE);
            }
        }
    }

    //请选择支付账户弹窗
    public void showPayDialog(ArrayList<EnterpriseItemInfo> enterpriseList) {
        Dialog payDialog = new Dialog(mContext, R.style.ActionSheet);
        View mDlgCallView = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_paystyle, null);
        LinearLayout accountLayout = (LinearLayout) mDlgCallView.findViewById(R.id.pay_dialog_company_layout);
        View[] accounts = new View[enterpriseList.size() + 1];
        PayStyleClickListener payStyleClickListener = new PayStyleClickListener(payDialog, accounts);
        View itemPayTempP = LayoutInflater.from(mContext).inflate(
                R.layout.item_pay_company_layout, null);
        ((TextView) itemPayTempP.findViewById(R.id.tv_businessname)).setText("使用个人余额账户");
        (itemPayTempP.findViewById(R.id.v_businessicon)).setBackgroundResource(R.drawable.mapcarfrg_personpay);

        accounts[0] = itemPayTempP;
        itemPayTempP.setOnClickListener(payStyleClickListener);
        itemPayTempP.setSelected(true);
        accountLayout.addView(itemPayTempP);
        for (int i = 0; i < enterpriseList.size(); i++) {

            View lineview = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyUtils.dip2px(mContext, 1));
            lineview.setLayoutParams(params);
            params.setMargins(MyUtils.dip2px(mContext, 14), 0, 0, 0);
            lineview.setBackgroundResource(R.color.line_eeeeee);
            accountLayout.addView(lineview);

            View itemPayTemp = LayoutInflater.from(mContext).inflate(
                    R.layout.item_pay_company_layout, null);
            accounts[i + 1] = itemPayTemp;
            itemPayTemp.setTag(enterpriseList.get(i));
            itemPayTemp.setOnClickListener(payStyleClickListener);
            ((TextView) itemPayTemp.findViewById(R.id.tv_businessname)).setText("使用" + enterpriseList.get(i).getName());

            if (enterpriseList.get(i).getBalanceOut() != 0) {
                (itemPayTemp.findViewById(R.id.tv_business_unenough)).setVisibility(View.GONE);
                ((TextView) itemPayTemp.findViewById(R.id.tv_businessname)).setWidth(MyUtils.dip2px(mContext, 140));
            } else {
                ((TextView) itemPayTemp.findViewById(R.id.tv_businessname)).setMaxWidth(MyUtils.dip2px(mContext, 205));
                (itemPayTemp.findViewById(R.id.tv_business_unenough)).setVisibility(View.GONE);
            }
            (itemPayTemp.findViewById(R.id.v_businessicon)).setBackgroundResource(R.drawable.mapcarfrg_businesspay);
            accountLayout.addView(itemPayTemp);
        }
        (mDlgCallView.findViewById(R.id.tv_left_txt)).setOnClickListener(payStyleClickListener);
        (mDlgCallView.findViewById(R.id.tv_right_txt)).setOnClickListener(payStyleClickListener);

        payDialog.setCancelable(true);
        payDialog.setContentView(mDlgCallView);
        payDialog.show();


        Window dlgWindow = payDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(activity2_1) / 10 * 8;
        dlgWindow.setAttributes(lp);
    }

    /**
     * 选择支付账户弹窗确定按钮点击事件
     * 点击支付方式选项的时候显示效果
     */
    private class PayStyleClickListener implements OnClickListener {
        private View[] accounts;
        private Dialog dialog;

        public PayStyleClickListener(Dialog dialog, View[] accounts) {
            this.accounts = accounts;
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_left_txt://取消按钮
                    dialog.dismiss();
                    break;
                case R.id.tv_right_txt://确定按钮
                    for (int i = 0; i < accounts.length; i++) {
                        if (accounts[i].isSelected()) {
                            EnterpriseItemInfo priseBean = (EnterpriseItemInfo) accounts[i].getTag();
                            if (priseBean == null) {
                                dialog.dismiss();
                                createOrderVO.setPayMode(0);
                                createOrderVO.setEntId(0);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("RenterStateReturnContentBean", tempStateReturnContentBean);
                                bundle.putSerializable("CreateOrderVO", createOrderVO);
                                bundle.putSerializable("enterPriseBean", enterPriseBean);
                                startActivityForResult(CreateOrderAty.class, bundle, 10060);

//                                if (createOrderVO.getStayType() != 3) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("CreateOrderVO", createOrderVO);
//                                    startActivity(CreateOrderAty.class, bundle);
//
//                                } else {
//                                    showOrderTimeDialog(0, 0);
//                                }

                            } else {
                                if (priseBean.getBalanceOut() == 0) {//企业余额是否充足
                                    dialog.dismiss();
                                    createOrderVO.setPayMode(1);
                                    createOrderVO.setEntId(priseBean.getId());

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("RenterStateReturnContentBean", tempStateReturnContentBean);
                                    bundle.putSerializable("CreateOrderVO", createOrderVO);
                                    bundle.putSerializable("enterPriseBean", enterPriseBean);
                                    startActivityForResult(CreateOrderAty.class, bundle, 10060);

                                } else {
                                    //欠费
                                    View toastLayout = inflater.inflate(R.layout.account_unenough_toast_layout, null);
                                    LinearLayout accout_not_enough_toast = (LinearLayout) toastLayout.findViewById(R.id.accout_not_enough_toast);
                                    accout_not_enough_toast.getLayoutParams().width = MyUtils.getScreenWidth(mContext);
                                    MyUtils.showToast(mContext, toastLayout, Gravity.TOP, MyUtils.dip2px(mContext, 55));
                                }
                            }
                        }
                    }
                    break;
                default:
                    for (int i = 0; i < accounts.length; i++) {
                        if (v == accounts[i]) {
                            accounts[i].setSelected(true);
                        } else {
                            accounts[i].setSelected(false);
                        }
                    }
                    break;
            }

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
        layout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    /**
     * @param view
     * @return
     */
    private Bitmap ViewToBitMapOritation(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    private void loadUserImage() {

        if (YYConstans.getUserInfoBean() != null && YYConstans.getUserInfoBean().getReturnContent().getUser() != null && YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb() != null) {

            ImageLoader.getInstance().loadImage(YYConstans.getUserInfoBean().getReturnContent().getUser().getThumb(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    iv_left.setImageResource(R.drawable.personal);
                    iv_left.setPadding(MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17));
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                            getLocationView(null, 0)));
                    iv_left.setImageResource(R.drawable.personal);
                    iv_left.setPadding(MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17));
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                            getLocationView(bitmap, 0)));
                    if (makeRoundCorner(bitmap) == null) {
                        iv_left.setImageResource(R.drawable.personal);
                        iv_left.setPadding(MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17));
                    } else {
                        iv_left.setImageBitmap(makeRoundCorner(bitmap));
                        iv_left.setPadding(MyUtils.dip2px(mContext, 13), MyUtils.dip2px(mContext, 13), MyUtils.dip2px(mContext, 13), MyUtils.dip2px(mContext, 13));
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    iv_left.setImageResource(R.drawable.personal);
                    iv_left.setPadding(MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17));
                }
            });
        } else {
            starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                    getLocationView(null, 0)));
            iv_left.setPadding(MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17), MyUtils.dip2px(mContext, 17));
            iv_left.setImageResource(R.drawable.personal);
        }
    }


    protected Bitmap makeRoundCorner(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        Bitmap output = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * 获取定位图标View
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private View getLocationView(Bitmap bitmap, float x) {
//        if (mLoctionView == null) {
        mLoctionView = inflater.inflate(R.layout.item_mlocation, null);
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
     * 获取当前站点View
     */
    private View getStationView(int number, boolean isSelected) {
        View view = inflater.inflate(R.layout.mapcarfrg_carlocation_icon_n, null);
        TextView tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
        ImageView iv_station_bg = (ImageView) view.findViewById(R.id.iv_station_bg);
        tv_car_number.setText(number + "");
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

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 显示跳转订单界面的快捷入口动画
     *
     * @param isShow
     */
    private void showImageAnim(boolean isShow) {
        requestData(false);
        if (isLl_bottomIsVisible()) {
            rl_iscaring.setVisibility(View.GONE);
            rl_fastrentcar.setVisibility(View.VISIBLE);
        } else {
            if (isShow) {
                requestData(false);
            } else {
                rl_fastrentcar.setVisibility(View.VISIBLE);
                rl_iscaring.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取底部ll_bottom是否弹出
     *
     * @return
     */
    public boolean isLl_bottomIsVisible() {
        return ll_bottomIsVisible;
    }

    /**
     * 初始化方向传感器
     */
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
                        mBaiduMap.setMyLocationEnabled(true);
                        MyLocationData myLocationData = new MyLocationData.Builder().direction(x).latitude(YYApplication.Latitude).longitude(YYApplication.Longitude).build();
                        mBaiduMap.setMyLocationData(myLocationData);

                        MyLocationConfiguration configeration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, starBitmapDescriptor);
                        mBaiduMap.setMyLocationConfigeration(configeration);


                    }
                });
    }

    //这里有网络请求更新页面状态
    private void initView() {
        initMapSet();
        initTitle();
        initTop();
        initBottomView();//这里有网络请求更新页面状态
        initCarIng();
        loadUserImage();
        MyUtils.setViewsOnClick(this, iv_left, iv_right, rl_top_location, rl_help,
                rl_fastrentcar, iv_location
                , rl_fastrentcar, tv_remindme, tv_nomindhavecarstation, tv_cancelremindme, tv_mindhavecarstation, ll_bottom_lay, rl_iscaring, ll_rentthiscar);
        initOritationListener();
        initCreateStatu();//这里有网络请求 更新页面状态

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.fastrentcar_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        iv_fastrentcar.startAnimation(operatingAnim);


    }

    private void initCarIng() {
        rl_iscaring = (RelativeLayout) mapCarFrgView.findViewById(R.id.rl_iscaring);
        iv_carimg = (ImageView) mapCarFrgView.findViewById(R.id.iv_carimg);
        tv_carnumber = (TextView) mapCarFrgView.findViewById(R.id.tv_carnumber);
        tv_elctric = (TextView) mapCarFrgView.findViewById(R.id.tv_elctric);
        tv_order_statu = (TextView) mapCarFrgView.findViewById(R.id.tv_order_statu);
        tv_order_statu.setOnClickListener(this);
    }
    private void initTitle() {
        tv_title = (TextView) mapCarFrgView.findViewById(R.id.tv_title);
        tv_title.setText(mContext.getResources().getString(
                R.string.app_name));

        iv_left = (ImageView) mapCarFrgView.findViewById(R.id.iv_left);
        iv_right = (ImageView) mapCarFrgView.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
    }

    private void initMapSet() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        mMapView = (MapView) mapCarFrgView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);// 不显示缩放控件
        mMapView.showScaleControl(false);// 不显示比例尺
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mBaiduMap = mMapView.getMap();
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(true);//设置是否可以手势俯视模式
        mBaiduMap.setOnMarkerClickListener(this);// 设置marker点击事件
        mBaiduMap.setOnMapClickListener(this);// 设置地图点击事件点击事件
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                .zoomTo(16f)); // 设定地图缩放比例百度地图缩放范围（3-19），12两公里
        starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMapOritation(
                getLocationView(null, 0)));
    }

    RelativeLayout rl_pager_parent;
    LinearLayout.LayoutParams params;
    ImageView iv_open_detail;

    private void initBottomView() {
        padding = MyUtils.dip2px(mContext, 3.5f);
        iv_location = (ImageView) mapCarFrgView.findViewById(R.id.iv_location);
        rl_fastrentcar = (RelativeLayout) mapCarFrgView.findViewById(R.id.rl_fastrentcar);
        iv_fastrentcar = (ImageView) mapCarFrgView.findViewById(R.id.iv_fastrentcar);

        ll_bottom_lay = (LinearLayout) mapCarFrgView.findViewById(R.id.ll_bottom_lay);
        view_nocar_remind = inflater.inflate(R.layout.mapcarfrg_bottom_nocar_remind, null);
        tv_cancelremindme = (TextView) view_nocar_remind.findViewById(R.id.tv_cancelremindme);
        tv_mindhavecarstation = (TextView) view_nocar_remind.findViewById(R.id.tv_mindhavecarstation);
        tv_remindmsg = (TextView) view_nocar_remind.findViewById(R.id.tv_remindmsg);
        view_havecar = inflater.inflate(R.layout.mapcarfrg_bottom_havecar, null);

        rl_pager_parent = (RelativeLayout) view_havecar.findViewById(R.id.rl_pager_parent);
        params = (LinearLayout.LayoutParams) rl_pager_parent.getLayoutParams();

        //下面的viewPager高度调整__________________________________________________________
        params.height = MyUtils.dip2px(getActivity(), 190);
        rl_pager_parent.setLayoutParams(params);
        iv_open_detail = (ImageView) view_havecar.findViewById(R.id.iv_open_detail);
        iv_open_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (params.height == MyUtils.dip2px(getActivity(), 190)) {
                    params.height = MyUtils.dip2px(getActivity(), 250);
                    rl_pager_parent.setLayoutParams(params);
                    iv_open_detail.setBackgroundResource(R.drawable.close_detail);
                } else {
                    params.height = MyUtils.dip2px(getActivity(), 190);
                    rl_pager_parent.setLayoutParams(params);
                    iv_open_detail.setBackgroundResource(R.drawable.open_detail);
                }
            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        ll_pointgroup = (LinearLayout) view_havecar.findViewById(R.id.ll_pointgroup);
        iv_rentthisnow = (ImageView) view_havecar.findViewById(R.id.iv_rentthisnow);


        tv_gavestyle = (TextView) view_havecar.findViewById(R.id.tv_gavestyle);
        tv_carnum = (TextView) view_havecar.findViewById(R.id.tv_carnum);
        tv_stationname = (TextView) view_havecar.findViewById(R.id.tv_stationname);
        ll_rentthiscar = (LinearLayout) view_havecar.findViewById(R.id.ll_rentthiscar);//立即用车图标
        vp_cardetail = (MapcarViewpager) view_havecar.findViewById(R.id.cvp_cardetail);
        view_loading = inflater.inflate(R.layout.mapcarfrg_bottom_progress, null);
        progress_circle = (ProgressBar) view_loading.findViewById(R.id.progress_circle);
        progress_circle_message = (TextView) view_loading.findViewById(R.id.progress_circle_message);
        view_nocarnoremind = inflater.inflate(R.layout.mapcarfrg_bottom_nocar_noremind, null);
        tv_remindme = (TextView) view_nocarnoremind.findViewById(R.id.tv_remindme);
        tv_nomindhavecarstation = (TextView) view_nocarnoremind.findViewById(R.id.tv_nomindhavecarstation);
        vp_cardetail.setOffscreenPageLimit(0);
        mapCarVPAdp = new MapCarVPAdp(getActivity(), null, this);
        vp_cardetail.setAdapter(mapCarVPAdp);
        vp_cardetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                 @Override
                                                 public void onPageScrolled(int i, float v, int i1) {

                                                 }

                                                 @Override
                                                 public void onPageSelected(int i) {
                                                     setCurrentPoint(i);
                                                 }

                                                 @Override
                                                 public void onPageScrollStateChanged(int i) {

                                                 }
                                             }

        );
        ll_bottomlayHight = MyUtils.dip2px(mContext, 315);//该数值只要大于实际控件高度隐藏就没问题
        v_locationHight = -(ll_bottomlayHight - MyUtils.dip2px(mContext, 110));//该数值只要大于实际控件高度隐藏就没问题
    }

    private void initTop() {
        rl_top_location = (RelativeLayout) mapCarFrgView.findViewById(R.id.rl_top_location);
        tv_location = (TextView) mapCarFrgView.findViewById(R.id.tv_location);
        tv_time = (TextView) mapCarFrgView.findViewById(R.id.tv_time);
        tv_morestation_remind = (TextView) mapCarFrgView.findViewById(R.id.tv_morestation_remind);
        rl_content = (RelativeLayout) mapCarFrgView.findViewById(R.id.rl_content);
        rl_content.setVisibility(View.GONE);
        tv_content = (TextView) mapCarFrgView.findViewById(R.id.tv_content);
        rl_help = (RelativeLayout) mapCarFrgView.findViewById(R.id.rl_help);
        v_help_point = mapCarFrgView.findViewById(R.id.v_help_point);
    }

    /**
     * 请求定位数据
     *
     * @param isShowDialog
     */
    public void requestLocationData(boolean isShowDialog) {
        //Log.e("OnChange", "走了requestLocationData");
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();

        if (YYApplication.mLocationClient.isStarted())
            YYApplication.mLocationClient.requestLocation();
        else {
            YYApplication.mLocationClient.start();
            YYApplication.mLocationClient.requestLocation();
        }
    }

    /**
     * 请求地图站点数量位置信息
     */
    public void requestStationData() {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("lng", YYApplication.Longitude + "");
        params.put("lat", YYApplication.Latitude + "");
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(TAG_GETSTATIONCARLIST, YYRunner.Method_POST,
                YYUrl.GETSTATIONCARLIST, params, this);
    }

    /**
     * 请求地图站点数量位置信息
     * 1提醒   ；0   取消
     *
     * @param isShowDialog
     */
    public void requestOprateremaintime(final int flag, final String time, final boolean isShowDialog) {
        startBottomDownUpAnim(200, 200);
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomViewInit();
                if (NetHelper.checkNetwork(mContext)) {
                    boottomViewFail("网络异常，请检查网络连接或重试");
                    return;
                }
                if (isShowDialog)
                    dialogShow();
                Map<String, String> params = new HashMap<String, String>();
                params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                params.put("remainTime", time);
                params.put("flag", flag + "");
                params.put("stationId", getMapcarfrgStationVo(currentMark).getStationId() + "");
                YYRunner.getData(TAG_GETOPRATEREMAINTIME, YYRunner.Method_POST,
                        YYUrl.GETOPRATEREMAINTIME, params, MapCarFrg.this);
            }
        }, 200);
    }

    /**
     * @param isShowDialog
     */
    private void requestTimes(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "remainTime");
        map.put("lng", YYApplication.Longitude + "");
        map.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(TAG_GETREMAINTIME, YYRunner.Method_POST, YYUrl.GETDOCUMENT, map,
                this);
    }

    /**
     * @param isShowDialog
     */
    private void requesHomeRemind(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        map.put("longitude", YYApplication.Longitude + "");
        map.put("latitude", YYApplication.Latitude + "");
        YYRunner.getData(TAG_ADDCARREMIND, YYRunner.Method_POST, YYUrl.ADDCARREMIND, map,//有车提醒
                this);
    }

    /**
     * @param isShowDialog
     */
    private void requestPageHelp(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "homePageHelp");
        map.put("lng", YYApplication.Longitude + "");
        map.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(TAG_GETHELP, YYRunner.Method_POST, YYUrl.GETDOCUMENT, map,
                this);
    }

    /**
     * 请求站点处车辆信息
     */
    public void requestCarlistData(MapcarfrgStationVo currentStaionVo) {
        if (NetHelper.checkNetwork(mContext)) {
            boottomViewFail("网络异常，请检查网络连接或重试");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        String idStr = currentStaionVo.getCarIds().toString();
        params.put("carIds", idStr.length() >= 2 ? idStr.substring(1, idStr.length() - 1) : "");
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(TAG_GETCARSBYIDS, YYRunner.Method_POST,
                YYUrl.GETCARSBYIDS, params, this);
    }

    /**
     * 请求企业账户信息
     */
    private void requestEnterprise() {
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(TAG_GETENTERPRISE, YYRunner.Method_POST,
                YYUrl.GETENTERPRISECENTER, params, this);
    }


    private void boottomViewFail(String msg) {
        addBottomView(view_loading);
        progress_circle.setVisibility(View.GONE);
        progress_circle_message.setVisibility(View.VISIBLE);
        progress_circle_message.setText(msg);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_rentthisnow.getBackground();
        animationDrawable.stop();
    }

    private void bottomViewInit() {
        addBottomView(view_loading);
        progress_circle.setVisibility(View.VISIBLE);
        progress_circle_message.setVisibility(View.GONE);
    }

    private void addBottomView(View view) {
        ll_bottom_lay.removeAllViews();
        ll_bottom_lay.addView(view);
    }

    private void initPoint() {
        ll_pointgroup.removeAllViews();
        pointArry = new ImageView[mapCarVPAdp.getCount()];
        for (int i = 0; i < pointArry.length; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams paramsPoint = new LinearLayout.LayoutParams(MyUtils.dip2px(mContext, 15), MyUtils.dip2px(mContext, 15));
            imageView.setLayoutParams(paramsPoint);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pointArry[i] = imageView;
            ll_pointgroup.addView(pointArry[i]);
        }
    }

    private void setCurrentPoint(int locationnum) {
        for (int i = 0; i < pointArry.length; i++) {
            if (i == locationnum) {
                pointArry[i].setPadding(padding, padding, padding, padding);
                pointArry[i].setImageResource(R.drawable.point_pre);
            } else {
                pointArry[i].setPadding(padding, padding, padding, padding);
                pointArry[i].setImageResource(R.drawable.point_n);
            }
        }
    }
}