package com.life.socket.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.PlaceModel;
import com.life.socket.command.BaseCommand;
import com.life.socket.entities.Packet;
import com.life.socket.message.MsgPlaceCheckOut;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class CheckOutPlaceHandler extends BaseCommand {

    private static final Logger LOGGER = LogManager.getLogger(CheckOutPlaceHandler.class);

    @Override
    public ApiMessage execute() {
        MonitorStats.createThreadMonitorStats("CheckOutPlaceHandler");
        try {
            Packet packet = getPacket();
            int uid = packet.getSourceId();

            Map<String, Object> data = packet.getData();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", data);
            jSONObject.put("viewId", uid);

            MsgPlaceCheckOut msg = MsgPlaceCheckOut.convertObject(data);

            ApiMessage apiMessage;
            if (msg == null) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                apiMessage = PlaceModel.instance.checkOutPlace(uid, msg);

            }

            jSONObject.put("result", apiMessage);
            LOGGER.info(jSONObject);

            return apiMessage;
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }
    }

}
