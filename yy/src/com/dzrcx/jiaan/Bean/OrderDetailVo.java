package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-7-15.
 */
public class OrderDetailVo implements Serializable {

    private String forePhoto;//开门之前 是否上传过照片 1已经 0没有
    private String afterPhoto;//还车之后 是否上传过照片 1已经 0 没有

    private String carPhoto;
    private String carThumb;
    private String license;
    private String make;
    private String model;
    private String orderId;
    /**
     * 增加一个状态
     * 1—等待取车
     * 2—用车中
     * 3—等待支付
     * 4—订单完成
     * 5—订单取消
     * 6—订单中止
     */
    private int orderState;
    private String parkLocAddress;
    private String payUrl;
    private String pickcarTime;

    private int orderType;// 0—个人,1—企业

    //以下是2.9新增加的字段
    private int stationType;
    private XY fromXY;
    private XY toXY;
    //3.0开始，当前车辆位置
    private XY currentXY;

    private OderDetailInfo feeDetail;

    private int leftMileage;//续航里程数


    private int rentedDayNumber;//已租天数

    private int expectedDayNumber;//期望天数

    private int inCharge ;//日租账单是否进入分时计费 1进入,0没进入

    private String payMode;//支付模式 企业支付 "ent"/ 用户支付 "user"

    private long createTime;//时间戳 订单创建时间 毫秒直接转换

    private long currentTime;//服务器当前时间

    private int payEnterpriseId;//企业支付id

    private int fromStationId;//起始站点id

    private double dailyRentalPrice;//日租价格

    private String payEnterpriseName;//支付企业的名称

    private int timeMode;//租车模式 0分时 1日租

    private int reletRemainingSeconds;//如果是>1 提示还剩30分钟日租还车 如果是0 不提示

    private double freedomMultiple;

    private int ifUsing;

    //单独给日租用
    private int dailyState;//1,等待支付；2，正在进行；8，订单取消；9订单完成

    public int getDailyState() {
        return dailyState;
    }

    public void setDailyState(int dailyState) {
        this.dailyState = dailyState;
    }

    public String getForePhoto() {
        return forePhoto;
    }

    public void setForePhoto(String forePhoto) {
        this.forePhoto = forePhoto;
    }

    public String getAfterPhoto() {
        return afterPhoto;
    }

    public void setAfterPhoto(String afterPhoto) {
        this.afterPhoto = afterPhoto;
    }

    public int getIfUsing() {
        return ifUsing;
    }

    public void setIfUsing(int ifUsing) {
        this.ifUsing = ifUsing;
    }

    public double getFreedomMultiple() {
        return freedomMultiple;
    }

    public void setFreedomMultiple(double freedomMultiple) {
        this.freedomMultiple = freedomMultiple;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public int getReletRemainingSeconds() {
        return reletRemainingSeconds;
    }

    public void setReletRemainingSeconds(int reletRemainingSeconds) {
        this.reletRemainingSeconds = reletRemainingSeconds;
    }

    public int getRentedDayNumber() {
        return rentedDayNumber;
    }

    public void setRentedDayNumber(int rentedDayNumber) {
        this.rentedDayNumber = rentedDayNumber;
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

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getParkLocAddress() {
        return parkLocAddress;
    }

    public void setParkLocAddress(String parkLocAddress) {
        this.parkLocAddress = parkLocAddress;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPickcarTime() {
        return pickcarTime;
    }

    public void setPickcarTime(String pickcarTime) {
        this.pickcarTime = pickcarTime;
    }

    public OderDetailInfo getFeeDetail() {
        return feeDetail;
    }

    public void setFeeDetail(OderDetailInfo feeDetail) {
        this.feeDetail = feeDetail;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderType() {
        return this.orderType;
    }


    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

    public XY getFromXY() {
        return fromXY;
    }

    public void setFromXY(XY fromXY) {
        this.fromXY = fromXY;
    }

    public XY getToXY() {
        return toXY;
    }

    public void setToXY(XY toXY) {
        this.toXY = toXY;
    }

    public XY getCurrentXY() {
        return currentXY;
    }

    public void setCurrentXY(XY currentXY) {
        this.currentXY = currentXY;
    }

    public int getLeftMileage() {
        return leftMileage;
    }

    public void setLeftMileage(int leftMileage) {
        this.leftMileage = leftMileage;
    }

}
