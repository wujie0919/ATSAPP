package com.dzrcx.jiaan.SearchCar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.PayResult;
import com.dzrcx.jiaan.Bean.UnionPayBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.WXPayInfoBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.PayUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.wxapi.WXPayEntryActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizeFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface, WXPayCallBackListener {
    private View AuthorizeView;
    private ImageView iv_left_raw;
    private TextView  barTitle;
    private RelativeLayout Ly_item0, Ly_item1;
    private ViewPager viewPager;


    private View payView;
    private TextView crruentCount, yuan;
    private EditText ed_Text;
    private LinearLayout aliLayout, weixinLayout;


    private View cardView;
    private TextView submit;

    private List<View> views;
    /**
     * 充值成功后的到账金额
     */
    private double allFee;
    private boolean isPaySucceed = false;
    private DecimalFormat df = new DecimalFormat("0.0");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (AuthorizeView == null) {
            AuthorizeView = inflater.inflate(R.layout.aty_authorize, null);
            registReceiver();
            iv_left_raw = (ImageView) AuthorizeView.findViewById(R.id.iv_left_raw);
            barTitle = (TextView) AuthorizeView.findViewById(R.id.tv_title);
            iv_left_raw.setVisibility(View.VISIBLE);
            Ly_item0 = (RelativeLayout) AuthorizeView.findViewById(R.id.searchcar_button1);
            Ly_item1 = (RelativeLayout) AuthorizeView.findViewById(R.id.searchcar_button2);
            viewPager = (ViewPager) AuthorizeView.findViewById(R.id.searchcar_vp);
            barTitle.setText("租车押金");


            Ly_item0.setSelected(true);
            Ly_item1.setSelected(false);

            views = new ArrayList<View>();
            payView = inflater.inflate(R.layout.auth_item_pay, null);
            crruentCount = (TextView) payView.findViewById(R.id.recharge_benefit);
            ed_Text = (EditText) payView.findViewById(R.id.recharge_edit);
            yuan = (TextView) payView.findViewById(R.id.yuan);
            aliLayout = (LinearLayout) payView.findViewById(R.id.recharge_ali);
            weixinLayout = (LinearLayout) payView.findViewById(R.id.recharge_weixin);

            cardView = inflater.inflate(R.layout.auth_item_cad, null);
            submit = (TextView) cardView.findViewById(R.id.auth_item_cad_submit);

            views.add(cardView);
            views.add(payView);

            iv_left_raw.setOnClickListener(this);

            Ly_item0.setOnClickListener(this);
            Ly_item1.setOnClickListener(this);

            aliLayout.setOnClickListener(this);
            weixinLayout.setOnClickListener(this);

            submit.setOnClickListener(this);
            initAdapter();
            registerBoradcastReceiver();
        }
        return AuthorizeView;
    }

    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("authorizeaty");
        mContext.registerReceiver(myrBroadcastReceiver, filter);
    }

    BroadcastReceiver myrBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            getActivity().finish();
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.auth_item_cad_submit:

