package com.dzrcx.jiaan.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class InvoiceAty extends YYBaseActivity {
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager vp_view_pager;
    private List<Fragment> fragmentList;

    private InvoiceFrg invoiceFrg;//按金额开票
    private AddValueInvoice addValueInvoice;//增值开票

    private ImageView iv_left_raw;
    private TextView tv_can_invoicing_money;

    private LinearLayout ll_ivoice, ll_add_value_ivoice;
    private TextView tv_ivoice, tv_add_value_ivoice,tv_invoice_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_apger_fragment_layout);
        tv_can_invoicing_money = (TextView) findViewById(R.id.tv_can_invoicing_money);
        tv_invoice_history = (TextView) findViewById(R.id.tv_invoice_history);
        tv_invoice_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到开票记录
                startActivity(new Intent(InvoiceAty.this,InvoiceHistoryAty.class));
                overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
            }
        });
        tv_can_invoicing_money.setText(getIntent().getExtras().getString("amount"));
        iv_left_raw = (ImageView) findViewById(R.id.iv_left_raw);
        iv_left_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vp_view_pager = (ViewPager) findViewById(R.id.vp_view_pager);

        invoiceFrg = new InvoiceFrg();
        invoiceFrg.setIntent(getIntent());

        addValueInvoice = new AddValueInvoice();
        addValueInvoice.setIntent(getIntent());

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(invoiceFrg);//普通发票
        fragmentList.add(addValueInvoice);//增值发票

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        vp_view_pager.setAdapter(fragmentPagerAdapter);

        vp_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    ll_ivoice.setBackgroundResource(R.drawable.invoice_green_background);
                    tv_ivoice.setTextColor(getResources().getColor(R.color.white));
                    ll_add_value_ivoice.setBackgroundColor(getResources().getColor(R.color.c_wode_btn_bg));
                    tv_add_value_ivoice.setTextColor(getResources().getColor(R.color.text_3d3f42));
                } else if (i == 1) {
                    ll_add_value_ivoice.setBackgroundResource(R.drawable.invoice_green_background);
                    tv_add_value_ivoice.setTextColor(getResources().getColor(R.color.white));
                    ll_ivoice.setBackgroundColor(getResources().getColor(R.color.c_wode_btn_bg));
                    tv_ivoice.setTextColor(getResources().getColor(R.color.text_3d3f42));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        invoiceFrg = new InvoiceFrg();
//        invoiceFrg.setIntent(getIntent());
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        transaction.replace(R.id.base_contextlayout, invoiceFrg);
//        transaction.commit();

        ll_ivoice = (LinearLayout) findViewById(R.id.ll_ivoice);
        tv_ivoice = (TextView) findViewById(R.id.tv_ivoice);
        ll_add_value_ivoice = (LinearLayout) findViewById(R.id.ll_add_value_ivoice);
        tv_add_value_ivoice = (TextView) findViewById(R.id.tv_add_value_ivoice);

        ll_ivoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_ivoice.setBackgroundResource(R.drawable.invoice_green_background);
                tv_ivoice.setTextColor(getResources().getColor(R.color.white));
                ll_add_value_ivoice.setBackgroundColor(getResources().getColor(R.color.white));
                tv_add_value_ivoice.setTextColor(getResources().getColor(R.color.text_3d3f42));
                vp_view_pager.setCurrentItem(0);
            }
        });
        ll_add_value_ivoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_add_value_ivoice.setBackgroundResource(R.drawable.invoice_green_background);
                tv_add_value_ivoice.setTextColor(getResources().getColor(R.color.white));
                ll_ivoice.setBackgroundColor(getResources().getColor(R.color.white));
                tv_ivoice.setTextColor(getResources().getColor(R.color.text_3d3f42));
                vp_view_pager.setCurrentItem(1);
            }
        });
    }
}
