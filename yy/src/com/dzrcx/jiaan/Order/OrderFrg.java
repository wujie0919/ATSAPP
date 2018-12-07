package com.dzrcx.jiaan.Order;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.BrandHelpMsgBean;
import com.dzrcx.jiaan.Bean.BrandHelpMsgVO;
import com.dzrcx.jiaan.Bean.GPSXY;
import com.dzrcx.jiaan.Bean.OrderBean;
import com.dzrcx.jiaan.Bean.OrderDetailBean;
import com.dzrcx.jiaan.Bean.OrderDetailVo;
import com.dzrcx.jiaan.Bean.OrderListBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Bean.ShareContentBeanReturnContent;
import com.dzrcx.jiaan.Bean.StationVo;
import com.dzrcx.jiaan.Bean.UpdateOrderStateBean;
import com.dzrcx.jiaan.Bean.XY;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Main.ChooseStationAty;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.MapAty;
import com.dzrcx.jiaan.User.FeelBackAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.distance.DistancePointVo;
import com.dzrcx.jiaan.speech.SpeakListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.LG;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.SpeechUtil;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.utils.TimeUtil;
import com.dzrcx.jiaan.widget.CarGaveDialog;
import com.dzrcx.jiaan.widget.MapCarFrgDialog;
import com.dzrcx.jiaan.widget.WarnDialog;
import com.dzrcx.jiaan.widget.YYOneButtonDialog;
import com.dzrcx.jiaan.yyinterface.FragmentCallActivity;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 进行中订单页面
 */
public class OrderFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface, SpeakListener, WarnDialog.DialogClick, CarGaveDialog.CheckCarOver {

    private OrderAty activity;
    private UpdateOrderStateBean stateBean;
    private View frgView;
    private TextView titleView, tv_give_back_car_time, tv_use_car_time;
    private ImageView iv_left_raw, iv_right_raw;
    //private PullToRefreshScrollView refreshScrollView;
    private ImageView speechImage;
    private TextView speechText;
    private View speechPoint;
    private MapView mapView;
    private TextView mapRef;
    private View backCarImage;
    private TextView backCarText, backCarSet;

    private RelativeLayout layoutChangeSet;//去这里

    private LinearLayout layoutOPen, layoutColsed, layouthWistle, layoutNavigate, ll_day_share, ll_time_share;

    private TextView orderButton, callServicePhone;

    private LinearLayout contentLinearLayout;
    private RelativeLayout contentRelativeLayout;

    private int nocomp = 1001;
    private static final int TAG_PAYENTERPRISE = 16902;// 企业订单支付
    private static final int TAG_NOTCOMPLETE = 16904;// 未完成订单
    private static final int TAG_CANCELORDER = 16906;// 更新订单
    private static final int TAG_GAVECAR = 16907;// 确认还车
    private static final int TAG_OPRATECAR = 16908;// c操作车辆
    private static final int TAG_OPENCAR = 16921;// c操作车辆
    private static final int TAG_LOCATION = 16909;// c操作车辆
    private static final int TAG_LOCATIONOnly = 16910;// c操作车辆
    private static final int TAG_BACKCAR = 16911;// c操作车辆
    private static final int TAG_CHECKENTPAY = 20021;//1.4　检测企业是否可支付
    private static final int TAG_PAYBYENTERPRISE = 20022;//企业支付
    private static final int TAG_PAYBYENTERPRISEAndPay = 20023;//企业支付
    private static final int TAG_UPDATEINFOANDPAY = 20024;//企业支付
    private static final int TAG_CHANGEBACKSTATION = 20030;//修改还车站点
    private Dialog backCarDlg, callDlg, stateDlg, sureGaveCarDialog;
//    private CarGuideDialog carGuideDialog;


    private OrderDetailVo orderDetailVo;

    private FragmentCallActivity fragmentCallActivity;
    private String parkLocAddress;

    private CarGaveDialog gaveDialog;
    boolean c = false;

    private CarUserDetailView userDetailView;

    private View mapCarView;

    private boolean needstartPushDialogActivity = true;

    /**
     * 帮助提示
     */
    private BrandHelpMsgVO brandHelpMsgVO;
    private boolean isFirst = false;
    public Handler orderFrgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 9999:

