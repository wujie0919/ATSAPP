package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-10-11.
 */
public class TransferTwoAty extends YYBaseActivity {

    private TransferTwoFrg transferTwoFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.yy_base_act);

        transferTwoFrg = new TransferTwoFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, transferTwoFrg);
        transaction.commit();
    }
}
