package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;

/**
 * 还车选项dialog
 */
public class CarGaveDialog extends Dialog implements View.OnClickListener {
    private RelativeLayout rl_closedoor, rl_parklegal, rl_takethings, rl_sureclosecar;
    private TextView tv_closedoor, tv_parklegal, tv_takethings, tv_sureclosecar;
    private View v_closedoor, v_parklegal, v_takethings, v_sureclosecar;

    private Context mContext;
    private CheckCarOver checkCarOver;

    public CarGaveDialog(Context context) {
        super(context, R.style.ActionSheet);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gavecar);
        initView();
    }

    public void initView() {
        rl_parklegal = (RelativeLayout) findViewById(R.id.rl_parklegal);
        rl_takethings = (RelativeLayout) findViewById(R.id.rl_takethings);
        rl_closedoor = (RelativeLayout) findViewById(R.id.rl_closedoor);
        rl_sureclosecar = (RelativeLayout) findViewById(R.id.rl_sureclosecar);

        tv_parklegal = (TextView) findViewById(R.id.tv_parklegal);
        tv_takethings = (TextView) findViewById(R.id.tv_takethings);
        tv_closedoor = (TextView) findViewById(R.id.tv_closedoor);
        tv_sureclosecar = (TextView) findViewById(R.id.tv_sureclosecar);

        v_closedoor = findViewById(R.id.v_closedoor);
        v_parklegal = findViewById(R.id.v_parklegal);
        v_takethings = findViewById(R.id.v_takethings);
        v_sureclosecar = findViewById(R.id.v_sureclosecar);

        rl_parklegal.setTag(R.id.tag_first, tv_parklegal);
        rl_takethings.setTag(R.id.tag_first, tv_takethings);
        rl_closedoor.setTag(R.id.tag_first, tv_closedoor);
        rl_sureclosecar.setTag(R.id.tag_first, tv_sureclosecar);

        rl_parklegal.setTag(R.id.tag_second, v_parklegal);
        rl_takethings.setTag(R.id.tag_second, v_takethings);
        rl_closedoor.setTag(R.id.tag_second, v_closedoor);
        rl_sureclosecar.setTag(R.id.tag_second, v_sureclosecar);
        MyUtils.setViewsOnClick(this, rl_parklegal, rl_sureclosecar, rl_takethings, rl_closedoor);
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
        if (v.isSelected()) {
            ((View) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
            if (checkCarOver != null && getStatus() == 0) {
                checkCarOver.onCheckOver();
                dismiss();
            }
        }
//        else {
//            ((View) v.getTag(R.id.tag_second)).setVisibility(View.GONE);
//        }
    }

    @Override
    public void show() {
        super.show();
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 *8;
        onWindowAttributesChanged(lp);
    }

    public int getStatus() {

        if (!rl_closedoor.isSelected()) {
            //SpeechUtil.getInstance().peechMessage(mContext, "请在关闭车窗、停稳车辆后拔出钥匙。");
            return 1;
        }
        if (!rl_parklegal.isSelected()) {
            // SpeechUtil.getInstance().peechMessage(mContext, "带好自己的私人物品");
            return 2;
        }
        if (!rl_takethings.isSelected()) {
            // SpeechUtil.getInstance().peechMessage(mContext, "关闭车门，并确认车门已锁");
            return 3;
        }
        if (!rl_sureclosecar.isSelected()) {
            // SpeechUtil.getInstance().peechMessage(mContext, "关闭车门，并确认车门已锁");
            return 4;
        }
        return 0;
    }

    public void setCheckCarOver(CheckCarOver CheckCarOver) {
        this.checkCarOver = CheckCarOver;
    }

    public interface CheckCarOver {
        void onCheckOver();
    }

}