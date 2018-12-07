package com.dzrcx.jiaan.Bean;

import java.io.Serializable;

/**
 * Created by zhangyu on 16-5-17.
 */
public class XY implements Serializable {
    private GPSXY gpsXY;
    private String location;

    public GPSXY getGpsXY() {
        return gpsXY;
    }

    public void setGpsXY(GPSXY gpsXY) {
        this.gpsXY = gpsXY;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
