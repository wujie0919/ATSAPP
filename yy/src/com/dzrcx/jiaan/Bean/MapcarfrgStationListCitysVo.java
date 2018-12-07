package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 所开通城市Bean
 * Created by Administrator on 2016/4/27.
 */
public class MapcarfrgStationListCitysVo {
    private List<MapcarfrgStationVo> currentCity;
    private List<MapcarfrgStationVo> otherCity;

    public List<MapcarfrgStationVo> getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(List<MapcarfrgStationVo> currentCity) {
        this.currentCity = currentCity;
    }

    public List<MapcarfrgStationVo> getOtherCity() {
        return otherCity;
    }

    public void setOtherCity(List<MapcarfrgStationVo> otherCity) {
        this.otherCity = otherCity;
    }
}
