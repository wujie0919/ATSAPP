package com.dzrcx.jiaan.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzrcx.jiaan.Bean.CheckUserForegiftVo;
import com.dzrcx.jiaan.R;
import com.dzrcx.jiaan.base.YYBaseFragment;

/**
 * Created by zhangyu on 16-7-29.
 */
public class DepositFrag extends YYBaseFragment implements View.OnClickListener {

    private View depositView;

    private TextView titleView;
    private ImageView iv_left_raw;
    private View hintView;
    private TextView depositTitle, depositMessage, depositButton;

    private LinearLayout depositPriceLayout;
    private TextView depositPrice;


    private CheckUserForegiftVo userForegiftVo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (depositView == null) {
            depositView = inflater.inflate(R.layout.frag_deposit, null);
            iv_left_raw = (ImageView) depositView.findViewById(R.id.iv_left_raw);
            titleView = (TextView) depositView.findViewById(R.id.tv_title);

            hintView = depositView.findViewById(R.id.user_deposit_hint_img);
            depositTitle = (TextView) depositView.findViewById(R.id.user_deposit_hint_result);
            depositMessage = (TextView) depositView.findViewById(R.id.user_deposit_line_messge);
            depositButton = (TextView) depositView.findViewById(R.id.user_deposit_button);

            iv_left_raw.setVisibility(View.VISIBLE);
            iv_left_raw.setOnClickListener(this);

            titleView.setText("申请提现");
            depositButton.setOnClickListener(this);

            depositPriceLayout = (LinearLayout) depositView.findViewById(R.id.user_deposit_line2);
            depositPriceLayout.setVisibility(View.GONE);
            depositPrice = (TextView) depositView.findViewById(R.id.user_deposit_price);

            if (getActivity().getIntent().getExtras() != null) {
                userForegiftVo = (CheckUserForegiftVo) getActivity().getIntent().getExtras().getSerializable("CheckUserForegiftVo");
            }

            if (userForegiftVo != null) {
                switch (userForegiftVo.getFlag()) {
                    case 4:
                        hintView.setBackgroundResource(R.drawable.defosit_suc);
                        depositPriceLayout.setVisibility(View.VISIBLE);
                        depositTitle.setText("提现申请成功");
                        depositPrice.setText(userForegiftVo.getAmount() + "元");
                        depositMessage.setText("提现金额将直接退还到原支付账户，由于银行原因，提现会在15个工作日内完成，提现成功后，您可以登录原支付账户进行账单核实，给您带来的不便请谅解～");
                        break;
                    case 2:
                        hintView.setBackgroundResource(R.drawable.user_tishi);
                        depositTitle.setText("租车押金提现提示");
                        depositMessage.setText("系统检测到您最近一次订单有违章记录，还未进行处理，暂时无法操作申请提现功能，给您带来的不便，请您谅解~");
                        break;
                    case 3:
                        hintView.setBackgroundResource(R.drawable.user_tishi);
                        depositTitle.setText("租车押金提现提示");
                        depositMessage.setText("工作人员正在努力对您最近一次用车订单进行违章查询，如您在用车过程中无任何违章罚款扣分情况，或未造成任何经济损失，租车押金将在最近一个订单结束日后满15天才可申请提现，给您带来的不便，请您谅解~");
                        break;
                    default:
                        hintView.setBackgroundResource(R.drawable.user_tishi);
                        depositTitle.setText("租车押金提现提示");
                        depositMessage.setText(userForegiftVo.getMessage());
                        break;
                }
                depositButton.setTag(userForegiftVo);


            }

        }

        return depositView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left_raw:
                getActivity().finish();
                break;
            case R.id.user_deposit_button:
                getActivity().finish();
                break;
        }

    }


}
