package com.dzrcx.jiaan.clicklistener;

import com.dzrcx.jiaan.Bean.StationVo;

import java.util.ArrayList;

/**
 * Created by zhangyu on 16-5-16.
 */
public interface OnAreaClick {
    void onArea(String area, ArrayList<StationVo> stationVos);
}
