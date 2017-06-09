package com.life.db;

import com.life.data.i32db.I32VDB;
import com.life.entity.MessageReceived;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageReceivedDb extends I32VDB<MessageReceived> {

    private static final Logger LOGGER = LogManager.getLogger(MessageReceivedDb.class);

    public static MessageReceivedDb instance = new MessageReceivedDb("MessageReceived");

    private MessageReceivedDb(String name) {
        super(name);
    }

    public void init() {

    }
    
     public static void main(String[] args) {
        MessageReceived b = new MessageReceived();
        b.idConversation = -1;
        b.idMessage = 1;
        

        System.err.println("put: " + instance.put(b.idConversation, b));
        System.err.println("get: " + instance.get(b.idConversation));

        System.exit(0);
    }
}
