package com.life.http.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgUpdateAppointment {

    public static final int COUNT_DATA = 6;

    public int appointmentId;
    public String name;
    public double lat;
    public double lon;
    public long time;
    public int zone;

    public static MsgUpdateAppointment convertObject(Map<String, Object> data) {

        if (data.size() >= COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgUpdateAppointment.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
