package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 车辆列表总bean
 */
public class HelpCenterBrandReContentVo extends YYBaseResBean {
    private List<HelpCenterBrandVo> returnContent;

    public List<HelpCenterBrandVo> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<HelpCenterBrandVo> returnContent) {
        this.returnContent = returnContent;
    }
}
