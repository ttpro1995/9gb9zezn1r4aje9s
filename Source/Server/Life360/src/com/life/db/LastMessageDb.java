package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.es.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LastMessageDb extends I32VDB<Message> {

    private static final Logger LOGGER = LogManager.getLogger(LastMessageDb.class);

    public static LastMessageDb instance = new LastMessageDb("LastMessage");

    private LastMessageDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {
        Message b = new Message(-1, (byte) 1, "temp", -1, -1);

        System.err.println("put: " + instance.put(b.idConversation, b));
        System.err.println("get: " + instance.get(b.idConversation));

        System.exit(0);
    }
}
