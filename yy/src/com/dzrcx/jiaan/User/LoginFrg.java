package com.dzrcx.jiaan.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Constans.YYBroadCastName;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.AES;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.YYRunner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginFrg extends YYBaseFragment implements OnClickListener, RequestInterface {
    private TextView barTitle;
    private ImageView iv_left_raw;
    private TextView tv_document;

    private TextView mTv_identify;
    private EditText mEt_phonenumber;
    private EditText mEt_code;
    private TextView mTv_login;
    private TimeCount mTimeCount;
    private final int WHAT_CODE = 16901;
    private final int WHAT_LOGIN = 16902;
    private View LoginView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LoginView == null) {
            LoginView = inflater.inflate(R.layout.aty_login, null);
            initviews();
        }
        return LoginView;
    }

    private void initviews() {
        // TODO Auto-generated method stub
        iv_left_raw = (ImageView) LoginView.findViewById(R.id.iv_left_raw);
        tv_document = (TextView) LoginView.findViewById(R.id.tv_document);
        iv_left_raw.setVisibility(View.VISIBLE);
        barTitle = (TextView) LoginView.findViewById(R.id.tv_title);
        barTitle.setText("手机快速登录");
        mTv_identify = (TextView) LoginView.findViewById(R.id.tv_identify);
        mEt_code = (EditText) LoginView.findViewById(R.id.et_code);
        mEt_phonenumber = (EditText) LoginView.findViewById(R.id.et_phonenumber);
        mEt_phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 11) {//换下一个输入框
                    mEt_code.requestFocus();
                }
            }
        });

        mTv_login = (TextView) LoginView.findViewById(R.id.tv_login);
        MyUtils.setViewsOnClick(this, mTv_login, mTv_identify, tv_document,
                iv_left_raw);
        mTimeCount = new TimeCount(30000, 1000);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String phonenumber = mEt_phonenumber.getText() + "";
        String identifynumber = mEt_code.getText() + "";
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_document:
                startActivity(new Intent(mContext, WebAty.class)
                        .putExtra("title", "用户使用协议").putExtra("url",
                                YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=law"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.tv_identify:
                if (phonenumber.length() < 11) {
                    MyUtils.showToast(mContext, "请输入正确的手机号");
                } else {
                    requestIdentify(phonenumber);
                    mTimeCount.start();
                }
                break;
            case R.id.tv_login:
                if (phonenumber.length() < 11 || TextUtils.isEmpty(identifynumber)) {
                    MyUtils.showToast(mContext, "请输入正确的信息");
                } else {
                    requestLogin(phonenumber, identifynumber);
                }

            default:
                break;
        }
    }

    /**
     * 验证码请求
     *
     * @param number
     */
    private void requestIdentify(String number) {
        AES mAes = new AES();
        byte[] mBytes = null;
        String endStr = null;
        try {
            mBytes = ("mobile=" + number).getBytes("UTF8");
            String enString = mAes.encrypt(mBytes);
            endStr = URLEncoder.encode(enString, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", number);
        map.put("versionCode", YYApplication.versionCode + "");
        map.put("sign", endStr);
        YYRunner.getData(WHAT_CODE, YYRunner.Method_POST, YYUrl.SENDVERIFYCODE,
                map, LoginFrg.this);
    }

    /**
     * 登录请求
     *
     * @param number
     * @param code
     */
    private void requestLogin(String number, String code) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", number);
        map.put("code", code);
        map.put("lng", YYApplication.Longitude + "");
        map.put("lat", YYApplication.Latitude + "");

        YYRunner.getData(WHAT_LOGIN, YYRunner.Method_POST, YYUrl.GETLOGIN, map,
                LoginFrg.this);

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
        switch (tag) {
            case WHAT_LOGIN:
                json2UserBean(json);
                break;
            case WHAT_CODE:
                String jn = json;
                break;

            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private void json2UserBean(String json) {
        if (!json.isEmpty())
            try {
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                    YYConstans.setUserInfoBean(userInfoBean);

                    Map<String, String> map1 = new HashMap<String, String>();
                    map1.put("skey", userInfoBean.getReturnContent().getSkey());
                    String xgtoken = SharedPreferenceTool.getPrefString(mContext,
                            SharedPreferenceTool.KEY_XGTOKEN, "");
                    map1.put("phoneId", xgtoken);
                    map1.put("mobile", userInfoBean.getReturnContent().getUser().getMobile());
                    map1.put("phoneType", "2");
                    map1.put("scene", "login");
                    YYRunner.getData(12121, YYRunner.Method_POST,
                            YYUrl.ADDPHONEUSE, map1, LoginFrg.this);
                    mContext.sendBroadcast(new Intent(YYBroadCastName.BC_LoginSuccess));

                }

                JSONObject jsonObject = new JSONObject(json);
                String errno = jsonObject.getString("errno");
                if (!"0".equals(errno)) {
                    MyUtils.showToast(
                            mContext,
                            jsonObject.has("error") ? jsonObject
                                    .getString("error") : "服务器错误");
                } else {
                    getActivity().finish();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            mTv_identify.setText("再发一次");
            mTv_identify.setClickable(true);
            mTv_identify.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTv_identify.setClickable(false);
            mTv_identify.setText((millisUntilFinished / 1000) + "s");
        }

    }

    /**
     * 验证手机号 （由于开头段号不确定暂时不使用）
     */
    // public static boolean isMobile(String mobiles) {
    // Pattern p = Pattern
    // .compile("^((17[0-9])|(13[0-9])|(147)|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    // Matcher m = p.matcher(mobiles);
    // return m.matches();
    // }
    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

}
