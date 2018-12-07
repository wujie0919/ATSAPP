package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 用户帮助中心
 */
public class HelpCenterAty extends YYBaseActivity {
    private HelpCenterFrg
            helpCenterFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helpCenterFrg = new HelpCenterFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, helpCenterFrg);
        transaction.commit();
    }
}
