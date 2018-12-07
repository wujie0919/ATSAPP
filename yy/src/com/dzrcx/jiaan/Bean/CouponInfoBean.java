package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * item
 * Created by jgw.
 */
public class CouponInfoBean implements Serializable {


    private int couponId;
    private double amount;
    private long startTimes;
    private long endTimes;
    private long sendTimes;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(long startTimes) {
        this.startTimes = startTimes;
    }

    public long getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(long endTimes) {
        this.endTimes = endTimes;
    }

    public long getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(long sendTimes) {
        this.sendTimes = sendTimes;
    }
}
