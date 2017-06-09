package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgUserOfGroupTimeline extends BaseMessage {

    public static final int COUNT_DATA = 1;

    public int groupId;

    public MsgUserOfGroupTimeline() {
    }

    /**
     *
     * @param data
     * @return null if fail
     */
    public static MsgUserOfGroupTimeline convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgUserOfGroupTimeline.class, data);
        }
        return null;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
