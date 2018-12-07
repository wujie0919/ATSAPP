package com.dzrcx.jiaan;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.dzrcx.jiaan.base.YYBaseActivity;

public class StartPageAty extends YYBaseActivity {
    private StartPageFrg startPageFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        setDownOut(false);
        startPageFrg = new StartPageFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, startPageFrg);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            YYApplication.getApplication().exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}