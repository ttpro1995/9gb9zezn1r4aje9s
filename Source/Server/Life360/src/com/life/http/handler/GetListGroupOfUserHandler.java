package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.GroupModel;
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
public class GetListGroupOfUserHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(GetListGroupOfUserHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("GetListGroupOfUserHandler");
		try {

			int uid = checkFilterAuthentication(req);

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("viewerId", uid);

			ApiMessage listGroup = GroupModel.instance.getGroupOfUser(uid, jSONObject);

			jSONObject.put("resultError", listGroup.getError());
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, listGroup);

		} finally {
			MonitorStats.closeThreadMonitorStats();
		}

	}

}
