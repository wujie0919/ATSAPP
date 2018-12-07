package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;

/**
 * Created by zhangyu on 16-10-11.
 */
public class TransferOneFrg extends YYBaseFragment implements View.OnClickListener {


    private View contextView;

    private ImageView iv_back;
    private TextView tv_title;
    private EditText ed_phone;
    private TextView tv_next;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contextView == null) {
            contextView = inflater.inflate(R.layout.aty_transferone, null);

            iv_back = (ImageView) contextView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contextView.findViewById(R.id.tv_title);
            ed_phone = (EditText) contextView.findViewById(R.id.transferone_phone);
            tv_next = (TextView) contextView.findViewById(R.id.transferone_next);

            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("账户迁移");
            iv_back.setOnClickListener(this);
            tv_next.setOnClickListener(this);

        }

        return contextView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.transferone_next:
                String phone = ed_phone.getText().toString();
                if (phone.length() < 11) {
                    MyUtils.showToast(mContext, "请输入正确的手机号");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                startActivity(TransferTwoAty.class, bundle);
                getActivity().finish();
                break;
        }

    }
}
