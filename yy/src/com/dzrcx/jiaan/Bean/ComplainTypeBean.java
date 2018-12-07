package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-8-22.
 */
public class ComplainTypeBean extends YYBaseResBean {
    private ArrayList<ComplainVo> returnContent;

    public ArrayList<ComplainVo> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(ArrayList<ComplainVo> returnContent) {
        this.returnContent = returnContent;
    }
}
