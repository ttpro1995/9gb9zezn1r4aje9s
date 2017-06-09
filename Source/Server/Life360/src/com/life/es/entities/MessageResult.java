package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageResult implements Serializable {

    private static final long serialVersionUID = 1979279502310788490L;

    private int error;
    private Message value;

    public MessageResult() {
    }

    public MessageResult(int error) {
        this.error = error;
    }

    public MessageResult(int error, Message value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Message getValue() {
        return value;
    }

    public void setValue(Message value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
