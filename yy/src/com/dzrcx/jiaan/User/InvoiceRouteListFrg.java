package com.dzrcx.jiaan.User;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceRouteListBeanIn;
import com.dzrcx.jiaan.Bean.InvoiceRouteListBeanOut;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.InvoiceListMoneyListener;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.LG;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceRouteListFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {
    private View invoiceroutelist, headView;
    private TextView tv_title, tv_receiptRoles, tv_right, tv_money_num, tv_route_num, tv_next, tv_totalmoney;
    private RelativeLayout rl_check;
    private PullToRefreshListView pull_refresh_list;
    private LayoutInflater inflater;
    private YYNotdataView notdataView;
    private List<InvoiceRouteListBeanIn> list;
    private InvoiceRouteListBeanOut countOrderBean;
    private InvoiceRouteListAdp listAdp;
    private ImageView iv_check, iv_left_raw;
    private double feeCount = 0;
    private int routeCount = 0;
    private Dialog chooseDlg;
    private DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (invoiceroutelist == null) {
            this.inflater = inflater;
            invoiceroutelist = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_invoiceroutelist, null);
            initView();
            requestData(true);
        }
        return invoiceroutelist;
    }

    InvoiceListMoneyListener invoiceListMoneyListener = new InvoiceListMoneyListener() {
        @Override
        public void changeCount(double num, boolean ischeck) {
            if (ischeck) {
                feeCount = feeCount + num;
                routeCount = routeCount + 1;
            } else {
                feeCount = feeCount - num;
                routeCount = routeCount - 1;
            }
            if (routeCount == list.size()) {
                iv_check.setSelected(true);
            } else {
                iv_check.setSelected(false);
            }
            feeCount = Double.parseDouble(df.format(feeCount));
            tv_money_num.setText(" " + feeCount + " ");
            tv_route_num.setText(routeCount + "");
        }
    };


    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(getActivity())) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            showNoNetDlg();
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", "2");
        YYRunner.getData(0, YYRunner.Method_POST,
                YYUrl.GETINVOICEROUTEINFO, params, this);
    }

    public void initView() {
        tv_title = (TextView) invoiceroutelist.findViewById(R.id.tv_title);
        tv_title.setText("按行程开票");
        iv_left_raw = (ImageView) invoiceroutelist.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_right = (TextView) invoiceroutelist.findViewById(R.id.tv_right);
//        tv_right.setText("开票历史");
//        tv_right.setVisibility(View.VISIBLE);
        tv_money_num = (TextView) invoiceroutelist.findViewById(R.id.tv_money_num);
        tv_route_num = (TextView) invoiceroutelist.findViewById(R.id.tv_route_num);
        tv_totalmoney = (TextView) invoiceroutelist.findViewById(R.id.tv_totalmoney);
        tv_next = (TextView) invoiceroutelist.findViewById(R.id.tv_next);
        rl_check = (RelativeLayout) invoiceroutelist.findViewById(R.id.rl_check);
        iv_check = (ImageView) invoiceroutelist.findViewById(R.id.iv_checkbox);
        pull_refresh_list = (PullToRefreshListView) invoiceroutelist.findViewById(R.id.pull_refresh_list);
        headView = inflater.inflate(R.layout.headview_invoicelist, null);
        tv_receiptRoles = (TextView) headView.findViewById(R.id.tv_receiptRoles);
        notdataView = new YYNotdataView(getActivity(), 195);
        notdataView.setMessage("还没有行程可开票据哦～");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(headView);
        linearLayout.addView(notdataView);
        notdataView.setVisible(false);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                requestData(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
            }
        });
        list = new ArrayList<InvoiceRouteListBeanIn>();
        listAdp = new InvoiceRouteListAdp(getActivity(), list, invoiceListMoneyListener);
        pull_refresh_list.getRefreshableView().addHeaderView(linearLayout);
        pull_refresh_list.setAdapter(listAdp);
        MyUtils.setViewsOnClick(this, tv_receiptRoles, tv_right, rl_check, tv_next, iv_left_raw, invoiceroutelist.findViewById(R.id.ll_bottom));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_receiptRoles:
                startActivity(new Intent(this.getActivity(), WebAty.class)
                        .putExtra("title", "开票说明").putExtra("url",
                                YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=receiptRoles"));
                mContext.overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.tv_right:
                startActivity(InvoiceHistoryAty.class, null);
                break;
            case R.id.tv_next:
                if (list == null || listAdp == null || countOrderBean.getReturnContent() == null)
                    return;
                if (countOrderBean.getReturnContent().getInvoiceAll() <= 0) {
                    MyUtils.showToast(getActivity(), "你没有发票可以开具哦~");
                } else if (routeCount <= 0) {
                    MyUtils.showToast(getActivity(), "您还未选择行程哦~");
                } else
//                if (feeCount < 200) {
//                    showChooseDialog("需满两百元才可免费开具发票");
//                } else
                    if (feeCount > countOrderBean.getReturnContent().getInvoiceAll()) {
                        showChooseDialog("您勾选行程总金额大于可开发票金额，请重新勾选");
                    } else {
                        String orderIdStr = "";
                        for (InvoiceRouteListBeanIn invoiceRouteListBeanIn : list) {
                            if (invoiceRouteListBeanIn.getChecked()) {
                                orderIdStr = orderIdStr + invoiceRouteListBeanIn.getOrderId() + ",";
                            }
                        }
                        LG.d(orderIdStr + "------------");
                        orderIdStr = orderIdStr.substring(0, orderIdStr.length()
                                - 1);
                        LG.d(orderIdStr + "------------");
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "2");
                        bundle.putString("orderIdStr", orderIdStr);
                        bundle.putString("amount", feeCount + "");
                        startActivity(InvoiceAty.class, bundle);
                    }
                break;
            case R.id.rl_check:
                iv_check.setSelected(!iv_check.isSelected());
                if (list == null || listAdp == null)
                    return;
                feeCount = 0.0;
                if (iv_check.isSelected()) {
                    for (InvoiceRouteListBeanIn invoiceRouteListBeanIn : list) {
                        invoiceRouteListBeanIn.setChecked(iv_check.isSelected());
                        feeCount = feeCount + invoiceRouteListBeanIn.getAmount();
                    }
                    routeCount = list.size();
                } else {
                    for (InvoiceRouteListBeanIn invoiceRouteListBeanIn : list) {
                        invoiceRouteListBeanIn.setChecked(iv_check.isSelected());
                    }
                    feeCount = 0;
                    routeCount = 0;
                }
                listAdp.notifyDataSetChanged();
                feeCount = Double.parseDouble(df.format(feeCount));
                tv_money_num.setText(" " + feeCount + " ");
                tv_route_num.setText(routeCount + "");
                break;
        }
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(getActivity(), "数据传输错误，请重试");
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        pull_refresh_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_list.onRefreshComplete();
            }
        }, 100);
        countOrderBean = (InvoiceRouteListBeanOut) GsonTransformUtil.fromJson(
                json, InvoiceRouteListBeanOut.class);
        if (countOrderBean.getErrno() != 0) {
            MyUtils.showToast(getActivity(), countOrderBean.getError());
        } else {
            fillData(countOrderBean);
        }
    }

    public void fillData(InvoiceRouteListBeanOut countOrderBean) {
        list = countOrderBean.getReturnContent().getOrderList();
        listAdp.replaceData(list);
        listAdp.notifyDataSetChanged();
        iv_check.setSelected(false);
        feeCount = 0;
        routeCount = 0;
        tv_money_num.setText(" " + df.format(feeCount) + " ");
        tv_route_num.setText(routeCount + "");
        if (listAdp.getCount() == 0) {
            notdataView.setVisible(true);
            headView.setVisibility(View.GONE);
        } else {
            notdataView.setVisible(false);
            headView.setVisibility(View.VISIBLE);
        }
        tv_totalmoney.setText("可开额度" + countOrderBean.getReturnContent().getInvoiceAll() + "元，200元以上免费开具发票");
    }

    @Override
    public void onLoading(long count, long current) {

    }

    /**
     * 打开拨打客服电话弹窗
     */
    public void showChooseDialog(String messge) {
        if (chooseDlg == null) {
            chooseDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_choose, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("确认");
            tv_cancel_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    chooseDlg.dismiss();
//                    getActivity().finish();
                }
            });
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            if (!TextUtils.isEmpty(messge)) {
                tv_number.setText(messge);
            }
            tv_call_txt.setText("联系客服");
            tv_call_txt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    try {
                        MobclickAgent.onEvent(mContext, "click_call");
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:"
                                        + YYConstans.getSysConfig().getReturnContent().getServicePhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mContext.overridePendingTransition(
                                R.anim.activity_up, R.anim.activity_push_no_anim);
                        chooseDlg.dismiss();
                    } catch (Exception e) {
                        MyUtils.showToast(mContext, "请检查是否开启电话权限");
                    }
                }
            });
            chooseDlg.setCanceledOnTouchOutside(false);
            chooseDlg.setContentView(mDlgCallView);
        }
        chooseDlg.show();

        Window dlgWindow = chooseDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
    }
}
