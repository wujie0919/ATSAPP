package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-8-18.
 */
public class BenefitDetailVo implements Serializable {

    private double benefitCouponPrice;
    private double benefitHourPrice;
    private double benefitMileagePrice;

    public double getBenefitCouponPrice() {
        return benefitCouponPrice;
    }

    public void setBenefitCouponPrice(double benefitCouponPrice) {
        this.benefitCouponPrice = benefitCouponPrice;
    }

    public double getBenefitHourPrice() {
        return benefitHourPrice;
    }

    public void setBenefitHourPrice(double benefitHourPrice) {
        this.benefitHourPrice = benefitHourPrice;
    }

    public double getBenefitMileagePrice() {
        return benefitMileagePrice;
    }

    public void setBenefitMileagePrice(double benefitMileagePrice) {
        this.benefitMileagePrice = benefitMileagePrice;
    }
}
