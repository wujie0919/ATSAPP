package com.dzrcx.jiaan.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.utils.TimeUtil;

/**
 * Created by chenh on 2017/1/18.
 */

public class InvoiceCompileteActivity extends YYBaseActivity {
    private TextView tv_time, tv_state;
    private ImageView iv_left_raw;
    private int state;
    private long createTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_compilete);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            state = intent.getIntExtra("state", 0);
            createTimes = intent.getLongExtra("createTimes", 0);
        }

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_state = (TextView) findViewById(R.id.tv_state);
        iv_left_raw = (ImageView) findViewById(R.id.iv_left_raw);
        tv_time.setText(TimeUtil.getData(0));
        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (createTimes > 0) {
            tv_time.setText("更新时间: "+TimeUtil.getSimpleData(createTimes));
            String stateStr = "";
            switch (state) {
                case 0:
                    stateStr = "提交成功, 等待工作人员受理";//"待确认"
                    break;
                case 1:
                    stateStr = "发票已受理, 正在开票中";//"待开票"
                    break;
                case 2:
                    stateStr = "邮票已经寄出, 请注意收件";//"已开票"
                    break;
                case 3:
                    stateStr = "邮票已经寄出, 请注意收件";//"已邮寄"
                    break;
                case 4:
                    stateStr = "发票信息可能错误, 被驳回";//"已驳回"
                    break;
            }
            tv_state.setText(stateStr);
        }else{
            tv_time.setText("更新时间: "+TimeUtil.getSimpleData(System.currentTimeMillis()));
        }
    }
}
