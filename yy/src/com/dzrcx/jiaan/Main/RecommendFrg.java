package com.dzrcx.jiaan.Main;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ShareBean;
import com.dzrcx.jiaan.Bean.ShareBeanReturnContent;
import com.dzrcx.jiaan.Bean.ShareContentBean;
import com.dzrcx.jiaan.Bean.ShareContentBeanReturnContent;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.User.LoginAty;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.ShareDialog;
import com.dzrcx.jiaan.zxing.encode.CodeCreator;
import com.google.zxing.WriterException;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import java.util.HashMap;
import java.util.Map;

/**
 * 推荐有奖
 */
public class RecommendFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener {
    private View recommendview;
    private TextView tv_title, tv_invitecode, tv_getmoney, tv_detail;
    private ImageView iv_left_raw, iv_qr_code;
    private LinearLayout ll_sharetocircle, ll_invitefriend;
    private static final int TAG_GETSHAREURL = 11003;
    private Dialog messageDialog;
    private String msg = "";
    private ShareBean shareBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (recommendview == null) {
            recommendview = inflater.inflate(R.layout.frg_recommend3_3, null);
            initView();
            requestData(true);
            requestURLData(TAG_GETSHAREURL, true);
        }
        return recommendview;
    }

    public void initView() {

        iv_left_raw = (ImageView) recommendview.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title = (TextView) recommendview.findViewById(R.id.tv_title);
        tv_title.setText("推荐有奖");
        iv_qr_code = (ImageView) recommendview.findViewById(R.id.iv_qr_code);
        tv_invitecode = (TextView) recommendview.findViewById(R.id.tv_invitecode);
        tv_invitecode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tv_invitecode.getText().toString());
                MyUtils.showToast(mContext, "复制成功");
                return false;
            }
        });
        tv_invitecode.setText(YYConstans.getUserInfoBean().getReturnContent().getUser().getInviteCode());
        tv_detail = (TextView) recommendview.findViewById(R.id.tv_detail);
        tv_getmoney = (TextView) recommendview.findViewById(R.id.tv_getmoney);
        ll_sharetocircle = (LinearLayout) recommendview.findViewById(R.id.ll_sharetocircle);
        ll_invitefriend = (LinearLayout) recommendview.findViewById(R.id.ll_invitefriend);
        MyUtils.setViewsOnClick(this, iv_left_raw, tv_detail, tv_getmoney, ll_sharetocircle, ll_invitefriend);

    }

    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "user_invite");
        YYRunner.getData(YYConstans.TAG_GETACTIVITYCONTENT, YYRunner.Method_POST,
                YYUrl.GETACTIVITYCONTENT, params, this);
    }

    /**
     * 请求分享信息
     *
     * @param isShowDialog
     */
    public void requestURLData(int Tag, boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("inviteCode", YYConstans.getUserInfoBean().getReturnContent().getUser().getInviteCode());
        YYRunner.getData(Tag, YYRunner.Method_POST,
                YYUrl.GETSHAREURL, params, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.ll_sharetocircle:
                if (TextUtils.isEmpty(YYConstans.getUserInfoBean().getReturnContent().getSkey())) {
                    startActivity(LoginAty.class, null);
                    return;
                }
                if (ShareDialog.isWeixinAvilible(mContext)) {
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setComponent(comp);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra("Kdescription", "小伙伴们，使用星辰出行，用车费比打出租车、快车更便宜，现在加入更有30元代金券拿哦~");
                    Bitmap bitmap = ShareDialog.getShareBitmap(mContext, YYConstans.getUserInfoBean().getReturnContent().getUser().getName(), shareBean.getUrl());
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, null, null)));
                    startActivity(intent);
//                    shareDialog.shareContent(shareBean.getUrl().replace("\\", ""), shareBean.getTitle(), shareBean.getDesc()
//                            , bitmap, "aa", SendMessageToWX.Req.WXSceneTimeline);//分享朋友圈
                } else {
                    MyUtils.showToast(mContext, "您还未安装微信手机客户端");
                }
                break;
            case R.id.tv_detail:
                if (!msg.isEmpty()) {
                    showMessageDialog(msg);
                }
                break;
            case R.id.ll_invitefriend:
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.logo_share);
                ShareDialog shareDialog2 = new ShareDialog(mContext, shareBean.getUrl().replace("\\", ""), shareBean.getTitle(), shareBean.getDesc()
                        , bitmap2, "aa");
                if (shareDialog2.isWeixinAvilible(mContext)) {
                    shareDialog2.shareContent(shareBean.getUrl().replace("\\", ""), shareBean.getTitle(), shareBean.getDesc()
                            , bitmap2, "aa", SendMessageToWX.Req.WXSceneSession);//分享朋友圈
                } else {
                    MyUtils.showToast(mContext, "您还未安装微信手机客户端");
                }
                break;
        }
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        switch (tag) {
            case YYConstans.TAG_GETACTIVITYCONTENT:
                ShareContentBeanReturnContent shareContentBeanReturnContent = (ShareContentBeanReturnContent) GsonTransformUtil.fromJson(json, ShareContentBeanReturnContent.class);
                if (shareContentBeanReturnContent == null || shareContentBeanReturnContent.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareContentBeanReturnContent == null ? "数据传输错误，请重试" : shareContentBeanReturnContent.getError() + "");
                } else {
                    ShareContentBean shareContentBean = shareContentBeanReturnContent.getReturnContent();
                    fillData(shareContentBean);
                }
                break;
            case TAG_GETSHAREURL:
                dismmisDialog();
                ShareBeanReturnContent shareBeanReturnContent = (ShareBeanReturnContent) GsonTransformUtil.fromJson(json, ShareBeanReturnContent.class);
                if (shareBeanReturnContent == null || shareBeanReturnContent.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareBeanReturnContent == null ? "数据传输错误，请重试" : shareBeanReturnContent.getError() + "");
                } else {
                    shareBean = shareBeanReturnContent.getReturnContent();
                    try {
                        iv_qr_code.setImageBitmap(CodeCreator.createQRCode(shareBean.getUrl()));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private void fillData(ShareContentBean shareContentBean) {
        if (shareContentBean == null) return;
        msg = "邀请好友注册，Ta得" + (shareContentBean.getCouponBeInvited_1() + shareContentBean.getCouponBeInvited_2())
                + "元现金券，您得" + shareContentBean.getCouponBeInvited_1() + "元现金优惠券。朋友完成首单，您再得" + shareContentBean.getCouponBeInvited_2() + "元现金优惠券。";
        String numberString = "<font color='#3D3F42'>邀请好友，双方各得</font> " + "<font color='#04b575'>" + (shareContentBean.getCouponBeInvited_1() +
                shareContentBean.getCouponBeInvited_2()) + "" + "</font>" + "<font color='#303437'>元现金优惠券</font>";
        tv_getmoney.setText(Html.fromHtml(numberString));
    }

    @Override
    public void onLoading(long count, long current) {
    }

    /**
     * 知道了窗口
     */
    public void showMessageDialog(String messge) {
        if (messageDialog == null) {
            messageDialog = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_recommend_more, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            TextView tv_title = (TextView) mDlgCallView
                    .findViewById(R.id.tv_title);
            tv_title.setText("邀请好友奖励说明");
            TextView tv_content = (TextView) mDlgCallView
                    .findViewById(R.id.tv_content);
            tv_content.setText(messge);
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
