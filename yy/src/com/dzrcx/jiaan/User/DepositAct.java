package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-7-29.
 * 提现结果
 */
public class DepositAct extends YYBaseActivity {


    private DepositFrag depositFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        depositFrag = new DepositFrag();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, depositFrag);
        transaction.commit();
    }
}
