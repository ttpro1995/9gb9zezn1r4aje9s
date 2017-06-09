package com.life.http.handler;

import com.jtn.monitorstats.MonitorStats;
import com.life.api.ApiMessage;
import com.life.model.GroupModel;
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
public class UpdateGroupHandler extends BaseHandler {

	private static final Logger LOGGER = LogManager.getLogger(UpdateGroupHandler.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MonitorStats.createThreadMonitorStats("UpdateGroupHandler");
		try {
			int uid = checkFilterAuthentication(req);
			Map<String, Object> map = getRequestBody(req);

			JSONObject jSONObject = new JSONObject();
            jSONObject.put("params", map);
			jSONObject.put("viewerId", uid);

			ApiMessage apiMessage;
			if (!map.containsKey("name")
					|| !map.containsKey("groupId")) {
				apiMessage = ApiMessage.INVALID_DATA;
			} else {
				try {
					int groupId = Integer.parseInt(map.get("groupId").toString());
					String firstName = map.get("name").toString();

					apiMessage = GroupModel.instance.updateGroup(uid, groupId, firstName);

				} catch (NumberFormatException ex) {
					LOGGER.error("api parse", ex);
					apiMessage = ApiMessage.INVALID_DATA;
				}
			}

			jSONObject.put("result", apiMessage);
			LOGGER.info("api: " + jSONObject.toJSONString());

			renderJson(resp, apiMessage);
		} finally {
			MonitorStats.closeThreadMonitorStats();
		}
	}

}
