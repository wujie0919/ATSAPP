package com.dzrcx.jiaan.Bean;

/**
 * 微信支付订单
 *
 * @author zhangyu
 */
public class WXPayInfoBeanReturnContent extends YYBaseResBean {

    private WXPayInfoVo returnContent;

    public WXPayInfoVo getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(WXPayInfoVo returnContent) {
        this.returnContent = returnContent;
    }
}
