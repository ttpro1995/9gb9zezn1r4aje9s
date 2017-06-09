package com.life.db;

import com.life.data.k32list32.K32List32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class FriendsDb extends K32List32 {

    private static final Logger LOGGER = LogManager.getLogger(FriendsDb.class);

    public static FriendsDb instance = new FriendsDb("Friends");

    private FriendsDb(String name) {
        super(name);
    }

    public void init() {

    }

}
