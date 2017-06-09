package com.life.backend;

import com.life.common.ECode;
import com.life.db.CheckMissUserChatInGroupDb;
import com.life.db.GroupDb;
import com.life.db.GroupMemberDb;
import com.life.db.GroupOfUserDb;
import com.life.db.UserDb;
import com.life.db.UserOfGroupDb;
import com.life.entity.Group;
import com.life.entity.GroupMember;
import com.life.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupBE {

    private static final Logger LOGGER = LogManager.getLogger(GroupBE.class);

    public static GroupBE instance = new GroupBE();

    private GroupBE() {
    }

    /**
     *
     * @param groupId
     * @param group
     * @return SUCCESS,-FAIL,- INVALID_DATA
     */
    public int createGroup(int groupId, Group group) {
        if (group == null) {
            return -ECode.INVALID_DATA.getValue();
        }
        int owner = group.getIdUser_Owner();

        if (groupId <= 0 || owner <= 0) {
            return -ECode.INVALID_DATA.getValue();
        }
        boolean putGroup = GroupDb.instance.put(groupId, group);
        int putEntries = GroupOfUserDb.instance.putEntries(owner, groupId);
        int putEntries1 = UserOfGroupDb.instance.putEntries(groupId, owner);

        GroupMember groupMember = new GroupMember(true, true, true, owner, groupId);
        boolean put = GroupMemberDb.instance.put(groupMember);

        if (putGroup && putEntries > 0 && putEntries1 > 0 && put) {
            CheckMissUserChatInGroupDb.instance.put(groupId, true);
            return ECode.SUCCESS.getValue();
        } else {
            if (putGroup) {
                GroupDb.instance.delete(groupId);
            }
            if (putEntries > 0) {
                GroupOfUserDb.instance.remove(owner, groupId);
            }
            if (putEntries1 > 0) {
                UserOfGroupDb.instance.remove(groupId, owner);
            }
            if (put) {
                GroupMemberDb.instance.delete(groupId, owner);
            }
        }
        return -ECode.FAIL.getValue();
    }

    /**
     *
     * @param groupId
     * @param uid
     * @return SUCCESS | - FAIL
     */
    public int joinGroup(int groupId, int uid) {

        if (groupId <= 0 || uid <= 0) {
            return -ECode.FAIL.getValue();
        }

        int putGroupOfUser = GroupOfUserDb.instance.putEntries(uid, groupId);
        int putUserOfGroup = UserOfGroupDb.instance.putEntries(groupId, uid);

        GroupMember groupMember = new GroupMember(false, true, true, uid, groupId);
        boolean putGroupMember = GroupMemberDb.instance.put(groupMember);

        if (putGroupOfUser > 0 && putUserOfGroup > 0 && putGroupMember) {
            return ECode.SUCCESS.getValue();
        }

        if (putGroupOfUser > 0) {
            GroupOfUserDb.instance.remove(uid, groupId);
        }
        if (putUserOfGroup > 0) {
            UserOfGroupDb.instance.remove(groupId, uid);
        }
        if (putGroupMember) {
            GroupMemberDb.instance.delete(groupId, uid);
        }

        return -ECode.FAIL.getValue();

    }

    public boolean leaveGroup(int groupId, int uid) {
        if (groupId <= 0 || uid <= 0) {
            return false;
        }
        int removeGroupOfUser = GroupOfUserDb.instance.remove(uid, groupId);
        int removeUserOfGroup = UserOfGroupDb.instance.remove(groupId, uid);
        boolean deleteGroupMember = GroupMemberDb.instance.delete(groupId, uid);

        return true;

    }

}