//                if (!checkbox.isChecked()) {
//                    MyUtils.showToast(mContext, " ");
//                    return;
//                }
                dialogShow();
                Map<String, String> alipara = new HashMap<String, String>();
                alipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.BUILDUNIONPAYSDK,
                        alipara, this);
                break;
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.searchcar_button1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.searchcar_button2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.recharge_ali:
                recharge(ed_Text.getText().toString(), 2);
                break;
            case R.id.recharge_weixin:
                recharge(ed_Text.getText().toString(), 1);
                break;

            default:
                break;
        }
    }

    @Override
    public void onStop() {
        WXPayEntryActivity.setWxPayCallBackListener(null);
        super.onStop();
    }

    @Override
    public void onStart() {
        WXPayEntryActivity.setWxPayCallBackListener(this);
        super.onStart();
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                UnionPayBean unionPayBean = (UnionPayBean) GsonTransformUtil.fromJson(json, UnionPayBean.class);
                if (unionPayBean != null && unionPayBean.getErrno() == 0) {
                    PayUtil.unpay(mContext, unionPayBean.getReturnContent());
                }
                break;
            case 2002:
                ALiPayInfoBean aLiPayInfoBean = (ALiPayInfoBean) GsonTransformUtil
                        .fromJson(json, ALiPayInfoBean.class);

                if (aLiPayInfoBean != null) {

                    switch (aLiPayInfoBean.getErrno()) {
                        case 0:
                            allFee = aLiPayInfoBean.getReturnContent().getAllFee();
                            PayUtil.alipay(getActivity(), mHandler, 10001,
                                    aLiPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, aLiPayInfoBean.getError());
                            break;
                    }

                }

                break;
            case 2001:

                WXPayInfoBean wxPayInfoBean = (WXPayInfoBean) GsonTransformUtil
                        .fromJson(json, WXPayInfoBean.class);

                if (wxPayInfoBean != null) {

                    switch (wxPayInfoBean.getErrno()) {
                        case 0:
                            allFee = wxPayInfoBean.getReturnContent().getAllFee();
                            PayUtil.WXPay(getActivity(), wxPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, wxPayInfoBean.getError());
                            break;
                    }

                }
                break;
            case 1009:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
//                    showChooseDialog("充值成功\n充值已到账,到账金额" + editText.getText().toString() + "元");
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }


    private void initAdapter() {

        if (YYConstans.getUserInfoBean() != null) {
            crruentCount.setText("当前余额 " + YYConstans.getUserInfoBean().getReturnContent().getUser().getBalance() + " 元");
            ed_Text.setHint("还差 " + df.format(1500.0 - YYConstans.getUserInfoBean().getReturnContent().getUser().getBalance()));
            //yuan.setText("还差 " + (1500.0 - YYConstans.getUserInfoBean().getUser().getBalance()) + " 元");
        }

        ed_Text.addTextChangedListener(new TextWatcher() {

            private boolean isChanged = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChanged) {// ----->如果字符未改变则返回
                    return;
                }
                String str = s.toString();
                isChanged = true;
                StringBuffer stringBuffer = new StringBuffer();

                for (int k = 0; k < str.length(); k++) {

                    switch (k) {
                        case 0:
                            stringBuffer.append(str.charAt(k));
                            break;
                        case 1:
                            if (stringBuffer.charAt(0) == '0') {
                                stringBuffer.deleteCharAt(0);
                            }
                            stringBuffer.append(str.charAt(k));
                            break;
                        default:
                            if (stringBuffer.charAt(0) == '0') {
                                stringBuffer.deleteCharAt(0);
                            }
                            stringBuffer.append(str.charAt(k));
                            break;
                    }

                }

                ed_Text.setText(stringBuffer.toString());
                ed_Text.setSelection(ed_Text.length());
                stringBuffer.delete(0, stringBuffer.length());
                stringBuffer = null;
                isChanged = false;

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                switch (i) {
                    case 0:
                        Ly_item0.setSelected(true);
                        Ly_item1.setSelected(false);
                        break;
                    case 1:
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(ed_Text.getWindowToken(), 0);

                        Ly_item0.setSelected(false);
                        Ly_item1.setSelected(true);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * @param amount
     * @param payType wxpay—微信1 alipay—支付宝2
     */
    private void recharge(String amount, int payType) {

        if (!TextUtils.isEmpty(amount) && Double.valueOf(amount) >= 0.01) {

            switch (payType) {
                case 1:
                    dialogShow();
                    Map<String, String> weixipara = new HashMap<String, String>();
                    weixipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    weixipara.put("amount", amount + "");
                    weixipara.put("payType", "wxpay");
                    YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.BULIDPAYTORECHARGE,
                            weixipara, this);
                    break;
                case 2:
                    dialogShow();
                    Map<String, String> alipara = new HashMap<String, String>();
                    alipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    alipara.put("amount", amount + "");
                    alipara.put("payType", "alipay");
                    YYRunner.getData(2002, YYRunner.Method_POST, YYUrl.BULIDPAYTORECHARGE,
                            alipara, this);
                    break;
            }


        } else {
            MyUtils.showToast(mContext, "金额非法");
        }


    }

    /**
     * 注册广播,接受信鸽传来的预授权信息时候调用该action
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
            doUnionPaySucceed(intent.getStringExtra("content"));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 处理支付成功
     */
    private void doPaySucceed() {
        MyUtils.showToast(mContext, "支付成功");
        YYApplication.TAGorder = "notcomplete";
        isPaySucceed = true;
        updateUserInfo();
        showChooseDialog("充值成功\n充值已到账,到账金额" + allFee + "元", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });

//        header.sendEmptyMessageDelayed(20001, 1000);
    }

    /**
     * 处理银联支付成功
     */
    private void doUnionPaySucceed(String content) {
        MyUtils.showToast(mContext, "支付成功");
        YYApplication.TAGorder = "notcomplete";
        isPaySucceed = true;
        updateUserInfo();
        showChooseDialog(content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });

//        header.sendEmptyMessageDelayed(20001, 1000);
    }

    private void updateUserInfo() {

        Intent data = new Intent();
        data.putExtra("isPaySucceed", isPaySucceed);
        getActivity().setResult(Activity.RESULT_OK, data);


        dialogShow();
        Map<String, String> wxparams = new HashMap<String, String>();
        wxparams.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        wxparams.put("lng", YYApplication.Longitude + "");
        wxparams.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(1009, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, this);
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
        Intent data = new Intent();
        data.putExtra("isPaySucceed", isPaySucceed);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

}
