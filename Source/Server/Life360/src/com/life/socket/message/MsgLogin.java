package com.life.socket.message;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MsgLogin extends BaseMessage {

    public static final int COUNT_DATA = 1;

    public String token;

    public MsgLogin() {
    }

    /**
     *
     * @param data
     * @return null if fail
     */
    public static MsgLogin convertObject(Map<String, Object> data) {

        if (data.size() > COUNT_DATA) {
            return JsonUtils.Instance.convertObject(MsgLogin.class, data);
        }
        return null;
    }

}
