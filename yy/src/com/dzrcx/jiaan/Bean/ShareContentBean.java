package com.dzrcx.jiaan.Bean;

/**
 * 邀请详细内容
 */
public class ShareContentBean {
    private long startTime;
    private long endTime;
    private String name;
    private int couponBeInvited_1;//被邀请人获得的第一张优惠券
    private int couponBeInvited_2;//被邀请人获得的第二张优惠券
    private int couponToInvite;//邀请人获得的优惠券
    private int couponAfterFirstOrder;//被邀请人完成首单后,邀请人获得优惠券

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCouponBeInvited_1() {
        return couponBeInvited_1;
    }

    public void setCouponBeInvited_1(int couponBeInvited_1) {
        this.couponBeInvited_1 = couponBeInvited_1;
    }

    public int getCouponBeInvited_2() {
        return couponBeInvited_2;
    }

    public void setCouponBeInvited_2(int couponBeInvited_2) {
        this.couponBeInvited_2 = couponBeInvited_2;
    }

    public int getCouponToInvite() {
        return couponToInvite;
    }

    public void setCouponToInvite(int couponToInvite) {
        this.couponToInvite = couponToInvite;
    }

    public int getCouponAfterFirstOrder() {
        return couponAfterFirstOrder;
    }

    public void setCouponAfterFirstOrder(int couponAfterFirstOrder) {
        this.couponAfterFirstOrder = couponAfterFirstOrder;
    }
}
