package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListUserOfGroupRet {

    public List<UserOfGroupRet> userOfGroups = new ArrayList<>();

    public ListUserOfGroupRet() {
    }

    public ListUserOfGroupRet(List<UserOfGroupRet> userOfGroups) {
        this.userOfGroups = userOfGroups;
    }

    public void add(UserOfGroupRet value) {
        userOfGroups.add(value);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
