/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.life.entityret;

import com.life.common.JsonUtils;

/**
 *
 * @author truongnguyen
 */
public class LoginRet {

    public int uid;
    public String token;

    public LoginRet() {
    }

    public LoginRet(int uid, String token) {
        this.uid = uid;
        this.token = token;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
