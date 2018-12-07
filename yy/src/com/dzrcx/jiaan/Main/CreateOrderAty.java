package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 *
 * 选择站点还车的车   填写在站点信息界面
 * Created by zhangyu on 16-5-10.
 */
public class CreateOrderAty extends YYBaseActivity {

    private CreateOrderFrag createOrderFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createOrderFrag = new CreateOrderFrag();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, createOrderFrag);
        transaction.commit();
    }
}
