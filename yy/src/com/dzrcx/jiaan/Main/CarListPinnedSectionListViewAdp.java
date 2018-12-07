package com.dzrcx.jiaan.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.CarListItemBean;
import com.dzrcx.jiaan.Bean.MapcarfrgCarVo;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYOptions;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.SearchCar.MapAty;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.distance.DistancePointVo;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.widget.PinnedSectionListView;
import com.dzrcx.jiaan.widget.RoundCornersImageViewAll;
import com.dzrcx.jiaan.widget.RoundImageView;
import com.dzrcx.jiaan.yyinterface.ChangePageInterface;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 车辆与站点列表适配器
 */
public class CarListPinnedSectionListViewAdp extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private List<CarListItemBean> listItemBeans;
    private LayoutInflater inflater;
    private Context context;
    private MainActivity2_1 activity2_1;
    private int screenWidth;
    private ChangePageInterface pageInterface;
    private Handler handler;

    public CarListPinnedSectionListViewAdp(Context context, List<CarListItemBean> listItemBeans,
                                           MainActivity2_1 activity2_1, Handler handler) {
        this.inflater = LayoutInflater.from(context);
        this.listItemBeans = listItemBeans;
        this.context = context;
        screenWidth = MyUtils.getScreenWidth(context);
        this.activity2_1 = activity2_1;
        this.handler = handler;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == YYConstans.TAG_SECTION;
    }

    @Override
    public int getCount() {
        return listItemBeans == null ? 0 : listItemBeans.size();
    }

    @Override
    public CarListItemBean getItem(int position) {
        return listItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolderBg viewHolderBg;
        ViewHolderSection holderSection;
        ViewHolderHasCar viewHolderHasCar;
        ViewHolderNoCar viewHolderNoCar;
        // 添加间隔背景
        if (listItemBeans.get(position).getType() == YYConstans.TAG_SECTIONBG) {
            if (convertView == null || convertView.getTag(R.id.tag_first) == null) {
                viewHolderBg = new ViewHolderBg();
                convertView = inflater.inflate(R.layout.item_carlistbg, null);
                viewHolderBg.v_bg = convertView.findViewById(R.id.v_bg);
                convertView.setTag(R.id.tag_first, viewHolderBg);
            } else {
                viewHolderBg = (ViewHolderBg) convertView.getTag(R.id.tag_first);
            }
            if (position == 0) {
                viewHolderBg.v_bg.setVisibility(View.GONE);
            } else {
                viewHolderBg.v_bg.setVisibility(View.VISIBLE);
            }
        } else if (listItemBeans.get(position).getType() == YYConstans.TAG_SECTION) {
            if (convertView == null || convertView.getTag(R.id.tag_second) == null) {
                holderSection = new ViewHolderSection();
                convertView = inflater.inflate(R.layout.headview_carlist, null);
                holderSection.tv_station = (TextView) convertView.findViewById(R.id.tv_station);
                holderSection.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
                convertView.setTag(R.id.tag_second, holderSection);
            } else {
                holderSection = (ViewHolderSection) convertView.getTag(R.id.tag_second);
            }
            holderSection.tv_distance.setText("距您 " + MyUtils.km2m(listItemBeans.get(position).getMapcarListStationAndCarsVo().getDistance() + ""));
            holderSection.tv_station.setText(listItemBeans.get(position).getMapcarListStationAndCarsVo().getStationName());
            convertView.setTag(R.id.tag_six, listItemBeans.get(position));
            convertView.setOnClickListener(listenerMap);
        } else if (listItemBeans.get(position).getType() == YYConstans.TAG_HASCARSECTION) {
            if (convertView == null || convertView.getTag(R.id.tag_threed) == null) {
                viewHolderHasCar = new ViewHolderHasCar();
                convertView = inflater.inflate(R.layout.item_carlisthascar, null);
//                int picwith = 261 * screenWidth / 720;
//                int picHeight = picwith * 181 / 261;
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                        picwith, picHeight);
                viewHolderHasCar.tv_carnumber = (TextView) convertView
                        .findViewById(R.id.tv_carnumber);
                viewHolderHasCar.tv_elctric = (TextView) convertView
                        .findViewById(R.id.tv_elctric);
//                viewHolderHasCar.tv_price = (TextView) convertView
//                        .findViewById(R.id.tv_price);
//                viewHolderHasCar.tv_startpricetxt = (TextView) convertView
//                        .findViewById(R.id.tv_startpricetxt);
                viewHolderHasCar.rc_carimg = (RoundImageView) convertView
                        .findViewById(R.id.rc_carimg);
                viewHolderHasCar.rl_carimg = (RelativeLayout) convertView
                        .findViewById(R.id.rl_carimg);
//                viewHolderHasCar.v_gavestyle = convertView
//                        .findViewById(R.id.v_gavestyle);
                convertView.setTag(R.id.tag_threed, viewHolderHasCar);
            } else {
                viewHolderHasCar = (ViewHolderHasCar) convertView.getTag(R.id.tag_threed);
            }
            CarListItemBean bean = listItemBeans.get(position);
            MapcarfrgCarVo mapcarfrgCarVo = bean.getMapcarListStationAndCarsVo().getCarList().get(bean.getNumber());
            if (mapcarfrgCarVo != null) {
                ImageLoader.getInstance().displayImage(mapcarfrgCarVo.getCarMainPhoto(),
                        viewHolderHasCar.rc_carimg, YYOptions.Option_CARITEM);
                viewHolderHasCar.tv_carnumber.setText(mapcarfrgCarVo.getBrand() + " " + mapcarfrgCarVo.getSeries() + " · " + mapcarfrgCarVo.getLicense());
                String mileage = "<B><font color='#04b575'>" + (int) mapcarfrgCarVo.getMileage() + "</font></B>" + "<font color='#04b575'>  公里</font>";
                viewHolderHasCar.tv_elctric.setText(Html.fromHtml(mileage));
                String price =  "<B><font color='#666666'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getWorkDayPrice()).trim() + "</font></B>" +
                        "<font color='#666666'>元/时+</font>"
                       + "<B><font color='#666666'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getMileagePrice()).trim() + "</font></B>" + "<font color='#666666'>元/公里</font>";
                //viewHolderHasCar.tv_price.setText(Html.fromHtml(price));
                String startprice = "<font color='#666666'>起步价:</font>" +
                        "<B><font color='#666666'>" + MyUtils.formatPriceShort(mapcarfrgCarVo.getStartPrice()) + "元</font></B>";
                //viewHolderHasCar.tv_startpricetxt.setText(Html.fromHtml(startprice));
//                switch (bean.getMapcarListStationAndCarsVo().getStayType()) {
//                    case 1:
//                        viewHolderHasCar.v_gavestyle.setBackgroundResource(R.drawable.mapcarfrg_aagavecar);
//                        break;
//                    case 2:
//                        viewHolderHasCar.v_gavestyle.setBackgroundResource(R.drawable.mapcarfrg_abgavecar);
//                        break;
//                    case 3:
//                        viewHolderHasCar.v_gavestyle.setBackgroundResource(R.drawable.mapcarfrg_angavecar);
//                        break;
//                }
            }
            convertView.setTag(R.id.tag_seven, bean);
            convertView.setOnClickListener(HasCarlistener);
        } else if (listItemBeans.get(position).getType() == YYConstans.TAG_NOCARSECTION) {
            if (convertView == null || convertView.getTag(R.id.tag_four) == null) {
                viewHolderNoCar = new ViewHolderNoCar();
                convertView = inflater.inflate(R.layout.item_carlistnocar, null);
                viewHolderNoCar.tv_remindme = (TextView) convertView.findViewById(R.id.tv_remindme);
                viewHolderNoCar.tv_cancelremindme = (TextView) convertView.findViewById(R.id.tv_cancelremindme);
                viewHolderNoCar.v_gavestyle = convertView.findViewById(R.id.v_gavestyle);
                viewHolderNoCar.rc_carimg = (RoundCornersImageViewAll) convertView.findViewById(R.id.rc_carimg);
                convertView.setTag(R.id.tag_four, viewHolderNoCar);
            } else {
                viewHolderNoCar = (ViewHolderNoCar) convertView.getTag(R.id.tag_four);
            }
            if (listItemBeans.get(position).getMapcarListStationAndCarsVo().getFlag() == 0) {
                viewHolderNoCar.tv_remindme.setVisibility(View.VISIBLE);
                viewHolderNoCar.tv_cancelremindme.setVisibility(View.GONE);
            } else {
                viewHolderNoCar.tv_remindme.setVisibility(View.GONE);
                viewHolderNoCar.tv_cancelremindme.setVisibility(View.VISIBLE);
            }
            CarListItemBean bean = listItemBeans.get(position);
            switch (bean.getMapcarListStationAndCarsVo().getStayType()) {
                case 1:
                    viewHolderNoCar.v_gavestyle.setBackgroundResource(R.drawable.carlist_nocar_aagavecar);
                    break;
                case 2:
                    viewHolderNoCar.v_gavestyle.setBackgroundResource(R.drawable.carlist_nocar_abgavecar);
                    break;
                case 3:
                    viewHolderNoCar.v_gavestyle.setBackgroundResource(R.drawable.carlist_nocar_angavecar);
                    break;
            }
            viewHolderNoCar.tv_remindme.setTag(R.id.tag_five, listItemBeans.get(position));
            viewHolderNoCar.tv_cancelremindme.setTag(R.id.tag_eight, listItemBeans.get(position));
            viewHolderNoCar.tv_remindme.setOnClickListener(remindLisetener);
            viewHolderNoCar.tv_cancelremindme.setOnClickListener(remindLisetener);
        }
        return convertView;
    }

    View.OnClickListener HasCarlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CarListItemBean itemBean = (CarListItemBean) v.getTag(R.id.tag_seven);
            activity2_1.changeToFragment(1);
            if (pageInterface != null) {
                pageInterface.OnChange(itemBean.getMapcarListStationAndCarsVo(), itemBean.getNumber());
            }
        }
    };
    View.OnClickListener remindLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CarListItemBean itemBean = null;
            if (v.getId() == R.id.tv_remindme) {
                itemBean = (CarListItemBean) v.getTag(R.id.tag_five);
            } else if (v.getId() == R.id.tv_cancelremindme) {
                itemBean = (CarListItemBean) v.getTag(R.id.tag_eight);
            }
            Message message = handler.obtainMessage();
            message.what = itemBean.getMapcarListStationAndCarsVo().getFlag();
            message.obj = itemBean;
            handler.sendMessage(message);
        }
    };
    View.OnClickListener listenerMap = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CarListItemBean itemBean = (CarListItemBean) v.getTag(R.id.tag_six);
            Intent intent = new Intent(context, MapAty.class);

            DistancePointVo pointVo = new DistancePointVo();
            pointVo.setTargetLng(Double.parseDouble(itemBean.getMapcarListStationAndCarsVo().getLongitude() + ""));
            pointVo.setTargetLat(Double.parseDouble(itemBean.getMapcarListStationAndCarsVo().getLatitude() + ""));
            pointVo.setTarAdrrName(itemBean.getMapcarListStationAndCarsVo().getAddress());

            pointVo.setLocationLng(YYApplication.Longitude);
            pointVo.setLocationLat(YYApplication.Latitude);
            pointVo.setLocaAdrrName(YYApplication.LocaAdrrName);

            intent.putExtra("name", "CarListFrg");
            intent.putExtra("className", itemBean.getMapcarListStationAndCarsVo().getStationName());
            intent.putExtra("carnum", itemBean.getMapcarListStationAndCarsVo().getCarCount() + "");
            intent.putExtra("latitude", itemBean.getMapcarListStationAndCarsVo().getLatitude() + "");
            intent.putExtra("longitude", itemBean.getMapcarListStationAndCarsVo().getLongitude() + "");
            Gson gson = new Gson();
            String strJson = gson.toJson(pointVo);
            intent.putExtra("pointVo", strJson);
            context.startActivity(intent);
        }
    };

    private class ViewHolderHasCar {
        private RoundImageView rc_carimg;
        private TextView tv_elctric,tv_carnumber;
        private RelativeLayout rl_carimg;
//        private View v_gavestyle;
    }

    private class ViewHolderSection {
        public TextView tv_station, tv_distance;
    }

    private class ViewHolderNoCar {
        public TextView tv_remindme, tv_cancelremindme;
        private RoundCornersImageViewAll rc_carimg;
        private View v_gavestyle;
    }

    private class ViewHolderBg {
        public View v_bg;
    }

    public void replaceData(List<CarListItemBean> mPostionlistItems) {
        listItemBeans.clear();
        if (mPostionlistItems != null) {
            listItemBeans.addAll(mPostionlistItems);
        }
    }

    public void setPageInterface(ChangePageInterface pageInterface) {
        this.pageInterface = pageInterface;
    }

    public String kmTom(double km) {
        if (km < 1) {
            return (int) (km * 1000) + "m";
        } else {
            return km + "KM";
        }
    }
}