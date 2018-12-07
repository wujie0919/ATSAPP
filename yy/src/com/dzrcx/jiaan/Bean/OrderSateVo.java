package com.dzrcx.jiaan.Bean;

/**
 * Created by zhangyu on 16-5-3.
 */
public class OrderSateVo {


    private int entId;
    private boolean paySucceed;
    private int reasonType;
    private double rental;
    private double deptLeft;
    private double entLeft;
    private String desc;


    /**
     * @return 部门预算剩余
     */
    public double getDeptLeft() {
        return deptLeft;
    }

    public void setDeptLeft(double deptLeft) {
        this.deptLeft = deptLeft;
    }

    /**
     * @return 描述  您的企业余额已不足,可以
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    /**
     * @return 是否可以完成支付  true—可以用企业完成支付全部租金
     * false—不可以用企业支付全部租金
     */
    public boolean isPaySucceed() {
        return paySucceed;
    }

    public void setPaySucceed(boolean paySucceed) {
        this.paySucceed = paySucceed;
    }

    /**
     * 不可完成支付原因
     *
     * @return 0—完成
     * 1—部门预算不足
     * 2—企业余额不足
     */
    public int getReasonType() {
        return reasonType;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }

    public double getRental() {
        return rental;
    }

    public void setRental(double rental) {
        this.rental = rental;
    }

    /**
     * 企业剩余
     *
     * @return
     */
    public double getEntLeft() {
        return entLeft;
    }

    public void setEntLeft(double entLeft) {
        this.entLeft = entLeft;
    }
}
