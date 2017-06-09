package com.life.entityret;

import com.life.common.JsonUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupShareRet {

    public int groupId;
    public String code;

    public GroupShareRet() {
    }

    public GroupShareRet(int groupId, String code) {
        this.groupId = groupId;
        this.code = code;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
