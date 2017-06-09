package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.common.HRequest;
import com.life.model.UserModel;
import java.io.IOException;
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
public class UserGetHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(UserGetHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("UserGetHandler");
		try {
			int uid = checkFilterAuthentication(req);

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("viewerId", uid);

			ApiMessage apiMessage = ApiMessage.FAIL;
			int uidGet = HRequest.getInt(req, "uid", -1);
			jSONObject.put("uid", uidGet);

			if (uidGet > 0) {
				apiMessage = UserModel.instance.get(uidGet);
			}

			jSONObject.put("resultError", apiMessage.getError());
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}
	}

}
