package com.dzrcx.jiaan.Bean;

/**
 * 充值记录
 * Created by zhangyu on 16-1-6.
 */
public class RechargeRecord {

    private double amount;
    private String payType;
    private long payTimes;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getPayTimes() {
        return payTimes;
    }

    public void setPayTimes(long payTimes) {
        this.payTimes = payTimes;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
