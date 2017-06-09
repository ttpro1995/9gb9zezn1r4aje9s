package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.UserModel;
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
public class UserUpdateHander extends BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(UserUpdateHander.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MonitorStats.createThreadMonitorStats("UserUpdateHander");
        try {
            int uid = checkFilterAuthentication(req);

            Map<String, Object> map = getRequestBody(req);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", map);
            jSONObject.put("viewerId", uid);

            ApiMessage apiMessage;
            if (!map.containsKey("firstName")
                    || !map.containsKey("lastName")) {
                apiMessage = ApiMessage.INVALID_DATA;
            } else {
                String firstName = map.get("firstName").toString();
                String lastName = map.get("lastName").toString();

                apiMessage = UserModel.instance.updateUser(uid, firstName, lastName);
            }

            jSONObject.put("result", apiMessage);
            LOGGER.info("api: " + jSONObject.toJSONString());

            renderJson(resp, apiMessage);
        } finally {
            MonitorStats.closeThreadMonitorStats();
        }
    }

}
