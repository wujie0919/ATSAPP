package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * Created by zhangyu on 16-1-6.
 */
public class RechargeRecordListBean extends YYBaseResBean {

    private List<RechargeRecordListVo> returnContent;

    public List<RechargeRecordListVo> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<RechargeRecordListVo> returnContent) {
        this.returnContent = returnContent;
    }
}
