package com.dzrcx.jiaan.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 企业账号列表
 * Created by zhangyu on 15-12-16.
 */
public class EnterpriseList implements Serializable {


    private ArrayList<EnterpriseItemInfo> enterpriseCenterList;
    private int tips;


    public ArrayList<EnterpriseItemInfo> getEnterpriseCenterList() {
        return enterpriseCenterList;
    }

    public void setEnterpriseCenterList(ArrayList<EnterpriseItemInfo> enterpriseCenterList) {
        this.enterpriseCenterList = enterpriseCenterList;
    }

    public int getTips() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }
}
