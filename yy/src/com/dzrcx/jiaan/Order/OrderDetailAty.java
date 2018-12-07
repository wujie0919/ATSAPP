package com.dzrcx.jiaan.Order;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

public class OrderDetailAty extends YYBaseActivity {
    private OrderDetailFrg orderDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailFrg = new OrderDetailFrg();
        orderDetailFrg.setIntent(getIntent());
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, orderDetailFrg);
        transaction.commit();
    }
}
