package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.CreateOrderVO;
import com.dzrcx.jiaan.Bean.OrderListBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Bean.PayResult;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.WXPayInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.PayUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.utils.TimeUtil;
import com.dzrcx.jiaan.wxapi.WXPayEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenh on 2017/1/4.
 * <p>
 * isRelet是其他页面intent传递过来的booler值key=PushDialogActivity 如果是true说明是续租页面 如果false说明是正常日租页面
 */
public class DayShareActivity extends YYBaseActivity {
    private Context mContext = this;
    private CreateOrderVO createOrderVO;
    private TextView titleView, tv_total_text;
    private ImageView iv_left_raw;
    //                使用天数    取车时间                              还车时间                                                共计费用    没钱提示   //账户名称         //底部提示语
    private TextView tv_use_day, tv_get_car_time, tv_get_car_time_text, tv_give_back_car_time, tv_give_back_car_time_text, tv_total, tv_no_money, tv_account_name, tv_hint, tv_setup_user_car_time_text;
    //                 减号     加号    账户支付图片     微信支付图片     支付宝支付图片
    private ImageView iv_sub, iv_add, iv_account_pay, iv_weixin_pay, iv_ali_pay;
    //                    账户支付布局     微信支付布局    支付宝支付布局
    private LinearLayout ll_account_pay, ll_weixin_pay, ll_ali_pay;
    private LinearLayout ll_sub, ll_add, ll_coupons;

    private Button btn_commit;//确认提交/
    private int usesDays = 1;//初始化用车天数
    private PayType payType = PayType.ACCOUNT_PAY;//默认个人账户支付

    public enum PayType {//支付类型
        //个人账户支付 微信支付    支付宝支付 企业账户支付
        ACCOUNT_PAY, WEIXIN_PAY, ALI_PAY, ENTERPRISE
    }

    private boolean isRelet = false;//是否是续租
    private boolean isFromOrderList = false;//是否是续租
    private OrderListBean listBean;//订单列表信息
    private OrderListItemBean listItemBean;//订单列表信息

    private double blance;//账户余额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_share);

        Bundle bundle = getIntent().getExtras();
        createOrderVO = (CreateOrderVO) bundle.getSerializable("createOrderVO");
        isFromOrderList = getIntent().getBooleanExtra("isFromOrderList", false);//是否来自订单列表
        isRelet = getIntent().getBooleanExtra("PushDialogActivity", false);//是否是续租
        blance = YYConstans.getUserInfoBean().getReturnContent().getUser().getBalance();
        initView();//初始化控件
        if (isRelet) {//如果是续租 或者 订单列表过来的
            ll_coupons.setVisibility(View.GONE);
            tv_total_text.setText("延长用车费用");
            requestOrderList();
        } else {//如果是不是续租
            initTitleView("创建订单");//设置标题
            setupView();//设置控件点击事件等
        }

