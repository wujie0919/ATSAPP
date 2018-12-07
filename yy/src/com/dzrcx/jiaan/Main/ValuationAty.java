package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 计价说明
 */
public class ValuationAty extends YYBaseActivity {
    private ValuationFrg
            valuationFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valuationFrg = new ValuationFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, valuationFrg);
        transaction.commit();
    }
}
