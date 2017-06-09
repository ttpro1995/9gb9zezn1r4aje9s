package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.common.HRequest;
import com.life.model.GroupShareModel;
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
public class GetCodeGroupShareHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(GetCodeGroupShareHandler.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("GetCodeGroupShareHandler");
		try {
			int uid = checkFilterAuthentication(req);
			Integer groupId = HRequest.getInt(req, "groupId", -1);

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("viewerId", uid);
			jSONObject.put("gid", groupId);

			ApiMessage apiMessage;
			if (groupId > 0) {
				apiMessage = GroupShareModel.instance.getCode(uid, groupId);
			} else {
				apiMessage = ApiMessage.INVALID_DATA;
			}

			jSONObject.put("result", apiMessage);
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}
	}

}
