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
public class LoginHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(LoginHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("LoginHandler");
		try {
			String userName = HRequest.getString(req, "userName", "");
			String password = HRequest.getString(req, "password", "");

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("userName", userName);

			if ("".equals(userName) || "".equals(password)) {
				renderJson(resp, ApiMessage.INVALID_DATA);
				return;
			}
			ApiMessage apiMessage = UserModel.instance.login(userName, password);

			jSONObject.put("resultError ", apiMessage.getError());
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}

	}
}
