package com.dzrcx.jiaan.Bean;

/**
 * Created by zhangyu on 16-7-26.
 */
public class WXPayInfoVo {
    private WXPayBean reqCode;

    private double allFee;

    public WXPayBean getReqCode() {
        return reqCode;
    }

    public void setReqCode(WXPayBean reqCode) {
        this.reqCode = reqCode;
    }

    public double getAllFee() {
        return allFee;
    }

    public void setAllFee(double allFee) {
        this.allFee = allFee;
    }
}
