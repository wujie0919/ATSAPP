package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * 总bean
 */
public class BreakRulesDetailBean implements Serializable {
    private int id;
    private int vType;//事故类型
    private int orderId;
    private long pickTime;
    private long vioTime;
    private long endTime;
    private String type;//行为
    private int dealState;//处理状态
    private String address;
    private int points;//扣分
    private double amount;//罚款
    private double needChargeAmount;
    private String desc;
    private String nature;
    private double repairCost;//维修费用
    private double outageLoss;//停运费用
    private double deductCashAmount;//需支付金
    //    0—未支付
//    1—已支付
    private int deductCashState;//支付状态
    /**
     * 0—未确认
     * 1—已确认
     * 为0时，需支付金额
     * 处显示‘正在核算’ 以需求图为准
     */
    private int confirmState;//金额计算是否确认

    public double getNeedChargeAmount() {
        return needChargeAmount;
    }

    public void setNeedChargeAmount(double needChargeAmount) {
        this.needChargeAmount = needChargeAmount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getPickTime() {
        return pickTime;
    }

    public void setPickTime(long pickTime) {
        this.pickTime = pickTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getvType() {
        return vType;
    }

    public void setvType(int vType) {
        this.vType = vType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getVioTime() {
        return vioTime;
    }

    public void setVioTime(long vioTime) {
        this.vioTime = vioTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDealState() {
        return dealState;
    }

    public void setDealState(int dealState) {
        this.dealState = dealState;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(double repairCost) {
        this.repairCost = repairCost;
    }

    public double getOutageLoss() {
        return outageLoss;
    }

    public void setOutageLoss(double outageLoss) {
        this.outageLoss = outageLoss;
    }

    public double getDeductCashAmount() {
        return deductCashAmount;
    }

    public void setDeductCashAmount(double deductCashAmount) {
        this.deductCashAmount = deductCashAmount;
    }

    public int getDeductCashState() {
        return deductCashState;
    }

    public void setDeductCashState(int deductCashState) {
        this.deductCashState = deductCashState;
    }

    public int getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(int confirmState) {
        this.confirmState = confirmState;
    }
}
