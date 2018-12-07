package com.dzrcx.jiaan.Bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhangyu on 16-5-16.
 */
public class StationListBean extends YYBaseResBean {


    private ArrayList<HashMap<String, ArrayList<StationVo>>> returnContent;

    public ArrayList<HashMap<String, ArrayList<StationVo>>> getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(ArrayList<HashMap<String, ArrayList<StationVo>>> returnContent) {
        this.returnContent = returnContent;
    }
}
