package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListUserRet {

    public int total = 0;
    public List<UserRet> users = new ArrayList<>();

    public ListUserRet() {
    }

    public ListUserRet(int total, List<UserRet> users) {
        this.total = total;
        this.users = users;
    }

    public void add(UserRet userRet) {
        users.add(userRet);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
