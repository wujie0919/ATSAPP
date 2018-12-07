package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.RechargeRecordListBean;
import com.dzrcx.jiaan.Bean.RechargeRecordListVo;
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
import com.dzrcx.jiaan.widget.PinnedSectionListView;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户明细
 */
public class RechargeRecordFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface {


    private View rechargeRecordView;
    private PullToRefreshScrollView pull_refresh_scrollview;//外层scrollview
    private PinnedSectionListView pinnedSectionListView;//N内层分组悬浮listview
    private RechargeRecordAdp listViewAdapter;
    private List<RechargeRecordListVo> recordListVos;//处理过的数据
    private List<RechargeRecordListVo> recordListVosOld;//原始数据
    private YYNotdataView notdataView;
    private int pageNum = 1;
    private boolean isDownRefresh;//是否下拉刷新
    private ImageView iv_left_raw;
    private TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rechargeRecordView == null) {
            rechargeRecordView = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_rechargerecord, null);
            initTile();


            pull_refresh_scrollview = (PullToRefreshScrollView) rechargeRecordView
                    .findViewById(R.id.pull_refresh_scroll);
            pinnedSectionListView = (PinnedSectionListView) rechargeRecordView
                    .findViewById(R.id.pinnedSectionListView);
            pinnedSectionListView.setShadowVisible(false);
            pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
            /**
             * 添加listview 头部信息
             */
            notdataView = new YYNotdataView(mContext, 70);
            pinnedSectionListView.addHeaderView(notdataView);
            recordListVos = new ArrayList<>();
            recordListVosOld = new ArrayList<>();
            listViewAdapter = new RechargeRecordAdp(mContext, recordListVos);
            pinnedSectionListView.setAdapter(listViewAdapter);
            pinnedSectionListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    // TODO Auto-generated method stub
                    if (firstVisibleItem == 0 && getScrollY(view) == 0) {
                        pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else if (isListViewReachBottomEdge(view)) {
                        pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    } else {
                        pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
            });
            showNullData("亲爱的\n您还没有账户记录哦！");
            pull_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

                @Override
                public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                    // TODO Auto-generated method stub
                    initData(false, 1);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                    // TODO Auto-generated method stub
                    if (listViewAdapter.getCount() == 0) {
                        pull_refresh_scrollview.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pull_refresh_scrollview.onRefreshComplete();
                            }
                        }, 50);
                        return;
                    }
                    initData(false, pageNum + 1);
                }
            });
            initData(true, 1);
            MyUtils.setViewsOnClick(this, iv_left_raw);
        }
        return rechargeRecordView;
    }

    private void showNullData(String msg) {
        if (listViewAdapter.getCount() == 0) {
            notdataView.setVisible(true, msg);
        } else {
            notdataView.setVisible(false);
        }
    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    private void initTile() {
        iv_left_raw = (ImageView) rechargeRecordView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        titleView = (TextView) rechargeRecordView.findViewById(R.id.tv_title);
        titleView.setText("账户明细");
        iv_left_raw.setOnClickListener(this);
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    private void initData(boolean isShowDialog, int number) {
        if (NetHelper.checkNetwork(getActivity())) {
            pull_refresh_scrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_scrollview.onRefreshComplete();
                }
            }, 100);
            showNoNetDlg();
            MyUtils.showToast(getActivity(), "网络异常，请检查网络连接或重试");
            return;
        }
        if (number == 1) {
            isDownRefresh = true;
        } else {
            isDownRefresh = false;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> weixipara = new HashMap<String, String>();
        weixipara.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        weixipara.put("pageNum", number + "");
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.GETSHOWALLUSERTRADESRECORD,
                weixipara, this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
        }

    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(getActivity(), "数据传输错误，请重试");
        pull_refresh_scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_scrollview.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        pull_refresh_scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_scrollview.onRefreshComplete();
            }
        }, 100);
        RechargeRecordListBean recordListBean = (RechargeRecordListBean) GsonTransformUtil
                .fromJson(json, RechargeRecordListBean.class);
        if (recordListBean != null && recordListBean.getErrno() == 0 && recordListBean.getReturnContent() != null) {
            if (isDownRefresh) {
                pageNum = 1;
                recordListVosOld.clear();
                recordListVosOld = recordListBean.getReturnContent();
                handlerData();
                filldata();
            } else {
                if (recordListBean.getReturnContent().size() == 0) {
                    MyUtils.showToast(mContext, "已经没有更多数据了");
                } else {
                    pageNum++;
                    recordListVosOld.addAll(recordListBean.getReturnContent());
                    handlerData();
                    filldata();
                }
            }
        }
    }

    private void filldata() {
        listViewAdapter.replaceData(recordListVos);
        listViewAdapter.notifyDataSetChanged();
        showNullData("亲爱的,您还没有账户记录哦！");
    }

    private void handlerData() {
        recordListVos.clear();
        String lastYM = "";
        for (RechargeRecordListVo rechargeRecordListVo : recordListVosOld) {
            if (!lastYM.equals(TimeDateUtil.dateToYM(rechargeRecordListVo.getTradesTime()))) {
                if (recordListVos.size() >= 1) {
                    recordListVos.get(recordListVos.size() - 1).setIslast(true);
                }
                recordListVos.add(new RechargeRecordListVo(listViewAdapter.getTAG_SECTION(), rechargeRecordListVo.getTradesTime()));
            }
            rechargeRecordListVo.setType(listViewAdapter.getTAG_CONTENT());
            recordListVos.add(rechargeRecordListVo);
            lastYM = TimeDateUtil.dateToYM(rechargeRecordListVo.getTradesTime());
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }
}
