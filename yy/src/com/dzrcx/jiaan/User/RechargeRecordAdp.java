package com.dzrcx.jiaan.User;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.RechargeRecordListVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.TimeDateUtil;
import com.dzrcx.jiaan.widget.PinnedSectionListView;

import java.util.List;

public class RechargeRecordAdp extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private List<RechargeRecordListVo> listItemBeans;
    private LayoutInflater inflater;
    private Context context;
    private int TAG_CONTENT = 0;//
    private int TAG_SECTION = 1;//

    public int getTAG_CONTENT() {
        return TAG_CONTENT;
    }

    public void setTAG_CONTENT(int TAG_CONTENT) {
        this.TAG_CONTENT = TAG_CONTENT;
    }

    public int getTAG_SECTION() {
        return TAG_SECTION;
    }

    public void setTAG_SECTION(int TAG_SECTION) {
        this.TAG_SECTION = TAG_SECTION;
    }

    public RechargeRecordAdp(Context context, List<RechargeRecordListVo> listItemBeans) {
        this.inflater = LayoutInflater.from(context);
        this.listItemBeans = listItemBeans;
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TAG_SECTION;
    }

    @Override
    public int getCount() {
        return listItemBeans == null ? 0 : listItemBeans.size();
    }

    @Override
    public RechargeRecordListVo getItem(int position) {
        return listItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolderContent viewHolderContent;
        ViewHolderSection holderSection;
        // 添加间隔背景
        if (listItemBeans.get(position).getType() == TAG_SECTION) {
            if (convertView == null || convertView.getTag(R.id.tag_first) == null) {
                holderSection = new ViewHolderSection();
                convertView = inflater.inflate(R.layout.item_rechargerecord_section, null);
                holderSection.tv_month_year = (TextView) convertView.findViewById(R.id.tv_month_year);
                convertView.setTag(R.id.tag_first, holderSection);
            } else {
                holderSection = (ViewHolderSection) convertView.getTag(R.id.tag_first);
            }
            String dataTime = TimeDateUtil.dateToYM(listItemBeans.get(position).getTradesTime());
            String nowTime = TimeDateUtil.dateToYM(System.currentTimeMillis());
            if (nowTime.substring(0, 4).equals(dataTime.substring(0, 4))) {
                if (nowTime.substring(4, 6).equals(dataTime.substring(4, 6))) {
                    holderSection.tv_month_year.setText("本月");
                } else {
                    holderSection.tv_month_year.setText(numToUpper(Integer.parseInt(dataTime.substring(4, 6))) + "月");
                }
            } else {
                holderSection.tv_month_year.setText(numToUpper(Integer.parseInt(dataTime.substring(4, 6))) + "月/" + dataTime.substring(0, 4));
            }
        } else if (listItemBeans.get(position).getType() == TAG_CONTENT) {
            if (convertView == null || convertView.getTag(R.id.tag_second) == null) {
                viewHolderContent = new ViewHolderContent();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_rechargerecord, null);
                viewHolderContent.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolderContent.v_line = convertView.findViewById(R.id.v_line);
                viewHolderContent.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                viewHolderContent.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(R.id.tag_second, viewHolderContent);
            } else {
                viewHolderContent = (ViewHolderContent) convertView.getTag(R.id.tag_second);
            }
            RechargeRecordListVo rechargeRecordListVo = listItemBeans.get(position);
            viewHolderContent.tv_content.setText(rechargeRecordListVo.getTradeName());
            viewHolderContent.tv_money.setText(rechargeRecordListVo.getTradeAmount() + "元");
            viewHolderContent.tv_time.setText(TimeDateUtil.getTime(rechargeRecordListVo.getTradesTime()));
            viewHolderContent.v_line.setVisibility(View.VISIBLE);
            if (rechargeRecordListVo.islast()) {
                viewHolderContent.v_line.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolderSection {
        public TextView tv_month_year;
    }

    private class ViewHolderContent {
        private TextView tv_content, tv_money, tv_time;
        private View v_line;
    }

    public void replaceData(List<RechargeRecordListVo> mPostionlistItems) {
        if (mPostionlistItems != null) {
            listItemBeans = mPostionlistItems;
        }
    }

    // 将数字转化为大写
    public static String numToUpper(int num) {
        // String u[] = {"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String u[] = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }
}
