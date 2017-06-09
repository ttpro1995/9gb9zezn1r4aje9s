package com.life.http.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgCreateAppointment {

    public static final int COUNT_DATA = 6;

    public int groupId;
    public String name;
    public double lat;
    public double lon;
    public long time;
    public int zone;

    public static MsgCreateAppointment convertObject(Map<String, Object> data) {

        if (data.size() >= COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgCreateAppointment.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
