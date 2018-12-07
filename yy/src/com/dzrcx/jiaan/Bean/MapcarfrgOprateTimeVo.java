package com.dzrcx.jiaan.Bean;

/**
 * 车辆列表总bean
 */
public class MapcarfrgOprateTimeVo {
    private int flag;
    private int stationId;
    private String message;
    private String remainTimeDesc;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemainTimeDesc() {
        return remainTimeDesc;
    }

    public void setRemainTimeDesc(String remainTimeDesc) {
        this.remainTimeDesc = remainTimeDesc;
    }
}
