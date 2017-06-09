package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Conversation implements Serializable {

    private static final long serialVersionUID = -609045657920815048L;

    public String id;
    public String name;
    public int idRef;
    public boolean chatPrivate;
    public int idConversation;
    public int idUser;
    public long createdTime;
    public long updatedTime;

    public Conversation() {
        this.updatedTime = System.currentTimeMillis();
        this.createdTime = this.updatedTime;
    }

    public Conversation(String id, String name, int idUser, boolean chatPrivate, int idRef, int idConversation) {
        this.id = id;
        this.name = name;
        this.idRef = idRef;
        this.chatPrivate = chatPrivate;
        this.idConversation = idConversation;
        this.idUser = idUser;
        this.updatedTime = System.currentTimeMillis();
        this.createdTime = this.updatedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRef() {
        return idRef;
    }

    public void setIdRef(int idRef) {
        this.idRef = idRef;
    }

    public boolean isChatPrivate() {
        return chatPrivate;
    }

    public void setChatPrivate(boolean chatPrivate) {
        this.chatPrivate = chatPrivate;
    }

    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
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
