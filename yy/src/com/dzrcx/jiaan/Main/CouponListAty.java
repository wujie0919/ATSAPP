package com.dzrcx.jiaan.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 优惠券列表
 * Created by zhangyu on 15-12-31.
 */
public class CouponListAty extends YYBaseActivity {
    private CouponListFrg couponListFrg;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        couponListFrg = new CouponListFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, couponListFrg);
        transaction.commit();
    }
}
