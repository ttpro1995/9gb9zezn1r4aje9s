package com.life.db;

import com.life.common.ECodeHelper;
import com.life.common.ESConfiguration;
import com.life.common.JsonUtils;
import com.life.es.ActionResult;
import com.life.es.entities.Message;
import com.life.es.entities.MessageResult;
import com.life.es.entities.MessageSearchResult;
import com.life.es.handler.MessageController;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageDb {

    private static final Logger LOGGER = LogManager.getLogger(MessageDb.class);

    private MessageController ac = MessageController.getInstance(ESConfiguration.instance.getIndex());

    public static MessageDb instance = new MessageDb();

    private MessageDb() {
        ac.start();
        ac.createMapping();
    }

    public void init() {

    }

    /**
     *
     * @param message
     * @return SUCCESS, -FAIL, -EXCEPTION, -INVALID_DATA
     */
    public int index(Message message) {

        ActionResult index = ac.index(message);
        return index.getError();

    }

    public Message get(int id) {
        MessageResult get = ac.get(id);
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
    public int remove(int id) {
        return ac.removeBy(id);

    }

    public MessageSearchResult messageOfConversation(int ConversationId, int from, int size) {
        return ac.getMessageOfConversation(ConversationId, from, size);
    }

    public static void main(String[] args) {

        Message message = new Message();
        message.id = 3;
        message.typeData = 1;
        message.data = "we....";

        message.idUserOwner = 2;
        message.idConversation = 1;

        System.out.println("index: " + instance.index(message));
        System.out.println("get: " + JsonUtils.Instance.toJson(instance.get(message.id)));

        //System.out.println("remove: " + instance.remove(message.id));
        System.out.println("list conversation: " + instance.messageOfConversation(1, 0, 10));
//        String[] data = new String[]{
//            "{\"_index\":\"life360\",\"_type\":\"message\",\"_id\":\"3\",\"_score\":1,\"_source\":{\"idConversation\":1,\"idUserOwner\":2,\"updatedTime\":11111,\"data\":\"we....\",\"createdTime\":1495558377343,\"id\":3,\"typeData\":1}}",
//            "{\"_index\":\"life360\",\"_type\":\"message\",\"_id\":\"1\",\"_score\":1,\"_source\":{\"idConversation\":1,\"idUserOwner\":1,\"updatedTime\":11111,\"data\":\"chat 2\",\"createdTime\":1495557977639,\"id\":1,\"typeData\":1}}",
//            "{\"_index\":\"life360\",\"_type\":\"message\",\"_id\":\"2\",\"_score\":1,\"_source\":{\"idConversation\":1,\"idUserOwner\":2,\"updatedTime\":11111,\"data\":\"we\",\"createdTime\":1495558162798,\"id\":2,\"typeData\":1}}"
//        };
//
//        for (String item : data) {
//            JSONObject convertObject = JsonUtils.Instance.convertObject(JSONObject.class, item);
//
//            Map map = (Map) convertObject.get("_source");
//            Message convertObject1 = JsonUtils.Instance.convertObject(Message.class, map);
//            System.out.println("index: " + instance.index(convertObject1));
//
//        }

        System.exit(0);
    }

}
