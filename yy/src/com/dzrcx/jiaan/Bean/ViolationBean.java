package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-3-31.
 */
public class ViolationBean implements Serializable {
    private int id;
    /**
     * 0—	未处理
     * 1—	处理中
     * 2—	已处理
     */
    private int dealState;
    private String type;
    /**
     * 1—违章
     * 2—事故
     */
    private int vType;
    private String orderId;
    private long vioTime;//违章时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDealState() {
        return dealState;
    }

    public void setDealState(int dealState) {
        this.dealState = dealState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getvType() {
        return vType;
    }

    public void setvType(int vType) {
        this.vType = vType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getVioTime() {
        return vioTime;
    }

    public void setVioTime(long vioTime) {
        this.vioTime = vioTime;
    }
}



