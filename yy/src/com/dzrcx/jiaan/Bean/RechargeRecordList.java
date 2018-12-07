package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-1-6.
 */
public class RechargeRecordList {

    private int nextPage;
    private ArrayList<RechargeRecord> rechargeRecordList;

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public ArrayList<RechargeRecord> getRechargeRecordList() {
        return rechargeRecordList;
    }

    public void setRechargeRecordList(ArrayList<RechargeRecord> rechargeRecordList) {
        this.rechargeRecordList = rechargeRecordList;
    }
}
