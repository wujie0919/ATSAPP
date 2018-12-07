package com.dzrcx.jiaan.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.widget.NoNetDialog;
import com.dzrcx.jiaan.widget.YYProgresssDialog;
import com.umeng.analytics.MobclickAgent;

public class YYBaseFragment extends Fragment {
    public Activity mContext;
    public String activity = "";//获取子类的名dd称
    public YYProgresssDialog progresssDialog;

    private Dialog callDlg, messageDlg, chooseDlg, nonetDlg;
    public Handler baseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
//        mContext = activity;
        mContext = getActivity();
        initDialog();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        // initDialog();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initDialog() {
        progresssDialog = new YYProgresssDialog(getActivity(),
                R.style.customProgressDialog);
        // progresssDialog.setContentView(R.layout.customprogressdialog);
        progresssDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nonetDlg != null && nonetDlg.isShowing()) {
            nonetDlg.cancel();
        }
        if (chooseDlg != null && chooseDlg.isShowing()) {
            chooseDlg.cancel();
        }
    }

    public void dialogShow() {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dialogShow(String message) {
        try {
            if (progresssDialog != null && !progresssDialog.isShowing()) {
                progresssDialog.show(message);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.toString());
        }
    }

    public void dismmisDialog() {
        if (progresssDialog != null && progresssDialog.isShowing()) {
            progresssDialog.dismiss();
        }
    }

    public void startActivityForResult(Class className, Bundle bundle,
                                       int requestCode) {
        Intent toIntent = new Intent(mContext, className);
        if (bundle != null) {
            toIntent.putExtras(bundle);
        }
        toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(toIntent, requestCode);
        mContext.overridePendingTransition(R.anim.activity_up,
                R.anim.activity_push_no_anim);
    }

    /**
     * 页面跳转
     *
     * @param className
     * @param bundle
     */
    public void startActivity(Class className, Bundle bundle) {
        Intent toIntent = new Intent(mContext, className);
        if (bundle != null) {
            toIntent.putExtras(bundle);
        }
        toIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(toIntent);
        mContext.overridePendingTransition(R.anim.activity_up,
                R.anim.activity_push_no_anim);
    }

    /**
     * 打开拨打客服电话弹窗
     */
    public void showCallDialog() {
        if (callDlg == null) {
            callDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_call, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    callDlg.dismiss();
                }
            });
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            tv_number.setText("拨打客服电话："
                    + YYConstans.getSysConfig().getReturnContent().getServicePhone());
            tv_call_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    try {
                        MobclickAgent.onEvent(mContext, "click_call");
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:"
                                        + YYConstans.getSysConfig().getReturnContent().getServicePhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mContext.overridePendingTransition(
                                R.anim.activity_up, R.anim.activity_push_no_anim);
                    } catch (Exception e) {
                        MyUtils.showToast(mContext, "请检查是否开启电话权限");
                    } finally {
                        callDlg.dismiss();
                    }
                }
            });
            callDlg.setCanceledOnTouchOutside(false);
            callDlg.setContentView(mDlgCallView);
        }
        callDlg.show();
        Window dlgWindow = callDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

    /**
     * 知道了窗口
     */
    public void showMessageDialog(String messge) {
        if (messageDlg == null) {
            messageDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_message, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    messageDlg.dismiss();
                    if ("Identification".equals(activity)) {
                        getActivity().finish();
                    }
                }
            });
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setContentView(mDlgCallView);
        }
        messageDlg.show();

        Window dlgWindow = messageDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

    /**
     * 确认窗口
     */
    public void showMessageSureDialog(String messge, String buttonStr, final Class className) {
        if (messageDlg == null) {
            messageDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_sure, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_do_txt);
            if (!TextUtils.isEmpty(buttonStr)) {
                tv_cancel_txt.setText(buttonStr);
            }

            tv_cancel_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    messageDlg.dismiss();
                    if (className != null) {
                        startActivity(className, null);
                    }
                }
            });
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_message);
            tv_number.setText(messge);
            messageDlg.setCanceledOnTouchOutside(false);
            messageDlg.setContentView(mDlgCallView);
        }
        messageDlg.show();

        Window dlgWindow = messageDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);

    }

    /**
     * 打开拨打客服电话弹窗
     */
    public void showChooseDialog(String messge, View.OnClickListener leftOnClickListener) {
        if (chooseDlg == null) {
            chooseDlg = new Dialog(mContext, R.style.MyDialog);
            View mDlgCallView = LayoutInflater.from(mContext).inflate(
                    R.layout.dlg_choose, null);
            TextView tv_cancel_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_cancel_txt);
            tv_cancel_txt.setText("确认");

            if (leftOnClickListener == null) {
                leftOnClickListener = new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        chooseDlg.dismiss();
                    }
                };
            }
            tv_cancel_txt.setOnClickListener(leftOnClickListener);
            final TextView tv_call_txt = (TextView) mDlgCallView
                    .findViewById(R.id.tv_call_txt);
            final TextView tv_number = (TextView) mDlgCallView
                    .findViewById(R.id.tv_number);
            if (!TextUtils.isEmpty(messge)) {
                tv_number.setText(messge);
            }
            tv_call_txt.setText("联系客服");
            tv_call_txt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stubt
                    try {
                        MobclickAgent.onEvent(mContext, "click_call");
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:"
                                        + YYConstans.getSysConfig().getReturnContent().getServicePhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mContext.overridePendingTransition(
                                R.anim.activity_up, R.anim.activity_push_no_anim);
                    } catch (Exception e) {
                        MyUtils.showToast(mContext, "请检查是否开启电话权限");
                    } finally {
                        chooseDlg.dismiss();
                    }
                }
            });
            chooseDlg.setCanceledOnTouchOutside(false);
            chooseDlg.setContentView(mDlgCallView);
        }
        chooseDlg.show();

        Window dlgWindow = chooseDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
    }


    /**
     * 打开拨打客服电话弹窗
     */
    public void showChooseDialog(String messge) {
        if (chooseDlg == null) {
            chooseDlg = new Dialog(mContext, R.style.MyDialog);
        }
        View mDlgCallView = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_choose, null);
        TextView tv_cancel_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_cancel_txt);
        tv_cancel_txt.setText("确认");
        tv_cancel_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                chooseDlg.dismiss();
            }
        });
        final TextView tv_call_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_call_txt);
        final TextView tv_number = (TextView) mDlgCallView
                .findViewById(R.id.tv_number);
        if (!TextUtils.isEmpty(messge)) {
            tv_number.setText(messge);
        }
        tv_call_txt.setText("联系客服");
        tv_call_txt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stubt
                try {
                    MobclickAgent.onEvent(mContext, "click_call");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:"
                                    + YYConstans.getSysConfig().getReturnContent().getServicePhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    mContext.overridePendingTransition(
                            R.anim.activity_up, R.anim.activity_push_no_anim);
                } catch (Exception e) {
                    MyUtils.showToast(mContext, "请检查是否开启电话权限");
                } finally {
                    chooseDlg.dismiss();
                }
            }
        });
        chooseDlg.setCanceledOnTouchOutside(false);
        chooseDlg.setContentView(mDlgCallView);

        chooseDlg.show();

        Window dlgWindow = chooseDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 7;
        dlgWindow.setAttributes(lp);
    }


    public void showNoNetDlg() {
        if (nonetDlg == null) {
            nonetDlg = new NoNetDialog(mContext, R.style.MyDialog);
            nonetDlg.show();
        } else if (nonetDlg != null && !nonetDlg.isShowing()) {
            nonetDlg.show();
        }
    }

}
