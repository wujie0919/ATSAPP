package com.dzrcx.jiaan.Order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

public class PayActivity extends YYBaseActivity {
    private PayFrg payFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payFrg = new PayFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, payFrg);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            Intent toIntent = new Intent(this, MainActivity2_1.class);
            toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(toIntent);
            overridePendingTransition(R.anim.activity_up,
                    R.anim.activity_push_no_anim);

        }
        return super.onKeyDown(keyCode, event);
    }

}
