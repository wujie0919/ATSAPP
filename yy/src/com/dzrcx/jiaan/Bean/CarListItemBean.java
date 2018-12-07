package com.dzrcx.jiaan.Bean;

/**
 * 汽车信息Bean
 */
public class CarListItemBean {
    private int type;
    private int number = -1;
    private MapcarListStationAndCarsVo mapcarListStationAndCarsVo;

    public CarListItemBean(int type, int number, MapcarListStationAndCarsVo mapcarListStationAndCarsVo) {
        this.type = type;
        this.number = number;
        this.mapcarListStationAndCarsVo = mapcarListStationAndCarsVo;
    }

    /**
     * 站点View
     *
     * @param mapcarListStationAndCarsVo
     * @param type
     */
    public CarListItemBean(int type, MapcarListStationAndCarsVo mapcarListStationAndCarsVo) {
        this.mapcarListStationAndCarsVo = mapcarListStationAndCarsVo;
        this.type = type;
    }

    /**
     * 背景View
     *
     * @param type
     */
    public CarListItemBean(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public MapcarListStationAndCarsVo getMapcarListStationAndCarsVo() {
        return mapcarListStationAndCarsVo;
    }

    public void setMapcarListStationAndCarsVo(MapcarListStationAndCarsVo mapcarListStationAndCarsVo) {
        this.mapcarListStationAndCarsVo = mapcarListStationAndCarsVo;
    }
}