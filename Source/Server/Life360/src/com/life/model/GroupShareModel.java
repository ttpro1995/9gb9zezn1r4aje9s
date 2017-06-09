package com.life.model;

import com.life.api.ApiMessage;
import com.life.backend.GroupShareBE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupShareModel {

    private static final Logger LOGGER = LogManager.getLogger(GroupShareModel.class);

    public static final GroupShareModel instance = new GroupShareModel();

    private GroupShareModel() {
    }

    public ApiMessage getCode(int uid, int groupId) {
        return GroupShareBE.instance.getCode(uid, groupId);
    }

}
