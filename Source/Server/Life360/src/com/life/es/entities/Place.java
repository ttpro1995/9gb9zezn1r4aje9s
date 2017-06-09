package com.life.es.entities;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Place implements Serializable {

    private static final long serialVersionUID = -1681593999395284156L;

    public int id;
    public String name="";
    public LatLon latlon;
    public long time = 0;
    public int zone = 500;
    public byte type = 0;
    public int idUserOwner;
    public int idGroup;
    public long createdTime;

    public Place() {
        createdTime = System.currentTimeMillis();
    }

    public Place(int id, String name, LatLon latlon, long time, int zone, byte type, int idUserOwner, int idGroup) {
        this.id = id;
        this.name = name;
        this.latlon = latlon;
        this.time = time;
        this.zone = zone;
        this.type = type;
        this.idUserOwner = idUserOwner;
        this.idGroup = idGroup;
        this.createdTime = System.currentTimeMillis();
    }

    public Place(int id, String name, double lat, double lon, long time, int zone, byte type, int idUserOwner, int idGroup) {
        this.id = id;
        this.name = name;
        this.latlon = new LatLon(lat, lon);
        this.time = time;
        this.zone = zone;
        this.type = type;
        this.idUserOwner = idUserOwner;
        this.idGroup = idGroup;
        this.createdTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLon getLatlon() {
        return latlon;
    }

    public void setLatlon(LatLon latlon) {
        this.latlon = latlon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getIdUserOwner() {
        return idUserOwner;
    }

    public void setIdUserOwner(int idUserOwner) {
        this.idUserOwner = idUserOwner;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
