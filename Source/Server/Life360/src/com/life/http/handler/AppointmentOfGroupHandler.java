package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.http.message.MsgAppointmentOfGroup;
import com.life.model.PlaceModel;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class AppointmentOfGroupHandler extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(AppointmentOfGroupHandler.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MonitorStats.createThreadMonitorStats("AppointmentOfGroupHandler");
        try {
            int uid = checkFilterAuthentication(req);
            Map<String, Object> map = getRequestBody(req);

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", map);
            jSONObject.put("viewerId", uid);

            ApiMessage apiMessage;

            MsgAppointmentOfGroup msg = MsgAppointmentOfGroup.convertObject(map);
            if (msg == null) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                apiMessage = PlaceModel.instance.appointmentOfGroup(uid, msg, jSONObject);
            }

            jSONObject.put("resultError", apiMessage.getError());
            LOGGER.info("api: " + jSONObject.toJSONString());

            renderJson(resp, apiMessage);
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }
    }

}
