package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserOfGroupTimelineRet {

    public int id;
    public String firstName;
    public String lastName;
    public String avatar;
    public LatLon location;
    // public String nameLoc="";
    public byte batery = -1;

    public UserOfGroupTimelineRet() {
    }

    public UserOfGroupTimelineRet(int id, String firstName, String lastName, String avatar, LatLon location,  byte batery) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.location = location;
       // this.nameLoc = nameLoc;
        this.batery = batery;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
