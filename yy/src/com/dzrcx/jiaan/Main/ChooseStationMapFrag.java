package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.StationListBean;
import com.dzrcx.jiaan.Bean.StationVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.tools.MyUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-6-16.
 */
public class ChooseStationMapFrag extends YYBaseFragment implements View.OnClickListener, BaiduMap.OnMarkerClickListener {

    private View contentView;
    private ImageView iv_right, iv_left_raw;
    private TextView tv_title;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private ArrayList<StationVo> stationVos;
    private BitmapDescriptor bitmapDescriptor = null;
    private BitmapDescriptor starBitmapDescriptor = null;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_choosestation_map, null);
            iv_right = (ImageView) contentView.findViewById(R.id.iv_right);
            iv_left_raw = (ImageView) contentView.findViewById(R.id.iv_left_raw);
            tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            mMapView = (MapView) contentView.findViewById(R.id.bmapView);
            mMapView.showZoomControls(false);// 不显示缩放控件
            mMapView.showScaleControl(false);// 不显示比例尺
            mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
            mBaiduMap = mMapView.getMap();
            mBaiduMap.setOnMarkerClickListener(this);// 设置marker点击事件
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .zoomTo(16f)); // 设定地图缩放比例百度地图缩放范围（3-19），12两公里
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getStationView(""), MyUtils.dip2px(mContext, 40),
                    MyUtils.dip2px(mContext, 40)));
            starBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewToBitMap(
                    getLocationView(), MyUtils.dip2px(mContext, 21),
                    MyUtils.dip2px(mContext, 32)));
            moveToMiddle(YYApplication.Latitude, YYApplication.Longitude);
            iv_left_raw.setVisibility(View.VISIBLE);
            iv_left_raw.setOnClickListener(this);
            tv_title.setText("选择租赁站");
//            iv_right.setVisibility(View.VISIBLE);
//            iv_right.setOnClickListener(this);
        }

        return contentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        } else {
            mMapView = null;
        }
        if (bitmapDescriptor != null)
            bitmapDescriptor.recycle();
        if (starBitmapDescriptor != null)
            starBitmapDescriptor.recycle();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.iv_right:
                showCallDialog();
                break;
        }
    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            if (menuVisible) {
                getView().setVisibility(
                        menuVisible ? getView().VISIBLE : getView().GONE);
            } else {
                getView().setVisibility(
                        getView().GONE);
            }
        }
    }

    public void showData(StationListBean stationListBean) {
        if (stationListBean == null) {
            return;
        }
        stationVos = stationListBean.getReturnContent().get(0).values().iterator().next();
        addData();
        return;
    }

    private void addData() {
        mBaiduMap.clear();
        moveToMiddle(YYApplication.Latitude, YYApplication.Longitude);
        for (int i = 0; stationVos != null && i < stationVos.size(); i++) {
            StationVo stationVo = stationVos.get(i);
            Marker marker = addMarker(stationVo.getLatitude(),
                    stationVo.getLongitude(), bitmapDescriptor);
            Bundle bundle = new Bundle();
            bundle.putSerializable("stationVo", stationVo);
            marker.setExtraInfo(bundle);
        }
        addMarker(YYApplication.Latitude, YYApplication.Longitude,//保证定位图标在上层，防止被覆盖掉
                starBitmapDescriptor);
    }

    /**
     * 移动到以当前坐标为中心的画面
     *
     * @param latitude
     * @param longitude
     */
    private void moveToMiddle(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        // 移动到以当前坐标为中心的画面
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    /**
     * 获取当前站点View
     *
     * @param number
     * @return
     */
    private View getStationView(String number) {
        View view;

        view = inflater.inflate(R.layout.mapcarfrg_carlocation_icon_n, null);
        TextView tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
        ImageView iv_station_bg = (ImageView) view.findViewById(R.id.iv_station_bg);
        tv_car_number.setTextColor(getResources().getColor(R.color.titlebar_background));
        tv_car_number.setText(null);
        iv_station_bg.setImageResource(R.drawable.mapcarfrg_havecar_bg);


//        view = inflater.inflate(R.layout.car_location_icon_y, null);
//        TextView tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
//        tv_car_number.setText(number + "");
        return view;
    }

    /**
     * 增加mark并显示在地图上
     *
     * @param latitude
     * @param longitude
     * @param bitmapDescriptor
     * @return Marker
     */

    private Marker addMarker(double latitude, double longitude,
                             BitmapDescriptor bitmapDescriptor) {
        LatLng ll = new LatLng(latitude, longitude);
        OverlayOptions oo = new MarkerOptions().position(ll)
                .icon(bitmapDescriptor).zIndex(9).draggable(false);
        Marker marker = ((Marker) mBaiduMap.addOverlay(oo));
        return marker;
    }

    /**
     * 获取站点图标View
     */
    private View getLocationView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.TRANSPARENT);
        ImageView iv_guide = new ImageView(mContext);
        iv_guide.setImageResource(R.drawable.my_location);
//        iv_guide.setLayoutParams(new LinearLayout.LayoutParams(with, hight));
        layout.addView(iv_guide);
        return layout;
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
    public boolean onMarkerClick(Marker marker) {
        if (marker.getExtraInfo() == null
                || !marker.getExtraInfo().containsKey("stationVo"))
            return false;
        StationVo stationVo = (StationVo) marker.getExtraInfo().get("stationVo");

        if (stationVo.getParkingNum() == 0) {
            MyUtils.showToast(mContext, "该租赁站停车位已满，请选择其它租赁站还车");
            return false;
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("StationVo", stationVo);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();

        return false;
    }
}
