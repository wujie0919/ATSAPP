package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-7-14.
 */
public class CountOrderVo implements Serializable {
    private int mileages;
    private double rentals;
    private long times;

    public int getMileages() {
        return mileages;
    }

    public void setMileages(int mileages) {
        this.mileages = mileages;
    }

    public double getRentals() {
        return rentals;
    }

    public void setRentals(double rentals) {
        this.rentals = rentals;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }
}
