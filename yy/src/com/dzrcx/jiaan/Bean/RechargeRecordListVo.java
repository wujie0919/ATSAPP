package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-7-15.
 */
public class RechargeRecordListVo implements Serializable {
    private long createTime;
    private int id;
    private double tradeAmount;
    private String tradeName;
    private int tradeType;
    private long tradesTime;
    private int userId;
    private int type;
    private boolean islast;

    public boolean islast() {
        return islast;
    }

    public void setIslast(boolean islast) {
        this.islast = islast;
    }

    public RechargeRecordListVo(int type, long tradesTime) {
        this.type = type;
        this.tradesTime = tradesTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getTradesTime() {
        return tradesTime;
    }

    public void setTradesTime(long tradesTime) {
        this.tradesTime = tradesTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
