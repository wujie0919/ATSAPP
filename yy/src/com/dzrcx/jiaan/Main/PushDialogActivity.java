package com.dzrcx.jiaan.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.utils.TimeUtil;

/**
 * Created by chenh on 2017/1/10.
 */

public class PushDialogActivity extends YYBaseActivity {
    private TextView tv_cancle;//准时还车
    private TextView tv_confirm;//延长时间
    private TextView tv_remaind_time_message;//剩余时间提示
    private String strtime = "30";
    String nickname = YYConstans.getUserInfoBean().getReturnContent().getUser().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_dialog_activity_layout);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_remaind_time_message = (TextView) findViewById(R.id.tv_remaind_time_message);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PushDialogActivity.this, DayShareActivity.class);
                intent.putExtra("PushDialogActivity", true);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.getIntExtra("getReletRemainingSeconds", 0) > 0) {
            strtime = TimeUtil.formatTimeMinute(intent.getIntExtra("getReletRemainingSeconds", 0) * 1000);
        }
        //String nickname = YYConstans.getUserInfoBean().getReturnContent().getUser().getName();
        tv_remaind_time_message.setText(nickname + ", 你的用车时间还有" + strtime + "分钟即将结束" +
                ", 是否需要延长用车时间, 若未按还车时间及时还车系统将按分时用车的计费方式计算费用~");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_remaind_time_message.setText(nickname + ", 你的用车时间还有" + strtime + "分钟即将结束" +
                    ", 是否需要延长用车时间, 若未按还车时间及时还车系统将按分时用车的计费方式计算费用~");
            int intTime = Integer.parseInt(strtime);
            if (intTime <= 0) {
                finish();
            }
            strtime = (intTime - 1) + "";
        }
    };

    protected void onNewIntent(Intent intent1) {
        super.onNewIntent(intent1);
        setIntent(intent1);
        //here we can use getIntent() to get the extra data.
        Intent intent = getIntent();
        if (intent != null && intent.getIntExtra("getReletRemainingSeconds", 0) > 0) {
            strtime = TimeUtil.formatTimeMinute(intent.getIntExtra("getReletRemainingSeconds", 0) * 1000);
        }
    }
}
