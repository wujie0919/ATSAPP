package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 开票历史
 * Created by zhangyu on 16-1-5.
 */
public class InvoiceHistoryAty extends YYBaseActivity {

    private InvoiceHistoryFrg historyFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        historyFrg = new InvoiceHistoryFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, historyFrg);
        transaction.commit();
    }
}