        getUserInfo();
    }

    /**
     * 获取个人信息
     */
    private void getUserInfo() {
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("lng", YYApplication.Longitude + "");
        wxparams.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(111111, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, new RequestInterface() {
                    @Override
                    public void onError(int tag, String error) {

                    }

                    @Override
                    public void onComplete(int tag, String json) {
                        UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                                .fromJson(json, UserInfoBean.class);
                        if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                            YYConstans.setUserInfoBean(userInfoBean);
                            //updataPersonalBalance(userInfoBean);
                        }
                    }

                    @Override
                    public void onLoading(long count, long current) {

                    }
                });
    }

    //初始化标题并接收Bundle数据
    private void initTitleView(String str) {
        //标题
        titleView = (TextView) findViewById(R.id.tv_title);
        StringBuffer titleBuf = new StringBuffer();
        titleBuf.append(createOrderVO.getMake() + createOrderVO.getModel());
        titleBuf.append(" | ").append(createOrderVO.getLicense());
        int a = titleBuf.length();
        titleBuf.append("\n");
        int b = titleBuf.length();
        titleBuf.append(str);
        Spannable spannable = new SpannableString(titleBuf.toString());
        spannable.setSpan(new AbsoluteSizeSpan(MyUtils.sp2px(this, 17)), 0, a, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(MyUtils.sp2px(this, 12)), b, b + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleView.setText(spannable);
        //返回按钮
        iv_left_raw = (ImageView) findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //初始控件
    private void initView() {
        tv_use_day = (TextView) findViewById(R.id.tv_use_day);
        tv_total_text = (TextView) findViewById(R.id.tv_total_text);
        tv_setup_user_car_time_text = (TextView) findViewById(R.id.tv_setup_user_car_time_text);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_account_name = (TextView) findViewById(R.id.tv_account_name);
        tv_get_car_time = (TextView) findViewById(R.id.tv_get_car_time);
        tv_get_car_time_text = (TextView) findViewById(R.id.tv_get_car_time_text);
        tv_give_back_car_time = (TextView) findViewById(R.id.tv_give_back_car_time);
        tv_give_back_car_time_text = (TextView) findViewById(R.id.tv_give_back_car_time_text);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_no_money = (TextView) findViewById(R.id.tv_no_money);
        iv_sub = (ImageView) findViewById(R.id.iv_sub);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_account_pay = (ImageView) findViewById(R.id.iv_account_pay);
        iv_weixin_pay = (ImageView) findViewById(R.id.iv_weixin_pay);
        iv_ali_pay = (ImageView) findViewById(R.id.iv_ali_pay);
        ll_account_pay = (LinearLayout) findViewById(R.id.ll_account_pay);
        ll_weixin_pay = (LinearLayout) findViewById(R.id.ll_weixin_pay);
        ll_sub = (LinearLayout) findViewById(R.id.ll_sub);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        ll_ali_pay = (LinearLayout) findViewById(R.id.ll_ali_pay);
        ll_coupons = (LinearLayout) findViewById(R.id.ll_coupons);
        btn_commit = (Button) findViewById(R.id.btn_commit);
    }

    //设置控件点击事件等或初始值
    private void setupView() {

        if (!isRelet) {//如果不是续租
            if (!TextUtils.isEmpty(createOrderVO.getAccountName())) {
                tv_account_name.setText(createOrderVO.getAccountName());
            } else {
                tv_account_name.setText("使用个人账户余额");
            }
        } else if (createOrderVO != null) {//如果是续租

            if (!TextUtils.isEmpty(createOrderVO.getPayEnterpriseName())) {
                tv_account_name.setText(createOrderVO.getPayEnterpriseName());
            } else {
                tv_account_name.setText("使用个人账户余额");
            }
        }

        tv_no_money.setVisibility(View.GONE);
        iv_account_pay.setVisibility(View.VISIBLE);
        ll_account_pay.setEnabled(true);
        //更新和用车时间有关的VIew
        updateUseCarView();
        //减号点击事件
        ll_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usesDays <= 1) {
                    return;
                }
                usesDays -= 1;
                updateUseCarView();
            }
        });
        //加号点击事件
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usesDays >= 99) {
                    return;
                }
                usesDays += 1;
                updateUseCarView();
            }
        });
        //更新和账户切换有关的页面
        updatePayImageView();
        //支付类型布局点击事件
        //个人账户
        ll_account_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payType = PayType.ACCOUNT_PAY;
                updatePayImageView();
            }
        });
        //微信
        ll_weixin_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payType = PayType.WEIXIN_PAY;
                updatePayImageView();
            }
        });
        //支付宝
        ll_ali_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payType = PayType.ALI_PAY;
                updatePayImageView();
            }
        });

        //确认支付
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submintOrder();
            }
        });

    }

    //更新和用车时间有关的View
    private void updateUseCarView() {
//        tv_use_day.setText(usesDays + "天");
//        tv_get_car_time.setText(TimeUtil.getData(0));
//        tv_give_back_car_time.setText(TimeUtil.getData(usesDays));
//        //更新用车总价
//        Double useMoney = createOrderVO.getDailyRentalPrice();
//        useMoney *= usesDays;
//        DecimalFormat df = new DecimalFormat("###,###.0");
//        String formatMoney = df.format(useMoney);
//        tv_total.setText(formatMoney + "元");
//        btn_commit.setText("确认支付(" + formatMoney + "元)");


        if (!isRelet) {//如果不是续租
            tv_use_day.setText(usesDays + "天");
            tv_get_car_time.setText(TimeUtil.getData(0));
            tv_give_back_car_time.setText(TimeUtil.getData(usesDays));
            //更新用车总价
            Double useMoney = createOrderVO.getDailyRentalPrice();
            useMoney *= usesDays;
            DecimalFormat df = new DecimalFormat("###,###.0");
            String formatMoney = df.format(useMoney);
            tv_total.setText(formatMoney + "元");
            btn_commit.setText("确认支付(" + formatMoney + "元)");

            if (blance < usesDays * useMoney && "使用个人账户余额".equals(tv_account_name.getText().toString())) {
                tv_no_money.setVisibility(View.VISIBLE);
            }else{
                tv_no_money.setVisibility(View.GONE);
            }
        } else if (createOrderVO != null) {//如果是续租
            tv_use_day.setText(usesDays + "天");
            tv_get_car_time.setText(TimeUtil.getSpecifiedDay(createOrderVO.getCreateTime(), createOrderVO.getRentedDayNumber()));
            tv_give_back_car_time.setText(TimeUtil.getSpecifiedDay(createOrderVO.getCreateTime(), usesDays + createOrderVO.getRentedDayNumber()));
            //更新用车总价
            Double useMoney = createOrderVO.getDailyRentalPrice();
            useMoney *= usesDays;
            DecimalFormat df = new DecimalFormat("###,###.0");
            String formatMoney = df.format(useMoney);
            tv_total.setText(formatMoney + "元");
            btn_commit.setText("确认支付(" + formatMoney + "元)");

            if (blance < usesDays * useMoney && "使用个人账户余额".equals(tv_account_name.getText().toString())) {
                tv_no_money.setVisibility(View.VISIBLE);
            }else{
                tv_no_money.setVisibility(View.GONE);
            }
        }
    }

    //更新选择支付模式的图片
    private void updatePayImageView() {
        if (payType == PayType.ACCOUNT_PAY) {
            iv_account_pay.setBackgroundResource(R.drawable.check_green);//
            iv_weixin_pay.setBackgroundResource(R.drawable.no_check_gray);
            iv_ali_pay.setBackgroundResource(R.drawable.no_check_gray);
        } else if (payType == PayType.WEIXIN_PAY) {
            iv_account_pay.setBackgroundResource(R.drawable.no_check_gray);
            iv_weixin_pay.setBackgroundResource(R.drawable.check_green);//
            iv_ali_pay.setBackgroundResource(R.drawable.no_check_gray);
        } else if (payType == PayType.ALI_PAY) {
            iv_account_pay.setBackgroundResource(R.drawable.no_check_gray);
            iv_weixin_pay.setBackgroundResource(R.drawable.no_check_gray);
            iv_ali_pay.setBackgroundResource(R.drawable.check_green);//
        }
    }


    //支付宝支付回调结果
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

    private WXPayCallBackListener wxPayCallBackListener = new WXPayCallBackListener() {
        @Override
        public void OnBackListener(int status, String message) {
            switch (status) {
                case 0:
                    doPaySucceed("支付成功");
                    break;

                default:
                    if (!TextUtils.isEmpty(message)) {
                        MyUtils.showToast(mContext, message);
                    }
                    break;
            }
        }
    };

    /**
     * 统一处理支付成功后结果
     */
    private void doPaySucceed(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DayShareActivity.this, OrderAty.class);
        startActivity(intent);
        setResult(Activity.RESULT_OK);
        finish();
    }


    /**
     * 提交订单
     */
    private void submintOrder() {
        dialogShow();
        final Map<String, String> params = new HashMap<String, String>();
        //支付方式
        if (payType == PayType.ACCOUNT_PAY) {
            if (createOrderVO.getEntId() != 0) {
                params.put("payType", "ent");//如果有企业id说明是企业账户支付
            } else {
                params.put("payType", "user");//如果没有企业id则是个人账户支付
            }
        } else if (payType == PayType.WEIXIN_PAY) {
            params.put("payType", "wxpay");
        } else if (payType == PayType.ALI_PAY) {
            params.put("payType", "alipay");
        }
        params.put("carId", createOrderVO.getCarId() + "");//车辆id
        params.put("entId", createOrderVO.getEntId() + "");//企业id
        params.put("duration", usesDays + "");//持续天数
        params.put("fromStationId", createOrderVO.getFromStationId() + "");//原来站点id
        if (isRelet) {//是续租
            params.put("orderId", createOrderVO.getOrderId() + "");//如果为续租传订单id
        } else {//不是续租
            params.put("orderId", "");
        }
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(0, YYRunner.Method_POST,
                YYUrl.buildPayForDailyRental, params, new RequestInterface() {
                    @Override
                    public void onError(int tag, String error) {
                        dismmisDialog();
                        Log.e("error错误", error);
                        Toast.makeText(DayShareActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int tag, String json) {
                        dismmisDialog();
                        Log.e("success成功", json);
                        JSONObject jsonObject = null;
                        String errno = null;
                        try {
                            jsonObject = new JSONObject(json);
                            errno = jsonObject.getString("errno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!"0".equals(errno)) {
                            if ("10060".equals(errno)) {
                                //下手太慢
                                setResult(10061);
                                finish();
                                return;
                            } else if ("10061".equals(errno)) {
                                //车辆已经下线
                                setResult(10061);
                                finish();
                                return;
                            }
                        }
                        if (payType == PayType.ALI_PAY) {

                            ALiPayInfoBean aLiPayInfoBean = (ALiPayInfoBean) GsonTransformUtil
                                    .fromJson(json, ALiPayInfoBean.class);

                            if (aLiPayInfoBean != null) {

                                switch (aLiPayInfoBean.getErrno()) {
                                    case 0:
                                        PayUtil.alipay(DayShareActivity.this, mHandler, 10001,
                                                aLiPayInfoBean.getReturnContent().getReqCode());
                                        break;

                                    default:
                                        MyUtils.showToast(mContext, aLiPayInfoBean.getError());
                                        break;
                                }

                            }
                        } else if (payType == PayType.WEIXIN_PAY) {//微信支付
                            WXPayInfoBean wxPayInfoBean = (WXPayInfoBean) GsonTransformUtil
                                    .fromJson(json, WXPayInfoBean.class);

                            if (wxPayInfoBean != null) {

                                switch (wxPayInfoBean.getErrno()) {
                                    case 0:
                                        WXPayEntryActivity.setWxPayCallBackListener(wxPayCallBackListener);
                                        PayUtil.WXPay(mContext, wxPayInfoBean.getReturnContent().getReqCode());
                                        break;

                                    default:
                                        MyUtils.showToast(mContext, wxPayInfoBean.getError());
                                        break;
                                }

                            }
                        } else if (payType == PayType.ACCOUNT_PAY) { //个人账户支付
                            Log.e("ACCOUNT_PAY", json);
                            YYBaseResBean baseResBean = GsonTransformUtil.fromJson(
                                    json, YYBaseResBean.class);
                            if (baseResBean != null) {
                                if (baseResBean.getErrno() != 10001) {
                                    doPaySucceed("支付成功");
                                } else {
                                    Toast.makeText(mContext, "账户余额不足", Toast.LENGTH_SHORT).show();
                                    tv_no_money.setVisibility(View.VISIBLE);
                                    iv_account_pay.setVisibility(View.GONE);
                                    ll_account_pay.setEnabled(false);
                                }
                            } else {
                                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onLoading(long count, long current) {

                    }
                });
    }

    //获取订单列表 这里只取未完成订单
    private void requestOrderList() {
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("status", "0");//0获取进行中订单 1是已完成等 2是所有
        params.put("pageNo", "1");
        params.put("pageSize", "10");
        YYRunner.getData(YYConstans.TAG_ORDERLISTATY2_1, YYRunner.Method_POST,
                YYUrl.GETORDERLIST, params, new RequestInterface() {
                    @Override
                    public void onError(int tag, String error) {
                        dismmisDialog();
                        Log.e("error错误", error);
                        Toast.makeText(DayShareActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int tag, String json) {
                        dismmisDialog();
                        Log.e("success成功", json);
                        listBean = (OrderListBean) GsonTransformUtil.fromJson(
                                json, OrderListBean.class);
                        if (listBean == null || listBean.getErrno() != 0) {
                            MyUtils.showToast(mContext,
                                    listBean == null ? "数据传输错误，请重试" : listBean.getError() + "");
                            return;
                        } else {
                            if (listBean.getReturnContent().getOrderList().size() > 0) {
                                listItemBean = listBean.getReturnContent().getOrderList().get(0);//获取第一条未完成订单
                                if (listItemBean.getInCharge() == 1) {//进入了分时
                                    MyUtils.showToast(mContext, "订单已经进入分时");
                                    finish();
                                    return;
                                }
                                createOrderVO = new CreateOrderVO();
                                createOrderVO.setMake(listItemBean.getMake());
                                createOrderVO.setModel(listItemBean.getModel());
                                createOrderVO.setLicense(listItemBean.getLicense());
                                createOrderVO.setCarId(listItemBean.getCarId());
                                createOrderVO.setEntId(listItemBean.getPayEnterpriseId());//企业支付id
                                createOrderVO.setFromStationId(listItemBean.getFromStationId());//起始点id
                                createOrderVO.setOrderId(listItemBean.getOrderId());
                                createOrderVO.setPayEnterpriseName(listItemBean.getPayEnterpriseName());
                                createOrderVO.setDailyRentalPrice(listItemBean.getDailyRentalPrice());//日租价格
                                createOrderVO.setRentedDayNumber(listItemBean.getRentedDayNumber());//使用天数
                                createOrderVO.setCreateTime(listItemBean.getCreateTime());
                                initView();//初始化控件
                                setupView();//设置控件点击事件等
                                if (listItemBean.getDailyState() == 1) {//待支付
                                    initTitleView("创建订单");//设置标题
                                } else {//续租
                                    initTitleView("确认用车时间");//设置标题
                                    tv_hint.setVisibility(View.GONE);//隐藏底部提示语
                                    tv_get_car_time_text.setText("还车时间");
                                    tv_give_back_car_time_text.setText("延长时间");
                                    tv_setup_user_car_time_text.setText("请设置用车延长时间");
                                }
                            } else {
                                MyUtils.showToast(mContext, "订单已经过期");
                                finish();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onLoading(long count, long current) {

                    }
                }

        );
    }

}
