package com.dzrcx.jiaan.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OderDetailInfo implements Serializable {

    /**
     * 订单金额
     */
    private double allPrice;

    /**
     * 优惠金额
     */
    private double benefitPrice;

    /**
     * 优惠细项
     */
    private ArrayList<BenefitItemVo> benefitItems;


    private BenefitDetailVo benefitDetail;


    /**
     * 实际支付金额（扣除优惠金额后）
     */
    private double payPrice;


    /**
     * 扣除优惠金额及账户余额后的费用
     */
    private double needPay;

    private long chargeTime;
    private long costTime;
    private double distance;
    private double distancePrice;
    private double hourPrice;
    private long orderTime;
    private long payTime;
    private long pickcarTime;
    private long returnTime;
    private double totalPrice;


    private long dayTime;
    private long nightTime;
    private double nightPrice;
    private double dayPrice;


    private int mode; // 0—个人,1—企业
    private OrderPayDetail payDetail;


    private double overMileage;// 公里收费起点

    private double premium;// 溢价参数




    public double getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(double allPrice) {
        this.allPrice = allPrice;
    }

    public double getBenefitPrice() {
        return benefitPrice;
    }

    public void setBenefitPrice(double benefitPrice) {
        this.benefitPrice = benefitPrice;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public long getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(long chargeTime) {
        this.chargeTime = chargeTime;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistancePrice() {
        return distancePrice;
    }

    public void setDistancePrice(double distancePrice) {
        this.distancePrice = distancePrice;
    }

    public double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public long getPickcarTime() {
        return pickcarTime;
    }

    public void setPickcarTime(long pickcarTime) {
        this.pickcarTime = pickcarTime;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public double getOverMileage() {
        return overMileage;
    }

    public void setOverMileage(double overMileage) {
        this.overMileage = overMileage;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }


    public double getNeedPay() {
        return needPay;
    }

    public void setNeedPay(double needPay) {
        this.needPay = needPay;
    }

    public ArrayList<BenefitItemVo> getBenefitItems() {
        return benefitItems;
    }

    public void setBenefitItems(ArrayList<BenefitItemVo> benefitItems) {
        this.benefitItems = benefitItems;
    }


    public OrderPayDetail getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(OrderPayDetail payDetail) {
        this.payDetail = payDetail;
    }

    public BenefitDetailVo getBenefitDetail() {
        return benefitDetail;
    }

    public void setBenefitDetail(BenefitDetailVo benefitDetail) {
        this.benefitDetail = benefitDetail;
    }

    public double getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(double dayPrice) {
        this.dayPrice = dayPrice;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public double getNightPrice() {
        return nightPrice;
    }

    public void setNightPrice(double nightPrice) {
        this.nightPrice = nightPrice;
    }

    public long getNightTime() {
        return nightTime;
    }

    public void setNightTime(long nightTime) {
        this.nightTime = nightTime;
    }
}
