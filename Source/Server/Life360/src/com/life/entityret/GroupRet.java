package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.Group;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupRet {

    public int id;

    public String name;

    public GroupRet() {
    }

    public GroupRet(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupRet(Group group) {
        id = group.getId();
        name = group.getName();

    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
