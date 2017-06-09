package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.GroupShare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupShareDb extends I32VDB<GroupShare> {

    private static final Logger LOGGER = LogManager.getLogger(GroupShareDb.class);

    public static GroupShareDb instance = new GroupShareDb("GroupShare");

    private GroupShareDb(String name) {
        super(name);
    }

    public void init() {

    }

}
