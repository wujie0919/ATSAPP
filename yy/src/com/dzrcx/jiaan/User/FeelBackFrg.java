package com.dzrcx.jiaan.User;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.FeelBackContentBean;
import com.dzrcx.jiaan.Bean.FeelBackContentReturnBean;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeelBackFrg extends YYBaseFragment implements OnClickListener,
        RequestInterface {
    private RelativeLayout rl_health, rl_inoperable, rl_havehitch, rl_nocar;
    private TextView tv_health, tv_inoperable, tv_havehitch, tv_nocar, tv_tishi1, tv_tishi2;
    private ImageView iv_health, iv_inoperable, iv_havehitch, iv_nocar;
    private TextView tv_commit, tv_title;
    private ImageView iv_left_raw;
    private EditText et_content;
    private View feelBackView;
    private ImageView iv_right;
    private String content = "";
    private TextView[] textView_contennts;
    /**
     * 1取消订单
     * <p/>
     * 2正常结束订单
     */
    private int type = 1;


    private String orderId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (feelBackView == null) {
            initView(inflater);
            getData(true, type);
        }
        return feelBackView;
    }

    public void getData(Boolean isShowDialog, int type) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "" + type);
        YYRunner.getData(type, YYRunner.Method_POST, YYUrl.GETFEELBACKCONTENT,
                params, this);
    }

    private void initView(LayoutInflater inflater) {
        feelBackView = inflater.inflate(R.layout.aty_feelback, null);
        //title
        iv_left_raw = (ImageView) feelBackView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_right = (ImageView) feelBackView.findViewById(R.id.iv_right);
//        iv_right.setVisibility(View.VISIBLE);
        tv_title = (TextView) feelBackView.findViewById(R.id.tv_title);
        tv_tishi1 = (TextView) feelBackView.findViewById(R.id.tv_tishi1);
        tv_tishi2 = (TextView) feelBackView.findViewById(R.id.tv_tishi2);

        //选项部分
        rl_health = (RelativeLayout) feelBackView.findViewById(R.id.rl_health);
        rl_inoperable = (RelativeLayout) feelBackView.findViewById(R.id.rl_inoperable);
        rl_havehitch = (RelativeLayout) feelBackView.findViewById(R.id.rl_havehitch);
        rl_nocar = (RelativeLayout) feelBackView.findViewById(R.id.rl_nocar);
        tv_health = (TextView) feelBackView.findViewById(R.id.tv_health);
        tv_inoperable = (TextView) feelBackView.findViewById(R.id.tv_inoperable);
        tv_havehitch = (TextView) feelBackView.findViewById(R.id.tv_havehitch);
        tv_nocar = (TextView) feelBackView.findViewById(R.id.tv_nocar);
        textView_contennts = new TextView[]{tv_health, tv_inoperable, tv_havehitch, tv_nocar};
        initColor();
        iv_health = (ImageView) feelBackView.findViewById(R.id.iv_health);
        iv_inoperable = (ImageView) feelBackView.findViewById(R.id.iv_inoperable);
        iv_havehitch = (ImageView) feelBackView.findViewById(R.id.iv_havehitch);
        iv_nocar = (ImageView) feelBackView.findViewById(R.id.iv_nocar);
        rightGone();
        rl_health.setTag(R.id.tag_first, tv_health);
        rl_health.setTag(R.id.tag_second, iv_health);
        rl_inoperable.setTag(R.id.tag_first, tv_inoperable);
        rl_inoperable.setTag(R.id.tag_second, iv_inoperable);
        rl_havehitch.setTag(R.id.tag_first, tv_havehitch);
        rl_havehitch.setTag(R.id.tag_second, iv_havehitch);
        rl_nocar.setTag(R.id.tag_first, tv_nocar);
        rl_nocar.setTag(R.id.tag_second, iv_nocar);
//提交按钮
        tv_commit = (TextView) feelBackView.findViewById(R.id.tv_commit);
        et_content = (EditText) feelBackView.findViewById(R.id.et_content);
        et_content.clearFocus();

        if (type == 1) {
            tv_title.setText("取消用车");
            tv_tishi1.setText("取消用车？告诉我们原因吧");
            tv_tishi2.setText("其它取消用车原因:");
            et_content.setHint("请详细描述原因...");
        } else {
            tv_title.setText("评价我们");
            tv_tishi1.setText("用车过程中是否存在问题？请告知我们");
            tv_tishi2.setText("其它问题与建议:");
            et_content.setHint("请详细写下您在用车过程中的问题与建议");
        }

        MobclickAgent.onEvent(mContext, "click_feedback");
        MyUtils.setViewsOnClick(this, tv_commit, iv_left_raw, iv_right, rl_health, rl_inoperable, rl_havehitch, rl_nocar);
    }

    /**
     * 初始化选线字体颜色
     */
    private void initColor() {
        tv_health.setTextColor(Color.parseColor("#525b63"));
        tv_inoperable.setTextColor(Color.parseColor("#525b63"));
        tv_havehitch.setTextColor(Color.parseColor("#525b63"));
        tv_nocar.setTextColor(Color.parseColor("#525b63"));
    }

    /**
     * 隐藏全部按钮对号
     */
    private void rightGone() {
        iv_health.setVisibility(View.GONE);
        iv_inoperable.setVisibility(View.GONE);
        iv_havehitch.setVisibility(View.GONE);
        iv_nocar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_health:
            case R.id.rl_inoperable:
            case R.id.rl_havehitch:
            case R.id.rl_nocar:
                if (((ImageView) v.getTag(R.id.tag_second)).getVisibility() == View.GONE) {
                    ((ImageView) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
                    ((TextView) v.getTag(R.id.tag_first)).setTextColor(getResources().getColor(R.color.titlebar_background));
                } else {
                    ((ImageView) v.getTag(R.id.tag_second)).setVisibility(View.GONE);
                    ((TextView) v.getTag(R.id.tag_first)).setTextColor(Color.parseColor("#525b63"));
                }
                break;
            case R.id.iv_left_raw:
                startActivity(MainActivity2_1.class, null);
                getActivity().finish();
                break;
            case R.id.iv_right:
                showCallDialog();
                break;
            case R.id.tv_commit:
                if (iv_havehitch.getVisibility() == View.VISIBLE) {
                    content = content + tv_havehitch.getText() + ";;";
                }
                if (iv_health.getVisibility() == View.VISIBLE) {
                    content = content + tv_health.getText() + ";;";
                }
                if (iv_inoperable.getVisibility() == View.VISIBLE) {
                    content = content + tv_inoperable.getText() + ";;";
                }
                if (iv_nocar.getVisibility() == View.VISIBLE) {
                    content = content + tv_havehitch.getText() + ";;";
                }
                if (!(et_content.getText() + "").isEmpty()) {
                    content = content + et_content.getText() + "";
                }
                if (content.isEmpty()) {
                    MyUtils.showToast(mContext, "请确认是否完成内容勾选或编辑，否则无法成功提交。");
                    return;
                }
                MobclickAgent.onEvent(mContext, "ok_feedback");
                commitContent(true);
                break;
            default:
                break;
        }
    }

    private void commitContent(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            showNoNetDlg();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", type + "");
        params.put("orderId", orderId);
        params.put("content", content);
        YYRunner.getData(0, YYRunner.Method_POST, YYUrl.GETFEELBACK,
                params, this);
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        MyUtils.showToast(mContext, "数据传输错误，请重试");
        progresssDialog.dismiss();
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        switch (tag) {
            case 1:
            case 2:
                FeelBackContentReturnBean feelBackContentReturnBean = (FeelBackContentReturnBean) GsonTransformUtil.fromJson(json, FeelBackContentReturnBean.class);
                if (feelBackContentReturnBean.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            feelBackContentReturnBean.getError() + "");
                } else {
                    List<FeelBackContentBean> backContentBeans = feelBackContentReturnBean.getReturnContent();
                    fillData(backContentBeans);
                }
                break;
            case 0:
                YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (yyBaseResBean.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            yyBaseResBean.getError() + "");
                } else {
                    MyUtils.showToast(mContext, "提交成功");
                    startActivity(MainActivity2_1.class, null);
                    getActivity().finish();
                }
                break;
        }

    }

    public void fillData(List<FeelBackContentBean> backContentBeans) {
        for (int i = 0; i < backContentBeans.size(); i++) {
            textView_contennts[i].setText(backContentBeans.get(i).getContent() + "");
        }
    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
