package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;


/**
 * 企业账户列表页
 * Created by zhangyu on 15-12-16.
 */
public class CompanyAccountAty extends YYBaseActivity {


    private CompanyAccountFrg accountFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.yy_base_act);
        accountFrg = new CompanyAccountFrg();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, accountFrg);
        transaction.commit();

    }


}
