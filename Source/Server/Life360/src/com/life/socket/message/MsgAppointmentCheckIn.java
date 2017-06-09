package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgAppointmentCheckIn extends BaseMessage {

    public static final int COUNT_DATA = 2;

    public int groupId;
    public int idAppointment;   

    public static MsgAppointmentCheckIn convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgAppointmentCheckIn.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
