package com.dzrcx.jiaan.SearchCar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.tools.MyUtils;

public class AuthorizeAty extends YYBaseActivity {
    private AuthorizeFrg
            AuthorizeFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthorizeFrg = new AuthorizeFrg();
        setContentView(R.layout.yy_base_act);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, AuthorizeFrg);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            MyUtils.showToast(this, "支付成功！");
//            dialogShow();
            AuthorizeFrg.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    finish();
                }
            }, 1000);

        } else if (str.equalsIgnoreCase("fail")) {
            MyUtils.showToast(this, "支付失败！");
        } else if (str.equalsIgnoreCase("cancel")) {
            MyUtils.showToast(this, "您取消了支付");
        }

    }
}
