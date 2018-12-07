package com.dzrcx.jiaan.Main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情页
 */
public class RunDetailFrg extends YYBaseFragment implements
        OnClickListener, BaiduMap.OnMapLoadedCallback {
    private ImageView iv_left_raw;
    private TextView tv_title,tv_timemoney_text,tv_multiple,tv_all_money;
    private TextView tv_carname, tv_usetime, tv_ordertime, tv_feetime, tv_getcartime, tv_gavecartime, tv_timemoney, tv_mileagemoney, tv_discount, tv_actualfee;
    private OrderListItemBean bean;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LayoutInflater inflater;
    private View runDetailView;
    //                     取车时间          计费时间     车费倍数    总费用
    private RelativeLayout tl_mileagemoney,tl_feetime,tl_multiple,tl_all_money;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        runDetailView = inflater.inflate(R.layout.frg_rundetail, null);
        initView();
        bean = (OrderListItemBean) mContext.getIntent().getSerializableExtra("OrderListItemBean");
        filldata();
        return runDetailView;
    }

    private void filldata() {
        if (bean != null) {
            if(bean.getTimeMode() == 1){
                tl_mileagemoney.setVisibility(View.GONE);
                tl_feetime.setVisibility(View.GONE);
                tl_multiple.setVisibility(View.GONE);
                tl_all_money.setVisibility(View.GONE);
            }

            tv_carname.setText(bean.getMake() + " " + bean.getModel() + "  " + bean.getLicense());
            if(bean.getTimeMode() == 1){//是日租
                tv_usetime.setText("用车时间：" + bean.getRentedDayNumber() + "天" + TimeDateUtil.formatTime(bean.getFeeDetail().getCostTime()));
                tv_ordertime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getOrderTime()));
                tv_feetime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getChargeTime()));
                tv_getcartime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getOrderTime()));
                tv_gavecartime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getReturnTime()));
                tv_timemoney_text.setText("用车费用");
                tv_timemoney.setText(MyUtils.formatPriceShort(bean.getRentalDay() + bean.getFeeDetail().getAllPrice()) + "元");
                tv_mileagemoney.setText(MyUtils.formatPriceShort(bean.getFeeDetail().getDistancePrice()) + "元");
                if (bean.getFeeDetail().getBenefitPrice() == 0) {
                    tv_discount.setText(MyUtils.formatPriceShort(0) + "元");
                } else {
                    tv_discount.setText("-" + MyUtils.formatPriceShort(bean.getFeeDetail().getBenefitPrice()) + "元");
                }
                tv_actualfee.setText(MyUtils.formatPriceShort(bean.getRentalDay() + bean.getFeeDetail().getAllPrice()) + "元");
            }else{//是时租
                tv_multiple.setText(bean.getFreedomMultiple() == 0 ? "1" : "x"+MyUtils.formatPriceShort(bean.getFreedomMultiple()));
                tv_all_money.setText(MyUtils.formatPriceShort(bean.getFeeDetail().getAllPrice())+"  元");
                tv_usetime.setText("用车时间：" + TimeDateUtil.formatTime(bean.getFeeDetail().getCostTime()) + " | 行驶里程" + bean.getFeeDetail().getDistance() + "公里");
                tv_ordertime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getOrderTime()));
                tv_feetime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getChargeTime()));
                tv_getcartime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getPickcarTime()));
                tv_gavecartime.setText(TimeDateUtil.dateToStrLong(bean.getFeeDetail().getReturnTime()));
                tv_timemoney.setText(MyUtils.formatPriceShort(bean.getFeeDetail().getHourPrice()) + "元");
                tv_mileagemoney.setText(MyUtils.formatPriceShort(bean.getFeeDetail().getDistancePrice()) + "元");
                if (bean.getFeeDetail().getBenefitPrice() == 0) {
                    tv_discount.setText(MyUtils.formatPriceShort(0) + "元");
                } else {
                    tv_discount.setText("-" + MyUtils.formatPriceShort(bean.getFeeDetail().getBenefitPrice()) + "元");
                }
                tv_actualfee.setText("" + MyUtils.formatPriceShort(bean.getFeeDetail().getPayPrice()) + "元");
            }
        }
    }

    private void initView() {
        tl_mileagemoney = (RelativeLayout) runDetailView.findViewById(R.id.tl_mileagemoney);
        tl_multiple = (RelativeLayout) runDetailView.findViewById(R.id.tl_multiple);
        tl_all_money = (RelativeLayout) runDetailView.findViewById(R.id.tl_all_money);
        tl_feetime = (RelativeLayout) runDetailView.findViewById(R.id.tl_feetime);
        iv_left_raw = (ImageView) runDetailView.findViewById(R.id.iv_left_raw);
        tv_title = (TextView) runDetailView.findViewById(R.id.tv_title);
        tv_title.setText("行程详情");
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(this);

        tv_carname = (TextView) runDetailView.findViewById(R.id.tv_carname);
        tv_multiple = (TextView) runDetailView.findViewById(R.id.tv_multiple);
        tv_all_money = (TextView) runDetailView.findViewById(R.id.tv_all_money);
        tv_timemoney_text = (TextView) runDetailView.findViewById(R.id.tv_timemoney_text);
        tv_usetime = (TextView) runDetailView.findViewById(R.id.tv_usetime);
        tv_ordertime = (TextView) runDetailView.findViewById(R.id.tv_ordertime);
        tv_feetime = (TextView) runDetailView.findViewById(R.id.tv_feetime);
        tv_getcartime = (TextView) runDetailView.findViewById(R.id.tv_getcartime);
        tv_gavecartime = (TextView) runDetailView.findViewById(R.id.tv_gavecartime);
        tv_timemoney = (TextView) runDetailView.findViewById(R.id.tv_timemoney);
        tv_mileagemoney = (TextView) runDetailView.findViewById(R.id.tv_mileagemoney);
        tv_discount = (TextView) runDetailView.findViewById(R.id.tv_discount);
        tv_actualfee = (TextView) runDetailView.findViewById(R.id.tv_actualfee);

        mMapView = (MapView) runDetailView.findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);// 不显示缩放控件
        mMapView.showScaleControl(false);// 不显示比例尺
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setAllGesturesEnabled(false);   //关闭一切手势操作
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        MapStatus mapStatus = new MapStatus.Builder().overlook(-45).zoom(18).build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mapStatus);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.animateMapStatus(u, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
        }
    }


    @Override
    public void onMapLoaded() {
        if (bean == null) {
            return;
        }
        LatLng end;
        LatLng start = new LatLng(bean.getFromXY().getGpsXY().getLat(), bean.getFromXY().getGpsXY().getLng());
        if (bean.getToXY().getGpsXY().getLat() == 0 && bean.getToXY().getGpsXY().getLng() == 0) {
            end = new LatLng(bean.getFromXY().getGpsXY().getLat(), bean.getFromXY().getGpsXY().getLng());
        } else {
            end = new LatLng(bean.getToXY().getGpsXY().getLat(), bean.getToXY().getGpsXY().getLng());
        }
        ImageView imageViewa = new ImageView(mContext);
        imageViewa.setImageResource(R.drawable.getcar_icon);
        OverlayOptions ooA = new MarkerOptions().position(start).icon(BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                imageViewa, MyUtils.dip2px(mContext, 37),
                MyUtils.dip2px(mContext, 46))));
        ImageView imageViewb = new ImageView(mContext);
        imageViewb.setImageResource(R.drawable.gavecar_icon);
        OverlayOptions ooB = new MarkerOptions().position(end).icon(BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                imageViewb, MyUtils.dip2px(mContext, 37),
                MyUtils.dip2px(mContext, 46))));
        LiOverlayManager
                overlayManager = new LiOverlayManager(mBaiduMap);
        List<OverlayOptions> optionses = new ArrayList<>();
        optionses.add(ooA);
        optionses.add(ooB);
        overlayManager.setData(optionses);
        overlayManager.addToMap();
        overlayManager.zoomToSpan();
    }

    /**
     * 获取bitmap
     *
     * @param
     * @param
     * @return
     */

    private Bitmap ViewToBitMap(View view, int with, int hight) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());
        layout.buildDrawingCache();
        return layout.getDrawingCache();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
    }

    public class LiOverlayManager extends OverlayManager {
        private List<OverlayOptions> optionsList = new ArrayList<OverlayOptions>();

        public LiOverlayManager(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public List<OverlayOptions> getOverlayOptions() {
            return optionsList;
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }

        public void setData(List<OverlayOptions> optionsList) {
            this.optionsList = optionsList;
        }

        @Override
        public boolean onPolylineClick(Polyline polyline) {
            return false;
        }
    }

}