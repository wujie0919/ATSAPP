package com.dzrcx.jiaan.Bean;

import java.util.List;

public class MapcarListStationAndCarsVo {
    private String address;//站点详细地址
    private int carCount;//拥有车的数量
    private List<Integer> carIds;
    private List<MapcarfrgCarVo> carList;
    private String city;
    private double dayTime;
    private String dayTimeDesc;
    private double distance;
    private String district;//区域
    private int flag;//0无提醒，1有提醒
    private double longitude;
    private double latitude;
    private double nightTime;
    private String nightTimeDesc;
    private int parkingNum;//站点车位数量
    private String privince;//省区
    private String remainTimeDesc;//提醒时间
    private int stationId;
    private String stationName;
    private int stayType;//1-A 2-B 3-N
    private int visible;

    public List<MapcarfrgCarVo> getCarList() {
        return carList;
    }

    public void setCarList(List<MapcarfrgCarVo> carList) {
        this.carList = carList;
    }

    public String getRemainTimeDesc() {
        return remainTimeDesc;
    }

    public void setRemainTimeDesc(String remainTimeDesc) {
        this.remainTimeDesc = remainTimeDesc;
    }

    public double getDayTime() {
        return dayTime;
    }

    public void setDayTime(double dayTime) {
        this.dayTime = dayTime;
    }

    public String getDayTimeDesc() {
        return dayTimeDesc;
    }

    public void setDayTimeDesc(String dayTimeDesc) {
        this.dayTimeDesc = dayTimeDesc;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getNightTime() {
        return nightTime;
    }

    public void setNightTime(double nightTime) {
        this.nightTime = nightTime;
    }

    public String getNightTimeDesc() {
        return nightTimeDesc;
    }

    public void setNightTimeDesc(String nightTimeDesc) {
        this.nightTimeDesc = nightTimeDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public List<Integer> getCarIds() {
        return carIds;
    }

    public void setCarIds(List<Integer> carIds) {
        this.carIds = carIds;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getParkingNum() {
        return parkingNum;
    }

    public void setParkingNum(int parkingNum) {
        this.parkingNum = parkingNum;
    }

    public String getPrivince() {
        return privince;
    }

    public void setPrivince(String privince) {
        this.privince = privince;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getStayType() {
        return stayType;
    }

    public void setStayType(int stayType) {
        this.stayType = stayType;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
