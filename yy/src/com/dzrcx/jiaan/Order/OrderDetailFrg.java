package com.dzrcx.jiaan.Order;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.OrderDetailBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单详情页
 */
public class OrderDetailFrg extends YYBaseFragment implements
        OnClickListener, RequestInterface {
    private ImageView carImage,iv_left_raw;
    private TextView  barTitle, orderState, carName, carCode;
    private TextView orderPlace, orderTimeholder, orderCostTime, orderBenefit,
            orderCostMileage, orderGetTimes, orderBillintTime, orderBookTime,
            orderBackTime, orderPayTime, orderCostCount;
    private String orderId;
    private View OrderDetailView;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (OrderDetailView == null) {
            OrderDetailView = inflater.inflate(R.layout.aty_orderdetail, null);
            initView();
            parseIntent();
            if (NetHelper.checkNetwork(mContext)) {
                showNoNetDlg();
                MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
                return OrderDetailView;
            }
            requestData();
        }
        return OrderDetailView;
    }

    private void initView() {
        iv_left_raw = (ImageView) OrderDetailView.findViewById(R.id.iv_left_raw);
        iv_left_raw.setVisibility(View.VISIBLE);
        barTitle = (TextView) OrderDetailView.findViewById(R.id.tv_title);
        barTitle.setText("订单详情");
        carImage = (ImageView) OrderDetailView.findViewById(R.id.orderdetail_car_image);
        orderState = (TextView) OrderDetailView.findViewById(R.id.orderdetail_state_tv);
        carName = (TextView) OrderDetailView.findViewById(R.id.orderdetail_car_name);
        carCode = (TextView) OrderDetailView.findViewById(R.id.orderdetail_car_code);

        orderPlace = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_place_tv);
        orderTimeholder = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_jishi_tv);
        orderCostTime = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_shizufeiyong_tv);
        orderCostMileage = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_lichengfeiyong_tv);
        orderBenefit = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_youhui_tv);
        orderGetTimes = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_qucheshijian_tv);
        orderBillintTime = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_jifeishijian_tv);
        orderBookTime = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_xiadanshijian_tv);
        orderBackTime = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_huancheshijian_tv);
        orderPayTime = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_zhifushijian_tv);
        orderCostCount = (TextView) OrderDetailView.findViewById(R.id.orderdetail_info_feiyongzongji_tv);

        int wid = MyUtils.getScreenWidth(mContext);
        int hei = (int) (wid * 1.0 * 9 / 16);
        // int hei = (int) (wid * 1.0 * 422 / 750);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                wid, hei);
        carImage.setLayoutParams(layoutParams);
        iv_left_raw.setOnClickListener(this);
    }

    private void parseIntent() {
        orderId = intent.getStringExtra("orderId");
    }

    private void requestData() {

        if (!TextUtils.isEmpty(orderId)) {
            dialogShow();
            Map<String, String> wxparams = new HashMap<String, String>();
            wxparams.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            wxparams.put("orderId", orderId);
            YYRunner.getData(1002, YYRunner.Method_POST, YYUrl.GETORDEDETAIL,
                    wxparams, this);

        } else {
            MyUtils.showToast(mContext, "数据错误");
        }

    }

    /**
     * 展示数据
     */
    private void showData(OrderDetailBean orderDetailBean) {

        ImageLoader.getInstance().displayImage(orderDetailBean.getReturnContent().getCarThumb(),
                carImage);

        switch (orderDetailBean.getReturnContent().getOrderState()) {
            case 1:
                orderState.setText("等待取车");
                break;
            case 2:
                orderState.setText("进行中");
                break;
            case 3:
                orderState.setText("支付租金");
                break;
            case 4:
                orderState.setText("已完成");
                break;
            case 5:
                orderState.setText("已取消");
                break;
            default:
                orderState.setText(null);
                break;
        }

        carName.setText(orderDetailBean.getReturnContent().getMake());
        carName.append(" ");
        carName.append(orderDetailBean.getReturnContent().getModel());
        carCode.setText("车牌号码：");
        carCode.append(orderDetailBean.getReturnContent().getLicense());

        orderPlace.setText(orderDetailBean.getReturnContent().getParkLocAddress());
        orderTimeholder.setText(TimeDateUtil.DHMTransform((int) orderDetailBean
                .getReturnContent().getFeeDetail().getCostTime()));
        orderCostTime.setText(MyUtils.formatPriceShort(orderDetailBean.getReturnContent().getFeeDetail().getHourPrice())
                + "元");
        orderCostMileage.setText(MyUtils.formatPriceShort(orderDetailBean.getReturnContent().getFeeDetail()
                .getDistancePrice()) + "元");
        orderBenefit.setText(MyUtils.formatPriceShort(orderDetailBean.getReturnContent().getFeeDetail()
                .getBenefitPrice()) + "元");
        orderGetTimes.setText(TimeDateUtil.MDHMTransform(orderDetailBean
                .getReturnContent().getFeeDetail().getPickcarTime()));
        orderBillintTime.setText(TimeDateUtil.MDHMTransform(orderDetailBean
                .getReturnContent().getFeeDetail().getChargeTime()));
        orderBookTime.setText(TimeDateUtil.MDHMTransform(orderDetailBean
                .getReturnContent().getFeeDetail().getOrderTime()));
        orderBackTime.setText(TimeDateUtil.MDHMTransform(orderDetailBean
                .getReturnContent().getFeeDetail().getReturnTime()));
        orderPayTime.setText(TimeDateUtil.MDHMTransform(orderDetailBean
                .getReturnContent().getFeeDetail().getPayTime()));
        orderCostCount.setText(MyUtils.formatPriceShort(orderDetailBean.getReturnContent().getFeeDetail().getTotalPrice())
                + "元");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(int tag, String error) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }

    @Override
    public void onComplete(int tag, String json) {
        // TODO Auto-generated method stub
        progresssDialog.dismiss();
        OrderDetailBean orderDetailBean = (OrderDetailBean) GsonTransformUtil
                .fromJson(json, OrderDetailBean.class);
        if (orderDetailBean != null && orderDetailBean.getErrno() == 0) {
            showData(orderDetailBean);
        } else {
            MyUtils.showToast(mContext, "数据错误");
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoading(long count, long current) {
        // TODO Auto-generated method stub

    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

}