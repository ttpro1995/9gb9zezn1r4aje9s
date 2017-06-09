package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.GroupMember;
import com.life.entity.User;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserOfGroupRet {

    public int id;
    public String firstName;
    public String lastName;
    public String avatar;
    public boolean admin;

    public UserOfGroupRet() {
    }

    public UserOfGroupRet(int id, String firstName, String lastName, String avatar, boolean admin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.admin = admin;
    }

    public UserOfGroupRet(User user, GroupMember groupMember) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.avatar = user.getAvatar();
        this.admin = groupMember.admin;
    }
    
     @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
