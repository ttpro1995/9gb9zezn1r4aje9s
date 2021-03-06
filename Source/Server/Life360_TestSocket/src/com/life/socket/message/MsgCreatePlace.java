package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgCreatePlace extends BaseMessage {

    public static final int COUNT_DATA = 6;

    public int groupId;
    public String name;
    public double lat;
    public double lon;
    public int zone;
    public byte type;

    public static MsgCreatePlace convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgCreatePlace.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
