package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

public class OrderListItemBean implements Serializable {
    private String carPhoto;
    private String carThumb;
    private XY currentXY;
    private XY fromXY;
    private XY toXY;
    private FeeDetailBean feeDetail;
    private String license;
    private String make;
    private String model;
    private int orderId;
    private int ifUsing;
    private int leftMileage;
    /**
     * 1—等待取车
     * 2—正在用车
     * 3—支付租金
     * 4—已完成
     * 5—已取消
     */
    private int orderState;

    /**
     * 0—个人
     * 1—企业
     */
    private int orderType;

    private String parkLocAddress;
    private int stationType;

    private int rentedDayNumber;//已租天数

    private int expectedDayNumber;//期望天数

    private int inCharge ;//日租账单是否进入分时计费 1进入,0没进入

    private String payMode;//支付模式 企业支付 "ent"/ 用户支付 "user"

    private long createTime;//时间戳 订单创建时间 毫秒直接转换

    private int payEnterpriseId;//企业支付id

    private int fromStationId;//起始站点id

    private double dailyRentalPrice;//日租价格

    private String payEnterpriseName;//支付企业的名称

    private int timeMode;//租车模式 0分时 1日租

    //单独给日租用
    private int dailyState;//1,等待支付；2，正在进行；8，订单取消；9订单完成

    private int carId;//车辆id

    private int reletRemainingSeconds;//如果是>1 提示还剩30分钟日租还车 如果是0 不提示

    //时租订单详情需要显示的字段
    private double rentalDay;//日租费用

    private double freedomMultiple;//倍数


    public double getFreedomMultiple() {
        return freedomMultiple;
    }

    public void setFreedomMultiple(double freedomMultiple) {
        this.freedomMultiple = freedomMultiple;
    }

    public double getRentalDay() {
        return rentalDay;
    }

    public void setRentalDay(double rentalDay) {
        this.rentalDay = rentalDay;
    }

    public int getReletRemainingSeconds() {
        return reletRemainingSeconds;
    }

    public void setReletRemainingSeconds(int reletRemainingSeconds) {
        this.reletRemainingSeconds = reletRemainingSeconds;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getDailyState() {
        return dailyState;
    }

    public void setDailyState(int dailyState) {
        this.dailyState = dailyState;
    }

    public int getRentedDayNumber() {
        return rentedDayNumber;
    }

    public void setRentedDayNumber(int rentedDayNumber) {
        this.rentedDayNumber = rentedDayNumber;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    public int getExpectedDayNumber() {
        return expectedDayNumber;
    }

    public void setExpectedDayNumber(int expectedDayNumber) {
        this.expectedDayNumber = expectedDayNumber;
    }

    public int getInCharge() {
        return inCharge;
    }

    public void setInCharge(int inCharge) {
        this.inCharge = inCharge;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPayEnterpriseId() {
        return payEnterpriseId;
    }

    public void setPayEnterpriseId(int payEnterpriseId) {
        this.payEnterpriseId = payEnterpriseId;
    }

    public int getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(int fromStationId) {
        this.fromStationId = fromStationId;
    }

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
    }

    public String getPayEnterpriseName() {
        return payEnterpriseName;
    }

    public void setPayEnterpriseName(String payEnterpriseName) {
        this.payEnterpriseName = payEnterpriseName;
    }

    public XY getToXY() {
        return toXY;
    }

    public void setToXY(XY toXY) {
        this.toXY = toXY;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getCarThumb() {
        return carThumb;
    }

    public void setCarThumb(String carThumb) {
        this.carThumb = carThumb;
    }

    public FeeDetailBean getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(FeeDetailBean feeDetail) {
        this.feeDetail = feeDetail;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getParkLocAddress() {
        return parkLocAddress;
    }

    public void setParkLocAddress(String parkLocAddress) {
        this.parkLocAddress = parkLocAddress;
    }

    public XY getCurrentXY() {
        return currentXY;
    }

    public void setCurrentXY(XY currentXY) {
        this.currentXY = currentXY;
    }

    public XY getFromXY() {
        return fromXY;
    }

    public void setFromXY(XY fromXY) {
        this.fromXY = fromXY;
    }

    public int getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(int ifUsing) {
        this.ifUsing = ifUsing;
    }

    public int getLeftMileage() {
        return leftMileage;
    }

    public void setLeftMileage(int leftMileage) {
        this.leftMileage = leftMileage;
    }

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }
}
