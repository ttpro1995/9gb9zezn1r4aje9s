package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineRet {

    public int id;
    public String content;
    public LatLon latLon;
    public long fromTime;
    public long toTime;
    public int zone;
    public byte type;

    public TimelineRet() {
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
