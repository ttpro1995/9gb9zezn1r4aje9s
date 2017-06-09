package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupShare implements Serializable {

    private static final long serialVersionUID = 2392069266752385139L;

    public int idGroup;
    public String code;
    public long expire;
    public long createdTime;

    public GroupShare() {
        createdTime = System.currentTimeMillis();
    }

    public GroupShare(int idGroup, String code) {
        this.idGroup = idGroup;
        this.code = code;
        this.expire = -1;
        createdTime = System.currentTimeMillis();
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getCreateTime() {
        return createdTime;
    }

    public void setCreateTime(long createTime) {
        this.createdTime = createTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
