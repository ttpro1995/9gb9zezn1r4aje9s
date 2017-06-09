package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageWatched implements Serializable {

    private static final long serialVersionUID = 887475843617791024L;

    /**
     * Key
     */
    public int idConversation;
    
    public int idMessage;
    public long updatedTime;

    public MessageWatched() {
    }

    public MessageWatched(int idConversation, int idMessage) {
        this.idConversation = idConversation;
        this.idMessage = idMessage;
        updatedTime = System.currentTimeMillis();
    }

    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
