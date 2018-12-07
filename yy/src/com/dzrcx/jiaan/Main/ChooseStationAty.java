package com.dzrcx.jiaan.Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dzrcx.jiaan.Bean.CreateOrderVO;
import com.dzrcx.jiaan.Bean.StationListBean;
import com.dzrcx.jiaan.Bean.StationVo;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 16-5-9.
 * 选择站点
 */
public class ChooseStationAty extends YYBaseActivity implements View.OnClickListener, RequestInterface {

    private FrameLayout contentLayout;
    private ImageView changeView;
    private ChooseStationMapFrag chooseStationMapFrag;
    private ChooseStationFrag chooseStationFrag;

    private CreateOrderVO createOrderVO;

    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFinish = false;
        setContentView(R.layout.aty_choose_station);
        contentLayout = (FrameLayout) findViewById(R.id.base_contextlayout);
        changeView = (ImageView) findViewById(R.id.iv_changebutton);
        changeView.setOnClickListener(this);
        changeToFragment(0);
        changeToFragment(1);
        changeView.setTag(new Object());
        changeView.setImageResource(R.drawable.changetomap);
        if (getIntent().getExtras() != null) {
            createOrderVO = (CreateOrderVO) getIntent().getExtras().getSerializable("CreateOrderVO");
        }
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getStationS();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_changebutton:
                Object c = v.getTag();
                if (c == null) {
                    changeToFragment(1);
                    v.setTag(new Object());
                    changeView.setImageResource(R.drawable.changetomap);
                } else {
                    changeToFragment(0);
                    v.setTag(null);
                    changeView.setImageResource(R.drawable.changetocarlist);
                }


                break;
        }
    }


    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(this, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        dismmisDialog();
        if (isFinish) {
            return;
        }
        switch (tag) {
            case 1001:
                StationListBean stationListBean = (StationListBean) GsonTransformUtil.fromJson(json, StationListBean.class);
                if (stationListBean != null && stationListBean.getErrno() == 0) {
                    ((ChooseStationFrag) fragments.getItem(1)).showData(stationListBean);
                    ((ChooseStationMapFrag) fragments.getItem(0)).showData(stationListBean);
                } else {
                    MyUtils.showToast(this, stationListBean == null ? "数据传输错误，请重试" : stationListBean.getError());
                }
                break;
        }
    }

    @Override
    public void onLoading(long count, long current) {

    }

    @Override
    protected void onDestroy() {
        isFinish = true;
        super.onDestroy();
    }

    private void getStationS() {
        if (createOrderVO != null && createOrderVO.getStayType() == 1) {
            StationListBean stationListBean = new StationListBean();
            ArrayList<HashMap<String, ArrayList<StationVo>>> returnContent = new ArrayList<HashMap<String, ArrayList<StationVo>>>();
            HashMap<String, ArrayList<StationVo>> listHashMap = new HashMap<String, ArrayList<StationVo>>();
            ArrayList<StationVo> stationVos = new ArrayList<StationVo>();
            StationVo stationVo = new StationVo();
            stationVo.setId(createOrderVO.getFromStationId());
            stationVo.setAddress(createOrderVO.getFromStationAddress());
            stationVo.setName(createOrderVO.getFromStationName());
            stationVo.setDistance(createOrderVO.getDistance());
            stationVo.setParkingNum(1);//A-A肯定有车位还车
            stationVo.setLatitude(createOrderVO.getLat());
            stationVo.setLongitude(createOrderVO.getLng());
            stationVos.add(stationVo);
            listHashMap.put("附近", stationVos);
            returnContent.add(listHashMap);
            stationListBean.setReturnContent(returnContent);
            stationListBean.setErrno(0);
            onComplete(1001, GsonTransformUtil.toJson(stationListBean));

        } else {
            if (NetHelper.checkNetwork(this)) {
                MyUtils.showToast(this, "网络异常，请检查网络连接或重试");
                return;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            params.put("lng", YYApplication.Longitude + "");
            params.put("lat", YYApplication.Latitude + "");
            params.put("stayType", "2");
            dialogShow();
            YYRunner.getData(1001, YYRunner.Method_POST,
                    YYUrl.GETSTATIONBYSTAYTYPE, params, this);
        }
    }


    /**
     * 改变到某一个fragment
     * 在fragment界面需要添加setMenuVisibility方法
     *
     * @param index 0=map页面 ，1=列表页面
     */
    public void changeToFragment(int index) {

        Fragment fragment = (Fragment) fragments.instantiateItem(contentLayout,
                index);
        fragments.setPrimaryItem(contentLayout, 0, fragment);
        fragments.finishUpdate(contentLayout);
    }


    private FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(
            getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (chooseStationMapFrag == null) {
                        chooseStationMapFrag = new ChooseStationMapFrag();
                    }
                    return chooseStationMapFrag;
                case 1:
                    if (chooseStationFrag == null) {
                        chooseStationFrag = new ChooseStationFrag();
                    }
                    return chooseStationFrag;
            }
            return null;
        }
    };
}
