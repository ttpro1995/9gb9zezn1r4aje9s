package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Group implements Serializable {

    private static final long serialVersionUID = 2109842537367450544L;

    public int id;
    public String name;
    public int idUser_Owner;
    public long createdTime;
    public long updatedTime;

    public Group() {
        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public Group(int id, String name, int idUser_Owner) {
        this.id = id;
        this.name = name;
        this.idUser_Owner = idUser_Owner;
        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdUser_Owner() {
        return idUser_Owner;
    }

    public void setIdUser_Owner(int idUser_Owner) {
        this.idUser_Owner = idUser_Owner;
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
