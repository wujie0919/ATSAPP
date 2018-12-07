package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.User.LoginAty;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * 分享至微信dialog
 */
public class ShareDialog3 extends Dialog implements View.OnClickListener {
    private Context mContext;
    private UIImageView uiv_weixin, uiv_weixin_circle;
    private TextView messageView, tv_cancel;
    private IWXAPI iwxapi;
    private String messageStr;
    private String url;
    private String title;
    private String description;
    private Bitmap bitmap;
    private String transaction;
    private String messageStr2;

    private View.OnClickListener cancelClick;

    public ShareDialog3(Context context, String messageStr, String url, String title, String description, Bitmap bitmap, String transaction) {
        super(context, R.style.ActionSheet);
        this.mContext = context;
        iwxapi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        iwxapi.registerApp(Constants.APP_ID);
        this.messageStr = messageStr;
        this.url = url;
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
        this.transaction = transaction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_payend_share);
        setCanceledOnTouchOutside(true);
        messageView = (TextView) findViewById(R.id.share_message);
        uiv_weixin = (UIImageView) findViewById(R.id.uiv_weixin);
        uiv_weixin_circle = (UIImageView) findViewById(R.id.uiv_weixin_circle);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        if (!TextUtils.isEmpty(messageStr)) {
            messageView.setText(Html.fromHtml(messageStr));
        } else {
            messageView.setText(null);
        }
        MyUtils.setViewsOnClick(this, tv_cancel, uiv_weixin_circle, uiv_weixin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                if (cancelClick != null) {
                    cancelClick.onClick(v);
                }
                break;
            case R.id.uiv_weixin_circle:
                if (isWeixinAvilible(mContext)) {
//                    shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
                    if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                        Intent toIntent = new Intent(mContext, LoginAty.class);
                        mContext.startActivity(toIntent);
                        return;
                    }
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setComponent(comp);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra("Kdescription", messageStr2);
                    Bitmap bitmap = ShareDialog.getShareBitmap(mContext, YYConstans.getUserInfoBean().getReturnContent().getUser().getName(), url);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, null, null)));
                    mContext.startActivity(intent);
                } else {
                    MyUtils.showToast(mContext, "您还未安装微信手机客户端");
                }
                dismiss();
                break;
            case R.id.uiv_weixin:
                if (isWeixinAvilible(mContext)) {
                    shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneSession);//分享朋友
                } else {
                    MyUtils.showToast(mContext, "您还未安装微信手机客户端");
                }
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 8;
        w.setAttributes(lp);
    }

    public void shareContent(String url, String title, String description, Bitmap bitmap, String transaction, int scene) {
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

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
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

    public String getMessageStr2() {
        return messageStr2;
    }

    public void setMessageStr2(String messageStr2) {
        this.messageStr2 = messageStr2;
    }

    public View.OnClickListener getCancelClick() {
        return cancelClick;
    }

    public void setCancelClick(View.OnClickListener cancelClick) {
        this.cancelClick = cancelClick;
    }
}
