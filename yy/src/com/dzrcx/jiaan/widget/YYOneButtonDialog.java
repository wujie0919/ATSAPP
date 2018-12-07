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
 * Created by zhangyu on 16-8-25.
 */
public class YYOneButtonDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView tv_button, tv_message, tv_title;
    private String buttonStr, messageStr, titleStri;
    private DialogClick dialogClick;


    /**
     * @param context
     * @param title
     * @param message
     * @param buttonStr
     */
    public YYOneButtonDialog(Context context, String title, String message, String buttonStr

    ) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.titleStri = title;
        this.messageStr = message;
        this.buttonStr = buttonStr;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_recommend_more);
        setCanceledOnTouchOutside(false);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_content);
        tv_button = (TextView) findViewById(R.id.tv_do_txt);
        tv_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (dialogClick != null) {
            switch (v.getId()) {
                case R.id.tv_do_txt:
                    dialogClick.buttonClick(v, this);
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
        void buttonClick(View v, Dialog dialog);
    }


    public void setOnDialogClick(
            DialogClick onDialogClick) {
        dialogClick = onDialogClick;
    }

    @Override
    public void show() {
        super.show();
        if (titleStri != null) {
            tv_title.setText(titleStri);
        }
        if (buttonStr != null) {
            tv_button.setText(buttonStr);
        }
        tv_message.setText(messageStr);
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(context) / 10 * 8;
        onWindowAttributesChanged(lp);
    }
}
