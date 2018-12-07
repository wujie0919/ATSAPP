package com.dzrcx.jiaan.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.FeeDetailBean;
import com.dzrcx.jiaan.Bean.OrderListItemBean;
import com.dzrcx.jiaan.Order.OrderAty;
import com.dzrcx.jiaan.Order.PayActivity;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseActivity;
import com.dzrcx.jiaan.tools.MyUtils;
import com.dzrcx.jiaan.tools.TimeDateUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyRunsListAdp extends BaseAdapter {
    private List<OrderListItemBean> listItemBeans;
    private LayoutInflater inflater;
    private Context context;

    public MyRunsListAdp(Context context, List<OrderListItemBean> listItemBeans) {
        this.inflater = LayoutInflater.from(context);
        this.listItemBeans = listItemBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItemBeans != null ? listItemBeans.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_orderlist, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_carnumber = (TextView) convertView
                    .findViewById(R.id.tv_carnumber);
            holder.ll_prepart = (LinearLayout) convertView
                    .findViewById(R.id.ll_prepart);
            holder.tv_ordernumber = (TextView) convertView
                    .findViewById(R.id.tv_ordernumber);
            holder.tv_travelmileage = (TextView) convertView
                    .findViewById(R.id.tv_travelmileage);
            holder.tv_travetime = (TextView) convertView
                    .findViewById(R.id.tv_travetime);
            holder.tv_completestatu = (TextView) convertView.findViewById(R.id.tv_completestatu);
            holder.tv_fee = (TextView) convertView
                    .findViewById(R.id.tv_fee);
            holder.tv_point_top = (TextView) convertView
                    .findViewById(R.id.tv_point_top);
            holder.line1 = convertView.findViewById(R.id.line1);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
                holder.line1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            convertView.setOnClickListener(listener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.tv_point_top.setBackgroundColor(context.getResources().getColor(R.color.bgcolor));
        } else {
            holder.tv_point_top.setBackgroundColor(Color.TRANSPARENT);
        }
        OrderListItemBean bean = listItemBeans.get(position);
        FeeDetailBean feeDetailBean = bean.getFeeDetail();
//        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
//        holder.tv_time.setText(""
//                + sFormat.format(feeDetailBean.getOrderTime()));
//        holder.tv_carnumber.setText("租用车辆：" + bean.getMake() + " " + bean.getModel() + "  " + bean.getLicense());
//        holder.tv_fee.setText("用车费用 " + MyUtils.formatPriceShort(bean.getFeeDetail().getAllPrice()) + "元");
//        holder.tv_ordernumber.setText("订单编号：" + bean.getOrderId());
//        holder.tv_travelmileage.setText("行驶里程：" + feeDetailBean.getDistance() + "公里");
//        holder.tv_travetime.setText("行驶时间：" + TimeDateUtil.formatTime(feeDetailBean.getCostTime()) + "");

        if (bean.getTimeMode() == 0) {//如果是分时
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
            holder.tv_time.setText(""
                    + sFormat.format(feeDetailBean.getOrderTime()));
            holder.tv_carnumber.setText("租用车辆：" + bean.getMake() + " " + bean.getModel() + "  " + bean.getLicense());
            holder.tv_fee.setText("用车费用：" + MyUtils.formatPriceShort(bean.getFeeDetail().getAllPrice()) + "元");
            holder.tv_ordernumber.setText("订单编号：" + bean.getOrderId());
            holder.tv_travelmileage.setText("行驶里程：" + feeDetailBean.getDistance() + "公里");
            holder.tv_travetime.setText("行驶时间：" + TimeDateUtil.formatTime(feeDetailBean.getCostTime()) + "");
            switch (bean.getOrderState()) {
                case 1:
                    holder.tv_completestatu.setText("等待取车");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.titlebar_background));
                    break;
                case 2:
                    holder.tv_completestatu.setText("正在用车");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.titlebar_background));
                    break;
                case 3:
                    holder.tv_completestatu.setText("支付租金");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.titlebar_background));
                    break;
                case 4:
                    holder.tv_completestatu.setText("已完成");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.text_3d3f42));
                    break;
                case 5:
                    holder.tv_completestatu.setText("已取消");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.text_3d3f42));
                    break;
                default:
                    holder.tv_completestatu.setText(null);
                    break;
            }
        } else if (bean.getTimeMode() == 1) {//如果是日租
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
            holder.tv_time.setText(""+ sFormat.format(feeDetailBean.getOrderTime()));
            holder.tv_carnumber.setText("租用车辆：" + bean.getMake() + " " + bean.getModel() + "  " + bean.getLicense());
            holder.tv_fee.setText("用车费用：" + MyUtils.formatPriceShort(bean.getRentalDay() + bean.getFeeDetail().getAllPrice()) + "元");
            holder.tv_ordernumber.setText("订单编号：" + bean.getOrderId());
            holder.tv_travelmileage.setText("行驶里程：" + feeDetailBean.getDistance() + "公里");
            holder.tv_travetime.setText("用车时间：" + bean.getRentedDayNumber() + "天" + TimeDateUtil.formatTime(bean.getFeeDetail().getCostTime()));
            switch (bean.getDailyState()) {
                case 1:
                    holder.tv_completestatu.setText("等待支付");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.titlebar_background));
                    break;
                case 2:
                    holder.tv_completestatu.setText("正在用车");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.titlebar_background));
                    break;
                case 8:
                    holder.tv_completestatu.setText("已取消");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.text_3d3f42));
                    break;
                case 9:
                    holder.tv_completestatu.setText("已完成");
                    holder.tv_completestatu.setTextColor(context.getResources().getColor(R.color.text_3d3f42));
                    break;
                default:
                    holder.tv_completestatu.setText(null);
                    break;
            }
        }

        convertView.setTag(R.id.tag_second, bean.getOrderId() + "");
        convertView.setTag(R.id.tag_threed, bean);
        return convertView;
    }

    public void replaceData(List<OrderListItemBean> mPostionlistItems) {
        listItemBeans.clear();
        if (mPostionlistItems != null) {
            listItemBeans.addAll(mPostionlistItems);
        }
    }

    private class ViewHolder {
        public TextView tv_ordernumber, tv_point_top, tv_time, tv_completestatu, tv_carnumber, tv_fee, tv_travelmileage, tv_travetime;
        public View line1;
        public LinearLayout ll_prepart;
    }

    OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String orderId = (String) v.getTag(R.id.tag_second);
            OrderListItemBean bean = (OrderListItemBean) v.getTag(R.id.tag_threed);
            if (!TextUtils.isEmpty(orderId) && bean != null) {
                if (bean.getTimeMode() == 0) {//分时
                    switch (bean.getOrderState()) {
                        case 1://等待取车
                        case 2://正在用车
                            Intent toOrder = new Intent(context, OrderAty.class);
                            toOrder.putExtra("orderId", orderId);
                            context.startActivity(toOrder);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);

                            break;
                        case 3://支付租金
                            Intent toPay = new Intent(context, PayActivity.class);
                            toPay.putExtra("orderId", orderId);
                            context.startActivity(toPay);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 4://已完成
                            if (bean == null) {
                                return;
                            }
                            Intent toDetail = new Intent(context, RunDetailAty.class);
                            toDetail.putExtra("OrderListItemBean", bean);
                            context.startActivity(toDetail);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        default://5已取消
                            break;
                    }
                } else if (bean.getTimeMode() == 1) {//日租
                    switch (bean.getDailyState()) {
                        case 1://等待支付
                            Intent toPay = new Intent(context, DayShareActivity.class);
                            toPay.putExtra("PushDialogActivity",true);
                            toPay.putExtra("orderId", orderId);
                            context.startActivity(toPay);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 2://正在用车
                            Intent toOrder = new Intent(context, OrderAty.class);
                            toOrder.putExtra("orderId", orderId);
                            context.startActivity(toOrder);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        case 8://已取消

                            break;
                        case 9://已完成
                            if (bean == null) {
                                return;
                            }
                            Intent toDetail = new Intent(context, RunDetailAty.class);
                            toDetail.putExtra("OrderListItemBean", bean);
                            context.startActivity(toDetail);
                            ((YYBaseActivity) context).overridePendingTransition(
                                    R.anim.activity_up, R.anim.activity_push_no_anim);
                            break;
                        default:
                            break;
                    }
                }

            }

        }
    };
}
