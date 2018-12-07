package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.BreakRulesDetailBean;
import com.dzrcx.jiaan.Bean.BreakRulesDetailReturnContent;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class BreakRulesDetailFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {
    private View frg_breakrulesdetail;
    private Intent intent;
    private Activity context;
    private ImageView iv_left_raw;
    private TextView tv_title, tv_right;
    private TextView tv_cartext, tv_order_type, tv_getcar_name, tv_gavecar_name, tv_breaktime, tv_breakwhere, tv_breaktype, tv_breakprogress, tv_break_punish, tv_break_money, tv_needtopay, tv_nowpay;
    private BreakRulesDetailBean breakRulesDetailBean;
    private View line_where;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (frg_breakrulesdetail == null) {
            frg_breakrulesdetail = inflater.inflate(R.layout.frg_breaksdetail, null);
            initView();
        }
        requestData(true);
        return frg_breakrulesdetail;
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
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("violationId", context.getIntent().getStringExtra("violationId"));
        YYRunner.getData(1001, YYRunner.Method_POST,
                YYUrl.GETVIOLATIONDETAIL, params, this);
    }

    private void initView() {
        intent = context.getIntent();
        iv_left_raw = (ImageView) frg_breakrulesdetail.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_title);
        tv_title.setText(intent.getStringExtra("title") + "");
        tv_right = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_right);
        tv_right.setText("投诉");
        tv_right.setVisibility(View.VISIBLE);
        tv_order_type = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_order_type);
        line_where = frg_breakrulesdetail.findViewById(R.id.line_where);
        tv_cartext = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_cartext);
        tv_getcar_name = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_getcar_name);
        tv_gavecar_name = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_gavecar_name);
        tv_breaktime = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_breaktime);
        tv_breakwhere = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_breakwhere);
        tv_breaktype = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_breaktype);
        tv_breakprogress = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_breakprogress);
        tv_break_punish = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_break_punish);
        tv_break_money = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_break_money);
        tv_needtopay = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_needtopay);
        tv_nowpay = (TextView) frg_breakrulesdetail.findViewById(R.id.tv_nowpay);
        MyUtils.setViewsOnClick(this, iv_left_raw, tv_right, tv_nowpay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                context.finish();
                break;
            case R.id.tv_right:
                showCallDialog();
                break;
            case R.id.tv_nowpay:
                Intent intent = new Intent(context, BreakRulesPayAty.class);
                intent.putExtra("violationId", breakRulesDetailBean.getId() + "");
                intent.putExtra("number", breakRulesDetailBean.getDeductCashAmount() + "");
                startActivityForResult(intent, 1001);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            boolean isPaySucceed = data.getBooleanExtra("isPaySucceed", false);
            if (isPaySucceed) {
                tv_nowpay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.breakdetail_pay_not));
                tv_nowpay.setText("已支付");
                tv_nowpay.setEnabled(false);
            }
        }
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                BreakRulesDetailReturnContent breakRulesDetailReturnContent = (BreakRulesDetailReturnContent) GsonTransformUtil.fromJson(json, BreakRulesDetailReturnContent.class);
                if (breakRulesDetailReturnContent == null || breakRulesDetailReturnContent.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            breakRulesDetailReturnContent == null ? "数据传输错误，请重试" : breakRulesDetailReturnContent.getError() + "");
                } else {
                    breakRulesDetailBean = breakRulesDetailReturnContent.getReturnContent();
                    fillAccident(breakRulesDetailBean.getvType(), breakRulesDetailBean);
                }
                break;
        }
    }


    /**
     * 填充事故数据
     */
    private void fillAccident(int type, BreakRulesDetailBean breakRulesDetailBean) {
        if (type == 1) {
            tv_order_type.setText("违章订单：" + breakRulesDetailBean.getOrderId());
            tv_cartext.setText("租用车辆：" + context.getIntent().getStringExtra("carname"));
            tv_breaktime.setText("违章时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getVioTime()));
            tv_breakwhere.setText("违章地点：" + context.getIntent().getStringExtra("address"));
            tv_getcar_name.setText("取车时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getPickTime()));
            tv_gavecar_name.setText("还车时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getEndTime()));
            tv_breaktype.setText("违章类型：" + breakRulesDetailBean.getType());
            tv_break_punish.setText("违章处罚：扣" + breakRulesDetailBean.getPoints() + "分");
            tv_break_money.setText("罚款：" + breakRulesDetailBean.getAmount() + "元");
            switch (breakRulesDetailBean.getDealState()) {
                case 0:
                    if (breakRulesDetailBean.getConfirmState() == 1) {
                        if (breakRulesDetailBean.getDeductCashState() == 1) {
                            tv_breakprogress.setText("处理进度：已处理");
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        } else {
                            tv_breakprogress.setText("处理进度：未处理");
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setEnabled(true);
                            tv_nowpay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mapcarfrg_fastrentcar_selector));
                            tv_nowpay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_breakprogress.setText("处理进度：未处理");
                        tv_needtopay.setText("需支付金额： 正在核算");
                        tv_nowpay.setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    if (breakRulesDetailBean.getConfirmState() == 1) {
                        if (breakRulesDetailBean.getDeductCashState() == 1) {
                            tv_breakprogress.setText("处理进度：已处理");
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        } else {
                            tv_breakprogress.setText("处理进度：处理中");
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        }
                    } else {
                        tv_breakprogress.setText("处理进度：处理中");
                        tv_needtopay.setText("需支付金额： 正在核算");
                        tv_nowpay.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    tv_breakprogress.setText("处理进度：已处理");
                    tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                    tv_nowpay.setVisibility(View.GONE);
                    break;
            }
        } else if (type == 2) {
            tv_breakwhere.setVisibility(View.GONE);
            line_where.setVisibility(View.GONE);
            tv_order_type.setText("事故订单：" + breakRulesDetailBean.getOrderId());
            tv_cartext.setText("租用车辆：" + context.getIntent().getStringExtra("carname"));
            tv_getcar_name.setText("取车时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getPickTime()));
            tv_gavecar_name.setText("还车时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getEndTime()));
            tv_breaktime.setText("事故时间：" + TimeDateUtil.dateToStrLong(breakRulesDetailBean.getVioTime()));
            tv_breaktype.setText("事故描述：" + breakRulesDetailBean.getDesc());
            tv_break_punish.setText("事故程度：" + context.getIntent().getStringExtra("nature"));
            tv_breakprogress.setText("事故造成经济损失：" + (breakRulesDetailBean.getRepairCost() + breakRulesDetailBean.getOutageLoss()) + "元");
            tv_break_money.setVisibility(View.GONE);
            switch (breakRulesDetailBean.getDealState()) {
                case 0:
                    if (breakRulesDetailBean.getConfirmState() == 1) {
                        if (breakRulesDetailBean.getDeductCashState() == 1) {
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        } else {
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setEnabled(true);
                            tv_nowpay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mapcarfrg_fastrentcar_selector));
                            tv_nowpay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tv_needtopay.setText("需支付金额： 正在核算");
                        tv_nowpay.setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    if (breakRulesDetailBean.getConfirmState() == 1) {
                        if (breakRulesDetailBean.getDeductCashState() == 1) {
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        } else {
                            tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                            tv_nowpay.setVisibility(View.GONE);
                        }
                    } else {
                        tv_needtopay.setText("需支付金额： 正在核算");
                        tv_nowpay.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    tv_needtopay.setText("需支付金额： " + breakRulesDetailBean.getDeductCashAmount() + "元");
                    tv_nowpay.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }
}
