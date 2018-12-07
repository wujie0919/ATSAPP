package com.dzrcx.jiaan.Bean;

/**
 * 获取发票历史列表
 * Created by zhangyu on 16-1-5.
 */
public class InvoiceHistoryReturn extends YYBaseResBean {

    private InvoiceHistoryBean returnContent;

    public InvoiceHistoryBean getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(InvoiceHistoryBean returnContent) {
        this.returnContent = returnContent;
    }
}
