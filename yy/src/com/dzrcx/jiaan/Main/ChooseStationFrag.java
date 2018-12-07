package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.StationListBean;
import com.dzrcx.jiaan.Bean.StationVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.OnAreaClick;
import com.dzrcx.jiaan.tools.MyUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhangyu on 16-5-9.
 */
public class ChooseStationFrag extends YYBaseFragment implements View.OnClickListener, OnAreaClick {


    private View contentView;

    private TextView titleView;
    private ImageView rightView, iv_left_raw;
    private PullToRefreshListView areaListView;
    private PullToRefreshListView stationListView;
    private TextView areaName;
    private AreaAdapter areaAdapter;
    private StationAdapter stationAdapter;
    private StationListBean stationListBean;
    private boolean hasCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_choose_station, null);
            initView();
            hasCreate = true;
            if (stationListBean != null) {
                showData(stationListBean);
            }
        }
        return contentView;
    }


    private void initView() {

        iv_left_raw = (ImageView) contentView.findViewById(R.id.iv_left_raw);
        titleView = (TextView) contentView.findViewById(R.id.tv_title);
        rightView = (ImageView) contentView.findViewById(R.id.iv_right);
        areaListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list_area);
        stationListView = (PullToRefreshListView) contentView.findViewById(R.id.pull_refresh_list_station);
        areaListView.setMode(PullToRefreshBase.Mode.DISABLED);
        stationListView.setMode(PullToRefreshBase.Mode.DISABLED);
        areaName = (TextView) contentView.findViewById(R.id.area_name);
        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(this);
        titleView.setText("选择租赁站");
//        rightView.setVisibility(View.VISIBLE);
//        rightView.setOnClickListener(this);

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


    @Override
    public void onArea(String area, ArrayList<StationVo> stationVos) {
        areaName.setText(area + "租赁站:");
        if (stationAdapter == null) {
            stationAdapter = new StationAdapter(this);
        }
        stationAdapter.setStationVos(stationVos);
        stationListView.setAdapter(stationAdapter);
        stationAdapter.notifyDataSetChanged();
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
//                show();
            } else {
//                hide();
//                baseHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                getView().setVisibility(
                        getView().GONE);
            }
//                }, 300);
        }
    }


    public void showData(StationListBean stationListBean) {
        if (stationListBean == null) {
            return;
        }
        this.stationListBean = stationListBean;


        if (hasCreate) {
            areaAdapter = new AreaAdapter(this);
            areaAdapter.setAreaList(stationListBean.getReturnContent());
            areaAdapter.setAreaClick(this);
            areaListView.setAdapter(areaAdapter);
            if (stationListBean.getReturnContent() != null && stationListBean.getReturnContent().size() > 0) {
                onArea(stationListBean.getReturnContent().get(0).keySet().iterator().next(), stationListBean.getReturnContent().get(0).values().iterator().next());
            }
        }

    }


    private class AreaAdapter extends BaseAdapter {

        private ArrayList<HashMap<String, ArrayList<StationVo>>> areaList = null;
        private YYBaseFragment baseFragment;
        private LayoutInflater inflater;
        private OnAreaClick areaClick;
        private OnItemClick itemClick;
        private int action = 0;

        public AreaAdapter(YYBaseFragment baseFragment) {
            this.baseFragment = baseFragment;
            this.inflater = LayoutInflater.from(baseFragment.mContext);
            this.itemClick = new OnItemClick();
        }

        @Override
        public int getCount() {
            return areaList == null ? 0 : areaList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            AreaViewHold viewHold = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_choose_station_area, null);
                viewHold = new AreaViewHold();
                viewHold.area = (TextView) convertView.findViewById(R.id.item_choosestation_area_text);

                convertView.setTag(viewHold);
            } else {
                viewHold = (AreaViewHold) convertView.getTag();
            }
            viewHold.area.setText(areaList.get(position).keySet().iterator().next());
            viewHold.area.setTag(areaList.get(position));
            viewHold.area.setTag(R.id.tag_second, position);
            viewHold.area.setOnClickListener(this.itemClick);

            if (action == position) {
                viewHold.area.setSelected(true);
            } else {
                viewHold.area.setSelected(false);
            }


            return convertView;
        }

        public ArrayList<HashMap<String, ArrayList<StationVo>>> getAreaList() {
            return areaList;
        }

        public void setAreaList(ArrayList<HashMap<String, ArrayList<StationVo>>> areaList) {
            this.areaList = areaList;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public OnAreaClick getAreaClick() {
            return areaClick;
        }

        public void setAreaClick(OnAreaClick areaClick) {
            this.areaClick = areaClick;
        }


        class OnItemClick implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                HashMap<String, ArrayList<StationVo>> key = (HashMap<String, ArrayList<StationVo>>) v.getTag();
                int action = (int) v.getTag(R.id.tag_second);

                if (key != null) {
                    if (areaClick != null) {
                        areaClick.onArea(key.keySet().iterator().next(), key.values().iterator().next());
                    }

                }
                AreaAdapter.this.setAction(action);
                AreaAdapter.this.notifyDataSetChanged();
            }
        }

    }


    private class StationAdapter extends BaseAdapter {

        private ArrayList<StationVo> stationVos;
        private YYBaseFragment baseFragment;
        private LayoutInflater inflater;
        private Onclick onclick;


        public StationAdapter(YYBaseFragment baseFragment) {
            this.baseFragment = baseFragment;
            this.inflater = LayoutInflater.from(baseFragment.mContext);
            this.onclick = new Onclick();
        }

        @Override
        public int getCount() {
            return stationVos == null ? 0 : stationVos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StationViewHold viewHold = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_choosesation_stations, null);
                viewHold = new StationViewHold();
                viewHold.stationName = (TextView) convertView.findViewById(R.id.item_choosestation_station_name);
                viewHold.stationDis = (TextView) convertView.findViewById(R.id.item_choosestation_station_dis);
                viewHold.stationLoca = (TextView) convertView.findViewById(R.id.item_choosestation_station_loca);
                convertView.setTag(viewHold);
            } else {
                viewHold = (StationViewHold) convertView.getTag();
            }


            StationVo stationVo = stationVos.get(position);
            convertView.setTag(R.id.tag_second, stationVo);
            convertView.setOnClickListener(onclick);
            viewHold.stationName.setText(stationVo.getName());
            viewHold.stationDis.setText(MyUtils.km2m(stationVo.getDistance() + ""));
            viewHold.stationLoca.setText(stationVo.getAddress());

            return convertView;
        }


        public ArrayList<StationVo> getStationVos() {
            return stationVos;
        }

        public void setStationVos(ArrayList<StationVo> stationVos) {
            this.stationVos = stationVos;
        }


        private class Onclick implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                StationVo stationVo = (StationVo) v.getTag(R.id.tag_second);
                if (stationVo != null) {


                    if (stationVo.getParkingNum() == 0) {
                        MyUtils.showToast(mContext, "该租赁站停车位已满，请选择其它租赁站还车");
                        return;
                    }


                    if (baseFragment instanceof ChooseStationFrag) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("StationVo", stationVo);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        baseFragment.getActivity().setResult(Activity.RESULT_OK, intent);
                        baseFragment.getActivity().finish();
                    }
                }
            }
        }


    }


    private class AreaViewHold {
        private TextView area;
    }

    private class StationViewHold {
        private TextView stationName, stationDis, stationLoca;
    }


}
