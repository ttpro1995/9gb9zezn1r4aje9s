package com.life.api;

import com.life.common.JsonUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class SApiMessage {

    public int command;
    public Object apiMessage;

    public SApiMessage() {
    }

    public SApiMessage(int command, Object apiMessage) {
        this.command = command;
        this.apiMessage = apiMessage;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
