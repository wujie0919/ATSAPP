package com.dzrcx.jiaan.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ShareUtils {
    private static IWXAPI iwxapi = null;
    private static ShareUtils shareUtils = null;

    private ShareUtils(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        iwxapi.registerApp(Constants.APP_ID);
    }

    public static ShareUtils getIntance(Context context) {
        if (shareUtils == null) {
            shareUtils = new ShareUtils(context);
        }
        return shareUtils;
    }

    public void shareToWeiXin(Context context, String url, String title, String description, Bitmap bitmap, String transaction) {
        if (isWeixinAvilible(context)) {
            shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneSession);//分享朋友
        } else {
            MyUtils.showToast(context, "您还未安装微信手机客户端");
        }

    }

    public void shareToWeiXinCircle(Context context, String url, String title, String description, Bitmap bitmap, String transaction) {
        if (isWeixinAvilible(context)) {
            shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
        } else {
            MyUtils.showToast(context, "您还未安装微信手机客户端");
        }

    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void shareContent(String url, String title, String description, Bitmap bitmap, String transaction, int scene) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage message = new WXMediaMessage(webpageObject);
        message.title = title;
        message.description = description;
        message.setThumbImage(bitmap);//thumbData = Util.bmpToByteArray(bitmap, true);//左侧小图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = message;
        req.scene = scene;
        iwxapi.sendReq(req);
    }
}
