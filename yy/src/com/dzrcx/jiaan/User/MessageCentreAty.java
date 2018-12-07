package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-10-13.
 */
public class MessageCentreAty extends YYBaseActivity {


    private MessageCentreFrg messageCentreFrg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        messageCentreFrg = new MessageCentreFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, messageCentreFrg);
        transaction.commit();

    }
}
