package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.common.HRequest;
import com.life.model.FriendModel;
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
public class GetListFriendRequestingHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(GetListFriendRequestingHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("GetListFriendRequestingHandler");
		try {
			int uid = checkFilterAuthentication(req);
			Integer from = HRequest.getInt(req, "from", -1);
			Integer size = HRequest.getInt(req, "size", -1);

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("viewerId", uid);

			ApiMessage apiMessage;

			apiMessage = FriendModel.instance.getlistRequesting(uid, from, size);

			jSONObject.put("resultError", apiMessage.getError());
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}
	}

}
