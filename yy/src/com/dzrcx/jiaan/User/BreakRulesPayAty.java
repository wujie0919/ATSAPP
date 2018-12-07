package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 违章支付
 */
public class BreakRulesPayAty extends YYBaseActivity {
    private BreakRulesPayFrg breakRulesPayFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        breakRulesPayFrg = new BreakRulesPayFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, breakRulesPayFrg);
        transaction.commit();
    }

}
