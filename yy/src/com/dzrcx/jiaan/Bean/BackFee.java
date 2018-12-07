package com.dzrcx.jiaan.Bean;

/**
 * Created by zhangyu on 16-1-26.
 */
public class BackFee {
    private double backAmount;

    private double amount;


    public double getBenefitFee() {
        return backAmount;
    }

    public void setBenefitFee(double backAmount) {
        this.backAmount = backAmount;
    }

    public double getOriAmount() {
        return amount;
    }

    public void setOriAmount(double amount) {
        this.amount = amount;
    }
}
