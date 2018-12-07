package com.dzrcx.jiaan.Order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.OrderDetailVo;
import com.dzrcx.jiaan.Bean.OrderListBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Bean.ShareBeanReturnContent;
import com.dzrcx.jiaan.Bean.ShareContentBean;
import com.dzrcx.jiaan.Bean.ShareContentBeanReturnContent;
import com.dzrcx.jiaan.Bean.YYBaseResBean;
import com.dzrcx.jiaan.Constans.YYConstans;
import com.dzrcx.jiaan.Constans.YYUrl;
import com.dzrcx.jiaan.Main.MainActivity2_1;
import com.dzrcx.jiaan.Main.RunDetailAty;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;
import com.dzrcx.jiaan.clicklistener.RequestInterface;
import com.dzrcx.jiaan.clicklistener.WXShareCallBackListener;
import com.dzrcx.jiaan.tools.GsonTransformUtil;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.NetHelper;
import com.dzrcx.jiaan.tools.ShareUtils;
import com.dzrcx.jiaan.tools.SharedPreferenceTool;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.tools.YYRunner;
import com.dzrcx.jiaan.widget.NoScrollGridView;
import com.dzrcx.jiaan.widget.ShareDialog3;
import com.dzrcx.jiaan.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 16-5-10.
 * 评价我们
 */
public class RatedUsFrag extends YYBaseFragment implements View.OnClickListener, RequestInterface, WXShareCallBackListener {


    private View contentView;
    private TextView titleView;
    private ImageView iv_left_raw;
    private NoScrollGridView massagegridview;
    private String[] splits;
    private RateUsAdp rateUsAdp;


    private ImageView carImage;
    private TextView carMode, carNo, spendTime, mileageSum;

    private TextView orderPrice, orderPriceDetail;
    private ImageView start0, start1, start2, start3, start4;

    private TextView submit;
    private ShareContentBean shareContentBean;
    private String orderId;
    private String money;
    private ShareDialog3 shareDialog3;

    private ShareBeanReturnContent shareBeanReturnContent;

    private HashMap<Integer, Boolean> isSelected;
    private OrderDetailVo orderDetailVo;

