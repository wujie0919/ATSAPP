package com.dzrcx.jiaan.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ViolationListBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 事故
 * Created by zhangyu on 16-3-29.
 */
public class BreakRulesFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {

    private View breakRulesView;
    private View heardView_nodata, heardView_break;
    private ImageView iv_left_raw;
    private TextView tv_title, tv_right, tv_nodatatag;
    private BreakRulesAdp rulesAdp;
    private LayoutInflater inflater;
    private RelativeLayout rl_peccancy, rl_accident;
    private PullToRefreshListView pull_refresh_list;
    private ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals> listBreak;
    private int pageNum = 1;
    private boolean isDownRefresh;//是否下拉刷新
    private BreakRulesAty activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (breakRulesView == null) {
            breakRulesView = inflater.inflate(R.layout.frg_breakrules, null);
            initView();
            requestData(true, 0);
        }
        return breakRulesView;
    }

    private void initView() {
        heardView_break = inflater.inflate(R.layout.headview_break, null);
        iv_left_raw = (ImageView) breakRulesView.findViewById(R.id.iv_left_raw);
        rl_peccancy = (RelativeLayout) heardView_break.findViewById(R.id.rl_peccancy);
        rl_accident = (RelativeLayout) heardView_break.findViewById(R.id.rl_accident);
        rl_accident.setSelected(true);
        tv_title = (TextView) breakRulesView.findViewById(R.id.tv_title);
        tv_right = (TextView) breakRulesView.findViewById(R.id.tv_right);
        pull_refresh_list = (PullToRefreshListView) breakRulesView.findViewById(R.id.pull_refresh_list);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title.setText("违章事故");
        iv_left_raw.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("投诉");
        tv_right.setOnClickListener(this);
        rulesAdp = new BreakRulesAdp(mContext);
        listBreak = new ArrayList<>();
        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        heardView_nodata = layoutInflater.inflate(R.layout.headview_nobreak, null);
        tv_nodatatag = (TextView) heardView_nodata.findViewById(R.id.tv_nodatatag);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(heardView_break);
        linearLayout.addView(heardView_nodata);
        pull_refresh_list.getRefreshableView().addHeaderView(linearLayout);
        pull_refresh_list.setAdapter(rulesAdp);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData(false, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData(false, pageNum + 1);

            }
        });
        MyUtils.setViewsOnClick(this, rl_peccancy);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_right:
                showCallDialog();
                break;
            case R.id.rl_peccancy:
                activity.changeToFragment(1);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BreakRulesAty) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            boolean isPaySucceed = data.getBooleanExtra("isPaySucceed", false);
            String violationId = data.getStringExtra("violationId");
            if (isPaySucceed) {
                for (ViolationListBean.BreakIllegaRecontent.BreakIllegals breakIllegals : listBreak) {
                    if (violationId.equals(breakIllegals.getId() + "")) {
                        breakIllegals.setDealState(1);
                        rulesAdp.setViolationBeans(listBreak);
                        tv_nodatatag.setText("没有事故记录，棒棒哒");
                        showListData();
                        break;
                    }

                }
            }
        }
    }

    private void requestData(boolean ref, int pageNum) {
        if (NetHelper.checkNetwork(getActivity())) {
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        if (ref) {
            dialogShow();
        }
        if (pageNum == 1) {
            isDownRefresh = true;
        } else {
            isDownRefresh = false;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("vType", 2 + "");
        params.put("pageNum", pageNum + "");
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.GETVIOLATION,
                params, this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        pull_refresh_list.onRefreshComplete();
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        pull_refresh_list.onRefreshComplete();
        ViolationListBean listBean = (ViolationListBean) GsonTransformUtil.fromJson(json, ViolationListBean.class);

        if (listBean != null && listBean.getErrno() == 0) {
            if (isDownRefresh) {
                pageNum = 0;
                listBreak = (ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals>) listBean.getReturnContent().getViolationInfoVoForLists();
            } else {
                if (listBean.getReturnContent().getViolationInfoVoForLists().size() == 0) {
                    MyUtils.showToast(mContext, "已经没有更多数据了");
                } else {
                    pageNum++;
                    listBreak.addAll((ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals>) listBean.getReturnContent().getViolationInfoVoForLists());
                }
            }
            rulesAdp.setViolationBeans(listBreak);
            tv_nodatatag.setText("没有事故记录，棒棒哒");
        } else {
            isDownRefresh = !isDownRefresh;
            MyUtils.showToast(mContext, listBean.getError());
        }
        showListData();
    }

    private void showListData() {
        rulesAdp.notifyDataSetChanged();
        heardView_nodata.setVisibility(View.GONE);
        if (rulesAdp.getCount() == 0) {
            heardView_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(
                    menuVisible ? getView().VISIBLE : getView().GONE);
        }
    }
}
