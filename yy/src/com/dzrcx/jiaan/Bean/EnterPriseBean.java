package com.dzrcx.jiaan.Bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 企业员工账号bean
 *
 * @author Administrator
 */
public class EnterPriseBean implements Serializable {
    private int id;

    private String name;// 企业名称

    private long createTime;

    private int inUse;// 企业是否在用

    private int balanceOut;// 企业是否余额充足

    private int limitType;//部门限额类型
    private int allowArrearage;//企业余额是否可以为负
    private BigDecimal balance;//企业余额

    private BigDecimal deptLeft;// 部门预算余额

    private BigDecimal amount;//部门额度


    public int getAllowArrearage() {
        return allowArrearage;
    }

    public void setAllowArrearage(int allowArrearage) {
        this.allowArrearage = allowArrearage;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getBalanceOut() {
        return balanceOut;
    }

    public void setBalanceOut(int balanceOut) {
        this.balanceOut = balanceOut;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getDeptLeft() {
        return deptLeft;
    }

    public void setDeptLeft(BigDecimal deptLeft) {
        this.deptLeft = deptLeft;
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

    public int getLimitType() {
        return limitType;
    }

    public void setLimitType(int limitType) {
        this.limitType = limitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
