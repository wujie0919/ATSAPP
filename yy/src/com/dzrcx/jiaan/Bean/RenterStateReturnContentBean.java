package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/29.
 */
public class RenterStateReturnContentBean implements Serializable {
    private int depositState;//押金状态
    private int userState;//认证状态
    private int depositAmount;//需要交押金金额
    private int withdrawFlag;//提现申请1:有0:无
    private int depositType;//违章押金类型)0:没有违章押金 1:违章金为预售权 2:违章金为支付宝或微信

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public int getDepositState() {

        return depositState;
    }

    public void setDepositState(int depositState) {
        this.depositState = depositState;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getWithdrawFlag() {
        return withdrawFlag;
    }

    public void setWithdrawFlag(int withdrawFlag) {
        this.withdrawFlag = withdrawFlag;
    }

    public int getDepositType() {
        return depositType;
    }

    public void setDepositType(int depositType) {
        this.depositType = depositType;
    }
}
