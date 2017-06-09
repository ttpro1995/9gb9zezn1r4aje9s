package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Batery implements Serializable {

    private static final long serialVersionUID = -4467836434305979294L;

    public byte batery;
    public long updatedTime;

    public Batery() {
        updatedTime = System.currentTimeMillis();
    }

    public Batery(byte batery) {
        this.batery = batery;
        updatedTime = System.currentTimeMillis();
    }

    public byte getBatery() {
        return batery;
    }

    public void setBatery(byte batery) {
        this.batery = batery;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
    
    

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
