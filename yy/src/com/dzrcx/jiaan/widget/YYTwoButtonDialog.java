package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;

/**
 * 提示信息dialog
 */
public class YYTwoButtonDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView tv_left, tv_right, tv_message, tv_title;
    private String leftText, rightText, message, title;
    private DialogClick dialogClick;

    /**
     * 设置文字与点击事件
     *
     * @param context
     * @param leftText
     * @param rightText
     */
    public YYTwoButtonDialog(Context context, String leftText, String rightText,
                             String message,
                             String title
    ) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.leftText = leftText;
        this.rightText = rightText;
        this.message = message;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_two_button);
        setCanceledOnTouchOutside(false);
        tv_left = (TextView) findViewById(R.id.tv_left_txt);
        tv_right = (TextView) findViewById(R.id.tv_right_txt);
        tv_message = (TextView) findViewById(R.id.tv_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        MyUtils.setViewsOnClick(this, tv_left, tv_right);
    }

    @Override
    public void onClick(View v) {
        if (dialogClick != null) {
            switch (v.getId()) {
                case R.id.tv_left_txt:
                    dismiss();
                    dialogClick.leftClick(v, this);
                    break;
                case R.id.tv_right_txt:
                    dismiss();
                    dialogClick.Rightclick(v, this);
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
        void leftClick(View v, Dialog dialog);

        void Rightclick(View v, Dialog dialog);
    }

    public void setOnDialogClick(
            DialogClick onDialogClick) {
        dialogClick = onDialogClick;
    }

    @Override
    public void show() {
        super.show();
        if (leftText != null && leftText != null) {
            tv_left.setText(leftText);
            tv_right.setText(rightText);
            tv_message.setText(message);
            tv_title.setText(title);
        }
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(context) / 10 * 8;
        onWindowAttributesChanged(lp);
    }
}