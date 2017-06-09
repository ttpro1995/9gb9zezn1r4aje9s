package com.life.socket.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.PlaceModel;
import com.life.socket.command.BaseCommand;
import com.life.socket.entities.Packet;
import com.life.socket.message.MsgPlaceOfGroup;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceOfGroupHandler extends BaseCommand {

    private static final Logger LOGGER = LogManager.getLogger(PlaceOfGroupHandler.class);

    @Override
    public ApiMessage execute() {
        MonitorStats.createThreadMonitorStats("PlaceOfGroupHandler");
        try {
            Packet packet = getPacket();
            int uid = packet.getSourceId();

            Map<String, Object> data = packet.getData();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", data);
            jSONObject.put("viewId", uid);

            MsgPlaceOfGroup msg = MsgPlaceOfGroup.convertObject(data);

            ApiMessage apiMessage;
            if (msg == null) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                apiMessage = PlaceModel.instance.placeOfGroup(uid, msg, jSONObject);

            }

            jSONObject.put("result", apiMessage);
            LOGGER.info(jSONObject);

            return apiMessage;
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }
    }

}
