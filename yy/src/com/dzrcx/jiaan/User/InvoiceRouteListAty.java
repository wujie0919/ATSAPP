package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

public class InvoiceRouteListAty extends YYBaseActivity {
    private InvoiceRouteListFrg invoiceRouteListFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        invoiceRouteListFrg = new InvoiceRouteListFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, invoiceRouteListFrg);
        transaction.commit();
    }
}
