package com.dzrcx.jiaan.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.dzrcx.jiaan.Bean.WXPayBean;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import java.util.Map;

/**
 * 支付工具
 *
 * @author zhangyu
 */
public class PayUtil {

    private static IWXAPI api;

    /**
     * 微信支付
     *
     * @param mContext
     * @param wxPayBean
     */
    public static void WXPay(Context mContext, WXPayBean wxPayBean) {

        if (wxPayBean != null && wxPayBean.getAppid() != null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            // 注册到微信
            api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID);
            if (!api.isWXAppInstalled()) {
                MyUtils.showToast(mContext, "您未安装微信");
                return;
            }
            api.registerApp(Constants.APP_ID);
            PayReq req = new PayReq();
            req.appId = wxPayBean.getAppid();
            req.partnerId = wxPayBean.getPartnerid();
            req.prepayId = wxPayBean.getPrepayid();
            req.nonceStr = wxPayBean.getNoncestr();
            req.timeStamp = wxPayBean.getTimestamp();
            req.packageValue = wxPayBean.getPackageStr();
            req.sign = wxPayBean.getSign();
            // 在支付之前，如果应用没有注册到微信，应该先
            // 调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        }
    }

    /**
     * 微信支付
     *
     * @param mContext
     * @param map
     */
    public static void WXPay(Context mContext, Map<String, String> map) {
        // System.out.println("----" + map.toString());
        if (map != null && map.get("appid") != null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            // 注册到微信
            api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID);
            api.registerApp(Constants.APP_ID);
            PayReq req = new PayReq();
            req.appId = map.get("appid");
            req.partnerId = map.get("partnerid");
            req.prepayId = map.get("prepayid");
            req.nonceStr = map.get("noncestr");
            req.timeStamp = map.get("timestamp");
            req.packageValue = map.get("package");
            req.sign = map.get("sign");
            // 在支付之前，如果应用没有注册到微信，应该先
            // 调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        }
    }

    /**
     * @param baseaActivity
     * @param handler
     * @param what
     * @param payinfo
     */
    public static void alipay(final Activity baseaActivity,
                              final Handler handler, final int what, final String payinfo) {

        // 启动支付宝
        new Thread() {
            public void run() {
                // // String orderInfos =
                // //
                // "partner=\"2088511384456203\"&out_trade_no=\"082117392827831\"&subject=\"大满贯订单123456789\"&body=\"大满贯订单详情\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fnotify.java.jpxx.org%2Findex.jsp\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&return_url=\"http%3A%2F%2Fm.alipay.com\"&payment_type=\"1\"&seller_id=\"bjgrandslam@qq.com\"&it_b_pay=\"1m\"&sign=\"b1BXaTEbsf3X3c65FPn6LFHmkCSPnd3FBtqae7duoPxVY8CeHCIMvfaih9iX7FC49Ev4zC0RNhdUikgiWWvoHBQ2P1MVaWxWLkEf9GlN%2F2BoYqDfc8SbrbItX%2FE%2FG3wveR3U6I6mKVfM%2FdzgDOKQIbRCEplWmnPFn0FaHgEXK2A%3D\"&sign_type=\"RSA\"";
//                AliPay alipay = new AliPay(baseaActivity, handler);
//                String result = alipay.pay(payinfo);
//                Message msg = new Message();
//                msg.what = what;
//                msg.obj = result;
//                handler.sendMessage(msg);

                // 构造PayTask 对象
                PayTask alipay = new PayTask(baseaActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payinfo);

                Message msg = new Message();
                msg.what = what;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        }.start();

    }

    /**
     * 银联支付
     *
     * @param mContext
     * @param orderInfo
     */
    public static void unpay(Context mContext, String orderInfo) {
//        mode —— 银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
        UPPayAssistEx.startPay(mContext, null, null, orderInfo, YYUrl.UNPAYTAG);
    }


}
