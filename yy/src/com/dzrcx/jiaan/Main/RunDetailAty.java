package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

public class RunDetailAty extends YYBaseActivity {
    private RunDetailFrg
            runDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runDetailFrg = new RunDetailFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, runDetailFrg);
        transaction.commit();
    }
}
