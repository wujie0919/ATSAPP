package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;

/**
 * Created by zhangyu on 16-10-11.
 */
public class IdentificationReqFrg extends YYBaseFragment implements View.OnClickListener {


    private View contextView;

    private ImageView iv_bac;
    private TextView tv_title;
    private TextView tv_user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (contextView == null) {
            contextView = inflater.inflate(R.layout.aty_identifyreq, null);

            iv_bac = (ImageView) contextView.findViewById(R.id.iv_left_raw);
            iv_bac.setVisibility(View.VISIBLE);
            tv_title = (TextView) contextView.findViewById(R.id.tv_title);
            tv_user = (TextView) contextView.findViewById(R.id.identfy_req_user);

            tv_title.setText("身份认证");
            iv_bac.setOnClickListener(this);
            tv_user.setOnClickListener(this);
        }


        return contextView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.identfy_req_user:
                showCallDialog();
                break;
        }

    }
}
