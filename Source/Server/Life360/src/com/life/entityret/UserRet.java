package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.User;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserRet {

    public int id;

    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String avatar;

    public UserRet() {
    }

    public UserRet(int id, String firstName, String lastName, String email, String phoneNumber, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    public UserRet(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.avatar = user.getAvatar();
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
