package com.life.entity.base;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LatLon implements Serializable{

    private static final long serialVersionUID = 3167869797825487936L;

    double lat;
    double lon;

    public LatLon() {
    }

    public LatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
    return JsonUtils.Instance.toJson(this);
    }
    
    
    
    
}
