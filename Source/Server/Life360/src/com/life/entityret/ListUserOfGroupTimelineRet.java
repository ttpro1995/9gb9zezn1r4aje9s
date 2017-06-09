package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListUserOfGroupTimelineRet {

    public List<UserOfGroupTimelineRet> users = new ArrayList<>();

    public ListUserOfGroupTimelineRet() {
    }

    public ListUserOfGroupTimelineRet(List<UserOfGroupTimelineRet> groups) {
        this.users = groups;
    }

    public void add(UserOfGroupTimelineRet value) {
        users.add(value);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
