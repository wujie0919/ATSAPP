package com.dzrcx.jiaan.Order;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzrcx.jiaan.R;

public class RateUsAdp extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private String[] splits;
    private HashMap<Integer, Boolean> isSelected;

    public RateUsAdp(Context context, String[] splits, HashMap<Integer, Boolean> isSelected) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.splits = splits;
        this.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return splits == null ? 0 : splits.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return splits[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_rateus, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setTag(position);
        holder.textView.setOnClickListener(clickListener);
        holder.textView.setSelected(isSelected.get(position));
        holder.textView.setText(splits[position]);
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
            isSelected.put((Integer) v.getTag(), v.isSelected());
        }
    };

    public String getStringData() {
        String s = "";
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i)) {
                s = s + splits[i];
                s = s + ";";
            }
        }
        if (s.endsWith(";")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
