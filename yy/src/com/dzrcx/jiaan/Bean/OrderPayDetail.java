package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-8-18.
 */
public class OrderPayDetail implements Serializable {

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
