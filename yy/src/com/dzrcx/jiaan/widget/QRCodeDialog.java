package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.dzrcx.jiaan.R;

/**
 * 二维码邀请dialog
 */
public class QRCodeDialog extends Dialog {
    private ImageView iv_qr_code;

    public QRCodeDialog(Context context, Bitmap src) {
        super(context, R.style.ActionSheet);
        setContentView(R.layout.dlg_qr_code);
        setCanceledOnTouchOutside(true);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        if (src != null) {
            iv_qr_code.setImageBitmap(src);
        } else {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
    }
}
