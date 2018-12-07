package com.dzrcx.jiaan.User;

import java.util.HashMap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.Bean.UpdataBean;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.ApkDownLoad;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.YYRunner;

public class AboutUsFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface {

    private View aboutView;
    private ImageView iv_left_raw;
    private TextView tv_content, tv_updata_txt, tv_cancel_txt;
    private TextView barTitle;
    private TextView version, chenVersion;
    private Dialog updataDialog;
    private View updataView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (aboutView == null) {
            aboutView = LayoutInflater.from(mContext).inflate(
                    R.layout.frg_aboutus, null);
            updataView = inflater.inflate(R.layout.dlg_updata, null);
            tv_content = (TextView) updataView.findViewById(R.id.tv_content);
            tv_updata_txt = (TextView) updataView
                    .findViewById(R.id.tv_updata_txt);
            tv_cancel_txt = (TextView) updataView
                    .findViewById(R.id.tv_cancel_txt);

            iv_left_raw = (ImageView) aboutView.findViewById(R.id.iv_left_raw);
            iv_left_raw.setVisibility(View.VISIBLE);
            barTitle = (TextView) aboutView.findViewById(R.id.tv_title);
            barTitle.setText("关于星辰");
            version = (TextView) aboutView.findViewById(R.id.about_version);
            chenVersion = (TextView) aboutView
                    .findViewById(R.id.about_check_version);

            iv_left_raw.setOnClickListener(this);
            chenVersion.setOnClickListener(this);
            MyUtils.setViewsOnClick(this, tv_content, tv_updata_txt,
                    tv_cancel_txt);
            try {
                PackageManager manager = mContext.getPackageManager();
                PackageInfo info = manager.getPackageInfo(
                        mContext.getPackageName(), 0);
                String versionStr = info.versionName;
                version.setText("当前版本： V" + versionStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return aboutView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.about_check_version:
                dialogShow();
                YYRunner.getData(0, YYRunner.Method_POST, YYUrl.GETUPDATA,
                        new HashMap<String, String>(), this);

                break;
            case R.id.tv_updata_txt:
                UpdataBean bean = (UpdataBean) v.getTag();
                new ApkDownLoad(mContext, bean.getAndroidUrl(), "星辰出行", "版本升级")
                        .execute();
            case R.id.tv_cancel_txt:
                updataDialog.dismiss();
                break;
            default:
                break;
        }

    }

    private void showUpdataDialog(final UpdataBean bean) {
        if (updataDialog == null) {
            updataDialog = new Dialog(this.getActivity(), R.style.MyDialog);
            updataDialog.setContentView(updataView);
            updataDialog.setCanceledOnTouchOutside(false);
        }
        if (bean.getUpdateForce() == 1) {
            tv_cancel_txt.setVisibility(View.GONE);// 强制
        } else {
            tv_cancel_txt.setVisibility(View.VISIBLE);
        }
        tv_content.setText(bean.getDescription());
        tv_updata_txt.setTag(bean);
        Window dlgWindow = updataDialog.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(this.getActivity()) / 5 * 4;
        dlgWindow.setAttributes(lp);
        updataDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (bean.getUpdateForce() == 1) {
                        return true;// 强制
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
        updataDialog.show();
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        UpdataBean bean = (UpdataBean) GsonTransformUtil.fromJson(json,
                UpdataBean.class);
        if (bean != null && bean.getVersion() > MyUtils.getVersionCode(this.getActivity())) {
            showUpdataDialog(bean);
        } else {
            MyUtils.showToast(mContext, "当前已是最新版本");
        }
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

}
