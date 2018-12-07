package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.CreateOrderVO;
import com.dzrcx.jiaan.Bean.DayAndNightTimeOutBean;
import com.dzrcx.jiaan.Bean.EnterpriseCenterBean;
import com.dzrcx.jiaan.Bean.EnterpriseItemInfo;
import com.dzrcx.jiaan.Bean.PayResult;
import com.dzrcx.jiaan.Bean.RenterStateBean;
import com.dzrcx.jiaan.Bean.RenterStateReturnContentBean;
import com.dzrcx.jiaan.Bean.StationVo;
import com.dzrcx.jiaan.Bean.UnionPayBean;
import com.dzrcx.jiaan.Bean.WXPayInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PayUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYTwoButtonDialog;
import com.dzrcx.jiaan.wxapi.WXPayEntryActivity;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 选择站点还车的车   填写在站点信息界面
 * Created by zhangyu on 16-5-10.
 */
public class CreateOrderFrag extends BaseBaiduMapFragment implements View.OnClickListener, RequestInterface, WXPayCallBackListener {
    private LinearLayout ll_pricing_manner;
    private ImageView iv_pricing_manner;
    private TextView tv_pricing_manner;

    private EnterpriseCenterBean enterPriseBean;

    private View contentView;
    private TextView titleView;
    private ImageView rightView, iv_left_raw, iv_give_back_car_explain;
    private TextView getStationView, backStationView, submitView, tv_price, tv_proment_message;
    private View backStationMore;
//    private MapView mapView;

    private RelativeLayout layoutPayOver;
    private LinearLayout layoutPay;


    private TextView deposit, tv_order_payover;
    private ImageView payIcon;
    private TextView paytext, payinfo;

    private LinearLayout ll_choose_account;//个人账户 选择账户
    private TextView tv_choose_account;//显示账户名字

    private View mapCarView;

    private CreateOrderVO createOrderVO;
    private RenterStateReturnContentBean stateReturnContentBean;
    private Dialog warnDlg, paytypeDlg;
    private YYTwoButtonDialog cancelDepositDialog;
    private LinearLayout ll_free_give_back;
    private ImageView iv_free_give_back;
    private TextView tv_x_multiple;//自由还车显示倍数

    private LinearLayout ll_give_back_station;//时租还车站点布局
    private LinearLayout ll_day_give_back_station;//日租原站点还车布局
    private TextView tv_day_give_back_station;//日租原站点还车TextView
    /**
     * 提示框标题：租车押金说明
     */
    private final String PROMPT_DEPOSIT = "为保障双方权益，在创建订单时，同时需要支付800元的租车押金，若在用车过程中未造成任何经济损失（如违章扣分罚款和事故车损等相关情况）在订单结束15日后，可在我的账户中进行申请提现操作，我们将按原路退回全部押金，如您使用的是信用卡预授权支付方式，在订单结束30天后，银行将自动解除预授权并退款至您原支付信用卡中；在支付一次押金后，并未进行申请提现操作或预授权还在有效期内，租车押金是可以重复使用的。在此我们祝您用车愉快~";

    /**
     * 用户选择支付宝和微信支付租车押金提示文案
     */
    private final String PROMPT_DEPOSIT_ALIWX = "支付的押金将存入您的租车押金账户中，在最近一个订单结 束15日后，您可以到我的账户中申请提现。";

    /**
     * 用户选择银联信用卡预授权方式支付租车押金提示文案：
     */
    private final String PROMPT_DEPOSIT_unpay = "预授权将由银行冻结租车押金30天，若在用车中未造成任何 经济上的损失，30天后押金将自动退回到原支付信用卡中。";
    /**
     * 用户使用租车押金账户直接使用提示文案：
     */
    private final String PROMPT_DEPOSIT_ACCOUNT = "系统将直接使用您押金账户中的金额来支付本次订单的租车押金，所以此次您无需再支付，谢谢~";
    /**
     * 用户有预授权未过期直接沿用预授权提示文案：
     */
    private final String PROMPT_DEPOSIT_CANDO = "系统检测到您尚有一笔预授权还在有效期内，本次订单可以 直接使用，所以本次无需再另支付租车押金。";

    //租车模式 分时用车或按日用车
    private PricingManner pricingManner = PricingManner.TimeShape;
    //是否自由还车
    private boolean isFreeGiveBack = false;

