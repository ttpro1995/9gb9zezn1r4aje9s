package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgPushTimeline extends BaseMessage {

    public static final int COUNT_DATA = 6;

    public String content;
    public double lat;
    public double lon;
    public long fromTime;
    public long toTime;
    public int zone;

    public static MsgPushTimeline convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgPushTimeline.class, data);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
