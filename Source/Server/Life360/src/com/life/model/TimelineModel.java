package com.life.model;

import com.jtn.monitorstats.MonitorStats;
import com.jtn.monitorstats.ThreadMonitor;
import com.life.api.ApiMessage;
import com.life.backend.TimelineBE;
import com.life.common.ECode;
import com.life.common.ECodeHelper;
import com.life.data.k32list32.Value;
import com.life.db.GroupDb;
import com.life.db.PlaceDb;
import com.life.db.IdGenI32Db;
import com.life.db.TimelineDb;
import com.life.db.TimelineOfUserDb;
import com.life.db.UserOfGroupDb;
import com.life.entity.Group;
import com.life.entity.Timeline;
import com.life.entityret.ListTimelineRet;
import com.life.entityret.TimelineRet;
import com.life.es.entities.Place;
import com.life.socket.message.MsgAppointmentCheckIn;
import com.life.socket.message.MsgAppointmentCheckOut;
import com.life.socket.message.MsgPushTimeline;
import com.life.socket.message.MsgTimelineOfUser;
import com.life.woker.NAppointmentCheckInWorker;
import com.life.woker.NAppointmentCheckOutWorker;
import com.life.woker.NotificationManager;
import java.util.List;
import java.util.Map;
import javax.management.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineModel {

    private static final Logger LOGGER = LogManager.getLogger(TimelineModel.class);

    public static final TimelineModel instance = new TimelineModel();

    private TimelineModel() {
    }

    public ApiMessage pushTimeline(int uid, MsgPushTimeline msg) {
        if (uid <= 0 || msg == null) {
            return ApiMessage.INVALID_DATA;
        }

        if (msg.fromTime > msg.toTime) {
            return ApiMessage.INVALID_DATA;
        }

        Integer id = IdGenI32Db.instance.nextIdTimeline();

        Timeline timeline = new Timeline(id, msg.content, msg.lat, msg.lon, msg.fromTime, msg.toTime, msg.zone, (byte) 0, uid);
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();

        monitor.push("addTimeline");
        int addTimeline = TimelineBE.instance.addTimeline(timeline);
        monitor.pop("addTimeline");

        if (ECodeHelper.isSuccess(addTimeline)) {
            return ApiMessage.SUCCESS;
        } else if (addTimeline == -ECode.INVALID_DATA.getValue()) {
            return ApiMessage.INVALID_DATA;
        }

        return ApiMessage.FAIL;
    }

    public ApiMessage timelineOfUser(int uid, MsgTimelineOfUser msg) {
        if (uid <= 0 || msg == null) {
            return ApiMessage.INVALID_DATA;
        }

        if (msg.from < 0) {
            msg.from = 0;
        }

        if (msg.size < 0 || msg.size > 20) {
            msg.size = 20;
        }
        ThreadMonitor monitor = MonitorStats.getThreadMonitorStats();
        monitor.push("getLastTimelineOfUser");
        Value timelineOfUsers = TimelineOfUserDb.instance.getLast(uid, msg.from, msg.size);
        monitor.pop("getLastTimelineOfUser");
        ApiMessage apiMessage = ApiMessage.ITEM_NOT_FOUND;
        if (ECodeHelper.isSuccess(timelineOfUsers.getError())) {
            List<Integer> IdTimelines = timelineOfUsers.value;
            monitor.push("multiGetTimeline");
            Map<Integer, Timeline> oTimelines = TimelineDb.instance.multiGet(IdTimelines);
            monitor.pop("multiGetTimeline");
            ListTimelineRet data = new ListTimelineRet();
            data.total = timelineOfUsers.total;

            for (int idTl : IdTimelines) {
                if (oTimelines.containsKey(idTl)) {
                    Timeline tl = oTimelines.get(idTl);
                    if (tl != null) {
                        TimelineRet tlret = new TimelineRet();
                        tlret.id = tl.id;
                        tlret.content = tl.content;
                        tlret.latLon = tl.latLon;
                        tlret.fromTime = tl.fromTime;
                        tlret.toTime = tl.toTime;
                        tlret.zone = tl.zone;
                        tlret.type = tl.type;

                        data.add(tlret);
                    }
                }
            }
            apiMessage = new ApiMessage(data);
        }

        return apiMessage;

    }

}
