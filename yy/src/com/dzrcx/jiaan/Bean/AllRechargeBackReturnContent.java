package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 车辆列表总bean
 */
public class AllRechargeBackReturnContent extends YYBaseResBean {
    private List<RechargeBackBean> returnContent;

    public List<RechargeBackBean> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<RechargeBackBean> returnContent) {
        this.returnContent = returnContent;
    }
}
