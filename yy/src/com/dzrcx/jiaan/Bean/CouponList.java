package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-1-5.
 */
public class CouponList {

    private ArrayList<Coupon> availableList;
    private ArrayList<Coupon> unavailableList;

    public ArrayList<Coupon> getAvailableList() {

        return availableList;
    }

    public void setAvailableList(ArrayList<Coupon> availableList) {
        this.availableList = availableList;
    }

    public ArrayList<Coupon> getUnavailableList() {
        return unavailableList;
    }

    public void setUnavailableList(ArrayList<Coupon> unavailableList) {
        this.unavailableList = unavailableList;
    }
}
