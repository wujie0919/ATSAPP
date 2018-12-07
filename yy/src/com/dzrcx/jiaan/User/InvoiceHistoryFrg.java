package com.dzrcx.jiaan.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceHistoryReturn;
import com.dzrcx.jiaan.Bean.InvoiceItem;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 16-1-5.
 */
public class InvoiceHistoryFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener {

    private View invoiceHistoryView;
    private TextView barTitle;
    private ImageView iv_left_raw;
    private PullToRefreshListView pull_refresh_list;
    private List<InvoiceItem> list;
    private YYNotdataView notdataView;
    private InvoiceHistoryAdp listAdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (invoiceHistoryView == null) {
            invoiceHistoryView = LayoutInflater.from(mContext).inflate(R.layout.frg_invoicehistorylist, null);
            iv_left_raw = (ImageView) invoiceHistoryView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            barTitle = (TextView) invoiceHistoryView.findViewById(R.id.tv_title);
            barTitle.setText("开票记录");
            iv_left_raw.setOnClickListener(this);
            pull_refresh_list = (PullToRefreshListView) invoiceHistoryView.findViewById(R.id.pull_refresh_list);
            notdataView = new YYNotdataView(getActivity(), 80);
            notdataView.setMessage("您还没有开票记录哦");
            notdataView.setVisible(false);
            pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    requestData(false);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                }
            });
            list = new ArrayList<InvoiceItem>();
            listAdp = new InvoiceHistoryAdp(getActivity(), list);
            pull_refresh_list.getRefreshableView().addHeaderView(notdataView);

            pull_refresh_list.setAdapter(listAdp);
            requestData(true);

            pull_refresh_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    InvoiceItem invoiceItem = (InvoiceItem) listAdp.getItem(i - 2);
                    String invoiceId = invoiceItem.getInvoiceId() + "";
//                    if (invoiceItem.getType() == 3) {//增值发票
//                        Intent intent = new Intent(getActivity(), InvoiceDetailActivity.class);
//                        intent.putExtra("invoiceId", invoiceId);
//                        startActivity(intent);
//                    }else{//
//                        Intent intent = new Intent(getActivity(), ValueInvoiceDeailActivity.class);
//                        intent.putExtra("invoiceId", invoiceId);
//                        startActivity(intent);
//                    }

                    Intent intent = new Intent(getActivity(), InvoiceCompileteActivity.class);
                    intent.putExtra("state", invoiceItem.getState());
                    intent.putExtra("createTimes", invoiceItem.getCreateTimes());
                    startActivity(intent);

                }
            });
        }

        return invoiceHistoryView;
    }

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
        YYRunner.getData(1001, YYRunner.Method_POST,
                YYUrl.GETINVOICEHISTORY, params, this);
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(getActivity(), "数据错误请重试");
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
        InvoiceHistoryReturn invoiceHistoryReturn = (InvoiceHistoryReturn) GsonTransformUtil.fromJson(json, InvoiceHistoryReturn.class);
        if (0 == invoiceHistoryReturn.getErrno()) {
            list = invoiceHistoryReturn.getReturnContent().getInvoiceHistoryList();
            listAdp.replaceData(list);
            listAdp.notifyDataSetChanged();
            if (listAdp.getCount() == 0) {
                notdataView.setVisible(true);
            } else {
                notdataView.setVisible(false);
            }
        } else {
            MyUtils.showToast(getActivity(), invoiceHistoryReturn.getError());
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
        }
    }
}
