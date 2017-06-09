package com.life.db;

import com.life.common.ECodeHelper;
import com.life.common.ESConfiguration;
import com.life.common.JsonUtils;
import com.life.es.ActionResult;
import com.life.es.entities.Conversation;
import com.life.es.entities.ConversationResult;
import com.life.es.entities.ConversationSearchResult;
import com.life.es.handler.ConversationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ConversationDb {

    private static final Logger LOGGER = LogManager.getLogger(ConversationDb.class);

    private ConversationController ac = ConversationController.getInstance(ESConfiguration.instance.getIndex());

    public static ConversationDb instance = new ConversationDb();

    private ConversationDb() {
        ac.start();
        ac.createMapping();
    }

    public void init() {

    }

    /**
     *
     * @param conversation
     * @return SUCCESS, -FAIL, -EXCEPTION, -INVALID_DATA
     */
    public int indexConversation(Conversation conversation) {

        ActionResult index = ac.index(conversation);
        return index.getError();

    }

    public Conversation get(String id) {
        ConversationResult get = ac.get(id);
        if (ECodeHelper.isSuccess(get.getError())) {
            return get.getValue();
        }
        return null;
    }

    /**
     *
     * @param id
     * @return SUCCESS, -NOT_FOUND, -FAIL, -EXCEPTION
     */
    public int remove(String id) {
        return ac.removeBy(id);

    }

    public ConversationSearchResult conversationOfUser(int idUser, int from, int size) {
        return ac.conversationOfUser(idUser, from, size);
    }

    /**
     *
     * @param idConversation
     * @return num item update
     */
    public long updateLastMessageTime(int idConversation) {
        return ac.updateLastMessageTime(idConversation);
    }

    public boolean exists(int idUser, boolean isChatPrivate, int idRef) {
        return ac.exists(idUser, isChatPrivate, idRef);
    }

    public static void main(String[] args) {

        Conversation conversation = new Conversation();
        conversation.id = "1t2";
        conversation.name = "2222";
        conversation.idRef = 2;
        conversation.chatPrivate = true;
        conversation.idConversation = 2;
        conversation.idUser = 1;

        //System.out.println("exists: " + instance.exists(1, true, 2));
        System.out.println("index: " + instance.indexConversation(conversation));
        //System.out.println("update: " + instance.updateLastMessageTime(1));
        System.out.println("get: " + JsonUtils.Instance.toJson(instance.get(conversation.id)));

       // System.out.println("remove: " + instance.remove(conversation.id));
//
        //System.out.println("list conversation: " + instance.conversationOfUser(1, 0, 10));
        //AppointmentSearchResult appointmentOfGroup = instance.appointmentOfGroup(1, System.currentTimeMillis(), 1, 10);
//
//        System.err.println("get: " + appointmentOfGroup);
        System.exit(0);
    }

}