    private int mark = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.frag_ratedus, null);
            initView();
            requestData(true);
            //if (!SharedPreferenceTool.getPrefBoolean(mContext, "RatedUsFrag", false)) {
            requestURLData(1001, true);
            //}
            WXEntryActivity.setShareCallBackListener(this);
        }

        return contentView;
    }

    /**
     * 请求分享信息
     *
     * @param isShowDialog
     */
    public void requestURLData(int tag, boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("inviteCode", YYConstans.getUserInfoBean().getReturnContent().getUser().getInviteCode());
        YYRunner.getData(tag, YYRunner.Method_POST,
                YYUrl.GETSHAREURL, params, this);
    }

    /**
     * @param isShowDialog
     */
    public void requestData(boolean isShowDialog) {
        if (NetHelper.checkNetwork(mContext)) {
            MyUtils.showToast(mContext, "网络异常，请检查网络连接或重试");
            return;
        }
        if (isShowDialog) {
            dialogShow();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "user_invite");
        YYRunner.getData(YYConstans.TAG_GETACTIVITYCONTENT, YYRunner.Method_POST,
                YYUrl.GETACTIVITYCONTENT, params, this);
    }

    private void initView() {
        iv_left_raw = (ImageView) contentView.findViewById(R.id.iv_left_raw);
        titleView = (TextView) contentView.findViewById(R.id.tv_title);
        massagegridview = (NoScrollGridView) contentView.findViewById(R.id.massagegridview);
        splits = setSpitlot();
        isSelected = new HashMap<>();
        for (int i = 0; i < splits.length; i++) {
            if (i == 0)
                isSelected.put(i, true);
            else
                isSelected.put(i, false);
        }
        rateUsAdp = new RateUsAdp(mContext, splits, isSelected);
        massagegridview.setAdapter(rateUsAdp);
        carImage = (ImageView) contentView.findViewById(R.id.rated_car_image);
        carMode = (TextView) contentView.findViewById(R.id.rated_carmode);
        carNo = (TextView) contentView.findViewById(R.id.rated_carno);
        spendTime = (TextView) contentView.findViewById(R.id.rated_spendtime);
        mileageSum = (TextView) contentView.findViewById(R.id.ratedr_left);


        orderPrice = (TextView) contentView.findViewById(R.id.rated_pay_count);
        orderPriceDetail = (TextView) contentView.findViewById(R.id.rated_pay_detail);


        start0 = (ImageView) contentView.findViewById(R.id.rated_star0);
        start1 = (ImageView) contentView.findViewById(R.id.rated_star1);
        start2 = (ImageView) contentView.findViewById(R.id.rated_star2);
        start3 = (ImageView) contentView.findViewById(R.id.rated_star3);
        start4 = (ImageView) contentView.findViewById(R.id.rated_star4);


        submit = (TextView) contentView.findViewById(R.id.rated_button);

        iv_left_raw.setVisibility(View.VISIBLE);
        iv_left_raw.setOnClickListener(this);
        titleView.setText("支付完成");


        orderPriceDetail.setOnClickListener(this);

        start0.setOnClickListener(this);
        start1.setOnClickListener(this);
        start2.setOnClickListener(this);
        start3.setOnClickListener(this);
        start4.setOnClickListener(this);
        submit.setOnClickListener(this);


        if (getActivity().getIntent().getExtras() != null) {
            orderDetailVo = (OrderDetailVo) getActivity().getIntent().getExtras().getSerializable("OrderDetailVo");

        }

        if (orderDetailVo != null) {
            orderId = orderDetailVo.getOrderId();

            ImageLoader.getInstance().displayImage(orderDetailVo.getCarPhoto(), carImage);

            carMode.setText(orderDetailVo.getMake() + orderDetailVo.getModel());
            carNo.setText(orderDetailVo.getLicense());
//            if(orderDetailVo.getTimeMode()==1){
//                spendTime.setText(orderDetailVo.getRentedDayNumber() + "天" + TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getCostTime()));
//            }else{
            spendTime.setText(TimeDateUtil.formatTime(orderDetailVo.getFeeDetail().getCostTime()));
//            }
            mileageSum.setText(((int) orderDetailVo.getFeeDetail().getDistance()) + "公里");

            orderPrice.setText("￥" + MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getPayPrice()));

        }
        mark = 3;
        start0.setSelected(true);
        start1.setSelected(true);
        start2.setSelected(true);
        start3.setSelected(true);
        start4.setSelected(true);
        massagegridview.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_raw:
                startActivity(MainActivity2_1.class, null);
                getActivity().finish();
                break;
            case R.id.iv_right:
                showCallDialog();
                break;
            case R.id.rated_star0:
                mark = 1;
                start0.setSelected(true);
                start1.setSelected(false);
                start2.setSelected(false);
                start3.setSelected(false);
                start4.setSelected(false);
                massagegridview.setVisibility(View.VISIBLE);
                break;
            case R.id.rated_star1:
                mark = 2;
                start0.setSelected(true);
                start1.setSelected(true);
                start2.setSelected(false);
                start3.setSelected(false);
                start4.setSelected(false);
                massagegridview.setVisibility(View.VISIBLE);
                break;
            case R.id.rated_star2:
                mark = 3;
                start0.setSelected(true);
                start1.setSelected(true);
                start2.setSelected(true);
                start3.setSelected(false);
                start4.setSelected(false);
                massagegridview.setVisibility(View.GONE);
                break;
            case R.id.rated_star3:
                mark = 4;
                start0.setSelected(true);
                start1.setSelected(true);
                start2.setSelected(true);
                start3.setSelected(true);
                start4.setSelected(false);
                massagegridview.setVisibility(View.GONE);
                break;
            case R.id.rated_star4:
                mark = 5;
                start0.setSelected(true);
                start1.setSelected(true);
                start2.setSelected(true);
                start3.setSelected(true);
                start4.setSelected(true);
                massagegridview.setVisibility(View.GONE);
                break;

            case R.id.rated_pay_detail:
                toRunDetai((OrderListItemBean) v.getTag());
                break;

            case R.id.rated_button:
                setSubmit();
                break;

        }
