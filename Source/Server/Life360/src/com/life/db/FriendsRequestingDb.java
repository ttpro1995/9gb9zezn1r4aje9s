package com.life.db;

import com.life.data.k32list32.K32List32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class FriendsRequestingDb extends K32List32 {

    private static final Logger LOGGER = LogManager.getLogger(FriendsRequestingDb.class);

    public static FriendsRequestingDb instance = new FriendsRequestingDb("FriendsRequesting");

    private FriendsRequestingDb(String name) {
        super(name);
    }

    public void init() {

    }

}
