package com.life.db;

import com.life.data.mapchecki32.MapCheckI32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *  key: groupId
 * @author truongnguyenax@gmail.com
 */
public class CheckMissUserChatInGroupDb extends MapCheckI32 {

    private static final Logger LOGGER = LogManager.getLogger(CheckMissUserChatInGroupDb.class);

    public static CheckMissUserChatInGroupDb instance = new CheckMissUserChatInGroupDb("CheckMissUserChatInGroup");

    private CheckMissUserChatInGroupDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {

        int groupId = -1;

        System.err.println("put: " + instance.put(groupId, true));
        System.err.println("get: " + instance.get(groupId));

        System.exit(0);
    }
}