                    break;
                case 3001:
                    if (userDetailView != null) {
                        userDetailView.setLaterTime(-2);
                    }
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    };
    private Runnable upOrderRunnable = new Runnable() {

        @Override
        public void run() {
            updateOrderInfo(false);

        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = (OrderAty) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (frgView == null) {
            frgView = LayoutInflater.from(mContext).inflate(R.layout.frg_order,
                    null);
            initView();
        }

//        if (orderDetailVo != null) {
//            showData(orderDetailVo);
//        } else {
        //请求未完成订单
        if (!(TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey()))) {
            dialogShow();
            Map<String, String> params = new HashMap<String, String>();
            params.put("status", "0");
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(1001, YYRunner.Method_POST,
                    YYUrl.GETORDERLIST, params, this);
        }

//        }

        return frgView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        YYApplication.setLocationIFC(null);
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        orderFrgHandler.removeCallbacks(upOrderRunnable);
        mapView.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        ll_day_share = (LinearLayout) frgView.findViewById(R.id.ll_day_share);
        ll_time_share = (LinearLayout) frgView.findViewById(R.id.ll_time_share);
        iv_left_raw = (ImageView) frgView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        titleView = (TextView) frgView.findViewById(R.id.tv_title);
        tv_give_back_car_time = (TextView) frgView.findViewById(R.id.tv_give_back_car_time);
        tv_use_car_time = (TextView) frgView.findViewById(R.id.tv_use_car_time);
        titleView.setText("星辰出行");
        iv_right_raw = (ImageView) frgView.findViewById(R.id.iv_right);
        iv_right_raw.setImageResource(R.drawable.icon_checkcar);
//        refreshScrollView = (PullToRefreshScrollView) frgView
//                .findViewById(R.id.order_refreshsv);
//        refreshScrollView
//                .setOnRefreshListener(new OnRefreshListener<ScrollView>() {
//
//                    @Override
//                    public void onRefresh(
//                            PullToRefreshBase<ScrollView> refreshView) {
//                        // TODO Auto-generated method stub
//                        if (orderDetailVo != null) {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("orderId",
//                                    orderDetailVo.getOrderId() + "");
//                            params.put("skey", YYConstans.getUserInfoBean()
//                                    .getReturnContent().getSkey());
//                            YYRunner.getData(1002, YYRunner.Method_POST,
//                                    YYUrl.GETORDEDETAIL, params,
//                                    OrderFrg.this);
//                        } else {
//                            if (!(TextUtils.isEmpty(YYConstans
//                                    .getUserInfoBean().getReturnContent().getSkey()))) {
//                                dialogShow();
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("status", "0");
//                                params.put("skey", YYConstans
//                                        .getUserInfoBean().getReturnContent().getSkey());
//                                YYRunner.getData(nocomp,
//                                        YYRunner.Method_POST,
//                                        YYUrl.GETORDERLIST, params,
//                                        OrderFrg.this);
//                            }
//
//                        }
//                    }
//                });
        try {
            SpeechUtil.getInstance().setSpeakListener(this);
        } catch (Exception e) {

        }
        //SpeechUtil.getInstance().setSpeakListener(this);
        contentLinearLayout = (LinearLayout) frgView.findViewById(R.id.order_Linearlayout);
        contentRelativeLayout = (RelativeLayout) frgView.findViewById(R.id.order_relativelayout);

        c = false;
        contentLinearLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom > 1 && !c) {
                    ViewGroup.LayoutParams layoutParams = contentRelativeLayout.getLayoutParams();

                    if (layoutParams != null) {
                        c = true;
                        layoutParams.width = right - left;
                        layoutParams.height = bottom - top;
                        contentRelativeLayout.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        speechImage = (ImageView) frgView.findViewById(R.id.order_speech_iv);
        speechText = (TextView) frgView.findViewById(R.id.order_speech_text);
        speechPoint = frgView.findViewById(R.id.order_speech_messagepoint);

        mapView = (MapView) frgView.findViewById(R.id.order_baidumap);

        mapRef = (TextView) frgView.findViewById(R.id.order_frg_map_ref);


        backCarText = (TextView) frgView.findViewById(R.id.order_car_location_tv);
        backCarImage = frgView.findViewById(R.id.order_car_back_set_iv);
        backCarSet = (TextView) frgView.findViewById(R.id.order_car_back_set_tv);

        layoutChangeSet = (RelativeLayout) frgView.findViewById(R.id.order_car_location_layout);

        layoutOPen = (LinearLayout) frgView.findViewById(R.id.order_layout_open);
        layoutColsed = (LinearLayout) frgView.findViewById(R.id.order_layout_colsed);
        layouthWistle = (LinearLayout) frgView.findViewById(R.id.order_layout_wran);
        layoutNavigate = (LinearLayout) frgView.findViewById(R.id.order_layout_navgiate);

        orderButton = (TextView) frgView.findViewById(R.id.order_button);
        callServicePhone = (TextView) frgView.findViewById(R.id.order_callservice);


        userDetailView = new CarUserDetailView();
        userDetailView.setLeftLable0((TextView) frgView.findViewById(R.id.order_frg_map_lable0));
        userDetailView.setLeftLable1((TextView) frgView.findViewById(R.id.order_frg_map_lable1));
        userDetailView.setMapMin((TextView) frgView.findViewById(R.id.order_frg_map_min));
        userDetailView.setMapSec((TextView) frgView.findViewById(R.id.order_frg_map_sec));
        userDetailView.setMapYuan((TextView) frgView.findViewById(R.id.order_frg_map_yuan));


        mapView.showZoomControls(false);// 不显示缩放控件
        mapView.showScaleControl(false);// 不显示比例尺
        mapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mapView.setOnClickListener(null);
        BaiduMap baiduMap = mapView.getMap();
        UiSettings mUiSettings = baiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(true);//设置是否可以手势俯视模式
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setScrollGesturesEnabled(false);
        MapStatus mapStatus = new MapStatus.Builder().overlook(0).zoom(15).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mapStatus);

        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        baiduMap.animateMapStatus(u, 1000); // 设定地图缩放比例百度地图缩放范围（3-19），12两公里

        iv_left_raw.setOnClickListener(this);
        iv_right_raw.setOnClickListener(this);
        speechText.setOnClickListener(this);
        speechImage.setOnClickListener(this);
        mapRef.setOnClickListener(this);
        layoutChangeSet.setOnClickListener(this);
        layoutOPen.setOnClickListener(this);
        layoutColsed.setOnClickListener(this);
        layouthWistle.setOnClickListener(this);
        layoutNavigate.setOnClickListener(this);
        orderButton.setOnClickListener(this);
        callServicePhone.setOnClickListener(this);
        frgView.findViewById(R.id.order_callservice).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.iv_left_raw:
                startActivity(MainActivity2_1.class, null);
                getActivity().finish();
                break;

            case R.id.iv_right:
                Bundle bundlecar = new Bundle();
                bundlecar.putString("orderID", orderDetailVo.getOrderId());
                bundlecar.putString("photoScene", "1");
                startActivity(CheakCarAct.class, bundlecar);
                break;
            case R.id.order_speech_text:
            case R.id.order_speech_iv:
                speechPoint.setVisibility(View.GONE);
                String message = (String) speechImage.getTag();
                if (!TextUtils.isEmpty(message)) {
                    try {
                        SpeechUtil.getInstance().peechMessage(mContext, message);
                    } catch (Exception e) {

                    }
                    //SpeechUtil.getInstance().peechMessage(mContext, message);
                } else {

                }
                break;
            case R.id.order_frg_map_ref:
                updateOrderInfo(true);
                break;
            case R.id.order_button:
                if (orderDetailVo != null) {

                    if (orderDetailVo.getTimeMode() == 1 && orderDetailVo.getOrderState() == 1) {//如果是日租
                        orderDetailVo.setOrderState(2);//设置可以直接还车
                    }

                    switch (orderDetailVo.getOrderState()) {
                        case 1:// 取消订单
                            showCancelOrderDialog();
                            break;
                        case 2:// 确认还车o申请还车

//                            if (!orderDetailVo.getOrderId().equals(SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER_BACK, ""))) {
//                                if (!TextUtils.isEmpty(brandHelpMsgVO.getEndOrderMsg())) {
//                                    SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getEndOrderMsg());
//                                }
//                            }
//                            SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER_BACK, orderDetailVo.getOrderId());
                            requestData(false);
                            if (gaveDialog == null) {
                                showBackCarDialog();
                            } else {
                                if (gaveDialog == null) {
                                    showGaveDialog();
                                    return;
                                } else if (gaveDialog.getStatus() != 0) {
                                    showGaveDialog();
                                    return;
                                }

                                showSureGaveCarDialog(mContext);
                            }
                            break;
                        case 3:// 支付租金
                            Bundle bundle = new Bundle();
                            if (orderDetailVo.getOrderType() == 1) {

                                //先检查企业账户余额
                                checkEntPay();
                            } else {
                                // 个人账号
                                bundle.putString("orderId",
                                        orderDetailVo.getOrderId());
                                bundle.putString("totalprice", orderDetailVo
                                        .getFeeDetail().getTotalPrice() + "");

                                bundle.putString("allPrice", orderDetailVo
                                        .getFeeDetail().getAllPrice() + "");
                                bundle.putString("benefitPrice", orderDetailVo
                                        .getFeeDetail().getBenefitPrice() + "");
                                bundle.putString("payPrice", orderDetailVo
                                        .getFeeDetail().getPayPrice() + "");
                                Intent intent = new Intent(getActivity(), PayActivity.class);
                                intent.putExtras(bundle);
                                getActivity().startActivityForResult(intent, 1001);
                                //startActivityForResult(PayActivity.class, bundle, 1001);
                            }
                            break;
                        case 4:


                            if (gaveDialog == null) {
                                showGaveDialog();
                                return;
                            } else if (gaveDialog.getStatus() != 0) {
                                showGaveDialog();
                                return;
                            }

                            startActivity(MainActivity2_1.class, null);
                            getActivity().finish();
                            break;

                    }
                }
                break;
            case R.id.order_layout_open:
                if (orderDetailVo != null) {
                    requestOprateCar(TAG_OPENCAR, "openCar", 1);
                    MobclickAgent.onEvent(mContext, "click_opencar");
                }
                break;
            case R.id.order_layout_colsed:
                if (orderDetailVo != null) {
                    requestOprateCar(TAG_OPRATECAR, "closeCar", 2);
                    MobclickAgent.onEvent(mContext, "click_closecar");
                }
                break;
            case R.id.order_layout_wran:
                if (orderDetailVo != null) {
                    requestOprateCar(TAG_OPRATECAR, "findCar", 3);
                    MobclickAgent.onEvent(mContext, "click_findcar");
                }
                break;
            case R.id.order_layout_navgiate:
                if (orderDetailVo != null && orderDetailVo.getFromXY() != null) {
                    backCar();
                }
                break;
//            case R.id.order_backcar_layout:
//                backCar();
//                break;
            case R.id.order_car_location_layout:
                XY xy = (XY) v.getTag();
                if (xy != null) {
                    toGetCarStation(xy);
                } else {
                    startActivityForResult(ChooseStationAty.class, null, 2001);
                }
                break;
            case R.id.order_callservice:
                showCallDialog();
                break;

            default:
                break;
        }

    }

    @Override
    public void onError(int tag, String error) {
        //refreshScrollView.onRefreshComplete();
        dismmisDialog();
        layoutOPen.setClickable(true);
        layoutColsed.setClickable(true);
        layouthWistle.setClickable(true);
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        switch (tag) {
            case 1001://获取未完成订单信息
                LG.d(json);
                OrderListBean listBean = (OrderListBean) GsonTransformUtil
                        .fromJson(json, OrderListBean.class);

                if (listBean != null && listBean.getReturnContent() != null && listBean.getReturnContent().getOrderList() != null) {
                    if (listBean.getReturnContent().getOrderList().size() > 0) {
                        OrderListItemBean notCompleteBean = listBean.getReturnContent().getOrderList()
                                .get(0);

                        parkLocAddress = notCompleteBean.getParkLocAddress();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("orderId", notCompleteBean.getOrderId() + "");
                        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                        YYRunner.getData(1002, YYRunner.Method_POST,
                                YYUrl.GETORDEDETAIL, params, this);
                    } else {
                        //refreshScrollView.onRefreshComplete();
                        dismmisDialog();

                        MyUtils.showToast(mContext, "订单已取消");
                        YYConstans.hasUnFinishOrder = false;
                        startActivity(MainActivity2_1.class, null);
                        baseHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().finish();
                            }
                        }, 100);
                    }
                } else {
                    //refreshScrollView.onRefreshComplete();
                    dismmisDialog();

                    MyUtils.showToast(mContext, "没有订单");
                    YYConstans.hasUnFinishOrder = false;
                    startActivity(MainActivity2_1.class, null);
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 100);
                }
                break;

            case 1002://原来的 刚进入页面的时候 获取订单
                dismmisDialog();
                //refreshScrollView.onRefreshComplete();
                OrderDetailBean orderDetailBean = (OrderDetailBean) GsonTransformUtil.fromJson(
                        json, OrderDetailBean.class);
                if (orderDetailBean != null && orderDetailBean.getErrno() == 0) {
                    orderDetailVo = orderDetailBean.getReturnContent();
                    YYConstans.currentOrderDetailVo = orderDetailVo;
                    showData(orderDetailVo);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }
                //Toast.makeText(getActivity(),"获取信息 剩余时间是:"+orderDetailBean.getReturnContent().getReletRemainingSeconds(),Toast.LENGTH_SHORT).show();
                //Log.e("orderid",orderDetailBean.getReturnContent().getOrderId());
                //如果是1 提示还剩30分钟日租还车 如果是0 不提示
                if (needstartPushDialogActivity == true && orderDetailBean.getReturnContent().getReletRemainingSeconds() > 0) {
                    //Toast.makeText(getActivity(),"准备弹窗",Toast.LENGTH_SHORT).show();
                    needstartPushDialogActivity = false;//needstartPushDialogActivity == true &&
//                    Intent intent = new Intent(getActivity(), PushDialogActivity.class);
//                    intent.putExtra("costTime",orderDetailBean.getReturnContent().getReletRemainingSeconds());
//                    getActivity().startActivity(intent);

                    Intent intent = new Intent();
                    intent.setAction("pushBroadcastReceiver");
                    intent.putExtra("getReletRemainingSeconds",orderDetailBean.getReturnContent().getReletRemainingSeconds());
                    getActivity().sendBroadcast(intent);
                }

                if (orderDetailVo.getTimeMode() == 1) {//如果是日租
                    orderDetailVo.setOrderState(2);//直接可以申请还车

                    if(orderDetailVo.getInCharge() == 1){//是否进入超时
                        ll_time_share.setVisibility(View.VISIBLE);
                        ll_day_share.setVisibility(View.GONE);
                        tv_give_back_car_time.setText("还车时间: " + TimeUtil.getSpecifiedDay(orderDetailVo.getCreateTime(), orderDetailVo.getRentedDayNumber()) + "");
                        tv_use_car_time.setText("使用时间: " + TimeUtil.formatTime(orderDetailVo.getCurrentTime() - orderDetailVo.getCreateTime()));
                    }else{

                        ll_time_share.setVisibility(View.GONE);
                        ll_day_share.setVisibility(View.VISIBLE);
                        tv_give_back_car_time.setText("还车时间: " + TimeUtil.getSpecifiedDay(orderDetailVo.getCreateTime(), orderDetailVo.getRentedDayNumber()) + "");
                        tv_use_car_time.setText("使用时间: " + TimeUtil.formatTime(orderDetailVo.getCurrentTime() - orderDetailVo.getCreateTime()));
                    }
                } else {//如果是时租

                    ll_time_share.setVisibility(View.VISIBLE);
                    ll_day_share.setVisibility(View.GONE);
                }

                break;
            case 2002://添加的 申请还车的时候 获取订单的结果
                dismmisDialog();
                //refreshScrollView.onRefreshComplete();
                OrderDetailBean orderDetailBean2 = (OrderDetailBean) GsonTransformUtil.fromJson(
                        json, OrderDetailBean.class);
                if (orderDetailBean2 != null && orderDetailBean2.getErrno() == 0) {
                    orderDetailVo = orderDetailBean2.getReturnContent();
                    YYConstans.currentOrderDetailVo = orderDetailVo;
                    //showData(orderDetailVo);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }

                if (orderDetailVo.getTimeMode() == 1) {//是日租
                    if (orderDetailVo.getInCharge() == 0) {//是否进入分时0没进入
                        //4是订单完成 tag是2003
                        Log.e("orderDetailVo", "没进入分时 完成日租订单");
                        requestUpdataOrderState(4, 2003);
                    } else if (orderDetailVo.getInCharge() == 1) {//是否进入分时 1进入
                        //3是待支付 tag是2003
                        Log.e("orderDetailVo", "日租进入了分时");
                        requestUpdataOrderState(3, TAG_GAVECAR);
                    }

                } else {//是分时
                    //更改订单状态为待支付
                    requestUpdataOrderState(3, TAG_GAVECAR);
                }

                break;
            case 2003://更改订单状态
                if (json2State(json, "")) {
                    YYApplication.TAGorder = "notcomplete";
                    YYConstans.currentOrderDetailVo = null;
                    YYConstans.hasUnFinishOrder = false;
                    Intent intent = new Intent(getActivity(), RatedUsAty.class);
                    intent.putExtra("isDayShare",true);
                    intent.putExtra("OrderDetailVo", orderDetailVo);
                    startActivity(intent);
                }
                break;
            case TAG_OPRATECAR:
                dismmisDialog();
                json2State(json, "opratecar");
                layoutOPen.setClickable(true);
                layoutColsed.setClickable(true);
                layouthWistle.setClickable(true);
                break;
            case TAG_OPENCAR://打开车门
                dismmisDialog();
                if (json2State(json, "opratecar")) {
                    if (!(orderDetailVo.getModel() + orderDetailVo.getMake()).equals(SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER, ""))) {
                        if (!TextUtils.isEmpty(brandHelpMsgVO.getUseMsg())) {
                            try {
                                SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getUseMsg());
                            } catch (Exception e) {

                            }
                            //  SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getUseMsg());

                        }
                    }
                    if (orderDetailVo.getFeeDetail().getPickcarTime() == 0) {
                        if (System.currentTimeMillis() - orderDetailVo.getFeeDetail().getChargeTime() > 0) {
                            //开始计费
                            iv_right_raw.setVisibility(View.GONE);
                            final MapCarFrgDialog orderDialog = new MapCarFrgDialog(mContext, "车辆完好", "验车拍照",
                                    "打开车门视为您已成功取车，在使用车辆前，" +
                                            "请先仔细检查车辆是否存在损坏并上传车辆相应位置照片以保证您的用车权益，" +
                                            "在检查车辆同时，也请您留意车辆周边是否存在安全隐患，并作出及时处理，在此祝您用车愉快~", "取车提示");
                            orderDialog.setOnDialogClick(new MapCarFrgDialog.DialogClick() {
                                @Override
                                public void leftClick() {
                                    orderDialog.dismiss();
                                }

                                @Override
                                public void Rightclick() {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderDetailVo.getOrderId());
                                    bundle.putString("photoScene", "1");
                                    startActivity(CheakCarAct.class, bundle);
                                    orderDialog.dismiss();
                                }
                            });
                            orderDialog.show();
                        } else {
                            iv_right_raw.setVisibility(View.GONE);
                            final MapCarFrgDialog orderDialog = new MapCarFrgDialog(mContext, "车辆完好", "验车拍照",
                                    "打开车门视为您已成功取车，计费也将开始，在使用车辆前，" +
                                            "请先仔细检查车辆是否存在损坏，并上传车辆相应位置照片以保证您的用车权益，" +
                                            "在检查车辆同时，也请您留意车辆周边是否存在安全隐患，并作出及时处理，在此祝您用车愉快~", "取车提示");
                            orderDialog.setOnDialogClick(new MapCarFrgDialog.DialogClick() {
                                @Override
                                public void leftClick() {
                                    orderDialog.dismiss();
                                }

                                @Override
                                public void Rightclick() {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderID", orderDetailVo.getOrderId());
                                    bundle.putString("photoScene", "1");
                                    startActivity(CheakCarAct.class, bundle);
                                    orderDialog.dismiss();
                                }
                            });
                            orderDialog.show();
                        }
                    }
                    speechText.setText("启动不了车辆，点我语音告诉您~");
                    speechImage.setTag(brandHelpMsgVO.getUseMsg());

                    SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER, (orderDetailVo.getModel() + orderDetailVo.getMake()));
                }
                layoutOPen.setClickable(true);
                layoutColsed.setClickable(true);
                layouthWistle.setClickable(true);

                //还车导航
