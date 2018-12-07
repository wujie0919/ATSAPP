package com.dzrcx.jiaan.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceRouteListBeanIn;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.clicklistener.InvoiceListMoneyListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceRouteListAdp extends BaseAdapter {
    private List<InvoiceRouteListBeanIn> listItemBeans;
    private LayoutInflater inflater;
    private Context context;
    private SimpleDateFormat sFormat = new SimpleDateFormat("MM月dd日 H:mm");
    private InvoiceListMoneyListener invoiceListMoneyListener;

    public InvoiceRouteListAdp(Context context, List<InvoiceRouteListBeanIn> listItemBeans, InvoiceListMoneyListener invoiceListMoneyListener) {
        this.inflater = LayoutInflater.from(context);
        this.listItemBeans = listItemBeans;
        this.context = context;
        this.invoiceListMoneyListener = invoiceListMoneyListener;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_invoiceroute, null);
            holder.tv_money_num = (TextView) convertView.findViewById(R.id.tv_money_num);
            holder.tv_starttime = (TextView) convertView.findViewById(R.id.tv_starttime);
            holder.tv_endtime = (TextView) convertView.findViewById(R.id.tv_endtime);
            holder.iv_checkbox = (ImageView) convertView.findViewById(R.id.iv_checkbox);
            holder.line = (View) convertView.findViewById(R.id.line);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
                holder.line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            holder.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InvoiceRouteListBeanIn item = listItemBeans.get(position);
        holder.iv_checkbox.setSelected(item.getChecked());
        holder.iv_checkbox.setTag(position);
        holder.tv_starttime.setText("开始计费时间："
                + sFormat.format(item.getChargeTimes()));
        holder.tv_endtime.setText("订单结束时间："
                + sFormat.format(item.getEndTimes()));
        holder.tv_money_num.setText(item.getAmount() + "元");
        holder.ll_content.setOnClickListener(listener);
        holder.ll_content.setTag(holder.iv_checkbox);
        return convertView;
    }

    public void replaceData(List<InvoiceRouteListBeanIn> mPostionlistItems) {
        listItemBeans.clear();
        if (mPostionlistItems != null) {
            listItemBeans.addAll(mPostionlistItems);
        }
    }

    private class ViewHolder {
        public TextView tv_money_num, tv_starttime, tv_endtime;
        public ImageView iv_checkbox;
        public View line;
        public LinearLayout ll_content;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ImageView iv_checkbox = (ImageView) v.getTag();
            iv_checkbox.setSelected(!iv_checkbox.isSelected());
            invoiceListMoneyListener.changeCount(listItemBeans.get((int) iv_checkbox.getTag()).getAmount(), iv_checkbox.isSelected());
            listItemBeans.get((int) iv_checkbox.getTag()).setChecked(iv_checkbox.isSelected());
        }
    };


}