    //租车模式 分时用车或按日用车
    public enum PricingManner {
        TimeShape, DayShape
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 10001:

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        doPaySucceed("支付成功");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            MyUtils.showToast(mContext, "支付结果确认中");
                            // Toast.makeText(PayDemoActivity.this, "支付结果确认中",
                            // Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            MyUtils.showToast(mContext, "支付失败");
                            // Toast.makeText(PayDemoActivity.this, "支付失败",
                            // Toast.LENGTH_SHORT).show();

                        }
                    }

                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_create_order, null);

            if (getActivity().getIntent().getExtras() != null) {
                createOrderVO = (CreateOrderVO) getActivity().getIntent().getExtras().getSerializable("CreateOrderVO");
                stateReturnContentBean = (RenterStateReturnContentBean) getActivity().getIntent().getExtras().getSerializable("RenterStateReturnContentBean");
                enterPriseBean = (EnterpriseCenterBean) getActivity().getIntent().getExtras().getSerializable("enterPriseBean");
            }
            initView();
            initLocationBaiduMap(contentView, R.id.create_order_baidumap, createOrderVO);
            //requestData(true);
        }
        return contentView;
    }


    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        mapView.onPause();
        mMapView.onPause();
    }


    /**
     * 获取日间夜间时段数据请求
     *
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("carId", createOrderVO.getCarId() + "");
        YYRunner.getData(1002, YYRunner.Method_POST,
                YYUrl.GETDAYANDNIGHT, params, this);
    }

    //租车模式 定价方式弹窗 分时用车/按日用车
    private void showPricingMannerDialog() {
        final Dialog pricingMannerDialog = new Dialog(mContext, R.style.ActionSheet);
        View ll_pricing_manner = mContext.getLayoutInflater().inflate(
                R.layout.dialog_pricing_manner, null);
        LinearLayout ll_top = (LinearLayout) ll_pricing_manner.findViewById(R.id.ll_top);
        LinearLayout ll_bottom = (LinearLayout) ll_pricing_manner.findViewById(R.id.ll_bottom);
        LinearLayout ll_time_share = (LinearLayout) ll_pricing_manner.findViewById(R.id.ll_time_share);
        LinearLayout ll_day_share = (LinearLayout) ll_pricing_manner.findViewById(R.id.ll_day_share);
        final ImageView iv_time_share = (ImageView) ll_pricing_manner.findViewById(R.id.iv_time_share);
        final ImageView iv_day_share = (ImageView) ll_pricing_manner.findViewById(R.id.iv_day_share);
        final TextView tv_time_share_price = (TextView) ll_pricing_manner.findViewById(R.id.tv_time_share_price);
        final TextView tv_day_share_price = (TextView) ll_pricing_manner.findViewById(R.id.tv_day_share_price);
        String time_price = createOrderVO.getPrice() + "元/时" + "+" + createOrderVO.getPricePerMileage() + "元/公里";
        String day_price = createOrderVO.getDailyRentalPrice() + "元/天";
        tv_time_share_price.setText(time_price + "\n" + "最低一小时起租");
        tv_day_share_price.setText(day_price + "\n" + "最低一天起(24h),价格更划算");
        if (pricingManner == PricingManner.TimeShape) {
            iv_time_share.setBackgroundResource(R.drawable.right_green);
            iv_day_share.setBackgroundResource(R.drawable.right_gray);
        } else if (pricingManner == PricingManner.DayShape) {
            iv_time_share.setBackgroundResource(R.drawable.right_gray);
            iv_day_share.setBackgroundResource(R.drawable.right_green);
        }

        ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingMannerDialog.dismiss();
            }
        });
        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingMannerDialog.dismiss();
            }
        });
        ll_time_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingManner = PricingManner.TimeShape;//组合类型 为 分时用车
                iv_time_share.setBackgroundResource(R.drawable.right_green);
                iv_day_share.setBackgroundResource(R.drawable.right_gray);
                iv_pricing_manner.setBackgroundResource(R.drawable.time_share);
                tv_pricing_manner.setText("分时用车");
                if (isFreeGiveBack) {
                    tv_x_multiple.setVisibility(View.VISIBLE);//x2倍数的textview
                }
                String price = createOrderVO.getPrice() + "元/时" + "+" + createOrderVO.getPricePerMileage() + "元/公里";
                tv_price.setText(price);
                tv_proment_message.setText("用车时长最低一小时起");
                ll_give_back_station.setVisibility(View.VISIBLE);//时租还车站点布局
                ll_day_give_back_station.setVisibility(View.GONE);//日租原站点还车布局
                pricingMannerDialog.dismiss();
            }
        });
        ll_day_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricingManner = PricingManner.DayShape;//租车类型 为 按日用车
                iv_time_share.setBackgroundResource(R.drawable.right_gray);
                iv_day_share.setBackgroundResource(R.drawable.right_green);
                iv_pricing_manner.setBackgroundResource(R.drawable.day_share);
                tv_pricing_manner.setText("按日用车");
                tv_x_multiple.setVisibility(View.GONE);//x2倍数的textview
                String price = createOrderVO.getDailyRentalPrice() + "元/天";
                tv_price.setText(price);
                tv_proment_message.setText("不计里程,更划算");
                ll_give_back_station.setVisibility(View.GONE);//时租还车站点布局
                ll_day_give_back_station.setVisibility(View.VISIBLE);//日租原站点还车布局
                pricingMannerDialog.dismiss();
            }
        });

        Window win = pricingMannerDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        pricingMannerDialog.setCancelable(true);
        pricingMannerDialog.setContentView(ll_pricing_manner);
        pricingMannerDialog.show();

    }

    //是否自由还车弹窗
    private void showIsFreeGiveBackDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.ActionSheet);
        View dialog_is_free_give_back = mContext.getLayoutInflater().inflate(
                R.layout.dialog_is_free_give_back, null);
        TextView tv_multiple = (TextView) dialog_is_free_give_back.findViewById(R.id.tv_multiple);
        TextView tv_multiple_detail = (TextView) dialog_is_free_give_back.findViewById(R.id.tv_multiple_detail);
        TextView tv_consent = (TextView) dialog_is_free_give_back.findViewById(R.id.tv_consent);
        TextView tv_disagree = (TextView) dialog_is_free_give_back.findViewById(R.id.tv_disagree);
        tv_multiple.setText("x" + createOrderVO.getFreedomMultiple());//倍数
        tv_multiple_detail.setText("自由还车费用将按照" + createOrderVO.getFreedomMultiple() + "倍来计算,您是否同意?");//倍数说明
        tv_consent.setOnClickListener(new View.OnClickListener() {//同意按钮
            @Override
            public void onClick(View view) {//同意
                isFreeGiveBack = true;
                backStationView.setText("");
                backStationView.setHint("任意合法还车位置均可还车");
                tv_x_multiple.setVisibility(View.VISIBLE);
                iv_free_give_back.setBackgroundResource(R.drawable.check);
                backStationView.setText("");
                createOrderVO.setToStationId(0);//站点还车id清空
                dialog.dismiss();
            }
        });

        tv_disagree.setOnClickListener(new View.OnClickListener() {//不同意按钮
            @Override
            public void onClick(View view) {//不同意
                isFreeGiveBack = false;
                tv_x_multiple.setVisibility(View.GONE);
                iv_free_give_back.setBackgroundResource(R.drawable.no_check);
                dialog.dismiss();
            }
        });

        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        dialog.setCancelable(true);
        dialog.setContentView(dialog_is_free_give_back);
        dialog.show();
    }

    private void initView() {
        //日租还是分时切换需要用到的布局_________________________________________________________________________
        ll_give_back_station = (LinearLayout) contentView.findViewById(R.id.ll_give_back_station);//时租还车站点布局
        ll_day_give_back_station = (LinearLayout) contentView.findViewById(R.id.ll_day_give_back_station);//日租原站点还车布局
        tv_day_give_back_station = (TextView) contentView.findViewById(R.id.tv_day_give_back_station);//日租原站点还车TextView
        tv_day_give_back_station.setText(createOrderVO.getFromStationName());//日租原站点还车TextView
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //租车模式 定价方式--------------------------------------------
        ll_pricing_manner = (LinearLayout) contentView.findViewById(R.id.ll_pricing_manner);
        iv_pricing_manner = (ImageView) contentView.findViewById(R.id.iv_pricing_manner);
        tv_pricing_manner = (TextView) contentView.findViewById(R.id.tv_pricing_manner);
        tv_order_payover = (TextView) contentView.findViewById(R.id.tv_order_payover);
        ll_pricing_manner.setEnabled(false);    //去掉日租功能
        ll_pricing_manner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //租车模式 定价方式弹窗
                showPricingMannerDialog();
            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //是否自由还车____________________________________________________________________________________
        iv_free_give_back = (ImageView) contentView.findViewById(R.id.iv_free_give_back);
        ll_free_give_back = (LinearLayout) contentView.findViewById(R.id.ll_free_give_back);
        ll_free_give_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFreeGiveBack == true) {
                    isFreeGiveBack = false;
                    tv_x_multiple.setVisibility(View.GONE);
                    iv_free_give_back.setBackgroundResource(R.drawable.no_check);
                    backStationView.setText("");
                    backStationView.setHint("选择还车站点");
                } else {
                    showIsFreeGiveBackDialog();
                }
            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //站点是否支持自由还车_________________________________________________________________
        if (createOrderVO.getCanChangeFreedomMode() == 1) {
            ll_free_give_back.setVisibility(View.VISIBLE);
        } else {
            ll_free_give_back.setVisibility(View.GONE);
        }
//        if (createOrderVO != null && createOrderVO.getStayType() != 3) {//不支持自由还车//1-A 2-B 3-N
//            ll_free_give_back.setVisibility(View.GONE);
//        } else {//支持自由还车
//            ll_free_give_back.setVisibility(View.VISIBLE);
//        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //还车说明___________________________________________________________________
        iv_give_back_car_explain = (ImageView) contentView.findViewById(R.id.iv_give_back_car_explain);
        iv_give_back_car_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "还车说明").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=carBack"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        ll_choose_account = (LinearLayout) contentView.findViewById(R.id.ll_choose_account);
        ll_choose_account.setOnClickListener(this);
        tv_choose_account = (TextView) contentView.findViewById(R.id.tv_choose_account);//个人账户<选择账户>

        iv_left_raw = (ImageView) contentView.findViewById(R.id.iv_left_raw);
        titleView = (TextView) contentView.findViewById(R.id.tv_title);
        tv_x_multiple = (TextView) contentView.findViewById(R.id.tv_x_multiple);
        tv_x_multiple.setText("x" + createOrderVO.getFreedomMultiple());
        rightView = (ImageView) contentView.findViewById(R.id.iv_right);
        getStationView = (TextView) contentView.findViewById(R.id.create_order_getstation);
        backStationView = (TextView) contentView.findViewById(R.id.create_order_backstation);
        backStationMore = contentView.findViewById(R.id.create_order_backstation_more);
        tv_price = (TextView) contentView.findViewById(R.id.tv_price);
        tv_proment_message = (TextView) contentView.findViewById(R.id.tv_proment_message);
        submitView = (TextView) contentView.findViewById(R.id.create_order_submit);

        layoutPayOver = (RelativeLayout) contentView.findViewById(R.id.create_order_layout_payover);
        layoutPay = (LinearLayout) contentView.findViewById(R.id.create_order_layout_pay);

        deposit = (TextView) contentView.findViewById(R.id.create_order_deposit);
        payIcon = (ImageView) contentView.findViewById(R.id.create_order_payicon);
        paytext = (TextView) contentView.findViewById(R.id.create_order_paytext);
        payinfo = (TextView) contentView.findViewById(R.id.create_order_pay_info);


        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(this);
        titleView.setText("创建订单");
//        rightView.setVisibility(View.VISIBLE);
//        rightView.setOnClickListener(this);
        backStationView.setOnClickListener(this);
        submitView.setOnClickListener(this);
        contentView.findViewById(R.id.create_order_xieyi).setOnClickListener(this);
        contentView.findViewById(R.id.create_order_question).setOnClickListener(this);
        contentView.findViewById(R.id.create_order_layout_changepaylayout).setOnClickListener(this);

//        mapView = (MapView) contentView.findViewById(R.id.create_order_baidumap);
//        mapView.showZoomControls(false);// false不显示缩放控件true显示缩放控件
//        mapView.showScaleControl(false);// 不显示比例尺
//        mapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
//        BaiduMap baiduMap = mapView.getMap();
//        UiSettings mUiSettings = baiduMap.getUiSettings();
//        mUiSettings.setOverlookingGesturesEnabled(true);//设置是否可以手势俯视模式
//        mUiSettings.setZoomGesturesEnabled(false);
//        mUiSettings.setScrollGesturesEnabled(false);
//        MapStatus mapStatus = new MapStatus.Builder().overlook(0).zoom(15).build();
//        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mapStatus);

        // 开启定位图层
//        baiduMap.setMyLocationEnabled(true);
//        baiduMap.animateMapStatus(u, 1000); // 设定地图缩放比例百度地图缩放范围（3-19），12两公里

        //添加导航功能____________________________________________________________________________________
//        RoutePlanSearch mSearch  = RoutePlanSearch.newInstance(); // 搜索模块，也可去掉地图模块独立使用
//        LatLng startLl, endLl;
//        startLl = new LatLng(YYApplication.Latitude,YYApplication.Longitude);
//        Log.e("LatLng",YYApplication.Latitude+"$$$$"+YYApplication.Longitude);
//        PlanNode stNode = PlanNode.withLocation(startLl);
//        endLl = new LatLng(createOrderVO.getLat(), createOrderVO.getLng());
//        Log.e("LatLng",createOrderVO.getLat()+"####"+createOrderVO.getLng());
//        PlanNode enNode = PlanNode.withLocation(endLl);
//        if (mSearch.walkingSearch((new WalkingRoutePlanOption()).from(
//                stNode).to(enNode))) {
//        } else {
//            MyUtils.showToast(mContext, "路线规划失败，请重试");
//        }
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        payIcon.setImageResource(R.drawable.weixin);
        paytext.setText("使用微信");
        //payinfo.setText(PROMPT_DEPOSIT_ALIWX);
        submitView.setTag(new String("2"));
        tv_x_multiple.setVisibility(View.GONE);
        registerBoradcastReceiver();
        showData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.iv_right:
                showCallDialog();
                break;
            case R.id.create_order_backstation:
                isFreeGiveBack = false;
                tv_x_multiple.setVisibility(View.GONE);
                iv_free_give_back.setBackgroundResource(R.drawable.no_check);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CreateOrderVO", createOrderVO);
                startActivityForResult(ChooseStationAty.class, bundle, 3001);
                break;
            case R.id.create_order_xieyi:
                startActivity(new Intent(getActivity(), WebAty.class)
                        .putExtra("title", "租赁协议").putExtra("url",
                                YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=rentalTerms"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);

                break;

            case R.id.create_order_layout_changepaylayout:
                choosePayChannel();
                break;

            case R.id.create_order_question:
                if (stateReturnContentBean != null) {
                    if (stateReturnContentBean.getDepositState() == 0) {
                        showMessageSureDialog(PROMPT_DEPOSIT, "知道了", null);
                    } else if (stateReturnContentBean.getDepositState() == -1) {
                        showMessageSureDialog(getString(R.string.deposit_not_enough), "知道了", null);
                    }
                }
                break;

            case R.id.create_order_submit:
                if (createOrderVO != null) {
                    createOrder(createOrderVO);
                } else {
                    MyUtils.showToast(mContext, "数据异常");
                }
                break;
            case R.id.ll_choose_account://个人账户点击事件(选择账户)
                showPayDialog(enterPriseBean.getReturnContent().getEnterpriseCenterList());
                break;
        }
    }

    /**
     * 选择支付账户弹窗确定按钮点击事件
     * 点击支付方式选项的时候显示效果
     */

    private class PayStyleClickListener implements View.OnClickListener {
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
                                i_select = i;
                                dialog.dismiss();
                                createOrderVO.setPayMode(0);
                                createOrderVO.setEntId(0);
                                createOrderVO.setAccountName("个人账户余额支付");
                                tv_choose_account.setText("个人账户");
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("RenterStateReturnContentBean", tempStateReturnContentBean);
//                                bundle.putSerializable("CreateOrderVO", createOrderVO);
//                                bundle.putSerializable("enterPriseBean",enterPriseBean);
//                                startActivityForResult(CreateOrderAty.class, bundle, 10060);

//                                if (createOrderVO.getStayType() != 3) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("CreateOrderVO", createOrderVO);
//                                    startActivity(CreateOrderAty.class, bundle);
//
//                                } else {
//                                    showOrderTimeDialog(0, 0);
//                                }

                            } else {
                                if (priseBean.getBalanceOut() == 0) {//企业余额充足
                                    i_select = i;
                                    dialog.dismiss();
                                    createOrderVO.setPayMode(1);
                                    createOrderVO.setEntId(priseBean.getId());
                                    //String accountName = enterPriseBean.getReturnContent().getEnterpriseCenterList().get(i).getName();
                                    createOrderVO.setAccountName(priseBean.getName());
//                                    tv_choose_account.setText(priseBean.getName());
                                    tv_choose_account.setText("企业账户");

//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("RenterStateReturnContentBean", tempStateReturnContentBean);
//                                    bundle.putSerializable("CreateOrderVO", createOrderVO);
//                                    bundle.putSerializable("enterPriseBean",enterPriseBean);
//                                    startActivityForResult(CreateOrderAty.class, bundle, 10060);

                                } else {//企业余额不足
                                    View toastLayout = getActivity().getLayoutInflater().inflate(R.layout.account_unenough_toast_layout, null);
                                    LinearLayout accout_not_enough_toast = (LinearLayout) toastLayout.findViewById(R.id.accout_not_enough_toast);
                                    accout_not_enough_toast.getLayoutParams().width = MyUtils.getScreenWidth(mContext);
                                    MyUtils.showToast(mContext, toastLayout, Gravity.TOP, MyUtils.dip2px(mContext, 55));
                                }
                            }
                        }
                    }
                    break;
                default://账号点击事件
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

    private int i_select;

    //请选择支付账户弹窗
    public void showPayDialog(ArrayList<EnterpriseItemInfo> enterpriseList) {
        Dialog payDialog = new Dialog(mContext, R.style.ActionSheet);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_paystyle, null);
        LinearLayout accountLayout = (LinearLayout) view.findViewById(R.id.pay_dialog_company_layout);
        View[] accounts = new View[enterpriseList.size() + 1];
        CreateOrderFrag.PayStyleClickListener payStyleClickListener = new CreateOrderFrag.PayStyleClickListener(payDialog, accounts);
        View itemPayTempP = LayoutInflater.from(mContext).inflate(
                R.layout.item_pay_company_layout, null);
        ((TextView) itemPayTempP.findViewById(R.id.tv_businessname)).setText("[个人账户]-" + YYConstans.getUserInfoBean().getReturnContent().getUser().getName());
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
            ((TextView) itemPayTemp.findViewById(R.id.tv_businessname)).setText("[企业账户]-" + enterpriseList.get(i).getName());

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
        for (int i = 0; i < accounts.length; i++) {
            accounts[i].setSelected(false);
        }
        accounts[i_select].setSelected(true);
        (view.findViewById(R.id.tv_left_txt)).setOnClickListener(payStyleClickListener);
        (view.findViewById(R.id.tv_right_txt)).setOnClickListener(payStyleClickListener);

        payDialog.setCancelable(true);
        payDialog.setContentView(view);
        payDialog.show();


        Window dlgWindow = payDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        //lp.width = MyUtils.getScreenWidth(activity2_1) / 10 * 8;
        dlgWindow.setAttributes(lp);
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
        //mContext.finish();
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1001://请求支付押金成功后的网络请求的返回结果
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String errno = jsonObject.getString("errno");
                    if (!"0".equals(errno)) {
                        if ("10011".equals(errno)) {
                            // closeView();
                            startActivity(OrderAty.class, null);
                            getActivity().finish();
                            MyUtils.showToast(
                                    mContext,
                                    jsonObject.has("error") ? jsonObject
                                            .getString("error") : "服务器错误");
                        } else if ("10060".equals(errno)) {
                            //下手太慢
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else if ("10061".equals(errno)) {
                            //车辆已经下线
                            getActivity().setResult(10061);
                            getActivity().finish();
                        } else if ("10059".equals(errno)) {
                            //需要支付押金

                            MyUtils.showToast(
                                    mContext, "请先支付押金");
                            stateReturnContentBean = new RenterStateReturnContentBean();
                            stateReturnContentBean.setDepositType(0);
                            stateReturnContentBean.setDepositState(0);
                            stateReturnContentBean.setDepositAmount(1000);
                            stateReturnContentBean.setUserState(1);
                            stateReturnContentBean.setWithdrawFlag(0);

                            payIcon.setImageResource(R.drawable.zhifubao);
                            paytext.setText("使用微信");
                            payinfo.setText(PROMPT_DEPOSIT_ALIWX);
                            submitView.setTag(new String("2"));

                            showData();//刷新页面数据

//                            if (createOrderVO != null) {
//                                createOrder(createOrderVO);
//                            } else {
//                                MyUtils.showToast(mContext, "数据异常");
//                            }


                        } else {
                            MyUtils.showToast(
                                    mContext,
                                    jsonObject.has("error") ? jsonObject
                                            .getString("error") : "服务器错误");
                        }

                    } else {
                        //closeView();
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
            case 1002:
                DayAndNightTimeOutBean dayAndNightTimeOutBean = (DayAndNightTimeOutBean) GsonTransformUtil.fromJson(json, DayAndNightTimeOutBean.class);
                if (dayAndNightTimeOutBean != null || dayAndNightTimeOutBean.getErrno() == 0 && dayAndNightTimeOutBean.getReturnContent() != null) {
//                    tv_daytime.setText(dayAndNightTimeOutBean.getReturnContent().getDayDescription());
//                    tv_nighttime.setText(dayAndNightTimeOutBean.getReturnContent().getNightDescription());
                } else if (dayAndNightTimeOutBean != null || dayAndNightTimeOutBean.getErrno() != 0) {
                    MyUtils.showToast(mContext, dayAndNightTimeOutBean.getError());
                    mContext.finish();
                } else {
                    MyUtils.showToast(mContext, "未知错误");
                    mContext.finish();
                }
                break;
            case 2001://银联支付
                UnionPayBean unionPayBean = (UnionPayBean) GsonTransformUtil.fromJson(json, UnionPayBean.class);
                if (unionPayBean != null && unionPayBean.getErrno() == 0) {
                    PayUtil.unpay(mContext, unionPayBean.getReturnContent());
                }
                break;
            case 2002://支付宝支付
                ALiPayInfoBean aLiPayInfoBean = (ALiPayInfoBean) GsonTransformUtil
                        .fromJson(json, ALiPayInfoBean.class);

                if (aLiPayInfoBean != null) {

                    switch (aLiPayInfoBean.getErrno()) {
                        case 0:
                            PayUtil.alipay(getActivity(), mHandler, 10001,
                                    aLiPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, aLiPayInfoBean.getError());
                            break;
                    }

                }
                break;
            case 2003://微信支付
                WXPayInfoBean wxPayInfoBean = (WXPayInfoBean) GsonTransformUtil
                        .fromJson(json, WXPayInfoBean.class);

                if (wxPayInfoBean != null) {

                    switch (wxPayInfoBean.getErrno()) {
                        case 0:
                            PayUtil.WXPay(getActivity(), wxPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, wxPayInfoBean.getError());
                            break;
                    }

                }
                break;
            case 3001:
                RenterStateBean renterStateBean = (RenterStateBean) GsonTransformUtil.fromJson(json, RenterStateBean.class);
                if (renterStateBean != null && renterStateBean.getErrno() == 0) {
                    stateReturnContentBean = renterStateBean.getReturnContent();
                    showData();
                    if (createOrderVO != null) {
                        createOrder(createOrderVO);
                    } else {
                        MyUtils.showToast(mContext, "数据异常");
                    }
                }
                break;
            case 3002:
                YYBaseResBean resBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (resBean != null) {
                    MyUtils.showToast(mContext, resBean.getError());
                    if (resBean.getErrno() == 0 && cancelDepositDialog != null) {
                        cancelDepositDialog.dismiss();
                    } else {
                        showDepositDialog();
                    }
                }
                break;
        }

    }

    @Override
    public void onLoading(long count, long current) {

    }


    /**
     * 显示内容
     */
    private void showData() {
        if (createOrderVO != null) {

            StringBuffer titleBuf = new StringBuffer();
            titleBuf.append(createOrderVO.getMake() + createOrderVO.getModel());
            titleBuf.append(" | ").append(createOrderVO.getLicense());
            int a = titleBuf.length();
            titleBuf.append("\n");
            int b = titleBuf.length();
            titleBuf.append("创建订单");
            Spannable spannable = new SpannableString(titleBuf.toString());
            spannable.setSpan(new AbsoluteSizeSpan(MyUtils.sp2px(mContext, 17)), 0, a, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(MyUtils.sp2px(mContext, 12)), b, b + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleView.setText(spannable);
            // titleView.setLineSpacing();

            getStationView.setText(createOrderVO.getFromStationName());
//            if (createOrderVO.getStayType() == 3) {
//                backStationView.setText("不限制还车点，合法停车点均可还车");
//                backStationMore.setVisibility(View.GONE);
//            } else {
//                backStationView.setHint("点击选择还车位置");
//                backStationMore.setVisibility(View.VISIBLE);
//            }
            //没写 等待确认
//            backStationView.setText("");
//            if (createOrderVO.getStayType() == 3) {
//                backStationMore.setVisibility(View.GONE);
//            } else {
//                backStationMore.setVisibility(View.VISIBLE);
//            }
//            String price = "<font color='#04b575'>" + MyUtils.formatPriceShort(createOrderVO.getPrice()).trim() + "</font>" + "<font color='#3d3f42'>元/时+</font>"
//                    + "<font color='#04b575'>" + MyUtils.formatPriceShort(createOrderVO.getPricePerMileage()).trim() + "</font>" + "<font color='#3d3f42'>元/公里（</font>"
//                    + "<font color='#3d3f42'>" + MyUtils.formatPriceShort(createOrderVO.getDiscount()).trim() + "折后</font>"
//                    + "<font color='#3d3f42'>）| 起步价</font>"
//                    + "<font color='#04b575'>" + MyUtils.formatPriceShort(createOrderVO.getStartPrice()).trim() + "</font>" + "<font color='#3d3f42'>元</font>";
//            tv_price.setText(Html.fromHtml(price));
            String price = createOrderVO.getPrice() + "元/时" + "+" + createOrderVO.getPricePerMileage() + "元/公里";
            tv_price.setText(price);
            switch (createOrderVO.getStayType()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCarView(createOrderVO.getStayType(), new LatLng(createOrderVO.getLat(), createOrderVO.getLng()), createOrderVO.getEv());
                }
            }, 1000);
//            LatLng ll = new LatLng(createOrderVO.getLat(), createOrderVO.getLng());
//            // 移动到以当前坐标为中心的画面
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            mapView.getMap().animateMapStatus(update);

        } else {
            MyUtils.showToast(mContext, "数据异常");
        }


        String depositStr = "<font color='#3d3f42'>" + "请支付租车押金" + MyUtils.formatPriceLong(stateReturnContentBean.getDepositAmount()) + "元(可退)" + "</font>";
        if (stateReturnContentBean != null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.deposit_pay, null);
