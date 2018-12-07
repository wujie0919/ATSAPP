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
 * Created by zhangyu on 16-10-12.
 */
public class TransferThreeFrg extends YYBaseFragment implements View.OnClickListener {


    private View contextView;

    private ImageView iv_back;
    private TextView tv_title, tv_submit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contextView == null) {
            contextView = inflater.inflate(R.layout.aty_transferthree, null);
            iv_back = (ImageView) contextView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contextView.findViewById(R.id.tv_title);
            tv_submit = (TextView) contextView.findViewById(R.id.transferone_back);
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("账户迁移成功");
            iv_back.setOnClickListener(this);
            tv_submit.setOnClickListener(this);

        }
        return contextView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.transferone_back:
                getActivity().finish();
                break;
        }

    }
}
