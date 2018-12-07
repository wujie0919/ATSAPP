package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceRouteListBeanOut;
import com.dzrcx.jiaan.Bean.OrderListBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.User.BreakRulesAty;
import com.dzrcx.jiaan.User.InvoiceAty;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.RiseNumberTextView;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的行程
 */
public class MyRunsListFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener {
    private View myRunsListView;
    private PullToRefreshListView pull_refresh_list;
    private TextView tv_title;
    private ImageView iv_left_raw;
    private TextView tv_breaks, tv_invoicemoney;
    private LinearLayout ll_invoicemoney, ll_breaks;
    private List<OrderListItemBean> list;
    private MyRunsListAdp listAdp;
    private LayoutInflater inflater;
    private View heardView;
    private YYNotdataView notdataView;
    private int pageNo = 1;

    public Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (myRunsListView == null) {
            myRunsListView = inflater.inflate(R.layout.frg_myruns, null);
            initView();
            requestData(true);
        }
        return myRunsListView;
    }

    /**
     * 请求订单列表
     * queryOrders.do
     * 1等待取车
     * 2正在使用
     * 3等待支付
     * 4订单完成
     * 5订单取消
     *
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_list.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_list.onRefreshComplete();
                }
            }, 100);
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("status", "2");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", "10");
        YYRunner.getData(YYConstans.TAG_ORDERLISTATY2_1, YYRunner.Method_POST,
                YYUrl.GETORDERLIST, params, this);
//        requestCount();
    }

//    private void requestCount() {
//        Map<String, String> params2 = new HashMap<String, String>();
//        params2.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
//        YYRunner.getData(YYConstans.TAG_GETCOUNTORDERS, YYRunner.Method_POST,
//                YYUrl.GETCOUNTORDERS, params2, this);
//    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initView() {
        // TODO Auto-generated method stub
        pull_refresh_list = (PullToRefreshListView) myRunsListView.findViewById(R.id.pull_refresh_list);
        inflater = LayoutInflater.from(mContext);
        heardView = inflater.inflate(R.layout.headview_runs, null);
        notdataView = new YYNotdataView(mContext);
        notdataView.setMessage("还没有行程记录哦～");
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(heardView);
        notdataView.setVisible(false);
        linearLayout.addView(notdataView);

        ll_invoicemoney = (LinearLayout) heardView.findViewById(R.id.ll_invoicemoney);
        ll_breaks = (LinearLayout) heardView.findViewById(R.id.ll_breaks);
        tv_invoicemoney = (TextView) heardView.findViewById(R.id.tv_invoicemoney);
        tv_breaks = (TextView) heardView.findViewById(R.id.tv_breaks);

        tv_title = (TextView) myRunsListView.findViewById(R.id.tv_title);
        iv_left_raw = (ImageView) myRunsListView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title = (TextView) myRunsListView.findViewById(R.id.tv_title);
        tv_title.setText("我的行程");
        pull_refresh_list = (PullToRefreshListView) myRunsListView.findViewById
                (R.id.pull_refresh_list);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                pageNo = 1;
                requestData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                pageNo++;
                requestData(false);
            }
        });
        list = new ArrayList<OrderListItemBean>();
        listAdp = new MyRunsListAdp(mContext, list);
        pull_refresh_list.getRefreshableView().addHeaderView(linearLayout);
        pull_refresh_list.setAdapter(listAdp);
        MyUtils.setViewsOnClick(this, ll_invoicemoney, ll_breaks, iv_left_raw);
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        pull_refresh_list.onRefreshComplete();
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        pull_refresh_list.onRefreshComplete();
        switch (tag) {
            case YYConstans.TAG_ORDERLISTATY2_1://请求订单列表
                OrderListBean listBean = (OrderListBean) GsonTransformUtil.fromJson(
                        json, OrderListBean.class);
                if (listBean == null || listBean.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            listBean == null ? "数据传输错误，请重试" : listBean.getError() + "");
                    return;
                }
                tv_invoicemoney.setText(listBean.getReturnContent().getVailableInvoice() + "元");
                tv_breaks.setText(listBean.getReturnContent().getViolationCount() + "次");
                if (pageNo == 1 && listBean.getReturnContent() != null) {
                    list = listBean.getReturnContent().getOrderList();
                } else {
                    if (listBean.getReturnContent() == null || listBean.getReturnContent().getOrderList().size() == 0) {
                        MyUtils.showToast(mContext, "已经没有更多订单了");
                        return;
                    }
                    list.addAll(listBean.getReturnContent().getOrderList());
                }
                listAdp.replaceData(list);
                listAdp.notifyDataSetChanged();
                if (listAdp.getCount() == 0) {
                    notdataView.setVisible(true);
                    heardView.setVisibility(View.GONE);
                } else {
                    notdataView.setVisible(false);
                    heardView.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    private void startNumber(float num, RiseNumberTextView v) {
        v.withNumber(num);
        v.setDuration(1200);
        v.start();
    }

    private void startNumber(int num, RiseNumberTextView v) {
        v.withNumber(num);
        v.setDuration(1200);
        v.start();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.ll_invoicemoney:
                requestData();
                //startActivity(InvoiceIssueAty.class, null);
                break;
            case R.id.ll_breaks:
                startActivity(BreakRulesAty.class, null);
                break;
            default:
                break;
        }
    }

    private void requestData() {
        if (NetHelper.checkNetwork(getActivity())) {
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", "1");
        YYRunner.getData(0, YYRunner.Method_POST,
                YYUrl.GETINVOICEROUTEINFO, params, new RequestInterface() {
                    @Override
                    public void onError(int tag, String error) {
                        dismmisDialog();
                        MyUtils.showToast(getActivity(), "数据传输错误，请重试");
                    }

                    @Override
                    public void onComplete(int tag, String json) {
                        dismmisDialog();
                        InvoiceRouteListBeanOut invoiceRouteListBeanOut = (InvoiceRouteListBeanOut) GsonTransformUtil.fromJson(json, InvoiceRouteListBeanOut.class
                        );
                        if (invoiceRouteListBeanOut.getErrno() != 0) {
                            MyUtils.showToast(getActivity(), invoiceRouteListBeanOut.getError());
                        } else {
                            if (invoiceRouteListBeanOut.getReturnContent().getInvoiceAll() <= 0) {
                                MyUtils.showToast(getActivity(), "暂无可发票可开具");
                                return;
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "1");//1=金额；2=行程
                            bundle.putString("amount", invoiceRouteListBeanOut.getReturnContent().getInvoiceAll() + "");//1=金额；2=行程
                            startActivity(InvoiceAty.class, bundle);
                        }
                    }

                    @Override
                    public void onLoading(long count, long current) {

                    }
                });
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }
}
