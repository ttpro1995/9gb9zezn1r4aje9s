package com.life.backend;

import com.life.api.ApiMessage;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;

import com.life.db.GroupShareDb;
import com.life.db.MapGroupShare_GroupIdDb;
import com.life.db.UserOfGroupDb;
import com.life.entity.Group;
import com.life.entity.GroupShare;
import com.life.entityret.GroupShareRet;
import com.life.model.GroupModel;
import com.life.model.UserOfGroupModel;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupShareBE {

    private static final Logger LOGGER = LogManager.getLogger(GroupShareBE.class);

    public static GroupShareBE instance = new GroupShareBE();

    private GroupShareBE() {
    }

    private static String genCode6Char() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public ApiMessage getCode(int uid, int groupId) {
        if (uid <= 0 || groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }
        Group group = GroupModel.instance.get(groupId);
        if (group != null) {
            Value userOfGroup = UserOfGroupDb.instance.get(groupId);
            if (ECodeHelper.isSuccess(userOfGroup.error)) {
                List<Integer> lstUid = userOfGroup.value;
                if (lstUid.contains(uid)) {
                    GroupShare groupShare = GroupShareDb.instance.get(groupId);

                    String code;
                    if (groupShare != null) {
                        code = groupShare.code;
                        if (code != null && code.length() == 6) {
                            int mapGroup = MapGroupShare_GroupIdDb.instance.get(code);
                            if (mapGroup <= 0) {
                                MapGroupShare_GroupIdDb.instance.delete(code);
                            }

                            if (mapGroup == groupId) {
                                return new ApiMessage(new GroupShareRet(groupId, code));
                            }
                        }
                    }

                    int count = 0;
                    while (count < 10) {
                        code = genCode6Char();
                        if (groupShare != null) {
                            MapGroupShare_GroupIdDb.instance.delete(groupShare.code);

                            groupShare.code = code;
                            groupShare.createdTime = System.currentTimeMillis();
                        } else {
                            groupShare = new GroupShare(groupId, code);
                        }

                        int existCode = MapGroupShare_GroupIdDb.instance.get(code);
                        if (existCode < 0) {
                            boolean put = GroupShareDb.instance.put(groupId, groupShare);
                            boolean put1 = MapGroupShare_GroupIdDb.instance.put(code, groupId);
                            if (put && put1) {
                                return new ApiMessage(new GroupShareRet(groupId, code));
                            }
                            if (put) {
                                GroupShareDb.instance.delete(groupId);
                            }

                            if (put1) {
                                MapGroupShare_GroupIdDb.instance.delete(code);
                            }

                        }
                        count++;
                    }
                    return ApiMessage.FAIL;

                } else {
                    return ApiMessage.PERMISSION_DENY;
                }
            }
        }
        return ApiMessage.ITEM_NOT_FOUND;
    }

}
