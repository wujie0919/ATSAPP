package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-5-12.
 */
public class CreateOrderVO implements Serializable {

    private int fromStationId;

    private String fromStationName;
    private String fromStationAddress;
    private double distance;
    private double lat;
    private double lng;

    private int stayType;//1-A 2-B 3-N

    private int parkingNum;//站点车位数量


    private int toStationId;
    private String toStationName;


    private int payMode;
    private int entId;
    private int carId;


    private double price;//价格
    private double discount;
    private double startPrice;
    private double pricePerMileage;//每公里收费

    private double ev;//续航

    private String license;
    private String make;
    private String model;

    private double dailyRentalPrice;//日租价格

    private double freedomMultiple;//当更换为自由还车的时候的价钱倍数

    private int canChangeFreedomMode;//是否可以更换成自由还车类型

    private int orderId;//企业id

    private String payEnterpriseName;//支付企业的名称

    private String accountName;//账户名称 个人名称或企业名称\

    private long createTime;//时间戳 订单创建时间 毫秒直接转换

    private int expectedDayNumber;//期望天数

    private int rentedDayNumber;//已租天数

    public int getRentedDayNumber() {
        return rentedDayNumber;
    }

    public void setRentedDayNumber(int rentedDayNumber) {
        this.rentedDayNumber = rentedDayNumber;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getExpectedDayNumber() {
        return expectedDayNumber;
    }

    public void setExpectedDayNumber(int expectedDayNumber) {
        this.expectedDayNumber = expectedDayNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPayEnterpriseName() {
        return payEnterpriseName;
    }

    public void setPayEnterpriseName(String payEnterpriseName) {
        this.payEnterpriseName = payEnterpriseName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getFreedomMultiple() {
        return freedomMultiple;
    }

    public void setFreedomMultiple(double freedomMultiple) {
        this.freedomMultiple = freedomMultiple;
    }

    public int getCanChangeFreedomMode() {
        return canChangeFreedomMode;
    }

    public void setCanChangeFreedomMode(int canChangeFreedomMode) {
        this.canChangeFreedomMode = canChangeFreedomMode;
    }

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
    }


    public double getPricePerMileage() {
        return pricePerMileage;
    }

    public void setPricePerMileage(double pricePerMileage) {
        this.pricePerMileage = pricePerMileage;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    public int getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(int fromStationId) {
        this.fromStationId = fromStationId;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getFromStationAddress() {
        return fromStationAddress;
    }

    public void setFromStationAddress(String fromStationAddress) {
        this.fromStationAddress = fromStationAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getParkingNum() {
        return parkingNum;
    }

    public void setParkingNum(int parkingNum) {
        this.parkingNum = parkingNum;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    public int getStayType() {
        return stayType;
    }

    public void setStayType(int stayType) {
        this.stayType = stayType;
    }

    public int getToStationId() {
        return toStationId;
    }

    public void setToStationId(int toStationId) {
        this.toStationId = toStationId;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getEv() {
        return ev;
    }

    public void setEv(double ev) {
        this.ev = ev;
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


}