//        rated_coupon

    }


    @Override
    public void onDestroyView() {
        WXEntryActivity.setShareCallBackListener(null);
        super.onDestroyView();
    }

    @Override
    public void onError(int tag, String error) {
        dismmisDialog();
        MyUtils.showToast(mContext, "数据传输错误，请重试");
    }


    @Override
    public void onComplete(int tag, String json) {

        dismmisDialog();
        switch (tag) {
            case 1001:
                ShareBeanReturnContent shareBeanReturnContent1001 = (ShareBeanReturnContent) GsonTransformUtil.fromJson(json, ShareBeanReturnContent.class);
                if (shareBeanReturnContent1001 == null && shareBeanReturnContent1001.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareBeanReturnContent1001.getError() + "");
                } else {
                    this.shareBeanReturnContent = shareBeanReturnContent1001;
                    SharedPreferenceTool.setPrefBoolean(mContext, "RatedUsFrag", true);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_share);
                    if (TextUtils.isEmpty(money) && YYConstans.shareContentBeanReturnContent != null) {
                        money = YYConstans.shareContentBeanReturnContent.getReturnContent().getCouponBeInvited_1() + YYConstans.shareContentBeanReturnContent.getReturnContent().getCouponBeInvited_2() + "";
                    }
                }
                break;
            case 1002:
                ShareBeanReturnContent shareBeanReturnContent1002 = (ShareBeanReturnContent) GsonTransformUtil.fromJson(json, ShareBeanReturnContent.class);
                if (shareBeanReturnContent1002 == null && shareBeanReturnContent1002.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareBeanReturnContent1002.getError() + "");
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_share);
                    ShareUtils.getIntance(mContext).shareToWeiXin(mContext, shareBeanReturnContent1002.getReturnContent().getUrl().replace("\\", ""), shareBeanReturnContent1002.getReturnContent().getTitle(), shareBeanReturnContent1002.getReturnContent().getDesc()
                            , bitmap, "aa");
                }
                break;
            case 1003:
                ShareBeanReturnContent shareBeanReturnContent1003 = (ShareBeanReturnContent) GsonTransformUtil.fromJson(json, ShareBeanReturnContent.class);
                if (shareBeanReturnContent1003 == null && shareBeanReturnContent1003.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareBeanReturnContent1003.getError() + "");
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_share);
                    ShareUtils.getIntance(mContext).shareToWeiXinCircle(mContext, shareBeanReturnContent1003.getReturnContent().getUrl().replace("\\", ""), shareBeanReturnContent1003.getReturnContent().getTitle(), shareBeanReturnContent1003.getReturnContent().getDesc()
                            , bitmap, "aa");
                }
                break;
            case YYConstans.TAG_GETACTIVITYCONTENT:
                ShareContentBeanReturnContent shareContentBeanReturnContent = (ShareContentBeanReturnContent) GsonTransformUtil.fromJson(json, ShareContentBeanReturnContent.class);
                if (shareContentBeanReturnContent == null || shareContentBeanReturnContent.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            shareContentBeanReturnContent == null ? "数据传输错误，请重试" : shareContentBeanReturnContent.getError() + "");
                } else {
                    ShareContentBean shareContentBean = shareContentBeanReturnContent.getReturnContent();
                    if (shareContentBean == null) return;
                    money = shareContentBean.getCouponBeInvited_1() +
                            shareContentBean.getCouponBeInvited_2() + "";
                    //        shareDialog2.setMoney(money);
                }
                break;
            case 2001:
                YYBaseResBean yyBaseResBean = GsonTransformUtil.fromJson(json, YYBaseResBean.class);
                if (yyBaseResBean.getErrno() != 0) {
                    MyUtils.showToast(mContext,
                            yyBaseResBean.getError() + "");
                } else {

                    if (shareBeanReturnContent != null && shareBeanReturnContent.getErrno() == 0) {
//
//                        if (TextUtils.isEmpty(money) && YYConstans.shareContentBeanReturnContent != null) {
//                            money = YYConstans.shareContentBeanReturnContent.getReturnContent().getCouponBeInvited_1() + YYConstans.shareContentBeanReturnContent.getReturnContent().getCouponBeInvited_2() + "";
//                        }
//
//                        StringBuffer sb = new StringBuffer();
//                        sb.append("<font color='#3d3f42'>分享给好友可获</font>");
//                        sb.append("<font color='#04b575'>" + money + "</font>");
//                        sb.append("<font color='#3d3f42'>元现金优惠券</font>");
//
//                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_share);
//                        shareDialog3 = new ShareDialog3(mContext, sb.toString(), shareBeanReturnContent.getReturnContent().getUrl().replace("\\", ""), shareBeanReturnContent.getReturnContent().getTitle(), shareBeanReturnContent.getReturnContent().getDesc(), bitmap, "aa");
//                        shareDialog3.setCancelClick(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(MainActivity2_1.class, null);
//                                getActivity().finish();
//                            }
//                        });
//
//                        StringBuffer stringBuffer = new StringBuffer();
//                        stringBuffer.append("我刚刚使用了一辆星辰出行的").append(orderDetailVo.getMake() + orderDetailVo.getModel() + "，");
//                        stringBuffer.append("驾驶了").append(TimeDateUtil.formatTimeMin(orderDetailVo.getFeeDetail().getCostTime()));
//                        stringBuffer.append("，跑了").append(((int) orderDetailVo.getFeeDetail().getDistance()) + "公里，");
//                        stringBuffer.append("用车费共支付了").append(MyUtils.formatPriceLong(orderDetailVo.getFeeDetail().getPayPrice())).append("元，");
//                        stringBuffer.append("这费用要搁着打出租车怎么可能？这么好的事当然要分享~");
//                        shareDialog3.setMessageStr2(stringBuffer.toString());
//                        shareDialog3.show();
                    MyUtils.showToast(mContext, "提交成功");
                    startActivity(MainActivity2_1.class, null);
                    getActivity().finish();

                    } else {
                        MyUtils.showToast(mContext, "提交成功");
                        startActivity(MainActivity2_1.class, null);
                        getActivity().finish();
                    }
                }
                break;
            case 2002:
                OrderListBean listBean = (OrderListBean) GsonTransformUtil.fromJson(
                        json, OrderListBean.class);

                if (listBean != null && listBean.getErrno() == 0 && listBean.getReturnContent() != null) {
                    if (listBean.getReturnContent().getOrderList() != null) {
                        a:
                        for (OrderListItemBean itemBean : listBean.getReturnContent().getOrderList()) {

                            if (itemBean != null && orderDetailVo.getOrderId().equals(itemBean.getOrderId() + "")) {
                                toRunDetai(itemBean);
                                break a;
                            }

                        }
                    }
                }

                break;
        }

    }


    @Override
    public void onLoading(long count, long current) {

    }

    private void setSubmit() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
        params.put("type", "2");
        params.put("orderId", orderId);
        params.put("mark", mark + "");
        if (mark <= 2) {
            params.put("content", rateUsAdp.getStringData() + "");
        } else {
            params.put("content", "");
        }


        dialogShow();
        YYRunner.getData(2001, YYRunner.Method_POST, YYUrl.GETFEELBACK,
                params, this);

    }

    private String[] setSpitlot() {
        String split = SharedPreferenceTool.getPrefString(mContext, SharedPreferenceTool.KEY_SPITSLOT, "找不到车");
        String[] splits = split.split(";");
        return splits;
    }


    private void toRunDetai(OrderListItemBean bean) {
        if (bean == null) {
            dialogShow();
            Map<String, String> params = new HashMap<String, String>();
            params.put("skey", YYConstans.getUserInfoBean().getReturnContent().getSkey());
            params.put("status", "2");
            params.put("pageNo", 1 + "");
            params.put("pageSize", "10");
            YYRunner.getData(2002, YYRunner.Method_POST,
                    YYUrl.GETORDERLIST, params, this);
        } else {
            orderPriceDetail.setTag(bean);
            Intent toDetail = new Intent(mContext, RunDetailAty.class);
            toDetail.putExtra("OrderListItemBean", bean);
            mContext.startActivity(toDetail);
            getActivity().overridePendingTransition(
                    R.anim.activity_up, R.anim.activity_push_no_anim);
        }
    }

    @Override
    public void OnBackListener(int status, String message) {

        startActivity(MainActivity2_1.class, null);
        getActivity().finish();

    }
}
