package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/22.
 */
public class FeelBackContentReturnBean extends YYBaseResBean {
    private List<FeelBackContentBean> returnContent;

    public List<FeelBackContentBean> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<FeelBackContentBean> returnContent) {
        this.returnContent = returnContent;
    }
}
