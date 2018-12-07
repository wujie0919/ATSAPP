package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 总bean
 */
public class CouponADBeanReturnContent extends YYBaseResBean {
    private List<CouponADBean> returnContent;

    public List<CouponADBean> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<CouponADBean> returnContent) {
        this.returnContent = returnContent;
    }
}
