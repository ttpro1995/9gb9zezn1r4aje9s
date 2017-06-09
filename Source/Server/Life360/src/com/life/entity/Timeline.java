package com.life.entity;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Timeline implements Serializable {

    private static final long serialVersionUID = -8851686518398031247L;

    public int id;
    public String content;
    public LatLon latLon;
    public long fromTime;
    public long toTime;
    public int zone;
    public byte type = 0;
    public int idUser;
    public int idAppointment;

    public Timeline() {
    }

    public Timeline(int id, String content, double lat, double lon, long fromTime, long toTime, int zone, byte type, int idUser, int idAppointment) {
        this.id = id;
        this.content = content;
        this.latLon = new LatLon(lat, lon);
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.zone = zone;
        this.type = type;
        this.idUser = idUser;
        this.idAppointment = idAppointment;
    }

    public Timeline(int id, String content, double lat, double lon, long fromTime, long toTime, int zone, byte type, int idUser) {
        this.id = id;
        this.content = content;
        this.latLon = new LatLon(lat, lon);
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.zone = zone;
        this.type = type;
        this.idUser = idUser;
    }

    public Timeline(int id, String content, LatLon latLon, long fromTime, int zone, byte type, int idUser, int idAppointment) {
        this.id = id;
        this.content = content;
        this.latLon = latLon;
        this.fromTime = fromTime;
        this.zone = zone;
        this.type = type;
        this.idUser = idUser;
        this.idAppointment = idAppointment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LatLon getLatLon() {
        return latLon;
    }

    public void setLatLon(LatLon latLon) {
        this.latLon = latLon;
    }

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(int idAppointment) {
        this.idAppointment = idAppointment;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
