package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.common.HRequest;
import com.life.model.UserModel;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class CheckExistsUserNameHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(CheckExistsUserNameHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("CheckExistsUserNameHandler");
		try {
			JSONObject logParam = new JSONObject();

			ApiMessage apiMessage = ApiMessage.INVALID_DATA;
			String userName = HRequest.getString(req, "userName", null);

			logParam.put("userName", userName);

			if (StringUtils.isNotBlank(userName)) {
				apiMessage = UserModel.instance.checkExistUsername(userName);

			}

			logParam.put("result", apiMessage);
			LOGGER.info("api: " + logParam.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}
	}

}
