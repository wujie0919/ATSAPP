package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceRouteListBeanOut;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;

import java.util.HashMap;
import java.util.Map;

public class InvoiceIssueFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {
    private View invoiceIssueView;
    private RelativeLayout rl_route, rl_money, rl_invocicehistory;
    private TextView tv_title;
    private ImageView iv_left_raw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (invoiceIssueView == null) {
            invoiceIssueView = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_invoiceissue, null);
            initView();
        }
        return invoiceIssueView;
    }

    public void initView() {
        tv_title = (TextView) invoiceIssueView.findViewById(R.id.tv_title);
        tv_title.setText("发票开具");
        iv_left_raw = (ImageView) invoiceIssueView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        rl_route = (RelativeLayout) invoiceIssueView.findViewById(R.id.rl_route);
        rl_money = (RelativeLayout) invoiceIssueView.findViewById(R.id.rl_money);
        rl_invocicehistory = (RelativeLayout) invoiceIssueView.findViewById(R.id.rl_invocicehistory);
        MyUtils.setViewsOnClick(this, rl_route, rl_money, iv_left_raw, rl_invocicehistory);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.rl_route:
                startActivity(InvoiceRouteListAty.class, null);
                break;
            case R.id.rl_money:
                requestData();
                break;
            case R.id.rl_invocicehistory:

                startActivity(InvoiceHistoryAty.class, null);
                break;
        }

    }

    public void requestData() {
        if (NetHelper.checkNetwork(getActivity())) {
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", "1");
        YYRunner.getData(0, YYRunner.Method_POST,
                YYUrl.GETINVOICEROUTEINFO, params, this);
    }

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
}