//                layoutNavigate.setClickable(true);
//                layoutNavigate.setSelected(true);
//                layoutNavigate.setOnClickListener(this);
                break;
            case TAG_LOCATION:
                dismmisDialog();
                json2location(json, true);

                break;
            case TAG_CANCELORDER:
                dismmisDialog();
                if (json2State(json, "")) {//更改订单状态 5取消订单
                    addCancelNum();
                    YYConstans.hasUnFinishOrder = false;
                    YYConstans.currentOrderDetailVo = null;
                    //之前先跳转到地图页
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putString("orderId", orderDetailVo.getOrderId() + "");
                    startActivity(FeelBackAty.class, bundle);
                    activity.finish();
                }
                break;
            case TAG_GAVECAR://更改订单状态 3等等支付
                dismmisDialog();
                if (json2State(json, "")) {
                    stateBean = (UpdateOrderStateBean) GsonTransformUtil.fromJson(
                            json, UpdateOrderStateBean.class);
                    YYConstans.currentOrderDetailVo = orderDetailVo;
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", orderDetailVo.getOrderId());
                    Intent intent1 = new Intent(getActivity(), PayActivity.class);
                    intent1.putExtras(bundle);
                    getActivity().startActivity(intent1);
                    //startActivity(PayActivity.class, bundle);
                    getActivity().finish();

                }
                break;
            case TAG_PAYENTERPRISE:
                dismmisDialog();
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json,
                        YYBaseResBean.class);
                if (baseResBean.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            baseResBean.getErrno() != -1 ? baseResBean.getError()
                                    : "服务器错误");
                } else {
                    MyUtils.showToast(mContext, "支付成功");
                    startActivity(MainActivity2_1.class, null);
                    activity.finish();
                }

                break;
            case TAG_LOCATIONOnly:
                json2location(json, false);
                break;
            case TAG_BACKCAR:
                backCar();
                break;
            case TAG_CHECKENTPAY:
                UpdateOrderStateBean bean = (UpdateOrderStateBean) GsonTransformUtil.fromJson(
                        json, UpdateOrderStateBean.class);

                if (bean != null && bean.getErrno() == 0 && bean.getReturnContent() != null) {
                    if (bean.getReturnContent().isPaySucceed()) {
                        //支付
                        dialogShow();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                        params.put("orderId", orderDetailVo.getOrderId());
                        YYRunner.getData(TAG_PAYBYENTERPRISE, YYRunner.Method_POST,
                                YYUrl.PAYBYENTERPRISE, params, this);
                    } else {
                        //弹窗
                        dismmisDialog();
                        WarnDialog warnDialog = new WarnDialog(mContext, "稍后再说", "现在支付", bean.getReturnContent().getDesc(), this);
                        warnDialog.show();

                    }
                } else {
                    dismmisDialog();
                }


                break;
            case TAG_PAYBYENTERPRISE:
                dismmisDialog();
                YYBaseResBean bean1 = GsonTransformUtil.fromJson(
                        json, YYBaseResBean.class);
                if (bean1 != null && bean1.getErrno() == 0) {
                    YYConstans.hasUnFinishOrder = false;
                    Bundle bundle = new Bundle();
//                    bundle.putInt("type", 2);
                    bundle.putString("orderId", orderDetailVo.getOrderId() + "");
//                    startActivity(FeelBackAty.class, bundle);
                    bundle.putString("imageurl", orderDetailVo.getCarPhoto());
                    Intent intent1 = new Intent(getActivity(), RatedUsAty.class);
                    intent1.putExtras(bundle);
                    getActivity().startActivity(intent1);
//                    startActivity(RatedUsAty.class, bundle);
                    getActivity().finish();
                } else if (bean1 != null) {
                    MyUtils.showToast(mContext, bean1.getError());
                }
                break;
            case TAG_PAYBYENTERPRISEAndPay:
                dismmisDialog();
                YYBaseResBean bean2 = GsonTransformUtil.fromJson(
                        json, YYBaseResBean.class);
                if (bean2 != null && bean2.getErrno() == 0) {

                    if (orderDetailVo != null) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("orderId", orderDetailVo.getOrderId() + "");
                        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                        YYRunner.getData(TAG_UPDATEINFOANDPAY, YYRunner.Method_POST,
                                YYUrl.GETORDEDETAIL, params, OrderFrg.this);
                    }

                } else if (bean2 != null) {
                    MyUtils.showToast(mContext, bean2.getError());
                }
                break;
            case TAG_UPDATEINFOANDPAY:
                dismmisDialog();
                //refreshScrollView.onRefreshComplete();
                orderDetailBean = (OrderDetailBean) GsonTransformUtil.fromJson(
                        json, OrderDetailBean.class);
                if (orderDetailBean != null && orderDetailBean.getErrno() == 0) {
                    showData(orderDetailVo);


                    Bundle bundle = new Bundle();
                    // 个人账号
                    bundle.putString("orderId",
                            orderDetailBean.getReturnContent().getOrderId());
                    bundle.putString("totalprice", orderDetailBean
                            .getReturnContent().getFeeDetail().getTotalPrice() + "");

                    bundle.putString("allPrice", orderDetailBean
                            .getReturnContent().getFeeDetail().getAllPrice() + "");
                    bundle.putString("benefitPrice", orderDetailBean
                            .getReturnContent().getFeeDetail().getBenefitPrice() + "");
                    bundle.putString("payPrice", orderDetailBean
                            .getReturnContent().getFeeDetail().getPayPrice() + "");
                    Intent intent2 = new Intent(getActivity(), PayActivity.class);
                    intent2.putExtras(bundle);
                    getActivity().startActivityForResult(intent2, 1001);
                    //startActivityForResult(PayActivity.class, bundle, 1001);


                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }
                break;


            case TAG_CHANGEBACKSTATION:
                YYBaseResBean changeStation = GsonTransformUtil.fromJson(
                        json, YYBaseResBean.class);
                dismmisDialog();
                if (changeStation != null && changeStation.getErrno() == 0) {
                    if (backCarText.getTag() != null) {
                        StationVo stationVo = (StationVo) backCarText.getTag();
                        backCarText.setText("还车位置：" + stationVo.getName());
                        XY xy = new XY();
                        GPSXY gpsxy = new GPSXY();
                        gpsxy.setLat(stationVo.getLatitude());
                        gpsxy.setLng(stationVo.getLongitude());
                        xy.setGpsXY(gpsxy);
                        xy.setLocation(stationVo.getName());
                        orderDetailVo.setToXY(xy);
                        MyUtils.showToast(mContext, "站点修改成功");
                    }
                } else {
                    MyUtils.showToast(mContext, "站点修改失败");
                }
                break;

            case 8001:
                ShareContentBeanReturnContent shareContentBeanReturnContent = (ShareContentBeanReturnContent) GsonTransformUtil.fromJson(json, ShareContentBeanReturnContent.class);

                if (shareContentBeanReturnContent != null && shareContentBeanReturnContent.getErrno() == 0) {
                    YYConstans.shareContentBeanReturnContent = shareContentBeanReturnContent;
                }
                break;

            default:
                break;
        }

    }

    public void setOrderDetailVo(OrderDetailVo orderDetailVo) {
        this.orderDetailVo = orderDetailVo;
        YYConstans.currentOrderDetailVo = this.orderDetailVo;
    }

    @SuppressLint("NewApi")
    private ArrayList<OrderBean> json2OrderList(String json) {
        if (!json.isEmpty()) {
            ArrayList<OrderBean> beans = new ArrayList<OrderBean>();
            try {
                JSONObject jsonObject = new JSONObject(json);
                String errno = jsonObject.getString("errno");
                if (!"0".equals(errno)) {
                    MyUtils.showToast(
                            mContext,
                            jsonObject.has("error") ? jsonObject
                                    .getString("error") : "服务器错误");
                    return beans;
                }
                JSONArray jsonArray = jsonObject.getJSONArray("orderList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                    String rectPrice = jsonObject2.getString("rectPrice");
                    String chargeTime = jsonObject2.getString("chargeTime");
                    String orderState = jsonObject2.getString("orderState");
                    String model = jsonObject2.getString("model");
                    String pickcarTime = jsonObject2.getString("pickcarTime");
                    String renterName = jsonObject2.getString("renterName");
                    String carThumb = jsonObject2.getString("carThumb");
                    String plate = jsonObject2.getString("plate");
                    String parkLocAddress = jsonObject2
                            .getString("parkLocAddress");
                    String make = jsonObject2.getString("make");
                    String orderId = jsonObject2.getString("orderId");
                    String totalPrice = jsonObject2.getString("totalPrice");
                    beans.add(new OrderBean(chargeTime, renterName, model,
                            orderState, totalPrice, parkLocAddress, rectPrice,
                            orderId, make, plate, carThumb, pickcarTime));
                }
                return beans;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean json2State(String json, String opratecar) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String errno = jsonObject.getString("errno");
            if (!"0".equals(errno)) {
                MyUtils.showToast(mContext,
                        jsonObject.has("error") ? jsonObject.getString("error")
                                : "服务器错误");
            } else {// 此处待定有问题
                if ("opratecar".equals(opratecar)) {
                    MyUtils.showToast(mContext, jsonObject.getString("returnContent"));
                    MobclickAgent.onEvent(mContext, "fault_opencar");
                    updateOrderInfo(false);
                } else if ("closeCar".equals(opratecar)) {
                    MyUtils.showToast(mContext, jsonObject.getString("returnContent"));
                    MobclickAgent.onEvent(mContext, "fault_closecar");
                } else if ("findCar".equals(opratecar)) {
                    MyUtils.showToast(mContext, jsonObject.getString("returnContent"));
                    MobclickAgent.onEvent(mContext, "fault_findcar");
                }
                return true;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public void json2location(String json, boolean jump) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String errno = jsonObject.getString("errno");
            if (!"0".equals(errno)) {
                MobclickAgent.onEvent(mContext, "fault_carlocation");
            } else {
                JSONObject object = jsonObject.getJSONObject("returnContent");
                DistancePointVo pointVo = new DistancePointVo();
                String lat = object.getString("lat");
                String lng = object.getString("lng");

                pointVo.setTargetLng(Double.parseDouble(lng));
                pointVo.setTargetLat(Double.parseDouble(lat));
                pointVo.setTarAdrrName(object.getString("location"));

                pointVo.setLocationLng(YYApplication.Longitude);
                pointVo.setLocationLat(YYApplication.Latitude);
                pointVo.setLocaAdrrName(YYApplication.LocaAdrrName);

                if (jump) {
                    Intent intent = new Intent(mContext, MapAty.class);
                    intent.putExtra("name", "orderfrg");
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lng);
                    intent.putExtra("location", parkLocAddress);
//                    intent.putExtra("pointVo", pointVo);
                    Gson gson = new Gson();
                    String strJson = gson.toJson(pointVo);
                    intent.putExtra("pointVo", strJson);
                    startActivity(intent);
//                    ((Activity) mContext).overridePendingTransition(
//                            R.anim.activity_up, R.anim.activity_push_no_anim);
                } else {
                    LatLng latLng = new LatLng(Double.parseDouble(lat),
                            Double.parseDouble(lng));
                    YYConstans.setUseCarLng(latLng);
                    if (fragmentCallActivity != null) {
                        fragmentCallActivity.onCall(1, null);
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 还车导航
     */
    public void backCar() {
        DistancePointVo pointVo = new DistancePointVo();

        pointVo.setLocationLng(YYApplication.Longitude);
        pointVo.setLocationLat(YYApplication.Latitude);
        pointVo.setLocaAdrrName(YYApplication.LocaAdrrName);

        pointVo.setTargetLng(orderDetailVo.getToXY().getGpsXY().getLng());
        pointVo.setTargetLat(orderDetailVo.getToXY().getGpsXY().getLat());
        pointVo.setTarAdrrName(orderDetailVo.getToXY().getLocation());

        Intent intent = new Intent(mContext, MapAty.class);
        intent.putExtra("name", "orderfrg");
        intent.putExtra("latitude", orderDetailVo.getToXY().getGpsXY().getLat() + "");
        intent.putExtra("longitude", orderDetailVo.getToXY().getGpsXY().getLng() + "");
        intent.putExtra("location", parkLocAddress);
//        intent.putExtra("pointVo", pointVo);
        Gson gson = new Gson();
        String strJson = gson.toJson(pointVo);
        intent.putExtra("pointVo", strJson);
        intent.putExtra("isBackCar", true);
        startActivity(intent);

    }

    /**
     * 还车导航
     */
    public void toGetCarStation() {
        DistancePointVo pointVo = new DistancePointVo();
        pointVo.setLocationLng(YYApplication.Longitude);
        pointVo.setLocationLat(YYApplication.Latitude);
        pointVo.setLocaAdrrName(YYApplication.LocaAdrrName);

        pointVo.setTargetLng(orderDetailVo.getCurrentXY().getGpsXY().getLng());
        pointVo.setTargetLat(orderDetailVo.getCurrentXY().getGpsXY().getLat());
        pointVo.setTarAdrrName(orderDetailVo.getCurrentXY().getLocation());

        Intent intent = new Intent(mContext, MapAty.class);
        intent.putExtra("name", "orderfrg");
        intent.putExtra("latitude", orderDetailVo.getCurrentXY().getGpsXY().getLat() + "");
        intent.putExtra("longitude", orderDetailVo.getCurrentXY().getGpsXY().getLng() + "");
        intent.putExtra("location", orderDetailVo.getCurrentXY().getLocation());
//        intent.putExtra("pointVo", pointVo);
        Gson gson = new Gson();
        String strJson = gson.toJson(pointVo);
        intent.putExtra("pointVo", strJson);
        intent.putExtra("isBackCar", false);
        startActivity(intent);

    }

    /**
     * 还车导航
     */
    public void toGetCarStation(XY xy) {

        if (xy == null) {
            return;
        }
        DistancePointVo pointVo = new DistancePointVo();

        pointVo.setLocationLng(YYApplication.Longitude);
        pointVo.setLocationLat(YYApplication.Latitude);
        pointVo.setLocaAdrrName(YYApplication.LocaAdrrName);

        pointVo.setTargetLng(xy.getGpsXY().getLng());
        pointVo.setTargetLat(xy.getGpsXY().getLat());
        pointVo.setTarAdrrName(xy.getLocation());

        Intent intent = new Intent(mContext, MapAty.class);
        intent.putExtra("name", "orderfrg");
        intent.putExtra("latitude", xy.getGpsXY().getLat() + "");
        intent.putExtra("longitude", xy.getGpsXY().getLng() + "");
        intent.putExtra("location", xy.getLocation());
        Gson gson = new Gson();
        String strJson = gson.toJson(pointVo);
        intent.putExtra("pointVo", strJson);
//        intent.putExtra("pointVo", pointVo);
        intent.putExtra("isBackCar", false);
        startActivity(intent);

    }


    private void checkEntPay() {
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("orderId", orderDetailVo.getOrderId());
        YYRunner.getData(TAG_CHECKENTPAY, YYRunner.Method_POST,
                YYUrl.CHECKENTPAY, params, this);
    }


    public void requestOprateCar(int tag, String op, int type) {
        if (NetHelper.checkNetwork(mContext)) {
            showNoNetDlg();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        switch (type) {
            case 1:
                dialogShow("正在打开车门");
                layoutOPen.setClickable(false);
                baseHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutOPen.setClickable(true);
                    }
                }, 8000);
                break;
            case 2:
                dialogShow("正在关闭车门");
                layoutColsed.setClickable(false);
                layoutColsed.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutColsed.setClickable(true);
                    }
                }, 8000);
                break;
            case 3:
                dialogShow("正在鸣笛找车");
                layouthWistle.setClickable(false);
                layouthWistle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layouthWistle.setClickable(true);
                    }
                }, 8000);
                break;
            case 4:
                dialogShow("正在车辆定位");
                break;

            default:
                dialogShow();
                break;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("orderId", orderDetailVo.getOrderId());
        params.put("op", op);
        YYRunner.getData(tag, YYRunner.Method_POST, YYUrl.GETOPERATECAR,
                params, this);
    }

    public void requestOprateCar() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("orderId", orderDetailVo.getOrderId());
        params.put("op", "getExistGpsInfo");
        YYRunner.getData(TAG_LOCATIONOnly, YYRunner.Method_POST,
                YYUrl.GETOPERATECAR, params, this);
    }

    /**
     * 唤起更新
     */
    private void awakeUP() {
        orderFrgHandler.removeCallbacks(upOrderRunnable);
        orderFrgHandler.postDelayed(upOrderRunnable, 60 * 1000);
    }


    //查询订单信息
    private void updateOrderInfo(boolean showDialog) {
        if (orderDetailVo != null) {
            if (showDialog) {
                dialogShow();
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("orderId", orderDetailVo.getOrderId() + "");
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(1002, YYRunner.Method_POST,
                    YYUrl.GETORDEDETAIL, params, OrderFrg.this);
        }
    }

    //查询订单信息
    private void updateOrderInfoDayUseCar(boolean showDialog) {
        if (orderDetailVo != null) {
            if (showDialog) {
                dialogShow();
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("orderId", orderDetailVo.getOrderId() + "");
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(2002, YYRunner.Method_POST,
                    YYUrl.GETORDEDETAIL, params, OrderFrg.this);
        }
    }

    private void showData(OrderDetailVo orderBean) {
        if (orderBean == null) {
            return;
        }

        if (orderDetailVo.getTimeMode() == 1) {//如果是日租
            orderDetailVo.setOrderState(2);//直接可以申请还车

            if(orderDetailVo.getInCharge() == 1){//是否进入超时
                ll_time_share.setVisibility(View.GONE);
                ll_day_share.setVisibility(View.VISIBLE);
                tv_give_back_car_time.setText("还车时间: " + TimeUtil.getSpecifiedDay(orderDetailVo.getCreateTime(), orderDetailVo.getRentedDayNumber()) + "");
                tv_use_car_time.setText("使用时间: " + TimeUtil.formatTime(orderDetailVo.getCurrentTime() - orderDetailVo.getCreateTime()));
            }else{

                ll_time_share.setVisibility(View.VISIBLE);
                ll_day_share.setVisibility(View.GONE);
                tv_give_back_car_time.setText("还车时间: " + TimeUtil.getSpecifiedDay(orderDetailVo.getCreateTime(), orderDetailVo.getRentedDayNumber()) + "");
                tv_use_car_time.setText("使用时间: " + TimeUtil.formatTime(orderDetailVo.getCurrentTime() - orderDetailVo.getCreateTime()));
            }
        } else {//如果是时租

            ll_time_share.setVisibility(View.VISIBLE);
            ll_day_share.setVisibility(View.GONE);
        }

        if (YYConstans.brandHelpMsgVOs == null) {
            BrandHelpMsgBean brandHelpMsgBean = (BrandHelpMsgBean) GsonTransformUtil.fromJson(SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_BRANDHELPMSG, ""), BrandHelpMsgBean.class);
            if (brandHelpMsgBean != null && brandHelpMsgBean.getErrno() == 0) {
                YYConstans.brandHelpMsgVOs = brandHelpMsgBean.getReturnContent();
            }
        }

        boolean find = false;
        if (YYConstans.brandHelpMsgVOs != null) {
            a:
            for (int i = 0; i < YYConstans.brandHelpMsgVOs.size(); i++) {
                if ((orderBean.getMake() + orderBean.getModel()).equals(YYConstans.brandHelpMsgVOs.get(i).getParentName() + YYConstans.brandHelpMsgVOs.get(i).getName())) {

                    if (brandHelpMsgVO == null) {
                        brandHelpMsgVO = YYConstans.brandHelpMsgVOs.get(i);
                    }
                    for (int j = 0; j < YYConstans.brandHelpMsgVOs.get(i).getMsgList().size(); j++) {
                        switch (YYConstans.brandHelpMsgVOs.get(i).getMsgList().get(j).getMsgType()) {
                            case 1:
                                brandHelpMsgVO.setCreateMsg(YYConstans.brandHelpMsgVOs.get(i).getMsgList().get(j).getMsg());
                                break;
                            case 2:
                                brandHelpMsgVO.setUseMsg(YYConstans.brandHelpMsgVOs.get(i).getMsgList().get(j).getMsg());
                                break;
                            case 3:
                                brandHelpMsgVO.setEndOrderMsg(YYConstans.brandHelpMsgVOs.get(i).getMsgList().get(j).getMsg());
                                break;
                        }
                    }
                    speechImage.setTag(brandHelpMsgVO.getUseMsg());
                    find = true;
//                    if (carGuideDialog == null) {
//                        carGuideDialog = new CarGuideDialog(mContext, orderBean.getMake() + orderBean.getModel(), YYConstans.brandHelpMsgVOs.get(i).getHelpPhoto());
//                        carGuideDialog.show();
//                    }
                    break a;
                }
            }
        }
        if (!find || brandHelpMsgVO == null || TextUtils.isEmpty(brandHelpMsgVO.getCreateMsg()) || TextUtils.isEmpty(brandHelpMsgVO.getUseMsg()) || TextUtils.isEmpty(brandHelpMsgVO.getEndOrderMsg())) {
            if (brandHelpMsgVO == null) {
                brandHelpMsgVO = new BrandHelpMsgVO();
            }

            if (TextUtils.isEmpty(brandHelpMsgVO.getCreateMsg())) {
                brandHelpMsgVO.setCreateMsg(YYConstans.sounds_create_def);
            }
            if (TextUtils.isEmpty(brandHelpMsgVO.getUseMsg())) {
                brandHelpMsgVO.setUseMsg(YYConstans.sounds_use_def);
                speechImage.setTag(brandHelpMsgVO.getUseMsg());
            }
            if (TextUtils.isEmpty(brandHelpMsgVO.getEndOrderMsg())) {
                brandHelpMsgVO.setEndOrderMsg(YYConstans.sounds_backcar_def);
            }

        }

        if (isFirst) {
            speechPoint.setVisibility(View.VISIBLE);
            isFirst = false;
            String carlist = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_USEDCARMODE, "");
            if (carlist.indexOf(orderBean.getMake() + orderBean.getModel()) < 0) {
                try {
                    SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getCreateMsg());
                } catch (Exception e) {

                }
                // SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getCreateMsg());
                SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_USEDCARMODE, carlist + orderBean.getMake() + orderBean.getModel() + "-");
            }

            YYOneButtonDialog oneButtonDialog = new YYOneButtonDialog(mContext, "用车提示", "即将进入找车等待期，等待期15分钟\n等待期内不计费，15分钟后开始计费", "知道了");
            oneButtonDialog.show();

        }


        if (orderDetailVo.getFeeDetail().getPickcarTime() > 0) {
            speechText.setText("启动不了车辆，点我语音告诉您~");
            speechImage.setTag(brandHelpMsgVO.getUseMsg());
            iv_right_raw.setVisibility(View.GONE);
        } else {
            speechText.setText("取车时注意事项，点我语音告诉您~");
            speechImage.setTag(brandHelpMsgVO.getCreateMsg());
            iv_right_raw.setVisibility(View.VISIBLE);
        }


        if (orderBean.getFeeDetail().getPickcarTime() < 1 || orderBean.getCurrentXY() == null) {
            backCarText.setText("车辆位置：" + orderBean.getFromXY().getLocation());
            showCarView(new LatLng(orderBean.getFromXY().getGpsXY().getLat(), orderBean.getFromXY().getGpsXY().getLng()));
            layoutNavigate.setSelected(true);
            layoutNavigate.setOnClickListener(null);
        } else {
            backCarText.setText("车辆位置：" + orderBean.getCurrentXY().getLocation());
            showCarView(new LatLng(orderBean.getCurrentXY().getGpsXY().getLat(), orderBean.getCurrentXY().getGpsXY().getLng()));
            layoutNavigate.setSelected(false);
            layoutNavigate.setOnClickListener(this);
        }



        if(orderBean.getTimeMode() == 1){//如果是日租
            layoutNavigate.setSelected(false);
            layoutNavigate.setOnClickListener(this);
        }else{//如果是时租
            if (orderBean.getStationType() == 3) {
                layoutNavigate.setSelected(true);
                layoutNavigate.setOnClickListener(null);
            }

            if(orderBean.getFreedomMultiple()>1){
                layoutNavigate.setSelected(true);
                layoutNavigate.setOnClickListener(null);
                backCarText.setText("还车站点：不限制还车点, 合法停车点均可还车");
            }
        }


        titleView.setText(orderBean.getLicense() + " | 可续航" + orderBean.getLeftMileage() + "公里");

        StringBuffer lable1 = new StringBuffer();
        StringBuffer lable2 = new StringBuffer();

        if (System.currentTimeMillis() - orderBean.getFeeDetail().getChargeTime() > 0) {
            //开始计费
            lable1.append("<font color='#3d3f42'>已行驶</font><font color='#04b575'>")
                    .append(((int) orderBean.getFeeDetail().getDistance())).append("</font>")
                    .append("<font color='#3d3f42'>公里</font>");


            lable2.append("<font color='#3d3f42'>已用时</font><font color='#04b575'>");
            long[] time = TimeDateUtil.formatTime_HM(orderBean.getFeeDetail().getCostTime());
            if (time != null) {
                if (time.length == 2) {
                    lable2.append(time[0]).append("</font>").append("<font color='#3d3f42'>时</font><font color='#04b575'>")
                            .append(time[1]).append("</font>").append("<font color='#3d3f42'>分</font>");
                } else {
                    lable2.append(time[0]).append("</font>").append("<font color='#3d3f42'>分</font>");
                }
            }
            userDetailView.setLable0Str(lable1.toString());
            userDetailView.setLable1Str(lable2.toString());
            userDetailView.setPrice(orderBean.getFeeDetail().getAllPrice());
            lable1 = null;
            lable2 = null;

        } else {
            //未计费
            lable1.append("<font color='#3d3f42'>开始计费时间</font>");
            userDetailView.setLable0Str(lable1.toString());
            lable2.append("<font color='#04b575'>").append(TimeDateUtil.MDHMTransform(orderBean.getFeeDetail().getChargeTime())).append("</font>");
            userDetailView.setLable1Str(lable2.toString());
            userDetailView.setLaterTime((int) ((orderBean.getFeeDetail().getChargeTime() - System.currentTimeMillis()) / 1000));
            lable1 = null;
            lable2 = null;

        }
        if (orderBean.getTimeMode() == 1) {//如果是日租
            switch (orderBean.getOrderState()) {
                case 1://等待取车
                case 2://正在进行
                    if(orderBean.getIfUsing()==1){//已经开门
                        backCarText.setText("还车站点：" + orderBean.getFromXY().getLocation());
                    }else{
                        backCarText.setText("车辆位置：" + orderBean.getFromXY().getLocation());
                    }
                    backCarImage.setBackgroundResource(R.drawable.go_that);
                    backCarSet.setText("去这里");
                    backCarSet.setVisibility(View.VISIBLE);
                    layoutChangeSet.setVisibility(View.VISIBLE);
                    layoutChangeSet.setTag(orderBean.getFromXY());

                    orderButton.setText("申请还车");
                    orderButton.setTextColor(mContext.getResources().getColor(R.color.titlebar_background));
                    orderButton.setBackgroundResource(R.drawable.mapcarfrg_havecarremind_bg_selector);
                    awakeUP();
                    YYConstans.hasUnFinishOrder = true;
                    if (orderDetailVo != null) {
                        requestOprateCar();
                    }
                    break;
                case 3:


                    Bundle bundle3 = new Bundle();
                    bundle3.putString("orderId", orderDetailVo.getOrderId());
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtras(bundle3);
                    getActivity().startActivity(intent);
                    //startActivity(PayActivity.class, bundle3);
                    getActivity().finish();

//            orderButton.setText("支付租金");
//            YYConstans.hasUnFinishOrder = true;
                    break;
                case 4:
                    // 企业账号直接跳转地图首页
                    YYConstans.hasUnFinishOrder = false;
//                            startActivity(MainActivity2_1.class, null);
                    Bundle bundle = new Bundle();
//                bundle.putInt("type", 2);
                    bundle.putString("orderId", orderDetailVo.getOrderId() + "");
                    //      startActivity(FeelBackAty.class, bundle);
                    bundle.putString("imageurl", orderDetailVo.getCarPhoto());
                    startActivity(RatedUsAty.class, bundle);
                    getActivity().finish();

                    break;
                case 5:
                    MyUtils.showToast(mContext, "订单已取消");
                    YYConstans.hasUnFinishOrder = false;
                    startActivity(MainActivity2_1.class, null);
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 100);

                    break;
            }
        } else {//如果是时租
            switch (orderBean.getOrderState()) {
                case 1:
                    backCarText.setText("车辆位置：" + orderBean.getFromXY().getLocation());
                    backCarImage.setBackgroundResource(R.drawable.go_that);
                    backCarSet.setText("去这里");
                    layoutChangeSet.setVisibility(View.VISIBLE);
                    layoutChangeSet.setTag(orderBean.getFromXY());


                    orderButton.setText("取消行程");
                    orderButton.setTextColor(mContext.getResources().getColor(R.color.text_a6a6a6));
                    orderButton.setBackgroundResource(R.drawable.mapcarfrg_top_location_bg_selector);
                    awakeUP();
                    YYConstans.hasUnFinishOrder = true;
                    if (orderDetailVo != null) {
                        requestOprateCar();
                    }
                    break;
                case 2:

                    if (orderBean.getFeeDetail().getPickcarTime() < 1 || orderBean.getCurrentXY() == null) {
                        backCarText.setText("取车站点：" + orderBean.getFromXY().getLocation());
                        backCarImage.setBackgroundResource(R.drawable.go_that);
                        backCarSet.setText("去这里");
                        layoutChangeSet.setVisibility(View.VISIBLE);
                        layoutChangeSet.setTag(orderBean.getFromXY());
                    } else {
                        switch (orderBean.getStationType()) {
                            case 1:
                                backCarText.setText("还车站点：" + orderBean.getToXY().getLocation());
                                backCarImage.setBackgroundResource(R.drawable.go_that);
//                                layoutChangeSet.setVisibility(View.GONE);
//                                layoutChangeSet.setTag(null);

                                backCarSet.setText("去这里");
                                backCarSet.setVisibility(View.VISIBLE);
                                layoutChangeSet.setVisibility(View.VISIBLE);
                                layoutChangeSet.setTag(orderBean.getFromXY());
                                break;
                            case 2:
                                if(orderBean.getFreedomMultiple()>1){//强制自由还车
                                    backCarText.setText("还车站点：不限还车点，合法停车点均可还车");
                                    backCarImage.setBackgroundResource(R.drawable.order_set);
                                    backCarSet.setText("重新设置");
                                    layoutChangeSet.setVisibility(View.GONE);
                                    layoutChangeSet.setTag(null);
                                }else{
                                    backCarText.setText("还车站点：" + orderBean.getToXY().getLocation());
                                    backCarImage.setBackgroundResource(R.drawable.order_set);
                                    backCarSet.setText("重新设置");
                                    layoutChangeSet.setVisibility(View.VISIBLE);
                                    layoutChangeSet.setTag(null);
                                }

                                break;
                            case 3:
                                backCarText.setText("还车站点：不限还车点，合法停车点均可还车");
                                backCarImage.setBackgroundResource(R.drawable.order_set);
                                backCarSet.setText("重新设置");
                                layoutChangeSet.setVisibility(View.GONE);
                                layoutChangeSet.setTag(null);
                                break;
                        }
                    }

                    orderButton.setText("申请还车");
                    orderButton.setTextColor(mContext.getResources().getColor(R.color.titlebar_background));
                    orderButton.setBackgroundResource(R.drawable.mapcarfrg_havecarremind_bg_selector);
                    awakeUP();
                    YYConstans.hasUnFinishOrder = true;
                    if (orderDetailVo != null) {
                        requestOprateCar();
                    }
                    break;
                case 3:


                    Bundle bundle3 = new Bundle();
                    bundle3.putString("orderId", orderDetailVo.getOrderId());
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtras(bundle3);
                    getActivity().startActivity(intent);
                    //startActivity(PayActivity.class, bundle3);
                    getActivity().finish();

//            orderButton.setText("支付租金");
//            YYConstans.hasUnFinishOrder = true;
                    break;
                case 4:
                    // 企业账号直接跳转地图首页
                    YYConstans.hasUnFinishOrder = false;
//                            startActivity(MainActivity2_1.class, null);
                    Bundle bundle = new Bundle();
//                bundle.putInt("type", 2);
                    bundle.putString("orderId", orderDetailVo.getOrderId() + "");
                    //      startActivity(FeelBackAty.class, bundle);
                    bundle.putString("imageurl", orderDetailVo.getCarPhoto());
                    startActivity(RatedUsAty.class, bundle);
                    getActivity().finish();

                    break;
                case 5:
                    MyUtils.showToast(mContext, "订单已取消");
                    YYConstans.hasUnFinishOrder = false;
                    startActivity(MainActivity2_1.class, null);
                    baseHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 100);

                    break;
            }
        }


    }

    //改变订单状态 更改订单状态
    public void showSureGaveCarDialog(Context context) {
        // TODO Auto-generated method stub
        updateOrderInfoDayUseCar(false);
//        requestUpdataOrderState(3, TAG_GAVECAR);
    }

    public void showCancelOrderDialog() {
        if (stateDlg == null) {
            stateDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_login, null);
            TextView tv_title = (TextView) mDlgCallView
                    .findViewById(R.id.tv_title);
            tv_title.setText("提示");
            TextView tv_msg = (TextView) mDlgCallView.findViewById(R.id.tv_msg);
            tv_msg.setText("当天最多可取消租车2次\n还剩" + (2 - getCancelNum()) + "次，是否取消？");
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("再想想");
            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    stateDlg.dismiss();
                }
            });
            final TextView tv_login_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_login_txt);
            tv_login_txt.setText("继续");
            tv_login_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    //更改订单状态 取消订单
                    requestUpdataOrderState(5, TAG_CANCELORDER);
                    stateDlg.dismiss();
                }
            });
            stateDlg.setCanceledOnTouchOutside(false);
            stateDlg.setContentView(mDlgCallView);
        }
        stateDlg.show();
        Window dlgWindow = stateDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 5 * 4;
        dlgWindow.setAttributes(lp);
    }

    /**
     * 改变订单状态
     *
     * @param state
     */
    private void requestUpdataOrderState(int state, int tag) {
        if (NetHelper.checkNetwork(mContext)) {
            showNoNetDlg();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("orderId", orderDetailVo.getOrderId());
        params.put("orderState", state + "");
        YYRunner.getData(tag, YYRunner.Method_POST, YYUrl.GETUPDATAORDER,
                params, this);
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1001:

                    if (data != null) {
                        boolean isPaySucceed = data.getBooleanExtra("isPaySucceed",
                                false);
                        if (isPaySucceed) {
                            YYConstans.hasUnFinishOrder = false;
                            Bundle bundle = new Bundle();
                            // bundle.putInt("type", 2);
                            bundle.putString("orderId", orderDetailVo.getOrderId() + "");
                            // startActivity(FeelBackAty.class, bundle);
                            bundle.putString("imageurl", orderDetailVo.getCarPhoto());
                            Intent intent = new Intent(getActivity(), RatedUsAty.class);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);//跳转到评价Activity
//                            getActivity().startActivity(RatedUsAty.class, bundle);//跳转到评价Activity
                            getActivity().finish();
                        }
                    }

                    break;

                case 2001:
                    if (data != null && data.getExtras() != null) {
                        StationVo stationVo = (StationVo) data.getExtras().getSerializable("StationVo");

                        if (stationVo != null) {
                            backCarText.setTag(stationVo);
                            dialogShow();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                            params.put("orderId", orderDetailVo.getOrderId());
                            params.put("stationId", stationVo.getId() + "");
                            YYRunner.getData(TAG_CHANGEBACKSTATION, YYRunner.Method_POST, YYUrl.CHANGEBACKSTATION,
                                    params, this);
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setFragmentCallActivity(
            FragmentCallActivity fragmentCallActivity) {
        this.fragmentCallActivity = fragmentCallActivity;
    }


    /**
     * 打开拨打客服电话弹窗
     */
    public void showBackCarDialog() {
        if (backCarDlg == null) {
            backCarDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_backcar, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    backCarDlg.dismiss();
                }
            });
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            tv_number.setText("还车后车辆会被整备到租赁站再次出租，您无法继续租用，是否确认还车？");
            tv_call_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    //语音提示
                    if (!orderDetailVo.getOrderId().equals(SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER_BACK, ""))) {
                        if (!TextUtils.isEmpty(brandHelpMsgVO.getEndOrderMsg())) {
                            try {
                                SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getEndOrderMsg());
                            } catch (Exception e) {

                            }
                            //SpeechUtil.getInstance().peechMessage(mContext, brandHelpMsgVO.getEndOrderMsg());
                        }
                    }
                    SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CURRENTORDER_BACK, orderDetailVo.getOrderId());


                    if (gaveDialog == null) {
                        showGaveDialog();
                    } else if (gaveDialog.getStatus() != 0) {
                        showGaveDialog();
                    }
