package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-10-18.
 */
public class ChooseSeciveTypeAty extends YYBaseActivity {

    private ChooseSecrviceTypeFrg chooseSecrviceTypeFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        chooseSecrviceTypeFrg = new ChooseSecrviceTypeFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, chooseSecrviceTypeFrg);
        transaction.commit();
    }
}
