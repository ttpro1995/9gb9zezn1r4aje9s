package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TokenFirebase implements Serializable {

    private static final long serialVersionUID = -7026923965657230677L;

    public String tokenFirebase;
    public long updatedTime;

    public TokenFirebase() {
        updatedTime = System.currentTimeMillis();

    }

    public TokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
        updatedTime = System.currentTimeMillis();
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
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
