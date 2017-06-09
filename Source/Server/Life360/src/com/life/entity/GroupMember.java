package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 608380325565002548L;

    public boolean admin;
    public boolean notifyArrive;
    public boolean notifyLeave;
    public int idUser;
    public int idGroup;
    public long createdTime;

    public GroupMember() {
        createdTime = System.currentTimeMillis();
    }

    public GroupMember(boolean admin, boolean notifyArrive, boolean notifyLeave, int idUser, int idGroup) {
        this.admin = admin;
        this.notifyArrive = notifyArrive;
        this.notifyLeave = notifyLeave;
        this.idUser = idUser;
        this.idGroup = idGroup;
        createdTime = System.currentTimeMillis();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isNotifyArrive() {
        return notifyArrive;
    }

    public void setNotifyArrive(boolean notifyArrive) {
        this.notifyArrive = notifyArrive;
    }

    public boolean isNotifyLeave() {
        return notifyLeave;
    }

    public void setNotifyLeave(boolean notifyLeave) {
        this.notifyLeave = notifyLeave;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
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
