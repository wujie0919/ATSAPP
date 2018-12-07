package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ALiPayInfoBean;
import com.dzrcx.jiaan.Bean.AllRechargeBackReturnContent;
import com.dzrcx.jiaan.Bean.BackFeeBean;
import com.dzrcx.jiaan.Bean.CheckUserForegiftBean;
import com.dzrcx.jiaan.Bean.CheckUserForegiftVo;
import com.dzrcx.jiaan.Bean.PayResult;
import com.dzrcx.jiaan.Bean.RechargeBackBean;
import com.dzrcx.jiaan.Bean.UserDepositBean;
import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Bean.WXPayInfoBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXPayCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.PayUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYTwoButtonDialog;
import com.dzrcx.jiaan.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的账户界面
 */
public class MyAccountFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener, WXPayCallBackListener {

    private View rechargeView;
    private ImageView iv_left_raw;
    private TextView titleView, rightView;
    private TextView tv_personnalbalance, tv_recharge_xieyi, tv_recharge;
    private TextView tv_deposit, tv_withdrawdeposit;
    private TextView recharge_benefit;//充金额送金额
    private RelativeLayout rl_paytype, rl_personalbalance;
    private EditText et_recharge;
    private LinearLayout ll_moneyone1, ll_moneyone2, ll_moneyone3,
            ll_moneytwo1, ll_moneytwo2, ll_moneytwo3;
    private TextView
            tv_rechargemoneyone1, tv_rechargemoneyone2, tv_rechargemoneyone3,
            tv_rechargemoneytwo1, tv_rechargemoneytwo2, tv_rechargemoneytwo3,
            tv_sendmoneyone1, tv_sendmoneyone2, tv_sendmoneyone3,
            tv_sendmoneytwo1, tv_sendmoneytwo2, tv_sendmoneytwo3;
    private boolean isPaySucceed = false;
    private List<RechargeBackBean> rechargeBackBeans;
    /**
     * 充值成功后的到账金额
     */
    private double allFee;
    private Dialog paytypeDlg;
    private static final int TAG_getuserinfo = 1009;
    private static final int TAG_accountmessage = 169505;
    private ImageView iv_paytype_icon;
    private TextView tv_paytype;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rechargeView == null) {
            rechargeView = LayoutInflater.from(mContext).inflate(R.layout.frg_mycount, null);
            initView();
            header.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserInfo();
                    getAccountMessge();
                    requestData(true);
                }
            }, 100);
        }
        return rechargeView;
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
        YYRunner.getData(TAG_getuserinfo, YYRunner.Method_POST, YYUrl.GETUSERINFO,
                wxparams, this);
    }

    /**
     * 请求充多少送多少--文案
     */
    private void getAccountMessge() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "chargeDoc");
        YYRunner.getData(TAG_accountmessage, YYRunner.Method_POST, YYUrl.GETDOCUMENT, map,
                this);
    }

    /**
     * 返现卡数据请求
     *
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        YYRunner.getData(1003, YYRunner.Method_POST,
                YYUrl.SHOWALLRECHARGEBACK, params, this);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("skey",
                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(1004, YYRunner.Method_POST,
                YYUrl.GETUSERDEPOSIT, params2, this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_moneyone1:
            case R.id.ll_moneyone2:
            case R.id.ll_moneyone3:
            case R.id.ll_moneytwo1:
            case R.id.ll_moneytwo2:
            case R.id.ll_moneytwo3:
                if (rechargeBackBeans == null) {
                    return;
                }
                initCardChoose();
                v.setSelected(true);
                clickSendMoneyStau((int) v.getTag());
                et_recharge.setText(rechargeBackBeans.get((int) v.getTag()).getAmount() + "");
                et_recharge.setCursorVisible(false);
                break;
            case R.id.et_recharge:
                et_recharge.setCursorVisible(true);
                break;
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.rl_personalbalance:
                startActivity(RechargeRecordAty.class, null);
                break;
            case R.id.tv_right:
                startActivity(CompanyAccountAty.class, null);
                break;
            case R.id.tv_withdrawdeposit:
                checkUserForegift();
                break;
            case R.id.rl_paytype:
                showBottomDialog();
                break;
            case R.id.tv_recharge:
                if (SharedPreferenceTool.getPrefInt(mContext, "MyAccountFrg", 2) == 2) {
                    recharge(et_recharge.getText().toString(), 2);
                } else if (SharedPreferenceTool.getPrefInt(mContext, "MyAccountFrg", 2) == 1) {
                    recharge(et_recharge.getText().toString(), 1);
                }
                break;
            case R.id.tv_recharge_xieyi:
                startActivity(new Intent(this.getActivity(), WebAty.class)
                        .putExtra("title", "充值协议").putExtra("url",
                                YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=rechargeRoles"));
                mContext.overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;

        }
    }

    /**
     * 初始化选项卡
     */
    private void initCardChoose() {
        ll_moneyone1.setSelected(false);
        ll_moneyone2.setSelected(false);
        ll_moneyone3.setSelected(false);
        ll_moneytwo1.setSelected(false);
        ll_moneytwo2.setSelected(false);
        ll_moneytwo3.setSelected(false);
        fillSendMoney(tv_sendmoneyone1, "#999999", "#13bb86", rechargeBackBeans, 0);
        fillSendMoney(tv_sendmoneyone2, "#999999", "#13bb86", rechargeBackBeans, 1);
        fillSendMoney(tv_sendmoneyone3, "#999999", "#13bb86", rechargeBackBeans, 2);
        fillSendMoney(tv_sendmoneytwo1, "#999999", "#13bb86", rechargeBackBeans, 3);
        fillSendMoney(tv_sendmoneytwo2, "#999999", "#13bb86", rechargeBackBeans, 4);
        fillSendMoney(tv_sendmoneytwo3, "#999999", "#13bb86", rechargeBackBeans, 5);
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    public void fillRechargeBackData() {
        if (rechargeBackBeans == null) {
            return;
        }
        tv_rechargemoneyone1.setText(rechargeBackBeans.get(0).getAmount() + "元");
        tv_rechargemoneyone2.setText(rechargeBackBeans.get(1).getAmount() + "元");
        tv_rechargemoneyone3.setText(rechargeBackBeans.get(2).getAmount() + "元");
        tv_rechargemoneytwo1.setText(rechargeBackBeans.get(3).getAmount() + "元");
        tv_rechargemoneytwo2.setText(rechargeBackBeans.get(4).getAmount() + "元");
        tv_rechargemoneytwo3.setText(rechargeBackBeans.get(5).getAmount() + "元");
        initCardChoose();
        ll_moneyone1.setSelected(true);
        clickSendMoneyStau((int) ll_moneyone1.getTag());
        et_recharge.setText(rechargeBackBeans.get((int) ll_moneyone1.getTag()).getAmount() + "");
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1004:
                UserDepositBean depositBean = (UserDepositBean) GsonTransformUtil.fromJson(json, UserDepositBean.class);
                if (depositBean != null && depositBean.getErrno() == 0) {
                    tv_deposit.setText("租车押金：" + depositBean.getReturnContent().getAmount() + "元");
                    if (depositBean.getReturnContent().getAmount() > 0) {
                        tv_withdrawdeposit.setVisibility(View.VISIBLE);
                        tv_withdrawdeposit.setTag(depositBean);
                    } else {
                        tv_withdrawdeposit.setVisibility(View.GONE);
                    }

                } else {
                    tv_deposit.setText("租车押金：--元");
                    tv_withdrawdeposit.setVisibility(View.GONE);
                }
                break;
            case 1003:
                AllRechargeBackReturnContent allRechargeBackReturnContent = (AllRechargeBackReturnContent) GsonTransformUtil
                        .fromJson(json, AllRechargeBackReturnContent.class);

                if (allRechargeBackReturnContent != null && allRechargeBackReturnContent.getErrno() == 0) {
                    rechargeBackBeans = allRechargeBackReturnContent.getReturnContent();
                    if (rechargeBackBeans.size() < 6) {
                        MyUtils.showToast(mContext, "数据传输错误，请重试");
                        return;
                    }
                    fillRechargeBackData();
                } else if (allRechargeBackReturnContent != null && allRechargeBackReturnContent.getErrno() != 0) {
                    MyUtils.showToast(getActivity(), allRechargeBackReturnContent.getError());
                }
                break;
            case 1002:
                ALiPayInfoBean aLiPayInfoBean = (ALiPayInfoBean) GsonTransformUtil
                        .fromJson(json, ALiPayInfoBean.class);

                if (aLiPayInfoBean != null && aLiPayInfoBean.getReturnContent() != null) {

                    switch (aLiPayInfoBean.getErrno()) {
                        case 0:
                            allFee = aLiPayInfoBean.getReturnContent().getAllFee();
                            PayUtil.alipay(getActivity(), header, 10001,
                                    aLiPayInfoBean.getReturnContent().getReqCode());
                            break;

                        default:
                            MyUtils.showToast(mContext, aLiPayInfoBean.getError());
                            break;
                    }

                }

                break;
            case 1001:

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
            case 1008:
                BackFeeBean backFeeBean = (BackFeeBean) GsonTransformUtil.fromJson(json, BackFeeBean.class);
                if (backFeeBean != null && backFeeBean.getErrno() == 0 && backFeeBean.getReturnContent() != null) {
                    if (String.valueOf((int) (backFeeBean.getReturnContent().getOriAmount())).equals(et_recharge.getText().toString())) {
                        tv_recharge.setText("立即充值(到账¥" + MyUtils.formatPriceShort(backFeeBean.getReturnContent().getBenefitFee() + backFeeBean.getReturnContent().getOriAmount()).trim() + ")");
                    } else {
                        tv_recharge.setText("立即充值");
                    }
                } else {
                    tv_recharge.setText("立即充值");
                }
                break;
            case TAG_getuserinfo:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);
                    updataPersonalBalance(userInfoBean);
                }
                break;
            case TAG_accountmessage:
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean != null && baseResBean.getErrno() == 0) {
                    recharge_benefit.setText(baseResBean.getError());
                }
                break;
            case 3001:
                CheckUserForegiftBean foregiftBean = (CheckUserForegiftBean) GsonTransformUtil.fromJson(json, CheckUserForegiftBean.class);
                if (foregiftBean != null && foregiftBean.getErrno() == 0) {
                    tv_withdrawdeposit.setTag(foregiftBean.getReturnContent());
                    switch (foregiftBean.getReturnContent().getFlag()) {
                        case 4:
                            showDrawalDialog();
                            break;
                        default:
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("CheckUserForegiftVo", foregiftBean.getReturnContent());
                            startActivity(DepositAct.class, bundle);
                            break;
                    }

                }
                break;
            case 3002:
                YYBaseResBean resBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (resBean != null) {
                    MyUtils.showToast(mContext, resBean.getError());
                    if (resBean.getErrno() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CheckUserForegiftVo", ((CheckUserForegiftVo) tv_withdrawdeposit.getTag()));
                        startActivity(DepositAct.class, bundle);


                        Map<String, String> params2 = new HashMap<String, String>();
                        params2.put("skey",
                                YYConstans.getUserInfoBean().getReturnContent().getSkey() == null ? ""
                                        : YYConstans.getUserInfoBean().getReturnContent().getSkey());
                        YYRunner.getData(1004, YYRunner.Method_POST,
                                YYUrl.GETUSERDEPOSIT, params2, this);
                    }
                }
                break;
        }

    }

    /**
     * 填充个人余额
     *
     * @param userInfoBean
     */
    private void updataPersonalBalance(UserInfoBean userInfoBean) {
        if (userInfoBean == null)
            return;
        if (userInfoBean.getReturnContent().getSkey().isEmpty()) {
            tv_personnalbalance.setText(null);
            rightView.setVisibility(View.GONE);
        } else {
            tv_personnalbalance.setText(userInfoBean.getReturnContent().getUser().getBalance() + " 元");

            if (userInfoBean.getReturnContent().getUser().getEntUser() == 1) {
                rightView.setVisibility(View.VISIBLE);
                rightView.setText("企业账户");
            } else {
                rightView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onLoading(long count, long current) {

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
                    YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.BULIDPAYTORECHARGE,
                            weixipara, this);
                    break;
                case 2:
                    dialogShow();
                    Map<String, String> alipara = new HashMap<String, String>();
                    alipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
                    alipara.put("amount", amount + "");
                    alipara.put("payType", "alipay");
                    YYRunner.getData(1002, YYRunner.Method_POST, YYUrl.BULIDPAYTORECHARGE,
                            alipara, this);
                    break;
            }


        } else {
            MyUtils.showToast(mContext, "金额非法");
        }


    }

    private void getBackFee(String amount) {
        Map<String, String> backFeeparams = new HashMap<String, String>();
        backFeeparams.put("amount", amount);
        YYRunner.getData(1008, YYRunner.Method_POST, YYUrl.SHOWRECHARGEBACKAMOUNT,
                backFeeparams, this);
    }

    private void checkUserForegift() {
        dialogShow();
        Map<String, String> backFeeparams = new HashMap<String, String>();
        backFeeparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(3001, YYRunner.Method_POST, YYUrl.CHECKUSERFOREGIFT,
                backFeeparams, this);
    }


    public void finish() {
        // TODO Auto-generated method stub
        Intent data = new Intent();
        data.putExtra("isPaySucceed", isPaySucceed);
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
        getUserInfo();
        showChooseDialog("充值成功\n充值已到账,到账金额" + allFee + "元");
//        header.sendEmptyMessageDelayed(20001, 1000);
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

    public void showBottomDialog() {
        if (paytypeDlg == null) {
            paytypeDlg = new Dialog(getActivity(), R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mDlgCallView = inflater.inflate(R.layout.dlg_mycountpaytype, null);
            final int cFullFillWidth = 10000;
            mDlgCallView.setMinimumWidth(cFullFillWidth);

            LinearLayout ll_recharge_ali = (LinearLayout) mDlgCallView
                    .findViewById(R.id.ll_recharge_ali);
            LinearLayout ll_recharge_weixin = (LinearLayout) mDlgCallView
                    .findViewById(R.id.ll_recharge_weixin);
            TextView tv_cancel = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel);

            ll_recharge_ali.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SharedPreferenceTool.setPrefInt(mContext, "MyAccountFrg", 2);
                    iv_paytype_icon.setImageResource(R.drawable.zhifubao);
                    tv_paytype.setText("使用支付宝支付");
                    paytypeDlg.dismiss();
                    recharge(et_recharge.getText().toString(), 2);
                }
            });
            ll_recharge_weixin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SharedPreferenceTool.setPrefInt(mContext, "MyAccountFrg", 1);
                    iv_paytype_icon.setImageResource(R.drawable.weixin);
                    tv_paytype.setText("使用微信支付");
                    paytypeDlg.dismiss();
                    recharge(et_recharge.getText().toString(), 1);
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    paytypeDlg.dismiss();
                }
            });
            paytypeDlg.setContentView(mDlgCallView);
            paytypeDlg.setCanceledOnTouchOutside(true);
            paytypeDlg.setCancelable(true);
        }
        paytypeDlg.show();
        Window w = paytypeDlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        paytypeDlg.onWindowAttributesChanged(lp);

    }

    public void initView() {
        initBackMoneyView();
        MyUtils.setViewsOnClick(this, ll_moneytwo1, ll_moneytwo2, ll_moneytwo3, ll_moneyone1, ll_moneyone2, ll_moneyone3);
        iv_left_raw = (ImageView) rechargeView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        titleView = (TextView) rechargeView.findViewById(R.id.tv_title);
        titleView.setText("我的账户");
        iv_left_raw.setOnClickListener(this);
        rightView = (TextView) rechargeView.findViewById(R.id.tv_right);

        tv_deposit = (TextView) rechargeView.findViewById(R.id.tv_deposit);
        tv_withdrawdeposit = (TextView) rechargeView.findViewById(R.id.tv_withdrawdeposit);
        tv_withdrawdeposit.setOnClickListener(this);

        tv_recharge = (TextView) rechargeView.findViewById(R.id.tv_recharge);
        tv_recharge.setText("立即充值");
        iv_paytype_icon = (ImageView) rechargeView.findViewById(R.id.iv_paytype_icon);
        tv_paytype = (TextView) rechargeView.findViewById(R.id.tv_paytype);
        if (SharedPreferenceTool.getPrefInt(mContext, "MyAccountFrg", 2) == 2) {
            iv_paytype_icon.setImageResource(R.drawable.zhifubao);
            tv_paytype.setText("使用支付宝支付");
        } else {
            iv_paytype_icon.setImageResource(R.drawable.weixin);
            tv_paytype.setText("使用微信支付");
        }
        tv_personnalbalance = (TextView) rechargeView.findViewById(R.id.tv_personnalbalance);
        updataPersonalBalance(YYConstans.getUserInfoBean());
        recharge_benefit = (TextView) rechargeView.findViewById(R.id.recharge_benefit);
        et_recharge = (EditText) rechargeView.findViewById(R.id.et_recharge);
        rl_paytype = (RelativeLayout) rechargeView.findViewById(R.id.rl_paytype);
        rl_personalbalance = (RelativeLayout) rechargeView.findViewById(R.id.rl_personalbalance);
        tv_recharge_xieyi = (TextView) rechargeView.findViewById(R.id.tv_recharge_xieyi);
        MyUtils.setViewsOnClick(this, iv_left_raw, rightView, rl_paytype, rl_personalbalance, et_recharge, tv_recharge_xieyi, tv_recharge);
        recharge_benefit.setText(null);
        et_recharge.setCursorVisible(false);
        et_recharge.addTextChangedListener(new TextWatcher() {

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

                et_recharge.setText(stringBuffer.toString());
                et_recharge.setSelection(et_recharge.length());
                getBackFee(stringBuffer.toString());
                ReturnChooseCard(stringBuffer);
                stringBuffer.delete(0, stringBuffer.length());
                tv_recharge.setText("立即充值");
                stringBuffer = null;
                isChanged = false;

            }

            /**
             * 输入金额的时候选项卡选择逻辑
             * @param stringBuffer
             */
            private void ReturnChooseCard(StringBuffer stringBuffer) {
                initCardChoose();
                if (stringBuffer == null || stringBuffer.toString().isEmpty() || rechargeBackBeans == null) {
                    return;
                }
                if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(0).getAmount() + ""))) {
                    ll_moneyone1.setSelected(true);
                    clickSendMoneyStau((int) ll_moneyone1.getTag());
                } else if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(1).getAmount() + ""))) {
                    ll_moneyone2.setSelected(true);
                    clickSendMoneyStau((int) ll_moneyone2.getTag());
                } else if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(2).getAmount() + ""))) {
                    ll_moneyone3.setSelected(true);
                    clickSendMoneyStau((int) ll_moneyone3.getTag());
                } else if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(3).getAmount() + ""))) {
                    ll_moneytwo1.setSelected(true);
                    clickSendMoneyStau((int) ll_moneytwo1.getTag());
                } else if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(4).getAmount() + ""))) {
                    ll_moneytwo2.setSelected(true);
                    clickSendMoneyStau((int) ll_moneytwo2.getTag());
                } else if ((Double.parseDouble(stringBuffer.toString()) == Double.parseDouble(rechargeBackBeans.get(5).getAmount() + ""))) {
                    ll_moneytwo3.setSelected(true);
                    clickSendMoneyStau((int) ll_moneytwo3.getTag());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initBackMoneyView() {
        ll_moneyone1 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneyone1);
        ll_moneyone2 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneyone2);
        ll_moneyone3 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneyone3);
        ll_moneytwo1 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneytwo1);
        ll_moneytwo2 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneytwo2);
        ll_moneytwo3 = (LinearLayout) rechargeView.findViewById(R.id.ll_moneytwo3);
        tv_rechargemoneyone1 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneyone1);
        tv_rechargemoneyone2 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneyone2);
        tv_rechargemoneyone3 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneyone3);
        tv_rechargemoneytwo1 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneytwo1);
        tv_rechargemoneytwo2 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneytwo2);
        tv_rechargemoneytwo3 = (TextView) rechargeView.findViewById(R.id.tv_rechargemoneytwo3);
        ll_moneyone1.setTag(0);
        ll_moneyone2.setTag(1);
        ll_moneyone3.setTag(2);
        ll_moneytwo1.setTag(3);
        ll_moneytwo2.setTag(4);
        ll_moneytwo3.setTag(5);
        tv_sendmoneyone1 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneyone1);
        tv_sendmoneyone2 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneyone2);
        tv_sendmoneyone3 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneyone3);
        tv_sendmoneytwo1 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneytwo1);
        tv_sendmoneytwo2 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneytwo2);
        tv_sendmoneytwo3 = (TextView) rechargeView.findViewById(R.id.tv_sendmoneytwo3);
        initCardChoose();
    }

    private void clickSendMoneyStau(int selected) {
        switch (selected) {
            case 0:
                fillSendMoney(tv_sendmoneyone1, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
            case 1:
                fillSendMoney(tv_sendmoneyone2, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
            case 2:
                fillSendMoney(tv_sendmoneyone3, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
            case 3:
                fillSendMoney(tv_sendmoneytwo1, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
            case 4:
                fillSendMoney(tv_sendmoneytwo2, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
            case 5:
                fillSendMoney(tv_sendmoneytwo3, "#ffffff", "#ffffff", rechargeBackBeans, selected);
                break;
        }
    }

    /**
     * 为textview填充文字及对应的颜色
     *
     * @param textView
     * @param color1_3
     * @param color2
     * @param rechargeBackBeans
     * @param index
     */
    private void fillSendMoney(TextView textView, String color1_3, String color2, List<RechargeBackBean> rechargeBackBeans, int index) {
        int data = 0;
        if (rechargeBackBeans == null) {
            data = 0;
        } else {
            data = rechargeBackBeans.get(index).getBackAmount();
        }
        textView.setVisibility(View.GONE);
        if (data > 0) {
            textView.setText(Html.fromHtml("<font color='" + color1_3 + "'>" + "赠送" + "</font>" + "<font color='" + color2 + "'>" + data + "</font>" + "<font color='" + color1_3 + "'>" + "元余额" + "</font>"));
            textView.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 提现
     */
    private void withDrawal(View v) {

        CheckUserForegiftVo foregiftVo = (CheckUserForegiftVo) v.getTag();
        if (foregiftVo != null && foregiftVo.getFlag() == 4) {
            dialogShow();
            Map<String, String> backFeeparams = new HashMap<String, String>();
            backFeeparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            backFeeparams.put("flag", "2");
            YYRunner.getData(3002, YYRunner.Method_POST, YYUrl.APPLICATIONWITHDRAWAL,
                    backFeeparams, this);
        }

    }

    private YYTwoButtonDialog twoButtonDialog;

    private void showDrawalDialog() {
        if (twoButtonDialog == null) {
            twoButtonDialog = new YYTwoButtonDialog(mContext, "取消", "确认", "是否确认进行提现申请", "提示");
            twoButtonDialog.setOnDialogClick(new YYTwoButtonDialog.DialogClick() {
                @Override
                public void leftClick(View v, Dialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void Rightclick(View v, Dialog dialog) {
                    withDrawal(tv_withdrawdeposit);
                    dialog.dismiss();
                }
            });
        }
        twoButtonDialog.show();
    }

}
