package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupDb extends I32VDB<Group> {

    private static final Logger LOGGER = LogManager.getLogger(GroupDb.class);

    public static GroupDb instance = new GroupDb("Group");

    private GroupDb(String name) {
        super(name);
    }

    public void init() {

    }
}
