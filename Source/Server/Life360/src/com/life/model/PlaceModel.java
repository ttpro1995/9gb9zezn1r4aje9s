package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.common.PlaceType;
import com.life.db.PlaceDb;
import com.life.db.GroupDb;
import com.life.db.GroupMemberDb;
import com.life.db.GroupOfUserDb;
import com.life.db.IdGenI32Db;
import com.life.db.UserOfGroupDb;
import com.life.entity.Group;
import com.life.entity.GroupMember;
import com.life.entity.base.LatLon;
import com.life.entityret.AppointmentRet;
import com.life.entityret.ListAppointmentRet;
import com.life.entityret.ListPlaceRet;
import com.life.entityret.PlaceRet;
import com.life.es.entities.Place;
import com.life.es.entities.PlaceSearchResult;
import com.life.http.message.MsgAppointmentOfGroup;
import com.life.http.message.MsgCreateAppointment;
import com.life.http.message.MsgUpdateAppointment;
import com.life.socket.message.MsgAppointmentCheckIn;
import com.life.socket.message.MsgAppointmentCheckOut;
import com.life.socket.message.MsgCreatePlace;
import com.life.socket.message.MsgPlaceCheckIn;
import com.life.socket.message.MsgPlaceCheckOut;
import com.life.socket.message.MsgPlaceOfGroup;
import com.life.socket.message.MsgRemovePlace;
import com.life.socket.message.MsgUpdatePlace;
import com.life.woker.NAppointmentCheckInWorker;
import com.life.woker.NAppointmentCheckOutWorker;
import com.life.woker.NPlaceCheckInWorker;
import com.life.woker.NPlaceCheckOutWorker;
import com.life.woker.NotificationManager;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceModel {

    private static final Logger LOGGER = LogManager.getLogger(PlaceModel.class);

    public static final PlaceModel instance = new PlaceModel();

    private PlaceModel() {
    }

    public ApiMessage createAppointment(int uid, MsgCreateAppointment msg) {
        if (msg == null) {
            return ApiMessage.INVALID_DATA;
        }
        long currTime = System.currentTimeMillis();
        if (msg.time < currTime) {
            return ApiMessage.INVALID_DATA;
        }
        return createPlace(uid, msg.groupId, msg.name, msg.lat, msg.lon, msg.time, msg.zone, PlaceType.APPOINTMENT);
    }

    public ApiMessage createPlace(int uid, MsgCreatePlace msg) {
        if (msg == null) {
            return ApiMessage.INVALID_DATA;
        }
        if (msg.type < PlaceType.PLACE_MIN || msg.type > PlaceType.PLACE_MAX) {
            return ApiMessage.INVALID_DATA;
        }
        return createPlace(uid, msg.groupId, msg.name, msg.lat, msg.lon, 0, msg.zone, msg.type);
    }

    private ApiMessage createPlace(int uid, int groupId, String name, double lat, double lon, long time, int zone, byte type) {
        if (uid <= 0 || groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        if (StringUtils.isBlank(name)) {
            return ApiMessage.INVALID_DATA;
        }

        if (zone < 50) {
            return ApiMessage.INVALID_DATA;
        }

        Integer id = IdGenI32Db.instance.nextIdAppointment();

        Place appointment = new Place(id, name, lat, lon, time, zone, type, uid, groupId);
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getGroup");
        Group group = GroupDb.instance.get(groupId);
        monitor.pop("getGroup");
        if (group != null) {
            monitor.push("existsUserOfGroup");
            int exists = UserOfGroupDb.instance.exists(groupId, uid);
            monitor.pop("existsUserOfGroup");
            if (ECodeHelper.isSuccess(exists)) {
                monitor.push("indexPlace");
                int createAppointment = PlaceDb.instance.indexPlace(appointment);
                monitor.pop("indexPlace");
                if (ECodeHelper.isSuccess(createAppointment)) {
                    return ApiMessage.SUCCESS;
                } else {
                    return ApiMessage.FAIL;
                }

            } else {
                return ApiMessage.PERMISSION_DENY;
            }
        }

        return ApiMessage.ITEM_NOT_FOUND;

    }

    public ApiMessage updateAppointment(int uid, MsgUpdateAppointment msg) {
        if (msg == null) {
            return ApiMessage.INVALID_DATA;
        }
        long currTime = System.currentTimeMillis();
        if (msg.time < currTime) {
            return ApiMessage.INVALID_DATA;
        }
        return updatePlace(uid, msg.appointmentId, msg.name, msg.lat, msg.lon, msg.time, msg.zone);
    }

    public ApiMessage updatePlace(int uid, MsgUpdatePlace msg) {
        if (msg == null) {
            return ApiMessage.INVALID_DATA;
        }

        return updatePlace(uid, msg.appointmentId, msg.name, msg.lat, msg.lon, 0, msg.zone);
    }

    /**
     *
     * @param uid
     * @param appointmentId
     * @param name
     * @param lat
     * @param lon
     * @param time dành riêng cho appointment, check time trước khi gọi vào đây
     * @param zone
     * @return
     */
    private ApiMessage updatePlace(int uid, int appointmentId, String name, double lat, double lon, long time, int zone) {
        if (uid <= 0 || appointmentId <= 0 || StringUtils.isBlank(name) || zone < 50) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getPlace");
        Place place = PlaceDb.instance.getPlace(appointmentId);
        monitor.pop("getPlace");
        if (place != null) {

            boolean isUpdate = false;
            if (place.idUserOwner == uid) {
                isUpdate = true;
            } else {
                monitor.push("containGroupOfUser");
                int contain = GroupOfUserDb.instance.contain(uid, place.idGroup);
                monitor.pop("containGroupOfUser");
                if (ECodeHelper.isSuccess(contain)) {
                    monitor.push("getGroupMember");
                    GroupMember groupMember = GroupMemberDb.instance.get(place.idGroup, uid);
                    monitor.pop("getGroupMember");
                    if (groupMember != null && groupMember.admin) {
                        isUpdate = true;
                    }
                }
            }

            if (isUpdate) {
                place.name = name;
                place.latlon = new LatLon(lat, lon);
                place.time = time;
                place.zone = zone;

                monitor.push("indexPlace");
                int indexPlace = PlaceDb.instance.indexPlace(place);
                monitor.pop("indexPlace");

                if (ECodeHelper.isSuccess(indexPlace)) {
                    return ApiMessage.SUCCESS;
                } else {
                    return ApiMessage.FAIL;
                }
            } else {
                return ApiMessage.PERMISSION_DENY;
            }

        }

        return ApiMessage.ITEM_NOT_FOUND;

    }

    public ApiMessage removePlace(int uid, MsgRemovePlace msg) {
        if (msg == null) {
            return ApiMessage.INVALID_DATA;
        }

        return removePlace(uid, msg.groupId, msg.placeId);
    }

    public ApiMessage removePlace(int uid, int groupId, int appointmentId) {

        if (uid <= 0 || groupId <= 0 || appointmentId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ApiMessage apiMessage = ApiMessage.UNKNOWN_EXCEPTION;
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

        monitor.push("getGroup");
        Group group = GroupDb.instance.get(groupId);
        monitor.pop("getGroup");
        if (group != null) {
            monitor.push("existsUserOfGroup");
            int exists = UserOfGroupDb.instance.exists(groupId, uid);
            monitor.pop("existsUserOfGroup");
            if (ECodeHelper.isSuccess(exists)) {

                monitor.push("getGroupMember");
                GroupMember groupMember = GroupMemberDb.instance.get(groupId, uid);
                monitor.pop("getGroupMember");
                if (groupMember != null) {
                    boolean allowRemove = false;

                    if (groupMember.admin) {
                        allowRemove = true;
                    } else {
                        monitor.push("getAppointment");
                        Place appointment = PlaceDb.instance.getPlace(appointmentId);
                        monitor.pop("getAppointment");
                        if (appointment != null) {
                            if (appointment.idUserOwner == uid) {
                                allowRemove = true;
                            } else {
                                apiMessage = ApiMessage.PERMISSION_DENY;
                            }
                        } else {
                            apiMessage = ApiMessage.ITEM_NOT_FOUND;
                        }

                    }

                    if (allowRemove) {
                        monitor.push("removeAppointment");
                        int removeAppointment = PlaceDb.instance.removePlace(appointmentId);
                        monitor.pop("removeAppointment");
                        if (ECodeHelper.isSuccess(removeAppointment)) {
                            apiMessage = ApiMessage.SUCCESS;
                        } else if (removeAppointment == -ECode.NOT_FOUND.getValue()) {
                            apiMessage = ApiMessage.ITEM_NOT_FOUND;
                        } else {
                            apiMessage = ApiMessage.FAIL;
                        }
                    }
                } else {
                    apiMessage = ApiMessage.PERMISSION_DENY;
                }
            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else {
            apiMessage = ApiMessage.ITEM_NOT_FOUND;
        }

        return apiMessage;
    }

    public ApiMessage appointmentOfGroup(int uid, MsgAppointmentOfGroup msg, JSONObject jSONObject) {

        if (uid <= 0 || msg == null || msg.groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        if (msg.from < 0) {
            msg.from = 0;
        }

        if (msg.size < 0 || msg.size > 20) {
            msg.size = 20;
        }
        long fromTime = System.currentTimeMillis();

        ApiMessage apiMessage;
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getGroup");
        Group group = GroupDb.instance.get(msg.groupId);
        monitor.pop("getGroup");
        if (group != null) {
            int exists = UserOfGroupDb.instance.exists(msg.groupId, uid);
            if (ECodeHelper.isSuccess(exists)) {
                monitor.push("appointmentOfGroup");
                PlaceSearchResult appointmentOfGroup = PlaceDb.instance.appointmentOfGroup(msg.groupId, fromTime, msg.from, msg.size);
                monitor.pop("appointmentOfGroup");
                if (ECodeHelper.isSuccess(appointmentOfGroup.error)) {
                    ListAppointmentRet data = new ListAppointmentRet();

                    data.total = appointmentOfGroup.total;
                    if (appointmentOfGroup.value != null && !appointmentOfGroup.value.isEmpty()) {
                        for (Place item : appointmentOfGroup.value) {
                            if (item != null) {
                                AppointmentRet apRet = new AppointmentRet();
                                apRet.id = item.id;
                                apRet.latlon = item.latlon;
                                apRet.name = item.name;
                                apRet.time = item.time;
                                apRet.zone = item.zone;
                                data.add(apRet);
                            }
                        }
                    }
                    jSONObject.put("size", appointmentOfGroup.value.size());
                    apiMessage = new ApiMessage(data);

                } else {
                    apiMessage = ApiMessage.FAIL;
                }
            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }

        } else {
            apiMessage = ApiMessage.ITEM_NOT_FOUND;
        }

        return apiMessage;
    }

    public ApiMessage placeOfGroup(int uid, MsgPlaceOfGroup msg, JSONObject jSONObject) {

        if (uid <= 0 || msg == null || msg.groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        if (msg.from < 0) {
            msg.from = 0;
        }

        if (msg.size < 0 || msg.size > 20) {
            msg.size = 20;
        }

        ApiMessage apiMessage;
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getGroup");
        Group group = GroupDb.instance.get(msg.groupId);
        monitor.pop("getGroup");
        if (group != null) {
            int exists = UserOfGroupDb.instance.exists(msg.groupId, uid);
            if (ECodeHelper.isSuccess(exists)) {
                monitor.push("appointmentOfGroup");
                PlaceSearchResult placeOfGroups = PlaceDb.instance.placeOfGroup(msg.groupId, msg.from, msg.size);
                monitor.pop("appointmentOfGroup");
                if (ECodeHelper.isSuccess(placeOfGroups.error)) {
                    ListPlaceRet data = new ListPlaceRet();

                    data.total = placeOfGroups.total;
                    if (placeOfGroups.value != null && !placeOfGroups.value.isEmpty()) {
                        for (Place item : placeOfGroups.value) {
                            if (item != null) {
                                PlaceRet apRet = new PlaceRet();
                                apRet.id = item.id;
                                apRet.latlon = item.latlon;
                                apRet.name = item.name;
                                apRet.zone = item.zone;
                                data.add(apRet);
                            }
                        }
                    }
                    jSONObject.put("size", placeOfGroups.value.size());
                    apiMessage = new ApiMessage(data);

                } else {
                    apiMessage = ApiMessage.FAIL;
                }
            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }

        } else {
            apiMessage = ApiMessage.ITEM_NOT_FOUND;
        }

        return apiMessage;
    }

    public ApiMessage checkInAppointment(int uid, MsgAppointmentCheckIn msg) {
        if (uid <= 0 || msg == null || msg.idAppointment <= 0 || msg.groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getAppointment");
        Place appointment = PlaceDb.instance.getPlace(msg.idAppointment);
        monitor.pop("getAppointment");

        monitor.push("containUserOfGroup");
        int contain = UserOfGroupDb.instance.contain(msg.groupId, uid);
        monitor.pop("containUserOfGroup");

        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        if (contain == ECode.SUCCESS.value && appointment != null) {
            if (appointment.type != PlaceType.APPOINTMENT) {
                apiMessage = ApiMessage.PERMISSION_DENY;
            } else if (appointment.idGroup == msg.groupId) {
                NotificationManager.pushNotify(new NAppointmentCheckInWorker(uid, msg.groupId, msg.idAppointment));
                apiMessage = ApiMessage.SUCCESS;

            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else if (contain == -ECode.NOT_CONTAIN.value) {
            apiMessage = ApiMessage.PERMISSION_DENY;
        }

        return apiMessage;
    }

    public ApiMessage checkOutAppointment(int uid, MsgAppointmentCheckOut msg) {
        if (uid <= 0 || msg == null || msg.groupId <= 0 || msg.idAppointment <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getAppointment");
        Place appointment = PlaceDb.instance.getPlace(msg.idAppointment);
        monitor.pop("getAppointment");

        monitor.push("containUserOfGroup");
        int contain = UserOfGroupDb.instance.contain(msg.groupId, uid);
        monitor.pop("containUserOfGroup");

        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        if (contain == ECode.SUCCESS.value && appointment != null) {
            if (appointment.type != PlaceType.APPOINTMENT) {
                apiMessage = ApiMessage.PERMISSION_DENY;
            } else if (appointment.idGroup == msg.groupId) {
                NotificationManager.pushNotify(new NAppointmentCheckOutWorker(uid, msg.groupId, msg.idAppointment));
                apiMessage = ApiMessage.SUCCESS;

            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else if (contain == -ECode.NOT_CONTAIN.value) {
            apiMessage = ApiMessage.PERMISSION_DENY;
        }

        return apiMessage;

    }

    public ApiMessage checkInPlace(int uid, MsgPlaceCheckIn msg) {
        if (uid <= 0 || msg == null || msg.idPlace <= 0 || msg.groupId <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getAppointment");
        Place appointment = PlaceDb.instance.getPlace(msg.idPlace);
        monitor.pop("getAppointment");

        monitor.push("containUserOfGroup");
        int contain = UserOfGroupDb.instance.contain(msg.groupId, uid);
        monitor.pop("containUserOfGroup");

        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        if (contain == ECode.SUCCESS.value && appointment != null) {
            if (appointment.type < PlaceType.PLACE_MIN || appointment.type > PlaceType.PLACE_MAX) {
                apiMessage = ApiMessage.PERMISSION_DENY;
            } else if (appointment.idGroup == msg.groupId) {
                NotificationManager.pushNotify(new NPlaceCheckInWorker(uid, msg.groupId, msg.idPlace));
                apiMessage = ApiMessage.SUCCESS;

            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else if (contain == -ECode.NOT_CONTAIN.value) {
            apiMessage = ApiMessage.PERMISSION_DENY;
        }

        return apiMessage;
    }

    public ApiMessage checkOutPlace(int uid, MsgPlaceCheckOut msg) {
        if (uid <= 0 || msg == null || msg.groupId <= 0 || msg.idPlace <= 0) {
            return ApiMessage.INVALID_DATA;
        }

        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getAppointment");
        Place appointment = PlaceDb.instance.getPlace(msg.idPlace);
        monitor.pop("getAppointment");

        monitor.push("containUserOfGroup");
        int contain = UserOfGroupDb.instance.contain(msg.groupId, uid);
        monitor.pop("containUserOfGroup");

        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        if (contain == ECode.SUCCESS.value && appointment != null) {
            if (appointment.type < PlaceType.PLACE_MIN || appointment.type > PlaceType.PLACE_MAX) {
                apiMessage = ApiMessage.PERMISSION_DENY;
            } else if (appointment.idGroup == msg.groupId) {
                NotificationManager.pushNotify(new NPlaceCheckOutWorker(uid, msg.groupId, msg.idPlace));
                apiMessage = ApiMessage.SUCCESS;
            } else {
                apiMessage = ApiMessage.PERMISSION_DENY;
            }
        } else if (contain == -ECode.NOT_CONTAIN.value) {
            apiMessage = ApiMessage.PERMISSION_DENY;
        }

        return apiMessage;

    }

    public static void main(String[] args) {
        int uid = 2;
        int groupId = 1;
        String name = "name ";
        double lat = 10;
        double lon = 106;
        long time = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10);
        int zone = 100;

        Random random = new Random();

//        for (int i = 10; i < 100; i++) {
//
//            ApiMessage createAppointment = instance.createAppointment(
//                    uid,
//                    groupId,
//                    name + i,
//                    lat + random.nextDouble(),
//                    lon + random.nextDouble(),
//                    time + TimeUnit.DAYS.toMillis(random.nextInt(100)),
//                    zone + random.nextInt(1000));
//
//            System.err.println(String.format("create %d: %s", i, createAppointment));
//        }
        System.exit(0);
    }
}
