package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-8-18.
 * <p/>
 * 优惠清单
 */
public class BenefitItemVo implements Serializable {


    private double benefitAmount;
    private String benefitName;
    private String benefitFlag;


    public double getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(double benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public String getBenefitFlag() {
        return benefitFlag;
    }

    public void setBenefitFlag(String benefitFlag) {
        this.benefitFlag = benefitFlag;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }
}
