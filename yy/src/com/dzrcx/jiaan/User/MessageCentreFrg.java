package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.MessageListBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 16-10-13.
 */
public class MessageCentreFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener {


    private View contentView;

    private ImageView iv_back;
    private TextView tv_title;
    private PullToRefreshListView refreshListView;
    private MessageCentreAdp centreAdp;
    private YYNotdataView notdataView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.aty_messagecentre, null);
            iv_back = (ImageView) contentView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            refreshListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);

            tv_title.setText("消息中心");
            iv_back.setOnClickListener(this);
            iv_back.setVisibility(View.VISIBLE);
            notdataView = new YYNotdataView(mContext, 70);
            notdataView.setMessage("亲爱的\n还没有系统通知哦！");
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(notdataView);
            notdataView.setVisible(false);
            refreshListView.getRefreshableView().addHeaderView(linearLayout);
            centreAdp = new MessageCentreAdp(this);
            refreshListView.setAdapter(centreAdp);
            refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getMessages(0);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getMessages((int) (Math.ceil(centreAdp.getCount() * 1.0 / 10) + 1));
                }
            });
        }
        getMessages(0);
        return contentView;
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
        refreshListView.onRefreshComplete();
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        refreshListView.onRefreshComplete();
        switch (tag) {
            case 1001:
                MessageListBean messageListBean = (MessageListBean) GsonTransformUtil.fromJson(json, MessageListBean.class);
                if (messageListBean != null && messageListBean.getErrno() == 0) {
                    centreAdp.getMessageList().clear();
                    centreAdp.getMessageList().addAll(messageListBean.getReturnContent());
                    centreAdp.notifyDataSetChanged();
                    if (centreAdp.getCount() == 0) {
                        notdataView.setVisible(true);
                    } else {
                        notdataView.setVisible(false);
                    }
                }
                break;
        }
        switch (tag) {
            case 2001:
                MessageListBean messageListBean = (MessageListBean) GsonTransformUtil.fromJson(json, MessageListBean.class);
                if (messageListBean != null && messageListBean.getErrno() == 0) {
                    centreAdp.getMessageList().addAll(messageListBean.getReturnContent());
                    centreAdp.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }


    private void getMessages(int page) {

        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("userId", YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "");

        if (page == 0) {
            page = 1;
            params.put("pageNo", page + "");
            YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.GETPUSHMESSAGELIST,
                    params, this);
        } else {
            params.put("pageNo", page + "");
            YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.GETPUSHMESSAGELIST,
                    params, this);
        }

    }


}
