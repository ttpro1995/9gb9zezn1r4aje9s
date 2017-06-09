package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListGroupOfUserRet {

    public List<GroupRet> groups = new ArrayList<>();

    public ListGroupOfUserRet() {
    }

    public ListGroupOfUserRet(List<GroupRet> groups) {
        this.groups = groups;
    }

    public void add(GroupRet value) {
        groups.add(value);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
