package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;

/**
 * 提示信息dialog
 */
public class WarnDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tv_left_txt, tv_right_txt, tv_message;
    private String leftText, rightText, message;
    private DialogClick dialogClick;

    /**
     * 设置文字与点击事件
     *
     * @param context
     * @param leftText
     * @param rightText
     */
    public WarnDialog(Context context, String leftText, String rightText, String message, DialogClick dialogClick) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.leftText = leftText;
        this.rightText = rightText;
        this.message = message;
        this.dialogClick = dialogClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_paymoney);
        setCanceledOnTouchOutside(false);
        tv_left_txt = (TextView) findViewById(R.id.tv_left_txt);
        tv_right_txt = (TextView) findViewById(R.id.tv_right_txt);
        tv_message = (TextView) findViewById(R.id.tv_message);
        if (leftText != null && leftText != null) {
            tv_left_txt.setText(leftText);
            tv_right_txt.setText(rightText);
            tv_message.setText(message);
        }
        MyUtils.setViewsOnClick(this, tv_left_txt, tv_right_txt);
    }

    @Override
    public void onClick(View v) {
        if (dialogClick != null) {
            switch (v.getId()) {
                case R.id.tv_left_txt:
                    dialogClick.leftClick();
                    dismiss();
                    break;
                case R.id.tv_right_txt:
                    dialogClick.Rightclick();
                    dismiss();
                    break;
            }
        } else {
            dismiss();
        }
    }

    /**
     * 如果处理外部点击事件则传入相应的接口
     */
    public interface DialogClick {
        void leftClick();

        void Rightclick();
    }
}