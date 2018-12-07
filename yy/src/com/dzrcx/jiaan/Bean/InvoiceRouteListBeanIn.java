package com.dzrcx.jiaan.Bean;

public class InvoiceRouteListBeanIn {
    private double amount;
    private int orderId;
    private long chargeTimes;
    private long endTimes;
    private Boolean checked = false;

    public double getAmount() {
        return amount;
    }

    public int getOrderId() {
        return orderId;
    }

    public long getChargeTimes() {
        return chargeTimes;
    }

    public long getEndTimes() {
        return endTimes;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setChargeTimes(long chargeTimes) {
        this.chargeTimes = chargeTimes;
    }

    public void setEndTimes(long endTimes) {
        this.endTimes = endTimes;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
