package com.life.socket.entities;

import com.life.common.JsonUtils;
import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Packet {

    private int uid;
    private Map<String, Object> data;

    public Packet() {
    }

    public Packet(int uid, Map<String, Object> data) {
        this.uid = uid;
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public int getSourceId() {
        return uid;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
