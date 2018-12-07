package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 充值记录
 * Created by zhangyu on 16-1-6.
 */
public class RechargeRecordAty extends YYBaseActivity {

    private RechargeRecordFrg recordFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        recordFrg = new RechargeRecordFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, recordFrg);
        transaction.commit();
    }
}
