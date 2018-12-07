package com.dzrcx.jiaan.Main;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.MapcarListReContentVo;
import com.dzrcx.jiaan.Bean.MapcarListStationAndCarsVo;
import com.dzrcx.jiaan.Bean.CarListItemBean;
import com.dzrcx.jiaan.Bean.MapcarfrgOprateTimeReContentVo;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.YYApplication;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.PinnedSectionListView;
import com.dzrcx.jiaan.widget.YYNotdataView;
import com.dzrcx.jiaan.yyinterface.ChangePageInterface;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆及站点列表
 */
public class CarListFrg extends YYBaseFragment implements
        OnClickListener, YYApplication.OnGetLocationlistener, RequestInterface {
    private View carListFrgView;
    private PullToRefreshScrollView pull_refresh_scrollview;//外层scrollview
    private PinnedSectionListView pinnedSectionListView;//N内层分组悬浮listview
    private CarListPinnedSectionListViewAdp listViewAdapter;
    private List<CarListItemBean> carListItemGTZEROBeans;//纯有车站点集合
    private List<CarListItemBean> carListItemLTZEROBeans;//混排站点集合
    private MainActivity2_1 activity2_1;
    private ImageView iv_leftraw;
    private TextView tv_title, tv_right;
    private ChangePageInterface pageInterface;
    private YYNotdataView notdataView;
    private double Longitude;
    private double Latitude;
    private static final int TAG_GETNEARSTATIONCARLIST = 11001;
    private static final int TAG_GETDOCUMENT = 11002;
    private static final int TAG_GETOPRATEREMAINTIME = 11003;
    private int pageNum = 1;
    private boolean isDownRefresh;//是否下拉刷新
    private boolean isHasCar = true;//是否是纯有车列表
    private Dialog remindTimeDlg;//提醒时间dialog
    private CarListItemBean carListItemBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            carListItemBean = (CarListItemBean) msg.obj;
            if (msg.what == 0) {
                requestTimes(true);
            } else if (msg.what == 1) {
                requestOprateremaintime(0, "", true);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        carListFrgView = inflater.inflate(R.layout.frg_carlist2, null);
        initView();
        dialogShow();
        requestLocationData();
        return carListFrgView;
    }

    /**
     * 请求定位数据
     */
    public void requestLocationData() {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            pull_refresh_scrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_scrollview.onRefreshComplete();
                }
            }, 50);
            showNullData("网络异常，请检查网络连接或重试");
            return;
        }
        if (YYApplication.mLocationClient.isStarted())
            YYApplication.mLocationClient.requestLocation();
        else {
            YYApplication.mLocationClient.start();
            YYApplication.mLocationClient.requestLocation();
        }
    }

    @Override
    public void onStop() {
        pull_refresh_scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_scrollview.onRefreshComplete();
            }
        }, 50);
        super.onStop();
    }

    private void initView() {
        // TODO Auto-generated method stub
        /**
         * 标题栏信息
         */
        tv_title = (TextView) carListFrgView.findViewById(R.id.tv_title);
        tv_title.setText("附近站点");
        iv_leftraw = (ImageView) carListFrgView.findViewById(R.id.iv_left_raw);
        tv_right = (TextView) carListFrgView.findViewById(R.id.tv_right);
        tv_right.setText("隐藏无车站点");
        if (isHasCar) {
            tv_right.setText("显示全部站点");
        }
        iv_leftraw.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);

        pull_refresh_scrollview = (PullToRefreshScrollView) carListFrgView
                .findViewById(R.id.pull_refresh_scroll);
        pinnedSectionListView = (PinnedSectionListView) carListFrgView
                .findViewById(R.id.pinnedSectionListView);
        pinnedSectionListView.setShadowVisible(false);
        pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        /**
         * 添加listview 头部信息
         */
        notdataView = new YYNotdataView(mContext, 70);
        pinnedSectionListView.addHeaderView(notdataView);
        /**
         * 填充数据
         */
        carListItemGTZEROBeans = new ArrayList<>();
        carListItemLTZEROBeans = new ArrayList<>();
        if (isHasCar) {//有车站点
            listViewAdapter = new CarListPinnedSectionListViewAdp(mContext, carListItemGTZEROBeans, activity2_1, handler);
        } else {//混排站点
            listViewAdapter = new CarListPinnedSectionListViewAdp(mContext, carListItemLTZEROBeans, activity2_1, handler);
        }
        pinnedSectionListView.setAdapter(listViewAdapter);
        pinnedSectionListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (firstVisibleItem == 0 && getScrollY(view) == 0) {
                    pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else if (isListViewReachBottomEdge(view)) {
                    pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                } else {
                    pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }
        });
        listViewAdapter.setPageInterface(pageInterface);
        String msg = "您附近所有车辆已全部出租\n" +
                "过会再来看看吧!";
        showNullData(msg);
        pull_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                requestLocationData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                if (listViewAdapter.getCount() == 0) {
                    pull_refresh_scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pull_refresh_scrollview.onRefreshComplete();
                        }
                    }, 50);
                    return;
                }
                requestStationData(pageNum + 1);
            }
        });
        MyUtils.setViewsOnClick(this, iv_leftraw, tv_right);
    }

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    /**
     * 若要使fragment切换界面，该方法必须要有
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        // TODO Auto-generated method stub
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(
                    menuVisible ? getView().VISIBLE : getView().GONE);
        }
        if (menuVisible) {
            YYApplication.setLocationIFC(this);//注册定位回调接口
        } else {
            if (pull_refresh_scrollview != null) {
                pull_refresh_scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_refresh_scrollview.onRefreshComplete();
                    }
                }, 50);
            }
            YYApplication.setLocationIFC(null);//注册定位回调接口
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity2_1 = (MainActivity2_1) activity;
    }

    private void showNullData(String msg) {
        if (listViewAdapter.getCount() == 0) {
            notdataView.setVisible(true, msg);
        } else {
            notdataView.setVisible(false);
        }
    }

    /**
     * 显示跳转订单界面的快捷入口动画
     *
     * @param v
     * @param isShow
     */
    private void showImageAnim(ImageView v, boolean isShow) {

        if (isShow) {
            AnimationDrawable frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.to_order_anim);
            v.setBackgroundDrawable(frameAnim);
            v.setVisibility(View.VISIBLE);
            frameAnim.start();
        } else {
            AnimationDrawable frameAnim = (AnimationDrawable) v.getBackground();
            if (frameAnim != null) {
                frameAnim.stop();
            }
            v.setVisibility(View.GONE);
            v.setBackgroundDrawable(null);
        }


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left_raw:
                activity2_1.changeToFragment(1);
                break;
            case R.id.tv_right:
                isHasCar = !isHasCar;
                tv_right.setText("隐藏无车站点");
                if (isHasCar) {
                    tv_right.setText("显示全部站点");
                }
                fillListData();
                break;
            default:
                break;
        }
    }

    public void setPageInterface(ChangePageInterface pageInterface) {
        this.pageInterface = pageInterface;
    }

    @Override
    public void getBdlocation(BDLocation location) {
        if (location != null) {
            if (location.getLocType() == BDLocation.TypeNetWorkLocation
                    || location.getLocType() == BDLocation.TypeOffLineLocation
                    || location.getLocType() == BDLocation.TypeGpsLocation) {
                Longitude = YYApplication.Longitude;
                Latitude = YYApplication.Latitude;
                if (activity2_1.isTopActivity()) {
                    requestStationData(1);
                }
            } else {
                pull_refresh_scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_refresh_scrollview.onRefreshComplete();
                    }
                }, 50);
                dismmisDialog();
                showNullData("定位失败，请重试");
            }
        } else {
            pull_refresh_scrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_scrollview.onRefreshComplete();
                }
            }, 50);
            dismmisDialog();
            showNullData("定位失败，请重试");
        }
    }

    /**
     * 请求地图站点数量位置信息
     */
    public void requestStationData(int pageNum) {
        if (NetHelper.checkNetwork(mContext)) {
            pull_refresh_scrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull_refresh_scrollview.onRefreshComplete();
                }
            }, 50);
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (pageNum == 1) {
            isDownRefresh = true;
        } else {
            isDownRefresh = false;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("lng", Longitude + "");
        params.put("lat", Latitude + "");
        params.put("pageNum", pageNum + "");
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        YYRunner.getData(TAG_GETNEARSTATIONCARLIST, YYRunner.Method_POST,
                YYUrl.GETNEARSTATIONCARLIST, params, this);
    }

    @Override
    public void onError(int tag, String error) {
        pull_refresh_scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull_refresh_scrollview.onRefreshComplete();
            }
        }, 50);
        showNullData("数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        switch (tag) {
            case TAG_GETNEARSTATIONCARLIST:
                pull_refresh_scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pull_refresh_scrollview.onRefreshComplete();
                    }
                }, 50);
                dismmisDialog();
                MapcarListReContentVo mapcarListReContentVo = (MapcarListReContentVo) GsonTransformUtil
                        .fromJson(json, MapcarListReContentVo.class);
                if (mapcarListReContentVo != null && mapcarListReContentVo.getErrno() != 0) {
                    isDownRefresh = !isDownRefresh;
                    showNullData(mapcarListReContentVo.getError());
                } else {
                    if (isDownRefresh) {
                        pageNum = 1;
                        carListItemGTZEROBeans = getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountGTZERO());
                        carListItemLTZEROBeans = getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountLTZERO());
                    } else {
                        if (isHasCar) {
                            if (mapcarListReContentVo.getReturnContent().getStationCarCountGTZERO().size() == 0) {
                                MyUtils.showToast(mContext, "已经没有更多数据了");
                            } else {
                                pageNum++;
                                carListItemGTZEROBeans.addAll(getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountGTZERO()));
                                carListItemLTZEROBeans.addAll(getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountLTZERO()));
                            }
                        } else {
                            if (mapcarListReContentVo.getReturnContent().getStationCarCountLTZERO().size() == 0) {
                                MyUtils.showToast(mContext, "已经没有更多数据了");
                            } else {
                                pageNum++;
                                carListItemGTZEROBeans.addAll(getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountGTZERO()));
                                carListItemLTZEROBeans.addAll(getCarListItemBeans(mapcarListReContentVo.getReturnContent().getStationCarCountLTZERO()));
                            }
                        }
                    }
                    fillListData();
                }
                break;
            case TAG_GETDOCUMENT:
                dismmisDialog();
                YYBaseResBean baseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (baseResBean == null || baseResBean.getErrno() != 0) {
                    MyUtils.showToast(mContext, baseResBean.getError());
                } else {
                    String[] strArray = null;
                    strArray = baseResBean.getError().split(","); //拆分字符为"," ,然后把结果交给数组strArray
                    showRemindTimeDialog(strArray);
                }
                break;
            case TAG_GETOPRATEREMAINTIME:
                dismmisDialog();
                MapcarfrgOprateTimeReContentVo mapcarfrgOprateTimeReContentVo = (MapcarfrgOprateTimeReContentVo) GsonTransformUtil.fromJson(json, MapcarfrgOprateTimeReContentVo.class);
                if (mapcarfrgOprateTimeReContentVo == null || mapcarfrgOprateTimeReContentVo.getErrno() != 0) {
                    MyUtils.showToast(mContext, (mapcarfrgOprateTimeReContentVo == null ? "数据传输错误,请重试" : mapcarfrgOprateTimeReContentVo.getError()));
                } else {
                    if (mapcarfrgOprateTimeReContentVo.getReturnContent().getStationId() != this.carListItemBean.getMapcarListStationAndCarsVo().getStationId()) {
                        return;
                    }
                    for (CarListItemBean carListItemBean : carListItemLTZEROBeans) {
                        if (this.carListItemBean != null && carListItemBean.getMapcarListStationAndCarsVo() != null && this.carListItemBean.getMapcarListStationAndCarsVo().getStationId() == carListItemBean.getMapcarListStationAndCarsVo().getStationId()) {
                            carListItemBean.getMapcarListStationAndCarsVo().setFlag(mapcarfrgOprateTimeReContentVo.getReturnContent().getFlag());
                            break;
                        }
                    }
                    fillListData();
                }
                break;
        }
    }

    /**
     * 请求地图站点数量位置信息
     * 1提醒   ；0   取消
     *
     * @param isShowDialog
     */

    public void requestOprateremaintime(final int flag, final String time, final boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("remainTime", time);
        params.put("flag", flag + "");
        params.put("stationId", carListItemBean.getMapcarListStationAndCarsVo().getStationId() + "");
        YYRunner.getData(TAG_GETOPRATEREMAINTIME, YYRunner.Method_POST,
                YYUrl.GETOPRATEREMAINTIME, params, this);
    }

    /**
     * 展示提醒时间dialog
     */
    public void showRemindTimeDialog(final String[] strArray) {
        if (remindTimeDlg == null) {
            remindTimeDlg = new Dialog(mContext, R.style.MyDialog);
        }
        View mDlgCallView = LayoutInflater.from(mContext).inflate(
                R.layout.dlg_mapcarfrg_remindtime, null);
        final LinearLayout ll_time1 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time1);
        final LinearLayout ll_time2 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time2);
        final LinearLayout ll_time3 = (LinearLayout) mDlgCallView.findViewById(R.id.ll_time3);
        TextView tv_time1 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time1);
        TextView tv_time2 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time2);
        TextView tv_time3 = (TextView) mDlgCallView
                .findViewById(R.id.tv_time3);
        tv_time1.setText(strArray[0] + "小时");
        ll_time1.setTag(strArray[0]);
        tv_time2.setText(strArray[1] + "小时");
        ll_time2.setTag(strArray[1]);
        tv_time3.setText(strArray[2] + "小时");
        ll_time3.setTag(strArray[2]);
        final TextView tv_left_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_left_txt);
        TextView tv_right_txt = (TextView) mDlgCallView
                .findViewById(R.id.tv_right_txt);
        ll_time1.setSelected(true);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_time1:
                    case R.id.ll_time2:
                    case R.id.ll_time3:
                        reSetSelectedData(v);
                        break;
                    case R.id.tv_left_txt:
                        remindTimeDlg.dismiss();
                        break;
                    case R.id.tv_right_txt:
                        if (carListItemBean == null) {
                            remindTimeDlg.dismiss();
                        }
                        if (ll_time1.isSelected()) {
                            requestOprateremaintime(1, ll_time1.getTag() + "", false);
                        } else if (ll_time2.isSelected()) {
                            requestOprateremaintime(1, ll_time2.getTag() + "", false);
                        } else if (ll_time3.isSelected()) {
                            requestOprateremaintime(1, ll_time3.getTag() + "", false);
                        } else {
                            MyUtils.showToast(mContext, "请选择提醒时间");
                            break;
                        }
                        remindTimeDlg.dismiss();
                        break;
                }
            }

            private void reSetSelectedData(View v) {
                ll_time1.setSelected(false);
                ll_time2.setSelected(false);
                ll_time3.setSelected(false);
                v.setSelected(true);
            }
        };
        MyUtils.setViewsOnClick(onClickListener, ll_time1, ll_time2, ll_time3, tv_left_txt, tv_right_txt);
        remindTimeDlg.setCanceledOnTouchOutside(false);
        remindTimeDlg.setContentView(mDlgCallView);
        remindTimeDlg.show();
        Window dlgWindow = remindTimeDlg.getWindow();
        WindowManager.LayoutParams lp = dlgWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = MyUtils.getScreenWidth(mContext) / 10 * 8;
        dlgWindow.setAttributes(lp);
    }

    private void fillListData() {
        if (isHasCar) {
            listViewAdapter.replaceData(carListItemGTZEROBeans);
        } else {
            listViewAdapter.replaceData(carListItemLTZEROBeans);
        }
        listViewAdapter.notifyDataSetChanged();//相同的索引不用调用replace方法换数据
        showNullData("您附近所有车辆已全部出租\n" +
                "过会再来看看吧!");
    }

    /**
     * 转化成带站点标签的数据集
     *
     * @param carClassBeans
     */
    private ArrayList<CarListItemBean> getCarListItemBeans(List<MapcarListStationAndCarsVo> carClassBeans) {
        ArrayList<CarListItemBean> carListItemBeans = new ArrayList<>();
        for (MapcarListStationAndCarsVo mapcarListStationAndCarsVo : carClassBeans) {
            carListItemBeans.add(new CarListItemBean(YYConstans.TAG_SECTIONBG));//背景
            if (mapcarListStationAndCarsVo != null && mapcarListStationAndCarsVo.getCarCount() > 0) {//有站点有车;
                carListItemBeans.add(new CarListItemBean(YYConstans.TAG_SECTION, mapcarListStationAndCarsVo));
                if (mapcarListStationAndCarsVo.getCarList() == null) {
                    break;
                }
                for (int i = 0; i < mapcarListStationAndCarsVo.getCarList().size(); i++) {
                    carListItemBeans.add(new CarListItemBean(YYConstans.TAG_HASCARSECTION, i, mapcarListStationAndCarsVo));//添加车辆
                }
            } else if (mapcarListStationAndCarsVo != null && mapcarListStationAndCarsVo.getCarCount() <= 0) {//有站点无车
                carListItemBeans.add(new CarListItemBean(YYConstans.TAG_SECTION, mapcarListStationAndCarsVo));
                carListItemBeans.add(new CarListItemBean(YYConstans.TAG_NOCARSECTION, mapcarListStationAndCarsVo));//添加车辆
            } else {
            }
        }
        return carListItemBeans;
    }

    @Override
    public void onLoading(long count, long current) {

    }

    /**
     * @param isShowDialog
     */
    private void requestTimes(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            dismmisDialog();
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog)
            dialogShow();
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "remainTime");
        map.put("lng", YYApplication.Longitude + "");
        map.put("lat", YYApplication.Latitude + "");
        YYRunner.getData(TAG_GETDOCUMENT, YYRunner.Method_POST, YYUrl.GETDOCUMENT, map,
                this);
    }
}
