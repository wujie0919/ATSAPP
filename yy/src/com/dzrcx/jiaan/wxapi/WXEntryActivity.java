package com.dzrcx.jiaan.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.clicklistener.WXShareCallBackListener;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by zhangyu on 16-4-12.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    private static WXShareCallBackListener shareCallBackListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        System.out.print(baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {

        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享已取消";
                break;
            default:
                result = "分享失败";
                break;
        }


        if (shareCallBackListener != null) {
            shareCallBackListener.OnBackListener(baseResp.errCode, result);
        }
        MyUtils.showToast(this, result);
        finish();
    }

    public static WXShareCallBackListener getShareCallBackListener() {
        return shareCallBackListener;
    }

    public static void setShareCallBackListener(WXShareCallBackListener shareCallBackListener) {
        WXEntryActivity.shareCallBackListener = shareCallBackListener;
    }
}
