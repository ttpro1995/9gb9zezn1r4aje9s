package com.life.model;

import com.life.data.svdb.SVDB;
import com.life.entity.GroupMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupMemberModel {

    private static final Logger LOGGER = LogManager.getLogger(GroupMemberModel.class);

    public static final GroupMemberModel instance = new GroupMemberModel();

    private GroupMemberModel() {
    }

}
