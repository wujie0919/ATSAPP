package com.dzrcx.jiaan.Bean;

/**
 * 车辆列表总bean
 */
public class MapcarfrgCarVo {
    private int belongCityId;
    private int boxType;
    private String brand;
    private int brandId;
    private String carMainPhoto;//价格页面大图
    private String carMainThumb;//列表中图片
    private long createTime;
    private int dayBeginHour;
    private int dayBeginMinute;
    private int id;
    private int initStationId;
    private double mileage;//总蓄力航程
    private double mileageNumber;//可蓄力航程
    private double mileageStart;//已蓄力航程
    private int nightBeginHour;
    private int nightBeginMinute;
    private String license;
    private String onListing;
    private String series;
    private int state;
    private int stationId;
    private double totalMileageNumber;//车辆总蓄力航程
    private double workDayPrice;
    private double workNightPrice;
    private double discount;
    private double startPrice;
    private double mileagePrice;
    public double getDailyRentalPrice;

    private double dailyRentalPrice;//日租价格

    public double getDailyRentalPrice() {
        return dailyRentalPrice;
    }

    public void setDailyRentalPrice(double dailyRentalPrice) {
        this.dailyRentalPrice = dailyRentalPrice;
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

    public int getBelongCityId() {
        return belongCityId;
    }

    public void setBelongCityId(int belongCityId) {
        this.belongCityId = belongCityId;
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getCarMainPhoto() {
        return carMainPhoto;
    }

    public void setCarMainPhoto(String carMainPhoto) {
        this.carMainPhoto = carMainPhoto;
    }

    public String getCarMainThumb() {
        return carMainThumb;
    }

    public void setCarMainThumb(String carMainThumb) {
        this.carMainThumb = carMainThumb;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDayBeginHour() {
        return dayBeginHour;
    }

    public void setDayBeginHour(int dayBeginHour) {
        this.dayBeginHour = dayBeginHour;
    }

    public int getDayBeginMinute() {
        return dayBeginMinute;
    }

    public void setDayBeginMinute(int dayBeginMinute) {
        this.dayBeginMinute = dayBeginMinute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitStationId() {
        return initStationId;
    }

    public void setInitStationId(int initStationId) {
        this.initStationId = initStationId;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getMileageNumber() {
        return mileageNumber;
    }

    public void setMileageNumber(double mileageNumber) {
        this.mileageNumber = mileageNumber;
    }

    public double getMileageStart() {
        return mileageStart;
    }

    public void setMileageStart(double mileageStart) {
        this.mileageStart = mileageStart;
    }

    public int getNightBeginHour() {
        return nightBeginHour;
    }

    public void setNightBeginHour(int nightBeginHour) {
        this.nightBeginHour = nightBeginHour;
    }

    public int getNightBeginMinute() {
        return nightBeginMinute;
    }

    public void setNightBeginMinute(int nightBeginMinute) {
        this.nightBeginMinute = nightBeginMinute;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getOnListing() {
        return onListing;
    }

    public void setOnListing(String onListing) {
        this.onListing = onListing;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public double getTotalMileageNumber() {
        return totalMileageNumber;
    }

    public void setTotalMileageNumber(double totalMileageNumber) {
        this.totalMileageNumber = totalMileageNumber;
    }

    public double getWorkDayPrice() {
        return workDayPrice;
    }

    public void setWorkDayPrice(double workDayPrice) {
        this.workDayPrice = workDayPrice;
    }

    public double getWorkNightPrice() {
        return workNightPrice;
    }

    public void setWorkNightPrice(double workNightPrice) {
        this.workNightPrice = workNightPrice;
    }

    public double getMileagePrice() {
        return mileagePrice;
    }

    public void setMileagePrice(double mileagePrice) {
        this.mileagePrice = mileagePrice;
    }
}
