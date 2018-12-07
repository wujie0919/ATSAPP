package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dzrcx.jiaan.R;

/**
 * Created by zhangyu on 16-3-16.
 */
public class NoNetDialog extends Dialog implements View.OnClickListener {

    private TextView cancel, open;


    public NoNetDialog(Context context) {
        super(context);
    }


    public NoNetDialog(Context context, int theme) {
        super(context, theme);
    }

    public NoNetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nonetdialog);
        cancel = (TextView) findViewById(R.id.tv_cancel_txt);
        open = (TextView) findViewById(R.id.tv_open_txt);
        cancel.setOnClickListener(this);
        open.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_cancel_txt:
                NoNetDialog.this.dismiss();
                break;
            case R.id.tv_open_txt:
                // 跳转到系统的网络设置界面
                Intent intent = null;
                // 先判断当前系统版本
                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                getContext().startActivity(intent);
                NoNetDialog.this.dismiss();
                break;
        }
    }
}
