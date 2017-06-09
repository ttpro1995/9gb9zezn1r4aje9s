package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgBatery extends BaseMessage {

    public static final int COUNT_DATA = 1;

    public byte batery;

    public static MsgBatery convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgBatery.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
