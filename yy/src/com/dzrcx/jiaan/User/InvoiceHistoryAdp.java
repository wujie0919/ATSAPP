package com.dzrcx.jiaan.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.InvoiceItem;
import com.dzrcx.jiaan.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceHistoryAdp extends BaseAdapter {
    private List<InvoiceItem> listItemBeans;
    private LayoutInflater inflater;
    private Context context;
    private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public InvoiceHistoryAdp(Context context, List<InvoiceItem> listItemBeans) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_invoicehistory, null);
            holder.tv_money_num = (TextView) convertView.findViewById(R.id.tv_money_num);
            holder.tv_invoice_number = (TextView) convertView.findViewById(R.id.tv_invoice_number);
            holder.tv_invoic_statu = (TextView) convertView.findViewById(R.id.tv_invoic_statu);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InvoiceItem item = listItemBeans.get(position);
        holder.tv_invoice_number.setText(item.getInvoiceNumber());
        holder.tv_money_num.setText(item.getAmount() + "元");
        holder.tv_time.setText(sFormat.format(item.getCreateTimes()));
        String stateStr = "";
        switch (item.getState()) {
            case 0:
                stateStr = "等待受理";//"待确认"
                break;
            case 1:
                stateStr = "正在开票";//"待开票"
                break;
            case 2:
                stateStr = "开票完成";//"已开票"
                break;
            case 3:
                stateStr = "开票完成";//"已邮寄"
                break;
            case 4:
                stateStr = "开票失败";//"已驳回"
                break;
        }
        holder.tv_invoic_statu.setText(stateStr);
        return convertView;
    }

    public void replaceData(List<InvoiceItem> mPostionlistItems) {
        listItemBeans.clear();
        if (mPostionlistItems != null) {
            listItemBeans.addAll(mPostionlistItems);
        }
    }

    private class ViewHolder {
        public TextView tv_money_num, tv_invoic_statu, tv_time,tv_invoice_number;
    }

}
