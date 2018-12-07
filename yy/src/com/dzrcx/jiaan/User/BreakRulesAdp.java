package com.dzrcx.jiaan.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.ViolationListBean;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.tools.TimeDateUtil;

import java.util.ArrayList;

/**
 *
 */
public class BreakRulesAdp extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals> violationBeans;

    public BreakRulesAdp(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals> getViolationBeans() {
        return violationBeans;
    }

    public void setViolationBeans(ArrayList<ViolationListBean.BreakIllegaRecontent.BreakIllegals> violationBeans) {
        this.violationBeans = violationBeans;
    }

    @Override
    public int getCount() {
        return violationBeans == null ? 0 : violationBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHold viewHold = null;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.item_break, null);
            viewHold.tv_break_time = (TextView) convertView.findViewById(R.id.tv_break_time);
            viewHold.tv_break_cartxt = (TextView) convertView.findViewById(R.id.tv_break_cartxt);
            viewHold.tv_breaktxt = (TextView) convertView.findViewById(R.id.tv_breaktxt);
            viewHold.tv_breakplace = (TextView) convertView.findViewById(R.id.tv_breakplace);
            viewHold.tv_pay_statu = (TextView) convertView.findViewById(R.id.tv_pay_statu);
            viewHold.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            viewHold.tv_bottom_middletxt = (TextView) convertView.findViewById(R.id.tv_bottom_middletxt);
            viewHold.tv_money_tupe = (TextView) convertView.findViewById(R.id.tv_money_tupe);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        ViolationListBean.BreakIllegaRecontent.BreakIllegals violationBean = violationBeans.get(position);
        viewHold.tv_break_time.setText(TimeDateUtil.dateToStrLong(violationBean.getVioTime()));
        viewHold.tv_break_cartxt.setText(violationBean.getCarBrand() + " " + violationBean.getCarSeries() + " " + violationBean.getCarLicense());
        if (violationBean.getvType() == 1) {
            viewHold.tv_breakplace.setText("违章地点：" + violationBean.getAddress());
            viewHold.tv_bottom_middletxt.setText("扣分" + violationBean.getPoints());
            viewHold.tv_money_tupe.setText("罚款 ¥");
            viewHold.tv_breaktxt.setText(violationBean.getType());
            viewHold.tv_money.setText(violationBean.getAmount() + "");

        } else if (violationBean.getvType() == 2) {
            viewHold.tv_breakplace.setText("");
            viewHold.tv_breaktxt.setText(violationBean.getDesc());
            viewHold.tv_bottom_middletxt.setText("程度：" + violationBean.getNature());
            viewHold.tv_money_tupe.setText("事故经济损失 ¥");
            viewHold.tv_money.setText(violationBean.getRepairCost() + violationBean.getOutageLoss() + "");
        }

        switch (violationBean.getDealState()) {
            case 0:
                if (violationBean.getConfirmState() == 1) {
                    if (violationBean.getDeductCashState() == 1) {
                        viewHold.tv_pay_statu.setText("已支付");
                    } else {
                        viewHold.tv_pay_statu.setText("需支付：¥" + violationBean.getDeductCashAmount());
                    }
                } else {
                    viewHold.tv_pay_statu.setText("需支付：正在核算");
                }
                break;
            case 1:
                viewHold.tv_pay_statu.setText("处理中");
                break;
            case 2:
                viewHold.tv_pay_statu.setText("已处理");
                break;
        }
        convertView.setTag(R.id.tag_first, violationBean);
        convertView.setOnClickListener(clickListener);
        return convertView;
    }

    private class ViewHold {
        private TextView tv_money, tv_break_cartxt, tv_break_time, tv_breaktxt, tv_breakplace, tv_pay_statu, tv_bottom_middletxt, tv_money_tupe;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViolationListBean.BreakIllegaRecontent.BreakIllegals violationBean = (ViolationListBean.BreakIllegaRecontent.BreakIllegals) v.getTag(R.id.tag_first);
            String vType = "";
            if (violationBean.getvType() == 1) {
                vType = "违章";
            } else if (violationBean.getvType() == 2) {
                vType = "事故";
            }
            Intent intent = new Intent(mContext, BreakRulesDetailAty.class);
            intent.putExtra("violationId", violationBean.getId() + "");
            intent.putExtra("address", violationBean.getAddress() + "");
            intent.putExtra("nature", violationBean.getNature() + "");
            intent.putExtra("carname", violationBean.getCarBrand() + " " + violationBean.getCarSeries() + " "
                    + violationBean.getCarLicense());
            intent.putExtra("title", vType + "详情");
            mContext.startActivity(intent);
        }
    };
}
