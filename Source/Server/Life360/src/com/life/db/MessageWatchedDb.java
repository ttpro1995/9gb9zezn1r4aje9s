package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.MessageReceived;
import com.life.entity.MessageWatched;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageWatchedDb extends I32VDB<MessageWatched> {

    private static final Logger LOGGER = LogManager.getLogger(MessageWatchedDb.class);

    public static MessageWatchedDb instance = new MessageWatchedDb("MessageWatched");

    private MessageWatchedDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {
        MessageWatched b = new MessageWatched();
        b.idConversation = -1;
        b.idMessage = 2;

        System.err.println("put: " + instance.put(b.idConversation, b));
        System.err.println("get: " + instance.get(b.idConversation));

        System.exit(0);
    }
}
