package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-8-24.
 */
public class EnterpriseListBean extends YYBaseResBean {

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
