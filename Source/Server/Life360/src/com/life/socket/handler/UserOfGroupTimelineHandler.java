package com.life.socket.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.GroupModel;
import com.life.socket.command.BaseCommand;
import com.life.socket.entities.Packet;
import com.life.socket.message.MsgUserOfGroupTimeline;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserOfGroupTimelineHandler extends BaseCommand {

    private static final Logger LOGGER = LogManager.getLogger(UserOfGroupTimelineHandler.class);

    @Override
    public ApiMessage execute() {
        MonitorStats.createThreadMonitorStats("UserOfGroupTimelineHandler");
        try {
            Packet packet = getPacket();
            int uid = packet.getSourceId();

            Map<String, Object> data = packet.getData();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", data);
            jSONObject.put("viewId", uid);

            MsgUserOfGroupTimeline msg = MsgUserOfGroupTimeline.convertObject(data);

            ApiMessage apiMessage;
            if (msg == null) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                apiMessage = GroupModel.instance.getUserOfGroupTimeline(uid, msg, jSONObject);

            }

            jSONObject.put("resultError", apiMessage.errorCode);
            LOGGER.info(jSONObject);

            return apiMessage;
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }
    }

}
