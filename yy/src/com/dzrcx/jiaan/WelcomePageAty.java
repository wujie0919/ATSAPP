package com.dzrcx.jiaan;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.base.YYBaseActivity;

public class WelcomePageAty extends YYBaseActivity {
    private WelcomePageFrg
            WelcomePageFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        WelcomePageFrg = new WelcomePageFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, WelcomePageFrg);
        transaction.commit();
    }

}