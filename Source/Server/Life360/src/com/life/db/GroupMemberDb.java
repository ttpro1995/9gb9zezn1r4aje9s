package com.life.db;

import com.life.data.svdb.SVDB;
import com.life.entity.GroupMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupMemberDb extends SVDB<GroupMember> {

    private static final Logger LOGGER = LogManager.getLogger(GroupMemberDb.class);

    public static GroupMemberDb instance = new GroupMemberDb("GroupMember");

    private GroupMemberDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static String buildKey(int idGroup, int idUser) {
        return String.format("%d_%d", idGroup, idUser);
    }

    /**
     *
     * @param groupId
     * @param uid
     * @return value | null
     */
    public GroupMember get(int groupId, int uid) {
        return super.get(buildKey(groupId, uid));
    }

    public boolean put(GroupMember value) {
        String key = buildKey(value.idGroup, value.idUser);
        return super.put(key, value);
    }

    public Map<String, GroupMember> multiGet(List<String> ids) {
        return super.multiGet(ids);
    }

    public static List<String> buildKey(int idGroup, List<Integer> uids) {
        List<String> keys = new ArrayList<>();
        if (uids != null) {
            uids.forEach((uid) -> {
                keys.add(buildKey(idGroup, uid));
            });

        }
        return keys;

    }

    public Map<String, GroupMember> multiGet(int groupId, List<Integer> uids) {
        List<String> buildKey = buildKey(groupId, uids);
        return this.multiGet(buildKey);
    }

    public boolean delete(int groupId, int userId) {
        return super.delete(buildKey(groupId, userId));
    }

}
