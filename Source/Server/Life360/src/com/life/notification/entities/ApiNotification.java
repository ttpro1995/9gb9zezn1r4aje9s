package com.life.notification.entities;

import com.life.common.JsonUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ApiNotification {
    
    public int command;
    public Object data;

    public ApiNotification(int command, Object data) {
        this.command = command;
        this.data = data;
    }

   

   
    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
