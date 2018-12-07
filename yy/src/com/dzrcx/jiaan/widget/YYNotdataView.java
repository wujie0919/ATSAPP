package com.dzrcx.jiaan.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;

public class YYNotdataView extends LinearLayout {

    private LayoutInflater mInflater;
    private TextView textView;

    private LinearLayout.LayoutParams layoutParamsVisible;

    private LinearLayout mLinearLayout;

    public YYNotdataView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        View rootview = mInflater.inflate(R.layout.yy_nodata, this);
        mLinearLayout = (LinearLayout) rootview
                .findViewById(R.id.yy_nodata_layout);
        textView = (TextView) rootview.findViewById(R.id.yy_nodata_message);
        int hei = MyUtils.getScreenHeigth(context)
                - MyUtils.dip2px(context, 55);
        layoutParamsVisible = new LinearLayout.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT, hei);
        // layoutParamsVisible.gravity = Gravity.CENTER;
        //
        mLinearLayout.setLayoutParams(layoutParamsVisible);

    }

    public YYNotdataView(Context context, int LeftHeght) {
        super(context);
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
        View rootview = mInflater.inflate(R.layout.yy_nodata, this);
        mLinearLayout = (LinearLayout) rootview
                .findViewById(R.id.yy_nodata_layout);
        textView = (TextView) rootview.findViewById(R.id.yy_nodata_message);
        int hei = MyUtils.getScreenHeigth(context)
                - MyUtils.dip2px(context, LeftHeght);
        layoutParamsVisible = new LinearLayout.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT, hei);
        // layoutParamsVisible.gravity = Gravity.CENTER;
        //
        mLinearLayout.setLayoutParams(layoutParamsVisible);

    }

    public void setMessage(String message) {
        textView.setText(message);
    }

    public void setVisible(boolean visible) {

        if (visible) {
            mLinearLayout.setVisibility(View.VISIBLE);
            // mLinearLayout.setLayoutParams(layoutParamsVisible);
        } else {
            mLinearLayout.setVisibility(View.GONE);
            // mLinearLayout.setLayoutParams(layoutParamsGone);
        }

    }

    public void setVisible(boolean visible, String msg) {
        textView.setText(msg);
        if (visible) {
            mLinearLayout.setVisibility(View.VISIBLE);
            // mLinearLayout.setLayoutParams(layoutParamsVisible);
        } else {
            mLinearLayout.setVisibility(View.GONE);
            // mLinearLayout.setLayoutParams(layoutParamsGone);
        }
    }
}
