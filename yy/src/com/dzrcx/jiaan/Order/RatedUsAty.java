package com.dzrcx.jiaan.Order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.dzrcx.jiaan.Bean.OrderDetailVo;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.Main.RatedUsFragDayShare;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-5-10.
 * 评价我们
 */
public class RatedUsAty extends YYBaseActivity {

    private RatedUsFrag ratedUsFrag;
    private RatedUsFragDayShare ratedUsFragDayShare;

    private boolean isDayShare = false;
    private OrderDetailVo orderDetailVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        isDayShare = getIntent().getBooleanExtra("isDayShare", false);
        orderDetailVo = (OrderDetailVo) getIntent().getSerializableExtra("OrderDetailVo");

        if (isDayShare) {
            ratedUsFragDayShare = new RatedUsFragDayShare();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.base_contextlayout, ratedUsFragDayShare);
            transaction.commit();
            if (orderDetailVo != null && "1".equals(orderDetailVo.getForePhoto())) {
                Bundle bundlecar = new Bundle();
                bundlecar.putString("orderID", orderDetailVo.getOrderId());
                bundlecar.putString("photoScene", "1");
                Intent intent = new Intent(this, CheakCarAct.class);
                intent.putExtras(bundlecar);
                startActivity(intent);
            }
        } else {

            ratedUsFrag = new RatedUsFrag();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.base_contextlayout, ratedUsFrag);
            transaction.commit();
        }
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
