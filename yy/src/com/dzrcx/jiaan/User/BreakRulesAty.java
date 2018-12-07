package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.FrameLayout;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;

/**
 * 违章事故
 */
public class BreakRulesAty extends YYBaseActivity {

    private BreakRulesFrg breakRulesFrg;
    private IllegalFrg illegalFrg;
    private FrameLayout base_contextlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yy_base_act);
        base_contextlayout = (FrameLayout) findViewById(R.id.base_contextlayout);
        changeToFragment(1);
    }


    /**
     * 改变到某一个fragment
     * 在fragment界面需要添加setMenuVisibility方法
     * 1违章  0事故
     */
    public void changeToFragment(int index) {
        FrameLayout frameLayout = base_contextlayout;
        Fragment fragment = (Fragment) fragments.instantiateItem(frameLayout,
                index);
        fragments.setPrimaryItem(frameLayout, 0, fragment);
        fragments.finishUpdate(frameLayout);
    }

    private FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(
            getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (breakRulesFrg == null) {
                        breakRulesFrg = new BreakRulesFrg();
                    }
                    return breakRulesFrg;
                case 1:
                    if (illegalFrg == null) {
                        illegalFrg = new IllegalFrg();
                    }
                    return illegalFrg;
            }
            return null;
        }
    };
}
