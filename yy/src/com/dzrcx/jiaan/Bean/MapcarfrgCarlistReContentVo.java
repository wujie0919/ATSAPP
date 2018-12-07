package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 车辆列表总bean
 */
public class MapcarfrgCarlistReContentVo extends YYBaseResBean {
    private List<MapcarfrgCarVo> returnContent;

    public List<MapcarfrgCarVo> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(List<MapcarfrgCarVo> returnContent) {
        this.returnContent = returnContent;
    }
}
