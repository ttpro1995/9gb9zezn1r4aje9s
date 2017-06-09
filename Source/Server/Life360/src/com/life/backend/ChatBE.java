package com.life.backend;

import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.ConversationDb;
import com.life.db.GroupDb;
import com.life.db.IdGenI32Db;
import com.life.db.LastMessageDb;
import com.life.db.MessageDb;
import com.life.db.UserDb;
import com.life.db.UserOfGroupDb;
import com.life.entity.Group;
import com.life.entity.User;
import com.life.es.entities.Conversation;
import com.life.es.entities.Message;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ChatBE {

    private static final Logger LOGGER = LogManager.getLogger(ChatBE.class);

    public static ChatBE instance = new ChatBE();

    private ChatBE() {
    }

    public int sendMessage(Message message) {

        if (message == null || message.id <= 0) {
            return -ECode.INVALID_DATA.value;
        }

        int index = MessageDb.instance.index(message);
        if (ECodeHelper.isSuccess(index)) {
            ConversationDb.instance.updateLastMessageTime(message.idConversation);
            LastMessageDb.instance.put(message.idConversation, message);
        }
        return index;

    }

    public int createConversation(int idConversation, int idUser, boolean isChatPrivate, int idRef) {
        if (idConversation <= 0 || idUser <= 0 || idRef <= 0) {
            return -ECode.INVALID_DATA.value;
        }

        int result;
        if (isChatPrivate) {
            result = createConversationPrivate(idConversation, idUser, idRef);
        } else {
            result = createConversationGroup(idConversation, idUser, idRef);
        }

        return result;

    }

    private int createConversationPrivate(int idConversation, int uidFrom, int uidTo) {

        //ConversationDb
        List<Integer> ids = Arrays.asList(uidFrom, uidTo);
        Map<Integer, User> users = UserDb.instance.multiGet(ids);
        User userFrom = users.get(uidFrom);
        User userTo = users.get(uidTo);
        if (userFrom != null && userTo != null) {

            boolean friend = FriendsBE.instance.isFriend(uidFrom, uidTo);
            if (friend) {
                Conversation c = new Conversation(String.format("%dt%d", uidFrom, uidTo),
                        userTo.firstName + " " + userTo.lastName,
                        uidFrom,
                        true,
                        uidTo,
                        idConversation);

                int indexC = ConversationDb.instance.indexConversation(c);
                if (ECodeHelper.isSuccess(indexC)) {

                    Conversation c2 = new Conversation(String.format("%dt%d", uidTo, uidFrom),
                            userFrom.firstName + " " + userFrom.lastName,
                            uidTo,
                            true,
                            uidFrom,
                            idConversation);

                    indexC = ConversationDb.instance.indexConversation(c2);
                    if (ECodeHelper.isSuccess(indexC)) {
                        return indexC;
                    } else {
                        ConversationDb.instance.remove(c.id);
                        return indexC;
                    }

                }
            } else {
                return -ECode.PERMISSION_DENY.value;

            }
        }
        return -ECode.FAIL.value;
    }

    private int createConversationGroup(int idConversation, int uid, int groupId) {

        Group group = GroupDb.instance.get(groupId);
        if (group != null) {
            Value valueR = UserOfGroupDb.instance.get(groupId);
            if (ECodeHelper.isSuccess(valueR.error)) {
                List<Integer> uids = valueR.value;
                if (uids.contains(uid)) {
                    Map<Integer, User> users = UserDb.instance.multiGet(uids);
                    int coutSucc = 0;
                    Conversation c;
                    for (int uidT : uids) {
                        User user = users.get(uidT);
                        if (user != null) {
                            c = new Conversation(String.format("%df%d", uidT, groupId),
                                    group.name,
                                    uidT,
                                    false,
                                    groupId,
                                    idConversation);

                            int indexConversation = ConversationDb.instance.indexConversation(c);
                            if (ECodeHelper.isSuccess(indexConversation)) {
                                coutSucc++;
                            }
                        }
                    }
                    return coutSucc;

                } else {
                    return -ECode.PERMISSION_DENY.value;
                }

            } else {
                return valueR.error;
            }
        }

        return -ECode.NOT_FOUND.value;

    }

}
