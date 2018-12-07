package com.dzrcx.jiaan.SearchCar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * webview 页面 带参数 url，tv_title
 *
 * @author Administrator
 */
public class WebAty extends YYBaseActivity {
    private WebFrg webFrg;
    private boolean isStartPate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webFrg = new WebFrg();
        setContentView(R.layout.yy_base_act);
        isStartPate = getIntent().getBooleanExtra("isStartPate", false);
        if (isStartPate) {
            setDownOut(false);
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.base_contextlayout, webFrg);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("ActionBar", "OnKey事件");
        if (webFrg != null) {
            webFrg.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
