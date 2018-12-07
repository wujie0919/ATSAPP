package com.dzrcx.jiaan.Order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.BenefitItemVo;
import com.dzrcx.jiaan.Bean.OrderDetailBean;
import com.dzrcx.jiaan.Bean.OrderDetailVo;
import com.dzrcx.jiaan.Bean.PayResult;
import com.dzrcx.jiaan.Bean.SpitslotBean;
import com.dzrcx.jiaan.Bean.WXPayInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PayUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.wxapi.WXPayEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PayFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface, WXPayCallBackListener {
    private DecimalFormat df = new DecimalFormat("#0.0");
    private View PayView;
    private TextView barTitle, tv_right, tv_order_todetail;

    private ImageView carImage;
    private ImageView iv_left_raw;
    private TextView carMode, carNo, spendTime, mileageSum;
    private TextView orderTime0, orderTime1, orderTime2;
    private TextView orderPrice0, orderPrice2;

    /**
     * 显示优惠细节
     */
    private LinearLayout benefitLayout;


    private ImageView takePayImage;
    private TextView takaPaytext, orderPrice3;


    private RelativeLayout layoutPayUser, layoutPayAli, layoutPayWX;
    private TextView orderPayButton;


    private boolean isPaySucceed = false;
    private String orderid;
    private long laterTime;
    private Intent intent;
    private Dialog messageDialog;

    private OrderDetailVo orderDetailVo;

    Handler header = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 10001:

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        doPaySucceed();
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
                case 20001:
                    finish();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intent = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (PayView == null) {
            PayView = inflater.inflate(R.layout.aty_paymethod, null);
            initView();
        }
        return PayView;
    }

    private void initView() {
        iv_left_raw = (ImageView) PayView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        barTitle = (TextView) PayView.findViewById(R.id.tv_title);
        barTitle.setText("行程结算");

        tv_right = (TextView) PayView.findViewById(R.id.tv_right);
        tv_right.setText("对账单有异议");
        tv_right.setVisibility(View.VISIBLE);


        carImage = (ImageView) PayView.findViewById(R.id.iv_order_car);

        carMode = (TextView) PayView.findViewById(R.id.tv_order_carmode);
        carNo = (TextView) PayView.findViewById(R.id.tv_order_carno);
        spendTime = (TextView) PayView.findViewById(R.id.tv_order_spendtime);
        mileageSum = (TextView) PayView.findViewById(R.id.tv_order_left);

        orderTime0 = (TextView) PayView.findViewById(R.id.tv_order_time0);
        orderTime1 = (TextView) PayView.findViewById(R.id.tv_order_time1);
        orderTime2 = (TextView) PayView.findViewById(R.id.tv_order_time2);

        orderPrice0 = (TextView) PayView.findViewById(R.id.tv_order_price0);
        orderPrice2 = (TextView) PayView.findViewById(R.id.tv_order_price2);
        orderPrice3 = (TextView) PayView.findViewById(R.id.tv_order_price3);
        tv_order_todetail = (TextView) PayView.findViewById(R.id.tv_order_todetail);
        tv_order_todetail.setOnClickListener(this);

        benefitLayout = (LinearLayout) PayView.findViewById(R.id.ly_order_benefit);


        takePayImage = (ImageView) PayView.findViewById(R.id.iv_order_pay_temp_0);
        takaPaytext = (TextView) PayView.findViewById(R.id.tv_order_pay_temp_0);


        layoutPayUser = (RelativeLayout) PayView.findViewById(R.id.layout_order_pay_user);
        layoutPayAli = (RelativeLayout) PayView.findViewById(R.id.layout_order_pay_ali);
        layoutPayWX = (RelativeLayout) PayView.findViewById(R.id.layout_order_pay_wx);

        layoutPayUser.setVisibility(View.GONE);
        layoutPayAli.setVisibility(View.GONE);
        layoutPayWX.setVisibility(View.GONE);


        orderPayButton = (TextView) PayView.findViewById(R.id.order_pay_button);
        orderid = intent.getExtras().getString("orderId");
        MobclickAgent.onEvent(mContext, "open_pay");

        PayView.findViewById(R.id.iv_left_raw).setOnClickListener(this);
        layoutPayUser.setOnClickListener(this);
        layoutPayAli.setOnClickListener(this);
        layoutPayWX.setOnClickListener(this);
        orderPayButton.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        updateOrderInfo();
        getDocument();
        toCheckCar(orderid);
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        WXPayEntryActivity.setWxPayCallBackListener(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        WXPayEntryActivity.setWxPayCallBackListener(null);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left_raw:
                startActivity(MainActivity2_1.class, null);
                finish();
                break;
            case R.id.tv_right:
                showCallDialog();
                break;
            case R.id.layout_order_pay_user:

                layoutPayUser.setSelected(true);
                layoutPayAli.setSelected(false);
                layoutPayWX.setSelected(false);

                break;

            case R.id.layout_order_pay_ali:
                if (!TextUtils.isEmpty(orderid)) {

                    layoutPayUser.setSelected(false);
                    layoutPayAli.setSelected(true);
                    layoutPayWX.setSelected(false);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }
                break;
            case R.id.layout_order_pay_wx:
                if (!TextUtils.isEmpty(orderid)) {

                    layoutPayUser.setSelected(false);
                    layoutPayAli.setSelected(false);
                    layoutPayWX.setSelected(true);

                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }

                break;
            case R.id.order_pay_button:
                if (!TextUtils.isEmpty(orderid)) {


                    String tag = (String) v.getTag();
                    if (!TextUtils.isEmpty(tag)) {
                        if ("0.00".equals(tag)) {
                            requestUpdataOrderState(4, 3001);
                        } else {

                            if (layoutPayUser.isSelected()) {
                                //个人账户支付
                                payByUserAccount();

                            } else if (layoutPayAli.isSelected()) {
                                //支付宝支付
                                payByAli();

                            } else if (layoutPayWX.isSelected()) {
                                //微信支付
                                payByWX();

                            }

                        }
                    } else {

                    }

                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }
                break;
            case R.id.tv_order_todetail:
                if (orderDetailVo != null) {
                    showMessageDialog(orderDetailVo);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        // System.err.println(error);
        dismmisDialog();
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        dismmisDialog();
        switch (tag) {

            case 2001:
                dismmisDialog();
                OrderDetailBean orderDetailBean = (OrderDetailBean) GsonTransformUtil.fromJson(
                        json, OrderDetailBean.class);
                if (orderDetailBean != null && orderDetailBean.getErrno() == 0) {
                    orderDetailVo = orderDetailBean.getReturnContent();
                    showData(orderDetailVo);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
                    header.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateOrderInfo();
                        }
                    }, 1000);
                }

                break;

            case 1001:
                ALiPayInfoBean aLiPayInfoBean = (ALiPayInfoBean) GsonTransformUtil
                        .fromJson(json, ALiPayInfoBean.class);

                if (aLiPayInfoBean != null && aLiPayInfoBean.getReturnContent() != null) {

                    switch (aLiPayInfoBean.getErrno()) {
                        case 0:
                            PayUtil.alipay(getActivity(), header, 10001,
                                    aLiPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, aLiPayInfoBean.getError());
                            break;
                    }

                }

                break;
            case 1002:

                WXPayInfoBean wxPayInfoBean = (WXPayInfoBean) GsonTransformUtil
                        .fromJson(json, WXPayInfoBean.class);

                if (wxPayInfoBean != null && wxPayInfoBean.getReturnContent() != null) {

                    switch (wxPayInfoBean.getErrno()) {
                        case 0:
                            PayUtil.WXPay(mContext, wxPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, wxPayInfoBean.getError());
                            break;
                    }

                }

                break;
            case 1003://个人账户支付返回结果
                YYBaseResBean yyBaseResBean = GsonTransformUtil
                        .fromJson(json, YYBaseResBean.class);
                if (yyBaseResBean != null) {
                    switch (yyBaseResBean.getErrno()) {
                        case 0:
                            doPaySucceed();
                            break;

                        default:
                            MyUtils.showToast(mContext, yyBaseResBean.getError());
                            break;
                    }
                }
                break;
            case 3001:
                YYBaseResBean payResBean = GsonTransformUtil
                        .fromJson(json, YYBaseResBean.class);
                if (payResBean != null) {
                    switch (payResBean.getErrno()) {
                        case 0:
                            doPaySucceed();
                            break;

                        default:
                            MyUtils.showToast(mContext, payResBean.getError());
                            break;
                    }
                }
                break;
            case 4001:

                SpitslotBean sBean = (SpitslotBean) GsonTransformUtil
                        .fromJson(json, SpitslotBean.class);
                if (sBean != null) {
                    switch (sBean.getErrno()) {
                        case 0:
                            SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_SPITSLOT, sBean.getReturnContent());
                            break;

                        default:
                            break;
                    }
                }


                break;
            default:
                break;
        }

    }

    @Override
    public void OnBackListener(int status, String message) {
        // TODO Auto-generated method stub
        switch (status) {
            case 0:
                doPaySucceed();
                break;

            default:
                if (!TextUtils.isEmpty(message)) {
                    MyUtils.showToast(mContext, message);
                }
                break;
        }

    }


    public void finish() {
        // TODO Auto-generated method stub
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra("isPaySucceed", isPaySucceed);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                }
            }
        }, 1000);

    }

    /**
     * 处理支付成功
     */
    private void doPaySucceed() {
        MyUtils.showToast(mContext, "支付成功");
        YYApplication.TAGorder = "notcomplete";
        YYConstans.currentOrderDetailVo = null;
        YYConstans.hasUnFinishOrder = false;
        isPaySucceed = true;


        Bundle bundle = new Bundle();
        bundle.putSerializable("OrderDetailVo", orderDetailVo);
        startActivity(RatedUsAty.class, bundle);

        header.sendEmptyMessageDelayed(20001, 1000);
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }


    private void updateOrderInfo() {
        if (!TextUtils.isEmpty(orderid)) {
            dialogShow();
            Map<String, String> params = new HashMap<String, String>();
            params.put("orderId", orderid);
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            YYRunner.getData(2001, YYRunner.Method_POST,
                    YYUrl.REVIEWORDER, params, PayFrg.this);
        }
    }


    private void showData(OrderDetailVo orderDetailVo) {

        if (orderDetailVo != null && "1".equals(orderDetailVo.getForePhoto())) {
            Bundle bundlecar = new Bundle();
            bundlecar.putString("orderID", orderDetailVo.getOrderId());
            bundlecar.putString("photoScene", "1");
            Intent intent = new Intent(getActivity(), CheakCarAct.class);
            intent.putExtras(bundlecar);
            startActivity(intent);
        }

        if (orderDetailVo == null) {
            return;
        } else {

            ImageLoader.getInstance().displayImage(orderDetailVo.getCarPhoto(), carImage);

            carMode.setText(orderDetailVo.getMake() + orderDetailVo.getModel());
            carNo.setText(orderDetailVo.getLicense());

            long[] time = TimeDateUtil.formatTime_HM(orderDetailVo.getFeeDetail().getCostTime());
            if (time != null) {
                if (time.length == 2) {
                    spendTime.setText(time[0] + "时" + time[1] + "分");
                } else {
                    spendTime.setText(time[0] + "分");
                }
            }


            mileageSum.setText(((int) orderDetailVo.getFeeDetail().getDistance()) + "公里");

            orderTime0.setText(TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getOrderTime(), "MM-dd HH:mm:ss"));
            orderTime1.setText(TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getChargeTime(), "MM-dd HH:mm:ss"));
            orderTime2.setText(TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getReturnTime(), "MM-dd HH:mm:ss"));


            orderPrice0.setText(MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getAllPrice()) + "元");
            orderPrice2.setText(MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getPayPrice()) + "元");
            /**
             * 优惠款项
             */
            benefitLayout.removeAllViews();
            if (orderDetailVo.getFeeDetail().getBenefitItems() != null && orderDetailVo.getFeeDetail().getBenefitItems().size() > 0) {
                for (BenefitItemVo itemVo : orderDetailVo.getFeeDetail().getBenefitItems()) {
                    if (itemVo != null) {
                        View benefitView = LayoutInflater.from(mContext).inflate(R.layout.item_order_benefit, null);
                        ((TextView) benefitView.findViewById(R.id.tv_order_benefitname)).setText(itemVo.getBenefitName());
                        ((TextView) benefitView.findViewById(R.id.tv_order_benefitprice)).setText("-" + MyUtils.formatPriceLong(itemVo.getBenefitAmount()) + "元");
                        benefitLayout.addView(benefitView);
                    }
                }
            } else {
                View benefitView = LayoutInflater.from(mContext).inflate(R.layout.item_order_benefit, null);
                ((TextView) benefitView.findViewById(R.id.tv_order_benefitname)).setText("使用优惠券");
                ((TextView) benefitView.findViewById(R.id.tv_order_benefitprice)).setText("-" + MyUtils.formatPriceLong(0) + "元");
                benefitLayout.addView(benefitView);
            }

            if (orderDetailVo.getOrderType() == 0) {
                takePayImage.setImageResource(R.drawable.icon_pay_account);
                takaPaytext.setText("个人账户余额抵扣");
                orderPrice3.setText("-" + MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getPayDetail().getUserPay()) + "元");
            } else {
                takePayImage.setImageResource(R.drawable.mapcarfrg_businesspay);
                takaPaytext.setText(orderDetailVo.getFeeDetail().getPayDetail().getEntName() + "账户余额抵扣");
                orderPrice3.setText("-" + MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getPayDetail().getEntPay()) + "元");
            }

            if (orderDetailVo.getFeeDetail().getNeedPay() > 0) {

                if (orderDetailVo.getOrderType() != 0) {
                    layoutPayUser.setVisibility(View.VISIBLE);
                    layoutPayAli.setVisibility(View.VISIBLE);
                    layoutPayWX.setVisibility(View.VISIBLE);
                    layoutPayUser.setSelected(true);
                    layoutPayAli.setSelected(false);
                    layoutPayWX.setSelected(false);
                } else {
                    layoutPayUser.setVisibility(View.GONE);
                    layoutPayAli.setVisibility(View.VISIBLE);
                    layoutPayWX.setVisibility(View.VISIBLE);
                    layoutPayUser.setSelected(false);
                    layoutPayAli.setSelected(true);
                    layoutPayWX.setSelected(false);
                }
            } else {

                if (orderDetailVo.getOrderType() != 0) {
                    layoutPayUser.setVisibility(View.VISIBLE);
                    layoutPayUser.setOnClickListener(null);
                } else {
                    layoutPayUser.setVisibility(View.GONE);
                }

                layoutPayAli.setVisibility(View.VISIBLE);
                layoutPayWX.setVisibility(View.VISIBLE);
                layoutPayAli.setOnClickListener(null);
                layoutPayWX.setOnClickListener(null);
                layoutPayUser.setSelected(false);
                layoutPayAli.setSelected(false);
                layoutPayWX.setSelected(false);
            }

            orderPayButton.setTag(MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getNeedPay()));
            orderPayButton.setText("确认支付" + MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getNeedPay()) + "元");
        }


    }


    /**
     * 改变订单状态,更新订单状态(支付完成，state=4)
     * updateOrderState.do
     * 1等待取车
     * 2正在使用
     * 3等待支付
     * 4订单完成
     * 5订单取消
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


    /**
     * 余额支付
     */
    private void payByUserAccount() {
        if (NetHelper.checkNetwork(mContext)) {
            showNoNetDlg();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("orderId", orderid);
        wxparams.put("payType", "balance");
        YYRunner.getData(1003, YYRunner.Method_POST, YYUrl.GETYUEPAY,
                wxparams, this);
    }

    /**
     * 支付宝支付
     */
    private void payByAli() {
        dialogShow();
        Map<String, String> alipara = new HashMap<String, String>();
        alipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        alipara.put("orderId", orderid);
        alipara.put("payType", "alipay");
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.BUILDPAY,
                alipara, this);
    }

    /**
     * 微信支付
     */
    private void payByWX() {
        dialogShow();
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("orderId", orderid);
        wxparams.put("payType", "wxpay");
        YYRunner.getData(1002, YYRunner.Method_POST, YYUrl.BUILDPAY,
                wxparams, this);
    }


    /**
     * 获取评论时的文案
     */
    private void getDocument() {
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("flag", "evaluateLabel");
        YYRunner.getData(4001, YYRunner.Method_POST, YYUrl.GETDOCUMENT,
                wxparams, this);
    }


    private void toCheckCar(String orderid) {

        String s = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_CHECKCAR_LASTORDER, "");
        if (!orderid.equals(s)) {
            Bundle bundle = new Bundle();
            bundle.putString("orderID", orderid);
            bundle.putString("photoScene", "2");
        }
        SharedPreferenceTool.setPrefString(mContext, SharedPreferenceTool.KEY_CHECKCAR_LASTORDER, orderid);

    }


    /**
     * 知道了窗口
     */
    public void showMessageDialog(OrderDetailVo orderDetailVo) {
        if (messageDialog == null) {
            messageDialog = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_feedetail_more, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            TextView tv_title = (TextView) mDlgCallView
                    .findViewById(R.id.tv_title);
            tv_title.setText("计费明细");
            TextView tv_usecartime = (TextView) mDlgCallView
                    .findViewById(R.id.tv_usecartime);
            TextView tv_usecarmileage = (TextView) mDlgCallView
                    .findViewById(R.id.tv_usecarmileage);
            TextView tv_mileagefee = (TextView) mDlgCallView
                    .findViewById(R.id.tv_mileagefee);
            TextView tv_usecarfee = (TextView) mDlgCallView
                    .findViewById(R.id.tv_usecarfee);

            tv_usecartime.setText(TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getCostTime()).trim());
            if (orderDetailVo.getFeeDetail().getCostTime() < 60000) {
                tv_usecartime.setTextColor(Color.parseColor("#a6a6a6"));
            }
            tv_usecarmileage.setText(((int) Math.ceil(orderDetailVo.getFeeDetail().getDistance())) + "公里");
            if (orderDetailVo.getFeeDetail().getDistance() == 0) {
                tv_usecarmileage.setTextColor(Color.parseColor("#a6a6a6"));
            }
            tv_mileagefee.setText(orderDetailVo.getFeeDetail().getDistancePrice() + "元");
            if (orderDetailVo.getFeeDetail().getDistancePrice() == 0) {
                tv_mileagefee.setTextColor(Color.parseColor("#a6a6a6"));
            }
            tv_usecarfee.setText(orderDetailVo.getFeeDetail().getAllPrice() + "元");
            if (orderDetailVo.getFeeDetail().getAllPrice() == 0) {
                tv_usecarfee.setTextColor(Color.parseColor("#a6a6a6"));
            }
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    messageDialog.dismiss();
                }
            });
            messageDialog.setCanceledOnTouchOutside(false);
            messageDialog.setContentView(mDlgCallView);
        }
        messageDialog.show();

        Window dlgWindow = messageDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }


}
