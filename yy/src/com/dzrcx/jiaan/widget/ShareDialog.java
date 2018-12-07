package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.wxapi.simcpux.Constants;
import com.dzrcx.jiaan.zxing.encode.CodeCreator;
import com.google.zxing.WriterException;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * 分享至微信dialog
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private UIImageView uiv_weixin, uiv_weixin_circle;
    private TextView tv_cancel;
    private IWXAPI iwxapi;
    private String url;
    private String title;
    private String description;
    private Bitmap bitmap;
    private String transaction;

    public ShareDialog(Context context, String url, String title, String description, Bitmap bitmap, String transaction) {
        super(context, R.style.ActionSheet);
        this.mContext = context;
        iwxapi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, true);
        iwxapi.registerApp(Constants.APP_ID);
        this.url = url;
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
        this.transaction = transaction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_share);
        setCanceledOnTouchOutside(true);
        uiv_weixin = (UIImageView) findViewById(R.id.uiv_weixin);
        uiv_weixin_circle = (UIImageView) findViewById(R.id.uiv_weixin_circle);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        MyUtils.setViewsOnClick(this, tv_cancel, uiv_weixin_circle, uiv_weixin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.uiv_weixin_circle:
                if (isWeixinAvilible(mContext)) {
                    shareContent(url, title, description, bitmap, transaction, SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
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
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
        super.show();
    }

    public void shareContent(String url, String title, String description, Bitmap bitmap, String transaction, int scene) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;
        WXMediaMessage message = new WXMediaMessage(webpageObject);
        message.title = title;
        message.description = description;
        message.setThumbImage(bitmap);//thumbData = Util.bmpToByteArray(bitmap, true);//左侧小图
        // message.mediaObject=textObject;
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


    public static Bitmap getShareBitmap(Context context, String name, String url) {
        LayoutInflater localinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View shareView = localinflater.inflate(R.layout.share_view, null);
        TextView textView = (TextView) shareView.findViewById(R.id.tv_name);
        ImageView iv_code = (ImageView) shareView.findViewById(R.id.iv_code);
        textView.setText(name + "送你30元代金券请领取");
        try {
            iv_code.setImageBitmap(CodeCreator.createQRCode(url));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        LinearLayout layout = new LinearLayout(context);
        layout.addView(shareView);
        shareView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MyUtils.dip2px(context, 320)));
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }


}