//            LinearLayout ll_recharge_unpay = (LinearLayout) view.findViewById(R.id.ll_recharge_unpay);
            LinearLayout ll_recharge_ali = (LinearLayout) view.findViewById(R.id.ll_recharge_ali);
            LinearLayout ll_recharge_weixin = (LinearLayout) view.findViewById(R.id.ll_recharge_weixin);
            TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
//            final ImageView iv_union_pay = (ImageView) view.findViewById(R.id.iv_union_pay);
            final ImageView iv_ali_pay = (ImageView) view.findViewById(R.id.iv_ali_pay);
            final ImageView iv_weixin_pay = (ImageView) view.findViewById(R.id.iv_weixin_pay);

//            ll_recharge_unpay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    payIcon.setImageResource(R.drawable.icon_unpay);
//                    paytext.setText("使用信用卡预授权");
//                    //payDeposit(stateReturnContentBean.getDepositAmount(), 0);
//                    //payinfo.setText(PROMPT_DEPOSIT_unpay);
//                    iv_union_pay.setBackgroundResource(R.drawable.check_green);
//                    iv_ali_pay.setBackgroundResource(R.drawable.no_check_gray);
//                    iv_weixin_pay.setBackgroundResource(R.drawable.no_check_gray);
//                    submitView.setTag(new String("0"));
//
//                }
//            });
            ll_recharge_ali.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    payIcon.setImageResource(R.drawable.zhifubao);
                    paytext.setText("使用支付宝");
                    //payDeposit(stateReturnContentBean.getDepositAmount(), 1);
                    //payinfo.setText(PROMPT_DEPOSIT_ALIWX);
