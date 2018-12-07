package com.dzrcx.jiaan.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.WebAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 计价说明
 */
public class ValuationFrg extends YYBaseFragment implements View.OnClickListener {
    private View valuationview;
    private ImageView iv_left_raw, iv_carimg;
    private TextView tv_title, tv_carname, tv_hourprice, tv_melleageprice, tv_startprice, tv_allcarprice, tv_pricetxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (valuationview == null) {
            valuationview = inflater.inflate(R.layout.frg_valuation, null);
            initView();
            initData();
        }
        return valuationview;
    }

    private void initData() {
        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        ImageLoader.getInstance().displayImage(bundle.getString("CarMainPhoto"),
                iv_carimg, YYOptions.Option_CARITEM);
        Log.d("ValuationFrg",bundle.getString("CarMainPhoto"));
        tv_carname.setText(bundle.getString("Brand"));
        tv_hourprice.setText("时间：" + bundle.getDouble("DayPrice") + "元 / 时");
        tv_melleageprice.setText("里程：" + bundle.getDouble("MileagePrice") + "元 / 公里");
        tv_startprice.setText("起步价：" + bundle.getString("StartPrice") + "元");
        tv_pricetxt.setText("使用星辰出行车辆，用车费用全部限时" +
                "" + bundle.getString("Discount") + "折，目前所展现的价格均是" + bundle.getString("Discount") + "折以后的价格；星辰出行车辆为最低1小时起租，1小时以内均按起步价为准收取费用，其中公里费按实际行驶公里数计算。");
    }

    private String getTime(String time) {
        if (time.length() < 2) {
            time = "0" + time;
        }
        return time;
    }

    public void initView() {
        iv_left_raw = (ImageView) valuationview.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        tv_title = (TextView) valuationview.findViewById(R.id.tv_title);
        tv_title.setText("计价说明");
        iv_carimg = (ImageView) valuationview.findViewById(R.id.iv_carimg);
        tv_carname = (TextView) valuationview.findViewById(R.id.tv_carname);
        tv_hourprice = (TextView) valuationview.findViewById(R.id.tv_hourprice);
        tv_melleageprice = (TextView) valuationview.findViewById(R.id.tv_melleageprice);
        tv_startprice = (TextView) valuationview.findViewById(R.id.tv_startprice);
        tv_allcarprice = (TextView) valuationview.findViewById(R.id.tv_allcarprice);
        tv_pricetxt = (TextView) valuationview.findViewById(R.id.tv_pricetxt);
        tv_allcarprice = (TextView) valuationview.findViewById(R.id.tv_allcarprice);
        MyUtils.setViewsOnClick(this, iv_left_raw, tv_allcarprice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.tv_allcarprice:
                startActivity(new Intent(mContext, WebAty.class).putExtra("title",
                        "全部车型计价说明").putExtra("url",
                        YYUrl.GETDOCUMENT + "?lng=" + YYApplication.Longitude + "&lat=" + YYApplication.Latitude + "&flag=chargeRole"));
                getActivity().overridePendingTransition(R.anim.activity_up,
                        R.anim.activity_push_no_anim);
                break;
        }
    }
}
