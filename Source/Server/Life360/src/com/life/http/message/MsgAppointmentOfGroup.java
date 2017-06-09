package com.life.http.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgAppointmentOfGroup {

    public static final int COUNT_DATA = 3;

    public int groupId;
    public int from;
    public int size;

    public static MsgAppointmentOfGroup convertObject(Map<String, Object> data) {

        if (data.size() >= COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgAppointmentOfGroup.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
