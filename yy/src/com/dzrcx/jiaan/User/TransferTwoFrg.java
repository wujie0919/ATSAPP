package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.UserInfoBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.pickerview.TimePickerDialog;
import com.dzrcx.jiaan.widget.pickerview.data.Type;
import com.dzrcx.jiaan.widget.pickerview.listener.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 16-10-12.
 */
public class TransferTwoFrg extends YYBaseFragment implements View.OnClickListener, RequestInterface, OnDateSetListener {


    private View contextView;
    private ImageView iv_back;
    private TextView tv_title, ed_date;
    private EditText ed_name, ed_sfz;
    private TextView tv_submit;
    private TimePickerDialog mDialogYearMonthDay;

    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (contextView == null) {
            contextView = inflater.inflate(R.layout.aty_transfertwo, null);
            iv_back = (ImageView) contextView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contextView.findViewById(R.id.tv_title);
            ed_name = (EditText) contextView.findViewById(R.id.transfertwo_name);
            ed_sfz = (EditText) contextView.findViewById(R.id.transfertwo_sfz);
            ed_date = (TextView) contextView.findViewById(R.id.transfertwo_date);
            tv_submit = (TextView) contextView.findViewById(R.id.transfertwo_next);
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("身份验证");
            ed_date.setOnClickListener(this);
            iv_back.setOnClickListener(this);
            tv_submit.setOnClickListener(this);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Long second = null;
            try {
                second = format.parse("19000101000000").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mDialogYearMonthDay = new TimePickerDialog.Builder()
                    .setType(Type.YEAR_MONTH_DAY)
                    .setCallBack(this)
                    .setTitleStringId("选择日期")
                    .setYearText("")
                    .setMonthText("")
                    .setDayText("")
                    .setCyclic(false)
                    .setMinMillseconds(second)
//                .setMinMillseconds(System.currentTimeMillis() - years50)
                    .setMaxMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.bgcolor))
                    .setWheelItemTextNormalColor(getResources().getColor(R.color.text_a6a6a6))
                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.text_3d3f42))
                    .setWheelItemTextSize(13)
                    .build();
            phone = getActivity().getIntent().getExtras().getString("phone");

        }
        return contextView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.transfertwo_date:
                mDialogYearMonthDay.show(getActivity().getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.transfertwo_next:
                submit();
                break;
        }

    }


    private void submit() {

        String name = ed_name.getText().toString();
        String sfz = ed_sfz.getText().toString();
        String date = ed_date.getText().toString();


        if (TextUtils.isEmpty(name)) {
            MyUtils.showToast(mContext, "请输入您的姓名");
            return;
        }
        if (TextUtils.isEmpty(sfz)) {
            MyUtils.showToast(mContext, "请输入您的身份证号码");
            return;
        }
        if (TextUtils.isEmpty(date)) {
            MyUtils.showToast(mContext, "请输入您的领证日期");
            return;
        }

        dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("userId", YYConstans.getUserInfoBean().getReturnContent().getUser().getUserId() + "");

        params.put("mobile", YYConstans.getUserInfoBean().getReturnContent().getUser().getMobile());
        params.put("oldMobile", phone);
        params.put("name", name);
        params.put("name", name);
        params.put("idCardNo", sfz);
        params.put("firstIssueDate", date);
        YYRunner.getData(1001, YYRunner.Method_POST, YYUrl.CHANGEMOBILE,
                params, this);


    }


    @Override
    public void onError(int tag, String error) {
        dismmisDialog();

    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        switch (tag) {
            case 1001:
                UserInfoBean userInfoBean = (UserInfoBean) GsonTransformUtil
                        .fromJson(json, UserInfoBean.class);
                if (userInfoBean != null) {
                    MyUtils.showToast(mContext, userInfoBean.getError());
                    if (userInfoBean.getErrno() == 0) {
                        if (userInfoBean != null && userInfoBean.getErrno() == 0) {
                            YYConstans.setUserInfoBean(userInfoBean);
                            startActivity(TransferThreeAty.class, null);
                            getActivity().finish();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds, "yyyy-MM-dd");
        ed_date.setText(text);
    }

    public String getDateToString(long time, String fomatestr) {
        SimpleDateFormat sf = new SimpleDateFormat(fomatestr);
        Date d = new Date(time);
        return sf.format(d);
    }
}