//                    showSureGaveCarDialog(mContext);
                    backCarDlg.dismiss();
                }
            });
            backCarDlg.setCanceledOnTouchOutside(false);
            backCarDlg.setContentView(mDlgCallView);
        }
        backCarDlg.show();
        Window dlgWindow = backCarDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }


    private void showGaveDialog() {
        if (gaveDialog == null) {
            gaveDialog = new CarGaveDialog(mContext);
            gaveDialog.setCheckCarOver(this);
        }


        if (!gaveDialog.isShowing()) {
            gaveDialog.show();
        }
    }


    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public void onSpeakBegin() {
//        speechImageView.setBackgroundResource(R.drawable.speech_list);
//        AnimationDrawable animationDrawable = (AnimationDrawable)
//                speechImageView.getBackground();
//        animationDrawable.start();
    }

    @Override
    public void onSpeakPaused() {
//        speechImageView.setBackgroundResource(R.drawable.speech_icon_selector);
    }

    @Override
    public void onSpeakResumed() {
//        speechImageView.setBackgroundResource(R.drawable.speech_list);
//        AnimationDrawable animationDrawable = (AnimationDrawable)
//                speechImageView.getBackground();
//        animationDrawable.start();
    }

    @Override
    public void onCompleted() {
//        speechImageView.setBackgroundResource(R.drawable.speech_icon_selector);
    }


    /**
     * onc
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


    private void addCancelNum() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CANCELORDER_DATE, ""));
        buffer.append(TimeDateUtil.formatTime(System.currentTimeMillis(), "yyyy-MM-dd")).append(";");
        SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CANCELORDER_DATE, buffer.toString());
    }


    @Override
    public void leftClick() {

    }

    @Override
    public void Rightclick() {
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("orderId", orderDetailVo.getOrderId());
        YYRunner.getData(TAG_PAYBYENTERPRISEAndPay, YYRunner.Method_POST,
                YYUrl.PAYBYENTERPRISE, params, this);

    }

    @Override
    public void onCheckOver() {
        showSureGaveCarDialog(mContext);
    }


    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "user_invite");
        YYRunner.getData(8001, YYRunner.Method_POST,
                YYUrl.GETACTIVITYCONTENT, params, this);
    }


    private void showCarView(LatLng latLng) {


        // 移动到以当前坐标为中心的画面
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mapView.getMap().animateMapStatus(update);

        if (mapCarView == null) {
            mapCarView = getCarView();
        }
        InfoWindow infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(mapCarView), latLng, -5, null);
        mapView.getMap().showInfoWindow(infoWindow);


    }

    /**
     * 获取站点图标View
     */
    private View getCarView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        ImageView iv_guide = new ImageView(mContext);
        iv_guide.setImageResource(R.drawable.create_order_car);
        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(MyUtils.dip2px(mContext, 28), MyUtils.dip2px(mContext, 28)));
        layout.addView(iv_guide);
        return layout;
    }


    class CarUserDetailView {

        private TextView leftLable0, leftLable1, mapMin, mapSec, mapYuan;

        public TextView getLeftLable0() {
            return leftLable0;
        }

        public void setLeftLable0(TextView leftLable0) {
            this.leftLable0 = leftLable0;
        }

        public TextView getLeftLable1() {
            return leftLable1;
        }

        public void setLeftLable1(TextView leftLable1) {
            this.leftLable1 = leftLable1;
        }

        public TextView getMapMin() {
            return mapMin;
        }

        public void setMapMin(TextView mapMin) {
            this.mapMin = mapMin;
        }

        public TextView getMapSec() {
            return mapSec;
        }

        public void setMapSec(TextView mapSec) {
            this.mapSec = mapSec;
        }

        public TextView getMapYuan() {
            return mapYuan;
        }

        public void setMapYuan(TextView mapYuan) {
            this.mapYuan = mapYuan;
        }


        private void setLable0Str(String str) {
            if (leftLable0 != null) {
                leftLable0.setText(Html.fromHtml(str));
            }
        }

        private void setLable1Str(String str) {
            if (leftLable1 != null) {
                leftLable1.setText(Html.fromHtml(str));
            }
        }


        private void setLaterTime(int time) {
            orderFrgHandler.removeMessages(3001);

            if (time == -2) {
                if (mapMin != null) {
                    time = (int) mapMin.getTag();
                }
            } else if (time == -1 || time == 0) {

                //开始计费
                userDetailView.setLable0Str("已行驶" + ((int) orderDetailVo.getFeeDetail().getDistance()) + "公里");
                userDetailView.setLable1Str("已用时" + TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getCostTime()));
                userDetailView.setPrice(orderDetailVo.getFeeDetail().getAllPrice());

                return;
            }
            int min = time / 60;
            int sec = time % 60;

            if (mapMin != null) {
                mapMin.setText("-" + min);
                time--;
                mapMin.setTag(time);
            }
            if (mapSec != null) {
                mapSec.setText(":" + sec + "");
            }
            if (mapYuan != null) {
                mapYuan.setText("分");
            }

            if (mapMin != null && time >= 0) {
                orderFrgHandler.sendEmptyMessageDelayed(3001, 1000);
            }
        }

        private void setPrice(double price) {
            orderFrgHandler.removeMessages(3001);
            String p = MyUtils.formatPriceLong(price);

            String[] index = p.split("\\.");

            if (mapMin != null) {
                mapMin.setText(index[0]);
            }

            if (mapSec != null) {
                mapSec.setText("." + index[1]);
            }

            if (mapYuan != null) {
                mapYuan.setText("元");
            }

        }


    }


}
