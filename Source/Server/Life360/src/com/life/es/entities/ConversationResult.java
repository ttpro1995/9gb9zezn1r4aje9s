package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ConversationResult implements Serializable {

    private static final long serialVersionUID = -434210132586972531L;

    private int error;
    private Conversation value;

    public ConversationResult() {
    }

    public ConversationResult(int error) {
        this.error = error;
    }

    public ConversationResult(int error, Conversation value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Conversation getValue() {
        return value;
    }

    public void setValue(Conversation value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
