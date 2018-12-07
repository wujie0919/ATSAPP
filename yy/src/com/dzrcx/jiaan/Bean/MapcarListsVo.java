package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 车辆列表
 */
public class MapcarListsVo {
    private List<MapcarListStationAndCarsVo> stationCarCountGTZERO;//有车站点排列
    private List<MapcarListStationAndCarsVo> stationCarCountLTZERO;//混排列

    public List<MapcarListStationAndCarsVo> getStationCarCountGTZERO() {
        return stationCarCountGTZERO;
    }

    public void setStationCarCountGTZERO(List<MapcarListStationAndCarsVo> stationCarCountGTZERO) {
        this.stationCarCountGTZERO = stationCarCountGTZERO;
    }

    public List<MapcarListStationAndCarsVo> getStationCarCountLTZERO() {
        return stationCarCountLTZERO;
    }

    public void setStationCarCountLTZERO(List<MapcarListStationAndCarsVo> stationCarCountLTZERO) {
        this.stationCarCountLTZERO = stationCarCountLTZERO;
    }
}
