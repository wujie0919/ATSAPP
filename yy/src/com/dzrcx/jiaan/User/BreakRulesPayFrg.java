package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.PayResult;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 违章支付
 */
public class BreakRulesPayFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface, WXPayCallBackListener {
    private DecimalFormat df = new DecimalFormat("#0.0");
    private TextView tv_title;
    private ImageView iv_left_raw;
    private TextView tv_truemoney;
    private RelativeLayout rl_zhifubao, rl_weixin;
    private View PayView;
    private boolean isPaySucceed = false;
    private String violationId;
    private long laterTime;
    private Intent intent;
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
            PayView = inflater.inflate(R.layout.breakrulespay_frg, null);
            iv_left_raw = (ImageView) PayView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            tv_title = (TextView) PayView.findViewById(R.id.tv_title);
            tv_title.setText("支付");
            tv_truemoney = (TextView) PayView.findViewById(R.id.tv_truemoney);
            rl_zhifubao = (RelativeLayout) PayView.findViewById(R.id.rl_zhifubao);
            rl_weixin = (RelativeLayout) PayView.findViewById(R.id.rl_weixin);
            MyUtils.setViewsOnClick(this, iv_left_raw, rl_zhifubao, rl_weixin);

            String money = intent.getExtras().getString("number");
            tv_truemoney.setText(df.format(Double.parseDouble(money)) + " 元");
            violationId = intent.getExtras().getString("violationId");
        }
        return PayView;
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
                finish();
                break;
            case R.id.rl_zhifubao:
                if (!TextUtils.isEmpty(violationId)) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - laterTime < 1000) {
                        laterTime = currentTime;
                        return;
                    }
                    laterTime = currentTime;
                    dialogShow();
                    Map<String, String> alipara = new HashMap<String, String>();
                    alipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    alipara.put("violationId", violationId);
                    alipara.put("payType", "alipay");
                    YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.BUILDPAYFORVIOLATION,
                            alipara, this);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
                }
                break;
            case R.id.rl_weixin:
                if (!TextUtils.isEmpty(violationId)) {

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - laterTime < 1000) {
                        laterTime = currentTime;
                        return;
                    }
                    laterTime = currentTime;
                    dialogShow();
                    Map<String, String> wxparams = new HashMap<String, String>();
                    wxparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    wxparams.put("violationId", violationId);
                    wxparams.put("payType", "wxpay");
                    YYRunner.getData(1002, YYRunner.Method_POST, YYUrl.BUILDPAYFORVIOLATION,
                            wxparams, this);
                } else {
                    MyUtils.showToast(mContext, "数据错误");
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

                WXPayInfoBean wxPayInfoBean
                        = (WXPayInfoBean) GsonTransformUtil
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
        Intent data = new Intent();
        data.putExtra("isPaySucceed", isPaySucceed);
        data.putExtra("violationId", violationId);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    /**
     * 处理支付成功
     */
    private void doPaySucceed() {
        MyUtils.showToast(mContext, "支付成功");
        YYApplication.TAGorder = "notcomplete";
        isPaySucceed = true;
        header.sendEmptyMessageDelayed(20001, 1000);
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

}
