package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 我的订单界面
 */
public class MyRunsListAty extends YYBaseActivity {
    private MyRunsListFrg
            OrderListFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OrderListFrg = new MyRunsListFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, OrderListFrg);
        transaction.commit();
    }
}
