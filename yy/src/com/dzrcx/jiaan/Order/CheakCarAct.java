package com.dzrcx.jiaan.Order;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * Created by zhangyu on 16-9-27.
 * <p/>
 * 车辆检测，上传照片
 */
public class CheakCarAct extends YYBaseActivity {

    private CheakCarFrag cheakCarFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        cheakCarFrag = new CheakCarFrag();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, cheakCarFrag);
        transaction.commit();

    }
}
