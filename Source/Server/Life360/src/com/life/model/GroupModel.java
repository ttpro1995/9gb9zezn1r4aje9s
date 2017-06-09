package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.db.IdGenI32Db;
import com.life.api.ApiMessage;
import com.life.backend.GroupBE;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.BateryDb;

import com.life.db.GroupDb;
import com.life.db.GroupMemberDb;
import com.life.db.GroupOfUserDb;
import com.life.db.LocationDb;
import com.life.db.MapGroupShare_GroupIdDb;
import com.life.db.UserDb;
import com.life.db.UserOfGroupDb;
import com.life.entity.Batery;
import com.life.entity.Group;
import com.life.entity.GroupMember;
import com.life.entity.Location;
import com.life.entity.User;
import com.life.entityret.ListGroupOfUserRet;
import com.life.entityret.ListUserOfGroupRet;
import com.life.entityret.GroupRet;
import com.life.entityret.ListUserOfGroupTimelineRet;
import com.life.entityret.UserOfGroupRet;
import com.life.entityret.UserOfGroupTimelineRet;
import com.life.socket.message.MsgUserOfGroupTimeline;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupModel {

    private static final Logger LOGGER = LogManager.getLogger(GroupModel.class);

    public static final GroupModel instance = new GroupModel();

    private GroupModel() {
    }

    public Group get(int groupId) {
        return GroupDb.instance.get(groupId);
    }

    public ApiMessage createGroup(int owner, String name) {
        if (owner <= 0) {
            return ApiMessage.INVALID_DATA;
        }
        if (StringUtils.isBlank(name)) {
            return ApiMessage.INVALID_DATA;
        }
        name = name.trim();
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        Integer groupId = IdGenI32Db.instance.nextIdGroup();
        if (groupId > 0) {

            Group group = new Group(groupId, name, owner);
            monitor.push("createGroup");
            int createGroup = GroupBE.instance.createGroup(groupId, group);
            monitor.pop("createGroup");
            if (ECodeHelper.isSuccess(createGroup)) {
                return ApiMessage.SUCCESS;
            } else if (createGroup == -ECode.INVALID_DATA.getValue()) {
                return ApiMessage.INVALID_DATA;
            }

        }

        return ApiMessage.FAIL;
    }

    /**
     *
     * @param owner
     * @param groupId
     * @param name
     * @return -INVALID_DATA | SUCCESS | -PERMISSION_DENY | -FAIL
     */
    public ApiMessage updateGroup(int owner, int groupId, String name) {

        if (owner <= 0 || groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }
        if (StringUtils.isBlank(name)) {
            return ApiMessage.INVALID_DATA;
        }
        name = name.trim();
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getGroup");
        Group get = GroupDb.instance.get(groupId);
        monitor.pop("getGroup");
        if (get != null) {
            if (get.getIdUser_Owner() == owner) {
                get.setName(name);
                get.setUpdatedTime(System.currentTimeMillis());
                monitor.push("putGroup");
                boolean put = GroupDb.instance.put(groupId, get);
                monitor.pop("putGroup");
                if (put) {
                    return ApiMessage.SUCCESS;
                }
            } else {
                return ApiMessage.PERMISSION_DENY;
            }
        }

        return ApiMessage.FAIL;
    }

    public ApiMessage getGroupOfUser(int uid, JSONObject jSONObject) {
        if (uid <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getGroupOfUserDb");
        Value get = GroupOfUserDb.instance.get(uid);
        monitor.pop("getGroupOfUserDb");
        if (ECodeHelper.isSuccess(get.error)) {
            List<Integer> lstGroup = get.value;

            monitor.push("multiGetGroup");
            Map<Integer, Group> multiGet = GroupDb.instance.multiGet(lstGroup);
            monitor.pop("multiGetGroup");
            ListGroupOfUserRet data = new ListGroupOfUserRet();
            multiGet.entrySet().forEach((item) -> {

                Group value = item.getValue();
                if (value != null) {
                    GroupRet gr = new GroupRet(value);
                    data.add(gr);
                }

            });
            jSONObject.put("size", data.groups.size());

            return new ApiMessage(data);
        }

        return ApiMessage.ITEM_NOT_FOUND;
    }

    public ApiMessage getUserOfGroup(int uid, int idGroup, JSONObject jSONObject) {
        if (idGroup <= 0) {
            return ApiMessage.INVALID_DATA;
        }
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

        monitor.push("getUserOfGroup");
        Value userOfGroup = UserOfGroupDb.instance.get(idGroup);
        monitor.pop("getUserOfGroup");
        if (ECodeHelper.isSuccess(userOfGroup.error)) {
            List<Integer> lstUid = userOfGroup.value;

            if (lstUid.contains(uid)) {

                List<String> lstKeyGroupMember = GroupMemberDb.buildKey(idGroup, lstUid);

                monitor.push("multiGetUser");
                Map<Integer, User> users = UserDb.instance.multiGet(lstUid);
                monitor.pop("multiGetUser");

                monitor.push("multiGetGroupMember");
                Map<String, GroupMember> groupMembers = GroupMemberDb.instance.multiGet(lstKeyGroupMember);
                monitor.pop("multiGetGroupMember");
                ListUserOfGroupRet data = new ListUserOfGroupRet();
                for (int i = 0; i < lstUid.size(); i++) {
                    int uidInGroup = lstUid.get(i);
                    String memberKey = lstKeyGroupMember.get(i);
                    if (users.containsKey(uidInGroup) && groupMembers.containsKey(memberKey)) {
                        User user = users.get(uidInGroup);
                        GroupMember groupMember = groupMembers.get(memberKey);
                        if (user != null && groupMember != null) {
                            UserOfGroupRet userOfGroupRet = new UserOfGroupRet(user, groupMember);
                            data.add(userOfGroupRet);
                        }
                    }
                }
                jSONObject.put("size", data.userOfGroups.size());
                return new ApiMessage(data);
            } else {
                return ApiMessage.PERMISSION_DENY;
            }
        }

        return ApiMessage.ITEM_NOT_FOUND;

    }

    public ApiMessage getUserOfGroupTimeline(int uid, MsgUserOfGroupTimeline msg, JSONObject jSONObject) {
        if (uid <= 0 || msg == null || msg.groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

        monitor.push("getUserOfGroup");
        Value userOfGroup = UserOfGroupDb.instance.get(msg.groupId);
        monitor.pop("getUserOfGroup");

        if (ECodeHelper.isSuccess(userOfGroup.error)) {
            List<Integer> lstUid = userOfGroup.value;

            if (lstUid.contains(uid)) {

                monitor.push("multiGetUser");
                Map<Integer, User> users = UserDb.instance.multiGet(lstUid);
                monitor.pop("multiGetUser");

                monitor.push("multiGetBatery");
                Map<Integer, Batery> baterys = BateryDb.instance.multiGet(lstUid);
                monitor.pop("multiGetBatery");

                monitor.push("multiGetLocation");
                Map<Integer, Location> locations = LocationDb.instance.multiGet(lstUid);
                monitor.pop("multiGetLocation");

                ListUserOfGroupTimelineRet data = new ListUserOfGroupTimelineRet();
                for (int i = 0; i < lstUid.size(); i++) {
                    int uidInGroup = lstUid.get(i);

                    if (users.containsKey(uidInGroup)) {
                        User user = users.get(uidInGroup);

                        if (user != null) {
                            UserOfGroupTimelineRet item = new UserOfGroupTimelineRet();
                            item.id = user.id;
                            item.firstName = user.firstName;
                            item.lastName = user.lastName;
                            item.avatar = user.avatar;

                            Batery batery = baterys.get(uidInGroup);
                            if (batery != null) {
                                item.batery = batery.batery;
                            }

                            Location location = locations.get(uidInGroup);
                            if (location != null) {
                                // item.nameLoc = location.name;
                                item.location = location.latLon;
                            }
                            data.add(item);
                        }
                    }
                }
                jSONObject.put("size", data.users.size());
                return new ApiMessage(data);
            } else {
                return ApiMessage.PERMISSION_DENY;
            }
        }

        return ApiMessage.ITEM_NOT_FOUND;

    }

    public ApiMessage joinGroup(int uid, String code) {

        if (uid <= 0 || StringUtils.isBlank(code) || code.length() != 6) {
            return ApiMessage.INVALID_DATA;
        }
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getUser");
        User user = UserDb.instance.get(uid);
        monitor.pop("getUser");

        if (user != null) {
            monitor.push("getMapGroupShare_GroupId");
            int getGroupId = MapGroupShare_GroupIdDb.instance.get(code);
            monitor.pop("getMapGroupShare_GroupId");
            if (getGroupId < 0) {
                return ApiMessage.NOT_EXISTS;
            }
            monitor.push("containUserOfGroup");
            int contain = UserOfGroupDb.instance.contain(getGroupId, uid);
            monitor.pop("containUserOfGroup");
            if (contain == ECode.SUCCESS.getValue()) {
                return ApiMessage.ALREADY_EXISTS;
            } else if (contain == -ECode.NOT_CONTAIN.getValue()) {
                monitor.push("joinGroup");
                int joinGroup = GroupBE.instance.joinGroup(getGroupId, uid);
                monitor.pop("joinGroup");
                if (ECodeHelper.isSuccess(joinGroup)) {
                    return ApiMessage.SUCCESS;
                }
            } else {
                return ApiMessage.ITEM_NOT_FOUND;
            }
            return ApiMessage.FAIL;
        }

        return ApiMessage.ITEM_NOT_FOUND;

    }

    public ApiMessage leaveGroup(int uid, int groupId) {
        if (uid <= 0 || groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("containUserOfGroup");
        int exists = UserOfGroupDb.instance.contain(groupId, uid);
        monitor.pop("containUserOfGroup");
        if (ECodeHelper.isSuccess(exists)) {
            monitor.push("leaveGroup");
            boolean leaveGroup = GroupBE.instance.leaveGroup(groupId, uid);
            monitor.pop("leaveGroup");
            if (leaveGroup) {
                apiMessage = ApiMessage.SUCCESS;
            } else {
                apiMessage = ApiMessage.FAIL;
            }
        } else if (exists == -ECode.NOT_CONTAIN.getValue()) {
            apiMessage = ApiMessage.FAIL;
        }

        return apiMessage;
    }

    public ApiMessage kickUser(int uid, int uid_Des, int groupId) {
        if (uid <= 0 || groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ApiMessage apiMessage;

        String keyUid = GroupMemberDb.buildKey(groupId, uid);
        String keyUid_Des = GroupMemberDb.buildKey(groupId, uid_Des);

        List<String> ids = Arrays.asList(keyUid, keyUid_Des);
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("multiGetGroupMember");
        Map<String, GroupMember> groupMembers = GroupMemberDb.instance.multiGet(ids);
        monitor.pop("multiGetGroupMember");

        GroupMember mbUid = groupMembers.get(keyUid);
        GroupMember mbUid_Des = groupMembers.get(keyUid_Des);

        if (mbUid != null) {
            if (mbUid.admin) {
                if (mbUid_Des != null) {
                    monitor.push("leaveGroup");
                    boolean leaveGroup = GroupBE.instance.leaveGroup(groupId, uid_Des);
                    monitor.pop("leaveGroup");
                    if (leaveGroup) {
                        apiMessage = ApiMessage.SUCCESS;
                    } else {
                        apiMessage = ApiMessage.FAIL;
                    }
                } else {
                    apiMessage = ApiMessage.ITEM_NOT_FOUND;
                }
            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else {
            apiMessage = ApiMessage.PERMISSION_DENY;
        }

        return apiMessage;
    }

}
