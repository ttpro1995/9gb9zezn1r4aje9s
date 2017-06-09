package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgUpdatePlace extends BaseMessage {

    public static final int COUNT_DATA = 5;

    public int appointmentId;
    public String name;
    public double lat;
    public double lon;
    public int zone;

    public static MsgUpdatePlace convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgUpdatePlace.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