//                    iv_union_pay.setBackgroundResource(R.drawable.no_check_gray);
                    iv_ali_pay.setBackgroundResource(R.drawable.check_green);
                    iv_weixin_pay.setBackgroundResource(R.drawable.no_check_gray);
                    submitView.setTag(new String("1"));
                }
            });
            ll_recharge_weixin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    payIcon.setImageResource(R.drawable.weixin);
                    paytext.setText("使用微信");
                    //payDeposit(stateReturnContentBean.getDepositAmount(), 2);
                    //payinfo.setText(PROMPT_DEPOSIT_ALIWX);
//                    iv_union_pay.setBackgroundResource(R.drawable.no_check_gray);
                    iv_ali_pay.setBackgroundResource(R.drawable.no_check_gray);
                    iv_weixin_pay.setBackgroundResource(R.drawable.check_green);
                    submitView.setTag(new String("2"));
                }
            });
            layoutPay.addView(view);
            if (stateReturnContentBean.getDepositState() == 0) {
                layoutPayOver.setVisibility(View.GONE);
                layoutPay.setVisibility(View.VISIBLE);
                deposit.setText(Html.fromHtml(depositStr));
                submitView.setText("立即支付 下一步");
                //showMessageSureDialog(PROMPT_DEPOSIT, "知道了", null);
                return;
            } else if (stateReturnContentBean.getDepositState() == -1) {
                layoutPayOver.setVisibility(View.GONE);
                layoutPay.setVisibility(View.VISIBLE);
                deposit.setText(Html.fromHtml(depositStr));
                showMessageSureDialog(getString(R.string.deposit_not_enough), "知道了", null);
                submitView.setText("立即支付 下一步");

                //0:没有违章押金 1:违章金为预售权 2:违章金为支付宝或微信
                switch (stateReturnContentBean.getDepositType()) {
                    case 0:
                        payinfo.setText(null);
                        break;
                    case 1:
                        payinfo.setText(PROMPT_DEPOSIT_CANDO);
                        break;
                    case 2:
                        payinfo.setText(PROMPT_DEPOSIT_ACCOUNT);
                        break;
                }
                return;
            } else if (stateReturnContentBean.getDepositState() == 1) {
                //无需再支付押金tv_order_payover
                layoutPayOver.setVisibility(View.VISIBLE);
                layoutPay.setVisibility(View.GONE);
                Log.e("Level", YYConstans.getUserInfoBean().getReturnContent().getUser().getLevel() + "");
                if (YYConstans.getUserInfoBean().getReturnContent().getUser().getLevel() == 10) {
                    tv_order_payover.setText("正在享受VIP免押金支付特权，可直接使用车辆");
                }
                submitView.setText("确认用车");

                //0:没有违章押金 1:违章金为预售权 2:违章金为支付宝或微信
                switch (stateReturnContentBean.getDepositType()) {
                    case 0:
                        payinfo.setText(null);
                        break;
                    case 1:
                        payinfo.setText(PROMPT_DEPOSIT_CANDO);
                        break;
                    case 2:
                        payinfo.setText(PROMPT_DEPOSIT_ACCOUNT);
                        break;
                }

            }
            showDepositDialog();
        } else {
            MyUtils.showToast(mContext, "数据异常");
        }

    }

    private void showDepositDialog() {

        if (stateReturnContentBean.getWithdrawFlag() == 1) {
            if (cancelDepositDialog == null) {
                cancelDepositDialog = new YYTwoButtonDialog(mContext, "取消下单", "继续下单", "系统检测到您有一笔租车押金正在提现中，若在此期间进行下单，系统将直接结束提现操作，直接用于此次订单的押金支付，您是否要继续下单？", " 租车押金提示");
            }
            cancelDepositDialog.setOnDialogClick(new YYTwoButtonDialog.DialogClick() {
                @Override
                public void leftClick(View v, Dialog dialog) {
                    getActivity().finish();
                }

                @Override
                public void Rightclick(View v, Dialog dialog) {
                    dialog.show();
                    cancelDeposit();
                }
            });

            cancelDepositDialog.setCanceledOnTouchOutside(false);
            cancelDepositDialog.setCancelable(false);
            cancelDepositDialog.show();
        }
    }

    /**
     * 支付之前请求订单信息
     *
     * @param createOrderVO
     */
    private void createOrder(CreateOrderVO createOrderVO) {
        Map<String, String> params = new HashMap<String, String>();
//        if (createOrderVO.getStayType() != 3 && createOrderVO.getToStationId() == 0
//                && isFreeGiveBack == false && pricingManner == PricingManner.TimeShape) {
        if (TextUtils.isEmpty(backStationView.getText().toString())
                && isFreeGiveBack == false&&createOrderVO.getStayType()!=3
                && pricingManner == PricingManner.TimeShape) {
            showMessageDialog("请选择还车租赁站");
            Log.e("11111111111111","111111111111111111");
            return;
        }
        //如果需要支付押金跳转支付
        if (stateReturnContentBean.getDepositState() != 1) {
            String tag = (String) submitView.getTag();
            if (tag != null) {
                if ("0".equals(tag)) {
                    payDeposit(stateReturnContentBean.getDepositAmount(), 0);
                } else if ("1".equals(tag)) {
                    payDeposit(stateReturnContentBean.getDepositAmount(), 1);
                } else if ("2".equals(tag)) {
                    payDeposit(stateReturnContentBean.getDepositAmount(), 2);
                }
                return;
            }
        }

        if (pricingManner == PricingManner.DayShape) {
            Intent intent = new Intent(getActivity(), DayShareActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("createOrderVO", createOrderVO);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);//跳转
            return;
        }

        if (createOrderVO.getPayMode() == 1) {
            params.put("entId", createOrderVO.getEntId() + "");
        }
        params.put("payMode", createOrderVO.getPayMode() + "");
        params.put("carId", createOrderVO.getCarId() + "");
        params.put("fromStationId", createOrderVO.getFromStationId() + "");
        params.put("toStationId", createOrderVO.getToStationId() + "");
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        if (isFreeGiveBack) {
            params.put("isFreedom", "1");//自由还车
        } else {
            params.put("isFreedom", "0");//站点还车
        }
        dialogShow();
        YYRunner.getData(1001, YYRunner.Method_POST,
                YYUrl.GETADDORDER, params, this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3001 && resultCode == Activity.RESULT_OK) {

            if (data != null && data.getExtras() != null) {
                StationVo stationVo = (StationVo) data.getExtras().getSerializable("StationVo");

                if (stationVo != null) {
                    backStationView.setText(stationVo.getName());
                    createOrderVO.setToStationId(stationVo.getId());
                }
            }
        }
        if (requestCode == 1 && resultCode == 10061) {
            getActivity().setResult(10061);
            getActivity().finish();
        }
    }


    /**
     * 注册广播,接受信鸽传来的预授权信息时候调用该BroadcastReceiver
     */
    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("authorize_yinlian");
        // 注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            doPaySucceed(intent.getStringExtra("content"));
        }
    };


    /**
     * 处理支付成功
     */
    private void doPaySucceed(String content) {
        MyUtils.showToast(mContext, "支付成功");
        update();
    }

    /**
     * 知道了窗口
     */
    public void showMessageDialog(String messge) {
        if (warnDlg == null) {
            warnDlg = new Dialog(mContext, R.style.MyDialog);
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_message, null);
            TextView tv_cancel_txt = (TextView) view
                    .findViewById(R.id.tv_do_txt);
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (createOrderVO != null && createOrderVO.getStayType() != 3) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CreateOrderVO", createOrderVO);
                        startActivityForResult(ChooseStationAty.class, bundle, 3001);
                    }
                    warnDlg.dismiss();
                }
            });
            final TextView tv_number = (TextView) view
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            warnDlg.setCanceledOnTouchOutside(false);
            warnDlg.setContentView(view);
        }
        warnDlg.show();

        Window dlgWindow = warnDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        WXPayEntryActivity.setWxPayCallBackListener(null);

        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


    private void showCarView(int stationType, LatLng latLng, double left) {
//        mapView.getMap().clear();
//        if (mapCarView == null) {
        mapCarView = LayoutInflater.from(mContext).inflate(R.layout.create_order_map_car, null);
//        }
        switch (stationType) {
            case 1:
                ((TextView) mapCarView.findViewById(R.id.create_order_car_backstation)).setText("原站还车");
                break;
            case 2:
                ((TextView) mapCarView.findViewById(R.id.create_order_car_backstation)).setText("站点还车");
                break;
            case 3:
                ((TextView) mapCarView.findViewById(R.id.create_order_car_backstation)).setText("自由还车");
                break;
        }
//        ((TextView) mapCarView.findViewById(R.id.create_order_car_left)).setText(((int) left) + "");
//        InfoWindow infoWindow = new InfoWindow(BitmapDescriptorFactory.fromBitmap(ViewToBitMap(mapCarView, LinearLayout.LayoutParams.WRAP_CONTENT, MyUtils.dip2px(mContext, 58))), latLng, -5, null);
//        mapView.getMap().showInfoWindow(infoWindow);

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
        layout.removeAllViews();
        layout.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    //更多支付方式
    public void choosePayChannel() {
        if (paytypeDlg == null) {
            paytypeDlg = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.dlg_deposit_pay, null);
            final int cFullFillWidth = 10000;
            view.setMinimumWidth(cFullFillWidth);


//            LinearLayout ll_recharge_unpay = (LinearLayout) view.findViewById(R.id.ll_recharge_unpay);


            LinearLayout ll_recharge_ali = (LinearLayout) view
                    .findViewById(R.id.ll_recharge_ali);
            LinearLayout ll_recharge_weixin = (LinearLayout) view
                    .findViewById(R.id.ll_recharge_weixin);
            TextView tv_cancel = (TextView) view
                    .findViewById(R.id.tv_cancel);

//            ll_recharge_unpay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    payIcon.setImageResource(R.drawable.icon_unpay);
//                    paytext.setText("使用信用卡预授权");
//                    paytypeDlg.dismiss();
//                    //payDeposit(stateReturnContentBean.getDepositAmount(), 0);
//                    payinfo.setText(PROMPT_DEPOSIT_unpay);
//                    submitView.setTag(new String("0"));
//
//                }
//            });

            ll_recharge_ali.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    payIcon.setImageResource(R.drawable.zhifubao);
                    paytext.setText("使用支付宝");
                    //payDeposit(stateReturnContentBean.getDepositAmount(), 1);
                    paytypeDlg.dismiss();
                    payinfo.setText(PROMPT_DEPOSIT_ALIWX);
                    submitView.setTag(new String("1"));
                }
            });
            ll_recharge_weixin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    payIcon.setImageResource(R.drawable.weixin);
                    paytext.setText("使用微信");
                    //payDeposit(stateReturnContentBean.getDepositAmount(), 2);
                    paytypeDlg.dismiss();
                    payinfo.setText(PROMPT_DEPOSIT_ALIWX);
                    submitView.setTag(new String("2"));
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    paytypeDlg.dismiss();
                }
            });
            paytypeDlg.setContentView(view);
            paytypeDlg.setCanceledOnTouchOutside(true);
            paytypeDlg.setCancelable(true);
        }
        paytypeDlg.show();
        Window w = paytypeDlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        paytypeDlg.onWindowAttributesChanged(lp);

    }


    /**
     * 支付押金
     *
     * @param price
     * @param channel
     */
    private void payDeposit(double price, int channel) {
        switch (channel) {
            case 0:
                //信用卡
                WXPayEntryActivity.setWxPayCallBackListener(null);
                dialogShow();
                Map<String, String> unpay = new HashMap<String, String>();
                unpay.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.BUILDUNIONPAYSDK,
                        unpay, this);
                break;
            case 1:
                //支付宝
                WXPayEntryActivity.setWxPayCallBackListener(null);
                dialogShow();
                Map<String, String> alipay = new HashMap<String, String>();
                alipay.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                alipay.put("payType", "alipay");
                YYRunner.getData(2002, YYRunner.Method_POST, YYUrl.BUILDPAYFORFOREGIFT,
                        alipay, this);
                break;
            case 2:
                //微信
                WXPayEntryActivity.setWxPayCallBackListener(this);
                dialogShow();
                Map<String, String> wxpay = new HashMap<String, String>();
                wxpay.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                wxpay.put("payType", "wxpay");
                YYRunner.getData(2003, YYRunner.Method_POST, YYUrl.BUILDPAYFORFOREGIFT,
                        wxpay, this);
                break;
        }

    }


    private void update() {
        dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(3001, YYRunner.Method_POST,
                YYUrl.GETRENTERSTATE, map, CreateOrderFrag.this);
    }


    private void cancelDeposit() {
        dialogShow();
        Map<String, String> backFeeparams = new HashMap<String, String>();
        backFeeparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        backFeeparams.put("flag", "1");
        YYRunner.getData(3002, YYRunner.Method_POST, YYUrl.APPLICATIONWITHDRAWAL,
                backFeeparams, this);
    }

    //微信支付返回结果
    @Override
    public void OnBackListener(int status, String message) {
        switch (status) {
            case 0:
                doPaySucceed(message);
                break;

            default:
                if (!TextUtils.isEmpty(message)) {
                    MyUtils.showToast(mContext, message);
                }
                break;
        }
    }
}
