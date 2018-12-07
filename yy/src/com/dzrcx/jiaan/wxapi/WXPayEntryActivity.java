package com.dzrcx.jiaan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private static WXPayCallBackListener wxPayCallBackListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

//        Log.d(TAG, "onReq, onReq = " + req.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
//        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "支付成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "您已取消支付";
                MyUtils.showToast(this, result);
                break;
            default:
                result = "支付失败,请重试";
                MyUtils.showToast(this, result);
                break;
        }
//        MyUtils.showToast(this, result);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (wxPayCallBackListener != null) {
                wxPayCallBackListener.OnBackListener(resp.errCode, resp.errStr);
            }
        }
        finish();
    }

    public static WXPayCallBackListener getWxPayCallBackListener() {
        return wxPayCallBackListener;
    }

    public static void setWxPayCallBackListener(
            WXPayCallBackListener wxPayCallBackListener) {
        WXPayEntryActivity.wxPayCallBackListener = wxPayCallBackListener;
    }

}