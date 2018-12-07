package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

public class CarBean implements Serializable {
    //      "distance": 0.5,  //车辆距离，单位km
    //             "model": "E6",  //车辆型号
    //             "carId": 11,  //车辆id
    //             "mileage":   //总续航里程,
    //             "thumbnail":
    // "http://123.57.65.251:9228/1234/56/123456789/201510/20151014_165111_0815870-4-3-2.jpg-0.jpg", 
    // //车辆图片
    //             "price": 25,  //当前租价
    //             "leftMileage": 246.5,  //剩余续航里程
    //             "defi":
    // "http://123.57.65.251:9161/yytimeshare/uploads/carthumb.jpg",  //车辆缺省图片
    //             "lng": 116.50618244752,  //车辆所在经度
    //             "modifyTime": 0,  //信息修改时间
    //             "make": "比亚迪",  //车辆品牌
    //             "lat": 39.982631759219  //车辆所在纬度
    private String thumbnail;
    private String price;
    private String mileage;
    private String make;
    private String carId;
    private String lat;
    private String distance;
    private String leftMileage;
    private String modifyTime;
    private String lng;
    private String model;
    private String defi;

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMileage() {
        return this.mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getMake() {
        return this.make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getCarId() {
        return this.carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLeftMileage() {
        return this.leftMileage;
    }

    public void setLeftMileage(String leftMileage) {
        this.leftMileage = leftMileage;
    }

    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getLng() {
        return this.lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDefi() {
        return this.defi;
    }

    public void setDefi(String defi) {
        this.defi = defi;
    }

    public CarBean() {
    }

    public CarBean(String thumbnail, String price, String mileage, String make,
                   String carId, String lat, String distance, String leftMileage,
                   String modifyTime, String lng, String model, String defi) {
        super();
        this.thumbnail = thumbnail;
        this.price = price;
        this.mileage = mileage;
        this.make = make;
        this.carId = carId;
        this.lat = lat;
        this.distance = distance;
        this.leftMileage = leftMileage;
        this.modifyTime = modifyTime;
        this.lng = lng;
        this.model = model;
        this.defi = defi;
    }

    public String toString() {
        return "CarListBean [thumbnail = " + thumbnail + ", price = " + price
                + ", mileage = " + mileage + ", make = " + make + ", carId = "
                + carId + ", lat = " + lat + ", distance = " + distance
                + ", leftMileage = " + leftMileage + ", modifyTime = "
                + modifyTime + ", lng = " + lng + ", model = " + model
                + ", defi = " + defi + "]";
    }
}