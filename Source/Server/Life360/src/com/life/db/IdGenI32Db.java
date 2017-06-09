package com.life.db;

import com.life.data.idgeni32.IdGenI32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class IdGenI32Db {

    private static final Logger LOGGER = LogManager.getLogger(IdGenI32Db.class);

    public static final IdGenI32Db instance = new IdGenI32Db();

    private IdGenI32 idGenInt32 = new IdGenI32("idGenI32");

    public static final String KEY_GOURP = "group";
    public static final String KEY_TIMELINE = "timeline";
    public static final String KEY_USER = "user";
    public static final String KEY_APPOINTMENT = "appointment";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CONVERSATION = "conversation";
    public static final String KEY_CONVERSATION_REAL = "conversationReal";

    private IdGenI32Db() {
    }

    public void init() {

    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdGroup() {
        return idGenInt32.nextId(KEY_GOURP);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdTimeline() {
        return idGenInt32.nextId(KEY_TIMELINE);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdUser() {
        return idGenInt32.nextId(KEY_USER);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdAppointment() {
        return idGenInt32.nextId(KEY_APPOINTMENT);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdMessage() {
        return idGenInt32.nextId(KEY_MESSAGE);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdConversation() {
        return idGenInt32.nextId(KEY_CONVERSATION);
    }

    /**
     *
     * @return -1 if error
     */
    public synchronized Integer nextIdConversationReal() {
        return idGenInt32.nextId(KEY_CONVERSATION_REAL);
    }
}
