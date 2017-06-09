package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserDb extends I32VDB<User> {

    private static final Logger LOGGER = LogManager.getLogger(UserDb.class);

    public static UserDb instance = new UserDb("user");

    private UserDb(String name) {
        super(name);
    }

    public void init() {

    }

}
