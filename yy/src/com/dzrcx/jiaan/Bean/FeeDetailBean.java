package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

public class FeeDetailBean implements Serializable {

    private double allPrice;//总额
    private BenefitDetail benefitDetail;
    //    private benefitItems;
    private double benefitPrice;
    private long chargeTime;
    private long costTime;
    private double dayPrice;
    private long dayTime;
    private int distance;
    private double distancePrice;
    private double hourPrice;
    private int mode;
    private double needPay;
    private double nightPrice;
    private long nightTime;
    private long orderTime;
    private int overMileage;
    private PayDetail payDetail;
    private double payPrice;
    private long payTime;
    private long pickcarTime;
    private int premium;
    private long returnTime;
    private double totalPrice;

    public double getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(double allPrice) {
        this.allPrice = allPrice;
    }

    public BenefitDetail getBenefitDetail() {
        return benefitDetail;
    }

    public void setBenefitDetail(BenefitDetail benefitDetail) {
        this.benefitDetail = benefitDetail;
    }

    public double getBenefitPrice() {
        return benefitPrice;
    }

    public void setBenefitPrice(double benefitPrice) {
        this.benefitPrice = benefitPrice;
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

    public double getNeedPay() {
        return needPay;
    }

    public void setNeedPay(double needPay) {
        this.needPay = needPay;
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

    public PayDetail getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(PayDetail payDetail) {
        this.payDetail = payDetail;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getOverMileage() {
        return overMileage;
    }

    public void setOverMileage(int overMileage) {
        this.overMileage = overMileage;
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

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
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

    public class BenefitDetail  implements Serializable{
        double benefitCouponPrice;
        double benefitHourPrice;
        double benefitMileagePrice;

        public double getBenefitCouponPrice() {
            return benefitCouponPrice;
        }

        public void setBenefitCouponPrice(double benefitCouponPrice) {
            this.benefitCouponPrice = benefitCouponPrice;
        }

        public double getBenefitHourPrice() {
            return benefitHourPrice;
        }

        public void setBenefitHourPrice(double benefitHourPrice) {
            this.benefitHourPrice = benefitHourPrice;
        }

        public double getBenefitMileagePrice() {
            return benefitMileagePrice;
        }

        public void setBenefitMileagePrice(double benefitMileagePrice) {
            this.benefitMileagePrice = benefitMileagePrice;
        }
    }

    public class PayDetail implements Serializable{
        private String entName;
        private double entPay;
        private double personPay;
        private double userPay;

        public String getEntName() {
            return entName;
        }

        public void setEntName(String entName) {
            this.entName = entName;
        }

        public double getEntPay() {
            return entPay;
        }

        public void setEntPay(double entPay) {
            this.entPay = entPay;
        }

        public double getPersonPay() {
            return personPay;
        }

        public void setPersonPay(double personPay) {
            this.personPay = personPay;
        }

        public double getUserPay() {
            return userPay;
        }

        public void setUserPay(double userPay) {
            this.userPay = userPay;
        }
    }
}
