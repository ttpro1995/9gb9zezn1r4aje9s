package com.life.entity;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Location implements Serializable {

    private static final long serialVersionUID = 8567903347604681463L;

    public LatLon latLon;
    public String name;
    public long updatedTime;

    public Location() {
    }

    public Location(String name, double lat, double lon) {
        this.name = name;
        latLon = new LatLon(lat, lon);
        updatedTime = System.currentTimeMillis();

    }

    public LatLon getLatLon() {
        return latLon;
    }

    public void setLatLon(LatLon latLon) {
        this.latLon = latLon;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
