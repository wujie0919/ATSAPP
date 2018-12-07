package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 违章
 * Created by zhangyu on 16-3-29.
 */
public class BreakRulesDetailAty extends YYBaseActivity {
    private BreakRulesDetailFrg breakRulesDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        breakRulesDetailFrg = new BreakRulesDetailFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, breakRulesDetailFrg);
        transaction.commit();

    }
}
