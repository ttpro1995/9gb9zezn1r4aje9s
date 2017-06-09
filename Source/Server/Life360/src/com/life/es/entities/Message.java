package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 7694609540037978844L;

    public int id;
    public byte typeData;
    public String data;
    public int idConversation;
    public int idUserOwner;

    public long createdTime;

    public Message() {
        this.createdTime = System.currentTimeMillis();
    }

    public Message(int id, byte typeData, String data, int idConversation, int idUserOwner) {
        this.id = id;
        this.typeData = typeData;
        this.data = data;
        this.idConversation = idConversation;
        this.idUserOwner = idUserOwner;
        this.createdTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getTypeData() {
        return typeData;
    }

    public void setTypeData(byte typeData) {
        this.typeData = typeData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public int getIdUserOwner() {
        return idUserOwner;
    }

    public void setIdUserOwner(int idUserOwner) {
        this.idUserOwner = idUserOwner;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
