package com.dzrcx.jiaan.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.widget.wheel.widget.CityModel;
import com.dzrcx.jiaan.widget.wheel.widget.DistrictModel;
import com.dzrcx.jiaan.widget.wheel.widget.OnWheelChangedListener;
import com.dzrcx.jiaan.widget.wheel.widget.ProvinceModel;
import com.dzrcx.jiaan.widget.wheel.widget.WheelView;
import com.dzrcx.jiaan.widget.wheel.widget.XmlParserHandler;
import com.dzrcx.jiaan.widget.wheel.widget.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ChooseAreaDialog extends Dialog implements OnClickListener,
        OnWheelChangedListener {
    private WheelView provinceWheelView;
    private WheelView cityWheelView;
    private WheelView countyWheelView;
    private TextView tv_sure, tv_cancel;
    private ChooseAreaListener chooseAreaListener;
    private Context mContext;
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    public ChooseAreaDialog(Context context) {
        super(context, R.style.AreaDialog);
        this.mContext = context;
        initProvinceDatas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_choose_area);
        initView();
        setUpListener();
        setUpData();
    }

    private void initView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.dialog_layout);
        layout.getLayoutParams().width = MyUtils.getScreenWidth(mContext);
        provinceWheelView = (WheelView) findViewById(R.id.wv_area_province);
        cityWheelView = (WheelView) findViewById(R.id.wv_area_city);
        countyWheelView = (WheelView) findViewById(R.id.wv_area_county);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    public ChooseAreaListener getChooseAreaListener() {
        return chooseAreaListener;
    }

    public void setChooseAreaListener(ChooseAreaListener chooseAreaListener) {
        this.chooseAreaListener = chooseAreaListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_sure:
                if (chooseAreaListener != null) {
                    int pCurrent = provinceWheelView.getCurrentItem();
                    String proviceName = mProvinceDatas[pCurrent];
                    String[] cities = mCitisDatasMap.get(proviceName);
                    int cityCurrent = cityWheelView.getCurrentItem();
                    String city = cities[cityCurrent];
                    String[] countys = mDistrictDatasMap.get(city);
                    int countyCurrent = countyWheelView.getCurrentItem();
                    String county = countys[countyCurrent];
                    chooseAreaListener.onChooseBack(proviceName, city, county);
                }
                break;
        }
        dismiss();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
    }

    private void setUpListener() {
        // 添加change事件
        provinceWheelView.addChangingListener(this);
        // 添加change事件
        cityWheelView.addChangingListener(this);
//        添加change事件
        countyWheelView.addChangingListener(this);
        // 添加onclick事件
        // mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        // initProvinceDatas();
        provinceWheelView.setViewAdapter(new ArrayWheelAdapter<String>(mContext
                , mProvinceDatas));
        // 设置可见条目数量
        provinceWheelView.setVisibleItems(7);
        cityWheelView.setVisibleItems(7);
        countyWheelView.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub

        if (wheel == provinceWheelView) {
            updateCities();
        } else if (wheel == cityWheelView) {
            updateAreas();
        }

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = provinceWheelView.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
            mCurrentCityName = "";
        } else {
            mCurrentCityName = cities[0];
        }
        cityWheelView.setViewAdapter(new ArrayWheelAdapter<String>(
                mContext, cities));
        cityWheelView.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的city，更新市WheelView的信息
     */
    private void updateAreas() {
        int cCurrent = cityWheelView.getCurrentItem();
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
            mCurrentCityName = "";
        } else {
            mCurrentCityName = cities[cCurrent];
        }
        String[] countys = mDistrictDatasMap.get(mCurrentCityName);
        if (countys == null) {
            countys = new String[]{""};
            mCurrentDistrictName = "";
        } else {
            mCurrentDistrictName = countys[0];
        }
        countyWheelView.setViewAdapter(new ArrayWheelAdapter<String>(
                mContext, countys));
        countyWheelView.setCurrentItem(0);
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    // mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];

//                if (cityNames.length == 1) {
//                    for (int j = 0; j < cityList.size(); j++) {
//                        // 遍历省下面的所有市的数据
//                        cityNames[j] = cityList.get(j).getName();
//                        List<DistrictModel> districtList = cityList.get(j)
//                                .getDistrictList();
//                        String[] distrinctNameArray = new String[districtList
//                                .size()];
//                        DistrictModel[] distrinctArray = new DistrictModel[districtList
//                                .size()];
//                        for (int k = 0; k < districtList.size(); k++) {
//                            // 遍历市下面所有区/县的数据
//                            DistrictModel districtModel = new DistrictModel(
//                                    districtList.get(k).getName(), districtList
//                                    .get(k).getZipcode());
//                            // 区/县对于的邮编，保存到mZipcodeDatasMap
//                            mZipcodeDatasMap.put(districtList.get(k).getName(),
//                                    districtList.get(k).getZipcode());
//                            distrinctArray[k] = districtModel;
//                            distrinctNameArray[k] = districtModel.getName();
//                        }
//                        // 市-区/县的数据，保存到mDistrictDatasMap
//                        mDistrictDatasMap.put(cityNames[j],
//                                distrinctNameArray);
//                        mCitisDatasMap.put(provinceList.get(i).getName(),
//                                distrinctNameArray);
//                    }
//                } else {
                // 省-市的数据，保存到mCitisDatasMap

                for (int j = 0; j < cityList.size(); j++) {
                    cityNames[j] = cityList.get(j).getName();

                    List<DistrictModel> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(
                                districtList.get(k).getName(), districtList
                                .get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j],
                            distrinctNameArray);
//                        mCitisDatasMap.put(provinceList.get(i).getName(),
//                                distrinctNameArray);
                }

                mCitisDatasMap
                        .put(provinceList.get(i).getName(), cityNames);
            }

//            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public interface ChooseAreaListener {
        void onChooseBack(String province, String city, String county);
    }

}
