package com.dzrcx.jiaan.Bean;

/**
 * Created by zhangyu on 16-7-26.
 */
public class ALiPayInfoVo {

    private String reqCode;

    /**
     * 充值优惠金额
     */
    private double allFee;

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public double getAllFee() {
        return allFee;
    }

    public void setAllFee(double allFee) {
        this.allFee = allFee;
    }
}
