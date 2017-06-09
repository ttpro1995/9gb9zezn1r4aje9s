package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.http.message.MsgCreateAppointment;
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
public class CreateAppointmentHandler extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(CreateAppointmentHandler.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MonitorStats.createThreadMonitorStats("CreateAppointmentHandler");
        try {
            int uid = checkFilterAuthentication(req);
            Map<String, Object> map = getRequestBody(req);

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", map);
            jSONObject.put("viewerId", uid);

            ApiMessage apiMessage;
            MsgCreateAppointment msg = MsgCreateAppointment.convertObject(map);

            if (msg == null) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                apiMessage = PlaceModel.instance.createAppointment(uid, msg);

            }
            jSONObject.put("result", apiMessage);
            LOGGER.info("api: " + jSONObject.toJSONString());

            renderJson(resp, apiMessage);
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }

    }

}
