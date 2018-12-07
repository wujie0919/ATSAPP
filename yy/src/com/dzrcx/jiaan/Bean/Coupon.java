package com.dzrcx.jiaan.Bean;

/**
 * 优惠券
 * Created by zhangyu on 16-1-5.
 */
public class Coupon {

    private int couponId;
    private String couponName;
    private int amount;
    /**
     * 发券时间
     */
    private long sendTimes;
    /**
     * 生效起始时间
     */
    private long startTimes;
    /**
     * 失效时间
     */
    private long endTimes;


    /**
     * 0可用；-1不可用
     */
    private int isAble;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
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

    public long getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(long startTimes) {
        this.startTimes = startTimes;
    }

    public int getIsAble() {
        return isAble;
    }

    public void setIsAble(int isAble) {
        this.isAble = isAble;
    }
}
