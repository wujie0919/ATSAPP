package com.dzrcx.jiaan.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.HelpCenterBrandReContentVo;
import com.dzrcx.jiaan.Bean.HelpCenterBrandVo;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮助中心
 */
public class HelpCenterFrg extends YYBaseFragment implements RequestInterface, View.OnClickListener {
    private View helpcenterview;
    private ImageView iv_left_raw;
    private TextView tv_title;
    private RelativeLayout rl_rentprocess, rl_price, rl_norquestion, rl_opration,rl_duty_detail;
    private LinearLayout ll_brands;

    private static final int TAG_GETSHOWALLBRANDNAME = 11001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (helpcenterview == null) {
            helpcenterview = inflater.inflate(R.layout.frg_helpcenter, null);
            initView();
            requestBrandNames(true);
        }
        return helpcenterview;
    }

    /**
     * @param isShowDialog
     */
    private void requestBrandNames(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("nullparams", "nullparams");
        YYRunner.getData(TAG_GETSHOWALLBRANDNAME, YYRunner.Method_POST, YYUrl.GETSHOWALLBRANDNAME, map,
                this);
    }

    public void initView() {
        iv_left_raw = (ImageView) helpcenterview.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title = (TextView) helpcenterview.findViewById(R.id.tv_title);
        tv_title.setText("用户帮助中心");
        rl_rentprocess = (RelativeLayout) helpcenterview.findViewById(R.id.rl_rentprocess);
        rl_duty_detail = (RelativeLayout) helpcenterview.findViewById(R.id.rl_duty_detail);
        rl_price = (RelativeLayout) helpcenterview.findViewById(R.id.rl_price);
        rl_norquestion = (RelativeLayout) helpcenterview.findViewById(R.id.rl_norquestion);
        rl_opration = (RelativeLayout) helpcenterview.findViewById(R.id.rl_opration);
        ll_brands = (LinearLayout) helpcenterview.findViewById(R.id.ll_brands);
        MyUtils.setViewsOnClick(this, iv_left_raw, rl_rentprocess, rl_price, rl_norquestion, rl_opration,rl_duty_detail);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.rl_rentprocess:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "租车流程").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=carRental"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.rl_price:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "计价说明").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=chargeRole"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.rl_norquestion:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "常见问题").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=help"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.rl_opration:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "还车说明").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=carBack"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
            case R.id.rl_duty_detail:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "租车协议").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=responsibility"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
        }
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        switch (tag) {
            case TAG_GETSHOWALLBRANDNAME:
                dismmisDialog();
                HelpCenterBrandReContentVo
                        helpCenterBrandReContentVo = (HelpCenterBrandReContentVo) GsonTransformUtil.fromJson(json,
                        HelpCenterBrandReContentVo.class);

                if (helpCenterBrandReContentVo == null || helpCenterBrandReContentVo.getErrno() != 0) {
                    MyUtils.showToast(mContext, helpCenterBrandReContentVo == null ? "数据传输错误,请重试" : helpCenterBrandReContentVo.getError());
                } else {
                    List<HelpCenterBrandVo> helpCenterBrandVos = helpCenterBrandReContentVo.getReturnContent();
                    ll_brands.removeAllViews();
                    for (final HelpCenterBrandVo helpCenterBrandVo : helpCenterBrandVos) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.view_helpcenterbrand, null);
                        TextView tv_brandname = (TextView) view.findViewById(R.id.tv_brandname);
                        view.setTag(helpCenterBrandVo);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent webIntent = null;
                                webIntent = new Intent(mContext,
                                        WebAty.class);
                                webIntent.putExtra("title", helpCenterBrandVo.getpName() + " " + helpCenterBrandVo.getcName());
                                webIntent.putExtra("url", helpCenterBrandVo.getBrandHelpUrl());
                                mContext.startActivity(webIntent);
                            }
                        });
                        tv_brandname.setText(helpCenterBrandVo.getpName() + " " + helpCenterBrandVo.getcName());
                        ll_brands.addView(view);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }
}
