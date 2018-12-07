package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ComplainTypeBean;
import com.dzrcx.jiaan.Bean.ComplainVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-10-18.
 */
public class ChooseSecrviceTypeFrg extends YYBaseFragment implements View.OnClickListener {


    private View contentView;
    private ImageView iv_back;
    private TextView tv_title;
    private PullToRefreshListView refreshListView;
    private ChooseSeciveTypeAdp seciveTypeAdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.aty_messagecentre, null);
            iv_back = (ImageView) contentView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            refreshListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list);
            iv_back.setOnClickListener(this);
            iv_back.setVisibility(View.VISIBLE);
            seciveTypeAdp = new ChooseSeciveTypeAdp(this);
            refreshListView.setAdapter(seciveTypeAdp);
//            refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//            refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//                @Override
//                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                    refreshListView.onRefreshComplete();
//                }
//            });
            tv_title.setText("请选择服务器类型");
            String str = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_SHOWALLCOMPLAINTYPE, "");
            ComplainTypeBean complainTypeBean = (ComplainTypeBean) GsonTransformUtil.fromJson(str, ComplainTypeBean.class);
            if (complainTypeBean != null && complainTypeBean.getErrno() == 0 && complainTypeBean.getReturnContent() != null) {
                int sum = complainTypeBean.getReturnContent().size();
                ArrayList<ComplainVo> types = new ArrayList<ComplainVo>();
                for (int i = 0; i < sum; i++) {
                    types.add(complainTypeBean.getReturnContent().get(i));
                }
                seciveTypeAdp.setMessageList(types);
                seciveTypeAdp.notifyDataSetChanged();

            }

        }

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
}
