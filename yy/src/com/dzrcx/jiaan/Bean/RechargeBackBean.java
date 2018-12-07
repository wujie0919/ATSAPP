package com.dzrcx.jiaan.Bean;

/**
 * Created by Administrator on 2016/5/12.
 * 充值返现卡Bean
 */
public class RechargeBackBean {

    private int amount;
    private int backAmount;
    private long createTime;
    private int id;
    private int inUse;
    private long updateTime;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(int backAmount) {
        this.backAmount = backAmount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInUse() {
        return inUse;
    }

    public void setInUse(int inUse) {
        this.inUse = inUse;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
